package com.app.fitbithealth.ui.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.fitbithealth.BuildConfig
import com.app.fitbithealth.common.extension.encodeToBase64
import com.app.fitbithealth.model.AuthResponseModel
import com.app.fitbithealth.model.RequestState
import com.app.fitbithealth.shareddata.base.BaseView
import com.app.fitbithealth.shareddata.repo.UserRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import retrofit2.Response
import javax.inject.Inject

/**
 * This view model class is used for access token fetching from fit bit API
 * @param mUserRepository User authentication related API calls reference
 */
@HiltViewModel
class LoginViewModel @Inject constructor(private val mUserRepository: UserRepo) : ViewModel() {
    /**
     * Auth credentials live data object
     */
    private val mLDAuthCredentialRequest =
        MutableLiveData<RequestState<Response<AuthResponseModel>>>()

    /**
     * getter method for authentication credential live data reference
     */
    fun getAuthCredentialRequest(): LiveData<RequestState<Response<AuthResponseModel>>> =
        mLDAuthCredentialRequest

    /**
     * To call the auth credentials API
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
     */
    fun getAuthCredentials(
        authCode: String?,
        refreshToken: String?,
        grantType: String,
        isInternetConnected: Boolean,
        baseView: BaseView,
        disposable: CompositeDisposable
    ) {
        /**
         * To get the base64 String from client id nd client secret
         */
        val authorizationKey =
            "${BuildConfig.APP_CLIENT_ID}:${BuildConfig.APP_CLIENT_SECRET}".encodeToBase64()

        mUserRepository.getAuthCredentials(
            "Basic $authorizationKey",
            authCode, refreshToken, grantType,
            isInternetConnected, baseView, disposable, mLDAuthCredentialRequest
        )
    }
}