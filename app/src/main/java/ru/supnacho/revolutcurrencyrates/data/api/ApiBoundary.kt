package ru.supnacho.revolutcurrencyrates.data.api

import io.reactivex.Single
import ru.supnacho.revolutcurrencyrates.domain.RatesDtoMapper
import ru.supnacho.revolutcurrencyrates.domain.RatesModel
import javax.inject.Inject

interface ApiBoundary {
    fun getRatesWithBase(base: String?): Single<RatesModel>
}

class ApiBoundaryImpl @Inject constructor(
    private val api: RevolutRatesApi,
    private val mapper: RatesDtoMapper
) : ApiBoundary {
    override fun getRatesWithBase(base: String?): Single<RatesModel> =
        api.getRates(baseCurrency = base).map { mapper.mapDto(it) }
}