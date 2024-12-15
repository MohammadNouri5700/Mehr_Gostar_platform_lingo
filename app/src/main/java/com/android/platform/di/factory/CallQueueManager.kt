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


    private var taskId = 0
    private var jobMap = mutableMapOf<Int, Job>()

    private val ioTaskQueue = ConcurrentLinkedQueue<suspend () -> Unit>()
    private val mainTaskQueue = ConcurrentLinkedQueue<suspend () -> Unit>()
    private val applicationTaskQueue = ConcurrentLinkedQueue<suspend () -> Unit>()


    private var isRunningIo = false
    private var isRunningMain = false
    private var isRunningApplication = false

    private var ioTaskQueueCounter = 0
    private var mainTaskQueueCounter = 0
    private var applicationTaskCounter = 0


    fun enqueueIoTask(task: suspend () -> Unit): Int {
        ioTaskQueue.add(task)
        ioTaskQueueCounter++

        val job = ioScope.launch {
            while (ioTaskQueue.isNotEmpty()) {
                val nextTask = ioTaskQueue.poll()
                nextTask?.invoke()
            }
        }
        taskId++
        jobMap[taskId] = job

        processIoQueue()

        Log.e("Thread","enqueueIoTask $ioTaskQueueCounter sizeIs ${ioTaskQueue.size}")
        return taskId
    }


    fun enqueueMainTask(task: suspend () -> Unit) : Int {
        mainTaskQueue.add(task)
        mainTaskQueueCounter++

        val job = mainScope.launch {
            while (mainTaskQueue.isNotEmpty()) {
                val nextTask = mainTaskQueue.poll()
                nextTask?.invoke()
            }
        }

        taskId++
        jobMap[taskId] = job

        processMainQueue()
        Log.e("Thread","enqueueMainTask $mainTaskQueueCounter sizeIs ${mainTaskQueue.size}")
        return taskId
    }

    fun enqueueApplicationTask(task: suspend () -> Unit): Int {
        applicationTaskQueue.add(task)
        applicationTaskCounter++

        val job = applicationScope.launch {
            while (applicationTaskQueue.isNotEmpty()) {
                val nextTask = applicationTaskQueue.poll()
                nextTask?.invoke()
            }
        }

        taskId++
        jobMap[taskId] = job

        processApplicationQueue()
        Log.e("Thread","enqueueApplicationTask $applicationTaskCounter sizeIs ${applicationTaskQueue.size}")
        return taskId
    }

    fun cancelTask(taskId: Int) {
        jobMap[taskId]?.cancel()
        jobMap.remove(taskId)
        Log.e("Thread", "Task $taskId cancelled")
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

