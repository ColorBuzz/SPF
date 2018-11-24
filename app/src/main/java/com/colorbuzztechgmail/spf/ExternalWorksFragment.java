package com.colorbuzztechgmail.spf;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


/**
 * Created by PC01 on 21/06/2017.
 */
public class ExternalWorksFragment extends Fragment  implements AdapterView.OnItemSelectedListener,View.OnClickListener,PopupMenu.OnMenuItemClickListener,SearchView.OnQueryTextListener{

    public static final int SORT_TYPE=0;
    public static final int SORT_EXTERNAL=1;
    public static final int SORT_MODEL=2;
    public static final int SORT_ALL=3;
    private static final Comparator<ExternalWorks> ALPHABETICAL_COMPARATOR = new Comparator<ExternalWorks>() {
        @Override
        public int compare(ExternalWorks a,ExternalWorks b) {
            return a.getExternal().toLowerCase().compareTo(b.getExternal().toLowerCase());
        }
    };
    public int filterMode=SORT_ALL;
    public MainActivity ma;
    public ExternalWorksAdapter  externalWorksAdapter;
    public List<ExternalWorks> bufferExternalWors = new ArrayList<>();
    public RecyclerView mRecycler;
    MenuItem viewItem;
    MenuItem categoryItem;
     ActionMode choiceActionMode;
    boolean start=false;
    private CustomSearchView customSearchView;
    private final ActionMode.Callback mCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {

            MenuInflater inflater = mode.getMenuInflater();

            inflater.inflate(R.menu.menu_model, menu);


            return true;

        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {

            mode.setCustomView(customSearchView);
            customSearchView.onActionViewExpanded();





            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            choiceActionMode = null;
            customSearchView.setQuery("",false);
        }
    };
    private ArrayAdapter spinnerExternalWorks;
    private GridSpacingItemDecoration itemDecoration;
    private int nColumn = 0;
    private  LinearLayout fragmentContainer;
    private int selectedCategory=0;

    public ExternalWorksFragment() {
        super();

    }

    private static List<ExternalWorks> filterModelType(List<ExternalWorks> filelist, String query, int type) {
        final String lowerCaseQuery = query.toLowerCase();

        final List<ExternalWorks> filteredModelList = new ArrayList<>();
        for (ExternalWorks  externalWorks : filelist) {

            if (type == SORT_TYPE) {// tipo
                final String text = externalWorks.getType().toLowerCase();
                if (text.contains(lowerCaseQuery)) {
                    filteredModelList.add(externalWorks);
                }
            }

            else if (type == SORT_EXTERNAL) {// External
                    final String text = externalWorks.getExternal().toLowerCase();
                    if (text.contains(lowerCaseQuery)) {
                        filteredModelList.add(externalWorks);
                    }


            }else if (type==SORT_ALL){//Todos

              filteredModelList.add(externalWorks);


            }
        }
        return filteredModelList;
    }

    private static List<ExternalWorks> filterExternal(List<ExternalWorks> filelist, String query,int type) {
        final String lowerCaseQuery = query.toLowerCase();

        final List<ExternalWorks> filteredModelList = new ArrayList<>();
        for (ExternalWorks externalWorks : filelist) {
            final String text = externalWorks.getExternal().toLowerCase();
            if (text.contains(lowerCaseQuery)) {
                filteredModelList.add(externalWorks);
            }
        }
        return filteredModelList;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.externalworks_fragment, container, false);
        setHasOptionsMenu(true);

        mRecycler = (RecyclerView) view.findViewById(R.id.materialrecyclerView);
        fragmentContainer=(LinearLayout) view.findViewById(R.id.materialFragmentContainer);
        FloatingActionButton addExternal=(FloatingActionButton) view.findViewById(R.id.addExternalWorkFloating);
        addExternal.setOnClickListener(this);

        loadInit();

        return view;

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //Utils.toast(getContext(), String.valueOf(position));

        if(start) {
            selectedCategory = position;
            selectCategory((String) spinnerExternalWorks.getItem(position));
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.menu_external_works, menu);


        categoryItem= menu.findItem(R.id.action_category);
         viewItem = menu.findItem(R.id.action_viewBar);
        customSearchView.setOnQueryTextListener(this);
        customSearchView.setQueryHint("Busqueda...");
        final MenuItem sortAll= menu.findItem(R.id.action_sortAll);
        final MenuItem sortExternal= menu.findItem(R.id.action_sortExternal);
        final MenuItem sortType= menu.findItem(R.id.action_sortType);
        final MenuItem sortModel= menu.findItem(R.id.action_sortModel);

        switch (filterMode){

            case SORT_TYPE:

                sortType.setChecked(true);


                break;

            case SORT_EXTERNAL:

                sortExternal.setChecked(true);

                break;


            case SORT_MODEL:

                sortModel.setChecked(true);

                break;


            case SORT_ALL:

                sortAll.setChecked(true);

                break;
        }
        customView();

