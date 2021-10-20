package com.zj.architecture.mainscreen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zj.architecture.repository.NewsItem
import com.zj.architecture.repository.NewsRepository
import com.zj.architecture.utils.FetchStatus
import com.zj.architecture.utils.PageState
import com.zj.architecture.utils.SingleLiveEvent
import com.zj.architecture.utils.asLiveData
import kotlinx.coroutines.launch


class MainViewModel : ViewModel() {
    private var count: Int = 0
    private val repository: NewsRepository = NewsRepository.getInstance()
    private val _viewStates: MutableLiveData<MainViewState> = MutableLiveData()
    val viewStates = _viewStates.asLiveData()
    private val _viewEvents: SingleLiveEvent<MainViewEvent> = SingleLiveEvent()
    val viewEvents = _viewEvents.asLiveData()

    init {
        emit(MainViewState(fetchStatus = FetchStatus.NotFetched, newsList = emptyList()))
    }

    fun dispatch(action: MainViewAction) =
        reduce(viewStates.value, action)

    private fun reduce(state: MainViewState?, viewAction: MainViewAction) {
        when (viewAction) {
            is MainViewAction.NewsItemClicked -> newsItemClicked(viewAction.newsItem)
            MainViewAction.FabClicked -> fabClicked()
            MainViewAction.OnSwipeRefresh -> fetchNews(state)
            MainViewAction.FetchNews -> fetchNews(state)
        }
    }

    private fun newsItemClicked(newsItem: NewsItem) {
        emit(MainViewEvent.ShowSnackbar(newsItem.title))
    }

    private fun fabClicked() {
        count++
        emit(MainViewEvent.ShowToast(message = "Fab clicked count $count"))
    }

    private fun fetchNews(state: MainViewState?) {
        emit(state?.copy(fetchStatus = FetchStatus.Fetching))
        viewModelScope.launch {
            when (val result = repository.getMockApiResponse()) {
                is PageState.Error -> {
                    emit(state?.copy(fetchStatus = FetchStatus.Fetched))
                    emit(MainViewEvent.ShowToast(message = result.message))
                }
                is PageState.Success -> {
                    emit(state?.copy(fetchStatus = FetchStatus.Fetched, newsList = result.data))
                }
            }
        }
    }

    private fun emit(state: MainViewState?) {
        _viewStates.value = state
    }

    private fun emit(event: MainViewEvent?) {
        _viewEvents.value = event
    }
}