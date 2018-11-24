package com.colorbuzztechgmail.spf;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
 import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
 import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.colorbuzztechgmail.spf.Utils.ViewMode;
import java.util.ArrayList;

import java.util.List;


/**
 * Created by PC01 on 21/06/2017.
 */
public class CustomMaterialFragment extends Fragment  implements View.OnClickListener,PopupMenu.OnMenuItemClickListener,SearchView.OnQueryTextListener,ItemActionListener,Utils.onSavedInterface{

    MenuItem viewItem;
    MenuItem categoryItem;

    public enum FilterMode{
        DEALER,
        TYPE,
        ALL
    }

    public static final int SEARCH_IN_NAME =0;
    public static final int SEARCH_IN_TYPE =1;
    public static final int SEARCH_IN_DEALER =2;
    public static final int SEARCH_IN_SEASONS =3;

    public FilterMode filterMode= FilterMode.ALL;
    public int searchMode=SEARCH_IN_NAME;

    public MainActivity ma;
    AddMaterialPopUp materialDialogFragment ;
    public CustomMaterialListAdapter materialAdapter;
    private ArrayAdapter spinnerMaterial;
    public CustomMaterialDataset materialDataset;

    public List<Object> bufferMaterial = new ArrayList<>();
    private String selectedCategory="";
    private int selectedPos=0;

    //Display
    public RecyclerView mRecycler;
    private GridSpacingItemDecoration itemDecoration;
    TitleGridItemDecoration titleItemDecoration;
    private int nColumn = 0;

    private View infoView;
    private  LinearLayout fragmentContainer;
    public boolean viewMode=false;
    private int screenContainerWidth;
    private int screenContainerHeight;
    boolean start=false;
    FloatingActionButton addMaterial;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public CustomMaterialFragment() {
        super();

    }

    public String getSelectedCategory() {
        return selectedCategory;
    }

    public void setSelectedCategory(String selectedCategory) {
        this.selectedCategory = selectedCategory;
    }

    public FilterMode getFilterMode() {
        return filterMode;
    }

    public void setFilterMode(FilterMode filterMode) {
        this.filterMode = filterMode;
    }