        setCustomAdapter(filterMode);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {



            case R.id.action_sortAll:

                if (item.isChecked()) item.setChecked(false);
                else {item.setChecked(true);}

                setCustomAdapter(SORT_ALL);


               break;

            case R.id.action_sortExternal:
               item.setChecked(true);


                setCustomAdapter(SORT_EXTERNAL);


                break;
            case R.id.action_sortType:

             item.setChecked(true);


                setCustomAdapter(SORT_TYPE);


                break;

            case R.id.action_sortModel:
                item.setChecked(true);


                setCustomAdapter(SORT_MODEL);


                break;

            case R.id.action_searchBar:

                startSupportActionModeIfNeeded();

                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {


        super.onPrepareOptionsMenu(menu);


// You can hide the state of the menu item here if you call getActivity().supportInvalidateOptionsMenu(); somewhere in your code

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    public void setActivity(MainActivity ma) {
        this.ma = ma;

    }

    private void loadInit() {

        ExternalWorksAdapter externalWorksAdapter = new ExternalWorksAdapter(getContext(), ALPHABETICAL_COMPARATOR, this);

        customSearchView = new CustomSearchView(getContext());




        if(externalWorksAdapter.externalList!=null) {
            refreshInfobar(externalWorksAdapter.getItemCount());
        }

      //  selectCategory(ma.spinnerAdapter.getItem(selectedCategory));

    }

    @Override
    public void onStart() {


            start=true;


        super.onStart();
    }

    public void customView() {



            fragmentContainer.removeAllViewsInLayout();
            fragmentContainer.setBackground(new ColorDrawable(android.R.attr.colorBackground));
            fragmentContainer.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));

            mRecycler=new RecyclerView(getContext());
            mRecycler.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            nColumn = 1;

            final GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), nColumn, LinearLayoutManager.VERTICAL, false);
            itemDecoration = new GridSpacingItemDecoration(nColumn, ((int)getContext().getResources().getDimension(R.dimen.padding))/4, false);

            mRecycler.addItemDecoration(itemDecoration);
            mRecycler.setLayoutManager(gridLayoutManager);


            fragmentContainer.addView(mRecycler);

            viewItem.setIcon(R.drawable.ic_grid_white_24dp);




    }

    public void setCustomAdapter(int filterType){

        filterMode=filterType;


        if(filterMode==SORT_TYPE){

            externalWorksAdapter = new ExternalWorksAdapter(getContext(),ALPHABETICAL_COMPARATOR,this);
            spinnerExternalWorks = new ArrayAdapter<String>(getContext(),
                    R.layout.dropdown_spinneritem, ma.userData.getMaterialsTypes());
            spinnerExternalWorks.setDropDownViewResource(R.layout.spinner_dropdown_item);


        }else if(filterMode==SORT_EXTERNAL) {

             List<String>dealerships= new ArrayList<>();
             List<Dealership>dealer=ma.userData.getDealerShips();
            dealerships.add(getContext().getString(R.string.importNoAsigned_Cat));

             if(dealer!=null){

                 for(int i=0;i<dealer.size();i++){

                     dealerships.add(dealer.get(i).getName());
                 }
             }


            externalWorksAdapter = new ExternalWorksAdapter(getContext(),ALPHABETICAL_COMPARATOR,this);
            spinnerExternalWorks = new ArrayAdapter<String>(getContext(),
                    R.layout.dropdown_spinneritem,dealerships);
            spinnerExternalWorks.setDropDownViewResource(R.layout.spinner_dropdown_item);



        }else if(filterMode==SORT_ALL){

            final ArrayList<String> all =new ArrayList<>();
            all.add(getContext().getString(R.string.action_sortAll));

            externalWorksAdapter = new ExternalWorksAdapter(getContext(),ALPHABETICAL_COMPARATOR,this);
            spinnerExternalWorks = new ArrayAdapter<String>(getContext(),
                    R.layout.dropdown_spinneritem, all);
            spinnerExternalWorks.setDropDownViewResource(R.layout.spinner_dropdown_item);



        }




    }

    public void selectCategory(String category) {


            bufferExternalWors.clear();
            customSearchView.onActionViewCollapsed();

            if (ma.userData.getCustomMaterialCount() > 0) {
                bufferExternalWors = filterModelType(ma.userData.getExternalWorks(), category, filterMode);
                externalWorksAdapter.replaceAll(bufferExternalWors);
                externalWorksAdapter.refreshInitiaPos = false;
                mRecycler.swapAdapter(externalWorksAdapter,true);



                    mRecycler.scrollToPosition(0);


            }


      refreshInfobar(externalWorksAdapter.getItemCount());

    }

    public void refreshInfobar(int count ){

        if(this.isVisible()) {
            AppCompatActivity activity = (AppCompatActivity) getActivity();
            ActionBar actionBar = activity.getSupportActionBar();
            String aux = String.valueOf(count);
            aux += " ";
            if(count==1){
                aux += getString(R.string.externalActivity);

            }else{
                aux += getString(R.string.externalActivity);


            }
            actionBar.setTitle(getString(R.string.externalActivity));

            actionBar.setSubtitle(aux);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.addExternalWorkFloating:

                showAddExternalPopup();

                break;

        }
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {

        if (bufferExternalWors.size() > 0) {
            final List<ExternalWorks> filteredExternalList = filterExternal(bufferExternalWors, query,filterMode);
            externalWorksAdapter.replaceAll(filteredExternalList);
            mRecycler.scrollToPosition(0);

        }
        return false;
    }

    private void showAddExternalPopup(){




    }

    public void editInfo(int externalWorksId){





    }

    public void startSupportActionModeIfNeeded() {
        if (choiceActionMode == null) {
            if (mCallback == null) {
                throw new IllegalStateException("No callback set");
            }
            choiceActionMode = ma.startSupportActionMode(mCallback);

            choiceActionMode.setTitle("Buscar:");

        }
    }

}

