package com.colorbuzztechgmail.spf;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

 import com.colorbuzztechgmail.spf.databinding.MaterialGridBinding;
import com.colorbuzztechgmail.spf.databinding.MaterialInfoBinding;

import com.wajahatkarim3.easyflipview.EasyFlipView;

public class MaterialCheckableListViewHolder extends ViewHolderMultiChoice{


    public TextView titleText;
    public ImageView image;
    public Toolbar toolbar;
    private View frontView,backView;
    public EasyFlipView flipView;




    public MaterialCheckableListViewHolder(View v) {
        super(v);
        toolbar   = (Toolbar) v.findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.submenu_material_option);
        frontView=v.findViewById(R.id.frontView);
        backView=v.findViewById(R.id.infoContainer);
        flipView=v.findViewById(R.id.flipView);
        image=v.findViewById(R.id.image2);
        v.setOnClickListener(this);
        v.setOnLongClickListener(this);
        setCheckableContainer(v.findViewById(R.id.checkLayout));


    }

    @Override
    public void performBind(final Object obj) {
        super.performBind(obj);


        final MaterialGridBinding MaterialBinding= DataBindingUtil.bind(frontView);

        MaterialBinding.setVariable(BR.obj,obj);
        MaterialBinding.executePendingBindings();

        MaterialBinding.notifyChange();
       final  MaterialinfoContainer materialinfoContainer= new MaterialinfoContainer(itemView.getContext(),MaterialinfoContainer.MODE_SUBMENU_MATERIAL);
        materialinfoContainer.setView(backView);

        flipView.setOnFlipListener(new EasyFlipView.OnFlipAnimationListener() {
            @Override
            public void onViewFlipCompleted(EasyFlipView easyFlipView, EasyFlipView.FlipState newCurrentSide) {

                switch (newCurrentSide){

                    case BACK_SIDE:




                        materialinfoContainer.startAnimation(MaterialinfoContainer.MODE_SUBMENU_MATERIAL,true,(Material)obj,null);

                        break;

                    case FRONT_SIDE:

                        materialinfoContainer.startAnimation(MaterialinfoContainer.MODE_SUBMENU_MATERIAL,false,(Material)obj,null);


                        break;

                }

            }
        });
     /*   final MaterialInfoBinding materialInfoBinding=DataBindingUtil.bind(backView);


        materialInfoBinding.setVariable(BR.obj,obj);
        materialInfoBinding.executePendingBindings();

        materialInfoBinding.notifyChange();*/

    }

    public static MaterialCheckableListViewHolder create(ViewGroup parent, Utils.ViewMode viewMode) {

        ViewDataBinding viewDataBinding;
        switch (viewMode) {


            case LIST:

                View view1=  LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.material_flippable_item, parent, false);
                return new  MaterialCheckableListViewHolder (view1);
            case GRID:
                View view=  LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.material_flippable_item, parent, false);
                return new  MaterialCheckableListViewHolder (view);

            default:

                return null;
        }

    }




    public void setPreview(View header, Material Material) {

        if (Material.getImage() != null) {
            image.setImageDrawable(Material.getImage());

        }else{

            image.setImageDrawable(header.getContext().getDrawable(R.drawable.ic_info_grey_24dp));

        }


    }
}






