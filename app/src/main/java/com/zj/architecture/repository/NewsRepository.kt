package com.zj.architecture.repository

import com.zj.architecture.LCE
import com.zj.architecture.mockapi.MockApi

class NewsRepository {

    companion object {
        // For Singleton instantiation
        @Volatile
        private var instance: NewsRepository? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: NewsRepository().also { instance = it }
            }
    }

    suspend fun getMockApiResponse(): LCE<List<NewsItem>> {
        val articlesApiResult = try {
            MockApi.create().getLatestNews()
        } catch (e: Exception) {
            return LCE.Error(e)
        }

        articlesApiResult.articles?.let { list ->
            return LCE.Success(data = list)
        } ?: run {
            return LCE.Error("Failed to get News")
        }
    }
}