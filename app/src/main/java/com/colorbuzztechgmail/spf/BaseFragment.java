package com.colorbuzztechgmail.spf;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.colorbuzztechgmail.spf.dataset.Dataset;
import com.colorbuzztechgmail.spf.adapter.BaseAdapter;

import java.util.ArrayList;
import java.util.List;
import com.colorbuzztechgmail.spf.Utils.ViewMode;
import com.colorbuzztechgmail.spf.CategoryFragment.ChooserType;

;


/**
 * Created by PC01 on 21/06/2017.
 */
public abstract class BaseFragment extends Fragment implements SearchView.OnQueryTextListener,ItemActionListener,Utils.onSavedInterface{

    public  enum FragmentType{

        MODEL,
        MATERIAL,
        CUTNOTE,
        RECENT

    }



    private String myTag="";

    public static String TAG="tag";
    public static String FRAGMENT_TYPE="fragment_type";
    public   static final String CHOOSER_TYPE="chooserType";
    public static final String CATEGORY="category";
    public RecyclerView mRecycler;
    private GridSpacingItemDecoration itemDecoration;
    private int nColumn = 0;
    public  ViewMode ViewMode = Utils.ViewMode.LIST;
    private FragmentType frType=null;
    private CategoryFragment.ChooserType chooserType;
    private  boolean start=false;


    public boolean collpseTitle=false;
    private Dataset dataset;


    //Display



   private String selectedCategory=null;
   public int lastImportsCount =10;
    public FrameLayout fragmentContainer;
    private FloatingActionButton importFab;
    private ModelDataBase db;
    private CustomSearchView customSearchView;
    private ActionMode choiceActionMode;
    private Menu menu;

   public static BaseFragment newInstance(String fragmentType,String category,String chooserType){

        Bundle args = new Bundle();



                RecentFragment c = new RecentFragment();
                args.putString(FRAGMENT_TYPE,fragmentType);
                args.getString(CATEGORY,category);
                args.putString(CHOOSER_TYPE,chooserType);
                c.setArguments(args);



       return c;


    }

   public BaseFragment(){
            super();
    }

