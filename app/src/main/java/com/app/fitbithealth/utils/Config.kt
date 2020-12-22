package com.app.fitbithealth.utils

import com.app.fitbithealth.BuildConfig

class Config {
    companion object {
        const val FIT_BIT_SHARED_PREFERENCE = "FitBitSharedPreference"

        const val CUSTOM_ERROR = "CustomError"

        const val NETWORK_ERROR = "InternetError"

        const val OAUTH_URL = "https://www.fitbit.com/oauth2/authorize?response_type=code" +
                "&client_id=${BuildConfig.APP_CLIENT_ID}" +
                "&redirect_uri=${BuildConfig.APP_CALLBACK_URL}" +
                "&scope=activity%20nutrition%20heartrate%20location%20" +
                "nutrition%20profile%20settings%20sleep%20social%20weight"

        const val GRANT_TYPE_REFRESH_TOKEN = "refresh_token"

        const val GRANT_TYPE_AUTHORIZATION_TOKEN = "authorization_code"
    }
}