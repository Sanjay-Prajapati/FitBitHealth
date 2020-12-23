package com.app.fitbithealth.shareddata.endpoint

import com.app.fitbithealth.BuildConfig
import com.app.fitbithealth.model.ActivitiesResponseModel
import com.app.fitbithealth.model.AuthResponseModel
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * Fit bit API end point
 */
interface ApiEndPoint {

    @POST("/oauth2/token")
    fun getAuthCredentials(
        @Header("Authorization") authorizationKey: String,
        @Header("Content-Type") contentType: String = "application/x-www-form-urlencoded",
        @Query("code") authCode: String?,
        @Query("refresh_token") refreshToken: String?,
        @Query("grant_type") grantType: String,
        @Query("redirect_uri") redirectUrl: String = BuildConfig.APP_CALLBACK_URL
    ): Observable<Response<AuthResponseModel>>

    @GET("/1/user/-/activities/list.json")
    fun getActivitiesByDate(
        @Header("Authorization") accessToken: String,
        @Query("afterDate") afterDate: String,
        @Query("offset") dataFrom: Int,
        @Query("sort") sort: String = "asc",
        @Query("limit") perPageData: Int = 5
    ): Observable<Response<ActivitiesResponseModel>>
}