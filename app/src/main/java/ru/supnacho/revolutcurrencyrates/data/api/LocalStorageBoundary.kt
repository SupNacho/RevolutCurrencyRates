package ru.supnacho.revolutcurrencyrates.data.api

import android.content.Context
import com.google.gson.Gson
import ru.supnacho.revolutcurrencyrates.screen.rates.viewmodel.RatesViewState
import javax.inject.Inject

interface LocalStorageBoundary {
    fun saveLastState(viewState: RatesViewState)
    fun restoreLastState(): RatesViewState?
}

class LocalStorageBoundaryImpl @Inject constructor(context: Context, val gson: Gson): LocalStorageBoundary {
    private val sharedPreferences = context.getSharedPreferences("ViewStateSP", Context.MODE_PRIVATE)

    override fun saveLastState(viewState: RatesViewState) {
        sharedPreferences.edit().putString("VS", gson.toJson(viewState, RatesViewState::class.java))
            .apply()
    }

    override fun restoreLastState(): RatesViewState? {
        val json = sharedPreferences.getString("VS", null)
        return json?.let { gson.fromJson(it, RatesViewState::class.java) }
    }
}