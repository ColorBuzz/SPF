package com.colorbuzztechgmail.spf;

import android.content.DialogInterface;
import android.database.Cursor;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v4.util.LongSparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.colorbuzztechgmail.spf.adapter.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.colorbuzztechgmail.spf.ModelDataBase.CUSTOM_MATERIAL_COLUMN_NAME;
import static com.colorbuzztechgmail.spf.ModelDataBase.CUSTOM_MATERIAL_COLUMN_SEASONS;
import static com.colorbuzztechgmail.spf.ModelDataBase.DEALER_COLUMN_NAME;
import static com.colorbuzztechgmail.spf.ModelDataBase.KEY_CUSTOM_MATERIAL_ID;
import static com.colorbuzztechgmail.spf.ModelDataBase.KEY_DEALER_ID;
import static com.colorbuzztechgmail.spf.ModelDataBase.KEY_ID;
import static com.colorbuzztechgmail.spf.ModelDataBase.KEY_MATERIAL_TYPE_ID;
import static com.colorbuzztechgmail.spf.ModelDataBase.KEY_MODEL_ID;
import static com.colorbuzztechgmail.spf.ModelDataBase.MATERIAL_TYPE_COLUMN_NAME;
import static com.colorbuzztechgmail.spf.ModelDataBase.MODEL_COLUMN_NAME;
import static com.colorbuzztechgmail.spf.ModelDataBase.TABLE_CUSTOM_MATERIAL;
import static com.colorbuzztechgmail.spf.ModelDataBase.TABLE_DEALERSHIP;
import static com.colorbuzztechgmail.spf.ModelDataBase.TABLE_DEALER_CUSTOM_MATERIAL_RELATIONS;
import static com.colorbuzztechgmail.spf.ModelDataBase.TABLE_MATERIAL_TYPES;
import static com.colorbuzztechgmail.spf.ModelDataBase.TABLE_MATERIAL_TYPES_CUSTOM_MATERIAL_RELATIONS;

public class CustomMaterialItemActivity extends BaseItemActivity{

    private CustomMaterialDataset dataset;
    private CustomMaterialListAdapter adapter;
    private ModelDataBase db;


    @Override
    protected List<Object> getRecentItems() {

        final List<Object> buffer=new ArrayList<>();
        buffer.addAll(db.getRecentCustomMaterial(lastImportsCount));
        return  buffer;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_search_material,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_search:

                loadSearchBar();

                return true;

            case R.id.action_sortName:
                item.setChecked(true);
                dataset.setSortType(CustomMaterialDataset.SortType.NAME);
                COLUMN_ORDER=CUSTOM_MATERIAL_COLUMN_NAME;
                GROUPED_VALUE="";
                filterType(getSelectedCategory(),getChooserType());
                return true;

            case R.id.action_sortDate:
                item.setChecked(true);
                dataset.setSortType(CustomMaterialDataset.SortType.LAST);
                COLUMN_ORDER=KEY_ID;
                GROUPED_VALUE="";
                filterType(getSelectedCategory(),getChooserType());
                return true;

            case R.id.action_sortType:
                item.setChecked(true);
                dataset.setSortType(CustomMaterialDataset.SortType.TYPE);
                COLUMN_ORDER=MATERIAL_TYPE_COLUMN_NAME;
                GROUPED_VALUE= " GROUP BY " + CUSTOM_MATERIAL_COLUMN_NAME;
                filterType(getSelectedCategory(),getChooserType());
                return true;

            case R.id.action_sortDealer:
                item.setChecked(true);
                dataset.setSortType(CustomMaterialDataset.SortType.DEALER);
                COLUMN_ORDER=DEALER_COLUMN_NAME;
                GROUPED_VALUE=" GROUP BY " +CUSTOM_MATERIAL_COLUMN_NAME;
                filterType(getSelectedCategory(),getChooserType());
                return true;
            case R.id.action_sortSeasons:
                item.setChecked(true);
                dataset.setSortType(CustomMaterialDataset.SortType.SEASONS);

                COLUMN_ORDER=CUSTOM_MATERIAL_COLUMN_SEASONS;
                GROUPED_VALUE=" GROUP BY " + CUSTOM_MATERIAL_COLUMN_NAME;
                filterType(getSelectedCategory(),getChooserType());

                return true;

            case R.id.action_view:
                ViewMode=ViewMode.equals(Utils.ViewMode.LIST) ? Utils.ViewMode.GRID : Utils.ViewMode.LIST;

                item.setIcon(ViewMode.equals(Utils.ViewMode.LIST) ? getResources().getDrawable(R.drawable.ic_view_module_white_24dp) : getResources().getDrawable(R.drawable.ic_view_list_white_24dp));

                customView();

                return true;

            case R.id.action_title:

                collapseTitle=!collapseTitle;
                getAdapter().setCollapsedAllTitle(collapseTitle);

                item.setTitle(collapseTitle ?  getString( R.string.action_expand_title) :  getString(R.string.action_collapse_title));

                return true;
        }


