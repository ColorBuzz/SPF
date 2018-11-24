package com.colorbuzztechgmail.spf;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import com.colorbuzztechgmail.spf.dataset.Dataset;
import com.colorbuzztechgmail.spf.popup.waitPoup;

import java.util.ArrayList;
import java.util.List;

public class CutNoteListActivity extends AppCompatActivity implements  ItemActionListener,View.OnClickListener,Utils.onSavedInterface{


    private static final String TAB="TabFragment1";
    public RecyclerView recyclerItems,recyclerView;
    waitPoup waitPoup;
    Toolbar toolbar;
     private  List<Object> cutNoteList= new ArrayList<>();
    RecyclerSliderAdapter sliderAdapter;
    CutNoteListAdapter adapter1;
    CutNoteListDataset dataset;
    int modelId;
    RecyclerView.ItemDecoration itemDecoration ;
    public boolean viewMode=false;
    private String selectedStatus;


    public void setCutNoteList(List<CutNoteList> mCutNoteList) {

        cutNoteList.clear();

        for(CutNoteList list : mCutNoteList)
            cutNoteList.add(list);


     }

    public List<Object> getCutNoteList() {
        return cutNoteList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cutnotelist__fragment);

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
        adapter1 = new CutNoteListAdapter(this,this,true);
        adapter1.setViewMode(Utils.ViewMode.GRID);
        dataset=new CutNoteListDataset(recyclerView,adapter1);
        adapter1.dataset(dataset);
        ((ImageView)findViewById(R.id.imageView5)).setActivated(viewMode);

        selectedStatus =getResources().getString(R.string.action_sortAll);


        loadData();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cutnotelist,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {




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


        new loadCutNoteListStatusDetailAsync().execute(obj);
    }

    private void loadCutNoteList(String status){

        selectedStatus =status;

        if(status.equals(getResources().getString(R.string.action_sortAll))){
            setupRecyclerAllCutNoteList(cutNoteList);
        }else{

            final List<Object> list= new ArrayList<>();

            for(Object obj:cutNoteList){



                if(((CutNoteList)obj).getStatus().equals(Utils.convertStringToStatus(getApplicationContext(),status))){

                    list.add(obj);

                }


            }

            setupRecyclerAllCutNoteList(list);





        }

        toolbar.setTitle(getResources().getString(R.string.cutNotesList) + " " + "("+ String.valueOf(cutNoteList.size())+")");
        customView(viewMode);


    }

    private void loadMenu(){
        toolbar.setTitle(getResources().getString(R.string.piece_Amount) + " " + String.valueOf(cutNoteList.size()) );
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

    private void setupRecyclerAllCutNoteList(List<Object>cutNoteList){


        dataset.removeAll();
        recyclerView.swapAdapter(adapter1,true);
        dataset.addAll(cutNoteList);


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
        recyclerView.getRecycledViewPool().clear();

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
                                ArrayList<Long> ids=new ArrayList<>();

                                for(int i =state.size()-1;i>=0;i--){

                                    for(int j =dataset.size()-1;j>=0;j--){

                                     if(dataset.getObject(j) instanceof Piece){

                                         if( dataset.getCutNoteList(j).getId()==state.keyAt(i)){

                                             ids.add(dataset.getCutNoteList(j).getId());

                                             if(state.get(Utils.toIntExact(dataset.getCutNoteList(j).getId()))) {
                                                 dataset.removeIndex(j);
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

    private void showUndoSnackbar(final ArrayList<Long> ids){

        CoordinatorLayout view = (CoordinatorLayout)  findViewById(R.id.parentLayout);

        String mssg;



        mssg =getResources().getString(R.string.piece_Amount) + " eliminada";



        Snackbar snackbar = Snackbar.make(view, mssg, Snackbar.LENGTH_SHORT)
                .setAction(getResources().getString(R.string.action_undo), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {



                        loadCutNoteList(selectedStatus);


                     }

                }).setCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar snackbar, int dismissType) {
                        super.onDismissed(snackbar, dismissType);

                        if(dismissType !=DISMISS_EVENT_ACTION){

                            ModelDataBase db= new ModelDataBase(getApplicationContext());


                            for(int i =ids.size()-1;i>=0;i--){

                                for(int j =cutNoteList.size()-1;j>=0;j--){

                                    if(cutNoteList.get(j) instanceof CutNoteList){

                                        if( ((CutNote)cutNoteList.get(j)).getId()==ids.get(i)){

                                                db.deleteCutNoteList(ids.get(i));
                                                cutNoteList.remove(j);



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

        loadData();


    }

    @Override
    public void onPreview(Object obj) {

     if((obj instanceof Preview)){

         loadCutNoteList(((Preview)obj).getName());

         }else {
         CutNoteItemActivity.newInstaceItemActivity(BaseItemActivity.activityType.CUTNOTE,this,String.valueOf((long)obj), CategoryFragment.ChooserType.CUTNOTE_CUTNOTELIST,
                 Utils.ViewMode.LIST,getString(R.string.cutNotesList));
     }


    }

    @Override
    public void toRemove(Object obj) {

     showRemoveDialog(obj);

    }

    @Override
    public void toEdit(long [] ids) {

        int[] positions= new int[ids.length];

        for(int i=0;i<=ids.length-1;i++){

            positions[i]=dataset.sortedList.indexOf(dataset.getCutNoteListById(ids[i]));

        }

        final EditCutNoteList editCutNoteList=EditCutNoteList.newInstanceEditListCutNotePopUp(positions,ids,this);
        editCutNoteList.show(getSupportFragmentManager(),"edit_material");

    }

    @Override
    public void onClick(View v, int position, boolean isLongClick) {

    }

    public class loadCutNoteListStatusDetailAsync extends AsyncTask<Object, Void,List<Preview>> {

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



                List<CutNoteList>cutNoteLists=new ArrayList<>();


               cutNoteLists=db.getCutNoteListByModel(modelId);

               setCutNoteList(cutNoteLists);


                String[] statusList = getBaseContext().getResources().getStringArray(R.array.cutNotes_status);

                 Preview mAllTitle=new Preview(0,getResources().getString(R.string.action_sortAll),null);

                itemTable.add(mAllTitle);

                int count=1;
                for(String status :statusList) {

                    Drawable img=Utils.getImageAtStatus(getApplicationContext(),Utils.convertStringToStatus(getApplicationContext(),status));

                    final Preview mTitle=new Preview( count++,status,img);

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
                loadCutNoteList(getResources().getString(R.string.action_sortAll));


            }

            if(waitPoup!=null){
                waitPoup.dismiss();

            }


        }
    }



}