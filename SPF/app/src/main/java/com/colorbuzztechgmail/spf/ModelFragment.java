package com.colorbuzztechgmail.spf;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import com.github.kimkevin.cachepot.CachePot;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


/**
 * Created by PC01 on 21/06/2017.
 */
public class ModelFragment extends Fragment  implements View.OnClickListener ,SearchView.OnQueryTextListener,ColorPickerDialog.OnColorChangedListener,ItemActionListener ,
Utils.onSavedInterface{

    @Override
    public void onResume() {



        super.onResume();
    }

    @Override
    public void onDestroyView() {

        start=false;
        super.onDestroyView();
    }

    @Override
    public void onStart() {



        super.onStart();
    }

    boolean start=false;
    MenuItem categoryItem;


    //ModelDataBase filters
    public static final int FILTER_ALL=1;
    public static final int FILTER_CATEGORY=0;

    public int filterMode = FILTER_CATEGORY;


    //Models search at value
    public static final int SEARCH_IN_NAME=0;
    public static final int SEARCH_IN_CUSTUMER=1;
    public static final int SEARCH_IN_CATEGORY=2;
    public static final int SEARCH_IN_SEASONS=3;

    private int searchMode=SEARCH_IN_NAME;


    public ModelDataset modelDataset;
    public ModelListAdapter modelAdapter;
    public List<Object> bufferModel;


    //Display
    public RecyclerView mRecycler;
    private GridSpacingItemDecoration itemDecoration;
    private int nColumn = 0;
    public int ViewMode = 0;
    public MainActivity ma;


    private String selectedCategory=null;
    private int selectedPos = 0;
    public int lastImportsCount =100;

    private FrameLayout fragmentContainer;


    public int ScreenContainerWidth;
    public int ScreenContainerHeight;
    private FloatingActionButton importFab;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public ModelFragment() {
        super();
    }


    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.model_fragment, container, false);
        setHasOptionsMenu(true);
     if(getSelectedCategory()!=null){
            selectCategory(getSelectedCategory());

        }else{
         setSelectedCategory(getResources().getString(R.string.importRecent_Cat));

        }

        importFab=(FloatingActionButton)getActivity().findViewById(R.id.fabbutton);
        importFab.setVisibility(View.VISIBLE);
        importFab.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_plus_white_40dp));

        importFab.setOnClickListener(this);
        fragmentContainer = (FrameLayout) view.findViewById(R.id.modelFragmentContainer);
        setScreenContainerWidth(ma.getWindowManager().getDefaultDisplay().getWidth());
        setScreenContainerHeight(ma.getWindowManager().getDefaultDisplay().getHeight());



        return view;

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_hub,menu);
        super.onCreateOptionsMenu(menu, inflater);

        bufferModel=new ArrayList<>();

        categoryItem= menu.findItem(R.id.action_category);

        customView(ViewMode, getScreenContainerWidth(),getScreenContainerHeight());

        selectCategory(getSelectedCategory());

         start=true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {


            case R.id.action_newModel:
                showAddNewModel();

                break;

            case R.id.action_category:

                showCategoryChooser();

            break;


            case R.id.action_searchBar:
               loadSearchBar();

                break;
            case R.id.action_searchName:
                item.setChecked(true);

                setSearchMode(SEARCH_IN_NAME);
                break;

            case R.id.action_searchCustumer:
                item.setChecked(true);
                 new ColorPickerDialog(getContext(),this,getResources().getColor(R.color.colorGreen_900)).show();
                setSearchMode(SEARCH_IN_CUSTUMER);

                break;
            case R.id.action_searchCategories:
                item.setChecked(true);


                setSearchMode(SEARCH_IN_CATEGORY);

                break;
            case R.id.action_searchSeasons:
                item.setChecked(true);

                setSearchMode(SEARCH_IN_SEASONS);

                break;
            case R.id.action_sortName:
                item.setChecked(true);
                modelDataset.setSortType(ModelDataset.ModelSortType.NAME);
                modelDataset.changeSortType();

                break;

            case R.id.action_sortCategory:
                item.setChecked(true);
                modelDataset.setSortType(ModelDataset.ModelSortType.CATEGORY);

                modelDataset.changeSortType();

                break;
            case R.id.action_sortDate:
                item.setChecked(true);
                modelDataset.setSortType(ModelDataset.ModelSortType.LAST);

                modelDataset.changeSortType();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void customView(int mode, int screenWidth,int screenHeight) {


        //Utils.toast(getContext(),"N View:" + String.valueOf(nView));
        //  mRecycler.removeItemDecoration(itemDecoration);


        if (mode == 0) {  ///ListView

            fragmentContainer.removeAllViewsInLayout();
            fragmentContainer.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            fragmentContainer.setBackground(new ColorDrawable(android.R.attr.colorBackground));


            nColumn = 1;

            setupRecycler();
            HeaderItemDecoration headerItemDecoration=new HeaderItemDecoration(mRecycler,modelAdapter);
            final GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), nColumn, LinearLayoutManager.VERTICAL, false);
            itemDecoration = new GridSpacingItemDecoration(nColumn, ((int)getContext().getResources().getDimension(R.dimen.padding))/4, false);
            mRecycler.addItemDecoration(itemDecoration);
            mRecycler.addItemDecoration(headerItemDecoration);

            mRecycler.setLayoutManager(gridLayoutManager);

            final CustomScrollListenerFadeView cstmScroller=new CustomScrollListenerFadeView(getContext(),importFab);

            mRecycler.addOnScrollListener(cstmScroller);


            fragmentContainer.addView(mRecycler);

            //viewItem.setIcon(R.drawable.ic_grid_white_24dp);

        } else if (mode == 1) {///GridView

            fragmentContainer.removeAllViewsInLayout();

            int width=0;
            float height=0;
            int cardWidth=0;
            int carGap=10;

            if (screenWidth>screenHeight){


                width=(screenHeight/2);

                height=width;
                cardWidth=(screenWidth/2);
                carGap=8;

                fragmentContainer.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,((int) height+(width/4))));
            }else{


                cardWidth=screenWidth-(((int)getContext().getResources().getDimension(R.dimen.padding))*4);
                width=screenWidth;
                float factor = 1.5f;
                height = width/factor;


                carGap=8;

                fragmentContainer.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,((int ) height)));
            }




            fragmentContainer.setBackground(getResources().getDrawable(R.drawable.drawable_color_primary_gradient));
            mRecycler.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(int)height));




            //   infoContainer.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,height));

            nColumn = 1;

            itemDecoration = new GridSpacingItemDecoration(nColumn, 1, true);
            mRecycler.addItemDecoration(itemDecoration);
            mRecycler.setLayoutManager(new CardSliderLayoutManager((screenWidth/2)-(cardWidth/2), cardWidth, carGap,3,null));

            new com.colorbuzztechgmail.spf.CardSnapHelper().attachToRecyclerView(mRecycler);

            CustomScrollListener customScrollListener = new CustomScrollListener(mRecycler);
            mRecycler.addOnScrollListener(customScrollListener);

            fragmentContainer.addView(mRecycler);
            //infoContainer.setVisibility(View.VISIBLE);

            //viewItem.setIcon(R.drawable.ic_list_white_24dp);
        }
        //selectCategory((String) spinnerCategory.getItem(selectedPos));
    }

    public void selectCategory(String category) {

         setSelectedCategory(category);


        if (!category.equals(getResources().getString(R.string.importRecent_Cat))) {


            bufferModel = filterType(ma.userData.loadData(), category, filterMode);
            fillModelDataset(bufferModel);


        } else {

            for (PreviewModelInfo model:ma.userData.getLastesModel(lastImportsCount) ){
                bufferModel.add(model);

            }


           fillModelDataset(bufferModel);

        }

        mRecycler.scrollToPosition(0);


        refreshInfobar();

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public String getSelectedCategory() {
        return selectedCategory;
    }

    public void setSelectedCategory(String selectedCategory) {
        this.selectedCategory = selectedCategory;
    }

    public int getSearchMode() {
        return searchMode;
    }

    public void setSearchMode(int searchMode) {
        this.searchMode = searchMode;
    }

    public void setActivity(MainActivity ma) {
        this.ma = ma;

    }

    public int getScreenContainerHeight() {
        return ScreenContainerHeight;
    }

    public void setScreenContainerHeight(int screenContainerHeight) {
        ScreenContainerHeight = screenContainerHeight;
    }

    private void setupRecycler(){

        mRecycler=new RecyclerView(getContext());
        mRecycler.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        Utils.setMargins(mRecycler,8,0,8,0);
        mRecycler.setHasFixedSize(true);
        modelAdapter = new ModelListAdapter(this,ma);
        modelDataset= new ModelDataset(mRecycler,modelAdapter);
        modelAdapter.dataset(modelDataset);
        mRecycler.setAdapter(modelAdapter);
    }

    private void fillModelDataset(List<Object>models){

            modelDataset.removeAll();
            modelDataset.AutomaticTitleGeneratorSortType(models,true);

    }

    public int getScreenContainerWidth() {
        return ScreenContainerWidth;
    }

    public void setScreenContainerWidth(int screenContainerWidth) {
        ScreenContainerWidth = screenContainerWidth;
    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static final List<Object> filterByParameter(List<Object> filelist, String query, int type) {


        final String lowerCaseQuery = query.toLowerCase();

        final List<Object> filteredModelList = new ArrayList<>();
        for (Object obj : filelist) {

            if (type == SEARCH_IN_CATEGORY) {// Categorias
                final String text =((PreviewModelInfo)obj).getCategory().toLowerCase();
                if (text.contains(lowerCaseQuery)) {
                    filteredModelList.add(obj);
                }
            }

            if (type == SEARCH_IN_NAME) {// Nombre
                final String text = ((PreviewModelInfo)obj).getName().toLowerCase();
                if (text.contains(lowerCaseQuery)) {
                    filteredModelList.add(obj);
                }
            }

            if (type == SEARCH_IN_CUSTUMER) {// Nombre

            }

            if (type == SEARCH_IN_SEASONS) {// Nombre
                final String text = ((PreviewModelInfo)obj).getDate().toLowerCase();
                if (text.contains(lowerCaseQuery)) {
                    filteredModelList.add(obj);
                }
            }

        }
        return filteredModelList;
    }

    private static List<Object> filterType(List<PreviewModelInfo> filelist, String query, int type) {
        final String lowerCaseQuery = query.toLowerCase();

        final List<Object> filteredModelList = new ArrayList<>();
        for (PreviewModelInfo model : filelist) {

            if (type ==FILTER_CATEGORY) {// tipo
                final String text = model.getCategory().toLowerCase();
                if (text.contains(lowerCaseQuery)) {
                    filteredModelList.add(model);
                }
            }
            else  {//Todos

                filteredModelList.add(model);


            }
        }
        return filteredModelList;
    }

    public void refreshInfobar( ) {
          int count=0;
        if (this.isVisible()) {


            count=modelDataset.getItemCount();
            String aux = getSelectedCategory();
            aux += " ";
            aux +=String.valueOf(count);

           ma.myToolbar.setTitle(getString(R.string.modelosText));
            ma.myToolbar.setSubtitle(aux);
        }
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
               // customSearchView.setQuery("",false);
                customSearchView.onActionViewCollapsed();
                ma.myToolbar.removeViewInLayout(customSearchView);
                ma.myToolbar.removeViewInLayout(v);
                ma.myToolbar.getMenu().setGroupVisible(R.id.group_search,false);
                ma.myToolbar.getMenu().setGroupVisible(R.id.group_options,true);
                //ma.myToolbar.getMenu().setGroupVisible(0,true);
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                ma.setToggle();

            }
        });

        customSearchView.onActionViewExpanded();

        ma.myToolbar.addView(customSearchView);



    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void importSpfFiles() {


        List<FileItem> model = CachePot.getInstance().pop(ArrayList.class);
        Object[] obj = new Object[model.size() - 1];

        obj = model.toArray();
        ma.showProgress(true,(model.size()-1)*100);
        new importAsync().execute(obj);

       /* if (this.isVisible()) {

            selectCategory(ma.spinnerAdapter.getItem(selectedPos));
        }*/

    }

    public void importJsonFiles(String name, int position) {

        String fileName = name;
        fileName += ".json";


        Object[] obj = new Object[2];

        obj[0] = fileName;
        obj[1] = position;

        new importJsonAsync().execute(obj);

       /* if (this.isVisible()) {

            selectCategory(ma.spinnerAdapter.getItem(selectedPos));
        }*/

    }

    @Override
    public void onClick(View v) {

        switch ( v.getId()){


            case R.id.fabbutton:

                ma.setItemNavigationView(0);

                break;

        }

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
      if(bufferModel.size()>0) {

        final List<Object> filteredModelList = filterByParameter( bufferModel, query,getSearchMode());
        if(filteredModelList.size()>0){
            modelDataset.removeAll();
            modelDataset.AutomaticTitleGeneratorSortType(filteredModelList,false);
            mRecycler.scrollToPosition(0);


        }else{
            modelDataset.removeAll();

            modelDataset.add( new CustomHeader("No hay coincidencias ",0,getResources().getDrawable(R.drawable.ic_info_black_24dp)));
        }


      }
        return false;
    }

    public void showSnackbar(String mssg,final String category) {

        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) ma.findViewById(R.id.parentLayout);
        Snackbar.make(coordinatorLayout, mssg, Snackbar.LENGTH_LONG).setAction("VER", new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ma.setItemNavigationView(1);
                setSelectedCategory(category);
                ;
            }
        }).show();

    }



    private void showAddNewModel(){


        Dialog d= new ModelEditPopUp(getContext(),null,true,this);

        d.setOwnerActivity(getActivity());
        d.show();

    }

    private void showEditDialog(int modelId){

      Dialog d= new ModelEditPopUp(getContext(),modelDataset.getPreviewModelInfoById(modelId),true,this);
        d.create();


     d.setOwnerActivity(getActivity());
     d.show();

    }

    private void showRemoveDialog(final SparseBooleanArray checkedList){



        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getContext());
        alertDialogBuilder
                .setTitle( getResources().getString(R.string.dialogTitle_remove))
                //.setMessage( getResources().getString(R.string.dialogMsg_remove))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.dialog_remove),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                final ArrayList<Integer> removeIds=new ArrayList<Integer>();
                                if(checkedList.size()>0) {

                                    final List<PreviewModelInfo>models=new ArrayList<>();
                                    for(int i=0;i<checkedList.size();i++) {

                                        if(checkedList.valueAt(i)) {


                                                final PreviewModelInfo model=modelDataset.getPreviewModelInfoById(checkedList.keyAt(i));

                                                models.add(model);

                                                removeIds.add(checkedList.keyAt(i));



                                        }
                                    }

                                    modelDataset.removeByIds(removeIds);
                                    refreshInfobar();
                                    modelAdapter.multiChoiceHelper.clearChoices();
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

    private void showUndoSnackbar(final List<PreviewModelInfo> modelList){

        CoordinatorLayout view = (CoordinatorLayout)ma.findViewById(R.id.parentLayout);

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

                            modelDataset.add(modelList);
                            refreshInfobar();

                    }

                }).setCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar snackbar, int dismissType) {
                        super.onDismissed(snackbar, dismissType);

                        if(dismissType !=DISMISS_EVENT_ACTION){

                            for(PreviewModelInfo model: modelList){

                                ModelDataBase db= new ModelDataBase(getContext());

                                db.deleteModelData(model.getId());


                            }


                        }

                    }
                });


        snackbar.show();
    }

    public void showAddCategory(final View.OnClickListener listener){


           final ModelDataBase db=new ModelDataBase(getContext());

            LayoutInflater li=LayoutInflater.from(getContext());

            final View promptsView = li.inflate(R.layout.editcategory_popup, null);
            final Button cancelBtn=(Button)promptsView.findViewById(R.id.cancel_btn);
            final Button saveBtn=(Button)promptsView.findViewById(R.id.save_btn);
            final TextView titleText=(TextView) promptsView.findViewById(R.id.titleEditTextView);



            final ImageView tilteImg=(ImageView)promptsView.findViewById(R.id.addImageView);

            ((LinearLayout)promptsView.findViewById(R.id.di)).removeAllViewsInLayout();

            final EditText nametxt=new EditText(getContext());
            nametxt.setHint(getResources().getString(R.string.categoryHint));
            nametxt.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            ((LinearLayout)promptsView.findViewById(R.id.di)).addView(nametxt);



            titleText.setText(getResources().getString(R.string.action_newCategory));
            tilteImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_plus_black_24dp));

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    getContext());
            alertDialogBuilder.setView(promptsView);


            final Dialog alertDialog = alertDialogBuilder.create();

            alertDialog.setCanceledOnTouchOutside(false);

            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertDialog.show();




            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    alertDialog.dismiss();

                }
            });

            saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String category = nametxt.getText().toString();

                    category = category.trim();


                    if (!(category.equals(""))) {
                        if (!(db.existCategory(category))) {

                            db.insertCategory(category);
                            alertDialog.dismiss();
                            listener.onClick(saveBtn);

                        } else {

                            Utils.toast(getContext(), getContext().getString(R.string.warning_exist));

                        }


                    }else {

                        Utils.toast(getContext(), getContext().getString(R.string.warning_emptyCategory));

                    }
                }
            });







    }

    public void showCategoryChooser(){

        final ModelDataBase db=new ModelDataBase(getContext());


        final List<String>categories=db.getCategoryString();


        LayoutInflater li=LayoutInflater.from(getContext());

        View v=li.inflate(R.layout.save_piece_popup,null);

        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());

        builder.setView(v);


        final Dialog d=builder.create();

        ((Toolbar)v.findViewById(R.id.pieceToolbar)).setTitle(getResources().getString(R.string.action_category) + " (" + String.valueOf(categories.size()) + ")");
        ((Toolbar)v.findViewById(R.id.pieceToolbar)).setNavigationIcon(getResources().getDrawable(R.drawable.ic_close_black_24dp));
        ((Toolbar)v.findViewById(R.id.pieceToolbar)).inflateMenu(R.menu.menu_add_edit);

        ((Toolbar)v.findViewById(R.id.pieceToolbar)).setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()){


                    case R.id.action_editCategory:


                        CategoryEditPopUp categoryEditPopUp= new CategoryEditPopUp(getContext(), new Utils.onSavedInterface() {
                            @Override
                            public void onSaved(Object obj, int position, boolean isEditable) {

                                d.dismiss();
                                showCategoryChooser();

                            }
                        });



                        categoryEditPopUp.create();
                        categoryEditPopUp.show();
                        return true;


                    case R.id.action_add:

                        showAddCategory(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                d.dismiss();
                                showCategoryChooser();
                            }
                        });

                        break;

                }
                return false;
            }
        });

        ((Toolbar)v.findViewById(R.id.pieceToolbar)).setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
           d.dismiss();
            }
        });
        final ArrayList<ListPopUpMenuItem> items =new ArrayList<>();

        for(String category:categories){

          if(category.equals(getResources().getString(R.string.importRecent_Cat))
                  ||category.equals(getResources().getString(R.string.importNoAsigned_Cat)) ){

              items.add(new ListPopUpMenuItem(category,R.drawable.ic_folder_accent_24dp));

          }else{

              items.add(new ListPopUpMenuItem(category,R.drawable.ic_folder_black_24dp));

          }





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

                        String category=((TextView)v.findViewById(R.id.text)).getText().toString();
                       // Utils.toast(getContext(),category);
                        d.dismiss();
                        selectCategory(category);

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

    @Override
    public void colorChanged(int color) {

    }

    @Override
    public void onPreview(Object obj) {

    }

    @Override
    public void toRemove(Object obj) {
        showRemoveDialog((SparseBooleanArray)obj);
    }

    @Override
    public void toEdit(int modelId) {

        showEditDialog(modelId);


    }

    @Override
    public void onClick(View v, int position, boolean isLongClick) {

        switch (v.getId()){

           default:

                Intent i = new Intent(ma,ModelActivity.class);
                i.putExtra("MODEL",String.valueOf(modelDataset.getPreviewModelInfo(position).getId()));
                ma.startActivity(i);
                break;


            case R.id.imageViewProfile:

                 ma.imagePicker(ma.REQUEST_CODE_MODEL_IMAGE_GALLERY,modelDataset.getPreviewModelInfo(position),v);

                break;

        }

    }

    @Override
    public void onSaved(Object obj,int position, boolean isEditable) {


        PreviewModelInfo model = (PreviewModelInfo) obj;
        if (model.getImage() == null) {
            model.setImage(getResources().getDrawable(R.mipmap.ic_launcher));
        }
        if (!isEditable) {
            //ma.userData.addNewModel(model);

        } else {
            ma.userData.updatePreviewData(model);
            final ArrayList<Integer> aux=new ArrayList<>();
            aux.add(model.getId());
            modelDataset.removeByIds(aux);
        if (!getSelectedCategory().equals(getResources().getString(R.string.importRecent_Cat))) {


            if (isVisible(model)) {

                modelDataset.updateHeaderForNewItem(model);
            }
        } else {
            selectCategory(getResources().getString(R.string.importRecent_Cat));

        }

    }

      ((ModelListAdapter)mRecycler.getAdapter()).multiChoiceHelper.clearChoices();

        refreshInfobar();

    }


    private boolean isVisible(PreviewModelInfo model){

        switch (filterMode) {
            //Tipo
            case FILTER_CATEGORY:
                if (model.getCategory().equals(getSelectedCategory())) {


                   return true;


                }
                return false;
            case FILTER_ALL:

                return true;

        }

        return false;

    }

    public class importAsync extends AsyncTask<Object, Float, List<Object>> {

        protected void onPreExecute() {


        }

        protected List<Object> doInBackground(Object[] urls) {

            List<Object> bufferList = new ArrayList<>();
            String text = "";
            List<Object> files;
            files = new ArrayList<>();


            files.addAll(Arrays.asList(urls));
           for (int i = 0; i < files.size(); i++) {

                try {
                     FileInputStream fis = new FileInputStream(((FileItem) files.get(i)).getPath());
                    int size = fis.available();
                    byte[] buffer = new byte[size];
                    fis.read(buffer);
                    fis.close();
                    text = new String(buffer,"ISO-8859-1");

                    final SPFdecoder decoder = new SPFdecoder(text,getContext());
                    final PreviewModelInfo preview = new PreviewModelInfo();
                    decoder.getSpfModel().setParent(((FileItem) files.get(i)).getParent());
                    decoder.getSpfModel().setDate(Utils.getDate());


                    decoder.getSpfModel().setCategory(((FileItem) files.get(i)).getCategory());


                    // decoder.getSpfModel().setBmp(getContext().getResources().getDrawable(R.drawable.ic_leather));

                    updateDataBase(decoder.getSpfModel());
                    preview.setId(ma.userData.getLastIndexModel());
                    decoder.getSpfModel().setId(preview.getId());
                    preview.setName(decoder.getSpfModel().getName());
                    preview.setPieceCount(decoder.getSpfModel().getPieceCount());
                    preview.setParent(decoder.getSpfModel().getParent());
                    preview.setDate(decoder.getSpfModel().getDate());
                    preview.setCategory(decoder.getSpfModel().getCategory());
                    preview.setSizeList(decoder.getSpfModel().getSize());
                    preview.setMaterialCount(decoder.getSpfModel().getMaterialCount());
                    preview.setDescription(decoder.getSpfModel().getDescription());

                  bufferList.add(preview);
                    //saveModelToFile(decoder.getSpfModel());

                    // bufferList.add(decoder.getSpfModel());
                    publishProgress(i * 100f);

                    Log.e("Importacion", ((FileItem) files.get(i)).getPath());

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("Fallo importacion", "Error:" +
                            e.toString() + "Modelo: " +((FileItem) files.get(i)).getName() );


                }
            }

            return bufferList;
        }

        protected void onProgressUpdate(Float... valores) {
            int p = Math.round(valores[0]);
            ma.mProgressBar.setProgress(p);
        }

        protected void onPostExecute(List<Object> model) {

            if (!model.isEmpty()) {


                //  modelList.addAll(model);
                modelDataset.addAll(model);

                ma.showProgress(false,0);
                showSnackbar(String.valueOf(model.size()) + " " + "Modelos" + " importados",((PreviewModelInfo)model.get(0)).getCategory());
            }






        }


    }

    private class importJsonAsync extends AsyncTask<Object, Float, SparseArray<Model>> {

        protected void onPreExecute() {


        }

        protected SparseArray<Model> doInBackground(Object[] path) {


            SparseArray<Model> sparseArray = new SparseArray<>();


            try {

                final String json = readFile(getContext(), (String) path[0]);
                Model model = jsonToModel(json);


               // modelAdapter.expandModel.append((int) path[1], model);
                sparseArray.append((int) path[1], model);

            } catch (Exception e) {

                e.printStackTrace();
                Log.e("Import", e.toString());


            }


            return sparseArray;
        }

        protected void onProgressUpdate(Float... valores) {
            int p = Math.round(valores[0]);
            ma.mProgressBar.setProgress(p);
        }

        protected void onPostExecute(SparseArray<Model> model) {


            //modelAdapter.stopLoad(model.keyAt(0));


        }


    }

    public void updateDataBase(Model model) {

        ma.userData.addNewModel(model);

    }

    public void saveModelToFile(Model model) {

        GsonBuilder builder = new GsonBuilder();
        builder.serializeNulls();
        Gson gson = builder.create();
        String name = String.valueOf(model.getId());
        name += "_" + model.getName();
        boolean isFileCreated = createFile(getContext(), name, gson.toJson(model));
        if (isFileCreated) {
            Log.e("Import", name);
            Log.e("Import", " saved to SPF+");

        } else {
            Log.e("Import", name);
            Log.e("Import", " not saved to SPF+");


        }

    }

    /////////////////////////////////////////////////////////////////////

    public String readFile(Context context, String name) {
        File sdcard = new File(ma.dataFolder);

//Get the text file
        File file = new File(sdcard, name);

//Read text from file
        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
        } catch (IOException e) {

            Utils.toast(getContext(), "Unreadable file");
        }

        return text.toString();
    }

    private boolean createFile(Context context, String fileName, String jsonString) {

        try {
            Writer output = null;
            File file = new File(ma.dataFolder + "/" + fileName + ".json");
            output = new BufferedWriter(new FileWriter(file));
            if (jsonString != null) {
                output.write(jsonString);
            }
            output.close();
            return true;
        } catch (IOException ioException) {

            Log.e("Import", ioException.toString());
            return false;
        }

    }

    public boolean isFilePresent(Context context, String Dir, String fileName) {
        String path = Dir + "/" + fileName;
        File file = new File(path);
        return file.exists();
    }

    public boolean removeFile(String fileName) {

        boolean removed = false;

        if (isFilePresent(getContext(), ma.dataFolder, fileName + ".json")) {

            final File file = new File(ma.dataFolder + "/" + fileName + ".json");

            try {
                file.delete();
                removed = true;
                Utils.toast(getContext(), fileName + "REMOVED");

            } catch (Exception ioException) {

                ioException.printStackTrace();
            }

        }


        return removed;
    }

    public boolean editFile(ContentValues values) {

        boolean edited = false;

        if (isFilePresent(getContext(), ma.dataFolder, values.getAsString("oldName") + ".json")) {

            final File file = new File(ma.dataFolder + "/" + values.getAsString("oldName") + ".json");

            try {

                final String json = readFile(getContext(), values.getAsString("oldName") + ".json");

                if (json != null) {

                    removeFile(values.getAsString("oldName"));
                    Gson gson = new Gson();
                    Model model = (Model) gson.fromJson(json, Model.class);
                    model.setName(values.getAsString("newName"));
                    model.setCategory(values.getAsString("newCategory"));
                    saveModelToFile(model);


                    edited = true;


                }


            } catch (Exception ioException) {

                ioException.printStackTrace();
                return false;
            }

        }


        return edited;
    }

    public Model jsonToModel(String json) {

        Gson gson = new Gson();
        Model model = (Model) gson.fromJson(json, Model.class);
        return model;
    }





}