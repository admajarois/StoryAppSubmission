package com.admaja.storyappsubmission.data

import androidx.lifecycle.LiveData
import com.admaja.storyappsubmission.data.remote.response.LoginResponse
import com.admaja.storyappsubmission.data.remote.response.RegisterResponse

interface HelperRepository {
    fun register(name: String, email: String, password: String): LiveData<Result<RegisterResponse>>

    fun login(email: String, password: String): LiveData<Result<LoginResponse>>
}