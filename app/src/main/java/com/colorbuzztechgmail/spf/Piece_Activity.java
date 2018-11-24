package com.colorbuzztechgmail.spf;

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.colorbuzztechgmail.spf.RecyclerSliderAdapter.Preview;
import com.colorbuzztechgmail.spf.popup.waitPoup;

import java.util.ArrayList;
import java.util.List;

public class Piece_Activity extends AppCompatActivity implements  ItemActionListener,View.OnClickListener,Utils.onSavedInterface{


    private static final String TAB="TabFragment1";
    public RecyclerView recyclerItems,recyclerView;
    waitPoup waitPoup;
    Toolbar toolbar;
     private  List<Object> pieceList= new ArrayList<>();
    RecyclerSliderAdapter sliderAdapter;
    PieceAdapter1 adapter1;
    PieceDataset pieceDataset;
    int modelId;
    RecyclerView.ItemDecoration itemDecoration ;
    public boolean viewMode=true;
    private String selectedMaterial;


    public void setPieceList(List<Piece> pieces) {

        pieceList.clear();

        for(Piece piece : pieces)
            pieceList.add(piece);


     }

    public List<Object> getPieceList() {
        return pieceList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.piece_fragment);

       modelId=Integer.valueOf(getIntent().getStringExtra("MODEL"));

       toolbar = (Toolbar) findViewById(R.id.toolbar);
       setSupportActionBar(toolbar);

        loadMenu();

         recyclerItems =(RecyclerView) findViewById(R.id.recycler);

        final LinearLayoutManager lm = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        lm.setSmoothScrollbarEnabled(true);
        recyclerItems.setLayoutManager(lm);
        findViewById(R.id.viewModeFrame).setOnClickListener(this);

        sliderAdapter = new RecyclerSliderAdapter(this,this);
        recyclerItems.swapAdapter(sliderAdapter,true);
        recyclerView=(RecyclerView)findViewById(R.id.previewRecycler);
       // recyclerView.getRecycledViewPool().setMaxRecycledViews(PieceAdapter1.ITEM,0);
        adapter1 = new PieceAdapter1(this,this,true);
        adapter1.setViewMode(Utils.ViewMode.GRID);
        pieceDataset=new PieceDataset(recyclerView,adapter1);
        adapter1.dataset(pieceDataset);

        selectedMaterial=getResources().getString(R.string.action_sortAll);


