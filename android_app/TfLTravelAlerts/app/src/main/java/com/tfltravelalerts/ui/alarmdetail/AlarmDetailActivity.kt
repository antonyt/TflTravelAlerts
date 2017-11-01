package com.tfltravelalerts.ui.alarmdetail

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxCompoundButton
import com.tfltravelalerts.R
import com.tfltravelalerts.common.BaseActivity
import com.tfltravelalerts.databinding.AlarmDetailBinding
import com.tfltravelalerts.databinding.AlarmDetailDayViewBinding
import com.tfltravelalerts.databinding.AlarmDetailLineViewBinding
import com.tfltravelalerts.model.AndroidTimePrinter
import com.tfltravelalerts.model.ConfiguredAlarm
import com.tfltravelalerts.model.Day
import com.tfltravelalerts.model.Line
import com.tfltravelalerts.model.Time
import io.reactivex.Observable
import kotlinx.android.synthetic.main.alarm_detail_day_view.view.*

private val EXTRA_ALARM = "alarm"

interface ViewActions {
    fun selectTimeIntent(): Observable<Any>
    fun selectDayIntent(): Observable<Pair<Day, Boolean>>
    fun selectLineIntent(): Observable<Pair<Line, Boolean>>
    fun notifyGoodServiceIntent(): Observable<Boolean>
    fun saveIntent(): Observable<Any>
    fun showData() : (UiData) -> Unit
}

interface Presenter {
    fun onAttach(view: ViewActions)
    fun onDetach()
}

data class UiData(val time: Time?, val days: Set<Day>, val lines: Set<Line>, val notifyGoodService: Boolean, val errorMessage : String?)

class AlarmDetailActivity : BaseActivity(), ViewActions {
    companion object {
        fun launchNewAlarm(context: Context, alarm: ConfiguredAlarm? = null) {
            val intent = Intent(context, AlarmDetailActivity::class.java)
            if (alarm != null) {
//                intent.putExtra(EXTRA_ALARM, alarm)
            }
            context.startActivity(intent)
        }

    }

    lateinit var binding: AlarmDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView<AlarmDetailBinding>(this, R.layout.alarm_detail)
        binding.timePrinter = AndroidTimePrinter(this)
//        setContentView(R.layout.alarm_detail)
        if (intent.hasExtra(EXTRA_ALARM)) {
//            binding.alarm
        }
    }

    override fun selectTimeIntent(): Observable<Any> {
        return RxView.clicks(binding.alarmDetailTimeLabel)
    }

    override fun selectDayIntent(): Observable<Pair<Day, Boolean>> {
        return selectDayIntent(binding.monday!!)
                .mergeWith(selectDayIntent(binding.tuesday!!))
                .mergeWith(selectDayIntent(binding.wednesday!!))
                .mergeWith(selectDayIntent(binding.thursday!!))
                .mergeWith(selectDayIntent(binding.friday!!))
                .mergeWith(selectDayIntent(binding.saturday!!))
                .mergeWith(selectDayIntent(binding.sunday!!))
    }

    private fun selectDayIntent(day: AlarmDetailDayViewBinding): Observable<Pair<Day, Boolean>> {
        return RxCompoundButton.checkedChanges(day.alarmDetailDaySelector.alarm_detail_day_selector)
                .map { checked -> Pair(day.day!!, checked) }
    }

    override fun selectLineIntent(): Observable<Pair<Line, Boolean>> {
        return selectLineIntent(binding.bakerloo!!)
                .mergeWith(selectLineIntent(binding.central!!))
                .mergeWith(selectLineIntent(binding.circle!!))
                .mergeWith(selectLineIntent(binding.district!!))
                .mergeWith(selectLineIntent(binding.hammersmithAndCity!!))
                .mergeWith(selectLineIntent(binding.jubilee!!))
                .mergeWith(selectLineIntent(binding.victoria!!))
                .mergeWith(selectLineIntent(binding.waterlooAndCity!!))
                .mergeWith(selectLineIntent(binding.overground!!))
                .mergeWith(selectLineIntent(binding.dlr!!))
//                .mergeWith(selectLineIntent(binding.tflRail!!))
//                .mergeWith(selectLineIntent(binding.trams!!))
    }

    private fun selectLineIntent(line: AlarmDetailLineViewBinding): Observable<Pair<Line, Boolean>> {
        return RxCompoundButton.checkedChanges(line.alarmDetailLineSelector.alarm_detail_day_selector)
                .map { checked -> Pair(line.line!!, checked) }
    }

    override fun notifyGoodServiceIntent(): Observable<Boolean> {
        return RxCompoundButton.checkedChanges(binding.notifyGoodService)
    }

    override fun saveIntent(): Observable<Any> {
        return RxView.clicks(binding.saveButton)
    }

    override fun showData(): (UiData) -> Unit = {
        with(binding) {
            days = it.days
            lines = it.lines
            doNotifyGoodService = it.notifyGoodService
            time = it.time
        }
    }
}