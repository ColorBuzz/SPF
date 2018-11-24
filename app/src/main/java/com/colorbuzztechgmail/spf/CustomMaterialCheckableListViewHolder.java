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
        toolbar   = (Toolbar) binding.getRoot().findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_pewview_model);
        image=binding.getRoot().findViewById(R.id.image);

        /* binding.getRoot().findViewById(R.id.container).setOnClickListener(this);
        binding.getRoot().findViewById(R.id.container).setOnLongClickListener(this);
        */
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);

        if(v.getId()==R.id.toolbar){

            toolbar.setSelected(true);

        }
    }

    @Override
    public void performBind(final Object obj) {

        switch (viewMode) {

            case GRID:
                super.performBind(obj);
                break;

/*

                final CustomMaterialGridBinding MaterialBinding= DataBindingUtil.bind(frontView);

                MaterialBinding.setVariable(BR.obj,obj);
                MaterialBinding.executePendingBindings();

                MaterialBinding.notifyChange();

                final  MaterialinfoContainer materialinfoContainer= new MaterialinfoContainer(itemView.getContext(),MaterialinfoContainer.MODE_CUSTOM_MATERIAL);
                materialinfoContainer.setView(backView);

                flipView.setOnFlipListener(new EasyFlipView.OnFlipAnimationListener() {
                    @Override
                    public void onViewFlipCompleted(EasyFlipView easyFlipView, EasyFlipView.FlipState newCurrentSide) {

                        switch (newCurrentSide) {

                            case BACK_SIDE:


                                materialinfoContainer.startAnimation(  true, obj);

                                break;

                            case FRONT_SIDE:

                                materialinfoContainer.startAnimation(  false, obj);


                                break;

                        }
                    }
                });
                break;
*/

            case LIST:


               super.performBind(obj);
                break;



        }





    }

    public static CustomMaterialCheckableListViewHolder create(ViewGroup parent, ViewMode viewMode) {
        ViewDataBinding viewDataBinding;

        switch (viewMode) {


            case LIST:

                viewDataBinding = CustomMaterialListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
                return new CustomMaterialCheckableListViewHolder(viewDataBinding,viewMode);


            case GRID:

                viewDataBinding = CustomMaterialGridBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
                return new CustomMaterialCheckableListViewHolder(viewDataBinding,viewMode);

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







