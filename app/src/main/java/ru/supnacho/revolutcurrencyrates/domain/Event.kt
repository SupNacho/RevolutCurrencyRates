package ru.supnacho.revolutcurrencyrates.domain

sealed class Event {
    data class Error(val errorType: CurrencyError): Event()
    object BaseCurrencySelected: Event()
}