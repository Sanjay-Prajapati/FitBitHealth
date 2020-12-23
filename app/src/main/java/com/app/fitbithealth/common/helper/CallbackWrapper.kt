package com.app.fitbithealth.common.helper

import com.app.fitbithealth.model.ErrorModel
import com.app.fitbithealth.shareddata.base.BaseView
import com.app.fitbithealth.utils.Constants.Companion.STATUS_200
import com.app.fitbithealth.utils.Constants.Companion.STATUS_401
import com.app.fitbithealth.utils.Constants.Companion.STATUS_500
import com.google.gson.Gson
import com.google.gson.JsonParser
import io.reactivex.observers.DisposableObserver
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response
import timber.log.Timber
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import javax.net.ssl.SSLHandshakeException

/**
 *  To handle error code from one place
 */
abstract class CallbackWrapper<T : Response<*>>(
    private val view: BaseView?
) : DisposableObserver<T>() {
    internal abstract fun onApiSuccess(response: T)

    internal abstract fun onApiError(e: Throwable?)

    override fun onNext(response: T) {
        when (response.code()) {
            STATUS_200 -> {
                onApiSuccess(response)
            }
            STATUS_500 -> {
                view?.internalServer()
            }
            STATUS_401 -> {
                view?.autoLogout()
            }
            else -> {
                Timber.d("Unknown Error repo ${response.errorBody()}")
                anotherErrorCode(response.errorBody())
            }
        }
    }

    override fun onError(e: Throwable) {
        Timber.d("Error in API call ${e.printStackTrace()}")
        when (e) {
            is HttpException -> {
                e.response()?.also { response ->
                    when (response.code()) {
                        STATUS_500 -> {
                            view?.internalServer()
                        }
                        STATUS_401 -> {
                            view?.autoLogout()
                        }
                        else -> {
                            anotherErrorCode(response.errorBody())
                        }
                    }
                }
            }
            is SSLHandshakeException -> view?.onServerDown()
            is SocketTimeoutException -> view?.onTimeout()
            is ConnectException -> view?.onConnectionError()
            is IOException -> view?.onNetworkError()
            else -> {
                view?.onUnknownError(e.message)
            }
        }
        onApiError(e)
    }

    override fun onComplete() {
        // no need to implement
    }

    private fun anotherErrorCode(responseBody: ResponseBody?) {
        val errorModel: ErrorModel? = getErrorMessage(responseBody)

        errorModel?.let {
            if (errorModel.errors.isNotEmpty())
                view?.onUnknownError(errorModel.errors[0].message)
        } ?: let {
            view?.internalServer()
        }
        onApiError(null)
    }

    private fun getErrorMessage(responseBody: ResponseBody?): ErrorModel? =
        try {
            responseBody?.let {
                val json = JsonParser().parse(it.string())
                Gson().fromJson(json, ErrorModel::class.java)
            } ?: let {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
}