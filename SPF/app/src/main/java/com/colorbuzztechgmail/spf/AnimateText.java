package com.colorbuzztechgmail.spf;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by PC01 on 14/01/2018.
 */

public class AnimateText {


    Context context;

    public AnimateText (Context context){

        this.context=context;

    }





  public void AnimateView(final View v,boolean enable) {


     if (enable) {
        if (v instanceof TextView) {
            //-- the below line is returning null
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.fade_in);

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

            v.startAnimation(animation);

        } else if (v instanceof LinearLayout) {

            Drawable mdrawable;

            mdrawable = ((LinearLayout) v).getDividerDrawable();

            if (mdrawable instanceof Animatable) {

                ((Animatable) mdrawable).start();


            }

        }

     } else {

        if (v instanceof TextView) {
            v.setVisibility(View.INVISIBLE);
        /*    //-- the below line is returning null
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.fade_out);

            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    v.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    v.setVisibility(View.INVISIBLE);
//            Intent it  = new Intent(SplashActivity.this, MainActivity.class);
//            startActivity(it);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            v.startAnimation(animation);*/



        }else if (v instanceof LinearLayout) {


/*            Drawable mdrawable;

            mdrawable = ((LinearLayout) v).getDividerDrawable();

            if (mdrawable instanceof Animatable) {

                ((Animatable) mdrawable).start();


            }*/

        }
     }
  }

//
//textview.startAnimation(AnimationUtils.loadAnimation(c, android.R.anim.fade_in));

}
