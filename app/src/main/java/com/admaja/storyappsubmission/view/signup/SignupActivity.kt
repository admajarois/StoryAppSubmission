package com.admaja.storyappsubmission.view.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.admaja.storyappsubmission.R
import com.admaja.storyappsubmission.data.Result
import com.admaja.storyappsubmission.databinding.ActivitySignupBinding
import com.admaja.storyappsubmission.view.custom.MyCustomEditText
import com.admaja.storyappsubmission.view.login.LoginActivity

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory: SignUpViewModelFactory = SignUpViewModelFactory.getInstance(this)
        val signUpViewModel: SignUpViewModel by  viewModels {
            factory
        }
        binding.apply {
            etInputName.globalChange()
            etInputEmail.globalChange()
            etInputPassword.globalChange()
            buttonRegister.setOnClickListener {
                signUpViewModel.setUser(etInputName.text.toString(),
                etInputEmail.text.toString(), etInputPassword.text.toString())
                signUpViewModel.register().observe(this@SignupActivity) {
                    if (it != null) {
                        when(it) {
                            is Result.Loading -> {
                                layoutForLoading.root.visibility = View.VISIBLE
                            }
                            is Result.Success -> {
                                layoutForLoading.root.visibility = View.GONE
                                Toast.makeText(this@SignupActivity, it.data.message, Toast.LENGTH_SHORT).show()
                                Intent(this@SignupActivity, LoginActivity::class.java).apply {
                                    startActivity(this)
                                }
                            }
                            is Result.Error -> {
                                layoutForLoading.root.visibility = View.GONE
                                Toast.makeText(this@SignupActivity, R.string.error_message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }
        playAnimation()
    }

    private fun playAnimation() {
        binding.apply {
            val view = ObjectAnimator.ofFloat(view, View.ALPHA, 1f).setDuration(500)
            val greeting = ObjectAnimator.ofFloat(greetingLogin,View.ALPHA, 1f).setDuration(500)
            val greetingTwo = ObjectAnimator.ofFloat(missedLogin,View.ALPHA, 1f).setDuration(500)
            val image = ObjectAnimator.ofFloat(imageView2,View.ALPHA, 1f).setDuration(500)
            val nameLabel = ObjectAnimator.ofFloat(usernameLabelRegister,View.ALPHA, 1f).setDuration(500)
            val nameEditText = ObjectAnimator.ofFloat(textInputLayout,View.ALPHA, 1f).setDuration(500)
            val emailLabel = ObjectAnimator.ofFloat(emailLabelRegister,View.ALPHA, 1f).setDuration(500)
            val emailEditText = ObjectAnimator.ofFloat(textInputLayout2,View.ALPHA, 1f).setDuration(500)
            val passwordLabel = ObjectAnimator.ofFloat(passwordLabelRegister,View.ALPHA, 1f).setDuration(500)
            val passwordEditText = ObjectAnimator.ofFloat(textInputLayout3,View.ALPHA, 1f).setDuration(500)
            val registerButton = ObjectAnimator.ofFloat(buttonRegister,View.ALPHA, 1f).setDuration(500)

            val viewTogether = AnimatorSet().apply {
                playTogether(view, image, greeting, greetingTwo)
            }

            val playNameTogether = AnimatorSet().apply {
                playTogether(nameLabel, nameEditText)
            }

            val playEmailTogether = AnimatorSet().apply {
                playTogether(emailLabel, emailEditText)
            }

            val playPasswordTogether = AnimatorSet().apply {
                playTogether(passwordLabel, passwordEditText)
            }

            AnimatorSet().apply {
                playSequentially(viewTogether, playNameTogether, playEmailTogether, playPasswordTogether, registerButton)
                start()
            }
        }
    }

    private fun MyCustomEditText.globalChange() {
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                with(binding) {
                    buttonRegister.isEnabled = etInputName.isValid == true && etInputPassword.isValid == true && etInputEmail.isValid == true
                }
            }

        })
    }
}