package com.admaja.storyappsubmission.view.signup

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.admaja.storyappsubmission.data.DataRepository
import com.admaja.storyappsubmission.di.Injection
import com.admaja.storyappsubmission.view.login.LoginViewModelFactory

class SignUpViewModelFactory private constructor(private val dataRepository: DataRepository):
    ViewModelProvider.NewInstanceFactory(){
    @Suppress("UCHEKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignUpViewModel::class.java)) {
            return SignUpViewModel(dataRepository) as T
        }
        throw IllegalArgumentException("Unknow View Model Class : ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var instance: SignUpViewModelFactory? = null
        fun getInstance(context: Context): SignUpViewModelFactory =
            instance?: synchronized(this) {
                instance?: SignUpViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}