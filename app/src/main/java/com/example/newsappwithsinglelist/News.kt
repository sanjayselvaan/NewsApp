package com.example.newsappwithsinglelist

data class News(
    val id: Int,
    val heading: String,
    val body: String,
    val timeSpent: Long,
    val type: NewsItemType
)