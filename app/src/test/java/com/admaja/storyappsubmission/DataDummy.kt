package com.admaja.storyappsubmission

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
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
                "error":false,
                "message":"success",
                "loginResult": {
                    "userId":"user-kqIXwTa_1qomdafn",
                    "name":"nama",
                    "token":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLWtxSVh3VGFfMXFvbWRhZm4iLCJpYXQiOjE2NjU5MjI2MTR9.O8vIguPE4crGgJhTeyk0ZCSvgB-ziLV3kvtcSXHBQpY"
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

    fun storyInDatabase(): List<StoryEntity> {
        val json = """
        [
            {
                "id": "story-4tpIUcOiIsgP_LXJ",
                "name": "WhoAyem",
                "description": "testimoni lagi",
                "photoUrl": "https://story-api.dicoding.dev/images/stories/photos-1665922125473_UkBIQYcq.jpg",
                "createdAt": "2022-10-16T12:08:45.474Z",
                "lat": null,
                "lon": null
            },
            {
                "id": "story-PclaCsHKc0D3J_ND",
                "name": "WhoAyem",
                "description": "Testimonial",
                "photoUrl": "https://story-api.dicoding.dev/images/stories/photos-1665922056266_9Vm_ZIYY.jpg",
                "createdAt": "2022-10-16T12:07:36.267Z",
                "lat": null,
                "lon": null
            },
            {
                "id": "story-nAKF61J4QitQxatx",
                "name": "reviewer123",
                "description": "testing",
                "photoUrl": "https://story-api.dicoding.dev/images/stories/photos-1665922046935_cO31-d5R.jpg",
                "createdAt": "2022-10-16T12:07:26.935Z",
                "lat": -6.2367933,
                "lon": 106.8442117
            },
            {
                "id": "story-7m44AP-BMmJS5nLp",
                "name": "reviewer123",
                "description": "deskripsi",
                "photoUrl": "https://story-api.dicoding.dev/images/stories/photos-1665921952401_J2Oj7z2g.jpg",
                "createdAt": "2022-10-16T12:05:52.401Z",
                "lat": -6.2367933,
                "lon": 106.8442117
            },
            {
                "id": "story-jh8KDfoXFgE0oFd7",
                "name": "reviewer123",
                "description": "deskripsi",
                "photoUrl": "https://story-api.dicoding.dev/images/stories/photos-1665921927179_oZ2zgoaa.jpg",
                "createdAt": "2022-10-16T12:05:27.179Z",
                "lat": -6.2367933,
                "lon": 106.8442117
            },
            {
                "id": "story-XjLe6hycE_D1qMyx",
                "name": "Tes",
                "description": "siap gan",
                "photoUrl": "https://story-api.dicoding.dev/images/stories/photos-1665921826287_NBAHQ30_.jpg",
                "createdAt": "2022-10-16T12:03:46.287Z",
                "lat": -6.2367933,
                "lon": 106.8442117
            },
            {
                "id": "story-z4AZyjgJrcmxJK4C",
                "name": "Tes",
                "description": "okedeh",
                "photoUrl": "https://story-api.dicoding.dev/images/stories/photos-1665921802210_jmKlFFqo.jpg",
                "createdAt": "2022-10-16T12:03:22.211Z",
                "lat": -6.2367933,
                "lon": 106.8442117
            },
            {
                "id": "story-ZD4ygByawHuDd1Yc",
                "name": "Tes",
                "description": "tesss",
                "photoUrl": "https://story-api.dicoding.dev/images/stories/photos-1665921790833_7iVV2YsE.jpg",
                "createdAt": "2022-10-16T12:03:10.834Z",
                "lat": -6.2367933,
                "lon": 106.8442117
            },
            {
                "id": "story-zUsYN4SRyl-_nT2X",
                "name": "WhoAyem",
                "description": "Testimoni ",
                "photoUrl": "https://story-api.dicoding.dev/images/stories/photos-1665921781092_-CtQLGqW.jpg",
                "createdAt": "2022-10-16T12:03:01.094Z",
                "lat": null,
                "lon": null
            },
            {
                "id": "story-Hl73agGak4URonkH",
                "name": "Tes",
                "description": "okeeeee",
                "photoUrl": "https://story-api.dicoding.dev/images/stories/photos-1665921576804_oAV0l39K.jpg",
                "createdAt": "2022-10-16T11:59:36.805Z",
                "lat": -6.2367933,
                "lon": 106.8442117
            }
        ]
        """.trimIndent()
        val listStory: Type = object : TypeToken<List<StoryEntity>>() {}.type
        return Gson().fromJson(json, listStory)
    }
}