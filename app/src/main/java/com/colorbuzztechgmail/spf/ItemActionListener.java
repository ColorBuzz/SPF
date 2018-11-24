package com.colorbuzztechgmail.spf;

import android.view.View;

public interface ItemActionListener {

    void onPreview(Object obj);

    void toRemove(Object obj);

    void toEdit(long[] position);

    void onClick(View v, int position, boolean isLongClick);
}
