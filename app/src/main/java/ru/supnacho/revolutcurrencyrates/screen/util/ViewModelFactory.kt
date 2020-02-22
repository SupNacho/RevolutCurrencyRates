package ru.supnacho.revolutcurrencyrates.screen.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.supnacho.revolutcurrencyrates.di.ratesDependencies
import ru.supnacho.revolutcurrencyrates.screen.di.DaggerRatesComponent

internal class ViewModelFactory: ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DaggerRatesComponent.factory().create(ratesDependencies).ratesViewModel as T
    }
}