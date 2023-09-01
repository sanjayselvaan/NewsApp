package com.example.newsappwithsinglelist

import java.text.ParsePosition

interface RecyclerItemClickListener {
    fun onClickListener(boolean: Boolean,index:Int,heading:String,body:String,timeSpent:Long)
}