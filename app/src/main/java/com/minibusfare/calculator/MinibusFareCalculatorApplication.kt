package com.minibusfare.calculator

import android.app.Application
import com.minibusfare.calculator.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MinibusFareCalculatorApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MinibusFareCalculatorApplication)
            modules(appModule)
        }
    }
}
