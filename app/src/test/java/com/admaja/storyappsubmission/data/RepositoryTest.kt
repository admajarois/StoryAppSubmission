package com.admaja.storyappsubmission.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.admaja.storyappsubmission.DataDummy
import com.admaja.storyappsubmission.data.local.preferences.UserPreference
import com.admaja.storyappsubmission.data.remote.response.BasicResponse
import com.admaja.storyappsubmission.data.remote.response.LoginResponse
import com.admaja.storyappsubmission.view.login.LoginViewModel
import com.admaja.storyappsubmission.view.main.MainViewModel
import com.admaja.storyappsubmission.view.maps.MapViewModels
import com.admaja.storyappsubmission.view.signup.SignUpViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import kotlin.math.sign


@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class RepositoryTest {

    @get: Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var dataRepository: DataRepository
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var signUpViewModel: SignUpViewModel
    private lateinit var mainViewModel: MainViewModel
    private lateinit var mapViewModel: MapViewModels
    private val dummyEmail = "john@test.com"
    private val dummyPassword = "12345678"
    private val dummyName = "John Doe"
    private val dummyLogin = DataDummy.loginResponse()
    private val dummyBasicResponse = DataDummy.basicResponse()
//    private val dummyStoryEntity = DataDummy.storyInDB()
    private val dummyFile = DataDummy.multipartFile()
    private val dummyDescription = DataDummy.requestBody("Dummy Description")
//    private val dummyLat = DataDummy.requestBody()
//    private val dummyLon = DataDummy.requestBody()

    @Before
    fun setup() {
        loginViewModel = LoginViewModel(dataRepository)
        signUpViewModel = SignUpViewModel(dataRepository)
        mapViewModel = MapViewModels(dataRepository)
    }

    @Test
    fun `when signup success`() {
        val expectedSignup = MutableLiveData<Result<BasicResponse>>()
        expectedSignup.value = Result.Success(dummyBasicResponse)
        `when`(dataRepository.doRegister(dummyName, dummyEmail, dummyPassword)).thenReturn(expectedSignup)
        val actualSignUp = signUpViewModel.register(dummyName, dummyEmail, dummyPassword)
        Mockito.verify(dataRepository).doRegister(dummyName, dummyEmail, dummyPassword)
        Assert.assertNotNull(actualSignUp)
    }

    @Test
    fun `when login success`() {
        val expectedLogin = MutableLiveData<Result<LoginResponse>>()
        expectedLogin.value = Result.Success(dummyLogin)
        `when`(dataRepository.login(dummyEmail, dummyPassword)).thenReturn(expectedLogin)
        val actualLogin = loginViewModel.login(dummyEmail, dummyPassword)
        Mockito.verify(dataRepository).login(dummyEmail, dummyPassword)
        Assert.assertNotNull(actualLogin)
    }
}