package com.admaja.storyappsubmission.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.admaja.storyappsubmission.utils.REPOSITORY_TAG

abstract class DataResources<RequestType> {

    private var result: LiveData<Result<RequestType>> = liveData {
        emit(Result.Loading)
        try {
            val apiResponse = createCall()
//            getResponse(apiResponse.body)
        } catch (e: IllegalArgumentException) {
            Log.e(REPOSITORY_TAG, "Having a trouble ${e.message.toString()}")
        }
    }

    protected abstract suspend fun createCall(): LiveData<Result<RequestType>>

    protected abstract fun getResponse(data: RequestType)

    fun asLiveData(): LiveData<Result<RequestType>> = result
}