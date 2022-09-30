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
import com.admaja.storyappsubmission.view.login.LoginActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory: MainViewModelFactory = MainViewModelFactory.getInstance(this)
        val mainViewModel: MainViewModel by viewModels {
            factory
        }

        val storyListAdapter = StoryListAdapter()
        val auth = "Bearer "+UserPreference(this).getUser().token
        mainViewModel.setAuth(auth)
        mainViewModel.getStories().observe(this) {
            if (it != null) {
                when(it) {
                    is Result.Loading -> {
                        binding.layoutForLoading.root.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding.layoutForLoading.root.visibility = View.GONE
                        val storyData = it.data
                        storyListAdapter.submitList(storyData)
                    }
                    is Result.Error -> {
                        binding.layoutForLoading.root.visibility = View.GONE
                        Toast.makeText(this, R.string.error_message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        binding.apply {
            rvListStory.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
            rvListStory.setHasFixedSize(true)
            rvListStory.adapter = storyListAdapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.logout -> {
                Intent(this@MainActivity, LoginActivity::class.java).apply {
                    startActivity(this)
                    finishAffinity()
                    UserPreference(this@MainActivity).clearUserPreference()
                }
                return true
            }
            else -> return false
        }
    }
}