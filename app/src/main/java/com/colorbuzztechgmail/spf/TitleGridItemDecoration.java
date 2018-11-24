package com.colorbuzztechgmail.spf;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

/**
 * Created by PC01 on 26/11/2017.
 */

public class TitleGridItemDecoration extends RecyclerView.ItemDecoration   {

    private int spacing;
    private RecyclerView.Adapter adapter;
    int spanCount;
    boolean includeEdge;
    int columnPos=0;
    int antPos=0;
    int headerCount=0;
    HeaderItemDecoration.StickyHeaderInterface mListener;

    public void setSpanCount(int spanCount) {
        this.spanCount = spanCount;
    }

    public TitleGridItemDecoration(int spacing, @Nullable HeaderItemDecoration.StickyHeaderInterface stickyHeaderInterface, int columns, boolean includeEdge) {
        this.spacing = spacing;
        this.spanCount=columns;
        this.includeEdge=includeEdge;
        this.mListener=stickyHeaderInterface;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // item position

        if (mListener!=null && position>-1) {

            if(mListener.isHeader(position)) {
                if(antPos>position){


                    Log.d("SCROLL","UP");
                    Log.d("POSITION",String.valueOf(position));

                }else{

                    Log.d("SCROLL","DOWN");
                    Log.d("POSITION",String.valueOf(position));

                }

                if (position != 0)
                    outRect.top = spacing;
            }else {

                int headerPos=mListener.getHeaderPositionForItem(position);
                int relativeItemPos=(position-headerPos)-1;

                 if (includeEdge) {

                     switch (getItemLocation(relativeItemPos % spanCount)){


                         case LEFT:

                             outRect.left =  spacing;
                             outRect.right =  (spacing/2);

                             outRect.top = spacing;
                             break;

                         case MIDDLE:
                             outRect.left =  spacing/2;
                             outRect.right =  (spacing/2);

                             outRect.top = spacing;

                             break;

                         case RIGHT:
                             outRect.left =  spacing/2;
                             outRect.right =  (spacing);

                             outRect.top = spacing;

                             break;






                     }





                     } else {
                         outRect.left = columnPos * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                         outRect.right = spacing - (columnPos + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)

                         outRect.top = spacing;


                }

            }

            if(antPos>position){

                Log.d("SCROLL","UP");
                Log.d("POSITION",String.valueOf(position));


            }else{

                Log.d("SCROLL","DOWN");
                Log.d("POSITION",String.valueOf(position));

            }

            antPos=position;

        }else{




            if (includeEdge) {
                outRect.left = spacing - columnPos * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (columnPos + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

           if(position < spanCount) { // top edge
                outRect.top = spacing;
            }
            outRect.top = spacing; // item bottom
            } else {
                outRect.left = columnPos * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (columnPos + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }

     }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
    }

    private enum ItemLocation{

        LEFT,
        RIGHT,
        MIDDLE;


    }


    private ItemLocation getItemLocation(int columnPosition){


        if(columnPosition==0){

            return ItemLocation.LEFT;

        }else if(columnPosition>=1 && columnPosition<spanCount-1){


            return ItemLocation.MIDDLE;


        }else {


            return ItemLocation.RIGHT;
        }




    }


}