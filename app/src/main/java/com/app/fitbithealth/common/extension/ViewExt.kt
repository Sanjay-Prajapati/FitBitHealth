package com.app.fitbithealth.common.extension

import android.view.View
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar

fun View.snack(@StringRes msg: Int) {
    Snackbar.make(this, context.getString(msg), Snackbar.LENGTH_SHORT).show()
}

fun View.snack(msg: String, duration: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(this, msg, duration).show()
}