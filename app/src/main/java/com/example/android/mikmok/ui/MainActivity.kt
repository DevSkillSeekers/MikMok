package com.example.android.mikmok.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
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
    private val exoPlay = ExoPlay()
    private var currentView: PlayerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

        getFeed()

        feedAdapter = FeedAdapter(this, feedList)
        viewBinding.itemRecyclerView.adapter = feedAdapter

        var position = -1
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            viewBinding.itemRecyclerView.addOnScrollListener(object :
                RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        var current =
                            (viewBinding.itemRecyclerView.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
                        if (current == -1)
                            current =
                                (viewBinding.itemRecyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

                        if (current > -1 && current != position) {
                            position = current
                            stopCurrentVideo()
                            playVideo(
                                viewBinding.itemRecyclerView.findViewWithTag(position),
                                position
                            )
                        }
                    }
                }

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                }
            })

        }
    }

    private fun getFeed() {
        setLoadingVisibility(false)

        apiClient.makeApiRequest().enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("Failed to execute request")
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.string().let { jsonString ->
                    val result = Gson().fromJson(jsonString, MikMokResponse::class.java)
                    runOnUiThread {
                        setLoadingVisibility(true)
                        feedAdapter.setData(result.feed.first().items as ArrayList<Item>)
                    }
                }
            }
        })
    }

    private fun setLoadingVisibility(isDone: Boolean) {
        if (isDone) {
            viewBinding.statusLoading.visibility = View.GONE
            viewBinding.itemRecyclerView.visibility = View.VISIBLE
        } else {
            viewBinding.statusLoading.visibility = View.VISIBLE
            viewBinding.itemRecyclerView.visibility = View.GONE
        }
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

    private fun stopCurrentVideo() {
        currentView?.player?.pause()
    }

    private fun playVideo(rootView: View?, index: Int) {
        if (rootView != null) {
            currentView = rootView.findViewById(R.id.video_view)
            exoPlay.setURL(feedList[index].url, this)
            currentView?.player = exoPlay.player
        }
    }
}