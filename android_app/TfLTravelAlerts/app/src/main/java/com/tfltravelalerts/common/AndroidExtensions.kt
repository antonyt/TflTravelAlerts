package com.tfltravelalerts.common

import android.app.Activity
import android.content.Context
import android.os.Build
import android.support.annotation.ColorRes
import android.support.annotation.IdRes
import android.view.View
import android.widget.TextView

fun Context.getColorCompat(@ColorRes resource: Int): Int {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        return resources.getColor(resource, theme)
    } else{
        @Suppress("DEPRECATION")
        return resources.getColor(resource)
    }
}

fun TextView.setTextColorRes(@ColorRes resource: Int) {
    val color = context.getColorCompat(resource)
    this.setTextColor(color)
}

// source: https://medium.com/@quiro91/improving-findviewbyid-with-kotlin-4cf2f8f779bb
fun <T : View> Activity.bind(@IdRes res: Int): Lazy<T> {
    return lazy(LazyThreadSafetyMode.NONE) { findViewById<T>(res) }
}

// source: https://medium.com/@quiro91/improving-findviewbyid-with-kotlin-4cf2f8f779bb
fun <T : View> View.bind(@IdRes res: Int): Lazy<T> {
    return lazy(LazyThreadSafetyMode.NONE) { findViewById<T>(res) }
}
