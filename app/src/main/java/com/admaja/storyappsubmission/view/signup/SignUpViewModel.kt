package com.admaja.storyappsubmission.view.signup

import androidx.lifecycle.ViewModel
import com.admaja.storyappsubmission.data.DataRepository

class SignUpViewModel(private val dataRepository: DataRepository): ViewModel() {
    fun register(name: String, email: String, password: String) = dataRepository.doRegister(name, email, password)
}