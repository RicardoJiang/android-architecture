package com.zj.architecture.mainscreen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zj.architecture.LCE
import com.zj.architecture.asLiveData
import com.zj.architecture.repository.NewsItem
import com.zj.architecture.repository.NewsRepository
import kotlinx.coroutines.launch


class MainActVM : ViewModel() {
    private var count: Int = 0
    private val repository: NewsRepository = NewsRepository.getInstance()
    private val _viewStates: MutableLiveData<MainViewState> = MutableLiveData()
    val viewStates = _viewStates.asLiveData()
    private val _viewEffects: MutableLiveData<MainViewEffect> = MutableLiveData()
    val viewEffects = _viewEffects.asLiveData()

    init {
        _viewStates.value =
            MainViewState(fetchStatus = FetchStatus.NotFetched, newsList = emptyList())
    }

    fun process(viewEvent: MainViewEvent) {
        when (viewEvent) {
            is MainViewEvent.NewsItemClicked -> newsItemClicked(viewEvent.newsItem)
            MainViewEvent.FabClicked -> fabClicked()
            MainViewEvent.OnSwipeRefresh -> fetchNews()
            MainViewEvent.FetchNews -> fetchNews()
        }
    }

    private fun newsItemClicked(newsItem: NewsItem) {
        _viewEffects.value = MainViewEffect.ShowSnackbar(newsItem.title)
    }

    private fun fabClicked() {
        count++
        _viewEffects.value = MainViewEffect.ShowToast(message = "Fab clicked count $count")
    }

    private fun fetchNews() {
        _viewStates.value = _viewStates.value?.copy(fetchStatus = FetchStatus.Fetching)
        viewModelScope.launch {
            when (val result = repository.getMockApiResponse()) {
                is LCE.Error -> {
                    _viewStates.value = _viewStates.value?.copy(fetchStatus = FetchStatus.Fetched)
                    _viewEffects.value = MainViewEffect.ShowToast(message = result.message)
                }
                is LCE.Success -> {
                    _viewStates.value =
                        _viewStates.value?.copy(
                            fetchStatus = FetchStatus.Fetched,
                            newsList = result.data
                        )
                }
            }
        }
    }
}