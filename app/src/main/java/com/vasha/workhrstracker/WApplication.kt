package com.vasha.workhrstracker

import android.app.Application
import com.vasha.workhrstracker.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import java.util.logging.Level

/**
 * Created by ivasil on 1/26/2022
 */

class WApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin{
            androidLogger(if (BuildConfig.DEBUG) org.koin.core.logger.Level.ERROR else  org.koin.core.logger.Level.NONE)
            androidContext(this@WApplication)
            modules(networkModule)
        }
    }
}