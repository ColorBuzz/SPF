package com.colorbuzztechgmail.spf;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.colorbuzztechgmail.spf.databinding.CategoryListBinding;


public class RecentCheckableListViewHolder extends ViewHolderMultiChoice{


    public TextView titleText;
    public ImageView image;
    public Toolbar toolbar;


    public RecentCheckableListViewHolder(ViewDataBinding binding) {

        super(binding, 0);

        toolbar = (Toolbar) binding.getRoot().findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_directory);
        image = binding.getRoot().findViewById(R.id.image);

        toolbar.setOnClickListener(this);
    }
    public void performBindRecent(Category category) {

        this.mBinding.setVariable(BR.obj,category);

        this.mBinding.executePendingBindings();
        //this.mBinding.setModel(model_cardview);

    }



    public static RecentCheckableListViewHolder create(ViewGroup parent, Utils.ViewMode viewMode) {
        ViewDataBinding viewDataBinding;



        viewDataBinding = CategoryListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);






        return new RecentCheckableListViewHolder(viewDataBinding);

    }




    public void setPreview(View header, PreviewModelInfo model) {

        if (model.getImage() != null) {
            image.setImageDrawable(new ShapeGenerator(this.mBinding.getRoot().getContext()).getDrawableShape(model.getImage(), ShapeGenerator.MODE_CIRCLE, 0, ShapeGenerator.SIZE_BIG));

        }else{

            image.setImageDrawable(new ShapeGenerator(this.mBinding.getRoot().getContext()).getDrawableShape(header.getContext().getDrawable(R.mipmap.ic_launcher), ShapeGenerator.MODE_CIRCLE, 0, ShapeGenerator.SIZE_BIG));

        }


    }
}






