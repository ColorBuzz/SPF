package com.colorbuzztechgmail.spf;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by PC01 on 02/01/2018.
 */

public class expandableContainer extends com.github.aakira.expandablelayout.ExpandableRelativeLayout {

    public expandableContainer(Context context) {
        super(context);
        this.generateDefaultLayoutParams();
        this.expand();

    }

    public expandableContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public expandableContainer(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public expandableContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setExpanded(boolean expanded) {
        super.setExpanded(expanded);
    }
}
