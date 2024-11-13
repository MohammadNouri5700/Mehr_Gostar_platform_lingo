package com.android.platform.di.factory

import android.util.Log
import com.android.platform.di.module.CoroutineModule
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import java.util.concurrent.ConcurrentLinkedQueue
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CallQueueManager  @Inject constructor(
    @CoroutineModule.IoScope private val ioScope: CoroutineScope,
    @CoroutineModule.ApplicationScope private val applicationScope: CoroutineScope,
    @CoroutineModule.MainScope private val mainScope: CoroutineScope

){

    private val ioTaskQueue = ConcurrentLinkedQueue<suspend () -> Unit>()
    private val mainTaskQueue = ConcurrentLinkedQueue<suspend () -> Unit>()
    private val applicationTaskQueue = ConcurrentLinkedQueue<suspend () -> Unit>()
    private var isRunningIo = false
    private var isRunningMain = false
    private var isRunningApplication = false

    private var IioTaskQueue = 0
    fun enqueueIoTask(task: suspend () -> Unit) {
        ioTaskQueue.add(task)
        IioTaskQueue++

        processIoQueue()
        Log.e("Thread","enqueueIoTask $IioTaskQueue sizeIs ${ioTaskQueue.size}")
    }

    private var ImainTaskQueue = 0
    fun enqueueMainTask(task: suspend () -> Unit) {
        mainTaskQueue.add(task)
        ImainTaskQueue++
        processMainQueue()
        Log.e("Thread","enqueueMainTask $ImainTaskQueue sizeIs ${mainTaskQueue.size}")
    }
    var IenqueueApplicationTask = 0
    fun enqueueApplicationTask(task: suspend () -> Unit) {
        applicationTaskQueue.add(task)
        IenqueueApplicationTask++
        processApplicationQueue()
        Log.e("Thread","enqueueApplicationTask $IenqueueApplicationTask sizeIs ${applicationTaskQueue.size}")
    }

    private fun processIoQueue() {
        if (isRunningIo || ioTaskQueue.isEmpty()) return

        isRunningIo = true
        ioScope.launch {
            while (ioTaskQueue.isNotEmpty()) {
                val nextTask = ioTaskQueue.poll()
                nextTask?.invoke()
            }
            isRunningIo = false
        }
    }

    private fun processMainQueue() {
        if (isRunningMain || mainTaskQueue.isEmpty()) return

        isRunningMain = true
        mainScope.launch {
            while (mainTaskQueue.isNotEmpty()) {
                val nextTask = mainTaskQueue.poll()
                nextTask?.invoke()
            }
            isRunningMain = false
        }
    }

    private fun processApplicationQueue() {
        if (isRunningApplication || applicationTaskQueue.isEmpty()) return

        isRunningApplication = true
        applicationScope.launch {
            while (applicationTaskQueue.isNotEmpty()) {
                val nextTask = applicationTaskQueue.poll()
                nextTask?.invoke()
            }
            isRunningApplication = false
        }
    }
}

