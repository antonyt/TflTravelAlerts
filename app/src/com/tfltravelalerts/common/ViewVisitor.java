
package com.tfltravelalerts.common;

import android.view.View;
import android.view.ViewGroup;

public class ViewVisitor {

    public static void visitAll(View root, ViewVisitorCallback callback) {
        callback.onViewVisited(root);

        if (root instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) root;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View child = viewGroup.getChildAt(i);
                visitAll(child, callback);
            }
        }
    }

    public interface ViewVisitorCallback {

        void onViewVisited(View view);
    }
}
