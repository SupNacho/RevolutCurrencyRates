package ru.supnacho.revolutcurrencyrates

import android.app.Application
import io.reactivex.plugins.RxJavaPlugins
import ru.supnacho.revolutcurrencyrates.di.DaggerRatesDependenciesComponent
import ru.supnacho.revolutcurrencyrates.di.RatesDiHolder
import ru.supnacho.revolutcurrencyrates.utils.safeLog

class App: Application() {

    override fun onCreate() {
        super.onCreate()

        RatesDiHolder.ratesDependencies = DaggerRatesDependenciesComponent.factory().create(this)
        RxJavaPlugins.setErrorHandler { safeLog("RX_ERROR", it.message ?: "") }
    }
}