package com.equipo13.reservacancha.provider

import com.equipo13.reservacancha.model.CourtModel
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

enum class FirebaseRDBProvider (val key: String) {
    // Schemes
    USERS("Users"), COURTS("Courts"),

    // Shared
    ID("id"),
    NAME("name"), PHONE("phone"), IMAGE("image"), EMAIL("email"),

    // Courts
    CITY("city"), STATE("state"), ADDRESS("address")
}

object FirebaseRDB {

    private lateinit var courtsRef: DatabaseReference
    private lateinit var usersRef: DatabaseReference

    fun getCourts(courtMutableList: MutableList<CourtModel>, success: () -> Unit, failure: (message: String) -> Unit){
        courtsRef = Firebase.database.reference.child(FirebaseRDBProvider.COURTS.key)

        courtsRef.addValueEventListener( object : ValueEventListener{
            override fun onDataChange(courtsSnapshot: DataSnapshot) {
                if (courtsSnapshot.exists() && courtsSnapshot.value != null){
                    for (court in courtsSnapshot.children){
                        court.getValue<CourtModel>()?.let { dbCourt ->
                            dbCourt.id = court.key // This has to change when I remake the DB
                            courtMutableList.add(dbCourt)
                        }
                    }
                    success()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                failure("Failed to load courts")
            }

        })
    }
}