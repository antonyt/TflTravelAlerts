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
import com.tfltravelalerts.model.TimePrinter
import com.tfltravelalerts.store.AlarmsStore
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers

private val EXTRA_ALARM = "alarm"
private val log = Logger.DEFAULT

interface ViewActions {
    fun selectTimeIntent(): Observable<Any>
    fun updateTimeIntent(): Observable<Time>
    fun selectDayIntent(): Observable<Pair<Day, Boolean>>
    fun selectLineIntent(): Observable<Pair<Line, Boolean>>
    fun notifyGoodServiceIntent(): Observable<Boolean>
    fun saveIntent(): Observable<Any>

    fun showData(): (UiData) -> Unit
    fun promptTime() // this is not quite
    fun close() // do I like this?
}

data class UiData(val id: Int, val time: Time?, val days: Set<Day>, val lines: Set<Line>, val notifyGoodService: Boolean, val enabled: Boolean, val errorMessage: String?) {

    constructor(alarm: ConfiguredAlarm) : this(alarm.id, alarm.time, alarm.days, alarm.lines, alarm.notifyGoodService, alarm.enabled, null)

    constructor() : this(ConfiguredAlarm.NEW_ALARM_ID, null, emptySet(), emptySet(), false, true, null)

    val alarm: ConfiguredAlarm by lazy { ConfiguredAlarm(id, time!!, days, lines, notifyGoodService, enabled) }

    val isNewAlarm = this.id == ConfiguredAlarm.NEW_ALARM_ID

    fun cloneWithTime(time: Time): UiData {

        return copy(time = time)
    }

    fun cloneWithDay(day: Day): UiData {
        return copy(days = days.plus(day))
    }

    fun cloneWithoutDay(day: Day): UiData {
        return copy(days = days.minus(day))
    }

    fun cloneWithLine(line: Line): UiData {
        return copy(lines = lines.plus(line))
    }

    fun cloneWithoutLine(line: Line): UiData {
        return copy(lines = lines.minus(line))
    }

    fun cloneWithNotifyGoodService(notifyGoodService: Boolean): UiData {
        return copy(notifyGoodService = notifyGoodService)
    }

    fun cloneWithErrorMessage(errorMessage: String?): UiData {
        return copy(errorMessage = errorMessage)
    }
}

interface UiEvent {
    fun reduceState(state: UiData): UiData
}

data class UpdateTimeEvent(private val value: Time) : UiEvent {
    override fun reduceState(state: UiData): UiData {
        return state.cloneWithTime(value)
    }
}

data class LineSelectionEvent(private val change: Pair<Line, Boolean>) : UiEvent {
    override fun reduceState(state: UiData): UiData {
        if (change.second) {
            return state.cloneWithLine(change.first)
        } else {
            return state.cloneWithoutLine(change.first)
        }
    }
}

data class DaySelectionEvent(private val change: Pair<Day, Boolean>) : UiEvent {
    override fun reduceState(state: UiData): UiData {
        return if (change.second) {
            state.cloneWithDay(change.first)
        } else {
            state.cloneWithoutDay(change.first)
        }
    }
}

data class NotifyGoodServiceEvent(private val value: Boolean) : UiEvent {
    override fun reduceState(state: UiData): UiData {
        return state.cloneWithNotifyGoodService(value)
    }
}

data class ErrorMessageEvent(private val value: String?) : UiEvent {
    override fun reduceState(state: UiData): UiData {
        return state.cloneWithErrorMessage(value)
    }
}

class NoChangeEvent : UiEvent {
    override fun reduceState(state: UiData): UiData {
        return state
    }
}

class StateReducer {
    val reducer = { previousState: UiData, event: UiEvent -> event.reduceState(previousState) }
}

class Interactor(private val view: ViewActions, private val store: AlarmsStore) {

    // pseudo inject:
    val stateReducer = StateReducer()
    // val service = ...RetrofitService...

