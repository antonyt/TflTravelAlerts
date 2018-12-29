package com.tfltravelalerts.common

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

abstract class StateMachine<State, Event>(initialState: State) {
    private var lastState = initialState

    private val subject: PublishSubject<Pair<State, Event>> = PublishSubject.create()

    fun observe(): Observable<Pair<State, Event>> = subject

    fun onEvent(event: Event): State {
        subject.onNext(lastState to event)
        lastState = reduceState(lastState, event)
        return lastState
    }

    /**
     * Subclasses should implement this method but nothing (except unit tests) should call this
     * directly because this doesn't keep track of the state.
     */
    internal abstract fun reduceState(currentState: State, event: Event): State
}
