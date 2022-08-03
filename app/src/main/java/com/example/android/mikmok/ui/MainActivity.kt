package com.example.android.mikmok.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.mikmok.data.model.Item
import com.example.android.mikmok.data.model.MikMokResponse
import com.example.android.mikmok.data.request.ApiClient
import com.example.android.mikmok.databinding.ActivityMainBinding
import com.example.android.mikmok.utils.Constants
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException


class MainActivity : AppCompatActivity(), FeedAdapter.OnClickListener {

    private lateinit var feedAdapter: FeedAdapter
    private var feedList = ArrayList<Item>()
    private val apiClient by lazy { ApiClient() }
    private val viewBinding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

        getFeed()

        val layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        feedAdapter = FeedAdapter(this, feedList)
        viewBinding.itemRecyclerView.layoutManager = layoutManager
        viewBinding.itemRecyclerView.adapter = feedAdapter
    }

    private fun getFeed() {
        apiClient.makeApiRequest().enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("Failed to execute request")
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.string().let { jsonString ->
                    val result = Gson().fromJson(jsonString, MikMokResponse::class.java)
                    runOnUiThread{
                        feedAdapter.setData(result.feed.first().items as ArrayList<Item>)
                    }
                }
            }
        })
    }



    override fun onClick(item: Item) {
        shareURL(item.url)
    }

    private fun shareURL(url: String) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, url)
            type = "text/plain"
        }
        startActivity(Intent.createChooser(sendIntent, null))
    }
}