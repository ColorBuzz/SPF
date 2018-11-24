package com.colorbuzztechgmail.spf;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.colorbuzztechgmail.spf.databinding.ModelCardBinding;
import com.colorbuzztechgmail.spf.databinding.ModelGridBinding;

public class ModelCheckableListViewHolder extends ViewHolderMultiChoice{


    public TextView titleText;
    public ImageView image;
    public Toolbar toolbar;


    public ModelCheckableListViewHolder(ViewDataBinding binding) {

        super(binding,0);

        setCheckableContainer(binding.getRoot().findViewById(R.id.checkLayout));
        toolbar   = (Toolbar) binding.getRoot().findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_pewview_model);
        image=binding.getRoot().findViewById(R.id.image);




    }

    public void performBindModel(PreviewModelInfo model) {

        this.mBinding.setVariable(BR.obj,model);

        this.mBinding.executePendingBindings();
        //this.mBinding.setModel(model_cardview);

    }


    public static ModelCheckableListViewHolder create(ViewGroup parent,Utils.ViewMode viewMode) {
        ViewDataBinding viewDataBinding;
        switch (viewMode){

            case LIST:
                viewDataBinding = ModelCardBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
                break;


            case GRID:

                viewDataBinding = ModelGridBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
                break;

                default:

                    return null;


        }



        return new ModelCheckableListViewHolder(viewDataBinding);

    }




    public void setPreview(View header, PreviewModelInfo model) {

        if (model.getImage() != null) {
            image.setImageDrawable(new ShapeGenerator(this.mBinding.getRoot().getContext()).getDrawableShape(model.getImage(), ShapeGenerator.MODE_CIRCLE, 0, ShapeGenerator.SIZE_BIG));

        }else{

            image.setImageDrawable(new ShapeGenerator(this.mBinding.getRoot().getContext()).getDrawableShape(header.getContext().getDrawable(R.mipmap.ic_launcher), ShapeGenerator.MODE_CIRCLE, 0, ShapeGenerator.SIZE_BIG));

        }


    }
}






