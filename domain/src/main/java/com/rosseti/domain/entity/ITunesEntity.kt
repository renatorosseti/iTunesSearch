package com.rosseti.domain.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ITunesEntity(
    val artistName: String = "",
    val artistViewUrl: String = "",
    val collectionName: String = "",
    val longDescription: String = "",
    val shortDescription: String = "",
): Parcelable