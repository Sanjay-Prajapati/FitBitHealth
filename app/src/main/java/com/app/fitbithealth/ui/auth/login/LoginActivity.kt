package com.app.fitbithealth.ui.auth.login

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.viewModels
import com.app.fitbithealth.R
import com.app.fitbithealth.common.extension.snack
import com.app.fitbithealth.common.helper.RxHelper
import com.app.fitbithealth.databinding.ActivityLoginBinding
import com.app.fitbithealth.shareddata.base.BaseActivity
import com.app.fitbithealth.ui.workout.WorkoutActivity
import com.app.fitbithealth.utils.Config
import com.app.fitbithealth.utils.Config.Companion.OAUTH_URL
import dagger.hilt.android.AndroidEntryPoint

/**
 * OAuth Authentication implementation
 */
@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>() {
    companion object {
        /**
         * to start the login activity
         * @param context calling activity context reference
         */
        fun start(context: Context) {
            context.startActivity(Intent(context, LoginActivity::class.java))
        }
    }

    private val mViewModel: LoginViewModel by viewModels()

    override fun getResource(): Int = R.layout.activity_login

    /**
     * This method handle the callback from browser
     * @param intent contains the deep link data
     */
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.data?.also { uri ->
            if (uri.scheme == "https") {
                val authCode = uri.getQueryParameter("code")
                if (!authCode.isNullOrBlank()) {
                    /**
                     * storing authCode in sharedPreference
                     */
                    mUserHolder.setAuthCode(authCode)
                    callAuthCredentialsApi()
                }
            }
        }
    }

    override fun initView() {
        // no need to implement
    }

    /**
     * To Observe the live data observable objects
     */
    override fun initObserver() {
        mViewModel.getAuthCredentialRequest().observe(this, { response ->
            response?.also { requestState ->
                showLoadingIndicator(mBinding.progressBar, requestState.progress)
                requestState.apiResponse?.body()?.also { model ->
                    if (model.accessToken != null && model.refreshToken != null && model.userId != null) {
                        /**
                         * store the user auth credentials in shared preference
                         */
                        mUserHolder.setAuthCredentials(
                            model.accessToken,
                            model.refreshToken,
                            model.userId
                        )
                        WorkoutActivity.start(this@LoginActivity)
                        finish()
                    }
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

    override fun handleListener() {
        RxHelper.onClick(mBinding.btnLogin, mDisposable) {
            launchWebBrowser()
        }
    }

    override fun displayMessage(message: String) {
        mBinding.root.snack(message)
    }

    /**
     * call this method to get the access Token
     */
    private fun callAuthCredentialsApi() {
        mViewModel.getAuthCredentials(
            mUserHolder.mAuthCode, null, Config.GRANT_TYPE_AUTHORIZATION_TOKEN,
            isInternetConnected, this, mDisposable
        )
    }

    /**
     * To launch the oAuth URL
     */
    private fun launchWebBrowser() {
        val browserIntent = Intent(Intent.ACTION_VIEW)
        browserIntent.data = Uri.parse(OAUTH_URL)
        startActivity(browserIntent)
    }
}