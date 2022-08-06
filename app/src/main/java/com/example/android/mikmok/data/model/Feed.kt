package com.example.android.mikmok.data.model

data class Feed(
    val description: String,
    val id: String,
    val image: String,
    val items: List<Item>,
    val title: String
)