package com.colorbuzztechgmail.spf;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v4.util.LongSparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.colorbuzztechgmail.spf.CategoryFragment.ChooserType;
import com.colorbuzztechgmail.spf.Utils.ViewMode;
import com.colorbuzztechgmail.spf.adapter.BaseAdapter;
import com.colorbuzztechgmail.spf.dataset.Dataset;

import java.util.ArrayList;
import java.util.List;

;import static com.colorbuzztechgmail.spf.ModelDataBase.MODEL_COLUMN_NAME;


/**
 * Created by PC01 on 21/06/2017.
 */
public abstract class BaseItemActivity extends AppCompatActivity implements SearchView.OnQueryTextListener,ItemActionListener,Utils.onSavedInterface,SwipeRefreshLayout.OnRefreshListener{

    public  enum activityType{

        MODEL,
        MATERIAL,
        CUTNOTELIST,
        CUTNOTE,
        PIECE

    }



    private String myTag;

    public static String TAG="tag";
    public static String FRAGMENT_TYPE="fragment_type";
    public static final String CHOOSER_TYPE="chooserType";
    public static final String CATEGORY="category";
    public static final String VIEW_MODE="view_mode";
    public String COLUMN_ORDER;
    public String GROUPED_VALUE;


    public RecyclerView mRecycler;
    private GridSpacingItemDecoration itemDecoration;
    private int nColumn = 0;
    public  ViewMode ViewMode = Utils.ViewMode.LIST;
    private activityType frType=null;
    private ChooserType chooserType;
    public Toolbar myToolbar;
    public boolean collapseTitle=false;
    private SwipeRefreshLayout swipeRefresh;

    private Dataset dataset;
    public Cursor mCursor;
    private int maxItems = 20;
    private int cursorPos=0;
    private boolean isMoreLoading = true;
    private AsyncTask mCursorAsyncTask;

    //Display



   private String selectedCategory=null;
   public int lastImportsCount =10;
    private FrameLayout fragmentContainer;
    private FloatingActionButton importFab;
    private ModelDataBase db;
    private CustomSearchView customSearchView;
    private ActionMode choiceActionMode;
    private Menu menu;


    public static void newInstaceItemActivity(@NonNull activityType activityType,@NonNull Context context, @NonNull String coincidenceValue, @NonNull ChooserType chooserType,  @Nullable Utils.ViewMode viewMode, @NonNull String tag){
        Intent h =new Intent();

        switch (activityType){

            case MATERIAL:
                h= new Intent(context,CustomMaterialItemActivity.class);
                break;

            case CUTNOTELIST:
                h= new Intent(context,CutNotesListItemActivity.class);
                break;


            case CUTNOTE:
                h= new Intent(context,CutNoteItemActivity.class);
                break;
            case MODEL:
                h= new Intent(context,ModelItemActivity.class);
                break;




        }

        h.putExtra(FRAGMENT_TYPE,activityType.name());
        h.putExtra(CATEGORY,coincidenceValue);
        h.putExtra(CHOOSER_TYPE,chooserType.name());
        h.putExtra(VIEW_MODE,viewMode.name());
        h.putExtra(TAG,tag);


        context.startActivity(h);

    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        if(getIntent().getStringExtra(VIEW_MODE)!=null)
            setViewMode( Utils.ViewMode.valueOf(getIntent().getStringExtra(VIEW_MODE)));

        if(getIntent().getStringExtra(TAG)!=null)
            setMyTag(getIntent().getStringExtra(TAG));

        if(getIntent().getStringExtra(FRAGMENT_TYPE)!=null)
         setFrType(activityType.valueOf(getIntent().getStringExtra(FRAGMENT_TYPE)));

        if(getIntent().getStringExtra(CHOOSER_TYPE)!=null)
        setChooserType(ChooserType.valueOf(getIntent().getStringExtra(CHOOSER_TYPE)));

        if(getIntent().getStringExtra(CATEGORY)!=null)
        setSelectedCategory(getIntent().getStringExtra(CATEGORY));

       setContentView(R.layout.base_item_activity);

        swipeRefresh = findViewById(R.id.swipeRefresh);
        swipeRefresh.setEnabled(false);
       // swipeRefresh.setOnRefreshListener(this);
        swipeRefresh.setOnChildScrollUpCallback(new SwipeRefreshLayout.OnChildScrollUpCallback() {
            @Override
            public boolean canChildScrollUp(SwipeRefreshLayout parent, @Nullable View child) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefresh.setRefreshing(false);


                    }
                }, 2000);

