package com.colorbuzztechgmail.spf;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.colorbuzztechgmail.spf.adapter.BaseAdapter;
import com.colorbuzztechgmail.spf.dataset.Dataset;

public class CutNoteAdapter extends BaseAdapter {


    private int itemClick;



    public CutNoteAdapter(@Nullable ItemActionListener itemClickListener, AppCompatActivity ma, boolean enableMultiChoice) {
        super(itemClickListener, ma, enableMultiChoice);
    }

    @Override
    public void dataset(Dataset dataset) {
        super.dataset(dataset);
    }

    public CutNoteDataset getDataset(){

        return (CutNoteDataset) dataset;

    }

    @Override
    protected ViewHolderMultiChoice getViewHolder(ViewGroup parent, int viewType) {



        return CutNoteCheckableListViewHolder.create(parent);
    }

    @Override
    protected void bindItem(final ViewHolderMultiChoice holder,final int position) {

        if(!(dataset.getObject(position) instanceof CustomHeader)){

            final CutNote cutNote = getDataset().getCutNote(position);
            holder.performBind(cutNote);

            holder.bind(multiChoiceHelper, cutNote.getId());


            ((CutNoteCheckableListViewHolder)holder).setItemClickListener(new ItemClickListener() {
                @Override
                public void onClick(View v, int position, boolean isLongClick) {
                    if(isLongClick){

                        if(!multiChoiceHelper.selectionMode) {

                            if (position != RecyclerView.NO_POSITION) {
                                multiChoiceHelper.setItemChecked(cutNote.getId(),position, true, true);
                                holder.updateCheckedState(cutNote.getId());
                                multiChoiceHelper.EnableSelectionMode(true);
                            }
                        }

                    }else{



                        if (multiChoiceHelper.getCheckedItemCount()==0) {

                            if (actionListener != null) {

                                actionListener.onClick(v,position,false);


                            }

                        }else if(multiChoiceHelper.selectionMode){

                            multiChoiceHelper.toggleItemChecked(cutNote.getId(),position, true);




                        }

                    }

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
                        multiChoiceHelper.setItemChecked(getDataset().getCutNote(i).getId(),i,enabled,true);

                    }
                }
            }
        }
    }
}