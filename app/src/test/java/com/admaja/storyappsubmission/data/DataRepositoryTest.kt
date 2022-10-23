package com.admaja.storyappsubmission.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.*
import androidx.recyclerview.widget.ListUpdateCallback
import com.admaja.storyappsubmission.DataDummy
import com.admaja.storyappsubmission.data.local.entity.StoryEntity
import com.admaja.storyappsubmission.data.local.preferences.UserPreference
import com.admaja.storyappsubmission.data.local.room.Dao
import com.admaja.storyappsubmission.data.local.room.StoryDatabase
import com.admaja.storyappsubmission.data.remote.config.ApiService
import com.admaja.storyappsubmission.getOrAwaitValue
import com.admaja.storyappsubmission.utils.MainDispatcherRule
import com.admaja.storyappsubmission.utils.StoryPagingSource
import com.admaja.storyappsubmission.view.adapter.StoryListAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class DataRepositoryTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var apiService: ApiService

    private lateinit var dao: Dao

    private lateinit var dataRepository: DataRepository

    @Mock
    private lateinit var storyDatabase: StoryDatabase

    @Mock
    private lateinit var userPreference: UserPreference

    private val dummyEmail = "john@test.com"
    private val dummyPassword = "12345678"
    private val dummyName = "John Doe"
    private val dummyFile = DataDummy.multipartFile()
    private val dummyDescription = DataDummy.requestBody("Dummy Description")
    private val dummyLat = DataDummy.requestBody("-7.795872")
    private val dummyLon = DataDummy.requestBody("110.384342")
    private val dummyBasicResponse = DataDummy.basicResponse()
    private val dummyLoginResponse = DataDummy.loginResponse()

    @Before
    fun setUp() {
        apiService = FakeApiService()
        dao = FakeStoryDao()
        dataRepository = DataRepository(apiService, dao,userPreference, storyDatabase)
    }

    @Test
    fun `When doRegister Success`() = runTest {
        val expectedData = dummyBasicResponse
        val actualResponse = dataRepository.doRegister(dummyName, dummyEmail, dummyPassword).getOrAwaitValue()
        if (actualResponse !is Result.Loading) {
            Assert.assertNotNull(actualResponse)
            Assert.assertTrue(actualResponse is Result.Success)
            Assert.assertFalse(actualResponse is Result.Error)
            Assert.assertEquals(expectedData.message, (actualResponse as Result.Success).data.message)
        }
    }

    @Test
    fun `When do Register Failed`() = runTest {
        val expectedData = Result.Error("")
        val actualResponse = dataRepository.doRegister(dummyName, dummyEmail, dummyPassword).getOrAwaitValue()
        if (actualResponse !is Result.Loading) {
            Assert.assertFalse(actualResponse is Result.Success)
            Assert.assertTrue(actualResponse is Result.Error)
            Assert.assertEquals(expectedData.error, (actualResponse as Result.Error).error)
        }
    }

    @Test
    fun `When login Success`() = runTest {
        val expectedResponse = dummyLoginResponse
        val actualResponse = dataRepository.login(dummyEmail, dummyPassword).getOrAwaitValue()
        if (actualResponse !is Result.Loading) {
            Assert.assertNotNull(actualResponse)
            Assert.assertTrue(actualResponse is Result.Success)
            Assert.assertFalse(actualResponse is Result.Error)
            Assert.assertEquals(expectedResponse.loginResult.userId, (actualResponse as Result.Success).data.loginResult.userId)
        }
    }

    @Test
    fun `When login Failed`() = runTest {
        val expectedResponse = Result.Error("")
        val actualResponse = dataRepository.login(dummyEmail, dummyPassword).getOrAwaitValue()
        if (actualResponse !is Result.Loading) {
            Assert.assertNull(actualResponse)
            Assert.assertTrue(actualResponse is Result.Error)
            Assert.assertFalse(actualResponse is Result.Success)
            Assert.assertEquals(expectedResponse.error, (actualResponse as Result.Error).error)
        }
    }

    @Test
    fun `When AddStory Success`() = runTest {
        val expectedResponse = dummyBasicResponse
        val actualResponse = dataRepository.addNewStory(dummyDescription, dummyFile, dummyLat, dummyLon).getOrAwaitValue()
        if (actualResponse !is Result.Loading) {
            Assert.assertNotNull(actualResponse)
            Assert.assertFalse(actualResponse is Result.Error)
            Assert.assertTrue(actualResponse is Result.Success)
            Assert.assertEquals(expectedResponse.message, (actualResponse as Result.Success).data.message)
        }
    }

    @Test
    fun `When AddStory Failed`() = runTest {
        val expectedResponse = Result.Error("")
        val actualResponse = dataRepository.addNewStory(dummyDescription, dummyFile, dummyLat, dummyLon).getOrAwaitValue()
        if (actualResponse !is Result.Loading) {
            Assert.assertFalse(actualResponse is Result.Success)
            Assert.assertTrue(actualResponse is Result.Error)
            Assert.assertEquals(expectedResponse.error, (actualResponse as Result.Error).error)
        }
    }

    @Test
    fun `When Add and Get Story From Database Not Null and Success`() = runTest {
        val expectedData = DataDummy.storyInDatabase()
        dao.insertStory(DataDummy.storyInDatabase())
        val actualData = dataRepository.getStoryFromDatabase().getOrAwaitValue()
        Assert.assertNotNull(actualData)
        Assert.assertEquals(expectedData.size, actualData.size)
    }

    @Test
    fun `When getStories Success and Not Null`() = runTest {
        val data = StoryPagingSource.snapshot(DataDummy.storyInDatabase())
        val expectedData = MutableLiveData<PagingData<StoryEntity>>()
        expectedData.value = data
        CoroutineScope(Dispatchers.IO).launch {
            val actualData: PagingData<StoryEntity> = dataRepository.getStory().getOrAwaitValue()
            val differ = AsyncPagingDataDiffer(
                diffCallback = StoryListAdapter.DIFF_CALLBACK,
                updateCallback = noopListUpdateCallback,
                workerDispatcher = Dispatchers.Main
            )
            differ.submitData(actualData)
            Assert.assertNotNull(differ.snapshot())
            Assert.assertEquals(DataDummy.storyInDatabase(), differ.snapshot())
            Assert.assertEquals(DataDummy.storyInDatabase().size, differ.snapshot().size)
            Assert.assertEquals(DataDummy.storyInDatabase()[0].id, differ.snapshot()[0]?.id)
        }
    }

    private val noopListUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }
}