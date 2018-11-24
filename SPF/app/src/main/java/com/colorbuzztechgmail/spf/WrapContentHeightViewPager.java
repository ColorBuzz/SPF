package com.colorbuzztechgmail.spf;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class WrapContentHeightViewPager extends ViewPager implements AdapterFinishUpdateCallbacks,AdapterStartUpdateCallbacks {

    boolean isSettingHeight=false;
    boolean isPaginEnabled=true;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        requestLayout();
    }

    public boolean isPaginEnabled() {
        return isPaginEnabled;
    }

    public void setPaginEnabled(boolean paginEnabled) {
        isPaginEnabled = paginEnabled;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        try{

            return this.isPaginEnabled && super.onInterceptTouchEvent(ev);

        }catch (IllegalArgumentException e){

            e.printStackTrace();
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
        return this.isPaginEnabled && super.canScroll(v, checkV, dx, x, y);
    }

    /**
     * Constructor
     *
     * @param context the context
     */
    public WrapContentHeightViewPager(Context context) {
        super(context);
    }

    /**
     * Constructor
     *
     * @param context the context
     * @param attrs the attribute set
     */
    public WrapContentHeightViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }



    public void setVariableHeight()
    {
        // super.measure() calls finishUpdate() in adapter, so need this to stop infinite loop
        if (!this.isSettingHeight)
        {

            this.isSettingHeight = true;
            int maxChildHeight = 0;
            int widthMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredWidth(), MeasureSpec.EXACTLY);
            for (int i = 0; i < getChildCount(); i++)
            {
                View child = getChildAt(i);
                child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(ViewGroup.LayoutParams.WRAP_CONTENT, MeasureSpec.UNSPECIFIED));
                maxChildHeight = child.getMeasuredHeight() > maxChildHeight ? child.getMeasuredHeight() : maxChildHeight;
            }

            int height = maxChildHeight + getPaddingTop() + getPaddingBottom();
            int heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);

            super.measure(widthMeasureSpec, heightMeasureSpec);
            requestLayout();
            this.isSettingHeight = false;
        }
    }


    @Override
    public void onFinishUpdate() {

        setVariableHeight();
    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
        super.setAdapter(adapter);
        ((SectionPageAdapter)getAdapter()).setUpdateFinishListener(this);
        ((SectionPageAdapter)getAdapter()).setUpdateStarListener(this);

    }

    @Override
    public void onStartUpdate() {

    }
}