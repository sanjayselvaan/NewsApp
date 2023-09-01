package com.example.newsappwithsinglelist

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.newsappwithsinglelist.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var draftAndCompleteViewModel: DraftAndCompleteViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setTitle(R.string.news)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val orientation = this.resources.configuration.orientation
        draftAndCompleteViewModel =
            ViewModelProvider(this)[DraftAndCompleteViewModel::class.java]
        if (savedInstanceState == null) {
            //ORIENTATION HANDLE FOR SAVED INSTANCE NULL
            draftAndCompleteViewModel.populateDataInNewsList()
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container1, RecyclerViewFragment())
                    .commit()
            } else {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container1, RecyclerViewFragment())
                    .commit()
            }
        }
        else{
            //ORIENTATION HANDLE FOR SAVED INSTANCE PRESENT
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                supportFragmentManager.findFragmentByTag(RecyclerViewFragment.fragmentDisplayKey)?.let {
                    val getAndShowNewsItemFragment=GetAndShowNewsItemFragment()
                    supportFragmentManager.popBackStack()
                    getAndShowNewsItemFragment.arguments=it.arguments
                    supportFragmentManager.beginTransaction().replace(R.id.container1,getAndShowNewsItemFragment,RecyclerViewFragment.fragmentDisplayKey).addToBackStack(RecyclerViewFragment.fragmentDisplayKey).commit()
                }
            } else {
                supportFragmentManager.findFragmentByTag(RecyclerViewFragment.fragmentDisplayKey)?.let {
                    val getAndShowNewsItemFragment=GetAndShowNewsItemFragment()
                    getAndShowNewsItemFragment.arguments=it.arguments
                    supportFragmentManager.popBackStack()
                    supportFragmentManager.beginTransaction().replace(R.id.container2,getAndShowNewsItemFragment,RecyclerViewFragment.fragmentDisplayKey).addToBackStack(RecyclerViewFragment.fragmentDisplayKey).commit()
                }
            }
        }
    }

}

