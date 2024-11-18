package com.android.platform.di.factory
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.atomic.AtomicBoolean

class SingleLiveEvent<T> : MutableLiveData<T>() {

    private val pending = AtomicBoolean(false)
    private val eventQueue = ConcurrentLinkedQueue<T>()

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner, Observer { t ->
            if (pending.compareAndSet(true, false)) {
                observer.onChanged(t)
                processNext()
            }
        })
    }

    override fun setValue(value: T?) {
        if (value != null) {
            eventQueue.add(value)
            processNext()
        }
    }
    override fun postValue(value: T?) {
        if (value != null) {
            eventQueue.add(value)
            processNext()
        }
    }
    private fun processNext() {
        if (pending.compareAndSet(false, true)) {
            val nextEvent = eventQueue.poll()
            if (nextEvent != null) {
                super.setValue(nextEvent)
            } else {
                pending.set(false)
            }
        }
    }

    fun call() {
        value = null
    }
}
