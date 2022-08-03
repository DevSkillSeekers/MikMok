package com.example.android.mikmok.data.model

import com.example.android.mikmok.data.model.Feed

data class MikMokResponse(
    val backgrounds: List<String>,
    val feed: List<Feed>
)