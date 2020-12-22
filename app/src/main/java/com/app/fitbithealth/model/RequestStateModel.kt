package com.app.fitbithealth.model

import com.google.gson.annotations.SerializedName

data class RequestState<T>(
    var error: ApiError? = null,
    var progress: Boolean = false,
    var apiResponse: T? = null
)

/**
 * @errorState error state defined in the Config.kt class
 * you can set CUSTOM_ERROR, NETWORK_ERROR
 * In case of CUSTOM_ERROR, you have to set customMessage also
 */
data class ApiError(val errorState: String, val customMessage: String? = null)

data class ErrorModel(
    @SerializedName("success")
    val isSuccess:Boolean,
    @SerializedName("errors")
    val errors:ArrayList<Error>
)

data class Error(
    @SerializedName("errorType")
    val errorType:String?,
    @SerializedName("message")
    val message:String?
)
