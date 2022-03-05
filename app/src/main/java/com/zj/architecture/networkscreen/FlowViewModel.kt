package com.zj.architecture.networkscreen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zj.architecture.network.commonCatch
import com.zj.architecture.setEvent
import com.zj.architecture.setState
import com.zj.mvi.core.SingleLiveEvents
import com.zj.architecture.utils.asLiveData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class FlowViewModel : ViewModel() {
    private val _viewStates = MutableLiveData(NetworkViewState())
    val viewStates = _viewStates.asLiveData()
    private val _viewEvents: com.zj.mvi.core.SingleLiveEvents<NetworkViewEvent> =
        com.zj.mvi.core.SingleLiveEvents()
    val viewEvents = _viewEvents.asLiveData()

    fun dispatch(viewAction: NetworkViewAction) {
        when (viewAction) {
            is NetworkViewAction.PageRequest -> pageRequest()
            is NetworkViewAction.PartRequest -> partRequest()
            is NetworkViewAction.MultiRequest -> multiSourceRequest()
            is NetworkViewAction.ErrorRequest -> errorRequest()
        }
    }

    /**
     * 页面请求，通常包括刷新页面loading状态等
     */
    private fun pageRequest() {
        viewModelScope.launch {
            flow {
                delay(2000)
                emit("页面请求成功")
            }.onStart {
                _viewStates.setState { copy(pageStatus = PageStatus.Loading) }
            }.onEach {
                _viewStates.setState { copy(content = it, pageStatus = PageStatus.Success) }
            }.commonCatch {
                _viewStates.setState { copy(pageStatus = PageStatus.Error(it)) }
            }.collect()
        }
    }

    /**
     * 页面局部请求，例如点赞收藏等，通常需要弹dialog或toast
     */
    private fun partRequest() {
        viewModelScope.launch {
            flow {
                delay(2000)
                emit("点赞成功")
            }.onStart {
                _viewEvents.setEvent(NetworkViewEvent.ShowLoadingDialog)
            }.onEach {
                _viewEvents.setEvent(
                    NetworkViewEvent.DismissLoadingDialog, NetworkViewEvent.ShowToast(it)
                )
                _viewStates.setState { copy(content = it) }
            }.commonCatch {
                _viewEvents.setEvent(NetworkViewEvent.DismissLoadingDialog)
            }.collect()
        }
    }

    /**
     * 多数据源请求
     */
    private fun multiSourceRequest() {
        viewModelScope.launch {
            val flow1 = flow {
                delay(1000)
                emit("数据源1")
            }
            val flow2 = flow {
                delay(2000)
                emit("数据源2")
            }
            flow1.zip(flow2) { a, b ->
                "$a,$b"
            }.onStart {
                _viewEvents.setEvent(NetworkViewEvent.ShowLoadingDialog)
            }.onEach {
                _viewEvents.setEvent(
                    NetworkViewEvent.DismissLoadingDialog, NetworkViewEvent.ShowToast(it)
                )
                _viewStates.setState { copy(content = it) }
            }.commonCatch {
                _viewEvents.setEvent(NetworkViewEvent.DismissLoadingDialog)
            }.collect()
        }
    }

    /**
     * 请求错误示例
     */
    private fun errorRequest() {
        viewModelScope.launch {
            flow {
                delay(2000)
                throw NullPointerException("")
                emit("请求失败")
            }.onStart {
                _viewEvents.setEvent(NetworkViewEvent.ShowLoadingDialog)
            }.onEach {
                _viewEvents.setEvent(
                    NetworkViewEvent.DismissLoadingDialog, NetworkViewEvent.ShowToast(it)
                )
                _viewStates.setState { copy(content = it) }
            }.commonCatch {
                _viewEvents.setEvent(NetworkViewEvent.DismissLoadingDialog)
            }.collect()
        }
    }
}