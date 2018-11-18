package com.tfltravelalerts.ui.alarmdetail2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.tfltravelalerts.common.BaseActivity
import com.tfltravelalerts.databinding.AlarmDetailBinding
import com.tfltravelalerts.di.Scopes
import com.tfltravelalerts.model.ConfiguredAlarm
import com.tfltravelalerts.model.Time
import com.tfltravelalerts.ui.alarmdetail.MyTimePickerListener
import com.tfltravelalerts.ui.alarmdetail.UiData
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.android.inject

class AlarmDetailActivity :
        BaseActivity(),
        MyTimePickerListener,
        AlarmDetailContract.Host {

    private lateinit var initialData: UiData
    private val presenter: AlarmDetailContract.Presenter by inject()
    private lateinit var view: AlarmDetailContract.View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getKoin().createScope(Scopes.ALARM_DETAIL_SCREEN)

        val binding = AlarmDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // TODO use savedInstanceState
        initialData = if (intent.hasExtra(EXTRA_ALARM)) {
            val configuredAlarm = intent.getParcelableExtra<ConfiguredAlarm>(EXTRA_ALARM)
            UiData(configuredAlarm)
        } else {
            UiData()
        }
        view = AlarmDetailView(binding, this)
        presenter.init(initialData, view)
    }

    // TODO the activity shouldn't worry about this (but Time picker dialog requires this)
    override fun onTimeSelected(time: Time) {
        presenter.onTimeSelected(time)
    }

    // TODO this could have been done in a better way (e.g. without exposing the fragment class)
    // but was done like this so because we are not touching that part yet
    override fun showFragment(tag: String, fragment: Fragment) {
        supportFragmentManager
                .beginTransaction()
                .add(fragment, tag)
                .addToBackStack(null)
                .commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        getKoin().getScope(Scopes.ALARM_DETAIL_SCREEN).close()
    }

    companion object {
        private const val EXTRA_ALARM = "alarm"

        fun launchNewAlarm(context: Context, alarm: ConfiguredAlarm? = null) {
            val intent = Intent(context, AlarmDetailActivity::class.java)
            if (alarm != null) {
                intent.putExtra(EXTRA_ALARM, alarm)
            }
            context.startActivity(intent)
        }
    }
}
