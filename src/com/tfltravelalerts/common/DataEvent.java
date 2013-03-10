
package com.tfltravelalerts.common;

import de.greenrobot.event.EventBus;

/**
 * Abstract class to represent an event with associated data. Used with
 * {@link EventBus#post(Object)}.
 * 
 * @param <T>
 */
public abstract class DataEvent<T> {

    private final T mData;

    public DataEvent(T data) {
        mData = data;
    }

    public T getData() {
        return mData;
    }

}
