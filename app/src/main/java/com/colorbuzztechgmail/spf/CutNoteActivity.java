package com.colorbuzztechgmail.spf;

import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.LongSparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.colorbuzztechgmail.spf.databinding.CutNoteActivityBinding;
import com.colorbuzztechgmail.spf.dataset.Dataset;
import com.colorbuzztechgmail.spf.popup.waitPoup;

import java.util.ArrayList;
import java.util.List;

import static com.colorbuzztechgmail.spf.BR.activity;
import static com.colorbuzztechgmail.spf.BR.fragment;
import static com.colorbuzztechgmail.spf.BR.obj;

public class CutNoteActivity extends AppCompatActivity implements  ItemActionListener,View.OnClickListener,Utils.onSavedInterface{


    public RecyclerView mRecycler;
    waitPoup waitPoup;
    Toolbar toolbar;
    public boolean expandTitle=false;

    CutNoteAdapter cutNoteAdapter;
    CutNoteDataset cutNoteDataset;
    CutNoteList cutNoteList;
    int id;
    HeaderItemDecoration headerItemDecoration;

      public Utils.ViewMode viewMode= Utils.ViewMode.LIST;
     public enum CutNoteActivityMode{

         MODEL,
         CUTNOTE_LIST



     }

     private CutNoteActivityMode mode;
     ViewDataBinding mBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding=  CutNoteActivityBinding.inflate(LayoutInflater.from(this));

        setContentView(mBinding.getRoot());

      id=Integer.valueOf(getIntent().getStringExtra("ID"));
      mode=CutNoteActivityMode.valueOf(getIntent().getStringExtra("MODE"));

       toolbar = (Toolbar) findViewById(R.id.toolbar);
       setSupportActionBar(toolbar);



         mRecycler=(RecyclerView)findViewById(R.id.previewRecycler);
       // recyclerView.getRecycledViewPool().setMaxRecycledViews(CutNoteCutNoteAdapter.ITEM,0);



        loadData();







    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cutnotes,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {



            case R.id.action_sortState:
                item.setChecked(true);
                cutNoteDataset.setSortType(CutNoteDataset.SortType.STATUS);

                cutNoteDataset.changeSortType();

                break;
            case R.id.action_sortPosition:
                item.setChecked(true);
                cutNoteDataset.setSortType(CutNoteDataset.SortType.POSITION);

               cutNoteDataset.changeSortType();
                 break;


            case R.id.action_sortSize:
                item.setChecked(true);
                cutNoteDataset.setSortType(CutNoteDataset.SortType.SIZE);

                cutNoteDataset.changeSortType();
                 break;
            case R.id.action_title:

                expandTitle=!expandTitle;
                cutNoteAdapter.setCollapsedAllTitle(expandTitle);

                item.setTitle(expandTitle ?  getString(R.string.action_collapse_title) : getString( R.string.action_expand_title));

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {

        super.onStart();

    }

    public void loadData(){


        Object[] obj=new Object[1];

        obj[0]=id;


        new loadCutNotesAsync().execute(obj);
    }


    private void bindView(){



        toolbar.setSubtitle("REF:" + " " + String.valueOf(cutNoteList.getReference()));
        mBinding.setVariable(activity,this);
        mBinding.setVariable(obj,cutNoteList);
        mBinding.notifyChange();
        mBinding.executePendingBindings();




    }

    private void loadCutNote(List<Object> cutNotes){

    
            setupRecycler(cutNotes);

        customView(viewMode);

        loadMenu();



    }

    private void loadMenu(){


        toolbar.setTitle( getResources().getString(R.string.cutNotesList) );

            toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_close_white_24dp));

