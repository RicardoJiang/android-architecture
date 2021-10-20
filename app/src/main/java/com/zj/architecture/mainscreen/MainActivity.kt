package com.zj.architecture.mainscreen

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.zj.architecture.R
import com.zj.architecture.repository.NewsItem
import com.zj.architecture.toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val viewModel: MainActVM by viewModels()

    private val newsRvAdapter by lazy {
        NewsRvAdapter {
            viewModel.process(MainViewEvent.NewsItemClicked(it.tag as NewsItem))
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
            viewModel.process(MainViewEvent.OnSwipeRefresh)
        }

        fabStar.setOnClickListener {
            viewModel.process(MainViewEvent.FabClicked)
        }
    }

    private fun initViewModel() {
        viewModel.viewStates.observe(this) {
            renderViewState(it)
        }
        viewModel.viewEffects.observe(this) {
            renderViewEffect(it)
        }
    }

    private fun renderViewState(viewState: MainViewState) {
        when (viewState.fetchStatus) {
            is FetchStatus.Fetched -> {
                srlNewsHome.isRefreshing = false
            }
            is FetchStatus.NotFetched -> {
                viewModel.process(MainViewEvent.FetchNews)
                srlNewsHome.isRefreshing = false
            }
            is FetchStatus.Fetching -> {
                srlNewsHome.isRefreshing = true
            }
        }
        newsRvAdapter.submitList(viewState.newsList)
    }

    private fun renderViewEffect(viewEffect: MainViewEffect) {
        when (viewEffect) {
            is MainViewEffect.ShowSnackbar -> {
                Snackbar.make(coordinatorLayoutRoot, viewEffect.message, Snackbar.LENGTH_SHORT)
                    .show()
            }
            is MainViewEffect.ShowToast -> {
                toast(message = viewEffect.message)
            }
        }
    }
}

