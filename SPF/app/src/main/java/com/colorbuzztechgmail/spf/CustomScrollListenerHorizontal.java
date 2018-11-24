package com.colorbuzztechgmail.spf;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

/**
 * Created by PC01 on 15/01/2018.
 */

public class CustomScrollListenerHorizontal extends RecyclerView.OnScrollListener {
    com.colorbuzztechgmail.spf.CardSliderLayoutManager lm;

    View v;
    boolean settling=false;


    public CustomScrollListenerHorizontal(View v) {
        this.v=v;

    }

    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

        switch (newState) {

            case RecyclerView.SCROLL_STATE_IDLE:



                break;

            case RecyclerView.SCROLL_STATE_DRAGGING:





                Log.e("SCROLL:","Scrolling now");
                break;
            case RecyclerView.SCROLL_STATE_SETTLING:
                ;
                break;

        }

    }

    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (dx > 0) {
            System.out.println("Scrolled Right");
        } else if (dx < 0) {
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




}