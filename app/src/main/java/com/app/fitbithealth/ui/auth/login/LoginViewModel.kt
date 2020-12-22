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
import io.reactivex.disposables.CompositeDisposable
import retrofit2.Response

class LoginViewModel(private val mUserRepository: UserRepo) : ViewModel() {
    private val mLDAuthCredentialRequest =
        MutableLiveData<RequestState<Response<AuthResponseModel>>>()

    fun getAuthCredentialRequest(): LiveData<RequestState<Response<AuthResponseModel>>> =
        mLDAuthCredentialRequest

    fun getAuthCredentials(
        authCode: String?,
        refreshToken: String?,
        grantType: String,
        isInternetConnected: Boolean,
        baseView: BaseView,
        disposable: CompositeDisposable
    ) {
        val authorizationKey =
            "${BuildConfig.APP_CLIENT_ID}:${BuildConfig.APP_CLIENT_SECRET}".encodeToBase64()

        mUserRepository.getAuthCredentials(
            "Basic $authorizationKey",
            authCode, refreshToken, grantType,
            isInternetConnected, baseView, disposable, mLDAuthCredentialRequest
        )
    }
}