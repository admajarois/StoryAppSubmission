package com.admaja.storyappsubmission.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.admaja.storyappsubmission.data.local.entity.StoryEntity
import com.admaja.storyappsubmission.data.local.room.Dao
import com.admaja.storyappsubmission.utils.StoryPagingSource

class FakeStoryDao: Dao {

    private var storyData = mutableListOf<StoryEntity>()

    override fun getStories(): PagingSource<Int, StoryEntity> {
        return StoryPagingSource<StoryEntity>(storyData)
    }

    override fun getStoryFromDatabase(): LiveData<List<StoryEntity>> {
        val observableStory = MutableLiveData<List<StoryEntity>>()
        observableStory.value = storyData
        return observableStory
    }

    override suspend fun getStoriesForWidget(): List<StoryEntity> {
        val observableWidgetData = mutableListOf<StoryEntity>()
        observableWidgetData.addAll(storyData)
        return observableWidgetData
    }

    override suspend fun insertStory(storyEntity: List<StoryEntity>) {
        storyData.addAll(storyEntity)
    }

    override suspend fun deleteAll() {
        storyData.clear()
    }
}