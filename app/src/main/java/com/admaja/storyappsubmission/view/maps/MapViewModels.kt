package com.admaja.storyappsubmission.view.maps

import androidx.lifecycle.ViewModel
import com.admaja.storyappsubmission.data.DataRepository

class MapViewModels(private val dataRepository: DataRepository): ViewModel() {
    fun getStoryLocation() = dataRepository.getStoryFromDatabase()
}