package com.app.fitbithealth.ui.workout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.fitbithealth.common.extension.DATE_FORMAT_YYYY_MM_DD
import com.app.fitbithealth.common.extension.toFormatDate
import com.app.fitbithealth.model.ActivitiesModel
import com.app.fitbithealth.model.ActivitiesResponseModel
import com.app.fitbithealth.model.RequestState
import com.app.fitbithealth.shareddata.base.BaseView
import com.app.fitbithealth.shareddata.repo.UserRepo
import io.reactivex.disposables.CompositeDisposable
import retrofit2.Response
import java.util.*

class WorkoutViewModel(private val mUserRepository: UserRepo) : ViewModel() {
    private val mLDActivitiesRequest =
        MutableLiveData<RequestState<Response<ActivitiesResponseModel>>>()

    private val mActivitiesList = ArrayList<ActivitiesModel>()
    private var mSelectedDate: Date = Date()
    private var doesNextPage: Boolean = true

    fun getActivitiesRequest(): LiveData<RequestState<Response<ActivitiesResponseModel>>> =
        mLDActivitiesRequest

    fun getActivitiesByDate(
        isInternetConnected: Boolean,
        baseView: BaseView,
        disposable: CompositeDisposable
    ) {
        if (doesNextPage) {
            mUserRepository.getActivitiesByDate(
                mSelectedDate.toFormatDate(DATE_FORMAT_YYYY_MM_DD), mActivitiesList.size,
                isInternetConnected, baseView, disposable, mLDActivitiesRequest
            )
        }
    }

    fun getActivitiesList(): ArrayList<ActivitiesModel> = mActivitiesList

    fun getSelectedDate() = mSelectedDate

    fun setNextPage(nextPage: Boolean) {
        doesNextPage = nextPage
    }

    fun clearPagination() {
        doesNextPage = true
    }

    fun setSelectedDate(date: Date) {
        mSelectedDate = date
    }
}