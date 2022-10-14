package com.admaja.storyappsubmission.view.login

import androidx.lifecycle.ViewModel
import com.admaja.storyappsubmission.data.DataRepository

class LoginViewModel(private val dataRepository: DataRepository): ViewModel() {

    private var email = ""
    private var password = ""

    fun setLoginParameter(email: String, password: String) {
        this.email = email
        this.password = password
    }

    fun login() = dataRepository.login(email, password)

}