package com.tfltravelalerts.ui.alarmdetail

import com.tfltravelalerts.common.Logger
import com.tfltravelalerts.common.StateMachine
import com.tfltravelalerts.databinding.AlarmDetailBinding
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import org.koin.core.parameter.parametersOf
import org.koin.standalone.KoinComponent
import org.koin.standalone.get

class AlarmDetailFactory : KoinComponent {
    fun createView(
            binding: AlarmDetailBinding,
            host: AlarmDetailContract.Host,
            initialData: UiData,
            subject: PublishSubject<AlarmDetailContract.Intent>,
            disposables: CompositeDisposable
    ) {
        val view = AlarmDetailView(binding, host)

        val stateMachine = StateMachine(initialData, get<AlarmDetailContract.Reducer>())

        val interactions = get<AlarmDetailContract.Interactions> {
            parametersOf(subject, view)
        }

        disposables.add(
                stateMachine
                        .observe()
                        .map { (state, event) ->
                            when (event) {
                                AlarmDetailContract.Intent.CloseView ->
                                    interactions.closeView()
                                AlarmDetailContract.Intent.OpenTimeSelection ->
                                    interactions.openTimeSelection(state)
                                AlarmDetailContract.Intent.Save ->
                                    interactions.save(state)
                            }
                        }.subscribe()
        )

        disposables.add(
                view
                        .getIntents()
                        .mergeWith(subject)
                        .doOnNext { Logger.d("on event: $it") }
                        .map(stateMachine::onEvent)
                        .doOnNext { Logger.d("new state: $it") }
                        .startWith(initialData)
                        .subscribe(view::render)

        )
    }
}
