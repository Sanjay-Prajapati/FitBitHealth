package com.app.fitbithealth.shareddata.repo

import androidx.lifecycle.MutableLiveData
import com.app.fitbithealth.model.ActivitiesResponseModel
import com.app.fitbithealth.model.AuthResponseModel
import com.app.fitbithealth.model.RequestState
import com.app.fitbithealth.shareddata.base.BaseView
import io.reactivex.disposables.CompositeDisposable
import retrofit2.Response

interface UserRepo {

    /**
     * To call the auth credentials API with the help of RxRetrofit
     * @param authorizationKey base64 string based on client id and client secret
     * @param authCode this code we are getting from callback url
     * For updating access token pass this params as null
     * @param refreshToken pass this refresh token in case of update the access token
     * for login pass this params as null
     * @param grantType pass params value authorization_code / refresh_token
     * to get the access token pass params value authorization_code as a String
     * to update the access token with the help of refresh token pass params value refresh_token as
     * a String
     * @param isInternetConnected whether internet available or not
     * @param baseView Reference of common error message display
     * @param disposable Composite disposable reference
     * @param callback live data reference
     */
    fun getAuthCredentials(
        authorizationKey: String,
        authCode: String?,
        refreshToken: String?,
        grantType: String,
        isInternetConnected: Boolean,
        baseView: BaseView,
        disposable: CompositeDisposable,
        callback: MutableLiveData<RequestState<Response<AuthResponseModel>>>
    )

    /**
     * To call the users Activities list API based on date
     * @param selectedDate after date for API params
     * @param currentOffset index number from where the next activity will get in API response
     * @param isInternetConnected whether internet available or not
     * @param baseView Reference of common error message display
     * @param disposable Composite disposable reference
     * @param callback live data reference
     */
    fun getActivitiesByDate(
        selectedDate: String,
        currentOffset: Int,
        isInternetConnected: Boolean,
        baseView: BaseView,
        disposable: CompositeDisposable,
        callback: MutableLiveData<RequestState<Response<ActivitiesResponseModel>>>
    )
}