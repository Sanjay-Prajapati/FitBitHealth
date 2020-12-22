package com.app.fitbithealth.model

import android.content.SharedPreferences
import com.app.fitbithealth.common.extension.prefString

class UserHolder(preference: SharedPreferences) {
    var mAccessToken by preference.prefString("")
        private set

    var mRefreshToken by preference.prefString("")
        private set

    var mUserId by preference.prefString("")
        private set

    var mAuthCode by preference.prefString("")
        private set

    fun setAuthCredentials(accessToken: String, refreshToken: String, userId: String) {
        mAccessToken = accessToken
        mRefreshToken = refreshToken
        mUserId = userId
    }

    fun setAuthCode(authCode: String) {
        mAuthCode = authCode
    }

    fun clearData() {
        mAccessToken = ""
        mRefreshToken = ""
        mUserId = ""
        mAuthCode = ""
    }
}