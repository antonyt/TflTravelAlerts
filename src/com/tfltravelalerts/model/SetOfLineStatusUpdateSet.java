
package com.tfltravelalerts.model;

import java.util.Collection;
import java.util.HashMap;

public class SetOfLineStatusUpdateSet {

    private HashMap<Integer, LineStatusUpdateSet> mLineStatusUpdateSet;

    public SetOfLineStatusUpdateSet() {
        // this would be more efficient if we used SparseArray
        mLineStatusUpdateSet = new HashMap<Integer, LineStatusUpdateSet>();
    }

    public LineStatusUpdateSet get(int key) {
        return mLineStatusUpdateSet.get(key);
    }

    public Collection<LineStatusUpdateSet> values() {
        return mLineStatusUpdateSet.values();
    }

    public void put(int key, LineStatusUpdateSet value) {
        mLineStatusUpdateSet.put(key, value);
    }

    public int size() {
        return mLineStatusUpdateSet.size();
    }

    public void remove(int key) {
        mLineStatusUpdateSet.remove(key);
    }

}
