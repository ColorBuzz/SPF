package com.colorbuzztechgmail.spf;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ArrayAdapter;

import java.lang.reflect.Array;
import java.util.List;

/**
 * Created by PC01 on 22/12/2017.
 */

public class SpinnerArrayAdapter extends ArrayAdapter {
    @Override
    public void add(@Nullable Object object) {
        super.add(object);
    }

    @Override
    public void remove(@Nullable Object object) {
        super.remove(object);
    }

    @Override
    public void clear() {
        super.clear();
    }

    @Override
    public boolean isEmpty() {
        return super.isEmpty();
    }

    public SpinnerArrayAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List objects) {
        super(context, resource, objects);



    }
}
