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

    fun getCourts(courtMutableList: MutableList<CourtModel>, success: () -> Unit, failure: () -> Unit){
        courtsRef = Firebase.database.reference.child(FirebaseRDBProvider.COURTS.key)

        courtsRef.addValueEventListener( object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists() && snapshot.value != null){
                    for (courtSnapshot in snapshot.children){
                        courtSnapshot.getValue<CourtModel>()?.let { dbCourt ->
                            courtMutableList.add(dbCourt)
                        }
                    }
                    success()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                failure()
            }

        })
    }
}