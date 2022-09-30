package com.admaja.storyappsubmission.view.add

import androidx.lifecycle.ViewModel
import com.admaja.storyappsubmission.data.DataRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddStoryViewModel(private val dataRepository: DataRepository): ViewModel() {

    fun addStory(
        auth: String?,
        description: RequestBody,
        photo: MultipartBody.Part,
        lat: RequestBody?,
        lon: RequestBody?) = dataRepository.addNewStory(auth, description, photo, lat, lon)
}