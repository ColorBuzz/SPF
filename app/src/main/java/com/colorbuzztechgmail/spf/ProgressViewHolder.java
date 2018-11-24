package com.colorbuzztechgmail.spf;

import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.colorbuzztechgmail.spf.databinding.HeaderBinding;
import com.colorbuzztechgmail.spf.databinding.ProgressBinding;


public class ProgressViewHolder extends ViewHolderMultiChoice implements View.OnClickListener,View.OnLongClickListener {



    public TextView titleText;
    public ImageView image;
     private ItemClickListener itemClickListener;

    public ProgressViewHolder(ViewDataBinding binding) {
        super(binding,0);

        setCheckableContainer(null);
    }

    public static ProgressViewHolder create(ViewGroup parent) {



        ViewDataBinding viewDataBinding = ProgressBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);






        return new ProgressViewHolder(viewDataBinding);



    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }



    @Override
    public void onClick(View view) {
        if(itemClickListener!=null)
       this.itemClickListener.onClick(view, getAdapterPosition(), false);

    }

    @Override
    public boolean onLongClick(View v) {
        if(itemClickListener!=null)
        this.itemClickListener.onClick(v,getAdapterPosition(),true);
    return false;
    }




}