    fun bindIntents(initialData: UiData) {
        val daysObservable: Observable<UiEvent>
                = view.selectDayIntent().map { DaySelectionEvent(it) }

        val linesObservable: Observable<UiEvent>
                = view.selectLineIntent().map { LineSelectionEvent(it) }

        val notifyGoodServiceObservable: Observable<UiEvent>
                = view.notifyGoodServiceIntent().map { NotifyGoodServiceEvent(it) }

        val selectTimeObservable: Observable<UiEvent>
                = view.selectTimeIntent()
                .map { view.promptTime() }
                .map { NoChangeEvent() }

        val updateTimeObservable: Observable<UiEvent>
                = view.updateTimeIntent()
                .map { UpdateTimeEvent(it) }

        // TODO add error message intents

        val uiDataObservable = Observable.mergeArray(daysObservable, linesObservable, notifyGoodServiceObservable, selectTimeObservable, updateTimeObservable)
                .doOnNext { log.d("event: $it") }
                .scan(initialData, stateReducer.reducer)
                .doOnNext { log.d("reduced state: $it") }
                .share()

        uiDataObservable
                .subscribe(view.showData())

        view.saveIntent()
                .withLatestFrom(uiDataObservable, BiFunction({ _: Any, data: UiData -> data }))
                .doOnNext { log.d("Alarm detail event: save") }
                .observeOn(Schedulers.io())
                .map { store.saveAlarm(it.alarm) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { view.close() }
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
    private lateinit var timePrinter: TimePrinter
    private var time: Time? = null
    private val timeObserver = TimeSetObservable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        timePrinter = AndroidTimePrinter(this)
        binding = DataBindingUtil.setContentView(this, R.layout.alarm_detail)
//        binding.
        binding.executePendingBindings()
        val initialData: UiData
        if (intent.hasExtra(EXTRA_ALARM)) {
            val configuredAlarm = intent.getParcelableExtra<ConfiguredAlarm>(EXTRA_ALARM)
            initialData = UiData(configuredAlarm)
        } else {
            initialData = UiData()
        }
        val interactor = Interactor(this, getTtaApp().alarmsStore)
        interactor.bindIntents(initialData)
    }

    override fun selectTimeIntent(): Observable<Any> {
        return RxView.clicks(binding.alarmDetailTimeLabel)
    }

    override fun selectDayIntent(): Observable<Pair<Day, Boolean>> {
        return selectDayIntent(binding.monday)
                .mergeWith(selectDayIntent(binding.tuesday))
                .mergeWith(selectDayIntent(binding.wednesday))
                .mergeWith(selectDayIntent(binding.thursday))
                .mergeWith(selectDayIntent(binding.friday))
                .mergeWith(selectDayIntent(binding.saturday))
                .mergeWith(selectDayIntent(binding.sunday))
    }

    private fun selectDayIntent(day: AlarmDetailDayViewBinding): Observable<Pair<Day, Boolean>> {
        return RxCompoundButton.checkedChanges(day.alarmDetailDaySelector)
                .skipInitialValue()
                .map { checked -> Pair(day.day!!, checked) }
    }

    override fun selectLineIntent(): Observable<Pair<Line, Boolean>> {
        return selectLineIntent(binding.bakerloo)
                .mergeWith(selectLineIntent(binding.central))
                .mergeWith(selectLineIntent(binding.circle))
                .mergeWith(selectLineIntent(binding.district))
                .mergeWith(selectLineIntent(binding.hammersmithAndCity))
                .mergeWith(selectLineIntent(binding.jubilee))
                .mergeWith(selectLineIntent(binding.metropolitan))
                .mergeWith(selectLineIntent(binding.northern))
                .mergeWith(selectLineIntent(binding.piccadilly))
                .mergeWith(selectLineIntent(binding.victoria))
                .mergeWith(selectLineIntent(binding.waterlooAndCity))
                .mergeWith(selectLineIntent(binding.overground))
                .mergeWith(selectLineIntent(binding.dlr))
//                .mergeWith(selectLineIntent(binding.tflRail))
//                .mergeWith(selectLineIntent(binding.trams))
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
        with(binding) {
            days = it.days
            lines = it.lines
            doNotifyGoodService = it.notifyGoodService
            it.time?.let {
                formattedTime = timePrinter.print(it)
                this@AlarmDetailActivity.time = it
            }
            isCreate = it.isNewAlarm
        }
    }

    override fun promptTime() {
        supportFragmentManager
                .beginTransaction()
                .add(MyAlarmTimePickerDialog.create(time ?: Time(8, 0)), "time-picker")
                .addToBackStack(null)
                .commit()

    }

    override fun onTimeSelected(time: Time) {
        timeObserver.onTimeSet(time)
    }

    override fun close() {
        finish()
    }
}