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
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by PC01 on 21/06/2017.
 */
public class CutNotesListFragment extends Fragment  implements View.OnClickListener ,SearchView.OnQueryTextListener,ItemActionListener ,
AddCutNotePopUp.onSavedInterface{

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
    public static final int FILTER_STATUS=0;


    //Models search at value
    public static final int SEARCH_IN_NAME=0;
    public static final int SEARCH_IN_MODEL=1;
    public static final int SEARCH_IN_REFERENCE=2;


    private int searchMode=SEARCH_IN_MODEL;
    public CutNoteListDataset cutNotesDataset;

    public CutNoteListAdapter cutNotesAdapter;

     public List<Object> cutNotesBuffer= new ArrayList<>();

     private CutNoteGenerator.GeneratorType mGeneratorType= CutNoteGenerator.GeneratorType.MULTI;




    //Display
    public RecyclerView mRecycler;
    private GridSpacingItemDecoration itemDecoration;
    private int nColumn = 0;
    private int spaceItem = 30;
    private boolean edges = true;
     public ItemTouchHelper touchHelper;

    public int filterMode = FILTER_ALL;
    public int ViewMode = 0;
    public MainActivity ma;


    private String selectedCategory=null;
    private int selectedPos = 0;
    public int lastImportsCount =300;

    private FrameLayout fragmentContainer;


    public int ScreenContainerWidth;
    public int ScreenContainerHeight;
    private FloatingActionButton importFab;

    public void setFilterMode(int filterMode) {
        this.filterMode = filterMode;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public CutNotesListFragment() {
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

        importFab.setOnClickListener(this);
        fragmentContainer = (FrameLayout) view.findViewById(R.id.modelFragmentContainer);
        setScreenContainerWidth(ma.getWindowManager().getDefaultDisplay().getWidth());
        setScreenContainerHeight(ma.getWindowManager().getDefaultDisplay().getHeight());




        return view;

    }

 

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_cutnotes,menu);
        super.onCreateOptionsMenu(menu, inflater);
        categoryItem= menu.findItem(R.id.action_filter);

        customView(ViewMode, getScreenContainerWidth(),getScreenContainerHeight());

        selectCategory(getSelectedCategory());

         start=true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {


            case R.id.action_filter:

                showStatusFilterChooser();

            break;

            case R.id.action_searchBar:
               loadSearchBar();

                break;
            case R.id.action_searchName:
                item.setChecked(true);

                setSearchMode(SEARCH_IN_NAME);
                break;

            case R.id.action_searchCustumer:

                setSearchMode(SEARCH_IN_MODEL);

                break;
            case R.id.action_searchCategories:
                item.setChecked(true);


                setSearchMode(SEARCH_IN_REFERENCE);

                break;
            


            case R.id.action_sortModel:
                item.setChecked(true);
                cutNotesDataset.setSortType(CutNoteListDataset.SortType.MODEL);
                cutNotesDataset.changeSortType( );

                break;
            case R.id.action_sortState:
                item.setChecked(true);
                cutNotesDataset.setSortType(CutNoteListDataset.SortType.STATUS);

                cutNotesDataset.changeSortType( );

                break;
            case R.id.action_sortDate:
                item.setChecked(true);
                cutNotesDataset.setSortType(CutNoteListDataset.SortType.LAST);

                cutNotesDataset.changeSortType( );
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
            HeaderItemDecoration headerItemDecoration=new HeaderItemDecoration(mRecycler,cutNotesAdapter);
            final GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), nColumn, LinearLayoutManager.VERTICAL, false);
            itemDecoration = new GridSpacingItemDecoration(nColumn, 8, false);
            mRecycler.addItemDecoration(itemDecoration);
            mRecycler.addItemDecoration(headerItemDecoration);

            mRecycler.setLayoutManager(gridLayoutManager);

            final CustomScrollListenerFadeView cstmScroller=new CustomScrollListenerFadeView(getContext(),importFab);

            mRecycler.addOnScrollListener(cstmScroller);


            fragmentContainer.addView(mRecycler);

            //viewItem.setIcon(R.drawable.ic_grid_white_24dp);

        
        }
        //selectCategory((String) spinnerCategory.getItem(selectedPos));
    }

    public void selectCategory(String category) {

         setSelectedCategory(category);





            cutNotesBuffer = filterType(ma.userData.getAllCutNotes(), category, filterMode);
            fillCutNoteDataset(cutNotesBuffer);


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

        cutNotesAdapter = new CutNoteListAdapter(this,ma);
        cutNotesDataset=new CutNoteListDataset(mRecycler,cutNotesAdapter);
        cutNotesAdapter.dataset(cutNotesDataset);
        mRecycler.setAdapter(cutNotesAdapter);
    }

    private void fillCutNoteDataset(List<Object>cutNoteLists){

            cutNotesDataset.removeAll();
            cutNotesDataset.AutomaticTitleGeneratorSortType(cutNoteLists,true);

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
            final CutNoteList cutNoteList=(CutNoteList)obj;

            if (type == SEARCH_IN_REFERENCE) {// Categorias
                final String text = String.valueOf(cutNoteList.getReference());
                if (text.contains(lowerCaseQuery)) {
                    filteredModelList.add(cutNoteList);
                }
            }

            if (type == SEARCH_IN_MODEL) {// Nombre
                final String text = cutNoteList.getModel().toLowerCase();
                if (text.contains(lowerCaseQuery)) {
                    filteredModelList.add(cutNoteList);
                }
            }

            if (type == SEARCH_IN_MODEL) {// Nombre

            }



        }
        return filteredModelList;
    }

    private   List<Object> filterType(List<CutNoteList> filelist, String query, int type) {
        final String lowerCaseQuery = query.toLowerCase();

        final List<Object> filteredModelList = new ArrayList<>();
        for (CutNoteList cutNoteList : filelist) {

            if (type ==FILTER_STATUS) {// tipo
                final String text =cutNotesDataset.convertStatusToString(cutNoteList.getStatus()).toLowerCase();
                if (text.contains(lowerCaseQuery)) {
                    filteredModelList.add(cutNoteList);
                }
            }
            else if(type==FILTER_ALL) {//Todos

                filteredModelList.add(cutNoteList);


            }
        }
        return filteredModelList;
    }

    public void refreshInfobar( ) {
          int count=0;
        if (this.isVisible()) {

            count=cutNotesDataset.getItemCount();
            String aux = getSelectedCategory();
            aux += " ";
            aux +=String.valueOf(count);

            ma.myToolbar.setTitle(getString(R.string.cutNotesList));
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
                customSearchView.setQuery("",false);
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

    @Override
    public void onClick(View v) {

        switch ( v.getId()){


            case R.id.fabbutton:

               Dialog d= new AddCutNotePopUp(getContext(),mGeneratorType,this);
               d.setOwnerActivity(ma);
               d.show();

                break;

        }

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
      if(cutNotesBuffer.size()>0) {

        final List<Object> filteredModelList = filterByParameter(cutNotesBuffer, query,getSearchMode());
        if(filteredModelList.size()>0){
            cutNotesDataset.removeAll();
            cutNotesDataset.AutomaticTitleGeneratorSortType(filteredModelList,false);
            mRecycler.scrollToPosition(0);


        }else{
            cutNotesDataset.removeAll();

            cutNotesDataset.add( new  CustomHeader("No hay coincidencias ",0,getResources().getDrawable(R.drawable.ic_info_black_24dp)));
        }


      }
        return false;
    }

    private void showEditDialog(int Id){

        /*Dialog d= new ModelEditPopUp(getContext(),cutNotesDataset.getCutNoteListById( Id),true,this);

     d.setOwnerActivity(getActivity());
     d.show();*/
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

                                    final List<CutNoteList>cutNotes=new ArrayList<>();
                                    for(int i=0;i<checkedList.size();i++) {

                                        if(checkedList.valueAt(i)) {


                                                final CutNoteList cutNoteList=cutNotesDataset.getCutNoteListById(checkedList.keyAt(i));

                                                cutNotes.add(cutNoteList);

                                                removeIds.add(checkedList.keyAt(i));



                                        }
                                    }

                                    cutNotesDataset.removeByIds(removeIds);
                                    refreshInfobar();
                                    cutNotesAdapter.multiChoiceHelper.clearChoices();
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

        CoordinatorLayout view = (CoordinatorLayout)ma.findViewById(R.id.parentLayout);

        String mssg;

        if(cutNoteList.size()==1){

            mssg = cutNoteList.get(0).getModel() + " eliminados";

        }else {

            mssg = String.valueOf(cutNoteList.size()) + " Notas " + "eliminados";

        }

        Snackbar snackbar = Snackbar.make(view, mssg, Snackbar.LENGTH_LONG)
                .setAction(getContext().getString(R.string.action_undo), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                            cutNotesDataset.add(cutNoteList);
                            refreshInfobar();

                    }

                }).setCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar snackbar, int dismissType) {
                        super.onDismissed(snackbar, dismissType);

                        if(dismissType !=DISMISS_EVENT_ACTION){

                            for(CutNoteList cutNotes: cutNoteList){

                                ModelDataBase db= new ModelDataBase(getContext());

                                db.deleteCutNoteList(cutNotes.getId());


                            }


                        }

                    }
                });


        snackbar.show();
    }

    public void showStatusFilterChooser(){

        String[] statusList =getContext().getResources().getStringArray(R.array.cutNotes_status);


        LayoutInflater li=LayoutInflater.from(getContext());

        View v=li.inflate(R.layout.save_piece_popup,null);

        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());

        builder.setView(v);


        final Dialog d=builder.create();

        ((Toolbar)v.findViewById(R.id.pieceToolbar)).setTitle(getResources().getString(R.string.cutNotes_status));
        ((Toolbar)v.findViewById(R.id.pieceToolbar)).setNavigationIcon(getResources().getDrawable(R.drawable.ic_keyboard_arrow_left_black_24dp));

        ((Toolbar)v.findViewById(R.id.pieceToolbar)).setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
           d.dismiss();
            }
        });
        final ArrayList<ListPopUpMenuItem> items =new ArrayList<>();

        items.add(new ListPopUpMenuItem(getResources().getString(R.string.action_sortAll),new ShapeGenerator(getContext()).getDrawableShape(null,ShapeGenerator.MODE_ROUND_RECT,
                getResources().getColor(R.color.selectItemList),ShapeGenerator.SIZE_SMALL)));

        items.add(new ListPopUpMenuItem(statusList[0],new ShapeGenerator(getContext()).getDrawableShape(null,ShapeGenerator.MODE_ROUND_RECT,
                getResources().getColor(R.color.colorGreen_A400),ShapeGenerator.SIZE_SMALL)));

        items.add(new ListPopUpMenuItem(statusList[1],new ShapeGenerator(getContext()).getDrawableShape(null,ShapeGenerator.MODE_ROUND_RECT,
                getResources().getColor(R.color.colorAmber_A400),ShapeGenerator.SIZE_SMALL)));

        items.add(new ListPopUpMenuItem(statusList[2],new ShapeGenerator(getContext()).getDrawableShape(null,ShapeGenerator.MODE_ROUND_RECT,
                getResources().getColor(R.color.colorRed_A400),ShapeGenerator.SIZE_SMALL)));





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

                            setFilterMode(FILTER_ALL);
                            selectCategory(text);

                        }else {
                            setFilterMode(FILTER_STATUS);
                            selectCategory(cutNotesDataset.convertStatusToString(CutNote.cutNoteStatus.values()[position-1]));


                        }
                        // Utils.toast(getContext(),category);
                        d.dismiss();

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

               ///CutNoteActivity

               /* Intent i = new Intent(ma,ModelActivity.class);
                i.putExtra("MODEL",String.valueOf(cutNotesDataset.getPreviewModelInfo(position).getId()));
                ma.startActivity(i);
                break;*/




        }

    }

    @Override
    public void onSaved(Object obj,int position, boolean isEditable) {

        cutNotesDataset.updateHeaderForNewItem((CutNoteList)obj);

        refreshInfobar();


    }


    private boolean isVisible(CutNoteList cutNoteList){

        switch (filterMode) {
            //Tipo
            case FILTER_STATUS:
                if ((String.valueOf(cutNoteList.getStatus())).equals(getSelectedCategory())) {


                   return true;


                }
                return false;
            case FILTER_ALL:

                return true;

        }

        return false;

    }






}