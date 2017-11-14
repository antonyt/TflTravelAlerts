package com.tfltravelalerts.common

import android.util.Log

class Logger(private val tag: String) {

    companion object {
        val DEFAULT = Logger("TTA")
        val DATABASE = Logger("TTA-DB")

        fun d(message: String) {
            DEFAULT.d(message)
        }
    }

    fun d(message: String, throwable: Throwable?) {
        Log.d(tag, message, throwable)
    }

    fun d(message: String) {
        Log.d(tag, message)
    }

    fun w(message: String, throwable: Throwable?) {
        Log.w(tag, message, throwable)
    }

    fun w(message: String) {
        Log.w(tag, message)
    }

    fun e(message: String, throwable: Throwable?) {
        Log.e(tag, message, throwable)
    }

    fun e(message: String) {
        Log.e(tag, message)
    }
}