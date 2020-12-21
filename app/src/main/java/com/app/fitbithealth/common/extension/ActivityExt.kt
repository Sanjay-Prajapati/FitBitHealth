package com.app.fitbithealth.common.extension

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.app.fitbithealth.R

fun Activity.resToast(@StringRes res:Int){
    Toast.makeText(this, res, Toast.LENGTH_LONG).show()
}

fun Activity.resToast(res:String){
    Toast.makeText(this, res, Toast.LENGTH_LONG).show()
}

fun Activity.hideKeyboard() {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    var view = currentFocus
    if (view == null) {
        view = View(this)
    }
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Activity.showAlert(
    title: String,
    msg: String?,
    positiveButtonText: String,
    negativeButtonText: String? = null,
    listener: DialogInterface.OnClickListener
) {
    val builder = AlertDialog.Builder(this)
        .setTitle(title)
        .setMessage(msg)
    if (negativeButtonText == null) {
        builder.setNeutralButton(positiveButtonText, listener)
    } else {
        builder.setPositiveButton(positiveButtonText, listener)
            .setNegativeButton(negativeButtonText, listener)
    }

    val dialog = builder.create()

    dialog.setOnShowListener {
        val btnYes = dialog.getButton(DialogInterface.BUTTON_POSITIVE)

        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(10, 0, 0, 0)
        btnYes.layoutParams = params

        val btnNo = dialog.getButton(DialogInterface.BUTTON_NEGATIVE)
        btnNo.setBackgroundColor(ContextCompat.getColor(this, android.R.color.white))
        btnNo.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
    }
    if (!this.isFinishing)
        dialog.show()
}