   public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.base_fragment, container, false);
        setHasOptionsMenu(true);

       setFrType(FragmentType.valueOf(getArguments().getString(FRAGMENT_TYPE)));

       fragmentContainer = (FrameLayout) view.findViewById(R.id.fragmentContainer);

        customView();

        return view;

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments().getString(TAG)!=null)
            setMyTag(getArguments().getString(TAG));

        if(getArguments().getString(FRAGMENT_TYPE)!=null)
            setFrType(FragmentType.valueOf(getArguments().getString(FRAGMENT_TYPE)));

        if(getArguments().getString(CHOOSER_TYPE)!=null)
            setChooserType(ChooserType.valueOf(getArguments().getString(CHOOSER_TYPE)));

        if(getArguments().getString(CATEGORY)!=null)
            setSelectedCategory(getArguments().getString(CATEGORY));

        db=new ModelDataBase(getContext());

        setupRecycler();

        initializeValues();

        setupAdapter();

        refreshInfobar(getMyTag());


        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_home_selector);



        importFab=(FloatingActionButton)getActivity().findViewById(R.id.fabbutton);
        importFab.setVisibility(View.GONE);
        importFab.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_plus_white_40dp));
        importFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewItem();
            }
        });



        customSearchView=new CustomSearchView(getContext());
        customSearchView.setOnQueryTextListener(this);


    }

    public void setupRecycler(){

       mRecycler= new RecyclerView(getContext());
        mRecycler.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mRecycler.setHasFixedSize(true);


        HeaderItemDecoration headerItemDecoration=new HeaderItemDecoration(mRecycler,getAdapter());
        mRecycler.addItemDecoration(headerItemDecoration);
       mRecycler.setAdapter(getAdapter());


   };

   private void setupAdapter(){


        getAdapter().dataset(dataset);


   };

   public void setDataset(Dataset dataset){

       this.dataset=dataset                         ;


   }

   protected abstract void initializeValues();

   protected abstract BaseAdapter getAdapter();

   protected abstract List<Object> getRecentItems();

    protected abstract  Object getObjectToLoad(Cursor mCursor);


    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    public FragmentType getFrType() {
        return frType;
    }

    public void setFrType(FragmentType frType) {
        this.frType = frType;
    }

    public String getMyTag() {
        return myTag;
    }

    public void setMyTag(String myTag) {
        this.myTag = myTag;
    }

    public String getSelectedCategory() {
        return selectedCategory;
    }

    public void setSelectedCategory(String selectedCategory) {
        this.selectedCategory = selectedCategory;
    }

    public void setChooserType(ChooserType type){

       this.chooserType=type;

    }

    public ChooserType getChooserType() {
        return chooserType;
    }

    public Utils.ViewMode getViewMode() {
        return ViewMode;
    }

    public void setViewMode(Utils.ViewMode viewMode) {
        ViewMode = viewMode;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void customView() {


        fragmentContainer.removeAllViewsInLayout();

        int spacing=Utils.pxTodp(8,getContext());


        //Utils.toast(getContext(),"N View:" + String.valueOf(nView));
        //  mRecycler.removeItemDecoration(itemDecoration);

        switch (ViewMode){



            case LIST:


               getAdapter().setViewMode(Utils.ViewMode.LIST);



                setupRecycler();

                nColumn = 1;

                TitleListItemDecoration decoration= new TitleListItemDecoration(spacing,getAdapter(),nColumn,false);
                mRecycler.addItemDecoration(decoration);
                final LinearLayoutManager lm = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
                lm.setAutoMeasureEnabled(false);



                mRecycler.setLayoutManager(lm);
                break;



            case GRID:

                getAdapter().setViewMode(Utils.ViewMode.GRID);


                setupRecycler();

                nColumn = 2;


                final GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), nColumn);
                gridLayoutManager.setAutoMeasureEnabled(false);


               final TitleGridItemDecoration titleItemDecoration1=new TitleGridItemDecoration(spacing,getAdapter(),nColumn,true);
                mRecycler.addItemDecoration( titleItemDecoration1);
                gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {



                        return dataset.isHeader(position) || dataset.getObject(position) instanceof ProgressItem ? gridLayoutManager.getSpanCount() : 1;
                    }
                });



                mRecycler.setLayoutManager(gridLayoutManager);

                break;





        }


        fragmentContainer.addView(mRecycler);



    }

    public void selectCategory(String category, CategoryFragment.ChooserType type) {

        setSelectedCategory(category);
        setChooserType(type);


        if (!category.equals(getResources().getString(R.string.importRecent_Cat))) {


            fillDataset(filterType(category,getChooserType()));


        } else {

            final List<Object> bufferModel= new ArrayList<>();

            bufferModel.addAll(getRecentItems());




            fillDataset(bufferModel);

        }

        mRecycler.scrollToPosition(0);


        refreshInfobar();

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void fillDataset(List<Object>models){

            dataset.removeAll();
            dataset.AutomaticTitleGeneratorSortType(models,collpseTitle);

    }


    public void refreshInfobar( ) {
        int count=0;



        ActionBar actionBar =((AppCompatActivity) getActivity()).getSupportActionBar();
        count= dataset.getItemCount();
        String aux =  myTag ;
        aux += " ";
        aux +=String.valueOf(count);

        actionBar.setTitle(getSelectedCategory());
        actionBar.setSubtitle(  "");


    }

    public void refreshInfobar(String title ) {
        int count=0;



        ActionBar actionBar =((AppCompatActivity) getActivity()).getSupportActionBar();


        actionBar.setTitle(title);
        actionBar.setSubtitle(  "");


    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void loadSearchBar(){
        startSupportActionModeIfNeeded();



    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

       menu.clear();

       this.menu=menu;


                inflater.inflate(R.menu.menu_recent, menu);



        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {

        if(query.length()>0){

            final List<Object> filteredModelList = filterByParameter(query);
            if(filteredModelList.size()>0){
                dataset.removeAll();
                dataset.AutomaticTitleGeneratorSortType(filteredModelList,false);
                mRecycler.scrollToPosition(0);
                return true;

            }else{
                dataset.removeAll();

                dataset.add( new CustomHeader("No hay coincidencias ",0,getResources().getDrawable(R.drawable.ic_info_white_24dp)));
                return true;
            }

        }else{
            dataset.removeAll();

            dataset.add( new CustomHeader("No hay coincidencias ",0,getResources().getDrawable(R.drawable.ic_info_white_24dp)));

        }
        return false;
    }

    protected abstract  List<Object> filterByParameter(String query);

    protected abstract List<Object> filterType(String query,ChooserType type);

    private final ActionMode.Callback mCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {



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
            onQueryTextChange("");
            finishSupportActionMode();
        }
    };

    public void startSupportActionModeIfNeeded() {
        if (choiceActionMode == null) {
            if (mCallback == null) {
                throw new IllegalStateException("No callback set");
            }

            customSearchView = new CustomSearchView(getContext());
            customSearchView.setOnQueryTextListener(this);
            customSearchView.setQueryHint("Buscar por Cliente,Directorio");

            choiceActionMode = ((AppCompatActivity)getActivity()).startSupportActionMode(mCallback);


        }
    }

    public void finishSupportActionMode(){


        if(choiceActionMode!=null){

             choiceActionMode.finish();
            choiceActionMode=null;
            selectCategory(getSelectedCategory(),chooserType);


        }


    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onPreview(Object obj) {
        Utils.showInfoPopUp((AppCompatActivity)getActivity(),obj);


    }

    @Override
    public void toRemove(Object obj) {

        showRemoveDialog((SparseBooleanArray)obj);

    }

    @Override
    public void toEdit(long[] id) {

       showEditDialog(id);

    }

    protected abstract void showRemoveDialog(SparseBooleanArray  sparseBooleanArray );

    protected abstract void showEditDialog(long[] ids);

    @Override
    public void onSaved(Object obj,int position, boolean isEditable) {



        if(!isEditable){
            if(isObjectVisible(obj))
            dataset.addNewItem(obj);



        }else{


            if(isObjectVisible(obj)) {

                dataset.editItem(obj,position);
            }else{

                dataset.remove(obj);
                dataset.deleteEmptyHeaders();



            }

        }


       getAdapter().multiChoiceHelper.clearChoices();

        refreshInfobar();

    }

    protected abstract void addNewItem();

    protected abstract boolean isObjectVisible(Object obj);

    protected abstract boolean isCategoryVisible(String category);

    @Override
    public void onStart() {

        if(!start) {

            if (getSelectedCategory() != null) {
                selectCategory(getSelectedCategory(), chooserType);

            } else {
                setSelectedCategory(getResources().getString(R.string.importRecent_Cat));
                selectCategory(getSelectedCategory(), chooserType);


            }
        }
        start=true;
        super.onStart();
    }








    /////////////////////////////////////////////////////////////////////






}