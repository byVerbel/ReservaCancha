package com.equipo13.reservacancha.model

import com.equipo13.reservacancha.R

data class TimeSlotModel(val time: String? = null, var status: Boolean? = null) {

    companion object {
        const val OCCUPIED = R.string.court_status_occupied
        const val AVAILABLE = R.string.court_status_available
    }

    fun available(): Int {

        var state = 0
        status?.let { timeStatus ->
            state = if (timeStatus) {
                AVAILABLE
            } else {
                OCCUPIED
            }
        }
        return state
    }
}
