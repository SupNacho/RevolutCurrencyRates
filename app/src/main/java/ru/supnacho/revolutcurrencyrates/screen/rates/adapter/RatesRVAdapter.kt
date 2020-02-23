package ru.supnacho.revolutcurrencyrates.screen.rates.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.supnacho.revolutcurrencyrates.R

class RatesRVAdapter(): RecyclerView.Adapter<RatesViewHolder>() {
    private var rates: List<RatesItemViewState> = emptyList()

    fun updateRates(newrates: List<RatesItemViewState>){
        val result = DiffUtil.calculateDiff(DiffCallback(rates, newrates))
        rates = newrates
        result.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.currency_item, parent, false)
        return RatesViewHolder(view)
    }

    override fun getItemCount(): Int = rates.size

    override fun onBindViewHolder(holder: RatesViewHolder, position: Int) {
        holder.bind(rates[position])
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