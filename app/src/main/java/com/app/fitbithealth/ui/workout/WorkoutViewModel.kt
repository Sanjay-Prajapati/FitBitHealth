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
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import retrofit2.Response
import java.util.*
import javax.inject.Inject

/**
 * This view model class is used for activities list fetching and store in objects
 * @param mUserRepository User related API calls reference
 */
@HiltViewModel
class WorkoutViewModel @Inject constructor(private val mUserRepository: UserRepo) : ViewModel() {
    /**
     * Activities live data object
     */
    private val mLDActivitiesRequest =
        MutableLiveData<RequestState<Response<ActivitiesResponseModel>>>()

    /**
     * List of Activities stored in this object
     */
    private val mActivitiesList = ArrayList<ActivitiesModel>()

    /**
     * User selected date
     * By default it stored today's date
     */
    private var mSelectedDate: Date = Date()

    /**
     * Whether next page is available or not in pagination
     */
    private var doesNextPage: Boolean = true

    /**
     * getter method for activities live data reference
     */
    fun getActivitiesRequest(): LiveData<RequestState<Response<ActivitiesResponseModel>>> =
        mLDActivitiesRequest

    /**
     * To call the users Activities list API based on date
     * @param isInternetConnected whether internet available or not
     * @param baseView Reference of common error message display
     * @param disposable Composite disposable reference
     */
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

    /**
     * getter method for stored activities list
     */
    fun getActivitiesList(): ArrayList<ActivitiesModel> = mActivitiesList

    /**
     * getter method for selected date
     */
    fun getSelectedDate() = mSelectedDate

    /**
     * setter method for next page exist or not
     * @param nextPage does next page exist or not
     */
    fun setNextPage(nextPage: Boolean) {
        doesNextPage = nextPage
    }

    /**
     * to clear the pagination and set the default value
     */
    fun clearPagination() {
        doesNextPage = true
    }

    /**
     * setter method for user selected date store
     */
    fun setSelectedDate(date: Date) {
        mSelectedDate = date
    }
}