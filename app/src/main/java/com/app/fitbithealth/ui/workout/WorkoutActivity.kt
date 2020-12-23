package com.app.fitbithealth.ui.workout

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.fitbithealth.R
import com.app.fitbithealth.common.extension.DATE_FORMAT_DD_MM_YYYY
import com.app.fitbithealth.common.extension.snack
import com.app.fitbithealth.common.extension.toFormatDate
import com.app.fitbithealth.common.helper.RxHelper
import com.app.fitbithealth.databinding.ActivityWorkoutBinding
import com.app.fitbithealth.model.ActivitiesModel
import com.app.fitbithealth.model.ActivitiesResponseModel
import com.app.fitbithealth.shareddata.base.BaseActivity
import com.app.fitbithealth.utils.Config
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

/**
 * To list the user workouts
 */
class WorkoutActivity : BaseActivity<ActivityWorkoutBinding>() {
    companion object {
        /**
         * to start the workout activity
         * @param context calling activity context reference
         */
        fun start(context: Context) {
            context.startActivity(Intent(context, WorkoutActivity::class.java))
        }
    }

    private val mViewModel: WorkoutViewModel by viewModel()
    private lateinit var mFilterDateDialog: DatePickerDialog
    private lateinit var mFilterCalendar: Calendar
    private var mEndWithAuto = false
    private lateinit var mAdapter: WorkoutAdapter

    override fun getResource(): Int = R.layout.activity_workout

    override fun initView() {
        initFilterAndActivities()
        updateFilterData()
        getActivities()
    }

    /**
     * To Observe the live data observable objects
     */
    override fun initObserver() {
        mViewModel.getActivitiesRequest().observe(this, { response ->
            response?.also { requestState ->
                if (requestState.progress) showProgress() else hideProgress()
                requestState.apiResponse?.body()?.apply {
                    bindActivitiesData()
                }
                requestState.error?.let { errorObj ->
                    when (errorObj.errorState) {
                        Config.NETWORK_ERROR ->
                            displayMessage(getString(R.string.text_error_network))
                        Config.CUSTOM_ERROR ->
                            errorObj.customMessage
                                ?.let { displayMessage(it) }
                    }
                }
            }
        })
    }

    /**
     * To bind the activity data
     */
    private fun ActivitiesResponseModel.bindActivitiesData() {
        mViewModel.setNextPage(!this.paginationModel?.nextPageLink.isNullOrEmpty())

        activityList?.also {
            mAdapter.updateMoreActivities(it)
        }

        if (mViewModel.getActivitiesList().isEmpty()) {
            showNoActivityFound(true)
        } else {
            showNoActivityFound(false)
        }
        mEndWithAuto = false
    }

    /**
     * To show progress in pagination API call
     */
    private fun showProgress() {
        var needToAdd = true
        if (mViewModel.getActivitiesList().size > 0
            && mViewModel.getActivitiesList()[mViewModel.getActivitiesList().size - 1]
                .doLoadMore == WorkoutAdapter.TYPE_LOAD_MORE
        ) {
            needToAdd = false
        }
        if (needToAdd) {
            val activitiesModel = ActivitiesModel()
            activitiesModel.doLoadMore = WorkoutAdapter.TYPE_LOAD_MORE
            mViewModel.getActivitiesList().add(activitiesModel)
            mBinding.rvExercise.post {
                mAdapter.notifyItemChanged(mViewModel.getActivitiesList().size - 1)
            }
        }
    }

    /**
     *  To hide progress in pagination API call
     */
    private fun hideProgress() {
        if (mViewModel.getActivitiesList().size > 0
            && mViewModel.getActivitiesList()[mViewModel.getActivitiesList().size - 1]
                .doLoadMore == WorkoutAdapter.TYPE_LOAD_MORE
        ) {
            mViewModel.getActivitiesList().removeAt(mViewModel.getActivitiesList().size - 1)
            mBinding.rvExercise.post {
                mAdapter.notifyItemRemoved(mViewModel.getActivitiesList().size - 1)
            }
        }
    }

    /**
     * To show and hide no activity found message view
     * @param isShow boolean params for show and hide
     */
    private fun showNoActivityFound(isShow: Boolean) {
        mBinding.rvExercise.visibility = if (isShow) View.GONE else View.VISIBLE
        mBinding.tvFilterDesc.visibility = if (isShow) View.GONE else View.VISIBLE
        mBinding.tvNotFound.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    override fun handleListener() {
        /**
         * to show case the date picker dialog
         */
        RxHelper.onClick(mBinding.tvFilter, mDisposable) {
            mFilterDateDialog.show()
        }

        /**
         * this scroll listener helps to call the pagination API
         */
        mBinding.rvExercise.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val mLayoutManager = mBinding.rvExercise.layoutManager
                val visibleItemCount = mLayoutManager!!.childCount
                val totalItemCount = mLayoutManager.itemCount

                var firstVisibleItemPosition = 0
                if (mLayoutManager is LinearLayoutManager) {
                    firstVisibleItemPosition = mLayoutManager.findLastVisibleItemPosition()
                }

                if (visibleItemCount + firstVisibleItemPosition >= totalItemCount) {
                    if (!mEndWithAuto) {
                        mEndWithAuto = true

                        if (totalItemCount > 2) {
                            getActivities()
                        }
                    }
                } else {
                    mEndWithAuto = false
                }
            }
        })
    }

    override fun displayMessage(message: String) {
        mBinding.root.snack(message)
    }

    /**
     * Initialize filter dialog and activities recyclerview objects
     */
    private fun initFilterAndActivities() {
        mFilterCalendar = Calendar.getInstance()

        mFilterDateDialog = DatePickerDialog(
            this,
            { _, year, monthOfYear, dayOfMonth ->
                val newCalendarObj = Calendar.getInstance()
                newCalendarObj.apply {
                    set(Calendar.YEAR, year)
                    set(Calendar.MONTH, monthOfYear)
                    set(Calendar.DAY_OF_MONTH, dayOfMonth)
                }
                mFilterCalendar = newCalendarObj
                mViewModel.setSelectedDate(mFilterCalendar.time)
                updateFilterData()
                getActivities()
            },
            Calendar.getInstance().get(Calendar.YEAR),
            Calendar.getInstance().get(Calendar.MONTH),
            Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        )
        mFilterDateDialog.datePicker.maxDate = (Calendar.getInstance().time as Date).time

        mAdapter = WorkoutAdapter(mViewModel.getActivitiesList())
        mBinding.rvExercise.adapter = mAdapter
    }

    /**
     * To update the filter text view date based on dialog date selection
     */
    private fun updateFilterData() {
        val formattedDate = mViewModel.getSelectedDate().toFormatDate(DATE_FORMAT_DD_MM_YYYY)
        mBinding.tvFilter.text = formattedDate
        mBinding.tvFilterDesc.text =
            String.format(getString(R.string.text_workout_desc), formattedDate)
        mAdapter.clearActivitiesData()
        mViewModel.clearPagination()
        mEndWithAuto = false
        showNoActivityFound(false)
    }

    /**
     * To call the activities list API
     */
    private fun getActivities() {
        mViewModel.getActivitiesByDate(isInternetConnected, this, mDisposable)
    }
}