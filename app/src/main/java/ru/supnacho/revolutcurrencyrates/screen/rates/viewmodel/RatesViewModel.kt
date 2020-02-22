package ru.supnacho.revolutcurrencyrates.screen.rates.viewmodel

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ru.supnacho.revolutcurrencyrates.data.api.ApiBoundary
import ru.supnacho.revolutcurrencyrates.utils.safeLog
import ru.supnacho.revolutcurrencyrates.utils.subscribeAndTrack
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class RatesViewModel @Inject constructor(
    private val api: ApiBoundary
): ViewModel() {

    private val compositeDisposable = CompositeDisposable()


    fun getRatesWithBase(base: String?){
        compositeDisposable.clear()
        api.getRatesWithBase(base)
            .toObservable()
            .subscribeOn(Schedulers.io())
            .repeatWhen { it.delay(1, TimeUnit.SECONDS) }
            .subscribeAndTrack(
                subscriptionsHolder = compositeDisposable,
                onSuccess = { safeLog("RATES", "${it.baseCurrency}")},
                onError = { safeLog("RATES", it.message?:"") }
            )

    }

    override fun onCleared() {
        compositeDisposable.clear()
    }
}