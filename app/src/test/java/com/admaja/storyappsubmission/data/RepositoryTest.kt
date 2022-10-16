package com.admaja.storyappsubmission.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.admaja.storyappsubmission.DataDummy
import com.admaja.storyappsubmission.MainDispatcherRule
import com.admaja.storyappsubmission.data.local.entity.StoryEntity
import com.admaja.storyappsubmission.data.remote.response.BasicResponse
import com.admaja.storyappsubmission.data.remote.response.LoginResponse
import com.admaja.storyappsubmission.getOrAwaitValue
import com.admaja.storyappsubmission.view.adapter.StoryListAdapter
import com.admaja.storyappsubmission.view.add.AddStoryViewModel
import com.admaja.storyappsubmission.view.login.LoginViewModel
import com.admaja.storyappsubmission.view.main.MainViewModel
import com.admaja.storyappsubmission.view.maps.MapViewModels
import com.admaja.storyappsubmission.view.signup.SignUpViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner


@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class RepositoryTest {

    @get: Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get: Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var dataRepository: DataRepository
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var signUpViewModel: SignUpViewModel
    private lateinit var mapViewModel: MapViewModels
    private lateinit var addStoryViewModel: AddStoryViewModel
    private val dummyEmail = "john@test.com"
    private val dummyPassword = "12345678"
    private val dummyName = "John Doe"
    private val dummyLogin = DataDummy.loginResponse()
    private val dummyBasicResponse = DataDummy.basicResponse()

    private val dummyStoryEntity = DataDummy.storyInDatabase()
    private val dummyFile = DataDummy.multipartFile()
    private val dummyDescription = DataDummy.requestBody("Dummy Description")
    private val dummyLat = DataDummy.requestBody("-7.795872")
    private val dummyLon = DataDummy.requestBody("110.384342")

    @Before
    fun setup() {
        loginViewModel = LoginViewModel(dataRepository)
        signUpViewModel = SignUpViewModel(dataRepository)
        mapViewModel = MapViewModels(dataRepository)
        addStoryViewModel = AddStoryViewModel(dataRepository)
    }

    @Test
    fun `When Signup Success`() {
        val expectedSignup = MutableLiveData<Result<BasicResponse>>()
        expectedSignup.value = Result.Success(dummyBasicResponse)
        `when`(dataRepository.doRegister(dummyName, dummyEmail, dummyPassword)).thenReturn(
            expectedSignup
        )
        val actualSignUp =
            signUpViewModel.register(dummyName, dummyEmail, dummyPassword).getOrAwaitValue()
        verify(dataRepository).doRegister(dummyName, dummyEmail, dummyPassword)
        Assert.assertNotNull(actualSignUp)
        Assert.assertTrue(actualSignUp is Result.Success)
        Assert.assertEquals(dummyBasicResponse.message, (actualSignUp as Result.Success).data.message)

    }

    @Test
    fun `When Register Failed`() {
        val signup = MutableLiveData<Result<BasicResponse>>()
        signup.value = Result.Error("Error")
        `when`(dataRepository.doRegister(dummyName, dummyEmail, dummyPassword)).thenReturn(signup)
        val actualSignUp = signUpViewModel.register(dummyName, dummyEmail, dummyPassword).getOrAwaitValue()
        verify(dataRepository).doRegister(dummyName, dummyEmail, dummyPassword)
        Assert.assertNotNull(actualSignUp)
        Assert.assertTrue(actualSignUp is Result.Error)
    }

    @Test
    fun `When Login Success`() {
        val expectedLogin = MutableLiveData<Result<LoginResponse>>()
        expectedLogin.value = Result.Success(dummyLogin)
        `when`(dataRepository.login(dummyEmail, dummyPassword)).thenReturn(expectedLogin)
        val actualLogin = loginViewModel.login(dummyEmail, dummyPassword).getOrAwaitValue()
        verify(dataRepository).login(dummyEmail, dummyPassword)
        Assert.assertNotNull(actualLogin)
        Assert.assertTrue(actualLogin is Result.Success)
        Assert.assertEquals(dummyLogin.loginResult.token, (actualLogin as Result.Success).data.loginResult.token)
    }

    @Test
    fun `When Login Failed`(){
        val login = MutableLiveData<Result<LoginResponse>>()
        login.value = Result.Error("Error")
        `when`(dataRepository.login(dummyEmail, dummyPassword)).thenReturn(login)
        val actualLogin = loginViewModel.login(dummyEmail, dummyPassword).getOrAwaitValue()
        verify(dataRepository).login(dummyEmail, dummyPassword)
        Assert.assertNotNull(actualLogin)
        Assert.assertTrue(actualLogin is Result.Error)
    }

    @Test
    fun `When Add New Story Success`() {
        val expectedAddStory = MutableLiveData<Result<BasicResponse>>()
        expectedAddStory.value = Result.Success(dummyBasicResponse)
        `when`(dataRepository.addNewStory(dummyDescription, dummyFile, dummyLat, dummyLon)).thenReturn(expectedAddStory)
        val actualAddStory = addStoryViewModel.addStory(dummyDescription, dummyFile, dummyLat, dummyLon).getOrAwaitValue()
        verify(dataRepository).addNewStory(dummyDescription, dummyFile, dummyLat, dummyLon)
        Assert.assertNotNull(actualAddStory)
        Assert.assertTrue(actualAddStory is Result.Success)
        Assert.assertEquals(dummyBasicResponse.message, (actualAddStory as Result.Success).data.message)
    }

    @Test
    fun `When Add New Story Failed`() {
        val addStory = MutableLiveData<Result<BasicResponse>>()
        addStory.value = Result.Error("Error")
        `when`(dataRepository.addNewStory(dummyDescription, dummyFile, dummyLat, dummyLon)).thenReturn(addStory)
        val actualAddStory = addStoryViewModel.addStory(dummyDescription, dummyFile, dummyLat, dummyLon).getOrAwaitValue()
        verify(dataRepository).addNewStory(dummyDescription, dummyFile, dummyLat, dummyLon)
        Assert.assertNotNull(actualAddStory)
        Assert.assertTrue(actualAddStory is Result.Error)
    }

    @Test
    fun `When Get Story From Database Success`() = runTest {
        val expectedStoryData = MutableLiveData<List<StoryEntity>>()
        expectedStoryData.value = dummyStoryEntity
        `when`(dataRepository.getStoryFromDatabase()).thenReturn(expectedStoryData)
        val mapViewModels = MapViewModels(dataRepository)
        val actualStoryData = mapViewModels.getStoryLocation().getOrAwaitValue()
        Assert.assertNotNull(dummyStoryEntity)
        Assert.assertEquals(dummyStoryEntity.size, actualStoryData.size)
    }


    @Test
    fun `When Get Story Should Not Null and Return Success`() = runTest {
        val data: PagingData<StoryEntity> = StoryPagingSource.snapshot(dummyStoryEntity)
        val expectedStory = MutableLiveData<PagingData<StoryEntity>>()
        expectedStory.value = data
        `when`(dataRepository.getStory()).thenReturn(expectedStory)

        val mainViewModel = MainViewModel(dataRepository)
        val actualStory: PagingData<StoryEntity> = mainViewModel.story.getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryListAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualStory)

        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dummyStoryEntity, differ.snapshot())
        Assert.assertEquals(dummyStoryEntity.size, differ.snapshot().size)
        Assert.assertEquals(dummyStoryEntity[0].name, differ.snapshot()[0]?.name)
    }

    class StoryPagingSource: PagingSource<Int, LiveData<List<StoryEntity>>>(){
        override fun getRefreshKey(state: PagingState<Int, LiveData<List<StoryEntity>>>): Int? {
            return 0
        }

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<StoryEntity>>> {
            return LoadResult.Page(emptyList(), 0, 1)
        }

        companion object {
            fun snapshot(items: List<StoryEntity>): PagingData<StoryEntity> {
                return PagingData.from(items)
            }
        }

    }

    private val noopListUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }
}