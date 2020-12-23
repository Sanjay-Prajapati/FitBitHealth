package com.app.fitbithealth.common.extension

import android.util.Base64

/**
 * Extension function for get Base64 string
 */
fun String.encodeToBase64(): String {
    return Base64.encodeToString(this.toByteArray(charset("UTF-8")), Base64.NO_WRAP)
}