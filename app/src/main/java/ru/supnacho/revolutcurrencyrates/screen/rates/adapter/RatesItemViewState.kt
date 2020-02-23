package ru.supnacho.revolutcurrencyrates.screen.rates.adapter

import ru.supnacho.revolutcurrencyrates.domain.CurrencyCode
import java.math.BigDecimal

data class RatesItemViewState (
    val currencyCode: CurrencyCode,
    val nameResId: Int,
    val rateToBase: BigDecimal,
    val baseAmount: BigDecimal,
    val isBaseCurrency: Boolean = false
) {
    val amount: BigDecimal = baseAmount.multiply(rateToBase)
    val imageUrl: String = "https://raw.githubusercontent.com/supnacho/revolutcurrencyrates/master/currency-flags/${currencyCode.code}.png"
}