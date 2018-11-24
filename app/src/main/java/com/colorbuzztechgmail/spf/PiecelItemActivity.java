package com.colorbuzztechgmail.spf;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.util.LongSparseArray;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.colorbuzztechgmail.spf.adapter.BaseAdapter;
import com.colorbuzztechgmail.spf.popup.waitPoup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PiecelItemActivity extends BaseItemActivity  implements View.OnClickListener{


    public static final String MODEL_ID="model_id";

    public static void newInstancePieceActivity(Context context, long modelId,String tag){


       Intent h= new Intent(context,PiecelItemActivity.class);
        h.putExtra(FRAGMENT_TYPE,activityType.PIECE.name());
        h.putExtra(MODEL_ID,String.valueOf(modelId));
        h.putExtra(VIEW_MODE, Utils.ViewMode.GRID);
        h.putExtra(TAG,tag);


        context.startActivity(h);



    }



    private PieceDataset dataset;
    private PieceListAdapter1 adapter;
    private ModelDataBase db;
    private long modelId;
    private Spinner mSpinnerMaterial;
    private Map<String,CustomMaterial> materialMap;
    private Map<String,Drawable> materialImageMap;


    @Override
    public void setupRecycler() {


        mRecycler= new RecyclerView(this);
        mRecycler.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mRecycler.setHasFixedSize(true);

        mRecycler.setAdapter(getAdapter());
    }

    @Override
    protected void initializeValues() {
        modelId=Long.valueOf(getIntent().getStringExtra(MODEL_ID));
        setSelectedCategory(getString(R.string.action_sortAll));
        setContentView(R.layout.piece_fragment1);
        setViewMode(Utils.ViewMode.GRID);

        ((ImageView)findViewById(R.id.imageView5)).setActivated(ViewMode.equals(Utils.ViewMode.LIST) ? true : false);



        materialMap=new HashMap<>();
        materialImageMap=new HashMap<>();


        db=new ModelDataBase(this);
        adapter=new PieceListAdapter1(this,this,true);
        this.dataset=new PieceDataset(mRecycler,adapter);
        setDataset(this.dataset);

        loadData();




    }


    private void setupView(){


        mSpinnerMaterial=findViewById(R.id.spinnerMaterial);

        final List<String>materialsName=new ArrayList<>();

        materialsName.add(getString(R.string.action_sortAll));
        CustomMaterial allMaterial= new CustomMaterial(getString(R.string.action_sortAll));


        for(Object obj:materialMap.keySet()){
            materialsName.add((String)obj);
        }
        Collections.sort(materialsName.subList(1,materialsName.size()-1));


        materialMap.put(getString(R.string.action_sortAll),allMaterial);
        materialImageMap.put(getString(R.string.action_sortAll),getDrawable(R.drawable.ic_leather_primary_dark_24dp));





        final ArrayAdapter spinnerMaterialAdapter = new ArrayAdapter<String>(this,
                R.layout.spinneritem2, materialsName){


            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

                View v = null;
                parent.setVerticalScrollBarEnabled(true);



                    View customView = LayoutInflater.from(getContext()).inflate(R.layout.dropdown_spinneritem2,null);
                    TextView tv = customView.findViewById(R.id.text);
                    TextView tv1 = customView.findViewById(R.id.subText);

                    ImageView imageView=customView.findViewById(R.id.image);
                    imageView.setImageDrawable( materialImageMap.get(materialsName.get(position)));
                    tv.setText(materialsName.get(position));
                if(materialsName.get(position).equals(getString(R.string.action_sortAll)))
                    tv1.setVisibility(View.INVISIBLE);
                    tv1.setText(materialMap.get(materialsName.get(position)).getName());
                    return customView;

                   /*
                    // Hide scroll bar because it appears sometimes unnecessarily, this does not prevent scrolling
                    v = super.getDropDownView(position, null, parent);
  */

            }

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

                View customView = LayoutInflater.from(getContext()).inflate(R.layout.spinneritem2,null);
                TextView tv = customView.findViewById(R.id.text);
                TextView tv1 = customView.findViewById(R.id.subText);

                ImageView imageView=customView.findViewById(R.id.image);
                imageView.setImageDrawable( materialImageMap.get(materialsName.get(position)));
                if(materialsName.get(position).equals(getString(R.string.action_sortAll)))
                    tv1.setVisibility(View.GONE);
                tv.setText(materialsName.get(position));
                tv1.setText(materialMap.get(materialsName.get(position)).getName());
                return customView;

            }
        }


                ;
        spinnerMaterialAdapter.setDropDownViewResource(R.layout.dropdown_spinneritem2);



        mSpinnerMaterial.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                if(!getSelectedCategory().equals((String)mSpinnerMaterial.getSelectedItem())){


                    selectCategory((String)mSpinnerMaterial.getSelectedItem(),null);
                };


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mSpinnerMaterial.setAdapter(spinnerMaterialAdapter);
        mSpinnerMaterial.setPrompt(getString(R.string.materialsTxt));



    }


    private void loadData(){


        Object[] obj=new Object[1];


            obj[0]=modelId;






        new loadPiecesDetailAsync().execute(obj);
    }



    @Override
    protected BaseAdapter getAdapter() {
        return adapter;
    }

    @Override
    protected List<Object> getRecentItems() {

        final List<Object> buffer=new ArrayList<>();

        buffer.addAll(db.getHighLightPieces(modelId,true));
        return buffer;
    }

    @Override
    protected List<Object> filterByParameter(String query) {


        final List<Object> filteredModelList = new ArrayList<>();


        return filteredModelList;


    }

    @Override
    protected List<Object> filterType(String query, CategoryFragment.ChooserType type) {
        final List<Object> filteredModelList = new ArrayList<>();



        if(!getSelectedCategory().equals(getString(R.string.action_sortAll))) {

            for(Object obj : db.getHighLightPieces(modelId,true)){

               if (((Piece)obj).getMaterial().equals(query)){
                    filteredModelList.add(obj);

                }

            }


        }else{


            for(Object obj : db.getHighLightPieces(modelId,true)){

                    filteredModelList.add(obj);

            }


        }


        return filteredModelList;

    }


    @Override
    public void fillDataset(List<Object> models) {

        dataset.removeAll();
        if (models.isEmpty()){

            dataset.setEmptyTitle();
        }else{
        dataset.addAll(models);
        }


    }

    @Override
    protected void showRemoveDialog(final LongSparseArray<Boolean> checkedList) {



        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);
        alertDialogBuilder
                .setTitle( getResources().getString(R.string.dialogTitle_remove))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.dialog_remove),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                final ArrayList<Long> removeIds=new ArrayList<>();
                                if(checkedList.size()>0) {

                                    final List<Object>pieceList=new ArrayList<>();
                                    for(int i=0;i<checkedList.size();i++) {

                                        if(checkedList.valueAt(i)) {


                                            final Piece piece=dataset.getPieceById(checkedList.keyAt(i));

                                            pieceList.add(piece);

                                            removeIds.add(checkedList.keyAt(i));



                                        }
                                    }

                                    dataset.removeByIds(removeIds);
                                    refreshInfobar();
                                    adapter.multiChoiceHelper.clearChoices();
                                    dialog.dismiss();
                                    showUndoSnackbar(pieceList);

                                }
                            }
                        }
                )
                .setNegativeButton(getResources().getString(R.string.dialog_cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.cancel();
                            }
                        }
                );
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_black_line));
        alertDialog.show();

    }

    private void showUndoSnackbar(final List<Object> pieceList){

        CoordinatorLayout view =  findViewById(R.id.parentLayout);

        String mssg;

        if(pieceList.size()==1){

            mssg = ((Piece)pieceList.get(0)).getName() + " eliminada";

        }else {

            mssg = String.valueOf(pieceList.size()) + " " + getString(R.string.piece_Amount) + " eliminadas";

        }

        Snackbar snackbar = Snackbar.make(view, mssg, Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.action_undo), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dataset.addAll(pieceList);


                    }

                }).setCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar snackbar, int dismissType) {
                        super.onDismissed(snackbar, dismissType);

                        if(dismissType !=DISMISS_EVENT_ACTION){

                            for(Object obj: pieceList){

                                db.deletePiece((Piece)obj);


                            }
                            db.closeDB();

                        }

                    }
                });


        snackbar.show();
    }

    @Override
    protected void showEditDialog(long[] ids) {


        int[] positions= new int[ids.length];

        for(int i=0;i<ids.length;i++){

            positions[i]=dataset.sortedList.indexOf(dataset.getPieceById(Utils.toIntExact(ids[i])));

        }

        EditPiece editPiece= EditPiece.newInstanceEditPiecePopUp(modelId,positions,ids,this);

        editPiece.show(getSupportFragmentManager(),"edit_piece");



    }

    @Override
    protected void addNewItem() {



    }

    @Override
    protected boolean isObjectVisible(Object obj) {



        if(!getSelectedCategory().equals(getString(R.string.action_sortAll))) {


            return ((Piece)obj).getMaterial().equals(getSelectedCategory());

        }
        return true;
    }

    @Override
    protected boolean isCategoryVisible(String category) {
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_piece, menu);





