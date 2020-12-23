package com.app.fitbithealth.model

import android.content.SharedPreferences
import com.app.fitbithealth.common.extension.prefString

/**
 * This class is used for store the User related shared preference data
 * @param preference shared preference object
 */
class UserHolder(preference: SharedPreferences) {
    var mAccessToken by preference.prefString("")
        private set

    var mRefreshToken by preference.prefString("")
        private set

    var mUserId by preference.prefString("")
        private set

    var mAuthCode by preference.prefString("")
        private set

    /**
     * To store the auth credentials in shared preference
     */
    fun setAuthCredentials(accessToken: String, refreshToken: String, userId: String) {
        mAccessToken = "Bearer $accessToken"
        mRefreshToken = refreshToken
        mUserId = userId
    }

    /**
     * to store the authCode in shared preference
     */
    fun setAuthCode(authCode: String) {
        mAuthCode = authCode
    }

    /**
     * To clear the user credentials
     * This method called from autoLogout
     * WE can call it on logout functionality
     */
    fun clearData() {
        mAccessToken = ""
        mRefreshToken = ""
        mUserId = ""
        mAuthCode = ""
    }
}