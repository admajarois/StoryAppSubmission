package com.admaja.storyappsubmission.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.admaja.storyappsubmission.data.DataRepository
import com.admaja.storyappsubmission.data.local.entity.StoryEntity

class MainViewModel(dataRepository: DataRepository): ViewModel() {

    val story: LiveData<PagingData<StoryEntity>> = dataRepository.getStory().cachedIn(viewModelScope)
}