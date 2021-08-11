package com.equipo13.reservacancha.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class  CourtModel(
    val name:String? = null,
    val city:String? = null,
    val image:String? = null,
    val address:String? = null,
    val state:String? = null,
    var id: String? = null
) : Parcelable