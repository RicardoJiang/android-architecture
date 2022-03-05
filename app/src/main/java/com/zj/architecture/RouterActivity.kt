package com.zj.architecture

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.zj.architecture.login.livedata.LoginActivity
import com.zj.architecture.networkscreen.FlowActivity

class RouterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_router)
    }

    fun simple(view: View) {
        startActivity(Intent(this, LoginActivity::class.java))
    }

    fun flow(view: View) {
        startActivity(Intent(this, FlowActivity::class.java))
    }
}