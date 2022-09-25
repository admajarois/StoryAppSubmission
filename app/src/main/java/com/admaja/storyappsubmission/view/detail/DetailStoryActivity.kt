package com.admaja.storyappsubmission.view.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.admaja.storyappsubmission.R
import com.admaja.storyappsubmission.databinding.ActivitySignupBinding
import com.admaja.storyappsubmission.view.login.LoginActivity

class DetailStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonRegister.setOnClickListener {
            Intent(this@DetailStoryActivity, LoginActivity::class.java).apply {
                startActivity(this)
            }
        }
    }
}