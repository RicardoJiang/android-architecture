package com.zj.architecture.login

import com.zj.architecture.networkscreen.NetworkViewEvent

data class LoginViewState(val userName: String = "", val password: String = "") {
    val isLoginEnable: Boolean
        get() = userName.isNotEmpty() && password.length >= 6
    val passwordTipVisible: Boolean
        get() = password.length in 1..5
}

sealed class LoginViewEvent {
    data class ShowToast(val message: String) : LoginViewEvent()
    object ShowLoadingDialog : LoginViewEvent()
    object DismissLoadingDialog : LoginViewEvent()
}

sealed class LoginViewAction {
    data class UpdateUserName(val userName: String) : LoginViewAction()
    data class UpdatePassword(val password: String) : LoginViewAction()
    object Login : LoginViewAction()
}