package com.admaja.storyappsubmission.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.*
import androidx.recyclerview.widget.ListUpdateCallback
import com.admaja.storyappsubmission.DataDummy
import com.admaja.storyappsubmission.data.local.entity.StoryEntity
import com.admaja.storyappsubmission.data.local.room.Dao
import com.admaja.storyappsubmission.data.local.room.StoryDatabase
import com.admaja.storyappsubmission.data.paging.StoryRemoteMediator
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
import org.mockito.Mockito.`when`
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

    private val dummyEmail = "john@test.com"
    private val dummyPassword = "12345678"
    private val dummyName = "John Doe"
    private val dummyFile = DataDummy.multipartFile()
    private val dummyDescription = DataDummy.requestBody("Dummy Description")
    private val dummyLat = DataDummy.requestBody("-7.795872")
    private val dummyLon = DataDummy.requestBody("110.384342")

    @Before
    fun setUp() {
        apiService = FakeApiService()
        dao = FakeStoryDao()
        dataRepository = DataRepository(apiService, dao, storyDatabase)
    }

    @Test
    fun `When doRegister Success`() = runTest {
        val expectedData = DataDummy.basicResponse()
        val actualResponse = apiService.register(dummyName, dummyEmail, dummyPassword)
        Assert.assertNotNull(actualResponse)
        Assert.assertFalse(actualResponse.error?:true)
        Assert.assertEquals(expectedData.message, actualResponse.message)
    }

    @Test
    fun `When login Success`() = runTest {
        val expectedResponse = DataDummy.loginResponse()
        val actualResponse = apiService.login(dummyEmail, dummyPassword)
        Assert.assertNotNull(actualResponse)
        Assert.assertFalse(actualResponse.error)
        Assert.assertEquals(expectedResponse.loginResult.userId, actualResponse.loginResult.userId)
    }

    @Test
    fun `When AddStory Success`() = runTest {
        val expectedResponse = DataDummy.basicResponse()
        val actualResponse = apiService.addStories(dummyDescription, dummyFile, dummyLat, dummyLon)
        Assert.assertNotNull(actualResponse)
        Assert.assertFalse(actualResponse.error?:true)
        Assert.assertEquals(expectedResponse.message, actualResponse.message)
    }

    @Test
    fun `When get story from API Success`() = runTest {
        val expectedResponse = DataDummy.storyResponse()
        val actualResponse = apiService.getStories(null, null, 1)
        Assert.assertNotNull(actualResponse)
        Assert.assertFalse(actualResponse.error)
        Assert.assertEquals(expectedResponse.listStory.size, actualResponse.listStory.size)
    }

    @Test
    fun `When Add and Get Story From Database Not Null and Success`() = runTest {
        val expectedData = DataDummy.storyInDatabase()
        dao.insertStory(DataDummy.storyInDatabase())
        val actualData = dao.getStoryFromDatabase().getOrAwaitValue()
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