package ru.supnacho.revolutcurrencyrates.domain

import ru.supnacho.revolutcurrencyrates.data.api.model.RatesResponseDto
import java.math.BigDecimal
import javax.inject.Inject

interface RatesDtoMapper {
    fun mapDto(dto: RatesResponseDto): RatesModel
}

class RatesDtoMapperImpl @Inject constructor(): RatesDtoMapper {
    override fun mapDto(dto: RatesResponseDto): RatesModel =
        RatesModel(
            baseCurrency = CurrencyCode(dto.baseCurrency?:""),
            rates = mapMap(dto.rates)
        )

    private fun mapMap(dto: Map<String, BigDecimal>) : Map<CurrencyCode, BigDecimal> {
        val mappedMap = HashMap<CurrencyCode, BigDecimal>()
        for (e: Map.Entry<String, BigDecimal> in dto ){
            mappedMap[CurrencyCode(e.key)] = e.value
        }
        return mappedMap
    }
}