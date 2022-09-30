package com.admaja.storyappsubmission.data.remote.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("loginResult")
	val loginResult: LoginResult,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)

data class LoginResult(

	@field:SerializedName("name")
	var name: String? = null,

	@field:SerializedName("userId")
	var userId: String? = null,

	@field:SerializedName("token")
	var token: String? = null
)
