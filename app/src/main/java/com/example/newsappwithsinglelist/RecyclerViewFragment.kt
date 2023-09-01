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
    private var recyclerViewDataList= listOf<News>()
    private lateinit var draftAndCompleteViewModel: DraftAndCompleteViewModel
    private lateinit var adapter:NewsRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentRecyclerViewBinding.inflate(inflater, container, false)
        binding.newsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        draftAndCompleteViewModel= ViewModelProvider(requireActivity())[DraftAndCompleteViewModel::class.java]
        //SETTING RECYCLER VIEW ADAPTER
        recyclerViewDataList=draftAndCompleteViewModel.returnNewsList().toList()
        adapter = NewsRecyclerViewAdapter(recyclerViewDataList,this)
        binding.newsRecyclerView.adapter=adapter

        //FRAGMENT RESULT LISTENER FOR SAVE BUTTON EVENT
        parentFragmentManager.setFragmentResultListener(GetAndShowNewsItemFragment.updateDraftItemEvent, this) { _, bundle ->
            val newBodyText=bundle.getString(GetAndShowNewsItemFragment.keyForBodyText)
            val index=bundle.getInt(GetAndShowNewsItemFragment.keyForPosition)
            val timeSpent=bundle.getLong(GetAndShowNewsItemFragment.keyForTimeSpent)
            newBodyText?.let {
                draftAndCompleteViewModel.updateDetailsForDraftItemInNewsList(index,
                    it,timeSpent)
            }
            val newList:List<News> = draftAndCompleteViewModel.returnNewsList().toList()
            adapter.setData(newList)
        }

        //FRAGMENT RESULT LISTENER FOR FINISH BUTTON EVENT
        parentFragmentManager.setFragmentResultListener(GetAndShowNewsItemFragment.updateCompleteAndDraftItemEvent, this) { _, bundle ->
            val newBodyText=bundle.getString(GetAndShowNewsItemFragment.keyForBodyText)
            val position=bundle.getInt(GetAndShowNewsItemFragment.keyForPosition)
            val timeSpent=bundle.getLong(GetAndShowNewsItemFragment.keyForTimeSpent)
            val heading=bundle.getString(GetAndShowNewsItemFragment.keyForHeading)
            if(heading!=null && newBodyText!=null){
                draftAndCompleteViewModel.updateDetailsForDraftItemInNewsList(position,null,timeSpent)
                val draftItem=draftAndCompleteViewModel.returnDraftItemFromNewsList(position)
                draftAndCompleteViewModel.removeDraftItemFromNewsList(position)
                if (draftItem != null) {
                    draftAndCompleteViewModel.addCompleteItemInNewsList(News(position,heading,newBodyText,draftItem.timeSpent,NewsItemType.COMPLETE))
                }
            }
            val newList:List<News> = draftAndCompleteViewModel.returnNewsList().toList()
            adapter.setData(newList)
        }

        //FRAGMENT RESULT LISTENER FOR BACK BUTTON EVENT
        parentFragmentManager.setFragmentResultListener(GetAndShowNewsItemFragment.updateTimeSpentEvent, this) { _, bundle ->
            val position=bundle.getInt(GetAndShowNewsItemFragment.keyForPosition)
            val timeSpent=bundle.getLong(GetAndShowNewsItemFragment.keyForTimeSpent)
            draftAndCompleteViewModel.updateDetailsForDraftItemInNewsList(position,null,timeSpent)
            val newList:List<News> = draftAndCompleteViewModel.returnNewsList().toList()
            adapter.setData(newList)
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
        const val fragmentDraftKey = "fragment_1"
        const val fragmentCompleteKey = "fragment_2"
        const val fragmentDisplayKey="fragment"
        const val bundleKeyForIsDraft="bundle_key_for_is_Draft"
        const val bundleKeyForBody="bundle_key_for_body"
        const val bundleKeyForTimeSpent="bundle_key_for_time_spent"
        const val bundleKeyForPosition="bundle_key_for_position"
        const val bundleKeyForHeading="bundle_key_for_heading"
    }
}