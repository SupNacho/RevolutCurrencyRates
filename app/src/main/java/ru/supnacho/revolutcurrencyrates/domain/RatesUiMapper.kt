package ru.supnacho.revolutcurrencyrates.domain

import ru.supnacho.revolutcurrencyrates.R
import ru.supnacho.revolutcurrencyrates.screen.rates.adapter.RatesItemViewState
import ru.supnacho.revolutcurrencyrates.screen.rates.viewmodel.RatesViewState
import java.math.BigDecimal
import javax.inject.Inject

interface RatesUiMapper {
    fun mapRatesModelToUI(previousState: RatesViewState?, ratesModel: RatesModel): RatesViewState?
}

class RatesUiMapperImpl @Inject constructor(): RatesUiMapper{
    override fun mapRatesModelToUI(
        previousState: RatesViewState?,
        ratesModel: RatesModel
    ): RatesViewState? {
        val preparedViewState = previousState?.copy(
            baseCurrency = ratesModel.baseCurrency,
            rates = ratesModel.rates.map { e ->
                RatesItemViewState(
                    currencyCode = e.key,
                    nameResId = CurrencyNames.names[e.key] ?: R.string.unknown,
                    rateToBase = e.value,
                    baseAmount = previousState.baseAmount.toBigDecimal(),
                    isBaseCurrency = e.key.code == ratesModel.baseCurrency.code
                )
            }
        )
        (preparedViewState?.rates as? MutableList)?.add(
            0, RatesItemViewState(
                currencyCode = ratesModel.baseCurrency,
                nameResId = CurrencyNames.names[ratesModel.baseCurrency] ?: R.string.unknown,
                rateToBase = BigDecimal.ONE,
                isBaseCurrency = true,
                baseAmount = previousState.baseAmount.toBigDecimal()
            )
        )
        return preparedViewState
    }
}