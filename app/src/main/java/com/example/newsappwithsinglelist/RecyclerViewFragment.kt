package com.example.newsappwithsinglelist

import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsappwithsinglelist.databinding.FragmentRecyclerViewBinding

class RecyclerViewFragment() : Fragment(),RecyclerItemClickListener {
    private lateinit var binding: FragmentRecyclerViewBinding
    private lateinit var draftAndCompleteViewModel: DraftAndCompleteViewModel
    private lateinit var adapter:NewsRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecyclerViewBinding.inflate(inflater, container, false)
        binding.newsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        draftAndCompleteViewModel= ViewModelProvider(requireActivity())[DraftAndCompleteViewModel::class.java]
        //SETTING RECYCLER VIEW ADAPTER
        val recyclerViewDataList=draftAndCompleteViewModel.returnNewsList().toList()
        adapter = NewsRecyclerViewAdapter(recyclerViewDataList,this)
        binding.newsRecyclerView.adapter=adapter

        //FRAGMENT RESULT LISTENER FOR SAVE BUTTON EVENT
        parentFragmentManager.setFragmentResultListener(GetAndShowNewsItemFragment.updateDraftItemEvent, this) { _, bundle ->
            val newBodyText=bundle.getString(GetAndShowNewsItemFragment.keyForBodyText)
            val index=bundle.getInt(GetAndShowNewsItemFragment.keyForPosition)
            val timeSpent=bundle.getLong(GetAndShowNewsItemFragment.keyForTimeSpent)
            if (newBodyText != null) {
                val list = draftAndCompleteViewModel.returnNewsList().toMutableList()
                for (i in list.indices){
                    if (list[i].id==index){
                        val heading= list[i].heading
                        val newTimeSpent=list[i].timeSpent+timeSpent
                        list[i]=News(index, heading = heading,newBodyText,newTimeSpent,NewsItemType.DRAFT)
                        adapter.setData(list)
                        draftAndCompleteViewModel.updateDetailsForDraftItemInDraftList(index,newBodyText,newTimeSpent)
                    }
                }
            }
        }

        //FRAGMENT RESULT LISTENER FOR FINISH BUTTON EVENT
        parentFragmentManager.setFragmentResultListener(GetAndShowNewsItemFragment.updateCompleteAndDraftItemEvent, this) { _, bundle ->
            val newBodyText=bundle.getString(GetAndShowNewsItemFragment.keyForBodyText)
            val index=bundle.getInt(GetAndShowNewsItemFragment.keyForPosition)
            val timeSpent=bundle.getLong(GetAndShowNewsItemFragment.keyForTimeSpent)
            val heading=bundle.getString(GetAndShowNewsItemFragment.keyForHeading)
            if(heading!=null && newBodyText!=null){
                val list = draftAndCompleteViewModel.returnNewsList().toMutableList()
                var newTimeSpent:Long
                for (i in list.indices){
                    if (list[i].id==index&&list[i].type==NewsItemType.DRAFT){
                        val existingHeading= list[i].heading
                        newTimeSpent=list[i].timeSpent+timeSpent
                        list.removeAt(i)
                        list.add(News(index, heading = existingHeading,newBodyText,newTimeSpent,NewsItemType.COMPLETE))
                        draftAndCompleteViewModel.addCompleteItemInCompleteList(News(index,heading,newBodyText,newTimeSpent,NewsItemType.COMPLETE))
                        draftAndCompleteViewModel.removeDraftItemFromDraftList(index)
                        adapter.setData(list)
                    }
                }
            }
        }

        //FRAGMENT RESULT LISTENER FOR BACK BUTTON EVENT
        parentFragmentManager.setFragmentResultListener(GetAndShowNewsItemFragment.updateTimeSpentEvent, this) { _, bundle ->
            val index=bundle.getInt(GetAndShowNewsItemFragment.keyForPosition)
            val timeSpent=bundle.getLong(GetAndShowNewsItemFragment.keyForTimeSpent)
            val list = draftAndCompleteViewModel.returnNewsList().toList().toMutableList()
            for (i in list.indices){
                if (list[i].id==index){
                    val existingHeading= list[i].heading
                    val existingBodyText=list[i].body
                    val newTimeSpent=list[i].timeSpent+timeSpent
                    list[i]=News(index, heading = existingHeading,existingBodyText,newTimeSpent,NewsItemType.DRAFT)
                    adapter.setData(list)
                    draftAndCompleteViewModel.updateDetailsForDraftItemInDraftList(index,null,timeSpent)
                }
            }
        }
        return binding.root
    }

    //RECYCLER ITEM CLICK LISTENER(INTERFACE)
    override fun onClickListener(boolean: Boolean,index:Int,heading:String,body:String,timeSpent:Long) {
        val orientation = this.resources.configuration.orientation
        val getAndShowNewsItemFragment=GetAndShowNewsItemFragment()
        if(orientation==Configuration.ORIENTATION_PORTRAIT){
            if (parentFragmentManager.findFragmentByTag(fragmentDisplayKey)!=null){
                parentFragmentManager.popBackStack()
            }
            val bundle=Bundle()
            bundle.putBoolean(bundleKeyForIsDraft,boolean)
            bundle.putString(bundleKeyForBody,body)
            bundle.putLong(bundleKeyForTimeSpent,timeSpent)
            bundle.putInt(bundleKeyForPosition,index)
            bundle.putString(bundleKeyForHeading,heading)
            getAndShowNewsItemFragment.arguments=bundle
            parentFragmentManager.beginTransaction().replace(R.id.container1,getAndShowNewsItemFragment,
                fragmentDisplayKey).addToBackStack(
                fragmentDisplayKey).commit()
        }else{
            if (parentFragmentManager.findFragmentByTag(fragmentDisplayKey)!=null){
                parentFragmentManager.popBackStack()
            }
            val bundle=Bundle()
            bundle.putBoolean(bundleKeyForIsDraft,boolean)
            bundle.putString(bundleKeyForBody,body)
            bundle.putLong(bundleKeyForTimeSpent,timeSpent)
            bundle.putInt(bundleKeyForPosition,index)
            bundle.putString(bundleKeyForHeading,heading)
            getAndShowNewsItemFragment.arguments=bundle
            parentFragmentManager.beginTransaction().replace(R.id.container2,getAndShowNewsItemFragment,
                fragmentDisplayKey).addToBackStack(
                fragmentDisplayKey).commit()
        }
    }

    companion object {
        const val fragmentDisplayKey="fragment"
        const val bundleKeyForIsDraft="bundle_key_for_is_Draft"
        const val bundleKeyForBody="bundle_key_for_body"
        const val bundleKeyForTimeSpent="bundle_key_for_time_spent"
        const val bundleKeyForPosition="bundle_key_for_position"
        const val bundleKeyForHeading="bundle_key_for_heading"
    }
}