package com.colorbuzztechgmail.spf;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by PC01 on 13/04/2018.
 */


public class CustomSpinnerAdapter extends ArrayAdapter {

    private List<String> itemList;

    @Override
    public void addAll(Object[] items) {
        super.addAll(items);
    }

    @Override
    public void add(@Nullable Object object) {
        super.add(object);
    }

    public CustomSpinnerAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
    }
}

