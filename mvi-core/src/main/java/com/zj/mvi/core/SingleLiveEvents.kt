package com.zj.mvi.core

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

/**
 * SingleLiveEvents
 * 负责处理多维度一次性Event
 * 比如我们在请求开始时发出ShowLoading，网络请求成功后发出DismissLoading与Toast事件
 * 如果我们在请求开始后回到桌面，成功后再回到App,这样有一个事件就会被覆盖，因此将所有事件通过List存储
 */
@Deprecated("Use LiveEvents instead")
class SingleLiveEvents<T> : MutableLiveData<List<T>>() {
    private val pending = AtomicBoolean(false)
    private val eventList = mutableListOf<List<T>>()

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in List<T>>) {
        super.observe(owner) { t ->
            if (pending.compareAndSet(true, false)) {
                eventList.clear()
                observer.onChanged(t)
            }
        }
    }

    @MainThread
    override fun setValue(t: List<T>?) {
        pending.set(true)
        t?.let {
            eventList.add(it)
        }
        val list = eventList.flatten()
        super.setValue(list)
    }
}