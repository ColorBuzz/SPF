package com.colorbuzztechgmail.spf;

import android.content.Context;
import android.content.DialogInterface;
import android.databinding.ViewDataBinding;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.colorbuzztechgmail.spf.databinding.PieceCardBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by PC01 on 25/11/2017.
 */

public class PieceCardAdapter extends RecyclerView.Adapter<PieceCardAdapter.ViewHolder> implements PopupMenu.OnMenuItemClickListener  {

    private final Comparator<Piece> mComparator;

    SparseBooleanArray expandState;
     private Context context;
    private LayoutInflater inflater;
    private RecyclerView mRecyclerView;
    private boolean  refreshInitialPos=false;
    boolean titleClick=false;
    TabFragment1 tabFragment1;
    private List<Piece> pieceList;

    public int viewMode;


    public void add(Piece piece) {
        pieceList.add(piece);

        expandState.append(piece.getId(),false);

        notifyItemInserted(pieceList.size()-1);
    }

    public void add(List<Piece> pieces) {
        for(int i=0;i<pieces.size();i++){
            expandState.append(pieces.get(i).getId(),false);


        }
        pieceList.addAll (pieces);

        notifyItemRangeInserted(pieceList.size()-1,pieces.size());

    }

    public Piece getPiece(int position){

        if(position<pieceList.size()){

            return pieceList.get(position);
        }

        return null;

    }

    public void remove(List<Piece> pieces) {

        for (int i=pieceList.size()-1;i>=0;i--) {

            for (int j=0;j<pieces.size();j++){

                if(pieceList.get(i).equals(pieces.get(j))){
                    expandState.delete(pieces.get(j).getId());
                    pieceList.remove(pieces.get(j));
                    break;
                }

            }
        }

       notifyAll();



    }

    public void remove(Integer position) {

        expandState.delete(pieceList.get(position).getId());

        pieceList.remove(position);

        notifyItemRemoved(position);



    }

    public void remove(ArrayList<Integer> position) {

        Collections.sort(position);


        for(int i=position.size()-1;i>=0;i--) {
            expandState.delete(pieceList.get(position.get(i)).getId());

            pieceList.remove(position.get(i));
        }

notifyAll();


    }

    public void remove(Piece piece){
        pieceList.remove(piece);

        expandState.delete(piece.getId());

notifyAll();


    }

    public void removeAll(){

        pieceList.clear();
        expandState.clear();

        notifyItemRangeRemoved(0,pieceList.size());


    }

    public void replaceAll(List<Piece> pieces) {

        expandState.clear();
        for (int i =pieceList.size() - 1; i >= 0; i--) {
            final Piece piece =pieceList.get(i);
            if (!pieces.contains(piece)) {
                pieceList.remove(piece);
            }
        }
        pieceList.addAll(pieces);

        for(int i=0;i<pieces.size();i++){
            expandState.append(pieces.get(i).getId(),false);


        }

        notifyAll();

    }

    public void replaceItem(Piece piece) {

        int index=-1;

        for (int i =pieceList.size() - 1; i >= 0; i--) {
            final Piece auxpiece =pieceList.get (i);
            if (piece.getId()==(auxpiece.getId())) {
                index=i;
                break;
            }
        }

        if (index > -1) {
            pieceList.remove(index);

            pieceList.add(index,piece);

        }else{

            index=0;
            pieceList.add(0,piece);


        }


        notifyItemChanged(index);


    }



    // Provide a suitable constructor (depends on the kind of dataset)
    public PieceCardAdapter(Context context, Comparator mComparator , TabFragment1 tab, RecyclerView recyclerView) {

        expandState= new SparseBooleanArray();
        this.context = context;
        this.mComparator=mComparator;
          this.mRecyclerView=recyclerView;
        this.tabFragment1=tab;
       this.pieceList=new ArrayList<>();

    }

