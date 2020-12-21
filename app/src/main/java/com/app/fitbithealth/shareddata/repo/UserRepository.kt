package com.app.fitbithealth.shareddata.repo

import com.app.fitbithealth.model.UserHolder
import com.app.fitbithealth.shareddata.endpoint.ApiEndPoint

class UserRepository(
    private val mApiEndPoint: ApiEndPoint,
    private val mUserHolder: UserHolder
) : UserRepo {
}