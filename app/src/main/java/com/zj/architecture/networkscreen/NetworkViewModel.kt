package com.zj.architecture.networkscreen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zj.architecture.network.rxLaunch
import com.zj.architecture.setEvent
import com.zj.architecture.setState
import com.zj.architecture.utils.SingleLiveEvent
import com.zj.architecture.utils.asLiveData
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay

class NetworkViewModel : ViewModel() {
    private val _viewStates = MutableLiveData(NetworkViewState())
    val viewStates = _viewStates.asLiveData()
    private val _viewEvents: SingleLiveEvent<NetworkViewEvent> = SingleLiveEvent()
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
        viewModelScope.rxLaunch<String> {
            onRequest = {
                _viewStates.setState { copy(pageStatus = PageStatus.Loading) }
                delay(2000)
                "页面请求成功"
            }
            onSuccess = {
                _viewStates.setState { copy(content = it, pageStatus = PageStatus.Success) }
            }
            onError = {
                _viewStates.setState { copy(pageStatus = PageStatus.Error(it)) }
            }
        }
    }

    /**
     * 页面局部请求，例如点赞收藏等，通常需要弹dialog或toast
     */
    private fun partRequest() {
        viewModelScope.rxLaunch<String> {
            onRequest = {
                _viewEvents.setEvent(NetworkViewEvent.ShowLoadingDialog)
                delay(2000)
                "点赞成功"
            }
            onSuccess = {
                _viewEvents.setEvent(NetworkViewEvent.DismissLoadingDialog)
                _viewEvents.setEvent(NetworkViewEvent.ShowToast(it))
                _viewStates.setState { copy(content = it) }
            }
            onError = {
                _viewEvents.setEvent(NetworkViewEvent.DismissLoadingDialog)
            }
        }
    }

    /**
     * 多数据源请求
     */
    private fun multiSourceRequest() {
        viewModelScope.rxLaunch<String> {
            onRequest = {
                _viewEvents.setEvent(NetworkViewEvent.ShowLoadingDialog)
                coroutineScope {
                    val source1 = async { source1() }
                    val source2 = async { source2() }
                    val result = source1.await() + "," + source2.await()
                    result
                }
            }
            onSuccess = {
                _viewEvents.setEvent(NetworkViewEvent.DismissLoadingDialog)
                _viewEvents.setEvent(NetworkViewEvent.ShowToast(it))
                _viewStates.setState { copy(content = it) }
            }
            onError = {
                _viewEvents.setEvent(NetworkViewEvent.DismissLoadingDialog)
            }
        }
    }

    /**
     * 请求错误示例
     */
    private fun errorRequest() {
        viewModelScope.rxLaunch<String> {
            onRequest = {
                _viewEvents.setEvent(NetworkViewEvent.ShowLoadingDialog)
                delay(2000)
                throw NullPointerException("")
                "请求失败"
            }
            onSuccess = {
                _viewEvents.setEvent(NetworkViewEvent.DismissLoadingDialog)
                _viewEvents.setEvent(NetworkViewEvent.ShowToast(it))
                _viewStates.setState { copy(content = it) }
            }
            onError = {
                _viewEvents.setEvent(NetworkViewEvent.DismissLoadingDialog)
            }
        }
    }

    private suspend fun source1(): String {
        delay(1000)
        return "数据源1"
    }

    private suspend fun source2(): String {
        delay(2000)
        return "数据源2"
    }
}