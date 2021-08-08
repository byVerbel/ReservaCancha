package com.equipo13.reservacancha.common

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

object FirebaseUtil {

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    fun firebaseAnalyticsEvent(event:String, extras: Bundle.() -> Unit = {}){
        firebaseAnalytics = Firebase.analytics
        firebaseAnalytics.logEvent(event, Bundle().apply(extras))
    }
}