package ru.supnacho.revolutcurrencyrates.screen.rates


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_rates.*

import ru.supnacho.revolutcurrencyrates.R
import ru.supnacho.revolutcurrencyrates.screen.rates.adapter.RatesRVAdapter
import ru.supnacho.revolutcurrencyrates.screen.rates.viewmodel.RatesViewModel
import ru.supnacho.revolutcurrencyrates.screen.util.ViewModelFactory

class RatesFragment : Fragment() {

    private val viewModel: RatesViewModel by lazy(mode = LazyThreadSafetyMode.NONE) {
        ViewModelProviders.of(this, ViewModelFactory()).get(RatesViewModel::class.java)
    }

    private lateinit var ratesAdapter: RatesRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_rates, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.getRatesWithBase("USD")
        ratesAdapter = RatesRVAdapter()
        val layoutManager = LinearLayoutManager(context).apply { orientation = RecyclerView.VERTICAL }
        rv_ratesList.run {
            setHasFixedSize(true)
            setLayoutManager(layoutManager)
            adapter = ratesAdapter
        }

        viewModel.liveState.observe(this, Observer {
            ratesAdapter.updateRates(it.rates)
        })
    }

    companion object {
        fun newInstance(): Fragment = RatesFragment()
    }

}
