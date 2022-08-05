package com.example.android.mikmok.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.widget.NestedScrollView
import androidx.media3.ui.PlayerView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.mikmok.R
import com.example.android.mikmok.data.model.Item
import com.example.android.mikmok.data.model.MikMokResponse
import com.example.android.mikmok.data.request.ApiClient
import com.example.android.mikmok.databinding.ActivityMainBinding
import com.example.android.mikmok.utils.Constants
import com.example.android.mikmok.utils.Constants.TAG
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

        feedAdapter = FeedAdapter(this, feedList)
        viewBinding.itemRecyclerView.adapter = feedAdapter
        viewBinding.itemRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        var oldVideoPlayer: PlayerView? = null
        var videoPlayer: PlayerView? = null

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            viewBinding.itemRecyclerView.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->

                if (videoPlayer != null) {
                    oldVideoPlayer = videoPlayer
                    oldVideoPlayer?.player?.pause()
                }
                val newView = viewBinding.itemRecyclerView.findViewWithTag(Constants.TAG) as View
                videoPlayer = newView.rootView.findViewById(R.id.video_view) as PlayerView
                videoPlayer?.player?.play()

            }
        }
    }

    private fun getFeed() {
        apiClient.makeApiRequest().enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("Failed to execute request")
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.string().let { jsonString ->
                    val result = Gson().fromJson(jsonString, MikMokResponse::class.java)
                    runOnUiThread {
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