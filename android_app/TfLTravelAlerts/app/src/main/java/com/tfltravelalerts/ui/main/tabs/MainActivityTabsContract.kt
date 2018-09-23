package com.tfltravelalerts.ui.main.tabs

import android.support.annotation.IdRes
import android.support.annotation.StringRes
import android.support.v7.widget.Toolbar


interface MainActivityTabsContract {

    interface View {
        fun render()
    }

    interface Host {
        fun setTitle(@StringRes stringRes: Int)

        fun setSupportActionBar(toolbar: Toolbar?)

        fun <T : android.view.View> bind(@IdRes res: Int): Lazy<T>

        fun getString(@StringRes resId: Int): CharSequence
    }
}
