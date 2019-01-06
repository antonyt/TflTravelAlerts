package com.tfltravelalerts.ui.alarmdetail

import android.support.design.widget.Snackbar
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxCompoundButton
import com.tfltravelalerts.R
import com.tfltravelalerts.databinding.AlarmDetailBinding
import com.tfltravelalerts.databinding.AlarmDetailDayViewBinding
import com.tfltravelalerts.databinding.AlarmDetailLineViewBinding
import com.tfltravelalerts.model.AndroidTimePrinter
import com.tfltravelalerts.model.Time
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class AlarmDetailView(
        private val binding: AlarmDetailBinding,
        private val host: AlarmDetailContract.Host
) : AlarmDetailContract.View {
    // TODO inject this
    // TODO maybe this formatter should be in a mapper
    private val timeFormatter = AndroidTimePrinter(binding.root.context)
    private val snackbar: Snackbar
    private val subject = PublishSubject.create<AlarmDetailContract.Intent>()

    init {
        snackbar = Snackbar
                .make(
                        binding.alarmDetailScrollContainer,
                        R.string.alarm_details_snackbar_message,
                        Snackbar.LENGTH_INDEFINITE
                )
                .setAction(R.string.alarm_details_snackbar_action) {
                    subject.onNext(AlarmDetailContract.Intent.OpenTimeSelection)
                }
    }

    override fun getIntents(): Observable<AlarmDetailContract.Intent> {
        val events = ArrayList<Observable<AlarmDetailContract.Intent>>(26)

        events.addAll(selectDayIntents())
        events.addAll(selectLineIntents())

        events.add(
                RxCompoundButton
                        .checkedChanges(binding.notifyGoodService)
                        .skipInitialValue()
                        .map { AlarmDetailContract.Intent.NotifyGoodService(it) }
        )

        events.add(
                RxView
                        .clicks(binding.saveButton)
                        .map { AlarmDetailContract.Intent.Save }
        )

        events.add(
                RxView
                        .clicks(binding.alarmDetailTimeLabel)
                        .map { AlarmDetailContract.Intent.OpenTimeSelection }
        )

        events.add(subject)
        return Observable.merge(events)
    }

    private fun selectDayIntents() =
            listOf(
                    selectDayIntent(binding.monday),
                    selectDayIntent(binding.tuesday),
                    selectDayIntent(binding.wednesday),
                    selectDayIntent(binding.thursday),
                    selectDayIntent(binding.friday),
                    selectDayIntent(binding.saturday),
                    selectDayIntent(binding.sunday)
            )

    private fun selectDayIntent(dayBinding: AlarmDetailDayViewBinding): Observable<AlarmDetailContract.Intent> =
            RxCompoundButton.checkedChanges(dayBinding.alarmDetailDaySelector)
                    .skipInitialValue()
                    .map { AlarmDetailContract.Intent.DaySelection(dayBinding.day!!, it) }

    private fun selectLineIntents() =
            listOf(
                    selectLineIntent(binding.bakerloo),
                    selectLineIntent(binding.central),
                    selectLineIntent(binding.circle),
                    selectLineIntent(binding.district),
                    selectLineIntent(binding.hammersmithAndCity),
                    selectLineIntent(binding.jubilee),
                    selectLineIntent(binding.metropolitan),
                    selectLineIntent(binding.northern),
                    selectLineIntent(binding.piccadilly),
                    selectLineIntent(binding.victoria),
                    selectLineIntent(binding.waterlooAndCity),
                    selectLineIntent(binding.overground),
                    selectLineIntent(binding.dlr),
                    selectLineIntent(binding.tflRail),
                    selectLineIntent(binding.tram)
            )

    private fun selectLineIntent(lineBinding: AlarmDetailLineViewBinding): Observable<AlarmDetailContract.Intent> =
            RxCompoundButton.checkedChanges(lineBinding.alarmDetailLineSelector)
                    .skipInitialValue()
                    .map { AlarmDetailContract.Intent.LineSelection(lineBinding.line!!, it) }

    override fun render(data: UiData) {
        with(data) {
            binding.days = days
            binding.doNotifyGoodService = notifyGoodService
            binding.isCreate = isNewAlarm
            binding.formattedTime = time?.let { timeFormatter.print(it) }
            binding.lines = lines

            if (data.requestTime) {
                snackbar.show()
            } else {
                snackbar.dismiss()
            }
        }
    }

    override fun showTimePicker(time: Time) {
        host.showFragment("time-picker", MyAlarmTimePickerDialog.create(time))
    }

    override fun finish() {
        host.finish()
    }
}
