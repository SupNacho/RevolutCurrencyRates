package ru.supnacho.revolutcurrencyrates.di

import android.content.Context
import retrofit2.Retrofit

interface RatesDependencies {
    val retrofit: Retrofit
    val context: Context
}