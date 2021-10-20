package com.zj.architecture.mockapi

import com.zj.architecture.repository.NewsItem

data class MockApiResponse(
    val articles: List<NewsItem>? = null
)