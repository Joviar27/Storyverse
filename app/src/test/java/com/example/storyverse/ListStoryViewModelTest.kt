package com.example.storyverse

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.example.storyverse.domain.entity.StoryEntity
import com.example.storyverse.domain.usecase.GetStoryListUseCase
import com.example.storyverse.domain.usecase.LogoutUseCase
import com.example.storyverse.ui.liststory.ListStoryViewModel
import com.example.storyverse.util.DataDummy
import com.example.storyverse.util.MainDispatcherRule
import com.example.storyverse.util.getOrAwaitValue
import com.example.storyverse.util.observeForTesting
import com.example.storyverse.utils.ResultState
import com.example.storyverse.utils.StoryAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ListStoryViewModelTest{

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var getStoryListUseCase: GetStoryListUseCase

    @Mock
    private lateinit var logoutUseCase : LogoutUseCase

    @Test
    fun `when Get Story Should Not Null and Return Data`() = runTest {
        val dummyStory = DataDummy.generateDummyStoriesResponse()
        val data: PagingData<StoryEntity> = StoryPagingSource.snapshot(dummyStory)

        val expectedPagedStory  = MutableLiveData<PagingData<StoryEntity>>()
        expectedPagedStory.value = data

        val expectedStory  = MutableLiveData<ResultState<List<StoryEntity>>>()
        expectedStory.value = ResultState.Success(dummyStory)

        Mockito.`when`(getStoryListUseCase.getStoryPaged()).thenReturn(expectedPagedStory)
        Mockito.`when`(getStoryListUseCase.getStoryList(0)).thenReturn(expectedStory)

        val listStoryViewModel = ListStoryViewModel(getStoryListUseCase, logoutUseCase)

        val actualPagedStory : PagingData<StoryEntity> = listStoryViewModel.story.getOrAwaitValue()
        val actualStory = listStoryViewModel.getStoryList(0)

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )
        differ.submitData(actualPagedStory)

        //Memastikan data tidak null
        assertNotNull(differ.snapshot())
        //Memastikan jumlah data sesuai yang diharapkan
        assertEquals(dummyStory.size, differ.snapshot().size)
        //Memastikan data pertama yang dikembalikan sesuai
        assertEquals(dummyStory[0], differ.snapshot()[0])

        actualStory.observeForTesting {
            assertNotNull(actualStory)
            assertEquals(dummyStory.size, (actualStory.value as ResultState.Success).data.size)
            assertEquals(dummyStory[0], (actualStory.value as ResultState.Success).data[0])
        }
    }

    @Test
    fun `when Get Story Empty Should Return No Data`() = runTest {
        val data: PagingData<StoryEntity> = StoryPagingSource.snapshot(emptyList())

        val expectedPagedStory =  MutableLiveData<PagingData<StoryEntity>>()
        expectedPagedStory.value = data

        val expectedStory  = MutableLiveData<ResultState<List<StoryEntity>>>()
        expectedStory.value = ResultState.Success(emptyList())

        Mockito.`when`(getStoryListUseCase.getStoryPaged()).thenReturn(expectedPagedStory)
        Mockito.`when`(getStoryListUseCase.getStoryList(0)).thenReturn(expectedStory)

        val listStoryViewModel = ListStoryViewModel(getStoryListUseCase, logoutUseCase)

        val actualPagedStory : PagingData<StoryEntity> = listStoryViewModel.story.getOrAwaitValue()
        val actualStory = listStoryViewModel.getStoryList(0)

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )
        differ.submitData(actualPagedStory)

        //Memastikan jumlah data nol
        assertEquals(0,differ.snapshot().size)
        actualStory.observeForTesting {
            assertEquals(0, (actualStory.value as ResultState.Success).data.size)
        }
    }

    private val noopListUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }

    class StoryPagingSource : PagingSource<Int, LiveData<List<StoryEntity>>>() {
        companion object {
            fun snapshot(items: List<StoryEntity>): PagingData<StoryEntity> {
                return PagingData.from(items)
            }
        }
        override fun getRefreshKey(state: PagingState<Int, LiveData<List<StoryEntity>>>): Int {
            return 0
        }
        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<StoryEntity>>> {
            return LoadResult.Page(emptyList(), 0, 1)
        }
    }
}