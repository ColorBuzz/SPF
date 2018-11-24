package com.colorbuzztechgmail.spf;

import android.content.Context;
import android.support.annotation.StringRes;
import android.support.design.widget.CoordinatorLayout;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

/**
 * Created by PC01 on 12/01/2018.
 */

public class CustomActionMode extends ActionMode {

    Context context;
    private MenuInflater inflater;
    String title="MULTICHOICE";

    public CustomActionMode(Context context) {
        super();

        this.context=context;

    }

    @Override
    public void setTitle(CharSequence title) {

        this.title=title.toString();

    }

    @Override
    public void setTitle(@StringRes int resId) {

    }

    @Override
    public void setSubtitle(CharSequence subtitle) {

    }

    @Override
    public void setSubtitle(@StringRes int resId) {

    }

    @Override
    public void setCustomView(View view) {


    }

    @Override
    public void invalidate() {

    }

    @Override
    public void finish() {

    }

    @Override
    public Menu getMenu() {
        return null;
    }

    @Override
    public CharSequence getTitle() {
        return title;
    }

    @Override
    public CharSequence getSubtitle() {
        return null;
    }

    @Override
    public View getCustomView() {
        return null;
    }

    @Override
    public MenuInflater getMenuInflater() {


        return new MenuInflater(context);
    }
}
