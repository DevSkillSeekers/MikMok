package com.example.android.mikmok.utils

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.util.Util
import androidx.media3.exoplayer.ExoPlayer

class ExoPlay(context: Context) {

    var player: ExoPlayer? = null
    private var playWhenReady = false//true
    private var currentItem = 0
    private var playbackPosition = 0L

    init {
        player = ExoPlayer.Builder(context)
            .build()
    }

    fun setURL(url: String) {
        player.also { exoPlayer ->
            val mediaItem =
                MediaItem.fromUri(url)
            if (exoPlayer != null) {
                exoPlayer.setMediaItem(mediaItem)
                exoPlayer.playWhenReady = playWhenReady
                exoPlayer.seekTo(currentItem, playbackPosition)
                exoPlayer.prepare()
            }
        }
    }
}