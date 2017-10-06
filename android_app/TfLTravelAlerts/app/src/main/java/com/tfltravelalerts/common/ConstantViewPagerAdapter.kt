package com.tfltravelalerts.common

import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup

class ConstantViewPagerAdapter(private val mImplementation: Implementation) : PagerAdapter() {
    private val mViews: Array<View?> by lazy {
        arrayOfNulls<View?>(count)
    }

    fun canPageScrollVertically(position: Int): Boolean {
        return mImplementation.canPageScrollVertically(position)
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(mViews[position])
    }

    override fun getCount(): Int {
        return mImplementation.pageCount
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mImplementation.getPageTitle(position)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        var view: View? = mViews[position]
        if (view == null) {
            view = mImplementation.instantiateView(container, position)
            mViews[position] = view
        }
        container.addView(view)
        return view
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return obj === view
    }

    interface Implementation {
        fun canPageScrollVertically(position: Int): Boolean

        val pageCount: Int

        fun getPageTitle(position: Int): CharSequence?

        fun instantiateView(parent: ViewGroup, position: Int): View
    }
}
