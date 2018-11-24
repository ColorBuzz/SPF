package com.colorbuzztechgmail.spf;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

public abstract class MyBaseAdapter
        extends RecyclerView.Adapter<ViewHolderMultiChoice> {

   public Dataset dataset;
   public MultiChoiceHelper multiChoiceHelper;

   public void setMultiChoiceHelper(MultiChoiceHelper multiChoiceHelper){

       this.multiChoiceHelper=multiChoiceHelper;
   }

    public MyBaseAdapter(Dataset dataset) {
        super();

        this.dataset=dataset;
    }

    public ViewHolderMultiChoice onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        LayoutInflater layoutInflater =
                LayoutInflater.from(parent.getContext());
        ViewDataBinding binding = DataBindingUtil.inflate(
                layoutInflater, viewType, parent, false);
        return new ViewHolderMultiChoice(binding, 0);
    }

    public void onBindViewHolder(ViewHolderMultiChoice1 holder,
                                 int position) {



    }

    @Override
    public int getItemViewType(int position) {
        return getLayoutIdForPosition(position);
    }

    protected abstract Object getObjForPosition(int position);

    protected abstract int getLayoutIdForPosition(int position);

}