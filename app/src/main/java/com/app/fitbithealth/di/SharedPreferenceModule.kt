package com.app.fitbithealth.di

import android.content.Context
import com.app.fitbithealth.common.extension.getPrefInstance
import com.app.fitbithealth.model.UserHolder
import com.app.fitbithealth.utils.Config
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class SharedPreferenceModule {

    @Provides
    @Singleton
    fun provideUserHolder(@ApplicationContext context: Context): UserHolder =
        UserHolder(context.getPrefInstance(Config.FIT_BIT_SHARED_PREFERENCE))
}