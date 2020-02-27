package ru.supnacho.revolutcurrencyrates.data.storage

import android.content.Context
import com.google.gson.Gson
import ru.supnacho.revolutcurrencyrates.screen.rates.viewmodel.RatesViewState
import javax.inject.Inject

interface LocalStorageBoundary {
    fun saveLastState(viewState: RatesViewState)
    fun restoreLastState(): RatesViewState?
}

class LocalStorageBoundaryImpl @Inject constructor(context: Context, private val gson: Gson):
    LocalStorageBoundary {
    private val sharedPreferences = context.getSharedPreferences(SP_FILE_NAME, Context.MODE_PRIVATE)

    override fun saveLastState(viewState: RatesViewState) {
        sharedPreferences.edit().putString(SP_VS, gson.toJson(viewState, RatesViewState::class.java))
            .apply()
    }

    override fun restoreLastState(): RatesViewState? {
        val json = sharedPreferences.getString(SP_VS, null)
        return json?.let { gson.fromJson(it, RatesViewState::class.java) }
    }

    private companion object {
        const val SP_FILE_NAME = "ViewStateSP"
        const val SP_VS = "VS"
    }
}