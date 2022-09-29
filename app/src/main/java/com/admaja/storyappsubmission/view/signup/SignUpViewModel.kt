package com.admaja.storyappsubmission.view.signup

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.admaja.storyappsubmission.data.DataRepository
import com.admaja.storyappsubmission.data.remote.config.ApiConfig
import com.admaja.storyappsubmission.data.remote.response.RegisterResponse
import com.admaja.storyappsubmission.utils.SIGN_UP_VIEW_MODEL
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpViewModel(private val dataRepository: DataRepository): ViewModel() {

    private var name:String = ""
    private var email:String = ""
    private var password:String = ""

    fun setUser(name: String, email: String, password: String) {
        this.name = name
        this.email = email
        this.password = password
    }

    fun register() = dataRepository.register(name, email, password)
//    private val _register = MutableLiveData<RegisterResponse>()
//    val registerResponse: LiveData<RegisterResponse> = _register
//
//    private val _isLoading = MutableLiveData<Boolean>()
//    val isLoading: LiveData<Boolean> = _isLoading
//
//    private val _error = MutableLiveData<ResponseBody>()
//    val error: LiveData<ResponseBody> = _error
//
//    init {
//        _isLoading.value = true
//    }
//
//    fun register(name: String, email: String, password: String) {
//        _isLoading.value = true
//        val client = ApiConfig.getApiSerivice().register(name, email, password)
//        client.enqueue(object : Callback<RegisterResponse>{
//            override fun onResponse(
//                call: Call<RegisterResponse>,
//                response: Response<RegisterResponse>
//            ) {
//                _isLoading.value = false
//                _register.value = response.body()
//                if (response.isSuccessful) {
//                    _register.value = response.body()
//                } else {
//                    _error.value = response.errorBody()
//                }
//            }
//
//            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
//                _isLoading.value = false
//                Log.e(SIGN_UP_VIEW_MODEL, "On Failure: ${t.message.toString()}")
//            }
//
//        })
//    }

//    companion object {
//        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
//            @Suppress("UNCHECKED_CAST")
//            override fun <T : ViewModel> create(modelClass: Class<T>): T {
//                if (modelClass.isAssignableFrom(SignUpViewModel::class.java)) {
//                    return SignUpViewModel(dataRepository) as T
//                }
//                throw IllegalArgumentException("Unknown ViewModel class : "+modelClass.name)
//            }
//        }
//    }
}