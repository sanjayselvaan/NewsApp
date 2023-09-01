package com.example.newsappwithsinglelist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.newsappwithsinglelist.databinding.ActivityMainBinding
import com.example.newsappwithsinglelist.databinding.CompleteListItemBinding
import com.example.newsappwithsinglelist.databinding.DraftListItemBinding

import java.util.concurrent.TimeUnit

class NewsRecyclerViewAdapter(
    private var itemList: List<News>,
    private val itemClick: RecyclerItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == ITEM_DRAFT) {
            val binding =
                DraftListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return DraftItemViewHolder(binding)
//            val itemView=LayoutInflater.from(parent.context).inflate(R.layout.draft_list_item,parent,false)
//            return DraftItemViewHolder(itemView)
        } else {
            val binding =
                CompleteListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return CompleteItemViewHolder(binding)
//            val itemView=LayoutInflater.from(parent.context).inflate(R.layout.complete_list_item,parent,false)
//            return CompleteItemViewHolder(itemView)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemIndex = itemList[position].index
        val itemBody = itemList[position].body
        val itemTimeSpent = itemList[position].timeSpent
        val itemHeading = itemList[position].heading
        val newsItem=itemList[position]
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
                itemClick.onClickListener(false, itemIndex,itemHeading,itemBody,itemTimeSpent)
            }
//            (holder as CompleteItemViewHolder).tvCompleteItemBody.text=itemBody
//            holder.tvCompleteItemTimeSpent.text=convertMillisToHMS(itemTimeSpent)
//            holder.tvCompleteItemHeading.text=itemHeading
//            holder.itemView.setOnClickListener {
//                itemClick.onClickListener(false,itemIndex,itemHeading,itemBody,itemTimeSpent,RecyclerViewFragment.fragmentCompleteKey)
//            }
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
            if (itemList[position].type == NewsItemType.DRAFT) {
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

//    class DraftItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val tvDraftItemBody: TextView = itemView.findViewById(R.id.tvDraftItemBody)
//        val tvDraftItemHeading: TextView = itemView.findViewById(R.id.tvDraftItemHeading)
//    }
//
//    class CompleteItemViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
//        val tvCompleteItemTimeSpent:TextView=itemView.findViewById(R.id.tvCompleteItemTimeSpent)
//        val tvCompleteItemBody:TextView=itemView.findViewById(R.id.tvCompleteItemBody)
//        val tvCompleteItemHeading:TextView=itemView.findViewById(R.id.tvCompleteItemHeading)
//    }

        fun setData(newList: List<News>) {
            val diffUtilCallBack = RecyclerViewDiffUtil(itemList, newList)
            val diffResult = DiffUtil.calculateDiff(diffUtilCallBack)
            itemList = newList
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
                    oldItems[oldItemPosition].index == newItems[newItemPosition].index && oldItems[oldItemPosition].type == newItems[newItemPosition].type -> true
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
