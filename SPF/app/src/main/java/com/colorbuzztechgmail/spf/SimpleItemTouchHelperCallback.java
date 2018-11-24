package com.colorbuzztechgmail.spf;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.LinearLayout;

/**
 * Created by PC01 on 29/04/2018.
 */

public class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback {

    boolean mOrderChanged=false;
     int finalPos=0;
     int startPos=0;
     int layout_orientation;
    int dragFlags;
    int swipeFlags;
    private final ItemTouchHelperAdapter mAdapter;

    public SimpleItemTouchHelperCallback(ItemTouchHelperAdapter adapter, int orientation) {
        mAdapter = adapter;
        this.layout_orientation=orientation;
         setItemDirection(orientation);
    }

    private void setItemDirection(int orientation){

        if(orientation== LinearLayout.HORIZONTAL){


            dragFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            swipeFlags = ItemTouchHelper.UP| ItemTouchHelper.DOWN;

        }else{


            dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;


        }


    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }



    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;
    }


    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {


        if (actionState == ItemTouchHelper.ACTION_STATE_IDLE && mOrderChanged) {


                mAdapter.onRefreshItem(startPos);
                mOrderChanged = false;
        }


    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {


        if(mAdapter.onMoveState(viewHolder.getAdapterPosition())) {

            return makeMovementFlags(dragFlags, swipeFlags);
        }
        return makeFlag(ItemTouchHelper.ACTION_STATE_IDLE,ItemTouchHelper.ACTION_STATE_IDLE);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                          RecyclerView.ViewHolder target) {

        if(target.getAdapterPosition()>  0 &&( viewHolder.getItemViewType()==RecyclerListAdapter.PIECE)){

            mAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
            mOrderChanged=true;
            finalPos=target.getAdapterPosition();
            startPos=viewHolder.getAdapterPosition();
            return true;
        }
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        mAdapter.onItemDismiss(viewHolder.getAdapterPosition());
    }

}