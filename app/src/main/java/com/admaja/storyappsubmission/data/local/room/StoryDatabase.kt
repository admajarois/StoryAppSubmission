package com.admaja.storyappsubmission.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.admaja.storyappsubmission.data.local.entity.StoryEntity
import com.admaja.storyappsubmission.utils.DATABASE_NAME
import com.admaja.storyappsubmission.utils.DATABASE_VERSION

@Database(entities = [StoryEntity::class], version = DATABASE_VERSION, exportSchema = false)
abstract class StoryDatabase: RoomDatabase() {
    abstract fun dao(): Dao

    companion object {
        @Volatile
        private var instance: StoryDatabase? = null
        fun getInstance(context: Context): StoryDatabase =
            instance?: Room.databaseBuilder(
                context.applicationContext,
                StoryDatabase::class.java, DATABASE_NAME
            ).build()
    }
}