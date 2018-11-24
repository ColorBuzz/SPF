package com.colorbuzztechgmail.spf;

import android.app.SearchableInfo;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.SearchView;
import android.util.AttributeSet;

/**
 * Created by PC01 on 03/12/2017.
 */

public class CustomSearchView extends SearchView {
    @Override
    public void setQuery(CharSequence query, boolean submit) {
        super.setQuery(query, submit);
    }

    public CustomSearchView(Context context) {
        super(context);

    }

    public CustomSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomSearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return super.generateDefaultLayoutParams();
    }

    @Override
    public void setBackgroundColor(@ColorInt int color) {


        super.setBackgroundColor(color);
    }


}
