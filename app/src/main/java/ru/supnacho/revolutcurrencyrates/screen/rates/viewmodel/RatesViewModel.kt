package ru.supnacho.revolutcurrencyrates.screen.rates.viewmodel

import androidx.lifecycle.*
import com.hadilq.liveevent.LiveEvent
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ru.supnacho.revolutcurrencyrates.R
import ru.supnacho.revolutcurrencyrates.data.api.ApiBoundary
import ru.supnacho.revolutcurrencyrates.data.api.LocalStorageBoundary
import ru.supnacho.revolutcurrencyrates.domain.*
import ru.supnacho.revolutcurrencyrates.screen.rates.adapter.RatesItemViewState
import ru.supnacho.revolutcurrencyrates.utils.safeLog
import ru.supnacho.revolutcurrencyrates.utils.subscribeAndTrack
import java.io.IOException
import java.math.BigDecimal
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class RatesViewModel @Inject constructor(
    private val api: ApiBoundary,
    private val localStorage: LocalStorageBoundary,
    private val uiMapper: RatesUiMapper
) : ViewModel() {

    private var cachedRatesModel: RatesModel? = null //todo add to shared pref
    private val compositeDisposable = CompositeDisposable()
    private val _liveState = MutableLiveData<RatesViewState>()
    private val eventState = LiveEvent<Event>()
    val liveState: LiveData<RatesViewState>
        get() = _liveState
    val event: LiveData<Event> = eventState

    fun init() {
        val initRatesViewState = RatesViewState(
            baseCurrency = CurrencyCode("USD"),
            baseAmount = "1.00",
            rates = emptyList()
        )
        _liveState.value = localStorage.restoreLastState() ?: initRatesViewState
    }

    fun setBaseAmount(baseCurrency: CurrencyCode, amount: String) {
        _liveState.value = _liveState.value?.copy(
            baseCurrency = baseCurrency,
            baseAmount = amount
        )
    }

    fun setBaseCurrency(baseCurrency: CurrencyCode) {
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
                cachedRatesModel = it
                val previousState = _liveState.value
                uiMapper.mapRatesModelToUI(previousState, it)
            }
            .subscribeOn(Schedulers.io())
            .repeatWhen { it.delay(1, TimeUnit.SECONDS) }
            .doOnError { doOfflineConversion() }
            .subscribeAndTrack(
                subscriptionsHolder = compositeDisposable,
                onSuccess = { _liveState.postValue(it) },
                onError = { handleError(it) }
            )
    }

    private fun doOfflineConversion() {
        Observable.interval(1, TimeUnit.SECONDS)
            .map { cachedRatesModel?.let { uiMapper.mapRatesModelToUI(_liveState.value, it) } }
            .subscribeOn(Schedulers.io())
            .subscribeAndTrack(
                subscriptionsHolder = compositeDisposable,
                onSuccess = { _liveState.postValue(it) },
                onError = { handleError(it) }
            )
    }

    fun tryConnect() {
        getRatesWithBase()
    }

    private fun handleError(it: Throwable) {
        val errorMessage = it.message ?: ""
        safeLog("RATES", errorMessage)
        val errorType = if (it is IOException) {
            CurrencyError.HTTP_ERRORS
        } else
            CurrencyError.UNKNOWN
        eventState.postValue(Event.Error(errorType))
    }

    fun onResume() {
        getRatesWithBase()
    }

    fun onPause() {
        compositeDisposable.clear()
        liveState.value?.let { localStorage.saveLastState(it) }
    }

    override fun onCleared() {
        compositeDisposable.clear()
    }
}