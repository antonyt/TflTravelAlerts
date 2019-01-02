package com.tfltravelalerts.ui.alarmdetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.tfltravelalerts.common.BaseActivity
import com.tfltravelalerts.databinding.AlarmDetailBinding
import com.tfltravelalerts.di.Scopes
import com.tfltravelalerts.model.ConfiguredAlarm
import com.tfltravelalerts.model.Time
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import org.koin.android.ext.android.getKoin

class AlarmDetailActivity :
        BaseActivity(),
        MyTimePickerListener,
        AlarmDetailContract.Host {
    private val subject = PublishSubject.create<AlarmDetailContract.Intent>()
    private val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getKoin().createScope(Scopes.ALARM_DETAIL_SCREEN)

        val binding = AlarmDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // TODO use savedInstanceState
        val initialData = if (intent.hasExtra(EXTRA_ALARM)) {
            val configuredAlarm = intent.getParcelableExtra<ConfiguredAlarm>(EXTRA_ALARM)
            UiData(configuredAlarm)
        } else {
            UiData()
        }
        AlarmDetailFactory().createView(binding, this, initialData, subject, disposables)
    }

    // the activity shouldn't worry about this (but time picker dialog requires this)
    override fun onTimeSelected(time: Time) {
        subject.onNext(AlarmDetailContract.Intent.OnTimeSelected(time))
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
        disposables.clear()
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