    @Nullable
    @Override



    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.custom_material_fragment, container, false);
        setHasOptionsMenu(true);

        mRecycler = (RecyclerView) view.findViewById(R.id.materialrecyclerView);
        fragmentContainer=(LinearLayout) view.findViewById(R.id.materialFragmentContainer);

        View infoContainer= view.findViewById(R.id.infomaterialContainer);


        LayoutInflater li=LayoutInflater.from(getContext());

        infoView=li.inflate(R.layout.animated_info_layout,null);
        ((FrameLayout)infoContainer).addView(infoView);
        setScreenContainerWidth(container.getWidth());
        setScreenContainerHeight(container.getHeight());
        addMaterial=(FloatingActionButton) getActivity().findViewById(R.id.fabbutton);
        addMaterial.setVisibility(View.VISIBLE);
        addMaterial.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_plus_white_40dp));
        addMaterial.setOnClickListener(this);
        setFilterMode(FilterMode.ALL);
        setSelectedCategory((getResources().getString(R.string.action_sortAll)));

        loadInit();

        return view;

    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.menu_custom_material, menu);


        categoryItem= menu.findItem(R.id.action_category);
        categoryItem.setActionView(ma.mSpinner);
        viewItem = menu.findItem(R.id.action_viewBar);

        materialAdapter = new CustomMaterialListAdapter(this,ma,true);
        materialDataset=new CustomMaterialDataset(mRecycler,materialAdapter);
        materialAdapter.dataset(materialDataset);

        setupRecycler();

        customView(viewMode,getScreenContainerWidth(),getScreenContainerHeight());

        selectCategory(getResources().getString(R.string.action_sortAll));


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_viewBar:

                viewMode=!viewMode;
                customView(viewMode,getScreenContainerWidth(),getScreenContainerHeight());

                return false;


            case R.id.action_filter:

                showFilterChooser(FilterMode.DEALER);

                return false;

            case R.id.action_searchBar:

                loadSearchBar();

                return false;

            case R.id.action_searchName:
                item.setChecked(true);

                setSearchMode(SEARCH_IN_NAME);
                return false;

            case R.id.action_searchType:
                item.setChecked(true);

                setSearchMode(SEARCH_IN_TYPE);

                return false;
            case R.id.action_searchDealer:
                item.setChecked(true);

                setSearchMode(SEARCH_IN_DEALER);

                return false;
            case R.id.action_searchSeasons:
                item.setChecked(true);

                setSearchMode(SEARCH_IN_SEASONS);

                return false;


            case R.id.action_sortName:
                item.setChecked(true);
                materialDataset.setSortType(CustomMaterialDataset.SortType.NAME);

                materialDataset.changeSortType( );

                return false;

            case R.id.action_sortDate:
                item.setChecked(true);
                materialDataset.setSortType(CustomMaterialDataset.SortType.LAST);
                materialDataset.changeSortType( );

                return false;

            case R.id.action_sortType:
                item.setChecked(true);
                materialDataset.setSortType(CustomMaterialDataset.SortType.TYPE);

                materialDataset.changeSortType( );

                return false;

            case R.id.action_sortDealer:
                item.setChecked(true);
                materialDataset.setSortType(CustomMaterialDataset.SortType.DEALER);

                materialDataset.changeSortType( );

                return false;
            case R.id.action_sortSeasons:
                item.setChecked(true);
                materialDataset.setSortType(CustomMaterialDataset.SortType.SEASONS);

                materialDataset.changeSortType( );

                return false;
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

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void setActivity(MainActivity ma) {
        this.ma = ma;

    }

    private void loadInit() {

      //CustomMaterialGenerator.create(250,getContext()).clear();

    }

    public int getScreenContainerHeight() {
        return screenContainerHeight;
    }

    public void setScreenContainerHeight(int screenContainerHeight) {
        this.screenContainerHeight = screenContainerHeight;
    }

    public int getScreenContainerWidth() {
        return screenContainerWidth;
    }

    public void setScreenContainerWidth(int setScreenContainerWidth) {
        this.screenContainerWidth = setScreenContainerWidth;
    }

    public int getSearchMode() {
        return searchMode;
    }

    public void setSearchMode(int searchMode) {
        this.searchMode = searchMode;
    }

    @Override
    public void onStart() {


        start=true;

        super.onStart();
    }

    private void setupRecycler(){



        mRecycler= new RecyclerView(getContext());
        mRecycler.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        Utils.setMargins(mRecycler,8,0,8,0);

        mRecycler.setHasFixedSize(true);
        mRecycler.setAdapter(materialAdapter);

        HeaderItemDecoration headerItemDecoration=new HeaderItemDecoration(mRecycler,materialAdapter);
        mRecycler.addItemDecoration(headerItemDecoration);

    }

    public void customView(boolean mode, int screenWidth, int screenHeight) {




        //Utils.toast(getContext(),"N View:" + String.valueOf(nView));
        //  mRecycler.removeItemDecoration(itemDecoration);

        if (!mode) {  ///ListView

            materialAdapter.setViewMode(ViewMode.LIST);

            this.getView().findViewById(R.id.scrollContainer).setVisibility(View.GONE);

            fragmentContainer.removeAllViews();

            setupRecycler();

            nColumn = 1;

            final GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), nColumn, LinearLayoutManager.VERTICAL, false);
            itemDecoration = new GridSpacingItemDecoration(nColumn, 8, false);

            mRecycler.addItemDecoration(itemDecoration);
            mRecycler.setLayoutManager(gridLayoutManager);
            fragmentContainer.addView(mRecycler);

            viewItem.setIcon(R.drawable.ic_grid_white_24dp);


        } else if (mode) {///GridView

            materialAdapter.setViewMode(ViewMode.GRID);

            fragmentContainer.removeAllViews();

            setupRecycler();

            nColumn = 2;


            final GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), nColumn);
            titleItemDecoration=new TitleGridItemDecoration(8,materialAdapter,nColumn,true);
             mRecycler.addItemDecoration( titleItemDecoration);
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {

                    return materialAdapter.isHeader(position) ? gridLayoutManager.getSpanCount() : 1;
                }
            });

            mRecycler.setLayoutManager(gridLayoutManager);
            fragmentContainer.addView(mRecycler);
            viewItem.setIcon(R.drawable.ic_list_white_24dp);


            /*fragmentContainer.removeAllViewsInLayout();


            int width=0;
            int height=0;
            int cardWidth=0;
            int carGap=10;

            if (screenWidth>screenHeight){


                width=(screenHeight/2);

                height=width;
                cardWidth=width-(width/4);
                carGap=8;

                fragmentContainer.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height+(width/4)));
            }else{

                cardWidth=screenWidth/2;
                width=screenWidth;
                height=(screenHeight/2);
                carGap=12;

                fragmentContainer.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            }



            fragmentContainer.setBackground(getResources().getDrawable(R.drawable.drawable_color_primary_gradient));
            mRecycler.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));




            //   infoContainer.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,height));

            nColumn = 1;

            itemDecoration = new GridSpacingItemDecoration(nColumn, 1, true);
            mRecycler.addItemDecoration(itemDecoration);

            mRecycler.setLayoutManager(new CardSliderLayoutManager((screenWidth/2)-(cardWidth/2), cardWidth, carGap,3,infoView));

            new com.colorbuzztechgmail.spf.CardSnapHelper().attachToRecyclerView(mRecycler);

            CustomScrollListener customScrollListener = new CustomScrollListener(mRecycler);
            mRecycler.addOnScrollListener(customScrollListener);


            fragmentContainer.addView(mRecycler);
            this.getView().findViewById(R.id.scrollContainer).setVisibility(View.VISIBLE);


            viewItem.setIcon(R.drawable.ic_list_white_24dp);*/

        }

    }

    public void selectCategory(String category) {

        setSelectedCategory(category);

        bufferMaterial.clear();


            bufferMaterial = filterType( category, getFilterMode());

            fillMateralDataset(bufferMaterial);

            mRecycler.scrollToPosition(0);

            refreshInfobar();

    }

    private  List<Object> filterType( String query, FilterMode filterMode) {
        final String lowerCaseQuery = query.toLowerCase();

        final ModelDataBase db=new ModelDataBase(getContext());

        final List<Object> filteredModelList = new ArrayList<>();


            switch (filterMode) {
                case TYPE:// tipo

                        filteredModelList.addAll(db.getCustomMaterialByType(query));

                    break;

                case DEALER:
                         filteredModelList.addAll(db.getCustomMaterialByDealer(query));

                    break;

                case ALL:

                    filteredModelList.addAll(db.getCustomMaterilas());
                    break;
            }

        return filteredModelList;
    }

    public void refreshInfobar( ) {
        int count=0;
        if (this.isVisible()) {


            count=materialDataset.getItemCount();
            String aux = getSelectedCategory();
            aux += " ";
            aux +=String.valueOf(count);

            ma.myToolbar.setTitle(getString(R.string.materialsTxt));
            ma.myToolbar.setSubtitle(aux);
        }
    }

    @Override
    public void onClick(View v) {


        switch (v.getId()){

            case R.id.fabbutton:

                showAddMaterialPopup();

                break;






        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {

        if(bufferMaterial.size()>0) {

            final List<Object> filteredModelList = filterByParamater(bufferMaterial, query,getSearchMode());
            if(filteredModelList.size()>0){
                materialDataset.removeAll();
                materialDataset.AutomaticTitleGeneratorSortType(filteredModelList,false);
                mRecycler.scrollToPosition(0);


            }else{
                materialDataset.removeAll();

                materialDataset.add( new CustomHeader("No hay coincidencias ",0,getResources().getDrawable(R.drawable.ic_info_black_24dp)));
            }


        }
        return false;
    }

    private static List<Object> filterByParamater(List<Object> filelist, String query,int parameter) {
        final String lowerCaseQuery = query.toLowerCase();

        final List<Object> filteredList = new ArrayList<>();
        for (Object obj : filelist) {
            CustomMaterial material=(CustomMaterial)obj;

            switch (parameter){

                case SEARCH_IN_NAME:
                    final String text1 = material.getName().toLowerCase();
                    if (text1.contains(lowerCaseQuery)) {
                        filteredList.add(material);
                    }
                    break;

                case SEARCH_IN_TYPE:
                    final String text2 = material.getType().toLowerCase();
                    if (text2.contains(lowerCaseQuery)) {
                        filteredList.add(material);
                    }

                    break;


                case SEARCH_IN_DEALER:
                    final String text3 = material.getDealership().toLowerCase();
                    if (text3.contains(lowerCaseQuery)) {
                        filteredList.add(material);
                    }

                    break;
                case SEARCH_IN_SEASONS:
                    final String text4 = material.getSeasons().toLowerCase();
                    if (text4.contains(lowerCaseQuery)) {
                        filteredList.add(material);
                    }

                    break;




            }


        }
        return filteredList;
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void showFilterChooser(final FilterMode filterMode){

        setFilterMode(filterMode);

        final ModelDataBase db=new ModelDataBase(getContext());

        ArrayList<String> values= new ArrayList<>();

        switch (getFilterMode()){


            case DEALER:

                if(db.getCustomMaterialCount()>0){

                    values.addAll(db.getCustomMaterialsDealersNames());

                }

                break;


        }

        LayoutInflater li=LayoutInflater.from(getContext());

        View v=li.inflate(R.layout.save_piece_popup,null);

        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());

        builder.setView(v);


        final Dialog d=builder.create();

        ((Toolbar)v.findViewById(R.id.pieceToolbar)).setTitle(getResources().getString(R.string.material_dealership) + " (" + String.valueOf(values.size()) + ")");
        ((Toolbar)v.findViewById(R.id.pieceToolbar)).setNavigationIcon(getResources().getDrawable(R.drawable.ic_keyboard_arrow_left_black_24dp));

        ((Toolbar)v.findViewById(R.id.pieceToolbar)).setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d.dismiss();
            }
        });
        final ArrayList<ListPopUpMenuItem> items =new ArrayList<>();

        items.add(new ListPopUpMenuItem(getResources().getString(R.string.action_sortAll),R.drawable.ic_supervisor_account_accent_24dp));

        for(String dealer: values){


            items.add(new ListPopUpMenuItem(dealer,R.drawable.ic_person_black_24dp));

        }

        final  ListPopupWindowAdapter adapter = new ListPopupWindowAdapter(getContext(),items){
            public View getView(final int position, View convertView, ViewGroup parent) {
                //Use super class to create the View
                View v = super.getView(position, convertView, parent);
                TextView tv = (TextView)v.findViewById(R.id.text);

                //Put the image on the TextView
                // tv.setCompoundDrawablesWithIntrinsicBounds(items.get(position).getImageDrawable(), null, null, null);
                int dp8=(int) (8 * getResources().getDisplayMetrics().density + 0.5f);
                //Add margin between image and text (support various screen densities)
                int dp5 = (int) (5 * getResources().getDisplayMetrics().density + 0.5f);
                tv.setCompoundDrawablePadding(dp5);
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String text=((TextView)v.findViewById(R.id.text)).getText().toString();

                        if(text.equals(getResources().getString(R.string.action_sortAll))){

                            setFilterMode(FilterMode.ALL);

                        }
                        // Utils.toast(getContext(),category);
                        d.dismiss();
                        selectCategory(text);

                    }
                });
                return v;
            }
        };


        d.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                ((ListView)d.getWindow().findViewById((R.id.lisview))).setAdapter(adapter);


                d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
        });
        d.show();
    }


    private void showMaterialPreview(Material material){

        byte[] image=ModelDataBase.imageViewtoByte(material.getImage());

        MaterialPreviewPopUp materialPopUp= MaterialPreviewPopUp.newInstance(image,material.getName());
        materialPopUp.show(getActivity().getSupportFragmentManager(),"materialPreview");


    }

    private void showRemoveDialog(final SparseBooleanArray checkedList){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getContext());
        alertDialogBuilder
                .setTitle(  getResources().getString(R.string.dialogTitle_remove))
                //.setMessage( getResources().getString(R.string.dialogMsg_remove))
                .setCancelable(false)
                .setPositiveButton(  getResources().getString(R.string.dialog_remove),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                final ArrayList<Integer> removeIds=new ArrayList<Integer>();
                                if(checkedList.size()>0) {

                                    final List<CustomMaterial>materials=new ArrayList<>();
                                    for(int i=0;i<checkedList.size();i++) {

                                        if(checkedList.valueAt(i)) {
                                            final CustomMaterial material=materialDataset.getCustomMaterialById(checkedList.keyAt(i));

                                            materials.add(material);

                                            removeIds.add(checkedList.keyAt(i));
                                        }
                                    }

                                    materialDataset.removeByIds(removeIds);
                                    refreshInfobar();
                                    materialAdapter.multiChoiceHelper.clearChoices();
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

        CoordinatorLayout view = (CoordinatorLayout)  ma.findViewById(R.id.parentLayout);

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

                            materialDataset.add(material);
                            refreshInfobar();

                        }
                    }

                }).setCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar snackbar, int dismissType) {
                        super.onDismissed(snackbar, dismissType);

                        if(dismissType !=DISMISS_EVENT_ACTION){


                            for(CustomMaterial material: materials){

                                ModelDataBase db= new ModelDataBase(getContext());

                                db.deleteCustomMaterial(material.getId());


                            }


                        }

                    }
                });


        snackbar.show();
    }

    private void showInfoPopUp(final Material material){


        Utils.showInfoPopUp(getContext(),material);
/*
        final Dialog d = new Dialog(getContext());


        LayoutInflater li= LayoutInflater.from(getContext());

        View v=li.inflate(R.layout.piece_popup,null);

        final FrameLayout container=(FrameLayout) v.findViewById(R.id.di);
        final MaterialinfoContainer infoContainer= new MaterialinfoContainer(getContext(),MaterialinfoContainer.MODE_MATERIAL);



        d.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {


                d.getWindow().setLayout(550,650);
                d.getWindow().getDecorView().setBackground(new ColorDrawable(Color.TRANSPARENT));
                infoContainer.startAnimation(MaterialinfoContainer.MODE_MATERIAL,true,material,null);

            }
        });

        Toolbar toolbar=(Toolbar)v.findViewById(R.id.toolbar2);




        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_selector);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });


        toolbar.setLogo(new ShapeGenerator(getContext()).getDrawableShape(material.getImage(),ShapeGenerator.MODE_CIRCLE,0,ShapeGenerator.SIZE_SMALL));


        container.addView(infoContainer.getView());

        d.setContentView(v);
        d.create();
        d.show();*/


    }

    private void showAddMaterialPopup(){

        AddMaterialPopUp d;

        d= new AddMaterialPopUp(getContext(),null,0,
                this) ;

        d.setOwnerActivity(ma);

        d.show();
    }

    public void editInfo(int id ){

        AddMaterialPopUp d;

        d= new AddMaterialPopUp(getContext(),materialDataset.getCustomMaterialById(id),materialDataset.sortedList.indexOf(materialDataset.getCustomMaterialById(id)),
                this) ;

        d.setOwnerActivity(ma);

        d.show();
    }

    private void loadSearchBar(){


        final CustomSearchView customSearchView=new CustomSearchView(getContext());

        ma.myToolbar.getMenu().setGroupVisible(R.id.group_options,false);
        ma.myToolbar.getMenu().setGroupVisible(R.id.group_search,true);

        customSearchView.setOnQueryTextListener(this);
        customSearchView.setQueryHint("Busqueda...");
        ma.myToolbar.setNavigationIcon(R.drawable.ic_arrow_back_selector_white);
        ma.myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customSearchView.setQuery("",false);
                ma.myToolbar.removeViewInLayout(customSearchView);
                ma.myToolbar.removeViewInLayout(v);
                ma.myToolbar.getMenu().setGroupVisible(R.id.group_search,false);
                ma.myToolbar.getMenu().setGroupVisible(R.id.group_options,true);
                //ma.myToolbar.getMenu().setGroupVisible(0,true);

                customSearchView.onActionViewCollapsed();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                ma.setToggle();

            }
        });

        customSearchView.onActionViewExpanded();

        ma.myToolbar.addView(customSearchView);


    }


    //Material Adapter ItemListener
    @Override
    public void onClick(View v, int position, boolean isLongClick) {


        switch (v.getId()){


            case R.id.imageViewProfile:


                showMaterialPreview(materialDataset.getCustomMaterial(position));

                break;

            case R.id.checkLayout:


                showInfoPopUp(materialDataset.getCustomMaterial(position));

                break;

        }

    }

    @Override
    public void onPreview(Object obj) {

    }

    @Override
    public void toRemove(Object obj) {

        showRemoveDialog((SparseBooleanArray)obj);

    }

    @Override
    public void toEdit(int position) {
        editInfo(position);
    }


    private void fillMateralDataset(List<Object>materials){


            materialDataset.removeAll();
            materialDataset.AutomaticTitleGeneratorSortType(materials,true);



    }

    @Override
    public void onSaved(Object obj,int position, boolean isEditable) {


        CustomMaterial material=(CustomMaterial)obj;
        if(material.getImage()==null){
            material.setImage(getResources().getDrawable(R.drawable.leather));
        }
        if(!isEditable) {
            material.setId(Utils.toIntExact(ma.userData.addCustomMaterial(material)));

        }else{

            ma.userData.updateCustomMAterial(material);
            materialDataset.remove(material);

        }

        if(isVisible(material)){

            materialDataset.updateHeaderForNewItem(material);
        }


        materialAdapter.multiChoiceHelper.clearChoices();
        refreshInfobar();
    }

    public  boolean isVisible(CustomMaterial material){

        switch (getFilterMode()) {
            //Tipo
            case  TYPE:
                if (material.getType().equals(getSelectedCategory())) {


                    return true;


                }

                return false ;
                //Proveedor
            case    DEALER:

                if (material.getDealership().equals(getSelectedCategory())) {

                    return  true;
                }
                return false ;


            case ALL:

                return  true;



        }

        return false;
    }



}


