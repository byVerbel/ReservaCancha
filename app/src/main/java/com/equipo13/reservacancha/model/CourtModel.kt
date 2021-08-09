package com.equipo13.reservacancha.model

data class  CourtModel(
    val name:String? = null,
    val city:String? = null,
    val image:String? = null,
    val address:String? = null,
    val state:String? = null
) {
    fun toCourt(): CourtModel = CourtModel(name, city, state, address, image)
}