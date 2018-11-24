package com.colorbuzztechgmail.spf;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.util.LongSparseArray;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.colorbuzztechgmail.spf.CategoryFragment.ChooserType;
import com.colorbuzztechgmail.spf.adapter.BaseAdapter;
import com.colorbuzztechgmail.spf.popup.AddCutNoteListPopUp;

import java.util.ArrayList;
import java.util.List;

import static com.colorbuzztechgmail.spf.ModelDataBase.CUTNOTE_COLUMN_REFERENCE;
import static com.colorbuzztechgmail.spf.ModelDataBase.CUTNOTE_COLUMN_STATUS;
import static com.colorbuzztechgmail.spf.ModelDataBase.KEY_CUTNOTE_LIST_ID;
import static com.colorbuzztechgmail.spf.ModelDataBase.KEY_ID;
import static com.colorbuzztechgmail.spf.ModelDataBase.KEY_MODEL_ID;
import static com.colorbuzztechgmail.spf.ModelDataBase.MODEL_COLUMN_NAME;
import static com.colorbuzztechgmail.spf.ModelDataBase.TABLE_CUNOTE_LIST;
import static com.colorbuzztechgmail.spf.ModelDataBase.TABLE_MODEL;
import static com.colorbuzztechgmail.spf.ModelDataBase.TABLE_MODEL_CUTNOTE_LIST_RELATIONS;


/**
 * Created by PC01 on 21/06/2017.
 */
public class CutNotesListItemActivity extends BaseItemActivity  {

    private CutNoteListDataset dataset;
    private CutNoteListAdapter adapter;
    private ModelDataBase db;



