package com.colorbuzztechgmail.spf;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

/**
 * Created by PC01 on 15/01/2018.
 */

public class CustomScrollListenerFadeView extends RecyclerView.OnScrollListener {

    View v;
    Context context;


    public CustomScrollListenerFadeView(Context context,View view) {
        this.v = view;
        this.context=context;


    }

    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

        switch (newState) {

            case RecyclerView.SCROLL_STATE_IDLE:

                enableAnimation(true,v);

                Log.e("SCROLL:", "Scrolling is not Scrolling");


                break;

            case RecyclerView.SCROLL_STATE_DRAGGING:

                enableAnimation(false,v);


                Log.e("SCROLL:", "Scrolling now Dragging");
                break;
            case RecyclerView.SCROLL_STATE_SETTLING:

                enableAnimation(false,v);
                Log.e("SCROLL:", "Scrolling now Settling");
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


    private void enableAnimation(boolean visible,final View v) {

        //-- the below line is returning null
        Animation animation=null;
        if(visible){

         animation = AnimationUtils.loadAnimation(context, R.anim.fade_in);

         animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                v.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

//            Intent it  = new Intent(SplashActivity.this, MainActivity.class);
//            startActivity(it);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        }else{

            if(v.getVisibility()==View.VISIBLE) {

                animation = AnimationUtils.loadAnimation(context, R.anim.fade_out);

                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        v.setVisibility(View.INVISIBLE);

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }


                });

            }

        }

        if(animation!=null){

            v.startAnimation(animation);

        }

    }

}