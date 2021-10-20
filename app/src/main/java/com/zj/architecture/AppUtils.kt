package com.rohitss.mvr

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

const val BASE_URL = "https://your_api_endpoint.com/"

fun inflate(
    context: Context,
    viewId: Int,
    parent: ViewGroup? = null,
    attachToRoot: Boolean = false
): View {
    return LayoutInflater.from(context).inflate(viewId, parent, attachToRoot)
}

fun Context.toast(message: String, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, length).show()
}

//LCE -> Loading/Content/Error
sealed class LCE<out T> {
    data class Success<T>(val data: T) : LCE<T>()
    data class Error<T>(val message: String) : LCE<T>() {
        constructor(t: Throwable) : this(t.message ?: "")
    }
}