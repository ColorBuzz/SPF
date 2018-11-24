package com.colorbuzztechgmail.spf;

import android.graphics.Color;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.TextView;

import com.colorbuzztechgmail.spf.adapter.BaseAdapter;

import java.util.ArrayList;

public class ModeListAdapter extends BaseAdapter{

    public ModeListAdapter(@Nullable ItemActionListener itemClickListener, AppCompatActivity ma, boolean enableMultiChoice) {
        super(itemClickListener, ma, enableMultiChoice);
    }

    @Override
    protected ViewHolderMultiChoice getViewHolder(ViewGroup parent, int viewType) {


        return ModelCheckableListViewHolder.create(parent,getViewMode());
    }

    @Override
    protected void bindItem(final ViewHolderMultiChoice holder,final int position) {


        if(getViewMode().equals(Utils.ViewMode.GRID))
         resizeView(((ModelCheckableListViewHolder)holder).image);

        final PreviewModelInfo model =(PreviewModelInfo) dataset.getObject(position);
        ((ModelCheckableListViewHolder) holder).performBindModel(model);

        holder.bind(multiChoiceHelper, model.getId());


        holder.setItemClickListener(new ItemClickListener() {

            @Override
            public void onClick(View v, int position, boolean isLongClick) {

                if (isLongClick) {

                    if (!multiChoiceHelper.selectionMode && enableMultiChoice) {

                        if (position != RecyclerView.NO_POSITION) {
                            multiChoiceHelper.setItemChecked(model.getId(), position, true, true);
                            ///holder.updateCheckedState(model.getId());
                            multiChoiceHelper.EnableSelectionMode(true);
                        }
                    }
                } else {


                    if (multiChoiceHelper.getCheckedItemCount() == 0) {

                        if (actionListener != null) {

                            actionListener.onClick(v, position, false);

                        }
                    } else if (multiChoiceHelper.selectionMode) {

                        multiChoiceHelper.toggleItemChecked(model.getId(), position, true);


                    }

                }

            }
        });




        ((ModelCheckableListViewHolder) holder).toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()){


                    case R.id.action_info:

                        if(actionListener!=null){

                            actionListener.onPreview(model);

                        }

                        return true;

                    case R.id.action_edit:


                        if(actionListener!=null){

                            final long[] id=new long[1];
                            id[0]=model.getId();
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





    @Override
    protected void selectAll(boolean enabled) {
        if(multiChoiceHelper!=null){
            if (dataset.size() > 0) {
                for(int i=0;i<dataset.size();i++) {

                    if( !isHeader(i)){
                        multiChoiceHelper.setItemChecked(((PreviewModelInfo)dataset.getObject(i)).getId(),i,enabled,true);

                    }
                }
            }
        }

    }
}