    @Override
    protected void initializeValues() {



        COLUMN_ORDER= CUTNOTE_COLUMN_STATUS;

        db=new ModelDataBase(this);

        adapter = new CutNoteListAdapter(this,this,true);

        this.dataset=new CutNoteListDataset(mRecycler,getAdapter());

        setDataset( dataset);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_search_cutnotelist,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {

            case R.id.action_search:

                loadSearchBar();

                return true;
            case R.id.action_sortModel:
                item.setChecked(true);
                dataset.setSortType(CutNoteListDataset.SortType.MODEL);
                COLUMN_ORDER=CUTNOTE_COLUMN_REFERENCE;
                filterType(getSelectedCategory(),getChooserType());
                return true;

            case R.id.action_sortState:
                item.setChecked(true);
                dataset.setSortType(CutNoteListDataset.SortType.STATUS);
                COLUMN_ORDER=CUTNOTE_COLUMN_STATUS;
                filterType(getSelectedCategory(),getChooserType());

                return true;

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
        buffer.addAll(db.getRecentCutNoteList(lastImportsCount));
        return  buffer;
    }

    @Override
    protected List<Object> filterByParameter(String query) {
        final List<Object> filteredList = new ArrayList<>();



        filteredList.addAll(db.getCutNoteListByCoincidenceValue(query));





        return filteredList;
    }

    @Override
    protected List<Object> filterType(String query, ChooserType type) {
        String selectQuery;
        closeCursor();


        switch (type) {

            case CUTNOTE_STATUS:
                selectQuery = "SELECT  * FROM " + TABLE_CUNOTE_LIST + " tcl "+
                        " WHERE tcl." + CUTNOTE_COLUMN_STATUS + " = '" + query +"'";
                break;


            case CUTNOTE_MODEL:

                selectQuery = "SELECT  * FROM " + TABLE_CUNOTE_LIST + " tcl, "
                        + TABLE_MODEL + " tm, "
                        + TABLE_MODEL_CUTNOTE_LIST_RELATIONS+ " tmcl WHERE tm."
                        + MODEL_COLUMN_NAME + " = '" + query + "'" + " AND tm." + KEY_ID
                        + " = " + "tmcl." + KEY_MODEL_ID + " AND tcl." + KEY_ID + " = " + "tmcl." + KEY_CUTNOTE_LIST_ID ;

                break;


            default:

                selectQuery = "SELECT  * FROM " + TABLE_CUNOTE_LIST;


                break;

        }

        mCursor  = db.getReadableDatabase().rawQuery(selectQuery + " ORDER BY " + COLUMN_ORDER + " COLLATE NOCASE "
                , null);

        dataset.removeAll();
        newLoadAsyncTask();
        return null;
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
                                if(checkedList.size()>0) {

                                    final List<CutNoteList>cutNotes=new ArrayList<>();
                                    for(int i=0;i<checkedList.size();i++) {

                                        if(checkedList.valueAt(i)) {


                                            final CutNoteList cutNoteList=dataset.getCutNoteListById(checkedList.keyAt(i));

                                            cutNotes.add(cutNoteList);

                                            removeIds.add(checkedList.keyAt(i));



                                        }
                                    }

                                    dataset.removeByIds(removeIds);
                                    dataset.deleteEmptyHeaders();
                                    refreshInfobar();
                                    adapter.multiChoiceHelper.clearChoices();
                                    dialog.dismiss();
                                    showUndoSnackbar(cutNotes);

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

    private void showUndoSnackbar(final List<CutNoteList> cutNoteList){

        CoordinatorLayout view = findViewById(R.id.parentLayout);

        String mssg;

        if(cutNoteList.size()==1){

            mssg = cutNoteList.get(0).getModel() + " eliminados";

        }else {

            mssg = String.valueOf(cutNoteList.size()) + " Notas " + "eliminados";

        }

        Snackbar snackbar = Snackbar.make(view, mssg, Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.action_undo), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dataset.add(cutNoteList);
                        refreshInfobar();

                    }

                }).setCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar snackbar, int dismissType) {
                        super.onDismissed(snackbar, dismissType);

                        if(dismissType !=DISMISS_EVENT_ACTION){

                            for(CutNoteList cutNotes: cutNoteList){

                                ModelDataBase db= new ModelDataBase(getBaseContext());

                                db.deleteCutNoteList(cutNotes.getId());


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

            positions[i]=dataset.getItemPositionByHash(dataset.getCutNoteListById(ids[i]).hashCode());

        }

        final EditCutNoteList editCutNoteList=EditCutNoteList.newInstanceEditListCutNotePopUp(positions,ids,this);
        editCutNoteList.show(getSupportFragmentManager(),"edit_material");

    }

    @Override
    protected void addNewItem() {

    }

    @Override
    protected boolean isObjectVisible(Object obj) {

        final CutNoteList cutNoteList=(CutNoteList)obj;
        if(!(getSelectedCategory().equals(getString(R.string.importRecent_Cat)))) {
            switch (getChooserType()) {
                //Tipo
                case CUTNOTE_STATUS:
                    if ((String.valueOf(cutNoteList.getStatus())).equals(getSelectedCategory())) {


                        return true;


                    }
                    return false;
                case CUTNOTE_MODEL:

                    if ((String.valueOf(cutNoteList.getModel()).equals(getSelectedCategory()))) {


                        return true;


                    }
                    return false;


                default:

                    return true;
            }

        }

        return true;
    }

    @Override
    protected boolean isCategoryVisible(String category) {
        return false;
    }

    @Override
    public void onPreview(Object obj) {

       /* Intent c = new Intent(this,CutNoteActivity.class);
        c.putExtra("ID",String.valueOf((long)obj));
        c.putExtra("MODE",CutNoteActivity.CutNoteActivityMode.CUTNOTE_LIST.name());

        startActivity(c);*/

        CutNoteItemActivity.newInstaceItemActivity(BaseItemActivity.activityType.CUTNOTE,this,String.valueOf((long)obj), ChooserType.CUTNOTE_CUTNOTELIST,
                Utils.ViewMode.LIST,getString(R.string.cutNotesList));
    }

    @Override
    public void onClick(View v, int position, boolean isLongClick) {


    }

    @Override
    public void selectCategory(String parameter, CategoryFragment.ChooserType type) {
        setSelectedCategory(parameter);

        filterType(parameter,type);
    }

    @Override
    public void fillDataset(List<Object> items) {

        if (!items.isEmpty()) {

            if(dataset.size()==0){
                dataset.AutomaticTitleGeneratorSortType(items, false);

            }else{

                for (Object obj:items){

                    dataset.addNewItem(obj,false);
                }


            }

        }

    }

    @Override
    protected Object getObjectToLoad(Cursor mCursor) {

        return db.createCutNoteList(mCursor);
    }
}