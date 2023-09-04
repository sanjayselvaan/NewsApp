package com.example.newsappwithsinglelist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.newsappwithsinglelist.databinding.CompleteListItemBinding
import com.example.newsappwithsinglelist.databinding.DraftListItemBinding

import java.util.concurrent.TimeUnit

class NewsRecyclerViewAdapter(
    itemList: List<News>,
    private val itemClick: RecyclerItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var mutableList = mutableListOf<News>()

    init {
        mutableList.clear()
        mutableList.addAll(itemList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == ITEM_DRAFT) {
            val binding =
                DraftListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return DraftItemViewHolder(binding)
        } else {
            val binding =
                CompleteListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return CompleteItemViewHolder(binding)
        }
    }

    override fun getItemCount(): Int {
        return mutableList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemIndex = mutableList[position].id
        val itemBody = mutableList[position].body
        val itemTimeSpent = mutableList[position].timeSpent
        val itemHeading = mutableList[position].heading
        val newsItem = mutableList[position]
        if (getItemViewType(position) == ITEM_DRAFT) {
            (holder as DraftItemViewHolder).bindDraftItem(newsItem)
//            (holder as DraftItemViewHolder).tvDraftItemBody.text = itemBody
//            (holder).tvDraftItemHeading.text = itemHeading
            holder.itemView.setOnClickListener {
                itemClick.onClickListener(
                    true,
                    itemIndex,
                    itemHeading,
                    itemBody,
                    itemTimeSpent
                )
            }
        } else {
            (holder as CompleteItemViewHolder).bindCompleteItem(newsItem)
            holder.itemView.setOnClickListener {
                itemClick.onClickListener(false, itemIndex, itemHeading, itemBody, itemTimeSpent)
            }
        }
    }

    inner class DraftItemViewHolder(val binding: DraftListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindDraftItem(dataItem: News) {
            binding.tvDraftItemHeading.text = dataItem.heading
            binding.tvDraftItemBody.text = dataItem.body
        }
    }

    inner class CompleteItemViewHolder(val binding: CompleteListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindCompleteItem(dataItem: News) {
            binding.tvCompleteItemBody.text = dataItem.body
            binding.tvCompleteItemHeading.text = dataItem.heading
            binding.tvCompleteItemTimeSpent.text = convertMillisToHMS(dataItem.timeSpent)
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (mutableList[position].type == NewsItemType.DRAFT) {
            return ITEM_DRAFT
        } else {
            return ITEM_COMPLETE
        }
    }

    companion object {
        const val ITEM_DRAFT = 0
        const val ITEM_COMPLETE = 1
    }

    private fun convertMillisToHMS(milliseconds: Long): String {
        val hours = TimeUnit.MILLISECONDS.toHours(milliseconds)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds) % 60

        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    fun setData(newList: List<News>) {
        val diffUtilCallBack = RecyclerViewDiffUtil(mutableList, newList)
        val diffResult = DiffUtil.calculateDiff(diffUtilCallBack)
        mutableList.clear()
        mutableList.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }


    private class RecyclerViewDiffUtil(
        private val oldItems: List<News>,
        private val newItems: List<News>
    ) :
        DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return oldItems.size
        }

        override fun getNewListSize(): Int {
            return newItems.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return when {
                oldItems[oldItemPosition].id == newItems[newItemPosition].id && oldItems[oldItemPosition].type == newItems[newItemPosition].type -> true
                else -> false
            }
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return when {
                oldItems[oldItemPosition].body != newItems[newItemPosition].body -> false
                oldItems[oldItemPosition].heading != newItems[newItemPosition].heading -> false
                oldItems[oldItemPosition].type != newItems[newItemPosition].type -> false
                oldItems[oldItemPosition].timeSpent != newItems[newItemPosition].timeSpent -> {
                    false
                }

                else -> true
            }
        }
    }
}
