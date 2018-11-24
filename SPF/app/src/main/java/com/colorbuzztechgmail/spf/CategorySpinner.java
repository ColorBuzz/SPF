package com.colorbuzztechgmail.spf;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by PC01 on 22/12/2017.
 */

public class CategorySpinner extends android.support.v7.widget.AppCompatSpinner {

    public CategorySpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private  String selectedCategory;
    @Override
    public void setOnItemSelectedListener(@Nullable OnItemSelectedListener listener) {
        super.setOnItemSelectedListener(listener);
    }

    @Override
    public void setPrompt(CharSequence prompt) {
        super.setPrompt(prompt);
    }

    public CategorySpinner(Context context) {
        super(context);

        this.setVisibility(VISIBLE);
        this.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
        this.setBackground(context.getResources().getDrawable(R.drawable.spinner_bakcground));
        this.setScrollBarStyle(SCROLLBARS_OUTSIDE_INSET);
        this.setVerticalScrollBarEnabled(true);


    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {


        return new Spinner.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
    }

    @Override
    public void setAdapter(SpinnerAdapter adapter) {

        super.setAdapter(adapter);

    }

    @Override
    public void setSelection(int position) {

        if(getAdapter().getCount()>0) {
            this.selectedCategory = ((String) getAdapter().getItem(position));
            super.setSelection(position);
        }
    }

    public  void setSelectedCategory(String category){

        final SpinnerAdapter adapter= this.getAdapter();

        for(int i=0; i<adapter.getCount();i++){

            if(adapter.getItem(i).equals(category)){

                this.setSelection(i);

            }


        }
    }

    public String getSelectedCategory() {
        return selectedCategory;
    }


    @Override
    public boolean performClick()
    {
        boolean bClicked = super.performClick();

        try
        {
            Field mPopupField = Spinner.class.getDeclaredField("mPopup");
            mPopupField.setAccessible(true);
            ListPopupWindow pop = (ListPopupWindow) mPopupField.get(this);
            ListView listview = pop.getListView();

            Field mScrollCacheField = View.class.getDeclaredField("mScrollCache");
            mScrollCacheField.setAccessible(true);
            Object mScrollCache = mScrollCacheField.get(listview);
            Field scrollBarField = mScrollCache.getClass().getDeclaredField("scrollBar");
            scrollBarField.setAccessible(true);
            Object scrollBar = scrollBarField.get(mScrollCache);
            Method method = scrollBar.getClass().getDeclaredMethod("setVerticalThumbDrawable", Drawable.class);
            method.setAccessible(true);
            method.invoke(scrollBar, getResources().getDrawable(R.drawable.drawable_orange_gradient));

            Method method1 = scrollBar.getClass().getDeclaredMethod("setVerticalTrackDrawable", Drawable.class);
            method1.setAccessible(true);
            method1.invoke(scrollBar, getResources().getDrawable(R.drawable.drawable_color_primary_gradient));

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            {
                Field mVerticalScrollbarPositionField = View.class.getDeclaredField("mVerticalScrollbarPosition");
                mVerticalScrollbarPositionField.setAccessible(true);
                mVerticalScrollbarPositionField.set(listview, SCROLLBAR_POSITION_RIGHT);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return bClicked;
    }
}


