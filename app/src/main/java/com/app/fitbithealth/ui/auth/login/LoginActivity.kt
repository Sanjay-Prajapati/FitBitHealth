package com.app.fitbithealth.ui.auth.login

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.app.fitbithealth.R
import com.app.fitbithealth.common.extension.snack
import com.app.fitbithealth.databinding.ActivityLoginBinding
import com.app.fitbithealth.shareddata.base.BaseActivity
import com.app.fitbithealth.ui.workout.WorkoutActivity
import com.app.fitbithealth.utils.Config
import com.app.fitbithealth.utils.Config.Companion.OAUTH_URL
import org.koin.android.viewmodel.ext.android.viewModel

class LoginActivity : BaseActivity<ActivityLoginBinding>() {
    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, LoginActivity::class.java))
        }
    }

    private val mViewModel: LoginViewModel by viewModel()

    override fun getResource(): Int = R.layout.activity_login

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.data?.also { uri ->
            if (uri.scheme == "https") {
                val authCode = uri.getQueryParameter("code")
                if (!authCode.isNullOrBlank()) {
                    mUserHolder.setAuthCode(authCode)
                    callAuthCredentialsApi()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (mUserHolder.mAuthCode.isNullOrEmpty()) {
            launchWebBrowser()
        }
    }

    override fun initView() {

    }

    override fun initObserver() {
        mViewModel.getAuthCredentialRequest().observe(this, { response ->
            response?.also { requestState ->
                showLoadingIndicator(mBinding.progressBar, requestState.progress)
                requestState.apiResponse?.body()?.also { model ->
                    if (model.accessToken != null && model.refreshToken != null && model.userId != null) {
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

    }

    override fun displayMessage(message: String) {
        mBinding.root.snack(message)
    }

    private fun callAuthCredentialsApi() {
        mViewModel.getAuthCredentials(
            mUserHolder.mAuthCode, null, Config.GRANT_TYPE_AUTHORIZATION_TOKEN,
            isInternetConnected, this, mDisposable
        )
    }

    private fun launchWebBrowser() {
        val browserIntent = Intent(Intent.ACTION_VIEW)
        browserIntent.data = Uri.parse(OAUTH_URL)
        startActivity(browserIntent)
    }
}