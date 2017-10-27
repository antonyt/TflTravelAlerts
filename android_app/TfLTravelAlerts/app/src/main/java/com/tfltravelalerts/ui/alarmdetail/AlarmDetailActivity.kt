
package com.tfltravelalerts.ui.alarmdetail

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import com.tfltravelalerts.R
import com.tfltravelalerts.common.BaseActivity
import com.tfltravelalerts.databinding.AlarmDetailBinding
import com.tfltravelalerts.model.AndroidTimePrinter
import com.tfltravelalerts.model.ConfiguredAlarm

private val EXTRA_ALARM = "alarm"

class AlarmDetailActivity : BaseActivity() {
    companion object {
        fun launchNewAlarm(context: Context, alarm: ConfiguredAlarm? = null) {
            val intent = Intent(context, AlarmDetailActivity::class.java)
            if (alarm != null) {
//                intent.putExtra(EXTRA_ALARM, alarm)
            }
            context.startActivity(intent)
        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<AlarmDetailBinding>(this, R.layout.alarm_detail)
        binding.timePrinter = AndroidTimePrinter(this)

        if (intent.hasExtra(EXTRA_ALARM)) {
//           TODO
//            binding.alarm
        }
    }
}