package com.tfltravelalerts.common

import android.content.Context
import android.os.Build
import android.support.annotation.ColorRes
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