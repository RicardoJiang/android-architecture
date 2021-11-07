package com.zj.architecture

import android.app.Application
import com.drake.statelayout.StateConfig

class MyApp : Application() {

    companion object {
        lateinit var instance: MyApp

        fun get(): MyApp {
            return instance
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        StateConfig.apply {
            loadingLayout = R.layout.layout_loading
            errorLayout = R.layout.layout_error
        }
    }
}