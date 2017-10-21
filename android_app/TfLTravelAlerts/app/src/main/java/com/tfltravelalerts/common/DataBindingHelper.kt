package com.tfltravelalerts.common

import android.content.Context
import android.databinding.BindingAdapter
import android.databinding.BindingMethod
import android.databinding.BindingMethods
import android.graphics.PorterDuff
import android.support.annotation.ColorRes
import android.support.v4.content.res.ResourcesCompat
import android.view.View
import android.widget.ImageView



@BindingMethods(
        BindingMethod(attribute = "android:src", method = "setImageResource", type = ImageView::class)
)
object DataBindingHelper {
    private fun getColorValueCompat(context: Context, @ColorRes colorResId: Int): Int {
        return ResourcesCompat.getColor(context.resources, colorResId, context.theme)
    }

    @JvmStatic
    @BindingAdapter("backgroundTintCompat")
    fun setBackgroundTintCompat(v: View, @ColorRes color: Int) {
        if (color == 0) {
            v.background.colorFilter = null
        } else {
            v.background.setColorFilter(getColorValueCompat(v.context, color), PorterDuff.Mode.MULTIPLY)
        }
    }
}