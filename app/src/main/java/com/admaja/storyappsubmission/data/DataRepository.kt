package com.admaja.storyappsubmission.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.admaja.storyappsubmission.data.local.entity.StoryEntity
import com.admaja.storyappsubmission.data.local.preferences.UserPreference
import com.admaja.storyappsubmission.data.local.room.Dao
import com.admaja.storyappsubmission.data.remote.config.ApiService
import com.admaja.storyappsubmission.data.remote.response.LoginResponse
import com.admaja.storyappsubmission.data.remote.response.LoginResult
import com.admaja.storyappsubmission.data.remote.response.BasicResponse
import com.admaja.storyappsubmission.data.remote.response.StoryResponse
import com.admaja.storyappsubmission.utils.AppExecutors
import okhttp3.MultipartBody
import okhttp3.RequestBody
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

    private val basicResult = MediatorLiveData<Result<BasicResponse>>()

    private val storyResult = MediatorLiveData<Result<List<StoryEntity>>>()

    fun addNewStory(auth: String?, description: RequestBody, photo: MultipartBody.Part, lat: RequestBody?, lon: RequestBody?): LiveData<Result<BasicResponse>> {
        basicResult.value = Result.Loading
        val client = apiService.addStories(auth, description, photo, lat, lon)
        client.enqueue(object: Callback<BasicResponse> {
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                if (response.isSuccessful) {
                    val basicResponse = response.body()
                    if (basicResponse != null) {
                        basicResult.value = Result.Success(basicResponse)
                    }
                }
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                basicResult.value = Result.Error(t.message.toString())
            }
        })
        return basicResult
    }

    fun getStory(auth: String?): LiveData<Result<List<StoryEntity>>> {
        storyResult.value = Result.Loading
        val client = apiService.getStories(auth)
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

    fun register(name: String?, email: String?, password: String?): LiveData<Result<BasicResponse>> {
        basicResult.value = Result.Loading
        val client = apiService.register(name, email, password)
        client.enqueue(object : Callback<BasicResponse> {
            override fun onResponse(
                call: Call<BasicResponse>,
                response: Response<BasicResponse>
            ) {
                if (response.isSuccessful) {
                    val register = response.body()
                    if (register != null) {
                        basicResult.value = Result.Success(register)
                    }
                }
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                basicResult.value = Result.Error(t.message.toString())
            }
        })
        return basicResult
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