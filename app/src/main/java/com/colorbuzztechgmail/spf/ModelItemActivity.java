package com.colorbuzztechgmail.spf;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v4.util.LongSparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.colorbuzztechgmail.spf.adapter.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.colorbuzztechgmail.spf.ModelDataBase.CUSTUMER_COLUMN_NAME;
import static com.colorbuzztechgmail.spf.ModelDataBase.DIRECTORY_COLUMN_NAME;
import static com.colorbuzztechgmail.spf.ModelDataBase.KEY_CUSTUMER_ID;
import static com.colorbuzztechgmail.spf.ModelDataBase.KEY_DIRECTORY_ID;
import static com.colorbuzztechgmail.spf.ModelDataBase.KEY_ID;
import static com.colorbuzztechgmail.spf.ModelDataBase.KEY_MODEL_ID;
import static com.colorbuzztechgmail.spf.ModelDataBase.MODEL_COLUMN_DATE;
import static com.colorbuzztechgmail.spf.ModelDataBase.MODEL_COLUMN_NAME;
import static com.colorbuzztechgmail.spf.ModelDataBase.TABLE_CUSTUMER;
import static com.colorbuzztechgmail.spf.ModelDataBase.TABLE_DIRECTORY;
import static com.colorbuzztechgmail.spf.ModelDataBase.TABLE_MODEL;
import static com.colorbuzztechgmail.spf.ModelDataBase.TABLE_MODEL_CUSTUMER_RELATIONS;
import static com.colorbuzztechgmail.spf.ModelDataBase.TABLE_MODEL_DIRECTORY_RELATIONS;

public class ModelItemActivity extends BaseItemActivity {


    private ModelDataset dataset;
    private ModeListAdapter adapter;
    private ModelDataBase db;
    private BaseAdapter searchAdapter;





    @Override
    protected void initializeValues() {
         GROUPED_VALUE="";
         COLUMN_ORDER=MODEL_COLUMN_NAME;

        db = new ModelDataBase(this);
        adapter = new ModeListAdapter(this, this, true);
        this.dataset = new ModelDataset(mRecycler, adapter);
        setDataset(this.dataset);
    }

    @Override
    protected BaseAdapter getAdapter() {
        return adapter;
    }

    @Override
    protected List<Object> getRecentItems() {

        final List<Object> buffer = new ArrayList<>();

        buffer.addAll(db.getAddRecentModel(lastImportsCount));
        return buffer;
    }

    @Override
    protected List<Object> filterByParameter(String query) {

        return null;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        String selectQuery;

        COLUMN_ORDER=MODEL_COLUMN_NAME;
        closeCursor();


        selectQuery = "SELECT  * FROM " + TABLE_MODEL + " m, " + TABLE_CUSTUMER + " c, "
                + TABLE_DIRECTORY + " d, " + TABLE_MODEL_DIRECTORY_RELATIONS + " tdm, "
                + TABLE_MODEL_CUSTUMER_RELATIONS +" tcm"
                + " WHERE d." + KEY_ID + " = " + "tdm." + KEY_DIRECTORY_ID
                + " AND m." + KEY_ID + " = " + "tdm." + KEY_MODEL_ID
                + " AND tcm." + KEY_CUSTUMER_ID + " = c." + KEY_ID
                + " AND tcm." + KEY_MODEL_ID + " = m." + KEY_ID
                + " OR d." + DIRECTORY_COLUMN_NAME +  " LIKE " +"'" + query + "'"
                + " OR c." + CUSTUMER_COLUMN_NAME  + " LIKE " + "'" + query + "'"
                + " OR m." + MODEL_COLUMN_NAME  +  " LIKE " + "'" + query + "'";

        mCursor  = db.getReadableDatabase().rawQuery(selectQuery + " ORDER BY " + COLUMN_ORDER + " COLLATE NOCASE "
                , null);
        dataset.removeAll();




            newLoadAsyncTask();


        return true;
    }

