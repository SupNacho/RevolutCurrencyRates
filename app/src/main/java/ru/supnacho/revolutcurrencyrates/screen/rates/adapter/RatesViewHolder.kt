package ru.supnacho.revolutcurrencyrates.screen.rates.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.currency_item.view.*
import ru.supnacho.revolutcurrencyrates.domain.CurrencyNames
import ru.supnacho.revolutcurrencyrates.utils.load

class RatesViewHolder(view: View): RecyclerView.ViewHolder(view), LayoutContainer {

    override val containerView: View?
        get() = itemView

    fun bind(itemViewState: RatesItemViewState){
        itemView.run {
            iv_currencyIcon.load(itemViewState.imageUrl)
            tv_currencyCode.text = itemViewState.currencyCode.code
            tv_currencyName.setText(itemViewState.nameResId)
            et_currencyAmount.setText(itemViewState.amount.toPlainString())
        }
    }
}