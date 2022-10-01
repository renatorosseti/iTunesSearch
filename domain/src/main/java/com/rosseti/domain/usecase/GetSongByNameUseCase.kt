package com.rosseti.domain.usecase

import com.rosseti.domain.Resource
import com.rosseti.domain.entity.ITunesEntity
import com.rosseti.domain.repository.ITunesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class GetSongByNameUseCase @Inject constructor(private val repository: ITunesRepository) {

    operator fun invoke(name: String): Flow<Resource<List<ITunesEntity>>> =
        flow { emit(repository.fetchSongByName(name)) }
            .map { Resource.success(it) }
            .onStart { emit(Resource.loading()) }
            .catch { emit(Resource.error(it)) }
            .flowOn(Dispatchers.IO)
}