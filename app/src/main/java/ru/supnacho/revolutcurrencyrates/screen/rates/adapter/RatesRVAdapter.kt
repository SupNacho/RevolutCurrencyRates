package ru.supnacho.revolutcurrencyrates.screen.rates.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.supnacho.revolutcurrencyrates.R
import ru.supnacho.revolutcurrencyrates.domain.CurrencyCode
import ru.supnacho.revolutcurrencyrates.screen.rates.viewmodel.RatesViewModel
import ru.supnacho.revolutcurrencyrates.utils.safeLog

class RatesRVAdapter(private val viewModel: RatesViewModel): RecyclerView.Adapter<RatesViewHolder>(), RatesViewHolder.HolderActions {
    private var rates: List<RatesItemViewState> = emptyList()

    fun updateRates(newRates: List<RatesItemViewState>){
        val diffResult = DiffUtil.calculateDiff(DiffCallback(rates, newRates))
        rates = newRates
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.currency_item, parent, false)
        return RatesViewHolder(view, this)
    }

    override fun getItemCount(): Int = rates.size

    override fun onViewDetachedFromWindow(holder: RatesViewHolder) {
        holder.onDetach()
    }



    override fun onBindViewHolder(holder: RatesViewHolder, position: Int) {
        holder.bind(rates[position])
    }

    override fun inputAmount(baseCurrency: CurrencyCode, amount: String) {
        viewModel.setBaseAmount(baseCurrency, amount)
    }

    override fun selectBaseCurrency(baseCurrency: CurrencyCode) {
        viewModel.setBaseCurrency(baseCurrency)
    }

    override fun onFocusLost() {
        safeLog("LOSTFOCUS", "OnLostFocus")
    }

    private class DiffCallback(
        private val oldItems: List<RatesItemViewState>,
        private val newItems: List<RatesItemViewState>
    ): DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldItems[oldItemPosition].currencyCode.code == newItems[newItemPosition].currencyCode.code

        override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? = Unit

        override fun getOldListSize(): Int = oldItems.size

        override fun getNewListSize(): Int = newItems.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldItems[oldItemPosition] == newItems[newItemPosition]
    }
}