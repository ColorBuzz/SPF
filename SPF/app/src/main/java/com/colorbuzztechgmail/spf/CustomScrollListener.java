package com.colorbuzztechgmail.spf;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.ramotion.cardslider.*;
import com.ramotion.cardslider.CardSliderLayoutManager;

/**
 * Created by PC01 on 15/01/2018.
 */

public class CustomScrollListener extends RecyclerView.OnScrollListener {
    com.colorbuzztechgmail.spf.CardSliderLayoutManager lm;
    RecyclerView recyclerView;
    int lastPos=0;
      int activePos=0;
    private OnScrollViewListener mListener;
      StopLoadPieces stopLoadPieces;
    boolean settling=false;


    public CustomScrollListener(RecyclerView recyclerView) {
        this.recyclerView=recyclerView;
        this.lm=( com.colorbuzztechgmail.spf.CardSliderLayoutManager)recyclerView.getLayoutManager();

    }


    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

        switch (newState) {

            case RecyclerView.SCROLL_STATE_IDLE:




                lm.infoVisibility(true);
                if( recyclerView.getAdapter()!=null) {
                      activePos = lm.getActiveCardPosition();



                        lm.infoVisibility(true);


                        if (lastPos != activePos) {

                            ///Con esta comprobacion evitamos el parpadeo de la animacion mientras se reposiciona la vista.
                            Log.e("SCROLL", "LAST CARD" + String.valueOf(lastPos));
                            Log.e("SCROLL", "ACTIVE CARD" + String.valueOf(activePos));

                            if (lm.getBiggerView() != null) {


                                lm.updateBiggerView(lm.getBiggerView());
                            }
                            lastPos = activePos;


                        }


                    }


                Log.e("SCROLL:","Scrolling is not Scrolling");


                break;

            case RecyclerView.SCROLL_STATE_DRAGGING:

                if(stopLoadPieces!=null){
                    stopLoadPieces.onStopLoad();

                }

                lm.infoVisibility(false);




                Log.e("SCROLL:","Scrolling now");
                break;
            case RecyclerView.SCROLL_STATE_SETTLING:
                if(stopLoadPieces!=null){
                    stopLoadPieces.onStopLoad();

                }
                lm.infoVisibility(false);

                Log.e("SCROLL:","Scrolling Settling");
                break;

        }

    }

    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (dx > 0) {
            if (mListener != null) {
                mListener.onScrollChanged(dx);
            }
            System.out.println("Scrolled Right");
        } else if (dx < 0) {
            if (mListener != null) {
                mListener.onScrollChanged(dx);
            }
            System.out.println("Scrolled Left");
        } else {
            System.out.println("No Horizontal Scrolled");
        }

        if (dy > 0) {


            System.out.println("Scrolled Downwards");
        } else if (dy < 0) {

            System.out.println("Scrolled Upwards");
        } else {
            System.out.println("No Vertical Scrolled");
        }



    }

    public void setOnStopLoadListener(StopLoadPieces stop){

        this.stopLoadPieces=stop;





    }


    public void setOnScrollViewListener(OnScrollViewListener listener) {
        mListener = listener;
    }
    public static interface OnScrollViewListener {
        public void onScrollChanged(int dx);
    }

}