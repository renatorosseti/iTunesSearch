package com.rosseti.data.api

import com.rosseti.data.model.ITunesResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ITunesApi {

    @GET(value = "users/{name}")
    suspend fun fetchSongByName(@Path("name") userId: String): ITunesResponse
}