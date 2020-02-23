package ru.supnacho.revolutcurrencyrates.utils

import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import ru.supnacho.revolutcurrencyrates.BuildConfig

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