                return false;
            }
        });
        db=new ModelDataBase(this);
        maxItems=(int)getResources().getInteger(R.integer.maxItemLoad);


        setupRecycler();

        initializeValues();

        setupAdapter();

        setupToolBar();

        fragmentContainer=findViewById(R.id.frame);

        customSearchView=new CustomSearchView(this);
        customSearchView.setOnQueryTextListener(this);

        customView();

        refreshInfobar(getMyTag());
    }

    private void setupToolBar(){

        myToolbar = (Toolbar) findViewById(R.id.appbar);
       setSupportActionBar(myToolbar);
       myToolbar.setNavigationIcon(R.drawable.ic_arrow_back_selector_white);
       myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               onBackPressed();


           }
       });




    }


   public void setupRecycler(){

        mRecycler= new RecyclerView(this);
        mRecycler.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mRecycler.setHasFixedSize(true);

       mRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
           @Override
           public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
               super.onScrolled(recyclerView, dx, dy);
               LinearLayoutManager llManager = (LinearLayoutManager) recyclerView.getLayoutManager();
               if (dy > 0 && llManager.findLastCompletelyVisibleItemPosition() == (getAdapter().getItemCount() - 2)) {


                   showLoading();

               }
           }
       });
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

    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    public activityType getFrType() {
        return frType;
    }

    public void setFrType(activityType frType) {
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

        int spacing=Utils.pxTodp(8,this);


        //Utils.toast(this,"N View:" + String.valueOf(nView));
        //  mRecycler.removeItemDecoration(itemDecoration);

        switch (ViewMode){



            case LIST:


               getAdapter().setViewMode(Utils.ViewMode.LIST);



                setupRecycler();

                nColumn = 1;

                TitleListItemDecoration decoration= new TitleListItemDecoration(spacing,getAdapter(),nColumn,false);
                mRecycler.addItemDecoration(decoration);
                final LinearLayoutManager lm = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
                lm.setAutoMeasureEnabled(false);



                mRecycler.setLayoutManager(lm);
                break;



            case GRID:

                getAdapter().setViewMode(Utils.ViewMode.GRID);


                setupRecycler();

                nColumn = 2;


                final GridLayoutManager gridLayoutManager = new GridLayoutManager(this, nColumn);
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

   public void selectCategory(String category, ChooserType type) {

        setSelectedCategory(category);
        setChooserType(type);





            fillDataset(filterType(category,getChooserType()));




        mRecycler.scrollToPosition(0);





    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void fillDataset(List<Object>models){

            dataset.removeAll();
            dataset.AutomaticTitleGeneratorSortType(models,false);

    }

    public void closeCursor(){

        if(mCursor!=null){
            mCursor.close();
            cursorPos=0;
        }
    }


    public void refreshInfobar( ) {
        int count=0;



        ActionBar actionBar =getSupportActionBar();
        count= dataset.getItemCount();
        String aux =  myTag ;
//        aux += " ";
//        aux +=String.valueOf(count);

        actionBar.setTitle(aux);
        //actionBar.setSubtitle("");


    }

    public void refreshInfobar(String title ) {
        int count=0;



        ActionBar actionBar = getSupportActionBar();


        actionBar.setTitle(title);
        actionBar.setSubtitle(  "");


    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void loadSearchBar(){
        //startSupportActionModeIfNeeded();

        switch (frType){


            case MODEL:



                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame,QuerySearchFragment.newInstance(BaseFragment.FragmentType.MODEL.name(), null,null))
                        .setTransition(FragmentTransaction.TRANSIT_ENTER_MASK)
                        .addToBackStack(null)
                        .commit();

                break;


            case MATERIAL:



                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame,QuerySearchFragment.newInstance(BaseFragment.FragmentType.MATERIAL.name(), null,null))
                        .setTransition(FragmentTransaction.TRANSIT_ENTER_MASK)
                        .addToBackStack(null)
                        .commit();

                break;


            case CUTNOTELIST:



                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame,QuerySearchFragment.newInstance(BaseFragment.FragmentType.CUTNOTE.name(), null,null))
                        .setTransition(FragmentTransaction.TRANSIT_ENTER_MASK)
                        .addToBackStack(null)
                        .commit();

                break;

        }
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

            customSearchView = new CustomSearchView(this);
            customSearchView.setOnQueryTextListener(this);
            customSearchView.setQueryHint("Buscar por Cliente,Directorio");

            choiceActionMode = startSupportActionMode(mCallback);



        }
    }

    public void finishSupportActionMode(){

        if(choiceActionMode!=null){

             choiceActionMode.finish();
            choiceActionMode=null;
            if(mCursorAsyncTask.getStatus().equals(AsyncTask.Status.RUNNING)){

                mCursorAsyncTask.cancel(true);
            }
            selectCategory(getSelectedCategory(),chooserType);


        }


    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



    @Override
    public void onPreview(Object obj) {
        Utils.showInfoPopUp(this,obj);


    }

    @Override
    public void toRemove(Object obj) {

        showRemoveDialog((LongSparseArray)obj);

    }

    @Override
    public void toEdit(long[] id) {
        if(getAdapter().enableMultiChoice)
            getAdapter().multiChoiceHelper.clearChoices();
       showEditDialog(id);


    }

    protected abstract void showRemoveDialog(LongSparseArray<Boolean>  sparseBooleanArray );

    protected abstract void showEditDialog(long[] id);

    @Override
    public void onSaved(Object obj,int position, boolean isEditable) {



        if(!isEditable){
            if(isObjectVisible(obj))
            dataset.addNewItem(obj);



        }else{


            if(isObjectVisible(obj)) {

                dataset.editItem(obj,position);
            }else{

                dataset.removeIndex(position);
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

        if(getSelectedCategory()!=null){
            selectCategory(getSelectedCategory(),chooserType);

        }else{
            setSelectedCategory(getResources().getString(R.string.importRecent_Cat));
            selectCategory(getSelectedCategory(),chooserType);


        }
        super.onStart();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }


/////////////////////////////////////////////////////////////////////


    @Override
    public void onRefresh() {

             new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    swipeRefresh.setRefreshing(false);


                }
            }, 2000);

    }

    public void showLoading() {
        if (isMoreLoading) {
            isMoreLoading = false;
            new Handler().post(new Runnable() {
                @Override
                public void run() {



                    newLoadAsyncTask();
                    dataset.add(new ProgressItem("+" + String.valueOf(mCursor.getCount()-cursorPos),999));
                }
            });
        }
    }

    public void setMore(boolean isMore) {
        this.isMoreLoading = isMore;
    }

    public void dismissLoading() {
        if (dataset.size() > 0) {
            dataset.removeIndex(dataset.size() - 1);

        }
    }

    protected abstract  Object getObjectToLoad(Cursor mCursor);


    public void newLoadAsyncTask(){

        if(mCursorAsyncTask!=null){

            if(mCursorAsyncTask.getStatus().equals(AsyncTask.Status.RUNNING)){

                mCursorAsyncTask.cancel(true);
            }
        }

      mCursorAsyncTask=  new AsyncTask<Void, Void, List<Object>>() {
            @Override
            protected List<Object> doInBackground(Void... voids) {
                /**
                 *    Delete everything what is below // and place your code logic
                 */
                ///////////////////////////////////////////

                int start = cursorPos;
                int count=0;
                List<Object> list = new ArrayList<>();
                int millis= start==0 ? 0 : 1500;
                boolean notNull=false;

                if(mCursor!=null) {
                    while ((count < maxItems) && (mCursor.moveToNext())) {


                        if (mCursor.getPosition() < mCursor.getCount()) {
                            list.add(getObjectToLoad(mCursor));
                            cursorPos++;
                            count++;
                        }

                    }

                    if(cursorPos>0) {
                        if (cursorPos == mCursor.getCount()) {

                            mCursor.close();

                        }
                    }
                    try {
                        Thread.sleep(millis);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                /////////////////////////////////////////////////

                return list;


            }

            @Override
            protected void onPostExecute(List<Object> items) {
                super.onPostExecute(items);
                dismissLoading();
                setMore(true);

                fillDataset(items);
            }
        }.execute();
    }


}