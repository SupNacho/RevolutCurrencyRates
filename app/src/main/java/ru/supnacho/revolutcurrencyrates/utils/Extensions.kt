package ru.supnacho.revolutcurrencyrates.utils

import android.content.Context
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.core.view.ViewCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import ru.supnacho.revolutcurrencyrates.BuildConfig
import java.math.BigDecimal
import java.math.RoundingMode

fun safeLog(tag: String, message: String) {
    if (BuildConfig.DEBUG) Log.d(tag, message)
}

fun <T> Single<T>.subscribeAndTrack(
    subscriptionsHolder: CompositeDisposable,
    onError: (Throwable) -> Unit = {},
    onSuccess: (T) -> Unit
) {
    subscriptionsHolder.add(this.subscribe(onSuccess, onError))
}
fun <T> Observable<T>.subscribeAndTrack(
    subscriptionsHolder: CompositeDisposable,
    onError: (Throwable) -> Unit = {},
    onSuccess: (T) -> Unit
) {
    subscriptionsHolder.add(this.subscribe(onSuccess, onError))
}

fun ImageView.load(url: String) {
    Glide.with(this)
        .load(url)
        .into(this)
}

fun View.hideSoftKeyboard() {
    if (ViewCompat.isAttachedToWindow(this)) {
        val imm = this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(this.windowToken, 0)
    }
}

fun BigDecimal.toView(): String = this.setScale(2, RoundingMode.HALF_EVEN).toPlainString()