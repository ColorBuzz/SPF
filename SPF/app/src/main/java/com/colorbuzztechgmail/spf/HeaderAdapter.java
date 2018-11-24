package com.colorbuzztechgmail.spf;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.colorbuzztechgmail.spf.databinding.ItemsBinding;
import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;

import java.util.Comparator;
import java.util.List;

/**
 * Created by PC01 on 25/11/2017.
 */

public class HeaderAdapter extends RecyclerView.Adapter<HeaderAdapter.ViewHolder> {


    private Context context;
    private LayoutInflater inflater;
    private final Comparator<FileItem> mComparator;
    private SearchFragment searchFragment;
    public int checkedViewCount=0;
    // Provide a suitable constructor (depends on the kind of dataset)
    public HeaderAdapter( Context context,Comparator mComparator,SearchFragment mSearchFragment) {
        this.context = context;
        this.mComparator=mComparator;
        this.searchFragment=mSearchFragment;

    }



    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public HeaderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        //create binding
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }


             ItemsBinding itemsBinding = ItemsBinding.inflate(inflater, parent, false);




        return     new ViewHolder(itemsBinding);




    }



    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element


        final FileItem file;
        file = mSortedList.get(position);
        holder.performBind(file);
        if(searchFragment.ViewMode==0){
            holder.getBinding().getRoot().setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }else{

            holder.getBinding().getRoot().setLayoutParams(new LinearLayout.LayoutParams((int)searchFragment.getResources().getDimension(R.dimen.searchItem_Width), ViewGroup.LayoutParams.WRAP_CONTENT));

        }
        if (!mSortedList.get(position).isCheck()){

            holder.checkBox.setChecked(false);

        }else if(mSortedList.get(position).isCheck()){

            holder.checkBox.setChecked(true);

        }


        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View v, int position, boolean isLongClick) {
                if (isLongClick) {
                    //Utils.toast(context, "LONGCLICK" + String.valueOf(position));
                } else {

                   /* Drawable mdrawable;

                    mdrawable = holder.imageView.getDrawable();*/
                   // Utils.toast(context, "SHORTCLICK" + String.valueOf(position));
                    if (!holder.getBinding().getFileName().isCheck()){


                       // animated(mdrawable,true);

                        mSortedList.get(position).setCheck(true);
                        holder.checkBox.setChecked(true);
                        holder.checkBox.setSelected(true);
                        checkedViewCount=checkedViewCount+1;

                    }else if(holder.getBinding().getFileName().isCheck()){
                      //  animated(mdrawable,false);

                        mSortedList.get(position).setCheck(false);
                        holder.checkBox.setChecked(false);
                        holder.checkBox.setSelected(false);

                        checkedViewCount=checkedViewCount-1;


                    }

                searchFragment.updateBar(checkedViewCount,null);

                }
            }
        });


    }


    public void animated(Drawable drawable,boolean enable) {

        Drawable mdrawable;

        mdrawable = drawable;

        if (enable){
            if (mdrawable instanceof Animatable) {

                ((Animatable) mdrawable).start();


            }
        }else{

            if (mdrawable instanceof Animatable) {

                ((Animatable) mdrawable).stop();


            }

        }


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
         return mSortedList.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    public static class ViewHolder extends SortedListAdapter.ViewHolder<FileItem> implements View.OnClickListener, View.OnLongClickListener {
        // each data item is just a string in this case

        ItemsBinding mBinding;
        CheckBox checkBox;
        ImageView imageView;
        private ItemClickListener itemClickListener;

        public ViewHolder(ItemsBinding itemsBinding) {
            super(itemsBinding.getRoot());
            this.mBinding = itemsBinding;

            checkBox=mBinding.checkBox4;
           imageView=mBinding.animateBack;
            itemsBinding.getRoot().setOnLongClickListener(this);
           // checkBox.setOnLongClickListener(this);


            itemsBinding.getRoot().setOnClickListener(this);
            checkBox.setOnClickListener(this);

        }

        public void performBind(FileItem File) {

            this.mBinding.setFileName(File);
        }

        public ItemsBinding getBinding() {

            return mBinding;
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {

            this.itemClickListener = itemClickListener;

        }


        @Override
        public void onClick(View v) {

            itemClickListener.onClick(v, getAdapterPosition(), false);
        }

        @Override
        public boolean onLongClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition(), true);
            return false;
        }
    }

//////////////////////////////////////////////////

    private final SortedList.Callback<FileItem> mCallback = new SortedList.Callback<FileItem>() {

        @Override
        public void onInserted(int position, int count) {
           notifyItemRangeInserted(position, count);
        }

        @Override
        public void onRemoved(int position, int count) {
            notifyItemRangeRemoved(position, count);
        }

        @Override
        public void onMoved(int fromPosition, int toPosition) {
            notifyItemMoved(fromPosition, toPosition);
        }

        @Override
        public void onChanged(int position, int count) {
             notifyItemRangeChanged(position, count);
        }

        @Override
        public int compare(FileItem a, FileItem b) {
            return mComparator.compare(a, b);
        }

        @Override
        public boolean areContentsTheSame(FileItem oldItem, FileItem newItem) {
            return oldItem.equals(newItem);
        }

        @Override
        public boolean areItemsTheSame(FileItem item1, FileItem item2) {
            return item1.getName() == item2.getName();
        }
    };



    final SortedList<FileItem> mSortedList = new SortedList<>(FileItem.class, mCallback);

    public void add(FileItem model) {
        mSortedList.add(model);
    }

    public void remove(FileItem model) {
        mSortedList.remove(model);
    }

    public void add(List<FileItem> models) {
        mSortedList.addAll(models);
    }

    public void remove(List<FileItem> models) {
        mSortedList.beginBatchedUpdates();
        for (FileItem model : models) {
            mSortedList.remove(model);
        }
        mSortedList.endBatchedUpdates();
    }

    public void order(){


    }

    public void replaceAll(List<FileItem> models) {
        mSortedList.beginBatchedUpdates();
        for (int i = mSortedList.size() - 1; i >= 0; i--) {
            final FileItem model = mSortedList.get(i);
            if (!models.contains(model)) {
                mSortedList.remove(model);
            }
        }
        mSortedList.addAll(models);
        mSortedList.endBatchedUpdates();
    }


}




