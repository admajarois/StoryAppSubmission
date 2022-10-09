package com.admaja.storyappsubmission.view.maps

import android.content.Context
import androidx.lifecycle.ViewModel
import com.admaja.storyappsubmission.data.DataRepository
import com.admaja.storyappsubmission.data.local.preferences.UserPreference

class MapsViewModel(private val dataRepository: DataRepository): ViewModel() {

    fun getToken(context: Context) = UserPreference(context).getUser().token

    fun getStories(auth: String?) = dataRepository.getStory(auth, location = "1")
}