package com.app.fitbithealth.shareddata.base

/**
 * BaseView interface used for handle common messages
 */
interface BaseView {

    /**
     * To showcase internal server message
     */
    fun internalServer()

    /**
     * To showcase the unknown error messages
     * @param error error message
     */
    fun onUnknownError(error: String?)

    /**
     * To showcase message on SocketTimeoutException
     */
    fun onTimeout()

    /**
     * To showcase message on IOException exception
     */
    fun onNetworkError()

    /**
     * To showcase message on ConnectException exception
     */
    fun onConnectionError()

    /**
     * To perform the action in UI
     */
    fun generalErrorAction()

    /**
     * To clear the session data and navigate user to login screen
     */
    fun autoLogout()

    /**
     * To showcase message on SSLHandshakeException exception
     */
    fun onServerDown()
}