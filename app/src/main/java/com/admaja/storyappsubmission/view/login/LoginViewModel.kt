package com.admaja.storyappsubmission.view.login

import androidx.lifecycle.ViewModel
import com.admaja.storyappsubmission.data.DataRepository

class LoginViewModel(private val dataRepository: DataRepository): ViewModel() {

    fun login(email: String, password: String) = dataRepository.login(email, password)

}