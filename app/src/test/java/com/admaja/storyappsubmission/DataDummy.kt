package com.admaja.storyappsubmission

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.admaja.storyappsubmission.data.local.entity.StoryEntity
import com.admaja.storyappsubmission.data.remote.response.BasicResponse
import com.admaja.storyappsubmission.data.remote.response.LoginResponse
import com.admaja.storyappsubmission.data.remote.response.StoryResponse
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

    fun storyResponse(): StoryResponse {
        val json = """
            {
                "error": false,
                "message": "Stories fetched successfully",
                "listStory": [
                    {
                        "id": "story-RUTTuPRlpl9tlTUO",
                        "name": "Hello, Im Guest.",
                        "description": "tes",
                        "photoUrl": "https://story-api.dicoding.dev/images/stories/photos-1666147545904_pF39wTY7.jpg",
                        "createdAt": "2022-10-19T02:45:45.906Z",
                        "lat": null,
                        "lon": null
                    },
                    {
                        "id": "story-MtAxDKtiAf7RC4SJ",
                        "name": "Hello, Im Guest.",
                        "description": "test hello",
                        "photoUrl": "https://story-api.dicoding.dev/images/stories/photos-1666147500322_fN7156l7.jpg",
                        "createdAt": "2022-10-19T02:45:00.327Z",
                        "lat": null,
                        "lon": null
                    },
                    {
                        "id": "story-lY23wjlFNi0H1eUM",
                        "name": "jaruk",
                        "description": "1",
                        "photoUrl": "https://story-api.dicoding.dev/images/stories/photos-1666147252614_s8M0NCld.jpg",
                        "createdAt": "2022-10-19T02:40:52.617Z",
                        "lat": null,
                        "lon": null
                    },
                    {
                        "id": "story-ax9t6bKFm8AgoO4A",
                        "name": "jaruk",
                        "description": "scroll to top",
                        "photoUrl": "https://story-api.dicoding.dev/images/stories/photos-1666147226914_JA0rUR6O.jpg",
                        "createdAt": "2022-10-19T02:40:26.915Z",
                        "lat": null,
                        "lon": null
                    },
                    {
                        "id": "story-uorVqw6ZgxXdz06h",
                        "name": "Hello, Im Guest.",
                        "description": "Hello",
                        "photoUrl": "https://story-api.dicoding.dev/images/stories/photos-1666146965213_V6FJvsic.jpg",
                        "createdAt": "2022-10-19T02:36:05.221Z",
                        "lat": null,
                        "lon": null
                    },
                    {
                        "id": "story-pt7CjqG0GtP62mxv",
                        "name": "Hello, Im Guest.",
                        "description": "sdsdsds",
                        "photoUrl": "https://story-api.dicoding.dev/images/stories/photos-1666145877725_zW3JYFAa.jpg",
                        "createdAt": "2022-10-19T02:17:57.726Z",
                        "lat": null,
                        "lon": null
                    },
                    {
                        "id": "story-mTKLVr-EYxILDJ55",
                        "name": "Hello, Im Guest.",
                        "description": "SDSDSD",
                        "photoUrl": "https://story-api.dicoding.dev/images/stories/photos-1666145860575_60GOhQ4Z.jpg",
                        "createdAt": "2022-10-19T02:17:40.577Z",
                        "lat": null,
                        "lon": null
                    },
                    {
                        "id": "story-ij6_l0wq-uv6JmEV",
                        "name": "Hello, Im Guest.",
                        "description": "Test",
                        "photoUrl": "https://story-api.dicoding.dev/images/stories/photos-1666145802552_0C0g4eEa.jpg",
                        "createdAt": "2022-10-19T02:16:42.554Z",
                        "lat": null,
                        "lon": null
                    },
                    {
                        "id": "story-bc3mIh4AR3JlBP9L",
                        "name": "Hello, Im Guest.",
                        "description": "dfdsdsfsdfdsfds",
                        "photoUrl": "https://story-api.dicoding.dev/images/stories/photos-1666145563347_5vEnVrkP.jpg",
                        "createdAt": "2022-10-19T02:12:43.348Z",
                        "lat": null,
                        "lon": null
                    },
                    {
                        "id": "story-t95BBnIgErY1cCT4",
                        "name": "Yugi Mutou",
                        "description": "Discard 1 card, then target 1 face-up monster you control that has a Level; send it to the Graveyard, also, after that, if it left the field by this effect, Special Summon from your Extra Deck, 1 \"Masked HERO\" monster with the same Attribute, but a higher Level than, the Attribute/Level the monster had when it was on the field (its original Attribute/Level, if face-down. This Special Summon is treated as a Special Summon with \"Mask Change\"). You can only activate 1 \"Mask Change II\" per turn.",
                        "photoUrl": "https://story-api.dicoding.dev/images/stories/photos-1666145234132_vTuFvgZH.jpg",
                        "createdAt": "2022-10-19T02:07:14.135Z",
                        "lat": null,
                        "lon": null
                    }
                ]
            }
        """.trimIndent()
        return Gson().fromJson(json, StoryResponse::class.java)
    }

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