package com.colorbuzztechgmail.spf;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.colorbuzztechgmail.spf.adapter.BaseAdapter;

public class PieceListAdapter1 extends BaseAdapter {


    public PieceListAdapter1(@Nullable ItemActionListener itemClickListener, AppCompatActivity ma, boolean enableMultiChoice) {
        super(itemClickListener, ma, enableMultiChoice);
    }


    @Override
    protected ViewHolderMultiChoice getViewHolder(ViewGroup parent, int viewType) {
        return PieceCheckableListViewHolder.create(parent,getViewMode());

    }

    @Override
    protected void bindItem(final ViewHolderMultiChoice holder,final int position) {


        if(getViewMode().equals(Utils.ViewMode.GRID))
            resizeView(((PieceCheckableListViewHolder)holder).image);

        final Piece piece =(Piece) dataset.getObject(position);
        ((PieceCheckableListViewHolder) holder).performBindPiece(piece);

        holder.bind(multiChoiceHelper,piece.getId());


        holder.setItemClickListener(new ItemClickListener() {

            @Override
            public void onClick(View v, int position, boolean isLongClick) {

                if (isLongClick) {

                    if (!multiChoiceHelper.selectionMode && enableMultiChoice) {

                        if (position != RecyclerView.NO_POSITION) {
                            multiChoiceHelper.setItemChecked(piece.getId(), position, true, true);
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

                        multiChoiceHelper.toggleItemChecked(piece.getId(), position, true);


                    }

                }

            }
        });




        ((PieceCheckableListViewHolder) holder).toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()){


                    case R.id.action_pieceList:

                        Utils.showPieceList(ma, piece);


                        return true;

                    case R.id.action_editPiece:
                        if(actionListener!=null){

                            final long[] id=new long[1];
                            id[0]=piece.getId();
                            actionListener.toEdit(id);

                        }

                        return true;


                    case R.id.action_preview:

                        Utils.showPreview(ma,piece);

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
                        multiChoiceHelper.setItemChecked(((Piece)dataset.getObject(i)).getId(),i,enabled,true);

                    }
                }
            }
        }

    }

}
