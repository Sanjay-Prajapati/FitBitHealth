package com.app.fitbithealth.common.extension

import java.text.SimpleDateFormat
import java.util.*

/**
 * This file contains Date related extension functions and constants
 */

const val DATE_FORMAT_DD_MM_YYYY = "dd MMM, yyyy"

const val DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd"

fun Date.toFormatDate(format: String): String =
    SimpleDateFormat(format, Locale.getDefault()).format(this)

/**
 * To convert the formatted string to date object
 */
@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
fun String.toDate(dateFormat: String = "yyyy-MM-dd'T'HH:mm:ss.SSSZZZ", timeZone: TimeZone = TimeZone.getTimeZone("UTC")): Date {
    val parser = SimpleDateFormat(dateFormat, Locale.getDefault())
    parser.timeZone = timeZone
    return parser.parse(this)
}

/**
 * To format String date from Date object
 */
fun Date.formatTo(dateFormat: String, timeZone: TimeZone = TimeZone.getDefault()): String {
    val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
    formatter.timeZone = timeZone
    return formatter.format(this)
}