package com.colorbuzztechgmail.spf;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

/**
 * Created by PC01 on 22/12/2017.
 */

public class CategoryCheck extends android.support.v7.widget.AppCompatCheckBox {

    private int id;


    public CategoryCheck(Context context) {
        super(context);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
    }

    @Override
    public boolean isChecked() {
        return super.isChecked();
    }

    @Override
    public void setChecked(boolean checked) {
        super.setChecked(checked);
    }

    @Override
    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        super.setOnCheckedChangeListener(listener);
    }

    @Override
    public CharSequence getText() {
        return super.getText();
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

}
