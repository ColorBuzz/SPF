package com.colorbuzztechgmail.spf;

import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.colorbuzztechgmail.spf.databinding.ExternalWorksBinding;

public class ExternalCheckableListViewHolder extends ViewHolderMultiChoice{


    public TextView titleText;
    public ImageView image;


    public ExternalCheckableListViewHolder(ViewDataBinding binding) {

        super(binding,0);

        setCheckableContainer(binding.getRoot().findViewById(R.id.checkLayout));

    }

    public void performBindExternal(ExternalWorks externalWorks) {

        this.mBinding.setVariable(BR.obj,externalWorks);

        this.mBinding.executePendingBindings();
        //this.mBinding.setModel(model_cardview);

    }


    public static ExternalCheckableListViewHolder create(ViewGroup parent) {


        ViewDataBinding viewDataBinding = ExternalWorksBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new ExternalCheckableListViewHolder(viewDataBinding);

    }




    public void setPreview(View header, ExternalWorks externalWorks) {


    }
}