    @Override
    protected List<Object> filterType(String query, CategoryFragment.ChooserType type) {

        String selectQuery;
       closeCursor();

        switch (type) {


            case MODEL_DIRECTORY:

                selectQuery = "SELECT  * FROM " + TABLE_MODEL + " m, " + TABLE_CUSTUMER + " c, "
                        + TABLE_DIRECTORY + " d, " + TABLE_MODEL_DIRECTORY_RELATIONS + " tdm, "
                        + TABLE_MODEL_CUSTUMER_RELATIONS +" tcm"
                        +" WHERE d." + DIRECTORY_COLUMN_NAME + " = '" + query + "'"
                        + " AND d." + KEY_ID + " = " + "tdm." + KEY_DIRECTORY_ID
                        + " AND m." + KEY_ID + " = " + "tdm." + KEY_MODEL_ID
                        + " AND tcm." + KEY_CUSTUMER_ID + " = c." + KEY_ID
                        + " AND tcm." + KEY_MODEL_ID + " = m." + KEY_ID;



                break;


            case MODEL_CUSTUMER:

                 selectQuery = "SELECT  * FROM " + TABLE_MODEL + " m, " + TABLE_DIRECTORY + " d, " + TABLE_MODEL_DIRECTORY_RELATIONS + " tdm, "
                        + TABLE_CUSTUMER + " c, " + TABLE_MODEL_CUSTUMER_RELATIONS +" tcm"
                        + " WHERE c." + CUSTUMER_COLUMN_NAME + " = '" + query + "'"
                        + " AND c." + KEY_ID + " = " + "tcm." + KEY_CUSTUMER_ID
                        + " AND m." + KEY_ID + " = " + "tcm." + KEY_MODEL_ID
                        + " AND tdm." + KEY_MODEL_ID + " = m." + KEY_ID
                        + " AND tdm." + KEY_DIRECTORY_ID + " = d." + KEY_ID;

                  break;


            default:
                selectQuery = "SELECT  * FROM " + TABLE_MODEL;


                break;


        }


        mCursor  = db.getReadableDatabase().rawQuery(selectQuery +  GROUPED_VALUE + " ORDER BY " + COLUMN_ORDER + " COLLATE NOCASE "
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
                .setTitle(getResources().getString(R.string.dialogTitle_remove))
                //.setMessage( getResources().getString(R.string.dialogMsg_remove))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.dialog_remove),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                final ArrayList<Long> removeIds = new ArrayList<>();
                                if (checkedList.size() > 0) {

                                    final List<PreviewModelInfo> models = new ArrayList<>();
                                    for (int i = 0; i < checkedList.size(); i++) {

                                        if (checkedList.valueAt(i)) {


                                            final PreviewModelInfo model = dataset.getPreviewModelInfoById(checkedList.keyAt(i));

                                            models.add(model);

                                            removeIds.add(checkedList.keyAt(i));


                                        }
                                    }

                                    dataset.removeByIds(removeIds);
                                    refreshInfobar();
                                    adapter.multiChoiceHelper.clearChoices();
                                    dialog.dismiss();
                                    showUndoSnackbar(models);

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

    private void showUndoSnackbar(final List<PreviewModelInfo> modelList) {

        CoordinatorLayout view = findViewById(R.id.parentLayout);

        String mssg;

        if (modelList.size() == 1) {

            mssg = modelList.get(0).getName() + " eliminados";

        } else {

            mssg = String.valueOf(modelList.size()) + " Modelos " + "eliminados";

        }

        Snackbar snackbar = Snackbar.make(view, mssg, Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.action_undo), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dataset.add(modelList);
                        refreshInfobar();

                    }

                }).setCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar snackbar, int dismissType) {
                        super.onDismissed(snackbar, dismissType);

                        if (dismissType != DISMISS_EVENT_ACTION) {

                            for (PreviewModelInfo model : modelList) {

                                db.deleteModelData(model.getId());


                            }
                            db.closeDB();

                        }

                    }
                });


        snackbar.show();
    }

    @Override
    protected void showEditDialog(long[] id) {


        int[] positions = new int[id.length];

        for (int i = 0; i < id.length; i++) {

            positions[i] = dataset.sortedList.indexOf(dataset.getPreviewModelInfoById(id[i]));

        }

        final EditModel editModel = EditModel.newInstanceEditModelPopUp(positions, id, true, this);
        editModel.show(getSupportFragmentManager(), "editModel");


    }

    @Override
    protected void addNewItem() {


        final Handler mDrawerActionHandler = new Handler();
        final long DRAWER_CLOSE_DELAY_MS = 165;


        mDrawerActionHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, new TabSearchFragment())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .addToBackStack(null)
                        .commit();


            }
        }, DRAWER_CLOSE_DELAY_MS);//((MainActivity)getActivity()).setItemNavigationView(0);

    }

    @Override
    protected boolean isObjectVisible(Object obj) {


        if (!getSelectedCategory().equals(getString(R.string.importRecent_Cat))) {

            switch (getChooserType()) {
                //Tipo
                case MODEL_DIRECTORY:
                    if (((PreviewModelInfo) obj).getDirectory().equals(getSelectedCategory())) {


                        return true;


                    }
                    return false;


                case MODEL_CUSTUMER:

                    if (((PreviewModelInfo) obj).getCustumer().equals(getSelectedCategory())) {


                        return true;


                    }
                    return false;


                default:

                    return false;

            }
        }
        return true;
    }

    @Override
    protected boolean isCategoryVisible(String category) {
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search_model, menu);


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

            case R.id.action_search:

                loadSearchBar();

                return true;

            case R.id.action_sortName:
                item.setChecked(true);
                dataset.setSortType(ModelDataset.ModelSortType.NAME);
                COLUMN_ORDER=MODEL_COLUMN_NAME;
                GROUPED_VALUE="";
                filterType(getSelectedCategory(),getChooserType());
                // dataset.changeSortType();
                return true;

            case R.id.action_sortCustumer:
                item.setChecked(true);
                dataset.setSortType(ModelDataset.ModelSortType.CUSTUMER);

                COLUMN_ORDER=CUSTUMER_COLUMN_NAME;
                GROUPED_VALUE=" GROUP BY " + MODEL_COLUMN_NAME;
                filterType(getSelectedCategory(),getChooserType());

                return true;

            case R.id.action_sortCategory:
                item.setChecked(true);
                dataset.setSortType(ModelDataset.ModelSortType.DIRECTORY);

                COLUMN_ORDER=DIRECTORY_COLUMN_NAME;
                GROUPED_VALUE=" GROUP BY "+MODEL_COLUMN_NAME;
                filterType(getSelectedCategory(),getChooserType());

                return true;

            case R.id.action_sortDate:
                item.setChecked(true);
                dataset.setSortType(ModelDataset.ModelSortType.LAST);
                COLUMN_ORDER=KEY_MODEL_ID;
                GROUPED_VALUE="";

                filterType(getSelectedCategory(),getChooserType());


                 return true;

            case R.id.action_view:
                ViewMode = ViewMode.equals(Utils.ViewMode.LIST) ? Utils.ViewMode.GRID : Utils.ViewMode.LIST;

                item.setIcon(ViewMode.equals(Utils.ViewMode.LIST) ? getResources().getDrawable(R.drawable.ic_view_module_white_24dp) : getResources().getDrawable(R.drawable.ic_view_list_white_24dp));

                customView();
                return true;

            case R.id.action_title:

                collapseTitle = !collapseTitle;
                getAdapter().setCollapsedAllTitle(collapseTitle);

                item.setTitle(collapseTitle ? getString(R.string.action_expand_title) : getString(R.string.action_collapse_title));

                return true;

        }


        return false;
    }

    @Override
    public void onClick(View v, int position, boolean isLongClick) {
        switch (v.getId()) {

            default:

                Intent i = new Intent(this, ModelActivity.class);
                i.putExtra("MODEL", String.valueOf(dataset.getPreviewModelInfo(position).getId()));
                startActivity(i);
                break;


        }
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

       return db.createPreviewModel(mCursor);

    }


    @Override
    public void startSupportActionModeIfNeeded() {
        super.startSupportActionModeIfNeeded();

        searchAdapter=new ModeListAdapter(this,this,false);
        dataset=new ModelDataset(mRecycler,searchAdapter);

        mRecycler.swapAdapter(searchAdapter,true);



    }

    @Override
    public void finishSupportActionMode() {

        closeCursor();
        dataset=(ModelDataset) adapter.dataset;
        mRecycler.swapAdapter(adapter,true);
        super.finishSupportActionMode();

    }
}
