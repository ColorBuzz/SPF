package com.colorbuzztechgmail.spf;

import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.colorbuzztechgmail.spf.databinding.CutNoteListBinding;
import com.colorbuzztechgmail.spf.databinding.ModelCardBinding;

public class CutNoteCheckableListViewHolder extends ViewHolderMultiChoice{


    public TextView titleText;
    public ImageView image;


    public CutNoteCheckableListViewHolder(ViewDataBinding binding) {

        super(binding,0);

        setCheckableContainer(binding.getRoot().findViewById(R.id.checkLayout));
        image=(ImageView)binding.getRoot().findViewById(R.id.imageViewProfile);


    }

    public void performBindCutNote(CutNoteList cutNoteList) {

        this.mBinding.setVariable(BR.obj,cutNoteList);

        setStatus(cutNoteList);

        this.mBinding.executePendingBindings();


        //this.mBinding.setModel(model_cardview);

    }


    public static CutNoteCheckableListViewHolder create(ViewGroup parent) {


        ViewDataBinding viewDataBinding = CutNoteListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new CutNoteCheckableListViewHolder(viewDataBinding);

    }




    public void setStatus(CutNoteList cutNoteList) {

        int color=0;

        switch (cutNoteList.getStatus()){

            case INDETERMINATE:

                color=mBinding.getRoot().getContext().getResources().getColor(R.color.colorRed_A400);
                break;

            case IN_PROGRESS:
                color=mBinding.getRoot().getContext().getResources().getColor(R.color.colorAmber_A400);

                break;

            case FINISHED:
                color=mBinding.getRoot().getContext().getResources().getColor(R.color.colorGreen_A400);

                break;


        }

        image.setImageDrawable(new ShapeGenerator(this.mBinding.getRoot().getContext()).getDrawableShape(null, ShapeGenerator.MODE_CIRCLE, color, ShapeGenerator.SIZE_SMALL));


    }
}






