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

import com.colorbuzztechgmail.spf.Utils.ViewMode;
import com.colorbuzztechgmail.spf.dataset.Dataset;
import com.colorbuzztechgmail.spf.adapter.BaseAdapter;

public class CustomMaterialListAdapter extends BaseAdapter {

    private int itemClick;



    public CustomMaterialListAdapter(@Nullable ItemActionListener itemClickListener, AppCompatActivity ma, boolean enableMultiChoice) {
        super(itemClickListener, ma, enableMultiChoice);
    }

    @Override
    public void dataset(Dataset dataset) {
        super.dataset(dataset);
    }

    public CustomMaterialDataset getDataset(){

        return (CustomMaterialDataset) dataset;

    }

    @Override
    protected ViewHolderMultiChoice getViewHolder(ViewGroup parent, int viewType) {
        return  CustomMaterialCheckableListViewHolder.create(parent,getViewMode());

    }

    @Override
    protected void bindItem(final ViewHolderMultiChoice holder,final int position) {
        final CustomMaterial material = getDataset().getCustomMaterial(position);

        if ( getViewMode().equals(ViewMode.GRID))
            resizeView(((CustomMaterialCheckableListViewHolder)holder).image);

        if(!(dataset.getObject(position) instanceof CustomHeader)){
             holder.performBind(material);



                holder.bind(multiChoiceHelper, material.getId());

                holder.setItemClickListener(new ItemClickListener (){
                    @Override
                    public void onClick(View v, int position, boolean isLongClick) {

                        if (isLongClick) {
                            itemClick = position;

                            if (!multiChoiceHelper.selectionMode && enableMultiChoice) {


                                if (position != RecyclerView.NO_POSITION) {


                                    multiChoiceHelper.setItemChecked(material.getId(), position, true, true);
                                    holder.updateCheckedState(position);

                                    multiChoiceHelper.EnableSelectionMode(true);

                                }
                            } else {


                                multiChoiceHelper.toggleItemChecked(material.getId(), position, true);


                            }

                        } else{

                            itemClick=position;

                            if(holder.isMultiChoiceActive()){


                                multiChoiceHelper.toggleItemChecked(material.getId(),position, true);


                            }else{
                                if (actionListener != null) {

                                    actionListener.onPreview(material);
                               }
                            }


                        }
                    }
                });




            ((CustomMaterialCheckableListViewHolder) holder).toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    switch (item.getItemId()){


                        case R.id.action_info:

                            if(actionListener!=null){

                                actionListener.onPreview(material);

                            }

                            return true;

                        case R.id.action_edit:


                            if(actionListener!=null){

                                final long[] id=new long[1];
                                id[0]=material.getId();
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
    public void selectAll(boolean enabled){

        if(multiChoiceHelper!=null){
            if (dataset.size() > 0) {
                for(int i=0;i<dataset.size();i++) {

                    if( !isHeader(i)){
                        multiChoiceHelper.setItemChecked(getDataset().getCustomMaterial(i).getId(),i,enabled,true);

                    }
                }
            }
        }
    }
}