package com.zj.architecture.login.livedata

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zj.architecture.login.LoginViewAction
import com.zj.architecture.login.LoginViewEvent
import com.zj.architecture.login.LoginViewState
import com.zj.mvi.core.setEvent
import com.zj.mvi.core.setState
import com.zj.architecture.utils.asLiveData
import com.zj.mvi.core.withState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val _viewStates = MutableLiveData(LoginViewState())
    val viewStates = _viewStates.asLiveData()
    private val _viewEvents: com.zj.mvi.core.SingleLiveEvents<LoginViewEvent> =
        com.zj.mvi.core.SingleLiveEvents()
    val viewEvents = _viewEvents.asLiveData()

    fun dispatch(viewAction: LoginViewAction) {
        when (viewAction) {
            is LoginViewAction.UpdateUserName -> updateUserName(viewAction.userName)
            is LoginViewAction.UpdatePassword -> updatePassword(viewAction.password)
            is LoginViewAction.Login -> login()
        }
    }

    private fun updateUserName(userName: String) {
        _viewStates.setState { copy(userName = userName) }
    }

    private fun updatePassword(password: String) {
        _viewStates.setState { copy(password = password) }
    }

    private fun login() {
        viewModelScope.launch {
            flow {
                loginLogic()
                emit("登录成功")
            }.onStart {
                _viewEvents.setEvent(LoginViewEvent.ShowLoadingDialog)
            }.onEach {
                _viewEvents.setEvent(
                    LoginViewEvent.DismissLoadingDialog, LoginViewEvent.ShowToast(it)
                )
            }.catch {
                _viewStates.setState { copy(userName = "", password = "") }
                _viewEvents.setEvent(
                    LoginViewEvent.DismissLoadingDialog, LoginViewEvent.ShowToast("登录失败")
                )
            }.collect()
        }
    }

    private suspend fun loginLogic() {
        withState(viewStates) {
            val userName = it.userName
            val password = it.password
            delay(2000)
            throw Exception("登录失败")
            "$userName,$password"
        }
    }
}