package ru.supnacho.revolutcurrencyrates.screen.host

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_rates.*
import ru.supnacho.revolutcurrencyrates.R
import ru.supnacho.revolutcurrencyrates.screen.rates.RatesFragment

class RatesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rates)

        supportFragmentManager.beginTransaction().replace(
            R.id.ratesHostContainer,
            RatesFragment.newInstance(),
            RatesFragment::class.java.name
        ).commit()
    }
}
