package com.app.fitbithealth.shareddata.repo

import androidx.lifecycle.MutableLiveData
import com.app.fitbithealth.common.helper.CallbackWrapper
import com.app.fitbithealth.model.*
import com.app.fitbithealth.shareddata.base.BaseView
import com.app.fitbithealth.shareddata.endpoint.ApiEndPoint
import com.app.fitbithealth.utils.Config
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import retrofit2.Response

class UserRepository(
    private val mApiEndPoint: ApiEndPoint,
    private val mUserHolder: UserHolder
) : UserRepo {

    override fun getAuthCredentials(
        authorizationKey: String,
        authCode: String?,
        refreshToken: String?,
        grantType: String,
        isInternetConnected: Boolean,
        baseView: BaseView,
        disposable: CompositeDisposable,
        callback: MutableLiveData<RequestState<Response<AuthResponseModel>>>
    ) {
        if (!isInternetConnected) {
            callback.value = RequestState(error = ApiError(Config.NETWORK_ERROR))
        } else {
            mApiEndPoint.getAuthCredentials(
                authorizationKey = authorizationKey,
                authCode = authCode,
                refreshToken = refreshToken,
                grantType = grantType
            )
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { callback.value = RequestState(progress = true) }
                .subscribeWith(object : CallbackWrapper<Response<AuthResponseModel>>(baseView) {
                    override fun onApiError(e: Throwable?) {
                        callback.value = RequestState(progress = false)
                    }

                    override fun onApiSuccess(response: Response<AuthResponseModel>) {
                        callback.value = RequestState(progress = false, apiResponse = response)
                    }
                }).addTo(disposable)
        }
    }

    override fun getActivitiesByDate(
        selectedDate: String,
        currentOffset: Int,
        isInternetConnected: Boolean,
        baseView: BaseView,
        disposable: CompositeDisposable,
        callback: MutableLiveData<RequestState<Response<ActivitiesResponseModel>>>
    ) {
        if (!isInternetConnected) {
            callback.value = RequestState(error = ApiError(Config.NETWORK_ERROR))
        } else {
            mUserHolder.mAccessToken?.also { token ->
                mApiEndPoint.getActivitiesByDate(token, selectedDate, currentOffset)
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { callback.value = RequestState(progress = true) }
                    .subscribeWith(object :
                        CallbackWrapper<Response<ActivitiesResponseModel>>(baseView) {
                        override fun onApiError(e: Throwable?) {
                            callback.value = RequestState(progress = false)
                        }

                        override fun onApiSuccess(response: Response<ActivitiesResponseModel>) {
                            callback.value = RequestState(progress = false, apiResponse = response)
                        }
                    }).addTo(disposable)
            }
        }
    }
}