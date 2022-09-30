package com.admaja.storyappsubmission.view.main

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.admaja.storyappsubmission.data.DataRepository
import com.admaja.storyappsubmission.di.Injection

class MainViewModelFactory private constructor(private val dataRepository: DataRepository):
    ViewModelProvider.NewInstanceFactory(){
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(dataRepository) as T

        }
        throw IllegalArgumentException("Unknown view model class : ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var instance: MainViewModelFactory? = null
        fun getInstance(context: Context): MainViewModelFactory =
            instance?: synchronized(this) {
                instance?: MainViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}