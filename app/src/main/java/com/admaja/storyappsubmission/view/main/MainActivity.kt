package com.admaja.storyappsubmission.view.main

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import android.Manifest
import com.admaja.storyappsubmission.R
import com.admaja.storyappsubmission.data.local.preferences.UserPreference
import com.admaja.storyappsubmission.databinding.ActivityMainBinding
import com.admaja.storyappsubmission.view.adapter.LoadingStateAdapter
import com.admaja.storyappsubmission.view.adapter.StoryListAdapter
import com.admaja.storyappsubmission.view.add.AddStoryActivity
import com.admaja.storyappsubmission.view.login.LoginActivity
import com.admaja.storyappsubmission.view.maps.MapsActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val adapter = StoryListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (!allPermissionGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        binding.fabAddStory.setOnClickListener {
            Intent(this@MainActivity, AddStoryActivity::class.java).apply {
                startActivity(this)
            }
        }
    }

    private fun getData() {
        val viewModelFactory: MainViewModelFactory = MainViewModelFactory.getInstance(this)
        val mainViewModel: MainViewModel by viewModels { viewModelFactory }
        binding.rvListStory.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = this@MainActivity.adapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    this@MainActivity.adapter.retry()
                }
            )
        }
        mainViewModel.story.observe(this) {
            adapter.submitData(lifecycle, it)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionGranted()) {
                Toast.makeText(this, R.string.permissions, Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun allPermissionGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onResume() {
        super.onResume()
        getData()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
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
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

}