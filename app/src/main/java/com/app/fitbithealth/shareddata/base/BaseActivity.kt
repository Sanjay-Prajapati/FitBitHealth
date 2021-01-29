package com.app.fitbithealth.shareddata.base

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.app.fitbithealth.R
import com.app.fitbithealth.common.extension.hideKeyboard
import com.app.fitbithealth.common.extension.resToast
import com.app.fitbithealth.model.UserHolder
import com.app.fitbithealth.ui.auth.login.LoginActivity
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import com.github.pwittchen.reactivenetwork.library.rx2.internet.observing.InternetObservingSettings
import com.github.pwittchen.reactivenetwork.library.rx2.internet.observing.strategy.SocketInternetObservingStrategy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import org.jetbrains.annotations.NotNull
import timber.log.Timber
import javax.inject.Inject

/**
 * This is the base activity
 * In this class internet connectivity check, toolbar setup, common error message display handled
 * BaseView interface implemented to handle the common API errors
 */
@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
abstract class BaseActivity<VB : ViewDataBinding> : AppCompatActivity(), BaseView {
    lateinit var mBinding: VB
    private lateinit var mToolbar: Toolbar
    @Inject
    lateinit var mUserHolder: UserHolder
    var isInternetConnected: Boolean = true
    open lateinit var mDisposable: CompositeDisposable


    /**
     * Give resource id for example, R.layout.activity_login
     */
    @LayoutRes
    abstract fun getResource(): Int

    /**
     * Initialize the screen views
     */
    abstract fun initView()

    /**
     * Observe the live data observable objects
     */
    abstract fun initObserver()

    /**
     * All click listener
     */
    abstract fun handleListener()

    /**
     * Show snackbar message
     */
    abstract fun displayMessage(message: String)

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDisposable()
        initConnectivity()
        setView(getResource())
    }

    private fun setView(@LayoutRes layoutId: Int) {
        try {
            mBinding = DataBindingUtil.setContentView(this, layoutId)

            initView()
            initObserver()
            handleListener()
        } catch (e: Exception) {
            Timber.e(this.localClassName, e.printStackTrace())
            resToast(e.localizedMessage)
        }
    }

    /**
     * to set toolbar
     * @param toolbar toolbar object
     * @param isBackEnabled to show back arrow or not
     * @param backgroundColor to change background color
     */
    @SuppressLint("RestrictedApi")
    protected fun setToolbar(
        @NotNull toolbar: Toolbar, isBackEnabled: Boolean = false,
        backgroundColor: Int = R.color.colorPrimary
    ) {
        this.mToolbar = toolbar
        super.setSupportActionBar(toolbar)
        toolbar.setBackgroundColor(ContextCompat.getColor(this, backgroundColor))
        supportActionBar?.setDisplayShowTitleEnabled(false)
        if (isBackEnabled) {
            supportActionBar?.setDefaultDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setHomeButtonEnabled(true)
            if (backgroundColor == R.color.white)
                toolbar.setNavigationIcon(R.drawable.vd_back)
            toolbar.setNavigationOnClickListener { onBackPressed() }
        }
    }

    /**
     * this method used for checking internet connection
     * It call the URL every 3 seconds and update the isInternetConnected object
     */
    private fun initConnectivity() {
        val settings = InternetObservingSettings.builder()
            .host("www.google.com")
            .strategy(SocketInternetObservingStrategy())
            .interval(3000)
            .build()

        ReactiveNetwork
            .observeInternetConnectivity(settings)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { isConnectedToHost ->
                isInternetConnected = isConnectedToHost
            }.addTo(mDisposable)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    /**
     * initialize the Composite disposable object
     */
    private fun initDisposable() {
        mDisposable = CompositeDisposable()
    }

    /**
     * To clear the Composite disposable object
     */
    override fun onDestroy() {
        super.onDestroy()
        mDisposable.clear()
        mDisposable.dispose()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        hideKeyboard()
    }

    override fun onUnknownError(error: String?) {
        error?.let {
            Timber.d("Base Activity Unknown error $error")
            displayMessage(error)
        }
        generalErrorAction()
    }

    override fun internalServer() {
        Timber.d("Base Activity API Internal server")
        displayMessage(getString(R.string.text_error_internal_server))
        generalErrorAction()
    }

    override fun onTimeout() {
        Timber.d("Base Activity API Timeout")
        displayMessage(getString(R.string.text_error_timeout))
        generalErrorAction()
    }

    override fun onNetworkError() {
        Timber.d("Base Activity network error")
        displayMessage(getString(R.string.text_error_network))
        generalErrorAction()
    }

    override fun onConnectionError() {
        Timber.d("Base Activity internet issue")
        displayMessage(getString(R.string.text_error_connection))
        generalErrorAction()
    }

    override fun onServerDown() {
        Timber.d("Base Activity Server Connection issue")
        displayMessage(getString(R.string.text_server_connection))
        generalErrorAction()
    }

    override fun generalErrorAction() {
        Timber.d("This method will use in child class for performing common task for all error")
    }

    /**
     * @param progressBar progress bar view reference
     * @param isShow to show and hide
     */
    fun showLoadingIndicator(progressBar: View, isShow: Boolean) {
        progressBar.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    /**
     * On 401 status code this method invoke
     */
    override fun autoLogout() {
        mUserHolder.clearData()
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(
            Intent.FLAG_ACTIVITY_CLEAR_TOP
                    or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        )
        startActivity(intent)
    }
}