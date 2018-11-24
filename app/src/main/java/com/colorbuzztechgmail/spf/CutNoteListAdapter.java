package com.colorbuzztechgmail.spf;


import android.graphics.Color;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.TextView;

import com.colorbuzztechgmail.spf.adapter.BaseAdapter;
import com.colorbuzztechgmail.spf.dataset.Dataset;

import java.util.ArrayList;

public class CutNoteListAdapter extends BaseAdapter{


    private int itemClick;



    public CutNoteListAdapter(@Nullable ItemActionListener itemClickListener, AppCompatActivity ma, boolean enableMultiChoice) {
        super(itemClickListener, ma, enableMultiChoice);
    }

    @Override
    public void dataset(Dataset dataset) {
        super.dataset(dataset);
    }

    public CutNoteListDataset getDataset(){

        return (CutNoteListDataset) dataset;

    }

    @Override
    protected ViewHolderMultiChoice getViewHolder(ViewGroup parent, int viewType) {
        return CutNoteListCheckableListViewHolder.create(parent,getViewMode());

    }

    @Override
    protected void bindItem(final ViewHolderMultiChoice holder,final int position) {

      if(!(dataset.getObject(position) instanceof CustomHeader)){

        final CutNoteList cutNoteList = getDataset().getCutNoteList(position);

        holder.performBind(cutNoteList);

        holder.bind(multiChoiceHelper, cutNoteList.getId());


          holder.setItemClickListener(new ItemClickListener() {

              @Override
              public void onClick(View v, int position, boolean isLongClick) {

                  if (isLongClick) {

                      if (!multiChoiceHelper.selectionMode && enableMultiChoice) {

                          if (position != RecyclerView.NO_POSITION) {
                              multiChoiceHelper.setItemChecked(cutNoteList.getId(), position, true, true);
                              holder.updateCheckedState(cutNoteList.getId());
                              multiChoiceHelper.EnableSelectionMode(true);
                          }
                      }
                  } else {


                      if (multiChoiceHelper.getCheckedItemCount() == 0) {

                          if (actionListener != null) {

                              actionListener.onPreview(cutNoteList.getId());

                          }
                      } else if (multiChoiceHelper.selectionMode) {

                          multiChoiceHelper.toggleItemChecked(cutNoteList.getId(), position, true);


                      }

                  }

              }
          });

          ((CutNoteListCheckableListViewHolder) holder).toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
              @Override
              public boolean onMenuItemClick(MenuItem item) {

                  switch (item.getItemId()){


                      case R.id.action_info:



                          return true;

                      case R.id.action_edit:


                          if(actionListener!=null){

                              final long[] id=new long[1];
                              id[0]=cutNoteList.getId();
                              actionListener.toEdit(id);

                          }

                          return true;


                      case R.id.action_send:


                          return true;


                  }

                  return false;
              }
          });

      }


}


    @Override
    protected void selectAll(boolean enabled) {


        if(multiChoiceHelper!=null){
            if (dataset.size() > 0) {
                for(int i=0;i<dataset.size();i++) {

                    if( !isHeader(i)){
                        multiChoiceHelper.setItemChecked(getDataset().getCutNoteList(i).getId(),i,enabled,true);

                    }
                }
            }
        }
    }
}