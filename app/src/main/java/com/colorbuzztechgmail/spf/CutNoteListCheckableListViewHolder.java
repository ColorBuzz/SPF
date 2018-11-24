package com.colorbuzztechgmail.spf;

import android.databinding.ViewDataBinding;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.colorbuzztechgmail.spf.databinding.CutNoteGridBinding;
import com.colorbuzztechgmail.spf.databinding.CutNoteListBinding;

public class CutNoteListCheckableListViewHolder extends ViewHolderMultiChoice{


    public TextView titleText;
    public ImageView image;
    public Toolbar toolbar;


    public CutNoteListCheckableListViewHolder(ViewDataBinding binding) {

        super(binding,0);

        setCheckableContainer(binding.getRoot().findViewById(R.id.checkLayout));
        image=(ImageView)binding.getRoot().findViewById(R.id.imageViewProfile);
        toolbar   = (Toolbar) binding.getRoot().findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_pewview_model);

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(itemClickListener!=null){
                    itemClickListener.onClick(view,getAdapterPosition(),false);
                }
            }
        });


    }

    @Override
    public void performBind(Object obj) {
    setStatus((CutNote) obj);
       super.performBind(obj);



    }



    public static CutNoteListCheckableListViewHolder create(ViewGroup parent, Utils.ViewMode viewMode) {

        ViewDataBinding viewDataBinding;
        switch (viewMode){


            case LIST:

                  viewDataBinding = CutNoteListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

                return new CutNoteListCheckableListViewHolder(viewDataBinding);

            case GRID:
                viewDataBinding = CutNoteGridBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

                return new CutNoteListCheckableListViewHolder(viewDataBinding);


        }

       return null;

    }




    public void setStatus(CutNote  cutNote ) {

        image.setImageDrawable(Utils.getImageAtStatus(getBinding().getRoot().getContext(),cutNote.getStatus()));



    }

    public static Drawable geDrawable(){

        Drawable drawable=null;





        return drawable;


    }
}






