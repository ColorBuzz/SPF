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
public class AccessoriesFragment extends Fragment  implements AdapterView.OnItemSelectedListener,View.OnClickListener,PopupMenu.OnMenuItemClickListener,SearchView.OnQueryTextListener{

    public static final int SORT_TYPE=0;
    public static final int SORT_DEALER=1;
    public static final int SORT_MODEL=2;
    public static final int SORT_ALL=3;
    public static final int SORT_NAME=4;
    private static final Comparator<Accessories> ALPHABETICAL_COMPARATOR = new Comparator<Accessories>() {
        @Override
        public int compare(Accessories a,Accessories b) {
            return a.getName().toLowerCase().compareTo(b.getName().toLowerCase());
        }
    };
    public int filterMode=SORT_ALL;
    public MainActivity ma;
    public AccessoriesAdapter  accessoriesAdapter;
    public List<Accessories> bufferAcessories = new ArrayList<>();
    public RecyclerView mRecycler;
    MenuItem viewItem;
    MenuItem categoryItem;
    AddAccessoriesPopUp addAccessoriesPopUp ;
    ActionMode choiceActionMode;
    boolean start=false;
    private CustomSearchView customSearchView;
    private final ActionMode.Callback mCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {

            MenuInflater inflater = mode.getMenuInflater();

            inflater.inflate(R.menu.menu_search, menu);


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
    private ArrayAdapter spinnerAccesories;
    private GridSpacingItemDecoration itemDecoration;
    private int nColumn = 0;
    private  LinearLayout fragmentContainer;
    private int selectedCategory=0;

    public AccessoriesFragment() {
        super();

    }

    private static List<Accessories> filterModelType(List<Accessories> filelist, String query, int type) {
        final String lowerCaseQuery = query.toLowerCase();

        final List<Accessories> filteredModelList = new ArrayList<>();
        for (Accessories  accessories : filelist) {

            if (type == SORT_TYPE) {// tipo
                final String text = accessories.getType().toLowerCase();
                if (text.contains(lowerCaseQuery)) {
                    filteredModelList.add(accessories);
                }
            }

            else if (type == SORT_DEALER) {// External
                    final String text = accessories.getDealer().toLowerCase();
                    if (text.contains(lowerCaseQuery)) {
                        filteredModelList.add(accessories);
                    }


            }else if (type == SORT_NAME) {// External
                final String text = accessories.getName().toLowerCase();
                if (text.contains(lowerCaseQuery)) {
                    filteredModelList.add(accessories);
                }


            }
            else if (type==SORT_ALL){//Todos

              filteredModelList.add(accessories);


            }
        }
        return filteredModelList;
    }

    private static List<Accessories> filterExternal(List<Accessories> filelist, String query,int type) {
        final String lowerCaseQuery = query.toLowerCase();

        final List<Accessories> filteredModelList = new ArrayList<>();
        for (Accessories accessories : filelist) {
            final String text = accessories.getName().toLowerCase();
            if (text.contains(lowerCaseQuery)) {
                filteredModelList.add(accessories);
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
            selectCategory((String) spinnerAccesories.getItem(position));
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.menu_accessories, menu);


        categoryItem= menu.findItem(R.id.action_category);
        categoryItem.setActionView(ma.mSpinner);
        viewItem = menu.findItem(R.id.action_viewBar);
        customSearchView.setOnQueryTextListener(this);
        customSearchView.setQueryHint("Busqueda...");
        final MenuItem sortAll= menu.findItem(R.id.action_sortAll);
        final MenuItem sortDealer= menu.findItem(R.id.action_sortDealer);
        final MenuItem sortType= menu.findItem(R.id.action_sortType);
        final MenuItem sortModel= menu.findItem(R.id.action_sortModel);

        switch (filterMode){

            case SORT_TYPE:

                sortType.setChecked(true);


                break;

             case SORT_DEALER:

                sortDealer.setChecked(true);



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

            case R.id.action_sortDealer:
               item.setChecked(true);


                setCustomAdapter(SORT_DEALER);


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

        AccessoriesAdapter accessoriesAdapter = new AccessoriesAdapter(getContext(), ALPHABETICAL_COMPARATOR, this);
        ma.mSpinner.setOnItemSelectedListener(this);

        customSearchView = new CustomSearchView(getContext());




        if(accessoriesAdapter.accesoriesList!=null) {
            refreshInfobar(accessoriesAdapter.getItemCount());
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

            accessoriesAdapter = new AccessoriesAdapter(getContext(),ALPHABETICAL_COMPARATOR,this);
            spinnerAccesories = new ArrayAdapter<String>(getContext(),
                    R.layout.spinneritem, ma.userData.getMaterialsTypes());
            spinnerAccesories.setDropDownViewResource(R.layout.spinner_dropdown_item);

            ma.mSpinner.setAdapter(spinnerAccesories);

        }else if(filterMode==SORT_DEALER) {

             List<String>dealerships= new ArrayList<>();
             List<Dealership>dealer=ma.userData.getDealerShips();
            dealerships.add(getContext().getString(R.string.importNoAsigned_Cat));

             if(dealer!=null){

                 for(int i=0;i<dealer.size();i++){

                     dealerships.add(dealer.get(i).getName());
                 }
             }


            accessoriesAdapter = new AccessoriesAdapter(getContext(),ALPHABETICAL_COMPARATOR,this);
            spinnerAccesories = new ArrayAdapter<String>(getContext(),
                    R.layout.spinneritem,dealerships);
            spinnerAccesories.setDropDownViewResource(R.layout.spinner_dropdown_item);

            ma.mSpinner.setAdapter(spinnerAccesories);


        }else if(filterMode==SORT_ALL){

            final ArrayList<String> all =new ArrayList<>();
            all.add(getContext().getString(R.string.action_sortAll));

            accessoriesAdapter = new AccessoriesAdapter(getContext(),ALPHABETICAL_COMPARATOR,this);
            spinnerAccesories = new ArrayAdapter<String>(getContext(),
                    R.layout.spinneritem, all);
            spinnerAccesories.setDropDownViewResource(R.layout.spinner_dropdown_item);

            ma.mSpinner.setAdapter(spinnerAccesories);


        }




    }

    public void selectCategory(String category) {


            bufferAcessories.clear();
            customSearchView.onActionViewCollapsed();

            if (ma.userData.getCustomMaterialCount() > 0) {
                bufferAcessories = filterModelType(ma.userData.getAccessories(), category, filterMode);
                accessoriesAdapter.replaceAll(bufferAcessories);
                accessoriesAdapter.refreshInitiaPos = false;
                mRecycler.swapAdapter(accessoriesAdapter,true);



                    mRecycler.scrollToPosition(0);


            }


      refreshInfobar(accessoriesAdapter.getItemCount());

    }

    public void refreshInfobar(int count ){

        if(this.isVisible()) {
            AppCompatActivity activity = (AppCompatActivity) getActivity();
            ActionBar actionBar = activity.getSupportActionBar();
            String aux = String.valueOf(count);
            aux += " ";
            if(count==1){
                aux += getString(R.string.action_complements);

            }else{
                aux += getString(R.string.action_complements);


            }
            actionBar.setTitle(getString(R.string.action_complements));

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

        if (bufferAcessories.size() > 0) {
            final List<Accessories> filteredExternalList = filterExternal(bufferAcessories, query,filterMode);
            accessoriesAdapter.replaceAll(filteredExternalList);
            mRecycler.scrollToPosition(0);

        }
        return false;
    }

    private void showAddExternalPopup(){


        addAccessoriesPopUp =
                AddAccessoriesPopUp.newInstance(0,false);


        addAccessoriesPopUp.show(ma.getSupportFragmentManager(),"addExternalDialog");


    }

    public void editInfo(int accessoriesId){



        addAccessoriesPopUp =
                AddAccessoriesPopUp.newInstance( accessoriesId,true);



       addAccessoriesPopUp.onAttach(this.getActivity());

        addAccessoriesPopUp.show(ma.getSupportFragmentManager(),"addExternalDialog");


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

