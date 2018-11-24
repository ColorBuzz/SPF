package com.colorbuzztechgmail.spf;

import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseBooleanArray;
import android.view.MenuItem;
import android.view.View;

import com.colorbuzztechgmail.spf.adapter.BaseAdapter;
import com.colorbuzztechgmail.spf.dataset.Dataset;

import java.util.ArrayList;
import java.util.List;

public class RecentFragment extends BaseFragment  {



    private RecentDataset dataset;
    private RecentListAdapter adapter;
    private ModelDataBase db;




    @Override
    protected void initializeValues() {
        setMyTag(getString(R.string.action_fast_access));
        db=new ModelDataBase(getContext());
        adapter=new RecentListAdapter(this,(AppCompatActivity) getActivity(),false);
        this.dataset=new RecentDataset(mRecycler,adapter);
        setDataset(this.dataset);
        setViewMode(Utils.ViewMode.GRID);

    }

    @Override
    protected BaseAdapter getAdapter() {
        return adapter;
    }

    @Override
    protected List<Object> getRecentItems() {

        final List<Object> buffer=new ArrayList<>();

        switch (getFrType()){

            case MODEL:
                buffer.addAll(db.getAddRecentModel(lastImportsCount));
                break;
            case MATERIAL:
                buffer.addAll(db.getRecentCustomMaterial(lastImportsCount));
                break;

            case CUTNOTE:
                buffer.addAll(db.getRecentCutNoteList(lastImportsCount));
                break;

            case RECENT:

                buffer.addAll(db.getAddRecentModel(lastImportsCount));
                buffer.addAll(db.getRecentCustomMaterial(lastImportsCount));
                buffer.addAll(db.getRecentCutNoteList(lastImportsCount));
                buffer.addAll(db.getAddRecentDirectory(lastImportsCount));
                break;



        }



        //buffer.addAll(db.getDealerShips());


        return buffer;
    }

    @Override
    protected List<Object> filterByParameter(String query) {



        final List<Object> filteredModelList = new ArrayList<>();


        filteredModelList.addAll(db.getPreviewModelCoincidenceValue(query));



        return filteredModelList;



    }

    @Override
    protected List<Object> filterType(String query, CategoryFragment.ChooserType type) {

        final List<Object> filteredModelList = new ArrayList<>();


        switch (type){


            case MODEL_DIRECTORY:

              //  filteredModelList.addAll(db.getPreviewModelByCategory(query));

                break;



            case MODEL_CUSTUMER:


                break;


            default:

                filteredModelList.addAll(db.loadData());

                break;


        }

        return filteredModelList;

    }

    @Override
    protected void showRemoveDialog(final SparseBooleanArray checkedList) {




    }

    private void showUndoSnackbar(final List<PreviewModelInfo> modelList){

        CoordinatorLayout view = (CoordinatorLayout)(((MainActivity)getActivity()).findViewById(R.id.parentLayout));

        String mssg;

        if(modelList.size()==1){

            mssg = modelList.get(0).getName() + " eliminados";

        }else {

            mssg = String.valueOf(modelList.size()) + " Modelos " + "eliminados";

        }

        Snackbar snackbar = Snackbar.make(view, mssg, Snackbar.LENGTH_LONG)
                .setAction(getContext().getString(R.string.action_undo), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dataset.add(modelList);
                        refreshInfobar();

                    }

                }).setCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar snackbar, int dismissType) {
                        super.onDismissed(snackbar, dismissType);

                        if(dismissType !=DISMISS_EVENT_ACTION){

                            for(PreviewModelInfo model: modelList){



                                db.deleteModelData(model.getId());


                            }


                        }

                    }
                });


        snackbar.show();
    }

    @Override
    protected void showEditDialog(long[] id) {



        switch (getFrType()){


            case MODEL:


            case CUTNOTE:

            case MATERIAL:


            case RECENT:






        }





    }

    @Override
    protected void addNewItem() {


    }

    @Override
    protected boolean isObjectVisible(Object obj) {



        if(!getSelectedCategory().equals(getString(R.string.importRecent_Cat))) {

        }
        return true;
    }

    @Override
    protected boolean isCategoryVisible(String category) {
        return false;
    }

    @Override
    public void onPreview(Object obj) {

        if(obj instanceof PreviewModelInfo){


            Intent i = new Intent(getActivity(),ModelActivity.class);
            i.putExtra("MODEL",String.valueOf(((PreviewModelInfo)(obj)).getId()));
            getActivity().startActivity(i);

        }else if(obj instanceof CutNoteList) {


            CutNoteItemActivity.newInstaceItemActivity(BaseItemActivity.activityType.CUTNOTE,getContext(),String.valueOf(((CutNoteList) obj).getId())
                    , CategoryFragment.ChooserType.CUTNOTE_CUTNOTELIST,Utils.ViewMode.LIST,getString(R.string.cutNotesList));


        }else if (obj instanceof Category){


            Intent i = new Intent(getContext(),ModelItemActivity.class);
            i.putExtra(BaseItemActivity.CATEGORY,String.valueOf(((Category) obj).getName()));
            i.putExtra(BaseItemActivity.CHOOSER_TYPE,String.valueOf(CategoryFragment.ChooserType.MODEL_DIRECTORY));

            startActivity(i);




        }else{



            Utils.showInfoPopUp(getContext(),obj);

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_sortName:
                item.setChecked(true);
                dataset.setSortType(RecentDataset.RecentSortType.NAME);
                dataset.changeSortType();
                return true;



            case R.id.action_sortDate:
                item.setChecked(true);
                dataset.setSortType(RecentDataset.RecentSortType.LAST);

                dataset.changeSortType();
                return true;
            case R.id.action_view:
                ViewMode=ViewMode.equals(Utils.ViewMode.LIST) ? Utils.ViewMode.GRID : Utils.ViewMode.LIST;

                item.setIcon(ViewMode.equals(Utils.ViewMode.LIST) ? getResources().getDrawable(R.drawable.ic_view_module_white_24dp) : getResources().getDrawable(R.drawable.ic_view_list_white_24dp));

                customView();
                return true;

            case R.id.action_title:

                collpseTitle=!collpseTitle;
                getAdapter().setCollapsedAllTitle(collpseTitle);

                item.setTitle(collpseTitle ?   getString( R.string.action_expand_title) : getString(R.string.action_collapse_title));
                item.setIcon( collpseTitle ?   getResources().getDrawable(R.drawable.ic_unfold_more_white_24dp) : getResources().getDrawable(R.drawable.ic_unfold_less_white_24dp));
                return true;

        }



        return false;
    }

    @Override
    public void onClick(View v, int position, boolean isLongClick) {
        switch (v.getId()){


        }
    }

    @Override
    protected Object getObjectToLoad(Cursor mCursor) {
        return null;
    }
}
