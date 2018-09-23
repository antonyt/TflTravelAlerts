package com.tfltravelalerts.common

abstract class StateMachine<State, Event>(initialState: State) {
    // TODO good or bad to allow to see this?
    // TODO should allow objects to observe this?
    var lastState = initialState


    fun onEvent(event: Event): State {
        lastState = reduceState(lastState, event)
        return lastState
    }

    /**
     * Subclasses should implement this method but nothing (except unit tests) should call this
     * directly because this doesn't keep track of the state.
     */
    internal abstract fun reduceState(currentState: State, event: Event): State
}
