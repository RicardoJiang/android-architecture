package com.zj.architecture.networkscreen

data class NetworkViewState(
    val content: String = "等待网络请求内容",
    val pageStatus: PageStatus = PageStatus.Success
)

sealed class NetworkViewEvent {
    data class ShowToast(val message: String) : NetworkViewEvent()
    object ShowLoadingDialog : NetworkViewEvent()
    object DismissLoadingDialog : NetworkViewEvent()
}

sealed class NetworkViewAction {
    object PageRequest : NetworkViewAction()
    object PartRequest : NetworkViewAction()
    object MultiRequest : NetworkViewAction()
    object ErrorRequest : NetworkViewAction()
}


sealed class PageStatus {
    object Loading : PageStatus()
    object Success : PageStatus()
    data class Error(val throwable: Throwable) : PageStatus()
}