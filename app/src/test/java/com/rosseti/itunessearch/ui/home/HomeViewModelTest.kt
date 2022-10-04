package com.rosseti.itunessearch.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.rosseti.domain.Resource
import com.rosseti.domain.entity.ITunesEntity
import com.rosseti.domain.usecase.GetSongBySearchUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class HomeViewModelTest {
    private lateinit var viewModel: HomeViewModel

    @MockK
    lateinit var getSongBySearchUseCase: GetSongBySearchUseCase

    @ExperimentalCoroutinesApi
    val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        MockKAnnotations.init(this)
        viewModel = HomeViewModel(getSongBySearchUseCase)
    }

    @ExperimentalCoroutinesApi
    @After
    fun tearDownDispatcher() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `When request fetchSongs with empty query and returns empty response, homeState value should be Empty`() {
        val songQuery = ""
        every { getSongBySearchUseCase(songQuery) } returns flow { emit(Resource.success(data = null)) }
        viewModel.fetchSongs(songQuery)
        assert(viewModel.homeState.value == HomeViewModel.HomeAction.Empty)
    }

    @Test
    fun `When request fetchSongs with non-empty query and return error, homeState value should be Error`() {
        val songQuery = "text"

        coEvery { getSongBySearchUseCase(songQuery) } answers { flow { emit(Resource.error(error = Throwable())) } }
        viewModel.fetchSongs(songQuery)
        assert(viewModel.homeState.value == HomeViewModel.HomeAction.Error)
    }

    @Test
    fun `When request fetchSongs with non-empty query and returns song list, homeState value should be Successful`() {
        val songQuery = "text"
        val songs = listOf(ITunesEntity())

        coEvery { getSongBySearchUseCase(songQuery) } answers { flow { emit(Resource.success(data = songs)) } }
        viewModel.fetchSongs(songQuery)
        assert(viewModel.homeState.value == HomeViewModel.HomeAction.Successful(data = songs))
    }
}