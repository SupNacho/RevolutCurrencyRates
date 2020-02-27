package ru.supnacho.revolutcurrencyrates.data.api

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import ru.supnacho.revolutcurrencyrates.data.api.model.RatesResponseDto

interface RevolutRatesApi {

    @GET(RATE_END_POINT)
    @Headers("Accept: application/json")
    fun getRates(@Query("base") baseCurrency: String?): Single<RatesResponseDto>

    companion object {
        const val BASE_URL = "https://hiring.revolut.codes"
        const val RATE_END_POINT = "/api/android/latest"
    }
}

