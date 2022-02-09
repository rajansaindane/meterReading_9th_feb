
package com.embeltech.meterreading.extensions

import android.text.TextUtils
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.util.regex.Pattern


fun Disposable.addToCompositeDisposable(composite: CompositeDisposable) {
    composite.add(this)
}

fun String.isEmpty(): Boolean =
    TextUtils.isEmpty(this.trim())

fun String.isAlphaNumeric(): Boolean {
    return Pattern.compile("(?!^[0-9]*\$)(?!^[a-zA-Z]*\$)^([a-zA-Z0-9]{6,15})\$").matcher(this).matches()
}