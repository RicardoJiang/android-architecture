package com.zj.architecture.mockapi

import com.zj.architecture.utils.BASE_URL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface MockApi {
    @GET("mock")
    suspend fun getLatestNews(): MockApiResponse

    companion object {
        // Please do not follow this code as this has been
        // modified to intercept API calls with mock response.
        fun create(): MockApi {
            val okHttpClient = OkHttpClient()
                .newBuilder()
                .addInterceptor(MockInterceptor())
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MockApi::class.java)
        }
    }
}