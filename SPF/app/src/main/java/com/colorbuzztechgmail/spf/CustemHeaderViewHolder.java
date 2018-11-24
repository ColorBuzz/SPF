package com.colorbuzztechgmail.spf;

import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

 import com.colorbuzztechgmail.spf.databinding.HeaderBinding;

public class CustemHeaderViewHolder extends ViewHolderMultiChoice implements View.OnClickListener,View.OnLongClickListener {



    public TextView titleText;
    public ImageView image;
    private ItemClickListener itemClickListener;

    public CustemHeaderViewHolder(ViewDataBinding binding) {
        super(binding,0);

        setCheckableContainer(binding.getRoot().findViewById(R.id.titleCheckLayout));
    }



    public static CustemHeaderViewHolder create(ViewGroup parent) {



        ViewDataBinding viewDataBinding=null;


                viewDataBinding = HeaderBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);





        return new CustemHeaderViewHolder(viewDataBinding);



    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void performBindTitle(Object obj) {





                this.mBinding.setVariable(BR.obj,obj);

                this.mBinding.executePendingBindings();

                setHeaderDataStyle(mBinding.getRoot(),obj);







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

    public void setHeaderDataStyle(View header, Object obj) {



    }

}






