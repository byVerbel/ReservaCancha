package com.equipo13.reservacancha.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class  CourtModel(
    var id: String? = null,
    val name:String? = null,
    val city:String? = null,
    val image:String? = null,
    val address:String? = null,
    val state:String? = null,
    val latitude:String? = null,
    val longitude:String? = null
) : Parcelable