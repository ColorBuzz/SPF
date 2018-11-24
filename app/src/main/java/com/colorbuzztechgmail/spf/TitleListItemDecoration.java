package com.colorbuzztechgmail.spf;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by PC01 on 26/11/2017.
 */

public class TitleListItemDecoration extends RecyclerView.ItemDecoration   {
     private int spacing;
    private RecyclerView.Adapter adapter;
    int spanCount;
    boolean includeEdge;
    int itemPos=0;
     HeaderItemDecoration.StickyHeaderInterface mListener;

    public TitleListItemDecoration(int spacing, @Nullable HeaderItemDecoration.StickyHeaderInterface stickyHeaderInterface, int columns, boolean includeEdge) {
        this.spacing = spacing;
        this.adapter=adapter;
        this.spanCount=columns;
        this.includeEdge=includeEdge;
        this.mListener=stickyHeaderInterface;

    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
         itemPos = parent.getChildAdapterPosition(view); // item itemPos

        if (mListener!=null && itemPos>-1) {

            if (mListener.isHeader(itemPos)) {

                if (itemPos != 0)
                    outRect.top = spacing;
            }else{


                outRect.top = spacing;

                outRect.left =  spacing;
                outRect.right = spacing;
            }


        }else{

            outRect.top = spacing;

            outRect.left =  spacing;
            outRect.right = spacing;



        }

     }


}