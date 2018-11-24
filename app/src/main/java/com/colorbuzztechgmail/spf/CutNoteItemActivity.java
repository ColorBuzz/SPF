package com.colorbuzztechgmail.spf;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.ObservableArrayMap;
import android.databinding.ViewDataBinding;
import android.os.AsyncTask;
import android.os.Parcel;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.util.LongSparseArray;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.colorbuzztechgmail.spf.CategoryFragment.ChooserType;
import com.colorbuzztechgmail.spf.adapter.BaseAdapter;
import com.colorbuzztechgmail.spf.databinding.CutNoteActivityBinding;
import com.colorbuzztechgmail.spf.popup.waitPoup;

import java.util.ArrayList;
import java.util.List;

import static com.colorbuzztechgmail.spf.BR.activity;
import static com.colorbuzztechgmail.spf.BR.map;
import static com.colorbuzztechgmail.spf.BR.obj;


/**
 * Created by PC01 on 21/06/2017.
 */
public class CutNoteItemActivity extends BaseItemActivity {


    public static final int NOTE_COUNT =7;
    public static final int PAIR_COUNT=8;

    private CutNoteDataset dataset;
    private CutNoteAdapter adapter;
    private ModelDataBase db;
    public CutNoteList cutNoteList;
    ViewDataBinding mBinding;
    ObservableArrayMap mObservableMap;





    @Override
    protected void initializeValues() {

        mBinding=  CutNoteActivityBinding.inflate(LayoutInflater.from(this));
        mObservableMap=new ObservableArrayMap();
        mObservableMap.put(PAIR_COUNT,0);
        mObservableMap.put(NOTE_COUNT,0);
        setContentView(mBinding.getRoot());

        db=new ModelDataBase(this);

        adapter = new CutNoteAdapter(this,this,true);

        this.dataset=new CutNoteDataset(mRecycler,getAdapter());

        setDataset( dataset);

       loadData();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_cutnotes,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {

            case R.id.action_search:

                loadSearchBar();

                return true;

            case R.id.action_sortState:
                item.setChecked(true);
                dataset.setSortType(CutNoteDataset.SortType.STATUS);

                dataset.changeSortType();

                break;
            case R.id.action_sortPosition:
                item.setChecked(true);
                dataset.setSortType(CutNoteDataset.SortType.POSITION);

                dataset.changeSortType();
                break;


            case R.id.action_sortSize:
                item.setChecked(true);
                dataset.setSortType(CutNoteDataset.SortType.SIZE);

                dataset.changeSortType();
                break;
          

            case R.id.action_title:

                collapseTitle=!collapseTitle;
                getAdapter().setCollapsedAllTitle(collapseTitle);

                item.setTitle(collapseTitle ?  getString( R.string.action_expand_title) :  getString(R.string.action_collapse_title));

                item.setIcon( collapseTitle ?  getDrawable(R.drawable.ic_unfold_more_white_24dp) : getDrawable(R.drawable.ic_unfold_less_white_24dp));

                return true;
        }


        return false;
    }

    @Override
    protected BaseAdapter getAdapter() {
        return adapter;
    }

    @Override
    protected List<Object> getRecentItems() {

        final List<Object> buffer=new ArrayList<>();
        return  buffer;
    }

    @Override
    protected List<Object> filterByParameter(String query) {
        final List<Object> filteredList = new ArrayList<>();



       // filteredList.addAll(db.getCutNoteListByCoincidenceValue(query));





        return filteredList;
    }

    @Override
    protected List<Object> filterType(String query, ChooserType type) {

        final List<Object> filteredModelList = new ArrayList<>();

        return filteredModelList;
    }

