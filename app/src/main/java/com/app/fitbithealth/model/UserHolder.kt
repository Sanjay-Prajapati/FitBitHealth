package com.app.fitbithealth.model

import android.content.SharedPreferences
import com.app.fitbithealth.common.extension.prefString

class UserHolder(preference: SharedPreferences) {
    var mAuthToken by preference.prefString("")
        private set

}