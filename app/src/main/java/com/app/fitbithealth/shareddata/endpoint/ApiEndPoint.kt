package com.app.fitbithealth.shareddata.endpoint

import com.app.fitbithealth.BuildConfig
import com.app.fitbithealth.model.AuthResponseModel
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiEndPoint {

    @POST("/oauth2/token")
    fun getAuthCredentials(
        @Header("Authorization") authorizationKey: String,
        @Header("Content-Type") contentType: String = "application/x-www-form-urlencoded",
        @Query("code") authCode: String?,
        @Query("refresh_token") refreshToken:String?,
        @Query("grant_type") grantType: String,
        @Query("redirect_uri") redirectUrl: String = BuildConfig.APP_CALLBACK_URL
    ): Observable<Response<AuthResponseModel>>
}