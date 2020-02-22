package ru.supnacho.revolutcurrencyrates.screen.di

import dagger.Binds
import dagger.Component
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import ru.supnacho.revolutcurrencyrates.data.api.ApiBoundary
import ru.supnacho.revolutcurrencyrates.data.api.ApiBoundaryImpl
import ru.supnacho.revolutcurrencyrates.data.api.RevolutRatesApi
import ru.supnacho.revolutcurrencyrates.di.RatesDependencies
import ru.supnacho.revolutcurrencyrates.domain.RatesDtoMapper
import ru.supnacho.revolutcurrencyrates.domain.RatesDtoMapperImpl
import ru.supnacho.revolutcurrencyrates.screen.rates.viewmodel.RatesViewModel
import javax.inject.Singleton

@Component(dependencies = [RatesDependencies::class], modules = [RatesModule::class])
@Singleton
interface RatesComponent {
    val ratesViewModel: RatesViewModel

    @Component.Factory
    interface Factory {
        fun create(ratesDependencies: RatesDependencies): RatesComponent
    }
}

@Module
abstract class RatesModule {

    @Binds
    abstract fun bindApiBoundary(boundary: ApiBoundaryImpl): ApiBoundary

    @Binds
    abstract fun bindRatesMapper(mapper: RatesDtoMapperImpl): RatesDtoMapper

    @Module
    companion object {
        @Provides
        @JvmStatic
        fun provideRatesApi(retrofit: Retrofit): RevolutRatesApi {
            return retrofit.create(RevolutRatesApi::class.java)
        }
    }
}