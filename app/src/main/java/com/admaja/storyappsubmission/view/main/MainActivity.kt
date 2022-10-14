package com.admaja.storyappsubmission.view.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.admaja.storyappsubmission.R
import com.admaja.storyappsubmission.data.Result
import com.admaja.storyappsubmission.data.local.preferences.UserPreference
import com.admaja.storyappsubmission.databinding.ActivityMainBinding
import com.admaja.storyappsubmission.view.adapter.StoryListAdapter
import com.admaja.storyappsubmission.view.add.AddStoryActivity
import com.admaja.storyappsubmission.view.login.LoginActivity
import com.admaja.storyappsubmission.view.maps.MapsActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val adapter = StoryListAdapter()
        val factory: MainViewModelFactory = MainViewModelFactory.getInstance(this)
        val mainViewModel: MainViewModel by viewModels { factory }
        mainViewModel.story.observe(this) {
            adapter.submitData(lifecycle, it)
        }
        binding.apply {
            rvListStory.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
            rvListStory.setHasFixedSize(true)
            fabAddStory.setOnClickListener {
                Intent(this@MainActivity, AddStoryActivity::class.java).apply {
                    startActivity(this)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.logout -> {
                Intent(this@MainActivity, LoginActivity::class.java).apply {
                    startActivity(this)
                    finishAffinity()
                    UserPreference(this@MainActivity).clearUserPreference()
                }
                true
            }
            R.id.map -> {
                Intent(this@MainActivity, MapsActivity::class.java).apply {
                    startActivity(this)
                }
                true
            }
            else -> return false
        }
    }

    companion object {
        const val EXTRA_TOKEN = "token"
    }

}