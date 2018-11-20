package com.example.akarpov.mainscreenplayground;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;

/**
 * Created by a.karpov on 12.11.2018.
 */

public class MainLayoutManager extends LinearLayoutManager {

    public MainLayoutManager(Context context) {
        super(context);
    }

    public MainLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public MainLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


}
