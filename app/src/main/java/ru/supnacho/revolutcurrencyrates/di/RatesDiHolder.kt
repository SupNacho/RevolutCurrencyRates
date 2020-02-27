package ru.supnacho.revolutcurrencyrates.di

internal object RatesDiHolder {
    lateinit var ratesDependencies: RatesDependencies
}

val ratesDependencies: RatesDependencies
    get() = RatesDiHolder.ratesDependencies