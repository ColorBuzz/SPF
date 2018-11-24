package com.colorbuzztechgmail.spf;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by PC01 on 26/11/2017.
 */

public class TitleListItemDecoration extends RecyclerView.ItemDecoration {

    private int spacing;
    private RecyclerView.Adapter adapter;
    int spanCount;
    boolean includeEdge;

    public TitleListItemDecoration(int spacing, RecyclerView.Adapter adapter, int columns, boolean includeEdge) {
        this.spacing = spacing;
        this.adapter=adapter;
        this.spanCount=columns;
        this.includeEdge=includeEdge;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // item position

        if (((ModelListAdapter)adapter).isHeader(position)) {

            if(position!=0)
                outRect.top = spacing;


        }else{


            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

           if(position < spanCount) { // top edge
                outRect.top = spacing;
            }
            outRect.top = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }


     }}