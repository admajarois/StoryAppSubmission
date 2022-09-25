package com.admaja.storyappsubmission.view.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.admaja.storyappsubmission.R
import com.admaja.storyappsubmission.databinding.ActivityLoginBinding
import com.admaja.storyappsubmission.view.main.MainActivity
import com.admaja.storyappsubmission.view.signup.SignupActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvToRegister.setOnClickListener {
            Intent(this@LoginActivity, SignupActivity::class.java).apply {
                startActivity(this)
            }
        }

        binding.buttonLogin.setOnClickListener {
            Intent(this@LoginActivity, MainActivity::class.java).apply {
                startActivity(this)
            }
        }
    }
}