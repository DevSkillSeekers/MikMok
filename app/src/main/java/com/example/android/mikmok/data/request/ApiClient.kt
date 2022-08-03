package com.example.android.mikmok.data.request

import com.example.android.mikmok.utils.Constants
import okhttp3.*

class ApiClient() {
    private val client = OkHttpClient()

    fun makeApiRequest(): Call {
        val request = Request.Builder()
            .url(Constants.BASE_URL)
            .build()

        return client.newCall(request)
    }
}