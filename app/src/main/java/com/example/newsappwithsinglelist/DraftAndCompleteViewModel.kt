package com.example.newsappwithsinglelist

import androidx.lifecycle.ViewModel

class DraftAndCompleteViewModel : ViewModel() {
    private var draftList = mutableListOf<News>()
    private var completeList = mutableListOf<News>()
    private var newsList = listOf<News>()
    fun populateDataInDraftList() {
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
            draftList.add(i, draftNews)
        }
    }


    fun updateDetailsForDraftItemInDraftList(id: Int, newBodyText: String?, newTimeSpent: Long) {
        val position = returnPositionForDraftItemInDraftList(id)
        if (position != null) {
            val existingHeading = draftList[position].heading
            val updatedTime=draftList[position].timeSpent+newTimeSpent
            if (newBodyText != null) {
                draftList[position] = News(
                    id,
                    existingHeading,
                    newBodyText,
                    timeSpent = updatedTime,
                    NewsItemType.DRAFT
                )
            } else {
                val existingBody = draftList[position].body
                val updatedTime=draftList[position].timeSpent+newTimeSpent
                draftList[position] = News(
                    id,
                    existingHeading,
                    existingBody,
                    timeSpent = updatedTime,
                    NewsItemType.DRAFT
                )
            }
        }
    }

    fun removeDraftItemFromDraftList(id: Int) {
        returnPositionForDraftItemInDraftList(id)?.let {
            draftList.removeAt(it)
        }
    }

    private fun returnPositionForDraftItemInDraftList(id: Int): Int? {
        var position: Int? = null
        for (i in 0 until draftList.size) {
            if (draftList[i].id == id) {
                position = i
            }
        }
        return position
    }

    fun addCompleteItemInCompleteList(newsItem: News) {
        completeList.add(newsItem)
    }

    fun returnNewsList(): List<News> {
        newsList = (draftList + completeList)
        return newsList
    }

}