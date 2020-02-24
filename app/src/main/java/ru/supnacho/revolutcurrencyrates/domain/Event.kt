package ru.supnacho.revolutcurrencyrates.domain

sealed class Event {
    data class Error(val message: String): Event()
    object BaseCurrencySelected: Event()
}