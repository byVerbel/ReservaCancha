package com.equipo13.reservacancha.common

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.widget.Toast

// String extensions

fun String?.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

// Context extensions

fun Context.showToast(message: String, length: Int = Toast.LENGTH_SHORT) = Toast.makeText(this, message, length).show()

fun <T> Context.openActivity(it: Class<T>, extras: Bundle.() -> Unit = {}) {
    val intent = Intent(this, it)
    intent.putExtras(Bundle().apply(extras))
    startActivity(intent)
}

