package com.admaja.storyappsubmission.data.local.preferences

import android.content.Context
import androidx.lifecycle.LiveData
import com.admaja.storyappsubmission.data.remote.response.LoginResult
import com.admaja.storyappsubmission.utils.PREFS_NAME
import com.admaja.storyappsubmission.utils.TOKEN
import com.admaja.storyappsubmission.utils.USER_ID
import com.admaja.storyappsubmission.utils.USER_NAME

class UserPreference(context: Context) {

    private val preference = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private val editor = preference.edit()

    fun setUser(value: LoginResult?) {
        editor.putString(USER_ID, value?.userId)
        editor.putString(USER_NAME, value?.name)
        editor.putString(TOKEN, value?.token)
        editor.apply()
    }

    fun getUser(): LoginResult {
        val model = LoginResult()
        model.userId = preference.getString(USER_ID, null)
        model.name = preference.getString(USER_NAME, null)
        model.token = preference.getString(TOKEN, null)
        return model
    }

    fun clearUserPreference() = editor.clear().apply()
}