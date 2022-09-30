package com.admaja.storyappsubmission.view.main

import androidx.lifecycle.ViewModel
import com.admaja.storyappsubmission.data.DataRepository

class MainViewModel(private val dataRepository: DataRepository): ViewModel() {

    private var location:String? = null
    private var page: String? = null
    private var size: String? = null

    fun setStories(location: String?, page: String?, size: String?) {
        this.location = location
        this.page = page
        this.size = size
    }

    fun getStories() = dataRepository.getStory(location, page, size)
}