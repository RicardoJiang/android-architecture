package com.zj.architecture.mainscreen

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.zj.architecture.R
import com.zj.architecture.repository.NewsItem
import com.zj.architecture.utils.FetchStatus
import com.zj.architecture.utils.map
import com.zj.architecture.utils.toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()

    private val newsRvAdapter by lazy {
        NewsRvAdapter {
            viewModel.dispatch(MainViewAction.NewsItemClicked(it.tag as NewsItem))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        initViewModel()
    }

    private fun initView() {
        rvNewsHome.adapter = newsRvAdapter

        srlNewsHome.setOnRefreshListener {
            viewModel.dispatch(MainViewAction.OnSwipeRefresh)
        }

        fabStar.setOnClickListener {
            viewModel.dispatch(MainViewAction.FabClicked)
        }
    }

    private fun initViewModel() {
        viewModel.viewStates.let { state ->
            state.map(MainViewState::newsList).observe(this) { list ->
                newsRvAdapter.submitList(list)
            }
            state.map(MainViewState::fetchStatus).observe(this){
                when (it) {
                    is FetchStatus.Fetched -> {
                        srlNewsHome.isRefreshing = false
                    }
                    is FetchStatus.NotFetched -> {
                        viewModel.dispatch(MainViewAction.FetchNews)
                        srlNewsHome.isRefreshing = false
                    }
                    is FetchStatus.Fetching -> {
                        srlNewsHome.isRefreshing = true
                    }
                }
            }
        }
        viewModel.viewStates.observe(this) {
            renderViewState(it)
        }
        viewModel.viewEvents.observe(this) {
            renderViewEvent(it)
        }
    }

    private fun renderViewState(viewState: MainViewState) {
        when (viewState.fetchStatus) {

        }

    }

    private fun renderViewEvent(viewEvent: MainViewEvent) {
        when (viewEvent) {
            is MainViewEvent.ShowSnackbar -> {
                Snackbar.make(coordinatorLayoutRoot, viewEvent.message, Snackbar.LENGTH_SHORT)
                    .show()
            }
            is MainViewEvent.ShowToast -> {
                toast(message = viewEvent.message)
            }
        }
    }
}

