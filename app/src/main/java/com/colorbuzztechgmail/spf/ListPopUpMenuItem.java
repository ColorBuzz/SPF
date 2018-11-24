package com.colorbuzztechgmail.spf;

import android.graphics.drawable.Drawable;

/**
 * Created by PC01 on 07/04/2018.
 */

public class ListPopUpMenuItem {

        private String title;
        private int imageRes=0;
        private Drawable imageDrawable=null;
        public ListPopUpMenuItem(String title, int imageRes) {
            this.title = title;
            this.imageRes = imageRes;
        }

    public ListPopUpMenuItem(String title, Drawable image) {
        this.title = title;
        this.imageDrawable =image ;
    }

    public Drawable getImageDrawable() {
        return imageDrawable;
    }

    public void setImageDrawable(Drawable imageDrawable) {
        this.imageDrawable = imageDrawable;
    }

    public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getImageRes() {
            return imageRes;
        }

        public void setImageRes(int imageRes) {
            this.imageRes = imageRes;
        }
    }