    @Override
    protected void showRemoveDialog(final LongSparseArray<Boolean> checkedList) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);
        alertDialogBuilder
                .setTitle( getResources().getString(R.string.dialogTitle_remove))
                //.setMessage( getResources().getString(R.string.dialogMsg_remove))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.dialog_remove),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                final ArrayList<Long> removeIds=new ArrayList<Long>();
                                int restCount=0;
                                if(checkedList.size()>0) {

                                    final List<Object>cutNotes=new ArrayList<>();
                                    for(int i=0;i<checkedList.size();i++) {

                                        if(checkedList.valueAt(i)) {


                                            final CutNote cutNote=dataset.getCutNoteById(checkedList.keyAt(i));

                                            cutNotes.add(cutNote);

                                            removeIds.add(checkedList.keyAt(i));

                                            restCount=restCount+cutNote.getPairCount();



                                        }
                                    }

                                    dataset.removeByIds(removeIds);
                                    dataset.deleteEmptyHeaders();
                                    refreshInfobar();
                                    adapter.multiChoiceHelper.clearChoices();
                                    dialog.dismiss();
                                    showUndoSnackbar(cutNotes);
                                    mObservableMap.put(NOTE_COUNT,dataset.getItemCount()-cutNotes.size());
                                    mObservableMap.put(PAIR_COUNT,(int)mObservableMap.get(PAIR_COUNT)-restCount);


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

    private void showUndoSnackbar(final List<Object> buffer){

        CoordinatorLayout view = findViewById(R.id.parentLayout);

        String mssg;

        if(buffer.size()==1){

            mssg = ((CutNote)buffer.get(0)).getModel() + " eliminados";

        }else {

            mssg = String.valueOf(buffer.size()) + " Notas " + "eliminados";

        }

        Snackbar snackbar = Snackbar.make(view, mssg, Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.action_undo), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        int pairCount=0;
                        for(Object obj:buffer){
                            dataset.addNewItem(obj);
                            pairCount=pairCount+((CutNote)obj).getPairCount();
                        }

                        mObservableMap.put(NOTE_COUNT,dataset.getItemCount());
                        mObservableMap.put(PAIR_COUNT,(int)mObservableMap.get(PAIR_COUNT)+pairCount);



                    }

                }).setCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar snackbar, int dismissType) {
                        super.onDismissed(snackbar, dismissType);

                        if(dismissType !=DISMISS_EVENT_ACTION){

                            for(Object obj:buffer){

                                ModelDataBase db= new ModelDataBase(getBaseContext());

                                db.deleteCutNote(((CutNote)obj).getId());


                            }


                        }

                    }
                });


        snackbar.show();
    }


    @Override
    protected void showEditDialog(long[] ids) {
        int[] positions= new int[ids.length];

        for(int i=0;i<ids.length;i++){

            positions[i]=dataset.getItemPositionByHash(dataset.getCutNoteById(ids[i]).hashCode());

        }

        final EditCutNote editCutNote=EditCutNote.newInstanceEditCutNotePopUp(positions,ids,this);
        editCutNote.show(getSupportFragmentManager(),"edit_material");

    }

    @Override
    protected void addNewItem() {

    }

    @Override
    protected boolean isObjectVisible(Object obj) {



       return true;
    }

    @Override
    protected boolean isCategoryVisible(String category) {
        return false;
    }

    @Override
    public void onPreview(Object obj) {
        Utils.showInfoPopUp(this,obj);

    }

    @Override
    public void onClick(View v, int position, boolean isLongClick) {


    }

    private void loadData(){


        Object[] obj=new Object[1];

        if(getSelectedCategory()!=null){
            obj[0]=getSelectedCategory();


        }



        new loadCutNotesAsync().execute(obj);
    }

    @Override
    public void fillDataset(List<Object> models) {

        dataset.removeAll();
        dataset.AutomaticTitleGeneratorSortType(models,true);



    }

    private void bindView(){



        myToolbar.setSubtitle("REF:" + " " + String.valueOf(cutNoteList.getReference()));
        mBinding.setVariable(activity,this);
        mBinding.setVariable(map,mObservableMap);
        mBinding.setVariable(obj,cutNoteList);
        mBinding.notifyChange();
        mBinding.executePendingBindings();




    }


    public class loadCutNotesAsync extends AsyncTask<Object,Void,List<Object>> {


        waitPoup waitPoup;

        protected void onPreExecute() {



            waitPoup = com.colorbuzztechgmail.spf.popup.waitPoup.newInstance(getString(R.string.dialog_loadModel));

            waitPoup.show(getSupportFragmentManager(), "waitPopup");


        }

        protected List<Object> doInBackground(Object[] ids) {


            //pos 0 -modelID

            String id=(String) ids[0];
            List<Object> objs= new ArrayList<>();

            ModelDataBase db = new ModelDataBase(getApplicationContext());

            try {




                        objs.addAll(db.getCutNoteByCuteNoteListId(Long.valueOf(id)));
                        cutNoteList=db.getCutNoteListById(Utils.toIntExact(Long.valueOf(id)));




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

                fillDataset(cutNotes);
                mObservableMap.put(NOTE_COUNT,cutNotes.size());
                mObservableMap.put(PAIR_COUNT,cutNoteList.getTotalPairCount());
                bindView();

            }

            if(waitPoup!=null){
                waitPoup.dismiss();

            }


        }
    }

    @Override
    protected Object getObjectToLoad(Cursor mCursor) {

        return db.createCutNote(mCursor);
    }
}