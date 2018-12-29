package com.tfltravelalerts.common

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

interface Reducer<State, Event> {
    fun reduce(currentState: State, event: Event): State
}

class StateMachine<State, Event>(
        initialState: State,
        private val reducer: Reducer<State, Event>
) {
    private var lastState = initialState

    private val subject: PublishSubject<Pair<State, Event>> = PublishSubject.create()

    fun observe(): Observable<Pair<State, Event>> = subject

    fun onEvent(event: Event): State {
        subject.onNext(lastState to event)
        lastState = reducer.reduce(lastState, event)
        return lastState
    }
}
