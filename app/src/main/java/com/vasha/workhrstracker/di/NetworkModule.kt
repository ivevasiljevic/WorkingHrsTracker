package com.vasha.workhrstracker.di

import com.vasha.workhrstracker.BuildConfig
import com.vasha.workhrstracker.api.WorkingHrsTrackerApi
import com.vasha.workhrstracker.data.PreferencesManager
import com.vasha.workhrstracker.data.ScannerRepository
import com.vasha.workhrstracker.data.UserRepository
import com.vasha.workhrstracker.features.scanner.ScannerViewModel
import com.vasha.workhrstracker.features.user.UserViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by ivasil on 1/26/2022
 */

val networkModule = module {

    single { provideHttpLoggingInterceptor() }
    single { provideOkHttpClient(get()) }
    single { provideApi(get()) }
    single { provideRetrofit(get()) }
    single { ScannerRepository(get()) }
    single { UserRepository(get()) }
    viewModel { ScannerViewModel(scannerRepository = get()) }
    viewModel { UserViewModel(userRepository = get(), PreferencesManager(androidContext())) }

}

fun provideHttpLoggingInterceptor() : HttpLoggingInterceptor =
    HttpLoggingInterceptor()

fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder().baseUrl(BuildConfig.API_URL).client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create()).build()
}

fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
    return OkHttpClient.Builder().addInterceptor(loggingInterceptor.apply { this.level = HttpLoggingInterceptor.Level.BODY })
        .build()
}

fun provideApi(retrofit: Retrofit): WorkingHrsTrackerApi = retrofit.create(WorkingHrsTrackerApi::class.java)