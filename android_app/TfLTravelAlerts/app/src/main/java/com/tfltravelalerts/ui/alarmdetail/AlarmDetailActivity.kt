package com.tfltravelalerts.ui.alarmdetail

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxCompoundButton
import com.tfltravelalerts.R
import com.tfltravelalerts.common.Assertions
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
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe

private val EXTRA_ALARM = "alarm"
private val log = Logger.DEFAULT

interface ViewActions {
    fun selectTimeIntent(): Observable<Any>
    fun updateTimeIntent() : Observable<Time>
    fun selectDayIntent(): Observable<Pair<Day, Boolean>>
    fun selectLineIntent(): Observable<Pair<Line, Boolean>>
    fun notifyGoodServiceIntent(): Observable<Boolean>
    fun saveIntent(): Observable<Any>

    fun showData() : (UiData) -> Unit
    fun promptTime() // this is not quite
}

data class UiData(val time: Time?, val days: Set<Day>, val lines: Set<Line>, val notifyGoodService: Boolean, val errorMessage : String?) {

    constructor(alarm: ConfiguredAlarm) : this(alarm.time, alarm.days, alarm.lines, alarm.notifyGoodService, null)


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

class UpdateTimeEvent(private val value: Time) : UiEvent() {
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

class NoChangeEvent : UiEvent() {
    override fun reduceState(state: UiData): UiData {
        return state
    }
}
class StateReducer {
    val reducer = { previousState : UiData, event: UiEvent -> event.reduceState(previousState)}
}

class Interactor(private val view : ViewActions) {

    // pseudo inject:
    val stateReducer = StateReducer()
    // val service = ...RetrofitService...

    fun bindIntents(initialData: UiData) {
        val daysObservable : Observable<UiEvent>
                = view.selectDayIntent().map { DaySelectionEvent(it) }

        val linesObservable : Observable<UiEvent>
                = view.selectLineIntent().map { LineSelectionEvent(it) }

        val notifyGoodServiceObservable : Observable<UiEvent>
                = view.notifyGoodServiceIntent().map { NotifyGoodServiceEvent(it)}

        val selectTimeObservable : Observable<UiEvent>
                = view.selectTimeIntent()
                .map { view.promptTime() }
                .map { NoChangeEvent() }

        val updateTimeObservable : Observable<UiEvent>
                = view.updateTimeIntent()
                .map { UpdateTimeEvent(it) }

        // TODO add error message intents
        Observable.mergeArray(daysObservable, linesObservable, notifyGoodServiceObservable, selectTimeObservable, updateTimeObservable)
                .doOnNext { log.d("AlarmDetail event: $it") }
                .scan(initialData, stateReducer.reducer )
                .subscribe(view.showData())
    }
}

class TimeSetObservable : ObservableOnSubscribe<Time> {
    private var emitter: ObservableEmitter<Time>? = null

    override fun subscribe(emitter: ObservableEmitter<Time>) {
        if (this.emitter != null) {
            Assertions.shouldNotHappen("subscribed twice")
        }
        this.emitter = emitter
    }

    fun onTimeSet(time: Time) {
        emitter?.onNext(time) ?: Assertions.shouldNotHappen("observer was null")
    }
}

class AlarmDetailActivity : BaseActivity(), ViewActions, MyTimePickerListener {
    companion object {
        fun launchNewAlarm(context: Context, alarm: ConfiguredAlarm? = null) {
            val intent = Intent(context, AlarmDetailActivity::class.java)
            if (alarm != null) {
                intent.putExtra(EXTRA_ALARM, alarm)
            }
            context.startActivity(intent)
        }
    }

    private lateinit var binding: AlarmDetailBinding
    private val timeObserver = TimeSetObservable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView<AlarmDetailBinding>(this, R.layout.alarm_detail)
        binding.timePrinter = AndroidTimePrinter(this)
        binding.executePendingBindings()
        val initialData: UiData
        if (intent.hasExtra(EXTRA_ALARM)) {
            val configuredAlarm = intent.getParcelableExtra<ConfiguredAlarm>(EXTRA_ALARM)
            initialData = UiData(configuredAlarm)
        } else {
            initialData = UiData(null, emptySet(), emptySet(), false, null)
        }
        val interactor  = Interactor(this)
        interactor.bindIntents(initialData)
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
                .skipInitialValue()
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
                .skipInitialValue()
                .map { checked -> Pair(line.line!!, checked) }
    }

    override fun notifyGoodServiceIntent(): Observable<Boolean> {
        return RxCompoundButton
                .checkedChanges(binding.notifyGoodService)
                .skipInitialValue()
    }

    override fun saveIntent(): Observable<Any> {
        return RxView.clicks(binding.saveButton)
    }

    override fun updateTimeIntent(): Observable<Time> {
        return Observable.create(timeObserver)
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

    override fun promptTime() {
        supportFragmentManager
                .beginTransaction()
                .add(MyAlarmTimePickerDialog.create(binding.time ?: Time(8, 0)), "time-picker")
                .addToBackStack(null)
                .commit()

    }

    override fun onTimeSelected(time: Time) {
        timeObserver.onTimeSet(time)
    }
}