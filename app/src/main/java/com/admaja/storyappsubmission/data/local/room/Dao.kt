package com.admaja.storyappsubmission.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.admaja.storyappsubmission.data.local.entity.StoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {

    @Query("SELECT * FROM story_entity")
    fun getStories(): Flow<List<StoryEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertStory(storyEntity: List<StoryEntity>)

    @Query("DELETE FROM story_entity WHERE id = :id")
    suspend fun deleteStory(id: String)
}