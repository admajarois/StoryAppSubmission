package com.admaja.storyappsubmission.data


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.*
import com.admaja.storyappsubmission.data.local.entity.StoryEntity
import com.admaja.storyappsubmission.data.local.preferences.UserPreference
import com.admaja.storyappsubmission.data.local.room.Dao
import com.admaja.storyappsubmission.data.local.room.StoryDatabase
import com.admaja.storyappsubmission.data.paging.StoryRemoteMediator
import com.admaja.storyappsubmission.data.remote.config.ApiService
import com.admaja.storyappsubmission.data.remote.response.LoginResponse
import com.admaja.storyappsubmission.data.remote.response.BasicResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

class DataRepository private constructor(
    private val apiService: ApiService,
    private val dao: Dao,
    private val userPreference: UserPreference,
    private val storyDatabase: StoryDatabase,
) {

    fun addNewStory(
        description: RequestBody,
        photo: MultipartBody.Part,
        lat: RequestBody?,
        lon: RequestBody?
    ): LiveData<Result<BasicResponse>>  = liveData{
        emit(Result.Loading)
        try {
            val response = apiService.addStories(description, photo, lat, lon)
            emit(Result.Success(response))

        }catch (e: Exception) {
            Log.d("DataRepository", "addNewStory(): ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getStory(): LiveData<PagingData<StoryEntity>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(pageSize = 5),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService),
            pagingSourceFactory = {
                storyDatabase.dao().getStories()
            }
        ).liveData
    }

    fun getStoryLocation(): LiveData<List<StoryEntity>> {
        return dao.getStoryLocations()
    }

    fun doRegister(
        name: String?,
        email: String?,
        password: String?
    ): LiveData<Result<BasicResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.register(name, email, password)
            emit(Result.Success(response))
        }catch (e: Exception) {
            Log.d("DataRepository", "doRegister(): ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun login(email: String, password: String): LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.login(email, password)
            val loginResult = response.loginResult
            userPreference.setUser(loginResult)
            emit(Result.Success(response))
        } catch (e: Exception) {
            Log.d("DataRepository", "login(): ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    companion object {
        @Volatile
        private var instance: DataRepository? = null
        fun getInstance(
            apiService: ApiService,
            dao: Dao,
            userPreference: UserPreference,
            userDatabase: StoryDatabase,
        ): DataRepository =
            instance ?: synchronized(this) {
                instance ?: DataRepository(apiService, dao, userPreference, userDatabase)
            }.also { instance = it }
    }

}