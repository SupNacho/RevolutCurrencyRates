package ru.supnacho.revolutcurrencyrates.screen.rates.viewmodel

import androidx.lifecycle.*
import com.hadilq.liveevent.LiveEvent
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ru.supnacho.revolutcurrencyrates.R
import ru.supnacho.revolutcurrencyrates.data.api.ApiBoundary
import ru.supnacho.revolutcurrencyrates.domain.CurrencyCode
import ru.supnacho.revolutcurrencyrates.domain.CurrencyNames
import ru.supnacho.revolutcurrencyrates.domain.Event
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
    private val eventState = LiveEvent<Event>()
    val liveState: LiveData<RatesViewState>
        get() = _liveState
    val event : LiveData<Event> = eventState

    fun init(){
        val initRatesViewState = RatesViewState(
            baseCurrency = CurrencyCode("USD"),
            baseAmount = "1.00",
            rates = emptyList()
        )
        _liveState.value = initRatesViewState
    }

    fun setBaseAmount(baseCurrency: CurrencyCode, amount: String){
        _liveState.value = _liveState.value?.copy(
            baseCurrency = baseCurrency,
            baseAmount = amount
        )
    }

    fun setBaseCurrency(baseCurrency: CurrencyCode){
        compositeDisposable.clear()
        _liveState.value = _liveState.value?.copy(
            baseCurrency = baseCurrency
        )
        getRatesWithBase()
        eventState.value = Event.BaseCurrencySelected
    }

    private fun getRatesWithBase() {
        compositeDisposable.clear()
        api.getRatesWithBase(_liveState.value?.baseCurrency?.code)
            .toObservable()
            .map {
                val previousState = _liveState.value
                val preparedViewState = previousState?.copy(
                    baseCurrency = it.baseCurrency,
                    rates = it.rates.map { e ->
                        RatesItemViewState(
                            currencyCode = e.key,
                            nameResId = CurrencyNames.names[e.key] ?: R.string.unknown,
                            rateToBase = e.value,
                            baseAmount = previousState.baseAmount.toBigDecimal(),
                            isBaseCurrency = e.key.code == it.baseCurrency.code
                        )
                    }
                )
                (preparedViewState?.rates as? MutableList)?.add(0, RatesItemViewState(
                    currencyCode = it.baseCurrency,
                    nameResId = CurrencyNames.names[it.baseCurrency] ?: R.string.unknown,
                    rateToBase = BigDecimal.ONE,
                    isBaseCurrency = true,
                    baseAmount = previousState.baseAmount.toBigDecimal()
                ))
                preparedViewState
            }
            .subscribeOn(Schedulers.io())
            .repeatWhen { it.delay(1, TimeUnit.SECONDS) }
            .subscribeAndTrack(
                subscriptionsHolder = compositeDisposable,
                onSuccess = {
                    safeLog("RATES", "${it?.rates?.size}")
                    _liveState.postValue(it)
                },
                onError = {
                    val errorMessage = it.message ?: ""
                    safeLog("RATES", errorMessage)
                    eventState.postValue(Event.Error(errorMessage))
                }
            )

    }

    fun onResume() {
        getRatesWithBase()
    }

    fun onPause() {
        compositeDisposable.clear()
    }

    override fun onCleared() {
        compositeDisposable.clear()
    }
}