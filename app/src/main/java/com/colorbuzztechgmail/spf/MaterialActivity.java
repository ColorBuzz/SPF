package com.colorbuzztechgmail.spf;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.colorbuzztechgmail.spf.dataset.Dataset;
import com.colorbuzztechgmail.spf.popup.MaterialAsigmentoPopUp;
import com.colorbuzztechgmail.spf.popup.waitPoup;

import java.util.ArrayList;
import java.util.List;

public class MaterialActivity extends AppCompatActivity implements ItemActionListener,View.OnClickListener {
    Toolbar toolbar;
    private RecyclerView recyclerView;
      public ModelDataBase db;
    int modelId;
    waitPoup waitPoup;
    MaterialAsigmentoPopUp poupMaterialAsigment;
    private Material bufferMaterial;
    MaterialDataset materialDataset;
    MaterialAdapter mAdapter;
    public boolean viewMode=false;
    RecyclerView.ItemDecoration itemDecoration ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.material_fragment);

        db=new ModelDataBase(this);
        recyclerView=(RecyclerView) findViewById(R.id.previewRecycler);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        modelId=Integer.valueOf(getIntent().getStringExtra("MODEL"));
        mAdapter = new MaterialAdapter(this,this,true);
        mAdapter.setViewMode(Utils.ViewMode.GRID);
        materialDataset=new MaterialDataset(recyclerView, mAdapter);
        mAdapter.dataset(materialDataset);

        findViewById(R.id.viewModeFrame).setOnClickListener(this);


        loadMenu();





        loadData();
     }

    public void loadData(){




        Object[] obj=new Object[1];

        obj[0]=modelId;


        new loadMaterialDetailAsync().execute(obj);
    }

    private void loadMenu(){

        toolbar.setTitle(getResources().getString(R.string.materialsTxt));
        toolbar.setTitleTextColor(getResources().getColor(R.color.iconsColor));
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_keyboard_arrow_left_white_24dp));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }

        });

    }

    public void loadMaterial(List<Object> materials){


        setupRecyclerAllPieces(materials);


        customView(viewMode);



    }


    private void setupRecyclerAllPieces(List<Object>materials)
    {


        materialDataset.removeAll();
        recyclerView.swapAdapter(mAdapter,true);
         materialDataset.addAll(materials);


    }


    public void customView(boolean mode) {

        int nColumn=0;

        if(itemDecoration!=null){
            recyclerView.removeItemDecoration(itemDecoration);

        }
        if (!mode) {  ///ListView

            mAdapter.setViewMode(Utils.ViewMode.LIST);




            nColumn = 1;

            final GridLayoutManager gridLayoutManager = new GridLayoutManager(this, nColumn, LinearLayoutManager.VERTICAL, false);
            itemDecoration = new GridSpacingItemDecoration(nColumn, 8, true);

            recyclerView.addItemDecoration(itemDecoration);
            recyclerView.setLayoutManager(gridLayoutManager);



        } else if (mode) {///GridView

            mAdapter.setViewMode(Utils.ViewMode.GRID);



            nColumn = 2;


            final GridLayoutManager gridLayoutManager = new GridLayoutManager(this, nColumn);

            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {

                    return mAdapter.isHeader(position) ? gridLayoutManager.getSpanCount() : 1;
                }
            });
            itemDecoration = new GridSpacingItemDecoration(nColumn, 8, true);

            recyclerView.addItemDecoration(itemDecoration);
            recyclerView.setLayoutManager(gridLayoutManager);





        }

    }

     @Override
    public void onPreview(Object obj) {

    }

    @Override
    public void toRemove(Object obj) {

    }

    @Override
    public void toEdit(long[] positions) {


        showAddMaterialPopup(modelId, Utils.toIntExact(positions[0]) ,true);

    }

    @Override
    public void onClick(View v, int position, boolean isLongClick) {

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

    public class loadMaterialDetailAsync extends AsyncTask<Object, Void,List<Object>> {

        protected void onPreExecute() {



            waitPoup = com.colorbuzztechgmail.spf.popup.waitPoup.newInstance(getString(R.string.dialog_loadModel));

            waitPoup.show(getSupportFragmentManager(), "waitPopup");


        }

        protected List<Object> doInBackground(Object[] ids) {


            final List<Material> materialList =new ArrayList<>();
            final List<Object> previewList =new ArrayList<>();

            int modelId=(Integer)ids[0];


            try {




                materialList.addAll(db.getModelMaterial(modelId));

                for (int i = 0; i < materialList.size(); i++) {

                    if(materialList.get(i).getCustomMaterialId()>0){

                        final long id=materialList.get(i).getCustomMaterialId();

                        materialList.get(i).setImage(db.getCustomMaterialImage(id));

                    }
                    previewList.add(materialList.get(i));
                }





                return previewList;

            } catch (Exception e) {

                e.printStackTrace();
                Log.e("Background", e.toString());


            }
            return previewList;
        }



        @Override
        protected void onPostExecute(List<Object> materials) {

            if(waitPoup!=null){
                waitPoup.dismiss();

            }


            if(!materials.isEmpty()){

                loadMaterial(materials);



            }else {

                Utils.toast(getApplicationContext(),"No se encontaron materiales");
            }


        }
    }

    public class updateMaterial extends AsyncTask<Object, Float,Boolean> {

        protected void onPreExecute() {



            waitPoup = com.colorbuzztechgmail.spf.popup.waitPoup.newInstance(getString(R.string.dialog_updateData));

            waitPoup.show(getSupportFragmentManager(), "waitPopup");


        }

        protected Boolean doInBackground(Object[] ids) {




            try {

               String materialName=(String)ids[0];
               int position=(int)ids[1];

                final Material material = materialDataset.getMaterial(position);

                material.setName(materialName);

                db.updateMaterial(modelId, material.getId(), materialName, material.getCustomMaterialId());

                for (Piece piece: db.getHighLightPiecesByMaterial(modelId,material.getId(),false)) {


                    db.updateMaterialAtPiece(piece, materialName);


                }


                return true;

            } catch (Exception e) {

                e.printStackTrace();
                Log.e("Background", e.toString());


            }
            return false;
        }

        protected void onProgressUpdate(Float... valores) {
            int p = Math.round(valores[0]);
            // ma.mProgressBar.setProgress(p);
        }

        @Override
        protected void onPostExecute(Boolean state) {

            if(waitPoup!=null){
                waitPoup.dismiss();

            }

            if (!state){
                 Utils.toast(getBaseContext(), "Error al actualizar");

            }else {
               loadData();

            }




        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        getMenuInflater().inflate(R.menu.menu_material,menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_addMaterial:

                showAddMaterialPopup(modelId,0,false);

                break;



        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {


        super.onStart();

    }

    public void showAddMaterialPopup(int mModelId,final int position, final boolean editable) {




        final int modelId=mModelId;

        LayoutInflater li=LayoutInflater.from(this);

        final View promptsView = li.inflate(R.layout.add_category_popup, null);

        final TextView titleText=(TextView) promptsView.findViewById(R.id.titleText);




        ((LinearLayout)promptsView.findViewById(R.id.di)).removeAllViewsInLayout();

        final EditText nametxt=new EditText(this);
        nametxt.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        ((LinearLayout)promptsView.findViewById(R.id.di)).addView(nametxt);

        if(editable){


            nametxt.setHint(materialDataset.getMaterial(position).getName());
            titleText.setText(getResources().getString(R.string.ediMaterial));
         }else{
            nametxt.setHint(getResources().getString(R.string.material_Name));
            titleText.setText(getResources().getString(R.string.addMaterial));

        }






        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);
        alertDialogBuilder.setView(promptsView);


        final Dialog alertDialog = alertDialogBuilder.create();

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();




        promptsView.findViewById(R.id.closeFrame).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();

            }
        });

        promptsView.findViewById(R.id.saveFrame).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String materialName =nametxt.getText().toString();
                materialName=materialName.trim();

                if(!materialName.equals("")) {

                    if (!db.existMaterial(modelId, materialName)) {

                        if (editable) {

                            Object[] obj=new Object[2];

                            obj[0]=materialName;
                            obj[1]=position;


                            new updateMaterial().execute(obj);

                            alertDialog.dismiss();


                        } else {


                            db.addMaterial(modelId, materialName, 0);

                            materialDataset.add(db.getMaterialByName(modelId, materialName));
                            alertDialog.dismiss();

                        }

                    } else {

                        Utils.toast(getBaseContext(),  getResources().getString(R.string.warning_exist));

                    }

                }else{



                    Utils.toast(getBaseContext(),getResources().getString(R.string.warning_emptyName));
                }


            }
        });





    }


    public void showRemoveDialog(final Material material){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        boolean enabled=true;

        if( material.getPieceCount()>0){

            enabled=false;
        }

        if(enabled){

            alertDialogBuilder
                    .setTitle(getResources().getString(R.string.dialogTitle_remove))
                    .setMessage(getResources().getString(R.string.dialogMsg_remove))
                    .setCancelable(false)
                    .setPositiveButton(getResources().getString(R.string.dialog_remove),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                   int pos=0;//materialDataset.getPositionById(material.getId());

                                    db.deleteMaterial(material.getModelId(), material.getId());
                                    materialDataset.remove(material);


                                    dialog.dismiss();

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

        }else{

            alertDialogBuilder

                    .setMessage( getResources().getString(R.string.warning_filledMaterial))
                    .setCancelable(true)

                    .setNegativeButton( getResources().getString(R.string.dialog_ok),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    dialog.dismiss();
                                }
                            }
                    );


        }

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_black_line));

        alertDialog.show();
    }






 }



