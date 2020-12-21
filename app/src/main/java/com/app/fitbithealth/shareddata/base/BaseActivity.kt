package com.app.fitbithealth.shareddata.base

import android.annotation.SuppressLint
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
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import com.github.pwittchen.reactivenetwork.library.rx2.internet.observing.InternetObservingSettings
import com.github.pwittchen.reactivenetwork.library.rx2.internet.observing.strategy.SocketInternetObservingStrategy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import org.jetbrains.annotations.NotNull
import org.koin.android.ext.android.inject
import timber.log.Timber

abstract class BaseActivity<VB : ViewDataBinding> : AppCompatActivity(), BaseView {
    lateinit var mBinding: VB
    private lateinit var mToolbar: Toolbar
    val mUserHolder: UserHolder by inject()
    var isInternetConnected: Boolean = true
    open lateinit var mDisposable: CompositeDisposable


    @LayoutRes
    abstract fun getResource(): Int
    abstract fun initView()
    abstract fun initObserver()
    abstract fun handleListener()
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

    private fun initDisposable() {
        mDisposable = CompositeDisposable()
    }

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

    fun showLoadingIndicator(progressBar: View, isShow: Boolean) {
        progressBar.visibility = if (isShow) View.VISIBLE else View.GONE
    }
}