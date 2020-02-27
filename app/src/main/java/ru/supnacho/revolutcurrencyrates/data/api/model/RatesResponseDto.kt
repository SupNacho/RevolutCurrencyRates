package ru.supnacho.revolutcurrencyrates.data.api.model

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class RatesResponseDto (
    @SerializedName("baseCurrency")
    val baseCurrency: String,
    @SerializedName("rates")
    val rates: Map<String, BigDecimal>
)