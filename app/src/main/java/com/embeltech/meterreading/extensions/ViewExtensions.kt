
package com.embeltech.meterreading.extensions

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.graphics.Typeface.BOLD
import android.graphics.Typeface.NORMAL
import android.view.inputmethod.InputMethodManager
import android.widget.TextView

fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)
}

fun EditText.isEmpty(): Boolean =
        TextUtils.isEmpty(this.text.toString().trim())

fun EditText.toTextString(): String =
        this.text.toString().trim()


fun View.hideSoftKey() {
    val view = this
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun TextView.makeBold() {
    this.setTypeface(this.typeface, BOLD)
}
fun TextView.makeNormal() {
    this.setTypeface(this.typeface, NORMAL)
}


