
package com.tfltravelalerts;

import de.greenrobot.event.EventBus;
import android.app.Service;

/**
 * Base class for a service interested in events posted on the default
 * {@link EventBus}. Subclass can override {@link #registerSticky()} to
 * customize behavior.
 */
public abstract class EventBusService extends Service {

    private EventBus mEventBus;

    @Override
    public void onCreate() {
        super.onCreate();
        mEventBus = EventBus.getDefault();
        if (registerSticky()) {
            mEventBus.registerSticky(this);
        } else {
            mEventBus.register(this);
        }
    }

    @Override
    public void onDestroy() {
        mEventBus.unregister(this);
        super.onDestroy();
    }

    public EventBus getEventBus() {
        return mEventBus;
    }

    protected boolean registerSticky() {
        return true;
    }
}
