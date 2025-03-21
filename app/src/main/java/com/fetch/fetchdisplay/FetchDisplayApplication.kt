package com.fetch.fetchdisplay

import android.app.Application
import com.fetch.fetchdisplay.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class FetchDisplayApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@FetchDisplayApplication)
            modules(appModule)
        }
    }

}