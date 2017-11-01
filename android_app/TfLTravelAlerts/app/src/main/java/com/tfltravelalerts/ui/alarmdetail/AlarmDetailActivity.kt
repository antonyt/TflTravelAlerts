package com.tfltravelalerts.ui.alarmdetail

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxCompoundButton
import com.tfltravelalerts.R
import com.tfltravelalerts.common.BaseActivity
import com.tfltravelalerts.common.Logger
import com.tfltravelalerts.databinding.AlarmDetailBinding
import com.tfltravelalerts.databinding.AlarmDetailDayViewBinding
import com.tfltravelalerts.databinding.AlarmDetailLineViewBinding
import com.tfltravelalerts.model.AndroidTimePrinter
import com.tfltravelalerts.model.ConfiguredAlarm
import com.tfltravelalerts.model.Day
import com.tfltravelalerts.model.Line
import com.tfltravelalerts.model.Time
import io.reactivex.Observable

private val EXTRA_ALARM = "alarm"
private val log = Logger.DEFAULT

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

data class UiData(val time: Time?, val days: Set<Day>, val lines: Set<Line>, val notifyGoodService: Boolean, val errorMessage : String?) {

    private fun cloneAux(time: Time? = this.time,
                         days: Set<Day> = this.days,
                         lines: Set<Line> = this.lines,
                         notifyGoodService: Boolean = this.notifyGoodService,
                         errorMessage: String? = this.errorMessage): UiData {
        return UiData(time, days, lines, notifyGoodService, errorMessage)
    }

    fun cloneWithTime(time: Time): UiData {
        return cloneAux(time = time)
    }

    fun cloneWithDay(day: Day): UiData {
        return cloneAux(days = days.plus(day))
    }

    fun cloneWithoutDay(day: Day): UiData {
        return cloneAux(days = days.minus(day))
    }

    fun cloneWithLine(line: Line): UiData {
        return cloneAux(lines = lines.plus(line))
    }

    fun cloneWithoutLine(line: Line): UiData {
        return cloneAux(lines = lines.minus(line))
    }

    fun cloneWithNotifyGoodService(notifyGoodService: Boolean): UiData {
        return cloneAux(notifyGoodService = notifyGoodService)
    }

    fun cloneWithErrorMessage(errorMessage: String?): UiData {
        return cloneAux(errorMessage = errorMessage)
    }
}

sealed class UiEvent {
    abstract fun reduceState(state:UiData) : UiData
}

class TimeEvent(private val value: Time) : UiEvent() {
    override fun reduceState(state: UiData): UiData {
        return state.cloneWithTime(value)
    }
}

class LineSelectionEvent(private val change: Pair<Line, Boolean>) : UiEvent() {
    override fun reduceState(state: UiData): UiData {
        if (change.second) {
            return state.cloneWithLine(change.first)
        } else {
            return state.cloneWithoutLine(change.first)
        }
    }
}

class DaySelectionEvent(private val change: Pair<Day, Boolean>) : UiEvent() {
    override fun reduceState(state: UiData): UiData {
        if (change.second) {
            return state.cloneWithDay(change.first)
        } else {
            return state.cloneWithoutDay(change.first)
        }
    }
}

class NotifyGoodServiceEvent(private val value: Boolean) : UiEvent() {
    override fun reduceState(state: UiData): UiData {
        return state.cloneWithNotifyGoodService(value)
    }
}

class ErrorMessageEvent(private val value: String?) : UiEvent() {
    override fun reduceState(state: UiData): UiData {
        return state.cloneWithErrorMessage(value)
    }
}

class StateReducer {
    val reducer = { previousState : UiData, event: UiEvent -> event.reduceState(previousState)}
}

class Interactor(private val view : ViewActions) {

    // inject
    val stateReducer = StateReducer()

    fun bindIntents(initialData: UiData) {
        val daysObservable : Observable<UiEvent>
                = view.selectDayIntent().doOnNext { log.d("rx event: $it") }.map { DaySelectionEvent(it) }

        val linesObservable : Observable<UiEvent>
                = view.selectLineIntent().doOnNext { log.d("rx event: $it") }.map { LineSelectionEvent(it) }

        val notifyGoodServiceObservable : Observable<UiEvent>
                = view.notifyGoodServiceIntent().doOnNext { log.d("rx event: $it") }.map { NotifyGoodServiceEvent(it)}

        // TODO add Time intents
        // TODO add error message intents
        Observable.merge(daysObservable, linesObservable, notifyGoodServiceObservable)
                .scan(initialData, stateReducer.reducer )
                .doOnNext { log.d("reduced state: $it") }
                .subscribe(view.showData())
    }
}

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
        binding.executePendingBindings()
        val interactor  = Interactor(this)
        interactor.bindIntents(UiData(null, emptySet(), emptySet(), false, null))
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
        return RxCompoundButton.checkedChanges(day.alarmDetailDaySelector)
                .map { checked -> Pair(day.day!!, checked) }
    }

    override fun selectLineIntent(): Observable<Pair<Line, Boolean>> {
        return selectLineIntent(binding.bakerloo!!)
                .mergeWith(selectLineIntent(binding.central!!))
                .mergeWith(selectLineIntent(binding.circle!!))
                .mergeWith(selectLineIntent(binding.district!!))
                .mergeWith(selectLineIntent(binding.hammersmithAndCity!!))
                .mergeWith(selectLineIntent(binding.jubilee!!))
                .mergeWith(selectLineIntent(binding.metropolitan!!))
                .mergeWith(selectLineIntent(binding.northern!!))
                .mergeWith(selectLineIntent(binding.piccadilly!!))
                .mergeWith(selectLineIntent(binding.victoria!!))
                .mergeWith(selectLineIntent(binding.waterlooAndCity!!))
                .mergeWith(selectLineIntent(binding.overground!!))
                .mergeWith(selectLineIntent(binding.dlr!!))
//                .mergeWith(selectLineIntent(binding.tflRail!!))
//                .mergeWith(selectLineIntent(binding.trams!!))
    }

    private fun selectLineIntent(line: AlarmDetailLineViewBinding): Observable<Pair<Line, Boolean>> {
        return RxCompoundButton.checkedChanges(line.alarmDetailLineSelector)
                .map { checked -> Pair(line.line!!, checked) }
    }

    override fun notifyGoodServiceIntent(): Observable<Boolean> {
        return RxCompoundButton.checkedChanges(binding.notifyGoodService)
    }

    override fun saveIntent(): Observable<Any> {
        return RxView.clicks(binding.saveButton)
    }

    override fun showData(): (UiData) -> Unit = {
        log.d("show data: $it")
        with(binding) {
            days = it.days
            lines = it.lines
            doNotifyGoodService = it.notifyGoodService
            time = it.time
        }
    }
}