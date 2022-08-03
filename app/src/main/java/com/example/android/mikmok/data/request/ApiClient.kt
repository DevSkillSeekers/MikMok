package com.example.android.mikmok.data.request

import android.util.Log
import com.example.android.mikmok.data.DataManager
import com.example.android.mikmok.data.model.MikMokResponse
import com.example.android.mikmok.utils.Constants
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

class ApiClient(val dataManager: DataManager) {
    private val client = OkHttpClient()

    fun makeApiRequest() {
        val request = Request.Builder()
            .url(Constants.BASE_URL)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("Failed to execute request")
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.string().let { jsonString ->
                    val result = Gson().fromJson(jsonString, MikMokResponse::class.java)
                    dataManager.saveMikMokData(result)
                    Log.i(Constants.TAG, "data=>: $result")
                }
            }
        })
    }
}