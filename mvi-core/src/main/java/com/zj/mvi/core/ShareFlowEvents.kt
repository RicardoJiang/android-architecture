package com.zj.mvi.core

import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.SharedFlow

class ShareFlowEvents<T>(override val replayCache: List<T>) :SharedFlow<T> {
    @InternalCoroutinesApi
    override suspend fun collect(collector: FlowCollector<T>) {
        TODO("Not yet implemented")
    }
}

