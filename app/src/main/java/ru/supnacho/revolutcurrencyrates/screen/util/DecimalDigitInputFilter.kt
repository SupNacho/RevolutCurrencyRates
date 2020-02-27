package ru.supnacho.revolutcurrencyrates.screen.util

import android.text.InputFilter
import android.text.Spanned
import java.util.regex.Pattern

class DecimalDigitInputFilter(digitsAfterZero: Int) : InputFilter {
    private val pattern: Pattern = Pattern.compile("[ 0-9]+((\\.[0-9]{0," + (digitsAfterZero - 1) + "})?)||(\\.)?")

    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        val matcher = pattern.matcher(dest)
        return if (!matcher.matches()) {
            if (dstart > dest.indexOf('.'))
                ""
            else
                null
        } else
            null
    }
}