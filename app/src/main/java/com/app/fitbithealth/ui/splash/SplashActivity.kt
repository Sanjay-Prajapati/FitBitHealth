package com.app.fitbithealth.ui.splash

import androidx.appcompat.app.AppCompatActivity
import com.app.fitbithealth.model.UserHolder
import com.app.fitbithealth.ui.auth.login.LoginActivity
import com.app.fitbithealth.ui.workout.WorkoutActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * splash screen
 */
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private lateinit var mCoroutineScope: CoroutineScope
    @Inject
    lateinit var mUserHolder: UserHolder

    /**
     * Launch screen based on session exist or not?
     */
    override fun onResume() {
        super.onResume()
        mCoroutineScope = CoroutineScope(Dispatchers.Main)
        mCoroutineScope.launch {
            delay(TimeUnit.SECONDS.toMillis(2))
            mUserHolder.let {
                if (it.mAccessToken.isNullOrBlank()) {
                    LoginActivity.start(this@SplashActivity)
                } else {
                    WorkoutActivity.start(this@SplashActivity)
                }
            }
            finish()
            overridePendingTransition(0, 0)
        }
    }

    override fun onStop() {
        super.onStop()
        mCoroutineScope.cancel()
    }
}