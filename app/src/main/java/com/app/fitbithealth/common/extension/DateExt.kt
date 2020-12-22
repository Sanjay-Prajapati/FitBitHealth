package com.app.fitbithealth.common.extension

import java.text.SimpleDateFormat
import java.util.*

const val DATE_FORMAT_DD_MM_YYYY = "dd MMM, yyyy"

const val DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd"

fun Date.toFormatDate(format: String): String =
    SimpleDateFormat(format, Locale.getDefault()).format(this)