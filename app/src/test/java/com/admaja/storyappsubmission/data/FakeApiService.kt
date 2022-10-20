package com.admaja.storyappsubmission.data

import com.admaja.storyappsubmission.DataDummy
import com.admaja.storyappsubmission.data.remote.config.ApiService
import com.admaja.storyappsubmission.data.remote.response.BasicResponse
import com.admaja.storyappsubmission.data.remote.response.LoginResponse
import com.admaja.storyappsubmission.data.remote.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

class FakeApiService : ApiService {
    private val dummyStoryResponse = DataDummy.storyResponse()
    private val dummyLogin = DataDummy.loginResponse()
    private val dummyBasicResponse = DataDummy.basicResponse()
    override suspend fun login(email: String, password: String): LoginResponse {
        return dummyLogin
    }

    override suspend fun register(name: String?, email: String?, password: String?): BasicResponse {
        return dummyBasicResponse
    }

    override suspend fun getStories(page: Int?, size: Int?, location: Int?): StoryResponse {
        return dummyStoryResponse
    }

    override suspend fun addStories(
        description: RequestBody,
        file: MultipartBody.Part,
        lat: RequestBody?,
        lon: RequestBody?
    ): BasicResponse {
        return dummyBasicResponse
    }
}