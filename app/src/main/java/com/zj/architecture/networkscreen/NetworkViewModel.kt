package com.zj.architecture.networkscreen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zj.architecture.network.rxLaunch
import com.zj.architecture.setState
import com.zj.architecture.utils.SingleLiveEvent
import com.zj.architecture.utils.asLiveData
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
     * 页面局部请求，利用点赞收藏等，通常需要弹dialog或toast
     */
    private fun partRequest() {

    }

    /**
     * 多数据源请求
     */
    private fun multiSourceRequest() {

    }

    /**
     * 请求错误示例
     */
    private fun errorRequest() {

    }
}