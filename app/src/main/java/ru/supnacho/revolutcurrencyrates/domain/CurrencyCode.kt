package ru.supnacho.revolutcurrencyrates.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CurrencyCode(val code: String): Parcelable