package com.colorbuzztechgmail.spf;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * Created by PC01 on 14/01/2018.
 */

public class EqualItemManager extends GridLayoutManager {

    int pos;
    @Override
    public void addView(View child, int index) {

    }

    @Override
    public void onItemsAdded(RecyclerView recyclerView, int positionStart, int itemCount) {
        super.onItemsAdded(recyclerView, positionStart, itemCount);

    }

    public EqualItemManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }


}
