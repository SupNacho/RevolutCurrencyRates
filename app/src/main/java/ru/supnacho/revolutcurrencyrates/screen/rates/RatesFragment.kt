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
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_rates.*

import ru.supnacho.revolutcurrencyrates.R
import ru.supnacho.revolutcurrencyrates.domain.CurrencyError
import ru.supnacho.revolutcurrencyrates.domain.Event
import ru.supnacho.revolutcurrencyrates.screen.rates.adapter.RatesRVAdapter
import ru.supnacho.revolutcurrencyrates.screen.rates.viewmodel.RatesViewModel
import ru.supnacho.revolutcurrencyrates.screen.util.ViewModelFactory

class RatesFragment : Fragment() {

    private val viewModel: RatesViewModel by lazy {
        ViewModelProviders.of(this, ViewModelFactory()).get(RatesViewModel::class.java)
    }

    private lateinit var ratesAdapter: RatesRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_rates, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initRecycler()
        bindViewModel()
    }

    private fun bindViewModel() {
        viewModel.liveState.observe(this, Observer {
            ratesAdapter.updateRates(it.rates)
        })

        viewModel.event.observe(this, Observer {
            when (it) {
                is Event.Error -> showError(it.errorType)
                is Event.BaseCurrencySelected ->
                    rv_ratesList.smoothScrollToPosition(0)
            }
        })
        viewModel.init()
    }

    private fun showError(error: CurrencyError) {
        when (error) {
            CurrencyError.HTTP_ERRORS -> showSnackBar(error) { viewModel.getRatesWithBase() }
            CurrencyError.UNKNOWN -> showSnackBar(error)
        }

    }

    private fun showSnackBar(error: CurrencyError, action: (() -> Unit)? = null) {
        Snackbar
            .make(rv_ratesList, error.message, Snackbar.LENGTH_INDEFINITE)
            .setAction(error.actionId) { action?.invoke() }.show()
    }

    private fun initRecycler() {
        ratesAdapter = RatesRVAdapter(viewModel)
        val layoutManager =
            LinearLayoutManager(context)
                .apply {
                    orientation = RecyclerView.VERTICAL
                    isSmoothScrollbarEnabled = true
                }

        rv_ratesList.run {
            setHasFixedSize(true)
            setLayoutManager(layoutManager)
            adapter = ratesAdapter
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume()
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }

    companion object {
        fun newInstance(): Fragment = RatesFragment()
    }

}
