package ru.supnacho.revolutcurrencyrates.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.math.BigDecimal

@Parcelize
data class RatesModel(
    val baseCurrency: CurrencyCode,
    val rates: Map<CurrencyCode, BigDecimal>
): Parcelable