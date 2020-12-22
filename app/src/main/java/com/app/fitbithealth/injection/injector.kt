package com.app.fitbithealth.injection

import android.content.Context
import com.app.fitbithealth.BuildConfig
import com.app.fitbithealth.common.extension.getPrefInstance
import com.app.fitbithealth.model.UserHolder
import com.app.fitbithealth.shareddata.endpoint.ApiEndPoint
import com.app.fitbithealth.shareddata.repo.UserRepo
import com.app.fitbithealth.shareddata.repo.UserRepository
import com.app.fitbithealth.ui.auth.login.LoginViewModel
import com.app.fitbithealth.utils.Config
import com.readystatesoftware.chuck.ChuckInterceptor
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val viewModelModule = module {
    single<UserRepo> { UserRepository(get(), get()) }
    viewModel { LoginViewModel(get()) }
}

val sharedPreferenceModule = module {
    single { provideUserHolder(androidContext()) }
}

val networkModule = module {
    single { provideHttpLogging(androidContext()) }
    single { provideRetrofit(get()) }
    single { provideApiService(get()) }
}

val appModules = viewModelModule + networkModule + sharedPreferenceModule

fun provideApiService(retrofit: Retrofit): ApiEndPoint = retrofit.create(ApiEndPoint::class.java)

fun provideRetrofit(client: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl(BuildConfig.API_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
        .client(client)
        .build()
}

fun provideHttpLogging(context: Context): OkHttpClient {
    val logging = HttpLoggingInterceptor()
    logging.level =
        if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE

    return OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .addNetworkInterceptor(ChuckInterceptor(context))
        .addNetworkInterceptor(logging)
        .build()
}

fun provideUserHolder(context: Context): UserHolder =
    UserHolder(context.getPrefInstance(Config.FIT_BIT_SHARED_PREFERENCE))
