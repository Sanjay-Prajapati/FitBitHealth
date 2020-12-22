package com.app.fitbithealth.shareddata.repo

import androidx.lifecycle.MutableLiveData
import com.app.fitbithealth.model.ActivitiesResponseModel
import com.app.fitbithealth.model.AuthResponseModel
import com.app.fitbithealth.model.RequestState
import com.app.fitbithealth.shareddata.base.BaseView
import io.reactivex.disposables.CompositeDisposable
import retrofit2.Response

interface UserRepo {

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

    fun getActivitiesByDate(
        selectedDate: String,
        currentOffset: Int,
        isInternetConnected: Boolean,
        baseView: BaseView,
        disposable: CompositeDisposable,
        callback: MutableLiveData<RequestState<Response<ActivitiesResponseModel>>>
    )
}