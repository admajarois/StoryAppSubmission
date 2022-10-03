package com.admaja.storyappsubmission.view.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.constraintlayout.motion.widget.MotionLayout
import com.admaja.storyappsubmission.R
import com.admaja.storyappsubmission.data.Result
import com.admaja.storyappsubmission.data.local.preferences.UserPreference
import com.admaja.storyappsubmission.databinding.ActivityLoginBinding
import com.admaja.storyappsubmission.view.custom.MyCustomEditText
import com.admaja.storyappsubmission.view.main.MainActivity
import com.admaja.storyappsubmission.view.signup.SignupActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val factory: LoginViewModelFactory = LoginViewModelFactory.getInstance(this)
        val loginViewModel:LoginViewModel by viewModels {
            factory
        }

        binding.apply {
            emailEditText.globalChange()
            passwordEditText.globalChange()
            buttonLogin.setOnClickListener {
                loginViewModel.setLoginParamenter(emailEditText.text.toString(), passwordEditText.text.toString())
                loginViewModel.login().observe(this@LoginActivity) {
                    if (it != null) {
                        when (it) {
                            is Result.Loading -> {
                                layoutLoadingLogin.root.visibility = View.VISIBLE
                            }
                            is Result.Success -> {
                                layoutLoadingLogin.root.visibility = View.GONE
                                if (it.data.error) {
                                    Toast.makeText(this@LoginActivity, R.string.login_failed, Toast.LENGTH_SHORT).show()
                                }
                                Toast.makeText(this@LoginActivity, R.string.login_success, Toast.LENGTH_SHORT).show()
                                goHome()
                            }
                            is Result.Error -> {
                                layoutLoadingLogin.root.visibility = View.GONE
                                Toast.makeText(this@LoginActivity, R.string.error_message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }
        binding.tvToRegister.setOnClickListener {
            Intent(this@LoginActivity, SignupActivity::class.java).apply {
                startActivity(this)
            }
        }
        animationSet()
    }

    private fun MyCustomEditText.globalChange() {
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                with(binding) {
                    buttonLogin.isEnabled = emailEditText.isValid == true && passwordEditText.isValid == true
                }
            }
        })
    }

    private fun goHome() {
        Intent(this@LoginActivity, MainActivity::class.java).apply {
            startActivity(this)
            finish()
        }
    }

    private fun animationSet() {
        binding.root.apply {
            setTransitionListener(object : MotionLayout.TransitionListener {
                override fun onTransitionStarted(
                    motionLayout: MotionLayout?,
                    startId: Int,
                    endId: Int
                ) {
                }

                override fun onTransitionChange(
                    motionLayout: MotionLayout?,
                    startId: Int,
                    endId: Int,
                    progress: Float
                ) {
                }

                override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                    if (currentId == R.id.end) {
                        if(UserPreference(this@LoginActivity).getUser().token != null) {
                            goHome()
                        }
                    }
                }

                override fun onTransitionTrigger(
                    motionLayout: MotionLayout?,
                    triggerId: Int,
                    positive: Boolean,
                    progress: Float
                ) {
                    TODO("Not yet implemented")
                }

            })
        }
    }
}