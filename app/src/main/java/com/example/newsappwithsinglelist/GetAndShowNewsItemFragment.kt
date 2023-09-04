package com.example.newsappwithsinglelist

import android.os.Bundle
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
    private var fragmentResultType: FragmentResultType? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
                updateActionBar()
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
            updateActionBar()
            parentFragmentManager.popBackStack()
        }

        //FINISH BUTTON CLICK LISTENER
        binding.finishButton.setOnClickListener {
            fragmentResultType = FragmentResultType.FINISH
            updateActionBar()
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

    //PUTTING EDIT TEXT INTO ARGUMENTS DURING SAVED INSTANCE
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        arguments?.getBoolean(RecyclerViewFragment.bundleKeyForIsDraft)?.let {
            if (it) {
                arguments?.putString(keyForSavedBodyText, binding.editText.text.toString())
            }
        }
    }

    override fun onPause() {
        super.onPause()
        arguments?.getBoolean(RecyclerViewFragment.bundleKeyForIsDraft)?.let {
            if (it) {
                val bundle = Bundle()
                val endTime = System.currentTimeMillis()
                arguments?.getInt(RecyclerViewFragment.bundleKeyForPosition)?.let {
                    bundle.putInt(keyForPosition, it)
                }
                bundle.putLong(keyForTimeSpent, endTime - startTime)
                when (fragmentResultType) {
                    FragmentResultType.SAVE -> {
                        bundle.putString(keyForBodyText, binding.editText.text.toString())
                        parentFragmentManager.setFragmentResult(updateDraftItemEvent, bundle)
                    }

                    FragmentResultType.FINISH -> {
                        bundle.putString(keyForBodyText, binding.editText.text.toString())
                        arguments?.getString(RecyclerViewFragment.bundleKeyForHeading)?.let {
                            bundle.putString(keyForHeading, it)
                        }
                        parentFragmentManager.setFragmentResult(
                            updateCompleteAndDraftItemEvent,
                            bundle
                        )
                    }
                    else -> {
                        parentFragmentManager.setFragmentResult(updateTimeSpentEvent, bundle)
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        startTime = System.currentTimeMillis()
    }

    companion object {
        const val updateDraftItemEvent = "save_button_event"
        const val updateCompleteAndDraftItemEvent = "finish_button_event"
        const val updateTimeSpentEvent = "back_button_event"
        const val keyForBodyText = "key_for_body_text"
        const val keyForTimeSpent = "key_for_time_spent"
        const val keyForPosition = "key_for_position"
        const val keyForHeading = "key_for_heading"
        const val keyForSavedBodyText = "key_for_saved_body_text"
    }

    private fun updateActionBar() {
        (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        (activity as MainActivity).supportActionBar?.title = getString(R.string.news)
    }

}