package com.equipo13.reservacancha.provider

import android.content.SharedPreferences
import android.text.Editable
import com.equipo13.reservacancha.R
import com.equipo13.reservacancha.common.FirebaseUtil
import com.equipo13.reservacancha.common.isValidEmail
import com.equipo13.reservacancha.model.CourtModel
import com.equipo13.reservacancha.model.TimeSlotModel
import com.google.firebase.auth.AuthCredential
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

enum class FirebaseLoginType {
    BASIC, GOOGLE, FACEBOOK
}

object FirebaseRDB {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth

    // User methods
    fun loginUser(
        sp: SharedPreferences,
        email: Editable,
        password: Editable,
        remember: (sp: SharedPreferences, id: String, email: String, provider: String) -> Unit,
        success: (id: String, provider: String) -> Unit,
        failure: (message: Int) -> Unit
    )   {
        firebaseAuth = Firebase.auth

        if (email.isNotEmpty() && password.isNotEmpty()){
            firebaseAuth.signInWithEmailAndPassword(email.toString(), password.toString())
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        task.result?.user?.uid?.let { id ->
                            remember(sp, id, email.toString(), FirebaseLoginType.BASIC.name)
                            FirebaseUtil.firebaseAnalyticsEvent("login"){
                                putString("message", "Login with BASIC")
                            }
                            success(id, FirebaseLoginType.BASIC.name)
                        }

                    } else {
                        failure(R.string.login_unregistered)
                    }
                }
        } else  {
            failure(R.string.missing_fields)
        }
    }

    fun loginFacebook(
        credential: AuthCredential,
        sp: SharedPreferences,
        remember: (sp: SharedPreferences, id: String, email: String, provider: String) -> Unit,
        success: (userId: String, provider: String) -> Unit,
        failure: (message: Int) -> Unit
    ){
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val userId = task.result?.user?.uid?:""
                val userEmail = task.result?.user?.email?:""

                val userDataMap = mapOf(
                    "name" to "No name YET", // TODO("Get FB name")
                    "email" to userEmail,
                    "phone" to "No phone YET" // TODO("Get FB name")
                )

                setNewUserDb(userId, userDataMap, {}, failure)

                remember(sp, userId, userEmail, FirebaseLoginType.FACEBOOK.name)
                FirebaseUtil.firebaseAnalyticsEvent("login") {
                    putString("message", "Login with FACEBOOK")
                }
                success(userEmail, FirebaseLoginType.FACEBOOK.name)
            }
            else{
                failure(R.string.invalid_register)
            }
        }
    }

    fun registerBasic(
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
                                "phone" to phone.toString()
                            )

                            setNewUserDb(userId, userDataMap, success, failure)

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

    private fun setNewUserDb(userId : String, userDataMap : Map<String,String>, success: () -> Unit, failure: (message: Int) -> Unit){
        databaseReference = Firebase.database.reference

        databaseReference.child(FirebaseRDBProvider.USERS.key).child(userId).setValue(userDataMap).addOnCompleteListener { task ->
            if(task.isSuccessful){
                success()
            } else {
                failure(R.string.db_register_fail)
            }
        }
    }

    fun getUser(userId: String, success: (name:String, email:String, phone:String) -> Unit, failure: (message: Int) -> Unit){
        databaseReference = Firebase.database.reference.child(FirebaseRDBProvider.USERS.key).child(userId)

        databaseReference.addValueEventListener( object : ValueEventListener{
            override fun onDataChange(userSnapshot: DataSnapshot) {
                if (userSnapshot.exists() && userSnapshot.value != null) {
                    val name = userSnapshot.child("name").value?.toString()?:"No name found"
                    val email = userSnapshot.child("email").value?.toString()?:"No email found"
                    val phone = userSnapshot.child("phone").value?.toString()?:"No phone found"

                    success(name, email, phone)
                }
                else {
                    failure(R.string.register_null)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                failure(R.string.db_user_load_fail)
            }

        })
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

    fun setCourtBooking(courtId: String, scheduleMap: Map<String?, Boolean?>, success: (message: Int) -> Unit, failure: (message: Int) -> Unit){
        databaseReference = Firebase.database.reference.child(FirebaseRDBProvider.COURTS.key).child(courtId)

        databaseReference.child(FirebaseRDBProvider.SCHEDULE.key).updateChildren(scheduleMap).addOnCompleteListener { task ->
            if(task.isSuccessful){
                success(R.string.db_set_reserve_successful)
            } else {
                failure(R.string.db_set_reservation_fail)
            }
        }
    }
}