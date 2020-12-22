package com.app.fitbithealth.ui.workout

import android.content.Context
import android.content.Intent
import com.app.fitbithealth.R
import com.app.fitbithealth.common.extension.snack
import com.app.fitbithealth.databinding.ActivityWorkoutBinding
import com.app.fitbithealth.shareddata.base.BaseActivity

class WorkoutActivity : BaseActivity<ActivityWorkoutBinding>() {
    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, WorkoutActivity::class.java))
        }
    }

    override fun getResource(): Int = R.layout.activity_workout

    override fun initView() {

    }

    override fun initObserver() {

    }

    override fun handleListener() {

    }

    override fun displayMessage(message: String) {
        mBinding.root.snack(message)
    }
}