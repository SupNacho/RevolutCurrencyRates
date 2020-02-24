package ru.supnacho.revolutcurrencyrates.domain

import ru.supnacho.revolutcurrencyrates.R

enum class CurrencyError(val message: Int, val actionId: Int) {
    HTTP_ERRORS(R.string.offline, R.string.retry),
    UNKNOWN(R.string.unknown, R.string.close)
}