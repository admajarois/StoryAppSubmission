package com.admaja.storyappsubmission.data.local.room

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.admaja.storyappsubmission.data.local.entity.StoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {

    @Query("SELECT * FROM story_entity ORDER BY createdAt DESC")
    fun getStories(): PagingSource<Int, StoryEntity>

    @Query("SELECT * FROM story_entity")
    fun getStoryFromDatabase(): LiveData<List<StoryEntity>>

    @Query("SELECT * FROM story_entity ORDER BY createdAt DESC")
    suspend fun getStoriesForWidget(): List<StoryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(storyEntity: List<StoryEntity>)

    @Query("DELETE FROM story_entity")
    suspend fun deleteAll()
}