        loadData();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_piece,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {


            case R.id.action_movepiece:

                final PieceMaterialAsigmentPopUp pieceMaterialAsigment = PieceMaterialAsigmentPopUp.newInstance(modelId);

                pieceMaterialAsigment.show(getSupportFragmentManager(), "movePiece");
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {

        super.onStart();

    }

    public void loadData(){


        Object[] obj=new Object[1];

        obj[0]=modelId;


        new loadPiecesDetailAsync().execute(obj);
    }

    private void loadPiece(String material){

        selectedMaterial=material;

        if(material.equals(getResources().getString(R.string.action_sortAll))){
            setupRecyclerAllPieces(pieceList);
        }else{

            final List<Object> list= new ArrayList<>();

            for(Object obj:pieceList){



                if(((Piece)obj).getMaterial().equals(material)){

                    list.add(obj);

                }


            }

            setupRecyclerAllPieces(list);





        }

        toolbar.setTitle(getResources().getString(R.string.piece_Amount) + " " + "("+ String.valueOf(pieceList.size())+")");
        customView(viewMode);


    }

    private void loadMenu(){
        toolbar.setTitle(getResources().getString(R.string.piece_Amount) + " " + String.valueOf(pieceList.size()) );
        toolbar.setTitleTextColor(getResources().getColor(R.color.iconsColor));
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_keyboard_arrow_left_white_24dp));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }

        });

    }

    public void setupRecyclerTitle (Object obj) {




        sliderAdapter.removeAll();

       sliderAdapter.addAll((List<Preview>) obj);


        recyclerItems.swapAdapter(sliderAdapter,true);

         recyclerItems.getLayoutManager().scrollToPosition(0);


    }

    private void setupRecyclerAllPieces(List<Object>pieces){


        pieceDataset.removeAll();
        recyclerView.swapAdapter(adapter1,true);
        pieceDataset.addAll(pieces);


    }

    public void customView(boolean mode) {

          int nColumn=0;

          if(itemDecoration!=null){
              recyclerView.removeItemDecoration(itemDecoration);

          }
        if (!mode) {  ///ListView

            adapter1.setViewMode(Utils.ViewMode.LIST);




            nColumn = 1;

            final GridLayoutManager gridLayoutManager = new GridLayoutManager(this, nColumn, LinearLayoutManager.VERTICAL, false);
           itemDecoration = new GridSpacingItemDecoration(nColumn, 8, true);

            recyclerView.addItemDecoration(itemDecoration);
            recyclerView.setLayoutManager(gridLayoutManager);



        } else if (mode) {///GridView

           adapter1.setViewMode(Utils.ViewMode.GRID);



            nColumn = 2;


            final GridLayoutManager gridLayoutManager = new GridLayoutManager(this, nColumn);

            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {

                    return adapter1.isHeader(position) ? gridLayoutManager.getSpanCount() : 1;
                }
            });
            itemDecoration = new GridSpacingItemDecoration(nColumn, 8, true);

            recyclerView.addItemDecoration(itemDecoration);
            recyclerView.setLayoutManager(gridLayoutManager);





        }

    }

    public void showRemoveDialog(final Object object ){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
              this);

        alertDialogBuilder
                .setTitle( getResources().getString(R.string.dialogTitle_remove))
                .setMessage( getResources().getString(R.string.dialogMsg_remove))
                .setCancelable(false)
                .setPositiveButton( getResources().getString(R.string.dialog_remove),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                SparseBooleanArray state= (SparseBooleanArray) object;
                                ArrayList<Integer> ids=new ArrayList<>();

                                for(int i =state.size()-1;i>=0;i--){

                                    for(int j =pieceDataset.size()-1;j>=0;j--){

                                     if(pieceDataset.getObject(j) instanceof Piece){

                                         if( pieceDataset.getPiece(j).getId()==state.keyAt(i)){

                                             ids.add(pieceDataset.getPiece(j).getId());

                                             if(state.get(pieceDataset.getPiece(j).getId())) {
                                                 pieceDataset.removeIndex(j);
                                             }


                                         }

                                     }



                                    }



                                }




                                adapter1.multiChoiceHelper.clearChoices();
                                loadMenu();
                                dialog.dismiss();
                                showUndoSnackbar(ids);

                            }
                        }
                )
                .setNegativeButton( getResources().getString(R.string.dialog_cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.cancel();
                            }
                        }
                );




        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable( getResources().getDrawable(R.drawable.bg_black_line));

        alertDialog.show();
    }

    private void showUndoSnackbar(final ArrayList<Integer> ids){

        CoordinatorLayout view = (CoordinatorLayout)  findViewById(R.id.parentLayout);

        String mssg;



        mssg =getResources().getString(R.string.piece_Amount) + " eliminada";



        Snackbar snackbar = Snackbar.make(view, mssg, Snackbar.LENGTH_SHORT)
                .setAction(getResources().getString(R.string.action_undo), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {



                        loadPiece(selectedMaterial);


                     }

                }).setCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar snackbar, int dismissType) {
                        super.onDismissed(snackbar, dismissType);

                        if(dismissType !=DISMISS_EVENT_ACTION){

                            ModelDataBase db= new ModelDataBase(getApplicationContext());


                            for(int i =ids.size()-1;i>=0;i--){

                                for(int j =pieceList.size()-1;j>=0;j--){

                                    if(pieceList.get(j) instanceof Piece){

                                        if( ((Piece)pieceList.get(j)).getId()==ids.get(i)){

                                                db.deletePiece((Piece)pieceList.get(j));
                                                pieceList.remove(j);



                                        }

                                    }



                                }



                            }



                        }

                    }
                });


        snackbar.show();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.viewModeFrame:

                viewMode=!viewMode;

                ((ImageView)findViewById(R.id.imageView5)).setActivated(viewMode);
                customView(viewMode);

                break;

        }
    }

    @Override
    public void onSaved(Object obj, int position, boolean isEditable) {

        if(!isEditable){
            if(isObjectVisible(obj))
                pieceDataset.add(obj);



        }else {


            if (isObjectVisible(obj)) {

                pieceDataset.replaceItem(obj, position);
            } else {

                pieceDataset.removeIndex(position);
                pieceDataset.deleteEmptyHeaders();


            }
        }

        adapter1.multiChoiceHelper.clearChoices();


    }

    protected boolean isObjectVisible(Object obj) {

         if(selectedMaterial.equals(getResources().getString(R.string.action_sortAll))){

             return true;
         }



       return selectedMaterial.equals(((Piece)obj).getMaterial()) ;
    }

    @Override
    public void onPreview(Object obj) {

     if(!(obj instanceof Piece)){

         loadPiece(((Preview)obj).getName());

         }else {


      /*   final BufferPiece bufferPiece=new BufferPiece();
         setBufferPiece(bufferPiece);
         getBufferPiece().setPiece((Piece)obj);

         setBufferPiece(bufferPiece);
         pieceFrame.removeAllViews();
         final DrawView drawView = new DrawView(this, getBufferPiece().getPiece(),null, getResources().getDisplayMetrics().widthPixels,
                 (int) getResources().getDimension(R.dimen.card_height) );

         pieceFrame.addView(drawView);





         drawView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {

                 if(getBufferPiece()!=null){
                     showPreview(getBufferPiece().getPiece());

                 }
             }
         });




        setInfoVisible(true);

         materialinfoContainer.startAnimation(MaterialinfoContainer.MODE_SUBMENU_PIECE, true,null,getBufferPiece().getPiece());
*/

     }


    }

    @Override
    public void toRemove(Object obj) {

     showRemoveDialog(obj);

    }

    @Override
    public void toEdit(long [] ids) {

        int[] positions= new int[ids.length];

        for(int i=0;i<ids.length;i++){

            positions[i]=pieceDataset.sortedList.indexOf(pieceDataset.getPieceById(Utils.toIntExact(ids[i])));

        }

        EditPiece editPiece= EditPiece.newInstanceEditPiecePopUp(modelId,positions,ids,this);

        editPiece.show(getSupportFragmentManager(),"edit_piece");


    }

    @Override
    public void onClick(View v, int position, boolean isLongClick) {

    }

    public class loadPiecesDetailAsync extends AsyncTask<Object, Void,List<Preview>> {

        protected void onPreExecute() {



               waitPoup = com.colorbuzztechgmail.spf.popup.waitPoup.newInstance(getString(R.string.dialog_loadModel));

               waitPoup.show(getSupportFragmentManager(), "waitPopup");


        }

        protected List<Preview> doInBackground(Object[] ids) {


            //pos 0 -modelID

             int modelId=(Integer)ids[0];

            ModelDataBase db = new ModelDataBase(getApplicationContext());
            List<Preview>itemTable=new ArrayList<>();



            try {



                List<Material>materials=new ArrayList<>();

                final PreviewModelInfo model = db.getPreviewModelById(modelId);
                final String size=model.getMinSize() +"-"+model.getMaxSize();

                materials=db.getModelMaterial(modelId);

               setPieceList(db.getHighLightPieces(modelId,true));



                for(int i=getPieceList().size()-1;i>=0;i--){

                        ((Piece)getPieceList().get(i)).setSize(size);



                }
                 Preview mAllTitle=new Preview(0,getResources().getString(R.string.action_sortAll),null);

                itemTable.add(mAllTitle);
                // toolbar.setTitle(String.valueOf(materials.size()) +" "  + getString(R.string.materialsTxt));

                for(Material material:materials) {

                    Drawable img=db.getCustomMaterialImage(material.getCustomMaterialId());

                    final Preview mTitle=new Preview( material.getId(),material.getName(),img);

                    itemTable.add(mTitle);


                }



                return itemTable;

            } catch (Exception e) {

                e.printStackTrace();
                Log.e("Background", e.toString());


            }
            return itemTable;
        }



        @Override
        protected void onPostExecute(List<Preview> previews) {



            if(previews.isEmpty()){



                Utils.toast(getApplicationContext(),"Fallo importar piezas");
            }else {



                setupRecyclerTitle(previews);
                loadPiece(getResources().getString(R.string.action_sortAll));

            }

            if(waitPoup!=null){
                waitPoup.dismiss();

            }


        }
    }







}