package com.example.developersapp

import android.app.Application
import com.example.developersapp.presentation.di.allModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ShoppingApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@ShoppingApplication)
            modules(allModules)
        }
    }
}