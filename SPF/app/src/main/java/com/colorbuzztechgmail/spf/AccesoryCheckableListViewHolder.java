package com.colorbuzztechgmail.spf;

import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.colorbuzztechgmail.spf.databinding.AccessoriesBinding;

public class AccesoryCheckableListViewHolder extends ViewHolderMultiChoice{


    public TextView titleText;
    public ImageView image;


    public AccesoryCheckableListViewHolder(ViewDataBinding binding) {

        super(binding,0);

        setCheckableContainer(binding.getRoot().findViewById(R.id.checkLayout));

    }

    public void performBindAccessories(Accessories accessory) {

        this.mBinding.setVariable(BR.obj,accessory);

        this.mBinding.executePendingBindings();
        //this.mBinding.setModel(model_cardview);

    }


    public static AccesoryCheckableListViewHolder create(ViewGroup parent) {


        ViewDataBinding viewDataBinding = AccessoriesBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new AccesoryCheckableListViewHolder(viewDataBinding);

    }




    public void setPreview(View header, Accessories accessory) {



    }
}






