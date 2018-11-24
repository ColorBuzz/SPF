package com.colorbuzztechgmail.spf;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.colorbuzztechgmail.spf.databinding.CustomMaterialGridBinding;
import com.colorbuzztechgmail.spf.databinding.CustomMaterialListBinding;
import com.colorbuzztechgmail.spf.Utils.ViewMode;
import com.wajahatkarim3.easyflipview.EasyFlipView;

public class CustomMaterialCheckableListViewHolder extends ViewHolderMultiChoice{


    public TextView titleText;
    public ImageView image;
    public Toolbar toolbar;
    private View frontView,backView;
    public EasyFlipView flipView;
    private ViewMode viewMode;

    public CustomMaterialCheckableListViewHolder(ViewDataBinding binding,ViewMode mode) {

        super(binding,0);

        this.viewMode=mode;


        setCheckableContainer(binding.getRoot().findViewById(R.id.checkLayout));

    }

    public CustomMaterialCheckableListViewHolder(View v,ViewMode mode) {
        super(v);

        this.viewMode=mode;
        switch (mode){

            case GRID:

                toolbar   = (Toolbar) v.findViewById(R.id.toolbar);
               // toolbar.inflateMenu(R.menu.submenu_custom_material);
                frontView=v.findViewById(R.id.frontView);
                backView=v.findViewById(R.id.infoContainer);
                flipView=v.findViewById(R.id.flipView);
                image=v.findViewById(R.id.image2);
                v.setOnClickListener(this);
                v.setOnLongClickListener(this);
                setCheckableContainer(v.findViewById(R.id.checkLayout));

                break;


        }


    }



    @Override
    public void performBind(Object obj) {

        switch (viewMode) {

            case GRID:

                final CustomMaterialGridBinding MaterialBinding= DataBindingUtil.bind(frontView);

                MaterialBinding.setVariable(BR.obj,obj);
                MaterialBinding.executePendingBindings();

                MaterialBinding.notifyChange();
                break;



        }

        super.performBind(obj);

    }

    public static CustomMaterialCheckableListViewHolder create(ViewGroup parent, ViewMode viewMode) {
        ViewDataBinding viewDataBinding;

        switch (viewMode) {


            case LIST:

                viewDataBinding = CustomMaterialListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
                return new CustomMaterialCheckableListViewHolder(viewDataBinding,viewMode);


            case GRID:
                View view=  LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.custom_material_flippable_item, parent, false);
                return new  CustomMaterialCheckableListViewHolder (view,viewMode);

            default:

                return null;
        }

    }




    public void setPreview(View header, Material material) {

        if (material.getImage() != null) {
            image.setImageDrawable(new ShapeGenerator(this.mBinding.getRoot().getContext()).getDrawableShape(material.getImage(), ShapeGenerator.MODE_CIRCLE, 0, ShapeGenerator.SIZE_BIG));

        }else{

            image.setImageDrawable(new ShapeGenerator(this.mBinding.getRoot().getContext()).getDrawableShape(header.getContext().getDrawable(R.mipmap.ic_launcher), ShapeGenerator.MODE_CIRCLE, 0, ShapeGenerator.SIZE_BIG));

        }


    }
}







