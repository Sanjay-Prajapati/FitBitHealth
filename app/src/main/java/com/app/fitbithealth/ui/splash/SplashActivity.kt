package com.app.fitbithealth.ui.splash

import androidx.appcompat.app.AppCompatActivity
import com.app.fitbithealth.model.UserHolder
import com.app.fitbithealth.ui.auth.login.LoginActivity
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import java.util.concurrent.TimeUnit

class SplashActivity : AppCompatActivity() {
    private lateinit var mCoroutineScope: CoroutineScope
    private val mUserHolder: UserHolder? by inject()

    override fun onResume() {
        super.onResume()
        mCoroutineScope = CoroutineScope(Dispatchers.Main)
        mCoroutineScope.launch {
            delay(TimeUnit.SECONDS.toMillis(5))
            mUserHolder?.let {
                if (it.mAuthToken.isNullOrBlank()) {
                    LoginActivity.start(this@SplashActivity)
                } else {
                    // call main activity
                }
            } ?: let {
                LoginActivity.start(this@SplashActivity)
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