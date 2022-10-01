package com.rosseti.data.api

import com.rosseti.data.model.ITunesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApi {

    @GET(value = "/search")
    suspend fun fetchSongByName(@Query("term") search: String): ITunesResponse
}