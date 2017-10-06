package com.tfltravelalerts.ui.main

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tfltravelalerts.R
import com.tfltravelalerts.common.Assertions
import com.tfltravelalerts.common.ConstantViewPagerAdapter
import kotlinx.android.synthetic.main.main_activity.*

class MainActivity : AppCompatActivity() {

    private val mViewPagerAdapter by lazy { ConstantViewPagerAdapter(ViewPagerImpl()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        setTitle(R.string.full_app_name)
        setSupportActionBar(main_toolbar)
        main_view_pager.adapter = mViewPagerAdapter
        main_tab_strip.setupWithViewPager(main_view_pager)
        // TODO: finish the following lines:
//        addOnPageChangeListener(new TabChangeListener());
//        updateToolbarScrollingStatus();
//        getInitialData();
    }

    inner class ViewPagerImpl : ConstantViewPagerAdapter.Implementation {
        override val pageCount = 3
        val mViews: Array<View?> = arrayOfNulls<View?>(pageCount)

        override fun canPageScrollVertically(position: Int): Boolean {
            val view: View? = mViews[position]
            return view != null
                    &&
                    (view.canScrollVertically(1) || view.canScrollVertically(-1))
        }

        override fun getPageTitle(position: Int): CharSequence? {
            when (position) {
                0 -> return getString(R.string.main_tab_title_now)
                1 -> return getString(R.string.main_tab_title_weekend)
                2 -> return getString(R.string.main_tab_title_alarms)
                else -> {
                    Assertions.shouldNotHappen("invalid index: $position")
                    return ""
                }
            }
        }

        override fun instantiateView(parent: ViewGroup, position: Int): View {
            // TODO
            return TextView(parent.context)
        }

    }
}




