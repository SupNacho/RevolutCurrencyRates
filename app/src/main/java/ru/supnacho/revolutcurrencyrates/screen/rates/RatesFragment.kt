package ru.supnacho.revolutcurrencyrates.screen.rates


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders

import ru.supnacho.revolutcurrencyrates.R
import ru.supnacho.revolutcurrencyrates.screen.rates.viewmodel.RatesViewModel
import ru.supnacho.revolutcurrencyrates.screen.util.ViewModelFactory

class RatesFragment : Fragment() {

    private val viewModel: RatesViewModel by lazy(mode = LazyThreadSafetyMode.NONE) {
        ViewModelProviders.of(this, ViewModelFactory()).get(RatesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_rates, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.getRatesWithBase("USD")
    }

    companion object {
        fun newInstance(): Fragment = RatesFragment()
    }

}
