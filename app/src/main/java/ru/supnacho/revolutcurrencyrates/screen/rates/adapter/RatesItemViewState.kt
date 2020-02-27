package ru.supnacho.revolutcurrencyrates.screen.rates.adapter

import android.os.Parcelable
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import ru.supnacho.revolutcurrencyrates.domain.CurrencyCode
import java.math.BigDecimal

@Parcelize
data class RatesItemViewState (
    val currencyCode: CurrencyCode,
    val nameResId: Int,
    val rateToBase: BigDecimal,
    val baseAmount: BigDecimal,
    val isBaseCurrency: Boolean = false
): Parcelable {
    @IgnoredOnParcel
    val amount: BigDecimal = if (isBaseCurrency) baseAmount else baseAmount.multiply(rateToBase)
    @IgnoredOnParcel
    val imageUrl: String = "https://raw.githubusercontent.com/supnacho/revolutcurrencyrates/master/currency-flags/${currencyCode.code}.png"
}