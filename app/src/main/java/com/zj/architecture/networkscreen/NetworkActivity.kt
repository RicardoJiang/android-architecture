package com.zj.architecture.networkscreen

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.zj.architecture.R
import com.zj.architecture.observeState
import kotlinx.android.synthetic.main.activity_network.*

class NetworkActivity : AppCompatActivity() {
    private val viewModel by viewModels<NetworkViewModel>()
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

        }
    }

    fun simpleRequest(view: View) {
        viewModel.dispatch(NetworkViewAction.PageRequest)
    }

    fun partRequest(view: View) {

    }

    fun multiSource(view: View) {

    }

    fun requestError(view: View) {

    }
}