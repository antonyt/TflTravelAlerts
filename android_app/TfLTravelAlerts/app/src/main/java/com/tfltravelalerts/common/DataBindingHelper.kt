package com.tfltravelalerts.common

import android.content.Context
import android.databinding.BindingAdapter
import android.databinding.BindingMethod
import android.databinding.BindingMethods
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.InsetDrawable
import android.graphics.drawable.RippleDrawable
import android.graphics.drawable.StateListDrawable
import android.os.Build.VERSION
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.support.v4.content.res.ResourcesCompat
import android.view.View
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.ToggleButton
import com.tfltravelalerts.R
import com.tfltravelalerts.model.Line


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

    @JvmStatic
    @BindingAdapter("constantToggleText")
    fun setConstantText(toggle: ToggleButton, string: String?) {
        toggle.textOff = string
        toggle.textOn = string
        toggle.text = string
    }

    @JvmStatic
    @BindingAdapter("constantToggleText")
    fun setConstantText(toggle: ToggleButton, @StringRes string: Int) {
        val txt: String?
        if (string == 0) {
            txt = null
        } else {
            txt = toggle.resources.getString(string)
        }
        setConstantText(toggle, txt)
    }

    @JvmStatic
    @BindingAdapter("configureColors")
    fun setupLineToggleButton(view: CompoundButton, line: Line) {
        val defaultDrawable = getDrawableCompat(view.context, R.drawable.line_selector_base)
        val selectedDrawable = getDrawableCompat(view.context, R.drawable.line_selector_base)
        setUpdateBackgroundColor(view, line, selectedDrawable)
        val hInsets = view.context.resources.getDimensionPixelSize(R.dimen.custom_toggle_horizontal_inset)
        val vInsets = view.context.resources.getDimensionPixelSize(R.dimen.custom_toggle_vertical_inset)
        val defaultDrawable2 = InsetDrawable(defaultDrawable, hInsets, vInsets, hInsets, vInsets)
        val selectedDrawable2 = InsetDrawable(selectedDrawable, hInsets, vInsets, hInsets, vInsets)
        val stateListDrawable = StateListDrawable()
        stateListDrawable.setExitFadeDuration(150)
        stateListDrawable.addState(intArrayOf(android.R.attr.state_checked), selectedDrawable2)
        stateListDrawable.addState(IntArray(0), defaultDrawable2)
        view.background = stateListDrawable
        if (!line.isDarkBackground) {
            val color = view.context.getColorCompat(R.color.alarm_detail_line_text_dark)
            view.setTextColor(color)
        }
    }

    private fun setUpdateBackgroundColor(view: CompoundButton, line: Line, selectedDrawable: Drawable) {
        @Suppress("NAME_SHADOWING")
        var selectedDrawable = selectedDrawable
        if (VERSION.SDK_INT >= 21 && selectedDrawable is RippleDrawable) {
            selectedDrawable = selectedDrawable.getDrawable(0)
        }
        if (selectedDrawable is GradientDrawable) {
            selectedDrawable.mutate()
            selectedDrawable.setColor(getColorValueCompat(view.context, line.colorResId))
            return
        }
        Assertions.shouldNotHappen("drawable is not a ShapeDrawable")
    }

    private fun getDrawableCompat(context: Context, @DrawableRes drawableId: Int): Drawable {
        return ResourcesCompat.getDrawable(context.resources, drawableId, context.theme) ?: throw IllegalStateException("Failed to get drawable $drawableId")
    }
}