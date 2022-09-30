package com.admaja.storyappsubmission.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.admaja.storyappsubmission.data.local.entity.StoryEntity
import com.admaja.storyappsubmission.data.local.preferences.UserPreference
import com.admaja.storyappsubmission.data.local.room.Dao
import com.admaja.storyappsubmission.data.remote.config.ApiService
import com.admaja.storyappsubmission.data.remote.response.LoginResponse
import com.admaja.storyappsubmission.data.remote.response.LoginResult
import com.admaja.storyappsubmission.data.remote.response.RegisterResponse
import com.admaja.storyappsubmission.data.remote.response.StoryResponse
import com.admaja.storyappsubmission.utils.AppExecutors
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DataRepository private constructor(
    private val apiService: ApiService,
    private val dao: Dao,
    private val userPreference: UserPreference,
    private val appExecutors: AppExecutors
){
    private val loginResult = MediatorLiveData<Result<LoginResult>>()

    private val registerResult = MediatorLiveData<Result<RegisterResponse>>()

    private val storyResult = MediatorLiveData<Result<List<StoryEntity>>>()

    fun getStory(page: String?, size: String?, location: String?): LiveData<Result<List<StoryEntity>>> {
        storyResult.value = Result.Loading
        val client = apiService.getStories(page, size, location)
        client.enqueue(object: Callback<StoryResponse> {
            override fun onResponse(call: Call<StoryResponse>, response: Response<StoryResponse>) {
                if (response.isSuccessful) {
                    val stories = response.body()?.listStory
                    val newStory = ArrayList<StoryEntity>()
                    appExecutors.diskIO.execute {
                        stories?.forEach {story ->
                            val storyEntity = StoryEntity(
                                story.photoUrl,
                                story.createdAt,
                                story.name,
                                story.description,
                                story.lon,
                                story.id,
                                story.lat
                            )
                            newStory.add(storyEntity)
                        }
                        dao.deleteAll()
                        dao.insertStory(newStory)
                    }
                }
            }
            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                storyResult.value = Result.Error(t.message.toString())
            }
        })
        val localData = dao.getStories()
        storyResult.addSource(localData) { newData: List<StoryEntity> ->
            storyResult.value = Result.Success(newData)
        }
        return storyResult
    }

    fun register(name: String?, email: String?, password: String?): LiveData<Result<RegisterResponse>> {
        registerResult.value = Result.Loading
        val client = apiService.register(name, email, password)
        client.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if (response.isSuccessful) {
                    val register = response.body()
                    if (register != null) {
                        registerResult.value = Result.Success(register)
                    }
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                registerResult.value = Result.Error(t.message.toString())
            }
        })
        return registerResult
    }

    fun login(email: String, password: String): LiveData<Result<LoginResult>> {
        loginResult.value = Result.Loading
        val client = apiService.login(email, password)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val user = response.body()?.loginResult
                    userPreference.setUser(user)
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                loginResult.value = Result.Error(t.message.toString())
            }
        })
        val dataUser = userPreference.getUser()
        loginResult.value = Result.Success(dataUser)
        return loginResult
    }

    companion object {
        @Volatile
        private var instance: DataRepository? = null
        fun getInstance(
            apiService: ApiService,
            dao: Dao,
            userPreference: UserPreference,
            appExecutors: AppExecutors
        ): DataRepository =
            instance ?: synchronized(this) {
                instance?: DataRepository(apiService, dao, userPreference, appExecutors)
            }.also { instance = it }
    }

}