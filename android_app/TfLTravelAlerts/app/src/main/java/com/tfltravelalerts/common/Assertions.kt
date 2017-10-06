package com.tfltravelalerts.common

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.tfltravelalerts.BuildConfig
import java.lang.RuntimeException

class Assertions {

    companion object {
        fun shouldNotHappen(info: String, exception: Throwable? = null) {
            if (BuildConfig.DEBUG) {
                crashApp("should not happen: $info", exception)
            } else {
                Logger.DEFAULT.w("should not happen: $info", exception)
                // TODO could do analytics too
            }
        }

        fun crashApp(info: String, exception: Throwable? = null) {
            Handler(Looper.getMainLooper()).post({throw RuntimeException(info, exception)})
        }
    }

}
