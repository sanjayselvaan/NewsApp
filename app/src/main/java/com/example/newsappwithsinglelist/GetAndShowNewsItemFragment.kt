package com.example.newsappwithsinglelist

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.example.newsappwithsinglelist.databinding.FragmentGetAndShowNewsItemBinding
import java.util.concurrent.TimeUnit

class GetAndShowNewsItemFragment : Fragment() {
    private lateinit var binding: FragmentGetAndShowNewsItemBinding
    private lateinit var callBack: OnBackPressedCallback
    private var startTime: Long = 0
    private var endTime: Long = 0
    private var fragmentResultType: FragmentResultType?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        startTime = System.currentTimeMillis()
        binding = FragmentGetAndShowNewsItemBinding.inflate(inflater, container, false)

        //SETTING THE FRAGMENT ACCORDING TO DRAFT OR COMPLETE NEED
        arguments?.getBoolean(RecyclerViewFragment.bundleKeyForIsDraft)?.let {
            (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
            if (it) {
                setUpForGetNewsItem()
                (activity as MainActivity).supportActionBar?.title = getString(R.string.draft)
            } else {
                setUpForShowNewsItem()
                (activity as MainActivity).supportActionBar?.title = getString(R.string.complete)
            }
        }
        setHasOptionsMenu(true)

        //BACK PRESSED DISPATCHER
        callBack = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                fragmentResultType = FragmentResultType.BACK
                val bundle = loadCommonDetailsForFragmentResultBundle()
                updateActionBar()
                parentFragmentManager.setFragmentResult(updateDraftItemEvent, bundle)
                parentFragmentManager.popBackStack()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callBack)
        return binding.root
    }

    //SETUP FOR GET DRAFT INPUT
    private fun setUpForGetNewsItem() {
        binding.editText.visibility = View.VISIBLE
        binding.saveButton.visibility = View.VISIBLE
        binding.finishButton.visibility = View.VISIBLE

        arguments?.getString(RecyclerViewFragment.bundleKeyForHeading)?.let {
            binding.tvHeading.text = it
        }

        //GETTING FROM EDITTEXT TEXT FROM ARGUMENTS STORED IN SAVED INSTANCE
        if (arguments?.getString(keyForSavedBodyText) != null) {
            arguments?.getString(keyForSavedBodyText)?.let {
                binding.editText.setText(it)
            }
        } else {
            arguments?.getString(RecyclerViewFragment.bundleKeyForBody)?.let {
                binding.editText.setText(it)
            }
        }

        //SAVE BUTTON CLICK LISTENER
        binding.saveButton.setOnClickListener {
            fragmentResultType = FragmentResultType.SAVE
            val bundle = loadCommonDetailsForFragmentResultBundle()
            bundle.putString(keyForBodyText, binding.editText.text.toString())
            updateActionBar()
            parentFragmentManager.setFragmentResult(updateDraftItemEvent, bundle)
            parentFragmentManager.popBackStack()
        }

        //FINISH BUTTON CLICK LISTENER
        binding.finishButton.setOnClickListener {
            fragmentResultType = FragmentResultType.FINISH
            val bundle = loadCommonDetailsForFragmentResultBundle()
            bundle.putString(keyForBodyText, binding.editText.text.toString())
            arguments?.getString(RecyclerViewFragment.bundleKeyForHeading)?.let {
                bundle.putString(keyForHeading, it)
            }
            updateActionBar()
            parentFragmentManager.setFragmentResult(updateCompleteAndDraftItemEvent, bundle)
            parentFragmentManager.popBackStack()
        }
    }

    //SETUP FOR SHOW COMPLETE ITEM
    private fun setUpForShowNewsItem() {
        binding.tvNewsBody.visibility = View.VISIBLE
        binding.tvTotalTimeSpent.visibility = View.VISIBLE

        arguments?.getString(RecyclerViewFragment.bundleKeyForHeading)?.let {
            binding.tvHeading.setText(it)
        }

        arguments?.getLong(RecyclerViewFragment.bundleKeyForTimeSpent)?.let {
            binding.tvTotalTimeSpent.text = convertMillisToHMS(it)
        }

        arguments?.getString(RecyclerViewFragment.bundleKeyForBody)?.let {
            binding.tvNewsBody.text = it
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                callBack.handleOnBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun convertMillisToHMS(milliseconds: Long): String {
        val hours = TimeUnit.MILLISECONDS.toHours(milliseconds)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds) % 60
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    //PUTTING TIME AND EDIT TEXT INTO ARGUMENTS DURING SAVED INSTANCE
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        arguments?.getBoolean(RecyclerViewFragment.bundleKeyForIsDraft)?.let {
            if (it) {
                endTime = System.currentTimeMillis()
                arguments?.putLong(keyForSavedTimeSpent, endTime - startTime)
                arguments?.putString(keyForSavedBodyText, binding.editText.text.toString())
            }
        }
    }

    override fun onPause() {
        super.onPause()
        //PREPARING TO HANDLE TIME IF NEXT LIFECYCLE CALLBACK IS ON RESUME
        Log.d("test1","onPause")
        if (fragmentResultType != FragmentResultType.FINISH && fragmentResultType != FragmentResultType.BACK && fragmentResultType != FragmentResultType.SAVE && fragmentResultType != FragmentResultType.REPLACE) {
            fragmentResultType = FragmentResultType.PAUSE
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("test1","onResume")
        //HANDLE TIME SPENT EVENT ON PAUSE TO ON RESUME CONDITION
        if (fragmentResultType != null) {
            if (fragmentResultType == FragmentResultType.PAUSE) {
                val bundle = Bundle()
                arguments?.getInt(RecyclerViewFragment.bundleKeyForPosition)?.let {
                    bundle.putInt(keyForPosition, it)
                }
                if (arguments?.getLong(keyForSavedTimeSpent) != null) {
                    arguments?.getLong(keyForSavedTimeSpent)?.let {
                        bundle.putLong(keyForTimeSpent, it + (endTime - startTime))
                    }
                }
                parentFragmentManager.setFragmentResult(updateTimeSpentEvent, bundle)
            }
        }
    }

    override fun onStop() {
        //HANDLE TIME SPENT EVENT ON ON STOP CONDITION
        super.onStop()
        if (fragmentResultType != FragmentResultType.FINISH && fragmentResultType != FragmentResultType.BACK && fragmentResultType != FragmentResultType.SAVE && fragmentResultType != FragmentResultType.PAUSE){
            fragmentResultType=FragmentResultType.STOP
            val bundle = loadCommonDetailsForFragmentResultBundle()
            updateActionBar()
            parentFragmentManager.setFragmentResult(updateTimeSpentEvent, bundle)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        //HANDLE TIME SPENT EVENT ON REPLACING THE FRAGMENT
        fragmentResultType=FragmentResultType.REPLACE
        if (fragmentResultType != FragmentResultType.FINISH && fragmentResultType != FragmentResultType.BACK && fragmentResultType != FragmentResultType.SAVE && fragmentResultType != FragmentResultType.PAUSE&&fragmentResultType!=FragmentResultType.STOP) {
            fragmentResultType = FragmentResultType.REPLACE
            val bundle = loadCommonDetailsForFragmentResultBundle()
            updateActionBar()
            parentFragmentManager.setFragmentResult(updateDraftItemEvent, bundle)
        }
    }

    companion object {
        const val updateDraftItemEvent = "save_button_event"
        const val updateCompleteAndDraftItemEvent = "finish_button_event"
        const val updateTimeSpentEvent = "back_button_event"
        const val keyForBodyText = "key_for_body_text"
        const val keyForTimeSpent = "key_for_time_spent"
        const val keyForPosition = "key_for_position"
        const val keyForHeading = "key_for_heading"
        const val keyForSavedTimeSpent = "key_for_saved_time_spent"
        const val keyForSavedBodyText = "key_for_saved_body_text"
    }

    private fun updateActionBar() {
        (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        (activity as MainActivity).supportActionBar?.title = getString(R.string.news)
    }
    private fun loadCommonDetailsForFragmentResultBundle(): Bundle {
        val bundle = Bundle()
        endTime = System.currentTimeMillis()
        arguments?.getInt(RecyclerViewFragment.bundleKeyForPosition)?.let {
            bundle.putInt(keyForPosition, it)
        }
        if (arguments?.getLong(keyForSavedTimeSpent) != null) {
            arguments?.getLong(keyForSavedTimeSpent)?.let {
                bundle.putLong(keyForTimeSpent, it + (endTime - startTime))
            }
        } else {
            bundle.putLong(keyForTimeSpent, endTime - startTime)
        }
        return bundle
    }

}