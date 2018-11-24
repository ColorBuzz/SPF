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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.TextView;

import com.colorbuzztechgmail.spf.adapter.BaseAdapter;

public class RecentListAdapter extends BaseAdapter{




    public RecentListAdapter(@Nullable ItemActionListener itemClickListener, AppCompatActivity ma, boolean enableMultiChoice) {
        super(itemClickListener, ma, enableMultiChoice);
    }

    @Override
    protected ViewHolderMultiChoice getViewHolder(ViewGroup parent, int viewType) {

        switch (viewType){


            case  MODEL_ITEM:


                return ModelCheckableListViewHolder.create(parent,getViewMode());

            case  MATERIAL_ITEM:


                return CustomMaterialCheckableListViewHolder.create(parent,getViewMode());

            case  CUTNOTELIST_ITEM:


                return CutNoteListCheckableListViewHolder.create(parent,getViewMode());

            case CATEGORY:


                return RecentCheckableListViewHolder.create(parent,getViewMode());


        }
        return null;

    }

    @Override
    protected void bindItem(final ViewHolderMultiChoice holder,final int position) {

        final Object obj=dataset.getObject(position);

       if(getViewMode().equals(Utils.ViewMode.GRID)) {
           if (obj instanceof PreviewModelInfo || obj instanceof CustomMaterial)
               resizeView((holder.getBinding().getRoot().findViewById(R.id.image)));
           if (obj instanceof Category)
               holder.getBinding().getRoot().findViewById(R.id.image).setVisibility(View.GONE);
       }

        holder.performBind(obj);

       if(enableMultiChoice)
        holder.bind(multiChoiceHelper, obj.hashCode());


        holder.setItemClickListener(new ItemClickListener() {

            @Override
            public void onClick(View v, int position, boolean isLongClick) {

                if (isLongClick) {

                    if (!multiChoiceHelper.selectionMode && enableMultiChoice) {

                        if (position != RecyclerView.NO_POSITION) {
                            multiChoiceHelper.setItemChecked(obj.hashCode(), position, true, true);
                            holder.updateCheckedState(obj.hashCode());
                            multiChoiceHelper.EnableSelectionMode(true);
                        }
                    }
                } else {


                    if (multiChoiceHelper.getCheckedItemCount() == 0) {

                        if (actionListener != null) {

                            actionListener.onPreview(obj);




                        }
                    } else if (multiChoiceHelper.selectionMode) {

                        multiChoiceHelper.toggleItemChecked(obj.hashCode(), position, true);


                    }

                }

            }
        });


        ((Toolbar)holder.getBinding().getRoot().findViewById(R.id.toolbar)).getMenu().findItem(R.id.action_edit).setVisible(false);

        ((Toolbar)holder.getBinding().getRoot().findViewById(R.id.toolbar)).setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()){


                    case R.id.action_info:

                        if(actionListener!=null){

                            actionListener.onPreview(obj);

                        }

                        return true;

                    case R.id.action_edit:


                        if(actionListener!=null){

                            //actionListener.toEdit(obj);

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
                       // multiChoiceHelper.setItemChecked(((PreviewModelInfo)dataset.getObject(i)).getId(),i,enabled,true);

                    }
                }
            }
        }

    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {

        menu.findItem(R.id.action_edit).setVisible(false);
        return super.onPrepareActionMode(mode, menu);
    }
}
