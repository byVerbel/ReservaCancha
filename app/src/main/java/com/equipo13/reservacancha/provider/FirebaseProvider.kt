package com.equipo13.reservacancha.provider

import android.content.Context
import com.equipo13.reservacancha.R
import com.equipo13.reservacancha.common.showToast
import com.equipo13.reservacancha.model.CourtModel
import com.google.firebase.database.FirebaseDatabase

enum class FirebaseRDBProvider (val key: String) {
    // Schemes
    USERS("Users"), COURTS("Courts"),

    // Shared
    ID("id"), NAME("name"), PHONE("phone"), IMAGE("image"),

    // Users
    EMAIL("email"),

    // Courts
    CITY("city"), STATE("state"), ADDRESS("address")
}

object FirebaseRDB {

    private val usersRef = FirebaseDatabase.getInstance().getReference(FirebaseRDBProvider.USERS.key)
    private val courtsRef = FirebaseDatabase.getInstance().getReference(FirebaseRDBProvider.COURTS.key)

    fun court(context: Context, courtId:String): CourtModel{
        var retrievedCourt = CourtModel()

        courtsRef.child(courtId).get().addOnSuccessListener { snapshot ->
            if (snapshot.exists() && snapshot != null) {
                val name = snapshot.child(FirebaseRDBProvider.NAME.key).value?.toString()
                val city = snapshot.child(FirebaseRDBProvider.CITY.key).value?.toString()
                val address = snapshot.child(FirebaseRDBProvider.ADDRESS.key).value?.toString()
                val image = snapshot.child(FirebaseRDBProvider.IMAGE.key).value?.toString()

                retrievedCourt = CourtModel(name, city, image, address)
            } else {
                context.showToast(context.getString(R.string.court_snapshot_fail))
            }
        }.addOnFailureListener {
            context.showToast(context.getString(R.string.court_snapshot_call_fail))
        }

        return retrievedCourt
    }
}