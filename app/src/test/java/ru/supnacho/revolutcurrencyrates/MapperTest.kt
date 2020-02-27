package ru.supnacho.revolutcurrencyrates

import com.google.gson.Gson
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import ru.supnacho.revolutcurrencyrates.data.api.model.RatesResponseDto
import ru.supnacho.revolutcurrencyrates.domain.CurrencyCode
import ru.supnacho.revolutcurrencyrates.domain.RatesDtoMapperImpl
import ru.supnacho.revolutcurrencyrates.domain.RatesModel
import ru.supnacho.revolutcurrencyrates.domain.RatesUiMapperImpl
import ru.supnacho.revolutcurrencyrates.screen.rates.viewmodel.RatesViewState

class MapperTest {

    private val gson = Gson()
    private val mapperDto = RatesDtoMapperImpl()
    private val mapperUi = RatesUiMapperImpl()
    private lateinit var dto: RatesResponseDto
    private lateinit var domain: RatesModel

    private val jsonString = """{
        "baseCurrency":"EUR",
           "rates":{
          "AUD":1.599,
              "BGN":1.988,
          "BRL":4.236,
              "CAD":1.504,
          "CHF":1.145,
              "CNY":7.737,
          "CZK":25.682,
              "DKK":7.464,
          "GBP":0.88,
              "HKD":9.001,
          "HRK":7.496,
              "HUF":322.238,
          "IDR":15989.325,
              "ILS":4.126,
          "INR":81.08,
              "ISK":134.913,
          "JPY":126.821,
              "KRW":1293.572,
          "MXN":21.948,
              "MYR":4.663,
          "NOK":9.79,
              "NZD":1.654,
          "PHP":59.497,
              "PLN":4.382,
          "RON":4.76,
              "RUB":75.296,
          "SEK":10.654,
              "SGD":1.533,
          "THB":35.84,
              "USD":1.142,
          "ZAR":16.085
               }
}""".trimIndent()

    @Before
    fun initModels(){
        dto = gson.fromJson(jsonString, RatesResponseDto::class.java)
        domain = mapperDto.mapDto(dto)
    }

    @Test
    fun `should convert dto to domain`() {
        assertEquals(domain.baseCurrency.code, dto.baseCurrency)
        domain.rates.keys.forEach { key ->
            assertTrue(dto.rates.containsKey(key.code))
            assertTrue(domain.rates[key] == dto.rates[key.code])
        }
    }

    @Test
    fun `should convert domain to UI`(){
        val previousState = RatesViewState(
            baseCurrency = CurrencyCode("USD"),
            baseAmount = "1.00",
            rates = emptyList()
        )
        val newState = mapperUi.mapRatesModelToUI(previousState, domain)

        assertTrue(
            newState?.baseCurrency == CurrencyCode("EUR") &&
                    newState.rates.size > previousState.rates.size &&
                    newState.baseAmount == previousState.baseAmount
        )
    }
}