        return false;
    }

    @Override
    protected void initializeValues() {

        GROUPED_VALUE="";
        COLUMN_ORDER=CUSTOM_MATERIAL_COLUMN_NAME;

        db=new ModelDataBase(this);

        adapter = new CustomMaterialListAdapter(this,this,true);

       this.dataset=new CustomMaterialDataset(mRecycler,getAdapter());

        setDataset( dataset);


    }

    @Override
    protected BaseAdapter getAdapter() {


        return adapter;
    }

    @Override
    protected void addNewItem() {

    }

    @Override
    protected List<Object> filterType(String query, CategoryFragment.ChooserType type) {

        String selectQuery;
        closeCursor();

        switch (type) {
            case MATERIAL_TYPE:// tipo


                selectQuery = "SELECT  * FROM " + TABLE_CUSTOM_MATERIAL + " tcm, "+ TABLE_DEALERSHIP + " td, " + TABLE_DEALER_CUSTOM_MATERIAL_RELATIONS +" tdcm, "
                        + TABLE_MATERIAL_TYPES + " tmt, " + TABLE_MATERIAL_TYPES_CUSTOM_MATERIAL_RELATIONS + " tmtcm"
                        + " WHERE tmt." + MATERIAL_TYPE_COLUMN_NAME + " = '" + query + "'"
                        + " AND tmt." + KEY_ID + " = " + "tmtcm." + KEY_MATERIAL_TYPE_ID
                        + " AND tcm." + KEY_ID + " = " + "tmtcm." + KEY_CUSTOM_MATERIAL_ID
                        + " AND tcm." + KEY_ID + " = " + "tdcm." + KEY_CUSTOM_MATERIAL_ID
                        + " AND td." + KEY_ID + " = " + "tdcm." + KEY_DEALER_ID;
                break;

            case MATERIAL_DEALER:
                selectQuery = "SELECT  * FROM " + TABLE_CUSTOM_MATERIAL + " tcm, " + TABLE_DEALERSHIP + " td, " + TABLE_DEALER_CUSTOM_MATERIAL_RELATIONS +" tdcm, " + TABLE_MATERIAL_TYPES + " tmt, "
                        +  TABLE_MATERIAL_TYPES_CUSTOM_MATERIAL_RELATIONS+" tmtcm"
                        + " WHERE td." + DEALER_COLUMN_NAME + " = '" + query + "'"
                        + " AND td." + KEY_ID + " = " + "tdcm." + KEY_DEALER_ID
                        + " AND tcm." + KEY_ID + " = " + "tdcm." + KEY_CUSTOM_MATERIAL_ID
                        + " AND tcm." + KEY_ID + " = " + "tmtcm." + KEY_CUSTOM_MATERIAL_ID
                        + " AND tmt." + KEY_ID + " = " + "tmtcm." + KEY_MATERIAL_TYPE_ID;

                break;

            default:
                selectQuery = "SELECT  * FROM " + TABLE_CUSTOM_MATERIAL;
                break;

        }

        mCursor  = db.getReadableDatabase().rawQuery(selectQuery  + GROUPED_VALUE+ " ORDER BY " + COLUMN_ORDER +" COLLATE NOCASE "
                , null);

        dataset.removeAll();
        newLoadAsyncTask();
        return null;
    }

    @Override
    protected List<Object> filterByParameter(String query) {

        final List<Object> filteredList = new ArrayList<>();



        filteredList.addAll(db.getCustomMaterialByValue(query));





        return filteredList;


    }

    @Override
    protected void showRemoveDialog(final LongSparseArray<Boolean> checkedList) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);
        alertDialogBuilder
                .setTitle(  getResources().getString(R.string.dialogTitle_remove))
                //.setMessage( getResources().getString(R.string.dialogMsg_remove))
                .setCancelable(false)
                .setPositiveButton(  getResources().getString(R.string.dialog_remove),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                final ArrayList<Long> removeIds=new ArrayList<Long>();
                                if(checkedList.size()>0) {

                                    final List<CustomMaterial>materials=new ArrayList<>();
                                    for(int i=0;i<checkedList.size();i++) {

                                        if(checkedList.valueAt(i)) {
                                            final CustomMaterial material=dataset.getCustomMaterialById(checkedList.keyAt(i));

                                            materials.add(material);

                                            removeIds.add(checkedList.keyAt(i));
                                        }
                                    }

                                    dataset.removeByIds(removeIds);
                                    refreshInfobar();
                                    adapter.multiChoiceHelper.clearChoices();
                                    dialog.dismiss();
                                    showUndoSnackbar(materials);
                                }
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

    private void showUndoSnackbar(final List<CustomMaterial> materials){

        CoordinatorLayout view = findViewById(R.id.parentLayout);

        String mssg;

        if(materials.size()==1){

            mssg = materials.get(0).getName() + " eliminado";

        }else {

            mssg = String.valueOf(materials.size()) + " Materiales " + "eliminados";

        }

        Snackbar snackbar = Snackbar.make(view, mssg, Snackbar.LENGTH_LONG)
                .setAction(getResources().getString(R.string.action_undo), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        for(CustomMaterial material: materials){

                            dataset.add(material);
                            refreshInfobar();

                        }
                    }

                }).setCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar snackbar, int dismissType) {
                        super.onDismissed(snackbar, dismissType);

                        if(dismissType !=DISMISS_EVENT_ACTION){


                            for(CustomMaterial material: materials){

                                ModelDataBase db= new ModelDataBase(getBaseContext());

                                db.deleteCustomMaterial(material.getId());


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

        for(int i=0;i<=ids.length-1;i++){

            positions[i]=dataset.sortedList.indexOf(dataset.getCustomMaterialById(ids[i]));

        }

      final EditMaterial editMaterial=EditMaterial.newInstanceEditMaterialPopUp(positions,ids,true,this);
        editMaterial.show(getSupportFragmentManager(),"edit_material");


    }

    @Override
    protected boolean isObjectVisible(Object obj) {

       if(!(getSelectedCategory().equals(getString(R.string.importRecent_Cat)))){

          CustomMaterial material=((CustomMaterial)obj);

          switch (getChooserType()) {
              //Tipo
              case  MATERIAL_TYPE:
                  if (material.getType().equals(getSelectedCategory())) {

                      return true;

                  }

                  return false ;
              //Proveedor
              case    MATERIAL_DEALER:

                  if (material.getDealership().equals(getSelectedCategory())) {

                      return  true;
                  }
                  return false ;


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
    public void onClick(View v, int position, boolean isLongClick) {}

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

      return   db.createCustomMaterial(mCursor,true);
    }

}
