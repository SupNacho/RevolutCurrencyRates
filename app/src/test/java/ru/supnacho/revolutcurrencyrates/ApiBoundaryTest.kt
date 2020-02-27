package ru.supnacho.revolutcurrencyrates

import com.google.gson.Gson
import okhttp3.OkHttpClient
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.supnacho.revolutcurrencyrates.data.api.ApiBoundaryImpl
import ru.supnacho.revolutcurrencyrates.data.api.RevolutRatesApi
import ru.supnacho.revolutcurrencyrates.domain.CurrencyCode
import ru.supnacho.revolutcurrencyrates.domain.RatesDtoMapperImpl
import ru.supnacho.revolutcurrencyrates.domain.RatesModel

class ApiBoundaryTest {

    private lateinit var retrofit: Retrofit
    private lateinit var api: RevolutRatesApi
    private lateinit var apiBoundary: ApiBoundaryImpl

    @Before
    fun init(){
        retrofit = Retrofit.Builder()
            .baseUrl(RevolutRatesApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(OkHttpClient())
            .build()

        api = retrofit.create(RevolutRatesApi::class.java)
        val mapperImpl = RatesDtoMapperImpl()

        apiBoundary = ApiBoundaryImpl(api, mapperImpl)
    }

    @Test
    fun `should get domain model from net`(){
        apiBoundary.getRatesWithBase("EUR")
            .test()
            .assertNoErrors()
            .assertValue { it.baseCurrency == CurrencyCode("EUR") }
    }
}