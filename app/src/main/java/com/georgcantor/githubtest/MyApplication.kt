package com.georgcantor.githubtest

import android.app.Application
import com.georgcantor.githubtest.di.appModule
import com.georgcantor.githubtest.di.repositoryModule
import com.georgcantor.githubtest.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MyApplication)
            modules(arrayListOf(appModule, viewModelModule, repositoryModule))
        }
    }

}