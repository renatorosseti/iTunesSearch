package com.rosseti.domain.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ITunesEntity(
    val artistName: String = "",
    val artistViewUrl: String = "",
    val collectionViewUrl: String = "",
    val trackViewUrl: String = "",
    val collectionName: String = "",
    val trackName: String = "",
    val primaryGenreName: String = "",
    val longDescription: String = "",
    val shortDescription: String = "",
    val artworkUrl100: String = ""
): Parcelable