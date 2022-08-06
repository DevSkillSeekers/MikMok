package com.example.android.mikmok.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.ui.PlayerView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.mikmok.R
import com.example.android.mikmok.data.model.Item
import com.example.android.mikmok.data.model.MikMokResponse
import com.example.android.mikmok.data.request.ApiClient
import com.example.android.mikmok.databinding.ActivityMainBinding
import com.example.android.mikmok.utils.Constants
import com.example.android.mikmok.utils.ExoPlay
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

        var position = 0
        var currentView: PlayerView? = null

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            viewBinding.itemRecyclerView.addOnScrollListener(object :
                RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        val current =
                            (viewBinding.itemRecyclerView.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
                        if (current > -1 && current != position) {
                            position = current
                        }

                        if (currentView != null) {
                            currentView?.player?.pause()
                        }
                        val rootView: View? = viewBinding.itemRecyclerView.findViewWithTag(position)
                        currentView = rootView?.findViewById(R.id.video_view)
                        currentView?.player?.play()

                        Log.d("POSITION", position.toString())
                    }
                }

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                }
            })

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