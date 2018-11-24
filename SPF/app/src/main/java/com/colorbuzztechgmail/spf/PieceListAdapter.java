package com.colorbuzztechgmail.spf;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC01 on 25/11/2017.
 */

public class PieceListAdapter extends RecyclerView.Adapter<PieceListAdapter.ViewHolder> implements PopupMenu.OnMenuItemClickListener{


    private Context context;

    public static final int MODE_SIZE=0;
    public static final int MODE_MATERIAL=1;
    public static final int MODE_ALL_PIECE=2;
    public static final int MODE_ALL_MATERIAL=3;

    private Activity activity;
    private int mode=0;
    private View view;

    private PieceListPopUp dialogFragment;
    private List<Piece> pieceList;
    private int clickView=0;
    // Provide a suitable constructor (depends on the kind of dataset)
    public PieceListAdapter(Context context, List<Piece> pieces, int mode, Activity activity, PieceListPopUp dialogFragment) {
        this.context = context;
        this.mode=mode;
        this.dialogFragment=dialogFragment;
        pieceList=new ArrayList<>();
        pieceList.addAll(pieces);
        this.activity=activity;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public PieceListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {

        View v=null;

       // Log.e("VIEWTYPE",String.valueOf(viewType));


              v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.piece_listview, parent, false);
            // set the view's size, margins, paddings and layout parameters


        return new ViewHolder(v);


    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element


        final Piece piece;
        piece = pieceList.get(position);

        if(mode==MODE_SIZE){
            holder.titleText.setText(context.getString(R.string.piece_Size));

            holder.subText.setText(String.valueOf(piece.getSize()));
        }


        if(mode==MODE_MATERIAL){
            holder.titleText.setVisibility(View.GONE);
            holder.subText.setText(String.valueOf(piece.getName()));
        }

        if(mode==MODE_ALL_PIECE){
            holder.container.setOrientation(LinearLayout.VERTICAL);
            holder.titleText.setText(piece.getMaterial());
            holder.subText.setText(String.valueOf(piece.getName()));
        }

        if(mode==MODE_ALL_MATERIAL){
            holder.container.setOrientation(LinearLayout.VERTICAL);
            holder.titleText.setText(piece.getMaterial());
            holder.subText.setText(String.valueOf(piece.getName()));
        }


        holder.imageContainer.setImageDrawable(new ShapeGenerator(context).getDrawableShape(piece.getImage(),ShapeGenerator.MODE_ROUND_RECT,R.color.iconsColor,ShapeGenerator.SIZE_BIG));



        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View v, int position, boolean isLongClick) {
                if (isLongClick) {
                   // Utils.toast(context, "LONGCLICK" + String.valueOf(position));



                            clickView=position;
                            showPopup(v,position);



                } else {

                   // Utils.toast(context, "SHORTCLICK" + String.valueOf(position));

                 showInfoPopUp(position);

                }
            }
        });


    }

    public void showRemoveDialog(final int posotion){

      final  ModelDataBase db=new ModelDataBase(context);

        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(
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

                                     updateDialog(piece);


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

        android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bg_black_line));

        alertDialog.show();
    }

    private void showInfoPopUp(int position){




        Utils.showInfoPopUp(context, pieceList.get(position));




    }

    public void showShare(int modelId ,View anchor){


        final ModelDataBase db=new ModelDataBase(context);

        List<ListPopUpMenuItem> itemList = new ArrayList<>();

        List<Material>materials=db.getModelMaterial(modelId);

            final ListPopupWindow popupWindow = new ListPopupWindow(context);

            for(Material material:materials){

                itemList.add(new ListPopUpMenuItem(material.getName(),db.getCustomMaterial(Integer.valueOf(material.getCustomMaterialId())).getImage()));

            }

            ListPopupWindowAdapter adapter = new ListPopupWindowAdapter(context, itemList);
            popupWindow.setAnchorView(anchor);
            popupWindow.setListSelector(context.getResources().getDrawable(R.drawable.checkedlayout));
            popupWindow.setContentWidth(ListPopupWindow.WRAP_CONTENT);

            popupWindow.setAdapter(adapter);
            popupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    popupWindow.dismiss();




                }
            });
            popupWindow.show();


    }
    public void showMaterialChooser(int modelId){

        final ModelDataBase db=new ModelDataBase(context);


        List<Material>materials=db.getModelMaterial(modelId);


        LayoutInflater li=LayoutInflater.from(context);

        View v=li.inflate(R.layout.save_piece_popup,null);

        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        ((Toolbar)v.findViewById(R.id.pieceToolbar)).setTitle(context.getResources().getString(R.string.action_moveTo));
        builder.setView(v);


        final Dialog d=builder.create();


        final ArrayList<ListPopUpMenuItem> items =new ArrayList<>();

        for(Material material:materials){

            if(db.getCustomMaterial(Integer.valueOf(material.getCustomMaterialId())).getImage()==null){

                items.add(new ListPopUpMenuItem(material.getName(),R.drawable.leather));



            }else{

                items.add(new ListPopUpMenuItem(material.getName(),db.getCustomMaterial(Integer.valueOf(material.getCustomMaterialId())).getImage()));

            }


        }

        final  ListPopupWindowAdapter adapter = new ListPopupWindowAdapter(context,items){
            public View getView(final int position, View convertView, ViewGroup parent) {
                //Use super class to create the View
                View v = super.getView(position, convertView, parent);
                TextView tv = (TextView)v.findViewById(R.id.text);

                //Put the image on the TextView
                // tv.setCompoundDrawablesWithIntrinsicBounds(items.get(position).getImageDrawable(), null, null, null);
                int dp8=(int) (8 *context.getResources().getDisplayMetrics().density + 0.5f);
                //Add margin between image and text (support various screen densities)
                int dp5 = (int) (5 *context.getResources().getDisplayMetrics().density + 0.5f);
                tv.setCompoundDrawablePadding(dp5);
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String material=((TextView)v.findViewById(R.id.text)).getText().toString();
                        Utils.toast(context,material);
                        if( !pieceList.get(clickView).getMaterial().equals(material)){

                            Object obj=new Object();
                            try {
                                obj=(pieceList.get(clickView)).clone();

                                Piece newPiece=(pieceList.get(clickView));
                                newPiece.setMaterial(material);

                                db.updateAllSamePieceData((Piece)obj,newPiece);

                                updateDialog((Piece)obj);


                                //String title=(String)dialogFragment.mSpinner.getAdapter().getItem(dialogFragment.getPosition());


                                //notifyItemRemoved(clickView);

                            } catch (CloneNotSupportedException e) {
                                e.printStackTrace();
                            }


                        }

                        d.dismiss();

                    }
                });
                return v;
            }
        };


        d.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                ((ListView)d.getWindow().findViewById((R.id.lisview))).setAdapter(adapter);


                d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
        });
        d.show();
    }


    public void showPopup(View v,int position) {


            view=v;

            PopupMenu popup = new PopupMenu(context, v);
            MenuInflater inflater = popup.getMenuInflater();
            popup.setOnMenuItemClickListener(this);
            //menuClick=position;
            inflater.inflate(R.menu.submenu_piece_option, popup.getMenu());

            if(mode==MODE_ALL_PIECE){
                popup.getMenu().getItem(0).setVisible(true);  //Tallaje
                popup.getMenu().getItem(1).setVisible(true);  //Vista Previa
                popup.getMenu().getItem(2).setVisible(false);  //Editar
                popup.getMenu().getItem(3).setVisible(true); //Mover
                popup.getMenu().getItem(4).setVisible(false); //Enviar
                popup.getMenu().getItem(5).setVisible(true); //Enviar


            }else if(mode==MODE_SIZE){


                ((MenuItem) popup.getMenu().getItem(0)).getSubMenu().getItem(0).setVisible(true);  //Tallaje
                ((MenuItem) popup.getMenu().getItem(0)).getSubMenu().getItem(1).setVisible(true);  //Vista Previa
                ((MenuItem) popup.getMenu().getItem(0)).getSubMenu().getItem(2).setVisible(true);  ///Editar
                ((MenuItem) popup.getMenu().getItem(0)).getSubMenu().getItem(3).setVisible(true); //Mover
                ((MenuItem) popup.getMenu().getItem(0)).getSubMenu().getItem(4).setVisible(true); //Enviar
                ((MenuItem) popup.getMenu().getItem(0)).getSubMenu().getItem(5).setVisible(true);   //Eliminar


            }else if((mode==MODE_MATERIAL) ||(mode==MODE_ALL_MATERIAL)){


                popup.getMenu().getItem(0).setVisible(false);  //Tallaje
                popup.getMenu().getItem(1).setVisible(true);  //Vista Previa
                popup.getMenu().getItem(2).setVisible(false); //Editar
                popup.getMenu().getItem(3).setVisible(true); //Mover
                popup.getMenu().getItem(4).setVisible(true); //Enviar
                popup.getMenu().getItem(5).setVisible(true);  //Enviar
//Enviar

            }

        popup.show();



    }

    private void updateDialog(Piece piece){

        dialogFragment.LoadInfo(piece.getModelId(),mode,piece.getMaterial());

        if(activity instanceof MaterialActivity){
            ((MaterialActivity) activity).loadData();

        }else if(activity instanceof Piece_Activity){
            ((Piece_Activity) activity).loadData();

        }



    }
       // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return pieceList.size();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item)  {

        final int id=pieceList.get(clickView).getId();
        final int modelId=pieceList.get(clickView).getModelId();
        final String name=pieceList.get(clickView).getName();

        switch (item.getItemId()){

            case R.id.action_editPiece:
 ;
                break;

            case R.id.action_pieceList:


                //Tallaje


                     int position=0;
                     for(int i=0;i<dialogFragment.titleValues.size();i++){

                         if(name.equals(dialogFragment.titleValues.get(i).getName())){

                             position=i;
                             break;

                         }

                     }
                    dialogFragment.mSpinner.setSelection(position);

                     //Vista Previa


                break;

            case R.id.action_preview:

                PiecePreviewPopUp piecePopUp= PiecePreviewPopUp.newInstance(modelId,id);

                piecePopUp.show(((AppCompatActivity)activity).getSupportFragmentManager(),"piecePopUp");
                break;

            case R.id.action_delete:

                showRemoveDialog(clickView);

                break;
            case R.id.action_move:

                 showMaterialChooser(modelId);

                break;
        }


       /*
        PieceEditPopUp popUp= PieceEditPopUp.newInstance(piece.getName(),piece.getId(),piece.getModelId());

        popUp.show(ma.getSupportFragmentManager(),"");*/
        return false;
    }



    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        // each data item is just a string in this case


        private ItemClickListener itemClickListener;
        private TextView subText,titleText;
        private ImageView imageContainer;
        LinearLayout container;


        public ViewHolder(View v) {
            super(v);

            titleText = (TextView) v.findViewById(R.id.text );
            subText = (TextView) v.findViewById(R.id.text1);
            imageContainer = (ImageView) v.findViewById(R.id.imgPiece);
            container = (LinearLayout) v.findViewById(R.id.container);

            imageContainer.setOnClickListener(this);


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

}


