package com.admaja.storyappsubmission.view.signup

import androidx.lifecycle.ViewModel
import com.admaja.storyappsubmission.data.DataRepository

class SignUpViewModel(private val dataRepository: DataRepository): ViewModel() {

    private var name:String = ""
    private var email:String = ""
    private var password:String = ""

    fun setUser(name: String, email: String, password: String) {
        this.name = name
        this.email = email
        this.password = password
    }

    fun register() = dataRepository.register(name, email, password)
}