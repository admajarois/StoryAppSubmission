package com.admaja.storyappsubmission.data.remote.config

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @FormUrlEncoded
    @POST("/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    )

    @FormUrlEncoded
    @POST("/register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    )
//
//    @GET("/stories")
//    suspend fun getStories(
//        @Query("page") page: String?,
//        @Query("size") size: String?,
//        @Query()
//    )
}