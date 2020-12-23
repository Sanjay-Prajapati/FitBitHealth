package com.app.fitbithealth.shareddata.repo

import androidx.lifecycle.MutableLiveData
import com.app.fitbithealth.common.extension.formatTo
import com.app.fitbithealth.common.extension.toDate
import com.app.fitbithealth.common.helper.CallbackWrapper
import com.app.fitbithealth.model.*
import com.app.fitbithealth.shareddata.base.BaseView
import com.app.fitbithealth.shareddata.endpoint.ApiEndPoint
import com.app.fitbithealth.utils.Config
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import retrofit2.Response

/**
 * This repository used for API calls
 * @param mApiEndPoint Retrofit API endpoint reference
 * @param mUserHolder Shared preference object reference
 */
class UserRepository(
    private val mApiEndPoint: ApiEndPoint,
    private val mUserHolder: UserHolder
) : UserRepo {

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

    /**
     * To call the users Activities list API based on date
     * @param selectedDate after date for API params
     * @param currentOffset index number from where the next activity will get in API response
     * @param isInternetConnected whether internet available or not
     * @param baseView Reference of common error message display
     * @param disposable Composite disposable reference
     * @param callback live data reference
     */
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
                    .map { response ->
                        response.body()?.let { body ->
                            /**
                             * To get the formatted date and duration and store
                             * in another response model variable
                             */
                            body.activityList?.forEach { activityModel ->
                                val dateAndTime =
                                    activityModel.startTime?.toDate()
                                        ?.formatTo("dd MMM, yyyy'T'hh:mm a")
                                activityModel.displayStartTime = dateAndTime?.let { dateTime ->
                                    val splitDate = dateTime.split("T")
                                    "${splitDate[0]} at ${splitDate[1]}"
                                } ?: let { "" }

                                activityModel.displayDuration = activityModel.duration?.let {
                                    "${it / 60000} min"
                                } ?: let {
                                    "0 min"
                                }
                            }

                            response
                        } ?: let {
                            response
                        }
                    }
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