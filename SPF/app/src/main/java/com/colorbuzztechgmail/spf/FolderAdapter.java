package com.colorbuzztechgmail.spf;

import android.content.Context;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by PC01 on 25/11/2017.
 */

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.ViewHolder> {

    private final Comparator<Category> mComparator;

    private Context context;
    private LayoutInflater inflater;
    private SearchFragment searchFragment;

    public int viewMode;


    public void add(Category category) {
        folderList.add(category);

     }

    public void add(List<Category> categorys) {

        folderList.addAll(categorys);


    }

    public void remove(List<Category> categorys) {

        folderList.beginBatchedUpdates();
        for (int i=0;i<folderList.size();i++) {
            for (int j=0;j<categorys.size();j++){

                if(folderList.get(i).equals(categorys.get(j))){

                    folderList.remove(categorys.get(j));
                    break;
                }

            }
        }
        folderList.endBatchedUpdates();



    }

    public void remove(Integer position) {

        folderList.beginBatchedUpdates();

        folderList.removeItemAt(position);

        folderList.endBatchedUpdates();


    }

    public void remove(ArrayList<Integer> position) {

        Collections.sort(position);

        folderList.beginBatchedUpdates();

        for(int i=position.size()-1;i>=0;i--) {

            folderList.removeItemAt(position.get(i));
        }

        folderList.endBatchedUpdates();



    }

    public void remove(Category category){
        folderList.beginBatchedUpdates();
        folderList.remove(category);



        folderList.endBatchedUpdates();


    }
    public void removeAll(){

        folderList.beginBatchedUpdates();
        folderList.clear();
         folderList.endBatchedUpdates();


    }

    public void replaceAll(List<Category> categorys) {
        folderList.beginBatchedUpdates();

         for (int i =folderList.size() - 1; i >= 0; i--) {
            final Category category =folderList.get(i);
            if (!categorys.contains(category)) {
                folderList.remove(category);
            }
        }
        folderList.addAll(categorys);


        folderList.endBatchedUpdates();



    }
    public void replaceItem(Category category) {
        folderList.beginBatchedUpdates();

        int index=-1;

        for (int i =folderList.size() - 1; i >= 0; i--) {
            final Category auxcategory =folderList.get(i);
            if (category.getId()==(auxcategory.getId())) {
                index=i;
                break;
            }
        }

        if (index > -1) {

            folderList.updateItemAt(index,category);
        }


        folderList.endBatchedUpdates();



    }
     // Provide a suitable constructor (depends on the kind of dataset)
    public FolderAdapter(Context context, Comparator comparator, SearchFragment mSearchFragment) {
        this.context = context;
     this.mComparator=comparator;
         this.searchFragment=mSearchFragment;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public FolderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {

        View v=null;

       // Log.e("VIEWTYPE",String.valueOf(viewType));

        if(searchFragment.ViewMode==1){
              v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.folderitem, parent, false);

            // set the view's size, margins, paddings and layout parameters


        }else {
              v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.folderlistview, parent, false);
            // set the view's size, margins, paddings and layout parameters

        }
        return new ViewHolder(v);


    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element


        final Category folder;
        folder = folderList.get(position);

        holder.folderText.setText(folder.getName());
        if( holder.countText!=null){
            String aux=context.getResources().getString(R.string.checkedSinleFileText);
            if(folder.getId()>1){
                aux=context.getResources().getString(R.string.checkedFileText);

            }
            holder.countText.setText(String.valueOf(folder.getId()) + " " + aux);

        }
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View v, int position, boolean isLongClick) {
                if (isLongClick) {
                   // Utils.toast(context, "LONGCLICK" + String.valueOf(position));

                } else {

                   // Utils.toast(context, "SHORTCLICK" + String.valueOf(position));

                    searchFragment.OpenFolder(folder.getName());

                }
            }
        });


    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return folderList.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        // each data item is just a string in this case


        private ItemClickListener itemClickListener;
        private TextView folderText;
        private TextView countText;
        public ViewHolder(View v) {
            super(v);

            folderText = (TextView) v.findViewById(R.id.pathText);
            countText = (TextView) v.findViewById(R.id.countTxt);
            v.setOnLongClickListener(this);
            v.setOnClickListener(this);


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
private final SortedList.Callback<Category> mCallback = new SortedList.Callback<Category>() {

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
    public int compare(Category a, Category b) {

        return mComparator.compare(a, b);
    }

    @Override
    public boolean areContentsTheSame(Category oldItem, Category newItem) {
        return oldItem.equals(newItem);
    }

    @Override
    public boolean areItemsTheSame(Category item1, Category item2) {
        return item1.getName() == item2.getName();
    }
};

    final SortedList<Category> folderList = new SortedList<>(Category.class, mCallback);

}


