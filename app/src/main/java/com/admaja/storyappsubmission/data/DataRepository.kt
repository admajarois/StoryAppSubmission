package com.admaja.storyappsubmission.data


import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.paging.*
import com.admaja.storyappsubmission.data.local.entity.StoryEntity
import com.admaja.storyappsubmission.data.local.preferences.UserPreference
import com.admaja.storyappsubmission.data.local.room.Dao
import com.admaja.storyappsubmission.data.local.room.StoryDatabase
import com.admaja.storyappsubmission.data.paging.StoryRemoteMediator
import com.admaja.storyappsubmission.data.remote.config.ApiService
import com.admaja.storyappsubmission.data.remote.response.LoginResponse
import com.admaja.storyappsubmission.data.remote.response.BasicResponse
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
    private val storyDatabase: StoryDatabase,
    private val appExecutors: AppExecutors
) {
    private val loginResult = MediatorLiveData<Result<LoginResponse>>()

    private val basicResult = MediatorLiveData<Result<BasicResponse>>()

    fun addNewStory(
        description: RequestBody,
        photo: MultipartBody.Part,
        lat: RequestBody?,
        lon: RequestBody?
    ): LiveData<Result<BasicResponse>> {
        basicResult.value = Result.Loading
        val client = apiService.addStories(description, photo, lat, lon)
        client.enqueue(object : Callback<BasicResponse> {
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

    fun getStory(): LiveData<PagingData<StoryEntity>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(pageSize = 1),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService),
            pagingSourceFactory = {
                storyDatabase.dao().getStories()
            }
        ).liveData
    }

    fun register(
        name: String?,
        email: String?,
        password: String?
    ): LiveData<Result<BasicResponse>> {
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

    fun login(email: String, password: String): LiveData<Result<LoginResponse>> {
        loginResult.value = Result.Loading
        val client = apiService.login(email, password)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val user = response.body()
                    if (user != null) {
                        userPreference.setUser(user.loginResult)
                        loginResult.value = Result.Success(user)
                    }
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                loginResult.value = Result.Error(t.message.toString())
            }
        })
        return loginResult
    }

    companion object {
        @Volatile
        private var instance: DataRepository? = null
        fun getInstance(
            apiService: ApiService,
            dao: Dao,
            userPreference: UserPreference,
            userDatabase: StoryDatabase,
            appExecutors: AppExecutors
        ): DataRepository =
            instance ?: synchronized(this) {
                instance ?: DataRepository(apiService, dao, userPreference, userDatabase, appExecutors)
            }.also { instance = it }
    }

}