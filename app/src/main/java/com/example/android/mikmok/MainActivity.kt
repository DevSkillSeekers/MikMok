package com.example.android.mikmok

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }





    private fun requestUsingOkHttp(){
        val request = Request.Builder().url("https://raw.githubusercontent.com/android/tv-samples/main/ClassicsKotlin/app/src/main/assets/media-feed.json").build()
        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
            }

            override fun onResponse(call: Call, response: Response) {
//                runOnUiThread {
//                }
            }
        })
    }
}