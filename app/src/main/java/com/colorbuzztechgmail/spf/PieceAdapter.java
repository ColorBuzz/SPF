/*
package com.colorbuzztechgmail.spf;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.util.SortedList;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.colorbuzztechgmail.spf.databinding.PieceInfodBinding;
import com.wajahatkarim3.easyflipview.EasyFlipView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

*/
/**
 * Created by PC01 on 25/11/2017.
 *//*


public class PieceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context context;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private ItemActionListener clickListener;




    private SparseBooleanArray expandState;

      private int mode=0;


    public void setClickListener(ItemActionListener clickListener) {
        this.clickListener = clickListener;
    }

    private int clickView=0;
    // Provide a suitable constructor (depends on the kind of dataset)
    public PieceAdapter(Context context, @Nullable ItemActionListener itemActionListener) {
        this.context = context;
        this.clickListener=itemActionListener;
        expandState=new SparseBooleanArray();

    }

    public void add(Piece piece) {
        pieceList.add(piece);

        expandState.append(piece.getId(),false);
    }

    public void add(List<Piece> pieces) {
        for(int i=0;i<pieces.size();i++){
            expandState.append(pieces.get(i).getId(),false);


        }
        pieceList.addAll(pieces);


    }

    public void removeIndex(List<Piece> pieces) {

        pieceList.beginBatchedUpdates();
        for (int i=0;i<pieceList.size();i++) {
            for (int j=0;j<pieces.size();j++){

                if(pieceList.get(i).equals(pieces.get(j))){
                    expandState.delete(pieces.get(j).getId());

                    pieceList.removeIndex(pieces.get(j));
                    break;
                }

            }
        }
        pieceList.endBatchedUpdates();



    }

    public void removeIndex(Integer position) {

        pieceList.beginBatchedUpdates();
        expandState.delete(pieceList.get(position).getId());

        pieceList.removeItemAt(position);

        pieceList.endBatchedUpdates();


    }

    public void removeIndex(ArrayList<Integer> position) {

        Collections.sort(position);

        pieceList.beginBatchedUpdates();

        for(int i=position.size()-1;i>=0;i--) {
            expandState.delete(pieceList.get(position.get(i)).getId());

            pieceList.removeItemAt(position.get(i));
        }

        pieceList.endBatchedUpdates();



    }

    public void removeIndex(Piece piece){
        pieceList.beginBatchedUpdates();
        pieceList.removeIndex(piece);

        expandState.delete(piece.getId());


        pieceList.endBatchedUpdates();


    }

    public void removeAll(){

        pieceList.beginBatchedUpdates();
        pieceList.clear();
        expandState.clear();
        pieceList.endBatchedUpdates();


    }

    public void replaceAll(List<Piece> pieces) {
        pieceList.beginBatchedUpdates();

        expandState.clear();
        for (int i =pieceList.size() - 1; i >= 0; i--) {
            final Piece piece =pieceList.get(i);
            if (!pieces.contains(piece)) {
                pieceList.removeIndex(piece);
            }
        }
        pieceList.addAll(pieces);

        for(int i=0;i<pieces.size();i++){
            expandState.append(pieces.get(i).getId(),false);


        }
        pieceList.endBatchedUpdates();



    }

    public void replaceItem(Piece piece) {
        pieceList.beginBatchedUpdates();

        int index=pieceList.indexOf(piece);

        pieceList.updateItemAt(index,piece);


        pieceList.endBatchedUpdates();



    }



    @Override
    public int getItemViewType(int position) {
        return TYPE_ITEM;
    }


    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        if (viewType == TYPE_ITEM) {

           View view=  LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.piece_flippable_item, parent, false);
            return new ItemViewHolder(view);

        } else if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.spinneritem,parent, false);
            return new HeaderViewHolder(view);

        }

        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        final Piece piece;
        piece = pieceList.get(position);


        if (holder instanceof ItemViewHolder) {


           // ((ItemViewHolder) holder).imageContainer.setImageDrawable(new ShapeGenerator(context).getDrawableShape(piece.getImage(), ShapeGenerator.MODE_ROUND_RECT, 1,ShapeGenerator.SIZE_BIG));
            ((ItemViewHolder) holder).imageContainer.setImageDrawable(piece.getImage());
//            ((ItemViewHolder)holder).text.setText(piece.getName());
//            ((ItemViewHolder)holder).subText.setText(piece.getMaterial());
            ((ItemViewHolder) holder).toolbar.setTitle(piece.getName());
             ((ItemViewHolder) holder).toolbar.setSubtitle(piece.getMaterial());
             ((ItemViewHolder) holder).perfomBind(piece);


            ((ItemViewHolder) holder).setItemClickListener(new ItemClickListener() {
                @Override
                public void onClick(View v, int position, boolean isLongClick) {

                    if (isLongClick) {
                        // Utils.toast(context, "LONGCLICK" + String.valueOf(position));

                        }else{

                        switch (v.getId()) {

                            case R.id.menuFrame:
                                clickView=position;

                                showImageMenu(v);

                                break;

                            case R.id.image1:
                                 if (clickListener != null) {

                                clickListener.onPreview(piece);


                            }

                                break;

                            case  R.id.oldImage:


                                break;

                            default:
                                ((ItemViewHolder) holder).flipView.flipTheView();


                            break;

                        }

                    }



                }
            });
        } else if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).title.setText(piece.getMaterial());
        }



    }


    public void showRemoveDialog(final int posotion){

      final  ModelDataBase db=new ModelDataBase(context);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                 context);

        final Piece piece=pieceList.get(clickView);

        final PreviewModelInfo model=db.getPreviewModel(piece.getModelId());


        if(model.getPieceCount()>1){

            alertDialogBuilder
                    .setTitle(context.getResources().getString(R.string.dialogTitle_remove))
                    .setMessage(context.getResources().getString(R.string.dialogMsg_remove))
                    .setCancelable(false)
                    .setPositiveButton(context.getResources().getString(R.string.dialog_remove),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {



                                     db.deletePiece(piece);

                                   removeIndex(piece);


                                }
                            }
                    )
                    .setNegativeButton(context.getResources().getString(R.string.dialog_cancel),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    dialog.cancel();
                                }
                            }
                    );


        }else{


            alertDialogBuilder

                    .setMessage(context.getResources().getString(R.string.warning_lastPiece))
                    .setCancelable(true)

                    .setNegativeButton(context.getResources().getString(R.string.dialog_ok),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    dialog.dismiss();


                                }
                            }
                    );


        }

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bg_black_line));

        alertDialog.show();
    }

       // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return pieceList.size();
    }


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    public static class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        // each data item is just a string in this case


        private ItemClickListener itemClickListener;
         private ImageView imageContainer,previewButton,detail;
         private TextView text,subText;
         private  FrameLayout menuFrame;
         private View view;
         private EasyFlipView flipView;
         private Toolbar toolbar;


        public ItemViewHolder(View v) {
            super(v);

             imageContainer = (ImageView) v.findViewById(R.id.oldImage);
            previewButton   = (ImageView) v.findViewById(R.id.image1);
            detail   = (ImageView) v.findViewById(R.id.detailImage);
           toolbar   = (Toolbar) v.findViewById(R.id.toolbar);
           toolbar.inflateMenu(R.menu.menu_piece);


//            text=(TextView)v.findViewById(R.id.text);
//             subText=(TextView)v.findViewById(R.id.text1) ;
//             menuFrame=(FrameLayout)v.findViewById(R.id.menuFrame);
             flipView=v.findViewById(R.id.flipView);
//            menuFrame.setOnClickListener(this);
            v.setOnClickListener(this);

             previewButton.setOnClickListener(this);
             view=v.findViewById(R.id.infoContainer);

        }

        public void setItemClickListener(ItemClickListener itemClickListener) {

            this.itemClickListener = itemClickListener;

        }

        public void perfomBind(Object obj){


            detail.setImageDrawable( ((Piece)obj).getImage() );

            final PieceInfodBinding pieceInfodBinding= DataBindingUtil.bind(view);

            pieceInfodBinding.setVariable(BR.obj,obj);
            pieceInfodBinding.executePendingBindings();

            pieceInfodBinding.notifyChange();




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

    public static class HeaderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        // each data item is just a string in this case


        private ItemClickListener itemClickListener;
        private TextView title;
        LinearLayout container;


        public HeaderViewHolder(View v) {
            super(v);

            title=(TextView)v.findViewById(R.id.spinnerItem);


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
       public Object clone() {
    try
    {
        return super.clone();
    }
    catch( CloneNotSupportedException e )
    {
        return null;
    }
}


    private final SortedList.Callback<Piece> mCallback = new SortedList.Callback<Piece>() {

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
        public int compare(Piece a, Piece b) {

            return ALPHABETICAL_COMPARATOR_PIECE_MATERIAL.compare(a, b);
        }

        @Override
        public boolean areContentsTheSame(Piece oldItem, Piece newItem) {
            return oldItem.equals(newItem);
        }

        @Override
        public boolean areItemsTheSame(Piece item1, Piece item2) {
            return item1.getId() == item2.getId();
        }
    };

   public    SortedList<Piece> pieceList = new SortedList<>(Piece.class, mCallback);

    private static final Comparator<Piece> ALPHABETICAL_COMPARATOR_PIECE_MATERIAL = new Comparator<Piece>() {
        @Override
        public int compare(Piece a, Piece b) {

            if(a.getMaterial().equals(b.getMaterial())){

                return a.getName().toLowerCase().compareTo(b.getName().toLowerCase());
            }

            return a.getMaterial().toLowerCase().compareTo(b.getMaterial().toLowerCase());
        }
    };


    private void showImageMenu( View anchor){
      final ListPopupWindow  popupWindow = new ListPopupWindow(context);
        String[] option = context.getResources().getStringArray(R.array.piece_menu);

        List<ListPopUpMenuItem> itemList = new ArrayList<>();
        itemList.add(new ListPopUpMenuItem(option[0], R.drawable.ic_share_black_24dp));
        itemList.add(new ListPopUpMenuItem(option[1], R.drawable.ic_mode_edit_black_24dp));
        itemList.add(new ListPopUpMenuItem(option[2], R.drawable.ic_delete_black_24dp));


        ListPopupWindowAdapter adapter = new ListPopupWindowAdapter(context, itemList);
        popupWindow.setAnchorView(anchor);
        popupWindow.setListSelector(context.getResources().getDrawable(R.drawable.checkedlayout));
        popupWindow.setContentWidth((int) (150 * context.getResources().getDisplayMetrics().density + 0.5f));
        popupWindow.setAdapter(adapter);
        popupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                 switch (i){

                    case 0:


                        break;
                    case 1:

                        clickListener.toEdit(clickView);

                        break;
                    case 2:

                        clickListener.toRemove(pieceList.get(clickView));
                        break;




                }

                popupWindow.dismiss();


            }
        });
        popupWindow.show();




    }



}


*/
