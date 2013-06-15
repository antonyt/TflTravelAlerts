
package com.tfltravelalerts.navigationdrawer;

import android.content.Context;

public class AppScreen {
    public enum Screen {
        CURRENT_STATUS,
        WEEKEND_STATUS,
        LIST_OF_ALERTS,
        CREATE_ALERT
    }
    
    final public String name;
    final public Screen screen;
    final public Class<?> activityClass;
    
    public AppScreen(Context c, int nameResource, Screen screen, Class<?> activityClass) {
        this(c.getString(nameResource), screen, activityClass);
    }
    
    public AppScreen(String name, Screen screen, Class<?> activityClass) {
        this.name = name;
        this.screen = screen;
        this.activityClass = activityClass;
    }
    
    @Override
    public String toString() {
        return name;
    }
}
