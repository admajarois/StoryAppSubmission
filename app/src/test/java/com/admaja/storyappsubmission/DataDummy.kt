package com.admaja.storyappsubmission

import com.admaja.storyappsubmission.data.local.entity.StoryEntity
import com.admaja.storyappsubmission.data.remote.response.BasicResponse
import com.admaja.storyappsubmission.data.remote.response.LoginResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.lang.reflect.Type

object DataDummy {
    fun loginResponse(): LoginResponse {
        val json = """
            {
                "error": false,
                "message": "success",
                "loginResult": {
                    "userId": "user-33xfuCmxS8-VBFeb",
                    "name": "test123",
                    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLTMzeGZ1Q214UzgtVkJGZWIiLCJpYXQiOjE2NjEyMTk0NjN9.aXBuDIsqJVWm-sGClOUFgbT2xU_ROOHEojhjupCLu6Y"
                }
            }
        """.trimIndent()
        return Gson().fromJson(json, LoginResponse::class.java)
    }

    fun basicResponse() = BasicResponse(
        false,
        "User created"
    )

    fun multipartFile() = MultipartBody.Part.create("dummyFile".toRequestBody())

    fun requestBody(text: String) = text.toRequestBody()

//    fun storyInDB(): List<StoryEntity> {
//        val json = """
//
//        """.trimIndent()
//        val listStory: Type = object : TypeToken<List<StoryEntity>>() {}.type
//        return Gson().fromJson(json, listStory)
//    }
}