    // Create new views (invoked by the layout manager)
    @Override
    public PieceCardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }

        ViewDataBinding pieceBinding=null;

        pieceBinding = PieceCardBinding.inflate(inflater, parent, false);

        return     new ViewHolder(pieceBinding);




    }

    @Override
    public void onViewAttachedToWindow(ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if (getBiggerView() == 0 && (refreshInitialPos == false)) {
            ((CardSliderLayoutManager) mRecyclerView.getLayoutManager()).updateBiggerView(holder.itemView);
            refreshInitialPos = true;
        } else if (getBiggerView() > 0 && (refreshInitialPos == true)) {


            refreshInitialPos = false;
        }

}

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element


        final Piece piece ;
        piece = pieceList.get(position);

       holder.performBind(piece );





        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View v, int position, boolean isLongClick) {
                if (isLongClick) {
                    // Utils.toast(context, "LONGCLICK" + String.valueOf(position));
                    switch (v.getId()) {

                    }

                } else {

                    if(getBiggerView()==position) {





                    }


                }

            }

        });


    }

    public void showPopup(View v,int position) {

        if(((CardSliderLayoutManager)mRecyclerView.getLayoutManager()).getBiggerPos()==position){

            PopupMenu popup = new PopupMenu(context, v);
            MenuInflater inflater = popup.getMenuInflater();
            popup.setOnMenuItemClickListener(this);
            //menuClick=position;
            inflater.inflate(R.menu.submenu_piece_option, popup.getMenu());
            popup.show();

        }

    }
    public void showRemoveDialog(final int posotion){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                tabFragment1.getContext());





                alertDialogBuilder
                    .setTitle(tabFragment1.getResources().getString(R.string.dialogTitle_remove))
                    .setMessage(tabFragment1.getResources().getString(R.string.dialogMsg_remove))
                    .setCancelable(false)
                    .setPositiveButton(tabFragment1.getResources().getString(R.string.dialog_remove),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    final Piece piece=pieceList.get(((CardSliderLayoutManager)mRecyclerView.getLayoutManager()).getBiggerPos());

                                    remove(posotion);


                                    SectionPageAdapter adapter= (SectionPageAdapter) ((ModelActivity)tabFragment1.getActivity()).sViewpager.getAdapter();

                                     dialog.dismiss();
                                    showUndoSnackbar(piece,posotion);

                                }
                            }
                    )
                    .setNegativeButton(tabFragment1.getResources().getString(R.string.dialog_cancel),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    dialog.cancel();
                                }
                            }
                    );




        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bg_black_line));

        alertDialog.show();
    }
    private void showUndoSnackbar(final Piece piece, final int position){

        CoordinatorLayout view = (CoordinatorLayout) tabFragment1.getActivity().findViewById(R.id.parentLayout);

        String mssg;



            mssg =piece.getName() + " eliminada";



        Snackbar snackbar = Snackbar.make(view, mssg, Snackbar.LENGTH_SHORT)
                .setAction(context.getString(R.string.action_undo), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        pieceList.add(piece);
                        mRecyclerView.smoothScrollToPosition(position-1);

                        SectionPageAdapter adapter= (SectionPageAdapter) ((ModelActivity)tabFragment1.getActivity()).sViewpager.getAdapter();

                        ((TabFragment2) adapter.getItem(1)).loadMaterial();
                     }

                }).setCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar snackbar, int dismissType) {
                        super.onDismissed(snackbar, dismissType);

                        if(dismissType !=DISMISS_EVENT_ACTION){

                                ModelDataBase db= new ModelDataBase(context);
                                db.deletePiece(piece);

                        }

                    }
                });


        snackbar.show();
    }


    public Integer getBiggerView(){





        return (((CardSliderLayoutManager) mRecyclerView.getLayoutManager()).getBiggerPos());
    }

    public RecyclerView.LayoutManager getLayoutManager(){


        return (mRecyclerView.getLayoutManager());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return pieceList.size();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {


        final int id=pieceList.get(((CardSliderLayoutManager)mRecyclerView.getLayoutManager()).getBiggerPos()).getId();
        final int modelId=pieceList.get(((CardSliderLayoutManager)mRecyclerView.getLayoutManager()).getBiggerPos()).getModelId();
        final String name=pieceList.get(((CardSliderLayoutManager)mRecyclerView.getLayoutManager()).getBiggerPos()).getName();
        final String material=pieceList.get(((CardSliderLayoutManager)mRecyclerView.getLayoutManager()).getBiggerPos()).getMaterial();

        switch (item.getItemId()) {


            case R.id.action_editPiece:
                tabFragment1.showEditPiece(name,id,modelId);
                return true;
            case R.id.action_pieceList:



                tabFragment1.loadPieceList( String.valueOf(id),PieceListAdapter.MODE_SIZE,modelId,null);

                return true;
            case R.id.action_delete:

                showRemoveDialog(getBiggerView());
                return true;

            default:
                return false;
        }
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder



    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        // each data item is just a string in this case

        private ViewDataBinding mBinding;
        private ItemClickListener itemClickListener;
        private TextView pieceText;
        private ImageView previewImage;

        public ViewHolder(ViewDataBinding itemsBinding) {
            super(itemsBinding.getRoot());
            {

                this.mBinding = itemsBinding;

                pieceText = (TextView) mBinding.getRoot().findViewById(R.id.text);
                previewImage = (ImageView) mBinding.getRoot().findViewById(R.id.image);
                pieceText.setOnClickListener(this);
                previewImage.setOnClickListener(this);
                previewImage.setOnLongClickListener(this);



            }
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {

            this.itemClickListener = itemClickListener;

        }

        public void performBind(Piece piece) {

            this.mBinding.setVariable(BR.obj,piece);

            this.mBinding.executePendingBindings();
            //this.mBinding.setModel(model_cardview);

        }

        public ViewDataBinding getBinding() {

            return mBinding;
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

   
}

//////////////////////////////////////////////////