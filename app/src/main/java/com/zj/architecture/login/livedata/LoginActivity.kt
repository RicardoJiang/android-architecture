package com.zj.architecture.login.livedata

import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.zj.architecture.R
import com.zj.architecture.login.LoginViewAction
import com.zj.architecture.login.LoginViewEvent
import com.zj.architecture.login.LoginViewState
import com.zj.mvi.core.observeEvent
import com.zj.mvi.core.observeState
import com.zj.architecture.utils.toast
import kotlinx.android.synthetic.main.layout_login.*

class LoginActivity : AppCompatActivity() {
    private val viewModel by viewModels<LoginViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_login)
        initView()
        initViewStates()
        initViewEvents()
    }

    private fun initView() {
        edit_user_name.addTextChangedListener {
            viewModel.dispatch(LoginViewAction.UpdateUserName(it.toString()))
        }
        edit_password.addTextChangedListener {
            viewModel.dispatch(LoginViewAction.UpdatePassword(it.toString()))
        }
        btn_login.setOnClickListener {
            viewModel.dispatch(LoginViewAction.Login)
        }
    }

    private fun initViewStates() {
        viewModel.viewStates.let { states ->
            states.observeState(this, LoginViewState::userName) {
                edit_user_name.setText(it)
                edit_user_name.setSelection(it.length)
            }
            states.observeState(this, LoginViewState::password) {
                edit_password.setText(it)
                edit_password.setSelection(it.length)
            }
            states.observeState(this, LoginViewState::isLoginEnable) {
                btn_login.isEnabled = it
                btn_login.alpha = if (it) 1f else 0.5f
            }
            states.observeState(this, LoginViewState::passwordTipVisible) {
                tv_label.visibility = if (it) View.VISIBLE else View.INVISIBLE
            }
        }
    }

    private fun initViewEvents() {
        viewModel.viewEvents.observeEvent(this) {
            when (it) {
                is LoginViewEvent.ShowToast -> toast(it.message)
                is LoginViewEvent.ShowLoadingDialog -> showLoadingDialog()
                is LoginViewEvent.DismissLoadingDialog -> dismissLoadingDialog()
            }
        }
    }

    private var progressDialog: ProgressDialog? = null

    private fun showLoadingDialog() {
        if (progressDialog == null)
            progressDialog = ProgressDialog(this)
        progressDialog?.show()
    }

    private fun dismissLoadingDialog() {
        progressDialog?.takeIf { it.isShowing }?.dismiss()
    }
}