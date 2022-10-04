package com.rosseti.data.model

data class ITunesResponse(
    val resultCount: Int,
    val results: List<Result>
)

data class Result(
    val artistName: String?,
    val artistViewUrl: String?,
    val artworkUrl100: String,
    val collectionArtistName: String?,
    val collectionArtistViewUrl: String?,
    val collectionName: String?,
    val collectionViewUrl: String?,
    val kind: String?,
    val longDescription: String?,
    val primaryGenreName: String?,
    val releaseDate: String?,
    val shortDescription: String?,
    val trackName: String?,
    val trackViewUrl: String?,
)