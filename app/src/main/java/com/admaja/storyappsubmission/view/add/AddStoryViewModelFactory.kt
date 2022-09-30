package com.admaja.storyappsubmission.view.add

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.admaja.storyappsubmission.data.DataRepository
import com.admaja.storyappsubmission.di.Injection

class AddStoryViewModelFactory private constructor(private val dataRepository: DataRepository):
    ViewModelProvider.NewInstanceFactory(){
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddStoryViewModel::class.java)) {
            return AddStoryViewModel(dataRepository) as T
        }
        throw IllegalArgumentException("Unknown view model class : ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var instance: AddStoryViewModelFactory? = null
        fun getInstance(context: Context): AddStoryViewModelFactory =
            instance?: synchronized(this) {
                instance?: AddStoryViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}