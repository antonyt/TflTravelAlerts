
package com.tfltravelalerts.common.eventbus;


import de.greenrobot.event.EventBus;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Base class for a fragment interested in events posted on the default
 * {@link EventBus}. Subclass can override {@link #registerSticky()} to
 * customize behavior.
 */
public abstract class EventBusFragment extends Fragment {

    private EventBus mEventBus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mEventBus = EventBus.getDefault();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (registerSticky()) {
            mEventBus.registerSticky(this);
        } else {
            mEventBus.register(this);
        }
    }

    @Override
    public void onPause() {
        mEventBus.unregister(this);
        super.onPause();
    }

    public EventBus getEventBus() {
        return mEventBus;
    }

    protected boolean registerSticky() {
        return true;
    }

}
