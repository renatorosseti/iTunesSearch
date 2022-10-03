package com.rosseti.data.repository

import android.os.Parcelable
import android.util.Log
import com.rosseti.data.api.ITunesApi
import com.rosseti.domain.entity.ITunesEntity
import com.rosseti.domain.repository.ITunesRepository
import javax.inject.Inject

class ITunesRepositoryImpl @Inject constructor(private val api: ITunesApi) : ITunesRepository {
    override suspend fun fetchSongByName(name: String): List<ITunesEntity> =
        api.fetchSongByName(name).results.map {
            Log.i("ITunesRepositoryImpl", "Response: $it")
            ITunesEntity(
                artistName = it.artistName ?: "",
                artistViewUrl = it.artistViewUrl ?: "",
                collectionViewUrl = it.collectionViewUrl ?: "",
                trackViewUrl = it.trackViewUrl ?: "",
                collectionName = it.collectionName ?: "",
                trackName = it.trackName ?: "",
                longDescription = it.longDescription ?: "",
                shortDescription = it.shortDescription ?: "",
                artworkUrl100 = it.artworkUrl100 ?: "",
            )
        }
}