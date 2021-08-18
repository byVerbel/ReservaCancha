package com.equipo13.reservacancha.provider

import android.content.SharedPreferences
import android.text.Editable
import com.equipo13.reservacancha.R
import com.equipo13.reservacancha.common.FirebaseUtil
import com.equipo13.reservacancha.common.isValidEmail
import com.equipo13.reservacancha.model.CourtModel
import com.equipo13.reservacancha.model.TimeSlotModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
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
    CITY("city"), STATE("state"), ADDRESS("address"), SCHEDULE("schedule")
}

enum class FirebaseRegisterType {
    BASIC, GOOGLE, FACEBOOK
}

object FirebaseRDB {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth

    // Users methods
    fun loginUser(
        sp: SharedPreferences,
        email: Editable,
        password: Editable,
        remember: (sp: SharedPreferences, id: String, email: String) -> Unit,
        success: (id: String) -> Unit,
        failure: (message: Int) -> Unit
    )   {
        firebaseAuth = Firebase.auth

        if (email.isNotEmpty() && password.isNotEmpty()){
            firebaseAuth.signInWithEmailAndPassword(email.toString(), password.toString())
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        task.result?.user?.uid?.let { id ->
                            remember(sp, id, email.toString())
                            FirebaseUtil.firebaseAnalyticsEvent("login"){
                                putString("message", "Login to app")
                            }
                            success(id)
                        }

                    } else {
                        failure(R.string.invalid_user)
                    }
                }
        } else  {
            failure(R.string.missing_fields)
        }

    }

    fun registerUser(
        name: Editable,
        email: Editable,
        password: Editable,
        phone: Editable,
        success: () -> Unit,
        failure: (message: Int) -> Unit
    )   {
        firebaseAuth = Firebase.auth

        if (email.isNotEmpty() && password.isNotEmpty() && name.isNotEmpty() && phone.isNotEmpty()){

            if (email.toString().isValidEmail()){

                firebaseAuth.createUserWithEmailAndPassword(email.toString(),password.toString())
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val userId = task.result?.user?.uid?: ""

                            val userDataMap: Map <String,String> = mapOf(
                                "name" to name.toString(),
                                "email" to email.toString(),
                                "password" to password.toString(),
                                "phone" to phone.toString()
                            )

                            setUser(userId, userDataMap, success, failure)

                        } else {
                            failure(R.string.invalid_register)
                        }
                    }
            } else {

                failure(R.string.invalid_email)
            }
        } else {

            failure(R.string.missing_fields)
        }
    }

    private fun setUser(userId : String, userDataMap : Map<String,String>, success: () -> Unit, failure: (message: Int) -> Unit){
        databaseReference = Firebase.database.reference

        databaseReference.child(FirebaseRDBProvider.USERS.key).child(userId).setValue(userDataMap).addOnCompleteListener { task ->
            if(task.isSuccessful){
                success()
            } else {
                failure(R.string.db_register_fail)
            }
        }
    }

    fun getUserBookings(){
        TODO()
    }


    // Courts Methods
    fun getCourts(courtMutableList: MutableList<CourtModel>, success: () -> Unit, failure: (message: Int) -> Unit){
        databaseReference = Firebase.database.reference.child(FirebaseRDBProvider.COURTS.key)

        databaseReference.addValueEventListener( object : ValueEventListener{
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
                failure(R.string.courts_load_fail)
            }

        })
    }

    fun getCourtSchedule(court: String, timeSlotList: MutableList<TimeSlotModel>, success: () -> Unit, failure: (message: Int) -> Unit){
        databaseReference = Firebase.database.reference
            .child(FirebaseRDBProvider.COURTS.key)
            .child(court)
            .child(FirebaseRDBProvider.SCHEDULE.key)

        databaseReference.addValueEventListener( object : ValueEventListener{
            override fun onDataChange(scheduleSnapshot: DataSnapshot) {
                if (scheduleSnapshot.exists() && scheduleSnapshot.value != null){
                    for (timeSlot in scheduleSnapshot.children){
                        timeSlot.getValue<Boolean>()?.let { status ->
                            timeSlotList.add(TimeSlotModel(timeSlot.key, status))
                        }
                    }
                    success()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                failure(R.string.court_schedule_load_failed)
            }

        })
    }

    fun setCourtBooking(courtId: String, scheduleMap: Map<String?, Boolean?>, success: () -> Unit, failure: (message: Int) -> Unit){
        databaseReference = Firebase.database.reference.child(FirebaseRDBProvider.COURTS.key).child(courtId)

        databaseReference.child(FirebaseRDBProvider.SCHEDULE.key).updateChildren(scheduleMap).addOnCompleteListener { task ->
            if(task.isSuccessful){
                success()
            } else {
                failure(R.string.db_set_booking_fail)
            }
        }

        /*setValue(scheduleMap).addOnCompleteListener { task ->
            if(task.isSuccessful){
                success()
            } else {
                failure(R.string.db_set_booking_fail)
            }
        }*/
    }
}