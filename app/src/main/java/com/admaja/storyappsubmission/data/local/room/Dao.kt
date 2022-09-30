package com.admaja.storyappsubmission.data.local.room

import androidx.lifecycle.LiveData
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
    fun getStories(): LiveData<List<StoryEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertStory(storyEntity: List<StoryEntity>)

    @Query("DELETE FROM story_entity WHERE id = :id")
    suspend fun deleteStory(id: String)

    @Query("DELETE FROM story_entity")
    fun deleteAll()
}