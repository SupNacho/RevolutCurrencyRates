package ru.supnacho.revolutcurrencyrates.screen.rates


import android.content.IntentFilter
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
import ru.supnacho.revolutcurrencyrates.utils.NetworkStateReceiver
import ru.supnacho.revolutcurrencyrates.utils.hideSoftKeyboard
import ru.supnacho.revolutcurrencyrates.utils.setVisibility

class RatesFragment : Fragment(), NetworkStateReceiver.OnConnectionChanged {

    private val viewModel: RatesViewModel by lazy {
        ViewModelProviders.of(this, ViewModelFactory()).get(RatesViewModel::class.java)
    }

    private lateinit var ratesAdapter: RatesRVAdapter
    private var snackBar: Snackbar? = null
    private val networkStateReceiver: NetworkStateReceiver by lazy { NetworkStateReceiver(this) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_rates, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initRecycler()
        bindViewModel()
    }

    private fun bindViewModel() {
        viewModel.liveState.observe(viewLifecycleOwner, Observer {
            setLoading(it.rates.isEmpty())
            v_listBlocker.setVisibility(false)
            ratesAdapter.updateRates(it.rates)
        })

        viewModel.event.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Event.Error -> showError(it.errorType)
                is Event.BaseCurrencySelected ->
                    rv_ratesList.smoothScrollToPosition(0)
            }
        })
        viewModel.init()
    }

    override fun onLostConnection() { showError(CurrencyError.HTTP_ERRORS) }

    override fun onRestoreConnection() { restoreConnection() }

    private fun setLoading(isLoading: Boolean) { pb_listLoader.setVisibility(isLoading) }

    private fun showError(error: CurrencyError) {
        when (error) {
            CurrencyError.HTTP_ERRORS -> {
                enableListBlocker()
                showSnackBar(error) { restoreConnection() }
            }
            CurrencyError.UNKNOWN -> showSnackBar(error)
        }

    }

    private fun restoreConnection() {
        snackBar?.dismiss()
        setLoading(true)
        viewModel.tryConnect()
    }

    private fun enableListBlocker() {
        v_listBlocker.setVisibility(true)
        setLoading(false)
        view?.hideSoftKeyboard()
    }

    private fun showSnackBar(error: CurrencyError, action: (() -> Unit)? = null) {
        view?.handler?.postDelayed({
            if (snackBar == null || snackBar?.isShown == false) {
                snackBar = Snackbar
                    .make(rv_ratesList, error.message, Snackbar.LENGTH_INDEFINITE)
                    .setAction(error.actionId) { action?.invoke() }
                snackBar?.show()
            }
        }, 300L)
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
        activity?.registerReceiver(networkStateReceiver, IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION))
        viewModel.onResume()
    }

    override fun onPause() {
        super.onPause()
        activity?.unregisterReceiver(networkStateReceiver)
        viewModel.onPause()
    }

    companion object {
        fun newInstance(): Fragment = RatesFragment()
    }

}
