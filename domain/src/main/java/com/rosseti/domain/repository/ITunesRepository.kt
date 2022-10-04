package com.rosseti.domain.repository

import com.rosseti.domain.entity.ITunesEntity

interface ITunesRepository {
    suspend fun fetchSongByQuery(query: String): List<ITunesEntity>
}