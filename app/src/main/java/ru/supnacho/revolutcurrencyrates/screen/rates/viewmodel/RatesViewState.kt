package ru.supnacho.revolutcurrencyrates.screen.rates.viewmodel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import ru.supnacho.revolutcurrencyrates.domain.CurrencyCode
import ru.supnacho.revolutcurrencyrates.screen.rates.adapter.RatesItemViewState

@Parcelize
data class RatesViewState(
    val rates: List<RatesItemViewState>,
    val baseAmount: String,
    val baseCurrency: CurrencyCode
):Parcelable