package com.zj.architecture.mainscreen

import com.zj.architecture.repository.NewsItem


data class MainViewState(val fetchStatus: FetchStatus, val newsList: List<NewsItem>)

sealed class MainViewEffect {
    data class ShowSnackbar(val message: String) : MainViewEffect()
    data class ShowToast(val message: String) : MainViewEffect()
}

sealed class MainViewEvent {
    data class NewsItemClicked(val newsItem: NewsItem) : MainViewEvent()
    object FabClicked : MainViewEvent()
    object OnSwipeRefresh : MainViewEvent()
    object FetchNews : MainViewEvent()
}

sealed class FetchStatus {
    object Fetching : FetchStatus()
    object Fetched : FetchStatus()
    object NotFetched : FetchStatus()
}