package ru.supnacho.revolutcurrencyrates.screen.rates.adapter

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.currency_item.view.*
import ru.supnacho.revolutcurrencyrates.domain.CurrencyCode
import ru.supnacho.revolutcurrencyrates.screen.util.DecimalDigitInputFilter
import ru.supnacho.revolutcurrencyrates.utils.hideSoftKeyboard
import ru.supnacho.revolutcurrencyrates.utils.load
import ru.supnacho.revolutcurrencyrates.utils.toView

class RatesViewHolder(view: View, val actionListener: HolderActions) :
    RecyclerView.ViewHolder(view), LayoutContainer {

    override val containerView: View?
        get() = itemView
    private var currencyCode: CurrencyCode? = null
    private var isBaseCurrency = false

    init {
        itemView.et_currencyAmount.run {
            filters = arrayOf(DecimalDigitInputFilter(2))
            addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {}

                override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    s?.let {
                        if (it.isNotEmpty())
                            if (isBaseCurrency)
                                currencyCode?.let { cc ->
                                    actionListener.inputAmount(
                                        baseCurrency = cc,
                                        amount = it.toString()
                                    )
                                }

                    }
                }
            })
        }


    }

    fun bind(itemViewState: RatesItemViewState) {
        itemView.run {
            currencyCode = itemViewState.currencyCode
            isBaseCurrency = itemViewState.isBaseCurrency
            setOnClickListener {
                actionListener.selectBaseCurrency(itemViewState.currencyCode)
                et_currencyAmount.requestFocus()
            }
            iv_currencyIcon.load(itemViewState.imageUrl)
            tv_currencyCode.text = itemViewState.currencyCode.code
            tv_currencyName.setText(itemViewState.nameResId)
            if (isBaseCurrency)
                et_currencyAmount.setText(itemViewState.baseAmount.toPlainString())
            else
                et_currencyAmount.setText(itemViewState.amount.toView())

        }
    }

    fun onDetach() {
        itemView.et_currencyAmount?.run {
            if (isFocused) {
                hideSoftKeyboard()
                actionListener.onFocusLost()
            }
        }
    }

    interface HolderActions {
        fun inputAmount(baseCurrency: CurrencyCode, amount: String)
        fun selectBaseCurrency(baseCurrency: CurrencyCode)
        fun onFocusLost()
    }
}