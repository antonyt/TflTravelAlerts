package com.tfltravelalerts.ui.main

import android.os.Bundle
import android.support.annotation.IdRes
import android.view.View
import com.tfltravelalerts.R
import com.tfltravelalerts.common.BaseActivity
import com.tfltravelalerts.ui.main.tabs.MainActivityTabsContract
import kotlinx.android.synthetic.main.main_activity.*

class MainActivity2 : BaseActivity(), MainActivityTabsContract.Host {

    private val toolbar by lazy { main_toolbar }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
    }

    override fun <T : View> bind(@IdRes res: Int): Lazy<T> {
        return bind(res)
    }
}
