package com.admaja.storyappsubmission.view.maps

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.admaja.storyappsubmission.data.DataRepository
import com.admaja.storyappsubmission.di.Injection
import com.admaja.storyappsubmission.view.main.MainViewModelFactory

class MapsViewModelFactory private constructor(private val dataRepository: DataRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MapViewModels::class.java)) {
            return MapViewModels(dataRepository) as T
        }
        throw IllegalArgumentException("Unknown view model class : ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var instance: MapsViewModelFactory? = null
        fun getInstance(context: Context): MapsViewModelFactory =
            instance?: synchronized(this) {
                instance?: MapsViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}