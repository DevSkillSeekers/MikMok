package com.example.android.mikmok.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.android.mikmok.R
import com.example.android.mikmok.data.MikMokResponse
import com.example.android.mikmok.utils.Contest
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    private fun requestApi() {
        val request = Request.Builder()
            .url(Contest.BASE_URL)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("Failed to execute request")
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.string().let { jsonString ->
                    val result = Gson().fromJson(jsonString, MikMokResponse::class.java)
                    println(result)
                    //runOnUiThread {
                    //}
                }
            }
        })
    }
}