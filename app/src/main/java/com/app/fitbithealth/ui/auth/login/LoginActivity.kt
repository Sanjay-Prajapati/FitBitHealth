package com.app.fitbithealth.ui.auth.login

import android.content.Context
import android.content.Intent
import com.app.fitbithealth.R
import com.app.fitbithealth.common.extension.snack
import com.app.fitbithealth.databinding.ActivityLoginBinding
import com.app.fitbithealth.shareddata.base.BaseActivity

class LoginActivity : BaseActivity<ActivityLoginBinding>() {
    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, LoginActivity::class.java))
        }
    }

    override fun getResource(): Int = R.layout.activity_login

    override fun initView() {

    }

    override fun initObserver() {

    }

    override fun handleListener() {

    }

    override fun displayMessage(message: String) {
        mBinding.root.snack(message)
    }
}