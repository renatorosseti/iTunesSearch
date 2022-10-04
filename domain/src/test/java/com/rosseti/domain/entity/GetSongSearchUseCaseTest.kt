package com.rosseti.domain.entity

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.rosseti.domain.Resource
import com.rosseti.domain.repository.ITunesRepository
import com.rosseti.domain.usecase.GetSongBySearchUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import java.io.IOException

class GetSongSearchUseCaseTest {
    private lateinit var getSongBySearchUseCase: GetSongBySearchUseCase

    @MockK(relaxed = true)
    lateinit var repository: ITunesRepository

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        getSongBySearchUseCase = GetSongBySearchUseCase(repository)
    }

    @Test
    fun `When request getSongBySearchUseCase returns a list of ITunesEntity, emit success with content as data`() {
        val response = listOf(ITunesEntity())
        val songQuery = "test"

        coEvery { repository.fetchSongByQuery(songQuery) } returns response
        runBlocking {
            getSongBySearchUseCase(songQuery).collectIndexed { index, value ->
                when (index) {
                    FIRST_ELEMENT_EMITTED -> {
                        assert(value.status == Resource.Status.LOADING)
                    }
                    SECOND_ELEMENT_EMITTED -> {
                        assert(value.status == Resource.Status.SUCCESS)
                        assert(value.data == response)
                    }
                }
            }
        }
    }

    @Test
    fun `When request getSongBySearchUseCase and returns error, emit IOException error`() {
        val songQuery = "test"

        coEvery { repository.fetchSongByQuery(songQuery) } throws IOException("IO error")
        runBlocking {
            getSongBySearchUseCase(songQuery).collectIndexed { index, value ->
                when (index) {
                    FIRST_ELEMENT_EMITTED -> {
                        assert(value.status == Resource.Status.LOADING)
                    }
                    SECOND_ELEMENT_EMITTED -> {
                        assert(value.status == Resource.Status.ERROR)
                        assert(value.data == null)
                        assert(value.error is IOException)
                    }
                }
            }
        }
    }

    companion object {
        const val FIRST_ELEMENT_EMITTED = 0
        const val SECOND_ELEMENT_EMITTED = 1
    }
}