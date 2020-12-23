package com.app.fitbithealth.model

import com.google.gson.annotations.SerializedName

data class ActivitiesResponseModel(
    @SerializedName("activities")
    val activityList: ArrayList<ActivitiesModel>?,
    @SerializedName("pagination")
    val paginationModel: PaginationModel?
)

data class ActivitiesModel(
    @SerializedName("activeDuration")
    val activeDuration: String? = null,
    @SerializedName("activityName")
    val activityName: String? = null,
    @SerializedName("calories")
    val calories: String? = null,
    @SerializedName("distance")
    val distance: String? = null,
    @SerializedName("distanceUnit")
    val distanceUnit: String? = null,
    @SerializedName("duration")
    val duration: Long? = null,
    @SerializedName("manualValuesSpecified")
    val manualValuesSpecified: ManualValuesSpecified? = null,
    @SerializedName("speed")
    val speed: String? = null,
    @SerializedName("startTime")
    val startTime: String? = null,
    @SerializedName("steps")
    val steps: String? = null,
    var displayDuration: String = "",
    var displayStartTime: String = "",
    var doLoadMore: Int = 0,
    )

data class ManualValuesSpecified(
    @SerializedName("calories")
    val calories: Boolean?,
    @SerializedName("distance")
    val distance: Boolean?,
    @SerializedName("steps")
    val steps: Boolean?
)

data class PaginationModel(
    @SerializedName("next")
    val nextPageLink: String?
)