        toolbar.setTitleTextColor(getResources().getColor(R.color.iconsColor));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }

        });

    }

    private void setupRecycler(List<Object>cutNotes){

        cutNoteAdapter= new CutNoteAdapter(this,this,false);
        cutNoteAdapter.setViewMode(viewMode);

        cutNoteDataset=new CutNoteDataset(mRecycler,cutNoteAdapter);

        cutNoteAdapter.dataset(cutNoteDataset);


        headerItemDecoration=new HeaderItemDecoration(mRecycler,cutNoteAdapter);

        mRecycler.addItemDecoration(headerItemDecoration);

        mRecycler.swapAdapter(cutNoteAdapter,true);

        switch (mode){

            case CUTNOTE_LIST:
                cutNoteDataset.AutomaticTitleGeneratorSortType(cutNotes,false);

                break;


            case MODEL:
                cutNoteDataset.AutomaticTitleGeneratorSortType(cutNotes,true);

                break;

        }

    }

    public void customView(Utils.ViewMode viewMode) {

        int spacing=Utils.pxTodp(8,this);

        switch (viewMode){

            case LIST:




                TitleListItemDecoration decoration= new TitleListItemDecoration(spacing,cutNoteAdapter,1,false);
                mRecycler.addItemDecoration(decoration);
                final LinearLayoutManager lm = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
                lm.setAutoMeasureEnabled(false);



                mRecycler.setLayoutManager(lm);

                break;


            case GRID:

                break;





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

                                LongSparseArray<Boolean> state= (LongSparseArray<Boolean>) object;

                                for(int i =state.size()-1;i>=0;i--){

                                    for(int j =cutNoteDataset.size()-1;j>=0;j--){

                                     if(cutNoteDataset.getObject(j) instanceof CutNote){

                                         if( cutNoteDataset.getCutNote(j).getId()==state.keyAt(i)){

                                             if(state.get(cutNoteDataset.getCutNote(j).getId())) {
                                                 cutNoteDataset.removeIndex(j);
                                             }


                                         }

                                     }



                                    }



                                }





                                dialog.dismiss();
                                showUndoSnackbar(state);

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

    private void showUndoSnackbar(final LongSparseArray<Boolean> state){

        CoordinatorLayout view = (CoordinatorLayout)  findViewById(R.id.parentLayout);

        String mssg;



        mssg =getResources().getString(R.string.piece_Amount) + " eliminada";



        Snackbar snackbar = Snackbar.make(view, mssg, Snackbar.LENGTH_SHORT)
                .setAction(getResources().getString(R.string.action_undo), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


/*

                        loadCutNote(cutNoteDataset.get);
*/


                     }

                }).setCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar snackbar, int dismissType) {
                        super.onDismissed(snackbar, dismissType);

                        if(dismissType !=DISMISS_EVENT_ACTION){

                            ModelDataBase db= new ModelDataBase(getApplicationContext());


                            for(int i =state.size()-1;i>=0;i--){

                                for(int j =cutNoteDataset.size()-1;j>=0;j--){

                                    if(cutNoteDataset.getObject(j) instanceof CutNote){

                                        if( ((CutNote)cutNoteDataset.getObject(j)).getId()==state.keyAt(i)){

                                            if(state.get(state.keyAt(i))) {

                                                state.delete(state.keyAt(i));

                                            }


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

        }
    }

    @Override
    public void onSaved(Object obj, int position, boolean isEditable) {

        loadData();


    }

    @Override
    public void onPreview(Object obj) {

        Utils.showInfoPopUp(this,obj);

    }

    @Override
    public void toRemove(Object obj) {

     showRemoveDialog(obj);

    }

    @Override
    public void toEdit(long [] ids) {



    }

    @Override
    public void onClick(View v,final int position, boolean isLongClick) {

        if(!isLongClick) {

            switch (v.getId()) {

                case R.id.imageViewProfile:


                    showStatusPopup(v, position);


                    break;


                default:

                    onPreview(cutNoteDataset.getCutNote(position));

                    break;


            }
        }

    }

    public class loadCutNotesAsync extends AsyncTask<Object,Void,List<Object>> {

        protected void onPreExecute() {



               waitPoup = com.colorbuzztechgmail.spf.popup.waitPoup.newInstance(getString(R.string.dialog_loadModel));

               waitPoup.show(getSupportFragmentManager(), "waitPopup");


        }

        protected List<Object> doInBackground(Object[] ids) {


            //pos 0 -modelID

             int id=(Integer)ids[0];
            List<Object> objs= new ArrayList<>();

            ModelDataBase db = new ModelDataBase(getApplicationContext());

            try {


               switch (mode){

                   case MODEL:

                       final List<CutNote>aux=new ArrayList<>();

                       for(CutNoteList list : db.getCutNoteListByModel(id)){
                           aux.addAll(db.getCutNoteByCuteNoteListId(list.getId()));

                       }

                       objs.addAll(aux);

                       break;


                   case CUTNOTE_LIST:
                       objs.addAll(db.getCutNoteByCuteNoteListId(id));
                       cutNoteList=db.getCutNoteListById(id);

                       break;


               }





                return objs;

            } catch (Exception e) {

                e.printStackTrace();
                Log.e("Background", e.toString());


            }
            return objs;
        }


        @Override
        protected void onPostExecute(List<Object> cutNotes) {



            if(cutNotes.isEmpty()){



                Utils.toast(getApplicationContext(),"Vacio");
            }else {

                if(mode.equals(CutNoteActivityMode.CUTNOTE_LIST))
                    bindView();

                 loadCutNote(cutNotes);

            }

            if(waitPoup!=null){
                waitPoup.dismiss();

            }


        }
    }

    public void showStatusPopup(View v,final int position) {

         final ListPopupWindow popupWindow = new ListPopupWindow(this);

            List<ListPopUpMenuItem> itemList = new ArrayList<>();
            itemList.add(new ListPopUpMenuItem(getString(R.string.cutNotes_status_finished),  Utils.getImageAtStatus(getBaseContext(),Utils.convertStringToStatus(getBaseContext(),
                    getString(R.string.cutNotes_status_finished)))));

            itemList.add(new ListPopUpMenuItem(getString(R.string.cutNotes_status_in_progress),Utils.getImageAtStatus(getBaseContext(),Utils.convertStringToStatus(getBaseContext(),
                    getString(R.string.cutNotes_status_in_progress)))));
            itemList.add(new ListPopUpMenuItem(getString(R.string.cutNotes_status_indeterminated),Utils.getImageAtStatus(getBaseContext(),Utils.convertStringToStatus(getBaseContext(),
                    getString(R.string.cutNotes_status_indeterminated)))));


            final ListPopupWindowAdapter adapter = new ListPopupWindowAdapter(this, itemList);
            popupWindow.setAnchorView(v);
            popupWindow.setListSelector(getResources().getDrawable(R.drawable.checkedlayout));
            popupWindow.setContentWidth((int) (150 * getResources().getDisplayMetrics().density + 0.5f));
            popupWindow.setAdapter(adapter);
            popupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    popupWindow.dismiss();

                    final ModelDataBase db = new ModelDataBase(getBaseContext());

                     CutNote cutNote = cutNoteDataset.getCutNote(position);
                    Log.d("POSTION: ", String.valueOf(position));
                    if (cutNote != null) {


                        switch (i) {

                            case 0:

                                cutNote.setStatus(CutNote.cutNoteStatus.FINISHED);


                                break;
                            case 1:
                                cutNote.setStatus(CutNote.cutNoteStatus.IN_PROGRESS);
                                 break;
                            case 2:
                                cutNote.setStatus(CutNote.cutNoteStatus.INDETERMINATE);


                                break;


                        }

                         db.updateCutNote(cutNote);

;                        cutNoteDataset.editItem(cutNote,position);



                    }
                }
            });
            popupWindow.show();



    }



}