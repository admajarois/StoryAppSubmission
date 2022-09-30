package com.admaja.storyappsubmission.di

import android.content.Context
import com.admaja.storyappsubmission.data.DataRepository
import com.admaja.storyappsubmission.data.local.preferences.UserPreference
import com.admaja.storyappsubmission.data.local.room.StoryDatabase
import com.admaja.storyappsubmission.data.remote.config.ApiConfig
import com.admaja.storyappsubmission.utils.AppExecutors

object Injection {
    fun provideRepository(context: Context): DataRepository {
        val apiService = ApiConfig.getApiService()
        val database = StoryDatabase.getInstance(context)
        val dao = database.dao()
        val userPreference = UserPreference(context)
        val appExecutors = AppExecutors()
        return DataRepository.getInstance(apiService, dao, userPreference, appExecutors)
    }
}