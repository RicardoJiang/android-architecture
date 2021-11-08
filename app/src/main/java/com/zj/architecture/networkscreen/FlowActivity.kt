package com.zj.architecture.networkscreen

import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.zj.architecture.R
import com.zj.architecture.observeState
import com.zj.architecture.utils.toast
import kotlinx.android.synthetic.main.activity_network.*

class FlowActivity : AppCompatActivity() {
    private val viewModel by viewModels<FlowViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_network)
        initViewModel()
    }

    private fun initViewModel() {
        viewModel.viewStates.let { state ->
            state.observeState(this, NetworkViewState::pageStatus) {
                when (it) {
                    is PageStatus.Success -> state_layout.showContent()
                    is PageStatus.Loading -> state_layout.showLoading()
                    is PageStatus.Error -> state_layout.showError()
                }
            }
            state.observeState(this, NetworkViewState::content) {
                tv_content.text = it
            }
        }

        viewModel.viewEvents.observe(this) {
            it.forEach { event ->
                when (event) {
                    is NetworkViewEvent.ShowToast -> toast(event.message)
                    is NetworkViewEvent.ShowLoadingDialog -> showLoadingDialog()
                    is NetworkViewEvent.DismissLoadingDialog -> dismissLoadingDialog()
                }
            }
        }
    }

    fun simpleRequest(view: View) {
        viewModel.dispatch(NetworkViewAction.PageRequest)
    }

    fun partRequest(view: View) {
        viewModel.dispatch(NetworkViewAction.PartRequest)
    }

    fun multiSource(view: View) {
        viewModel.dispatch(NetworkViewAction.MultiRequest)
    }

    fun errorRequest(view: View) {
        viewModel.dispatch(NetworkViewAction.ErrorRequest)
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