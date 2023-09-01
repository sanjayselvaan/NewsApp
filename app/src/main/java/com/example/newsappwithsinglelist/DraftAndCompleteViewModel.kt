package com.example.newsappwithsinglelist

import androidx.lifecycle.ViewModel

class DraftAndCompleteViewModel : ViewModel() {
    private var newsList = mutableListOf<News>()
    fun populateDataInNewsList() {
        val headingList = mutableListOf(
            "One",
            "Two",
            "Three",
            "Four",
            "Five",
            "Six",
            "Seven",
            "Eight",
            "Nine",
            "Ten",
            "Eleven",
            "Twelve",
            "Thirteen",
            "Fourteen",
            "Fiveteen",
            "Sixteen",
            "Seventeen",
            "Eighteen",
            "Nineteen",
            "Twenty"
        )
        val bodyList = mutableListOf(
            "ASDFDAfduojnwbfibsbdfjnsdjlnf",
            "ahbfiubdgifbdafiabkf",
            "badkbfbudbfbdabfdfuobdf",
            "fkadbfubdbfbdf",
            "ndbufbdbdsvjbvd",
            "dajbfnudbfdfb",
            "ndndfnudhf",
            "kabhdbhkaksdbabsd",
            "tdfttfthfhff",
            "gtfctftgvjfjgdddhtdhfgjfgjfvhjghgfyudrsesesfggnfhjfuyfyfhvhhfgyfgyljvjhffyjfjvfyjfjfjktkfkhffgvhvhmvhjvhjvjhggygggtfctftgvjfjgdddhtdhfgjfgjfvhjghgfyudrsesesfggnfhjfuyfyfhvhhfgyfgyljvjhffyjfjvfyjfjfjktkfkhffgvhvhmvhjvhjvjhggygggtfctftgvjfjgdddhtdhfgjfgjfvhjghgfyudrsesesfggnfhjfuyfyfhvhhfgyfgyljvjhffyjfjvfyjfjfjktkfkhffgvhvhmvhjvhjvjhggygggtfctftgvjfjgdddhtdhfgjfgjfvhjghgfyudrsesesfggnfhjfuyfyfhvhhfgyfgyljvjhffyjfjvfyjfjfjktkfkhffgvhvhmvhjvhjvjhggygggtfctftgvjfjgdddhtdhfgjfgjfvhjghgfyudrsesesfggnfhjfuyfyfhvhhfgyfgyljvjhffyjfjvfyjfjfjktkfkhffgvhvhmvhjvhjvjhggygg",
            "ASDFDAfduojnwbfibsbdfjnsdjlnf",
            "ahbfiubdgifbdafiabkf",
            "badkbfbudbfbdabfdfuobdf",
            "fkadbfubdbfbdf",
            "ndbufbdbdsvjbvd",
            "dajbfnudbfdfb",
            "ndndfnudhf",
            "kabhdbhkaksdbabsd",
            "tdfttfthfhff",
            "gtfctftgvjfjgdddhtdhfgjfgjfvhjghgfyudrsesesfggnfhjfuyfyfhvhhfgyfgyljvjhffyjfjvfyjfjfjktkfkhffgvhvhmvhjvhjvjhggygggtfctftgvjfjgdddhtdhfgjfgjfvhjghgfyudrsesesfggnfhjfuyfyfhvhhfgyfgyljvjhffyjfjvfyjfjfjktkfkhffgvhvhmvhjvhjvjhggygggtfctftgvjfjgdddhtdhfgjfgjfvhjghgfyudrsesesfggnfhjfuyfyfhvhhfgyfgyljvjhffyjfjvfyjfjfjktkfkhffgvhvhmvhjvhjvjhggygggtfctftgvjfjgdddhtdhfgjfgjfvhjghgfyudrsesesfggnfhjfuyfyfhvhhfgyfgyljvjhffyjfjvfyjfjfjktkfkhffgvhvhmvhjvhjvjhggygggtfctftgvjfjgdddhtdhfgjfgjfvhjghgfyudrsesesfggnfhjfuyfyfhvhhfgyfgyljvjhffyjfjvfyjfjfjktkfkhffgvhvhmvhjvhjvjhggygg12312"
        )
        for (i in bodyList.indices) {
            val draftNews = News(i, headingList[i], bodyList[i], 0, NewsItemType.DRAFT)
            newsList.add(i, draftNews)
        }
    }


    fun updateDetailsForDraftItemInNewsList(index: Int, newBodyText: String?, newTimeSpent: Long?) {
        val position = returnPositionForDraftItemInNewsList(index)
        if (position != null) {
            if (newBodyText != null) {
                newsList[position].body = newBodyText
            }
            if (newTimeSpent != null) {
                newsList[position].timeSpent += newTimeSpent
            }
        }
    }

    fun removeDraftItemFromNewsList(index: Int) {
        returnPositionForDraftItemInNewsList(index)?.let {
            newsList.removeAt(it)
        }

    }

    private fun returnPositionForDraftItemInNewsList(index: Int): Int? {
        var position: Int? = null
        for (i in 0 until newsList.size) {
            if (newsList[i].index == index) {
                if (newsList[i].type == NewsItemType.DRAFT) {
                    position = i
                }
            }
        }
        return position
    }

    fun addCompleteItemInNewsList(newsItem: News) {
        newsList.add(newsItem)
    }

    fun returnDraftItemFromNewsList(index: Int): News? {
        var newsItem: News? = null
        returnPositionForDraftItemInNewsList(index)?.let {
            newsItem = newsList[it]
        }
        return newsItem
    }

    fun returnNewsList(): MutableList<News> {
        return newsList
    }

}