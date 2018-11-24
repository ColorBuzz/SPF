package com.colorbuzztechgmail.spf;

public abstract class SingleLayoutAdapter extends MyBaseAdapter {



    public SingleLayoutAdapter(Dataset dataset,MainActivity ma) {
        super(dataset);

    }

    @Override
    protected int getLayoutIdForPosition(int position) {


        if (dataset.getObject(position) instanceof CustomHeader) {

            return R.layout.custom_header;
        }


        return R.layout.model_listview_item;


    }
}

