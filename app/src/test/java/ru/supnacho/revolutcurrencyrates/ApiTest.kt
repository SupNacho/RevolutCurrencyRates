package ru.supnacho.revolutcurrencyrates

import com.google.gson.Gson
import okhttp3.OkHttpClient
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.supnacho.revolutcurrencyrates.data.api.RevolutRatesApi
import ru.supnacho.revolutcurrencyrates.data.api.model.RatesResponseDto

class ApiTest {

    private lateinit var retrofit: Retrofit
    private lateinit var api: RevolutRatesApi

    @Before
    fun init(){
        retrofit = Retrofit.Builder()
            .baseUrl(RevolutRatesApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(OkHttpClient())
            .build()

        api = retrofit.create(RevolutRatesApi::class.java)
    }

    @Test
    fun `should successfully get dto model from net`(){
        api.getRates("EUR").test()
            .assertNoErrors()
            .assertValue { t: RatesResponseDto ->
                t.baseCurrency == "EUR" && t.rates.isNotEmpty()
            }
    }
}