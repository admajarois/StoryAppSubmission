package com.admaja.storyappsubmission.view.main

import androidx.lifecycle.ViewModel
import com.admaja.storyappsubmission.data.DataRepository

class MainViewModel(private val dataRepository: DataRepository): ViewModel() {

    private var auth:String? = null

    fun setAuth(auth:String?) {
        this.auth = auth
    }

    fun getStories() = dataRepository.getStory(auth, null)
}