package ru.supnacho.revolutcurrencyrates.di

import android.content.Context
import com.google.gson.Gson
import dagger.*
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.supnacho.revolutcurrencyrates.BuildConfig
import ru.supnacho.revolutcurrencyrates.data.api.RevolutRatesApi
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Component(modules = [RatesDependenciesModule::class])
@Singleton
interface RatesDependenciesComponent : RatesDependencies {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): RatesDependenciesComponent
    }
}

@Module
internal abstract class RatesDependenciesModule {

    @Module
    companion object {
        private const val HTTP_TIMEOUT = 40L

        @JvmStatic
        @Provides
        @Singleton
        fun provideRetrofit(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(RevolutRatesApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(getGson()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(getOkHttpClient())
                .build()
        }

        private fun getGson(): Gson = Gson()

        private fun getOkHttpClient(): OkHttpClient {
            val logInterceptor = HttpLoggingInterceptor().apply {
                level =
                    if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
            }

            return OkHttpClient.Builder()
                .connectTimeout(HTTP_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(HTTP_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(HTTP_TIMEOUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .connectionPool(ConnectionPool(0, 1, TimeUnit.NANOSECONDS))
                .addInterceptor(logInterceptor)
                .build()
        }
    }
}