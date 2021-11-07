package com.zj.architecture.network

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class CoroutineScopeHelper<T>(private val coroutineScope: CoroutineScope) {
    fun rxLaunch(init: LaunchBuilder<T>.() -> Unit): Job {
        val result = LaunchBuilder<T>().apply(init)
        val handler = NetworkExceptionHandler {
            result.onError?.invoke(it)
        }
        return coroutineScope.launch(handler) {
            val res: T = result.onRequest()
            result.onSuccess?.invoke(res)
        }
    }
}

class LaunchBuilder<T> {
    lateinit var onRequest: (suspend () -> T)
    var onSuccess: ((data: T) -> Unit)? = null
    var onError: ((Throwable) -> Unit)? = null
}

fun <T> CoroutineScope.rxLaunch(init: LaunchBuilder<T>.() -> Unit) {
    val scopeHelper = CoroutineScopeHelper<T>(this)
    scopeHelper.rxLaunch(init)
}