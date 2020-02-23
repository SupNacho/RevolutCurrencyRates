package ru.supnacho.revolutcurrencyrates.screen.rates.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ru.supnacho.revolutcurrencyrates.R
import ru.supnacho.revolutcurrencyrates.data.api.ApiBoundary
import ru.supnacho.revolutcurrencyrates.domain.CurrencyCode
import ru.supnacho.revolutcurrencyrates.domain.CurrencyNames
import ru.supnacho.revolutcurrencyrates.screen.rates.adapter.RatesItemViewState
import ru.supnacho.revolutcurrencyrates.utils.safeLog
import ru.supnacho.revolutcurrencyrates.utils.subscribeAndTrack
import java.math.BigDecimal
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class RatesViewModel @Inject constructor(
    private val api: ApiBoundary
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private val _liveState = MutableLiveData<RatesViewState>()
    val liveState: LiveData<RatesViewState>
        get() = _liveState


    fun getRatesWithBase(base: String?) {
        compositeDisposable.clear()
        api.getRatesWithBase(base)
            .toObservable()
            .map {
                it.rates.map { e ->
                    RatesItemViewState(
                        currencyCode = e.key,
                        nameResId = CurrencyNames.names[e.key] ?: R.string.unknown,
                        rateToBase = e.value,
                        baseAmount = BigDecimal.ONE,
                        isBaseCurrency = e.key.code == base
                    )
                }
            }
            .subscribeOn(Schedulers.io())
            .repeatWhen { it.delay(1, TimeUnit.SECONDS) }
            .subscribeAndTrack(
                subscriptionsHolder = compositeDisposable,
                onSuccess = {
                    safeLog("RATES", "${it.size}")
                    _liveState.value?.let { rvs ->
                        _liveState.postValue(
                            rvs.copy(
                                rates = it,
                                baseCurrency = CurrencyCode("EUR"),
                                baseAmount = BigDecimal.ONE.toPlainString()
                            )
                        )
                    } ?: _liveState.postValue(
                        RatesViewState(
                            rates = it,
                            baseAmount = BigDecimal.ONE.toPlainString(),
                            baseCurrency = CurrencyCode("USD")
                        )
                    )
                },
                onError = { safeLog("RATES", it.message ?: "") }
            )

    }

    override fun onCleared() {
        compositeDisposable.clear()
    }
}