//
        return true;


    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

                case R.id.action_movepiece:

                    final PieceMaterialAsigmentPopUp pieceMaterialAsigment = PieceMaterialAsigmentPopUp.newInstance(Utils.toIntExact(modelId));

                    pieceMaterialAsigment.show(getSupportFragmentManager(), "movePiece");
                   return true;

        }



        return false;
    }

    @Override
    public void onClick(View v, int position, boolean isLongClick) {



        if(!isLongClick){



            final Piece piece=dataset.getPiece(position);
            piece.setSize(db.getPreviewModelById(modelId).getSizeList().toString());
            Utils.showInfoPopUp(this,piece);



        }


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.viewModeFrame:

                ViewMode=ViewMode.equals(Utils.ViewMode.LIST) ? Utils.ViewMode.GRID : Utils.ViewMode.LIST;


                ((ImageView)findViewById(R.id.imageView5)).setActivated(ViewMode.equals(Utils.ViewMode.LIST) ? true : false);

                customView();

                break;





        }
    }



    public class loadPiecesDetailAsync extends AsyncTask<Object, Void,Void> {

          waitPoup waitPoup;

        protected void onPreExecute() {



            waitPoup = com.colorbuzztechgmail.spf.popup.waitPoup.newInstance(getString(R.string.dialog_loadModel));

            waitPoup.show(getSupportFragmentManager(), "waitPopup");


        }

        protected Void doInBackground(Object[] ids) {


            //pos 0 -modelID

            long modelId=(Long) ids[0];

            ModelDataBase db = new ModelDataBase(getApplicationContext());



            try {



                List<Material>materials=new ArrayList<>();

                PreviewModelInfo model=db.getPreviewModelById(modelId);

                final String size=model.getMinSize() +"-"+model.getMaxSize();

                materials=db.getModelMaterial(Utils.toIntExact(modelId));



                for(Material material:materials){

                    final CustomMaterial customMaterial=db.getCustomMaterial(material.getCustomMaterialId());

                    if(customMaterial!=null){

                        materialMap.put(material.getName(),db.getCustomMaterial(material.getCustomMaterialId()));
                        materialImageMap.put(material.getName(), ShapeGenerator.getShapeDrawable(getApplicationContext(),customMaterial.getImage(),ShapeGenerator.MODE_ROUND_RECT,48,48));

                    }else{

                        final CustomMaterial emptyMaterial=new CustomMaterial(getString(R.string.importNoAsigned_Cat));
                        emptyMaterial.setImage(getDrawable(R.drawable.ic_leather_primary_dark_24dp));

                        materialMap.put(material.getName(),emptyMaterial);
                        materialImageMap.put(material.getName(), ShapeGenerator.getShapeDrawable(getApplicationContext(),emptyMaterial.getImage(),ShapeGenerator.MODE_ROUND_RECT,48,48));

                    }
                }



            } catch (Exception e) {

                e.printStackTrace();
                Log.e("Background", e.toString());


            }


            return null;
        }



        @Override
        protected void onPostExecute( Void result) {

             setupView();

            if(waitPoup!=null){
                waitPoup.dismiss();

            }


        }
    }

    @Override
    protected Object getObjectToLoad(Cursor mCursor) {

        return db.createPiece(mCursor,true);
    }
}
