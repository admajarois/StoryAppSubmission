package com.admaja.storyappsubmission.data.remote.config

import com.admaja.storyappsubmission.data.remote.response.LoginResponse
import com.admaja.storyappsubmission.data.remote.response.BasicResponse
import com.admaja.storyappsubmission.data.remote.response.ListStoryItem
import com.admaja.storyappsubmission.data.remote.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name: String?,
        @Field("email") email: String?,
        @Field("password") password: String?
    ): Call<BasicResponse>

    @GET("stories")
    suspend fun getStories(
        @Query("page") page : Int?,
        @Query("size") size : Int?,
        @Query("location") location : Int = 1
    ): StoryResponse

    @Multipart
    @POST("stories")
    fun addStories(
        @Part("description") description: RequestBody,
        @Part file: MultipartBody.Part,
        @Part("lat") lat: RequestBody?,
        @Part("lon") lon: RequestBody?
    ): Call<BasicResponse>
}