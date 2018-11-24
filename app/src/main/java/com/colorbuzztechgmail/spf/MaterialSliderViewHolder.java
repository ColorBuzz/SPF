package com.colorbuzztechgmail.spf;

import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.renderscript.ScriptGroup;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.colorbuzztechgmail.spf.databinding.MaterialCardBinding;

public class MaterialSliderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


    private ViewDataBinding mBinding;
    private ItemClickListener itemClickListener;
    public TextView titleText;
    public CheckableLinearLayout checkLinearLyt;
    public MaterialSliderViewHolder(ViewDataBinding binding)  {

        super(binding.getRoot());
        this.mBinding = binding;

        titleText = (TextView) mBinding.getRoot().findViewById(R.id.text);
        checkLinearLyt = (CheckableLinearLayout) mBinding.getRoot().findViewById(R.id.checkLayout);
        //v.setOnLongClickListener(this);
        binding.getRoot().setOnClickListener(this);

    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public static MaterialSliderViewHolder create(ViewGroup parent){

        View view =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_slider_layout, parent, false);

       ViewDataBinding viewDataBinding= MaterialCardBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);

        return new MaterialSliderViewHolder(viewDataBinding);

    }





    public void bindTo(final Material material){

        this.mBinding.setVariable(BR.obj,material);

        this.mBinding.executePendingBindings();
        //this.mBinding.setModel(model_cardview);

    }

    @Override
    public void onClick(View view) {

        this.itemClickListener.onClick(view,getAdapterPosition(),false);

    }

    public void  setHeaderDataStyle(View header,Material material) {

        final TextView title=(TextView) header.findViewById(R.id.text);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) title.getLayoutParams();
        params.width=FrameLayout.LayoutParams.WRAP_CONTENT;
        params.height=FrameLayout.LayoutParams.WRAP_CONTENT;

        if(material.getImage() !=null) {

            params.gravity= Gravity.CENTER_HORIZONTAL|Gravity.TOP;

            title.setBackground(this.itemView.getContext().getDrawable(R.drawable.drawable_transparent_gradient));

        }else{



            params.gravity=Gravity.CENTER;

            title.setBackground(new ColorDrawable(Color.TRANSPARENT));
        }
        title.setLayoutParams(params);





    }



}
