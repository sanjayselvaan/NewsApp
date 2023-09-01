package com.example.newsappwithsinglelist

data class News(val index:Int, var heading:String, var body:String, var timeSpent:Long, var type:NewsItemType)