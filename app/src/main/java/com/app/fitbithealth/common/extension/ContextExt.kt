package com.app.fitbithealth.common.extension

import android.content.Context
import android.content.SharedPreferences

/**
 * This file contains context related extension functions
 */

fun Context.getPrefInstance(prefName: String): SharedPreferences =
    this.getSharedPreferences(prefName, Context.MODE_PRIVATE)
