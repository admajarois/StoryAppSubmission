package com.admaja.storyappsubmission.view.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
                            is Result.Loading -> {}
                            is Result.Success -> {
                                Toast.makeText(this@SignupActivity, it.data.message, Toast.LENGTH_SHORT).show()
                                Intent(this@SignupActivity, LoginActivity::class.java).apply {
                                    startActivity(this)
                                }
                            }
                            is Result.Error -> {
                                Toast.makeText(this@SignupActivity, R.string.error_message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
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