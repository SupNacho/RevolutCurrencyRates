package ru.supnacho.revolutcurrencyrates.screen.rates.viewmodel

import ru.supnacho.revolutcurrencyrates.domain.CurrencyCode
import ru.supnacho.revolutcurrencyrates.screen.rates.adapter.RatesItemViewState

data class RatesViewState(
    val rates: List<RatesItemViewState>,
    val baseAmount: String,
    val baseCurrency: CurrencyCode
)