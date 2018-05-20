package com.tfltravelalerts.common

import android.support.v7.app.AppCompatActivity
import com.tfltravelalerts.application.TtaApplication

abstract class BaseActivity : AppCompatActivity() {

    fun getTtaApp() = application as TtaApplication
}