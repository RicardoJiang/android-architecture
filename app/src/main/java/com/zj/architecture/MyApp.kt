package com.zj.architecture

import android.app.Application

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
    }
}