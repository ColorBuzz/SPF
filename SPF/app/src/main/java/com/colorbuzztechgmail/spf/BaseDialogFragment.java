package com.colorbuzztechgmail.spf;

import android.app.Activity;
import android.support.v4.app.DialogFragment;
import android.view.ViewTreeObserver;

/**
 * Created by PC01 on 25/12/2017.
 */

public abstract class BaseDialogFragment<T> extends DialogFragment implements ViewTreeObserver.OnWindowFocusChangeListener{
    private T mActivityInstance;

    public final T getActivityInstance() {
        return mActivityInstance;
    }

    @Override
    public void onAttach(Activity activity) {
        mActivityInstance = (T) activity;
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivityInstance = null;
    }



    public abstract void onWindowFocusChanged(boolean hasFocus);
}