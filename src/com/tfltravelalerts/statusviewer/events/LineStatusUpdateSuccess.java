
package com.tfltravelalerts.statusviewer.events;

import com.tfltravelalerts.model.LineStatusUpdateSet;

public class LineStatusUpdateSuccess {

    private LineStatusUpdateSet mLineStatusUpdateSet;

    public LineStatusUpdateSuccess(LineStatusUpdateSet lineStatusUpdateSet) {
        mLineStatusUpdateSet = lineStatusUpdateSet;
    }

    public LineStatusUpdateSet getLineStatusUpdateSet() {
        return mLineStatusUpdateSet;
    }

}
