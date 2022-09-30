package com.rosseti.domain.repository

import com.rosseti.domain.entity.ITunesEntity

interface ITunesRepository {
    suspend fun fetchSongByName(name: String): List<ITunesEntity>
}