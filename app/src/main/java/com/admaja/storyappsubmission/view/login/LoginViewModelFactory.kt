package com.admaja.storyappsubmission.view.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.admaja.storyappsubmission.data.DataRepository
import com.admaja.storyappsubmission.di.Injection

class LoginViewModelFactory private constructor(private val dataRepository: DataRepository):
    ViewModelProvider.NewInstanceFactory(){
    @Suppress("UNCHEKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(dataRepository) as T
        }
        throw IllegalArgumentException("Unknown View Model class : ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var instance: LoginViewModelFactory? = null
        fun getInstance(context: Context): LoginViewModelFactory =
            instance?: synchronized(this) {
                instance?: LoginViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}