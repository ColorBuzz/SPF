package com.colorbuzztechgmail.spf;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayMap;
import android.databinding.ViewDataBinding;
import android.databinding.adapters.SearchViewBindingAdapter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.colorbuzztechgmail.spf.adapter.BaseAdapter;
import com.colorbuzztechgmail.spf.databinding.QuerySearchFragmentBinding;

import java.util.ArrayList;
import java.util.List;

import static com.colorbuzztechgmail.spf.ModelDataBase.CUSTOM_MATERIAL_COLUMN_NAME;
import static com.colorbuzztechgmail.spf.ModelDataBase.CUSTUMER_COLUMN_NAME;
import static com.colorbuzztechgmail.spf.ModelDataBase.CUTNOTE_COLUMN_REFERENCE;
import static com.colorbuzztechgmail.spf.ModelDataBase.DEALER_COLUMN_NAME;
import static com.colorbuzztechgmail.spf.ModelDataBase.DIRECTORY_COLUMN_NAME;
import static com.colorbuzztechgmail.spf.ModelDataBase.FTS_CUSTOM_MATERIAL;
import static com.colorbuzztechgmail.spf.ModelDataBase.FTS_CUTNOTE_LIST;
import static com.colorbuzztechgmail.spf.ModelDataBase.FTS_MODEL;
import static com.colorbuzztechgmail.spf.ModelDataBase.KEY_CUSTOM_MATERIAL_ID;
import static com.colorbuzztechgmail.spf.ModelDataBase.KEY_CUSTUMER_ID;
import static com.colorbuzztechgmail.spf.ModelDataBase.KEY_DEALER_ID;
import static com.colorbuzztechgmail.spf.ModelDataBase.KEY_DIRECTORY_ID;
import static com.colorbuzztechgmail.spf.ModelDataBase.KEY_ID;
import static com.colorbuzztechgmail.spf.ModelDataBase.KEY_MATERIAL_TYPE_ID;
import static com.colorbuzztechgmail.spf.ModelDataBase.KEY_MODEL_ID;
import static com.colorbuzztechgmail.spf.ModelDataBase.MATERIAL_TYPE_COLUMN_NAME;
import static com.colorbuzztechgmail.spf.ModelDataBase.MODEL_COLUMN_NAME;
import static com.colorbuzztechgmail.spf.ModelDataBase.TABLE_CUNOTE;
import static com.colorbuzztechgmail.spf.ModelDataBase.TABLE_CUNOTE_LIST;
import static com.colorbuzztechgmail.spf.ModelDataBase.TABLE_CUSTOM_MATERIAL;
import static com.colorbuzztechgmail.spf.ModelDataBase.TABLE_CUSTUMER;
import static com.colorbuzztechgmail.spf.ModelDataBase.TABLE_DEALERSHIP;
import static com.colorbuzztechgmail.spf.ModelDataBase.TABLE_DEALER_CUSTOM_MATERIAL_RELATIONS;
import static com.colorbuzztechgmail.spf.ModelDataBase.TABLE_DIRECTORY;
import static com.colorbuzztechgmail.spf.ModelDataBase.TABLE_MATERIAL_TYPES;
import static com.colorbuzztechgmail.spf.ModelDataBase.TABLE_MATERIAL_TYPES_CUSTOM_MATERIAL_RELATIONS;
import static com.colorbuzztechgmail.spf.ModelDataBase.TABLE_MODEL;
import static com.colorbuzztechgmail.spf.ModelDataBase.TABLE_MODEL_CUSTUMER_RELATIONS;
import static com.colorbuzztechgmail.spf.ModelDataBase.TABLE_MODEL_DIRECTORY_RELATIONS;


public class QuerySearchFragment extends Fragment implements ItemActionListener,SearchView.OnQueryTextListener {

    public static QuerySearchFragment newInstance(String fragmentType,String tag){

        Bundle args = new Bundle();



        QuerySearchFragment c = new QuerySearchFragment();
        args.putString(FRAGMENT_TYPE,fragmentType);
        args.getString(TAG,tag);
         c.setArguments(args);


        return c;


    }


    private RecentDataset dataset;
    private RecentListAdapter adapter;
    private ModelDataBase db;
    public Cursor mCursor;
    private int maxItems = 10;
    private int cursorPos=0;
    private boolean isMoreLoading = true;
    private MoveCursorAsyncTask moveCursorAsyncTask;
    private QuerySeachAsyncTask querySeachAsyncTask;
     private RecyclerView mRecycler;
    private String myTag="";
    public BaseFragment.FragmentType frType;
    public ObservableArrayMap<Integer,Object> mValuesMap;


    public static String TAG="tag";
    public static String FRAGMENT_TYPE="fragment_type";

    public static final int ITEMS_COUNT=1;


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

         ViewDataBinding mBinding=  DataBindingUtil.inflate(inflater,R.layout.query_search_fragment,container,false);



        final View view = mBinding.getRoot();

        setHasOptionsMenu(true);



        ((SearchView)view.findViewById(R.id.searchView)).setOnQueryTextListener(this);
        ((SearchView)view.findViewById(R.id.searchView)).onActionViewExpanded();
        if(!((SearchView)view.findViewById(R.id.searchView)).isFocused()) {
            ((SearchView)view.findViewById(R.id.searchView)).clearFocus();
        }
        mRecycler=view.findViewById(R.id.recyclerView);
        db=new ModelDataBase(getContext());

        setupRecycler();

        initializeValues();


        customView();


        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_home_selector);

        mBinding.setVariable(BR.fragment,this);
        mBinding.setVariable(BR.map,mValuesMap);

        return view;

    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments().getString(TAG)!=null)
            setMyTag(getArguments().getString(TAG));

        if(getArguments().getString(FRAGMENT_TYPE)!=null)
            setFrType(BaseFragment.FragmentType.valueOf(getArguments().getString(FRAGMENT_TYPE)));


        mValuesMap=new ObservableArrayMap<>();


        mValuesMap.put(ITEMS_COUNT,0);




    }


    public void setupRecycler() {


        mRecycler.setHasFixedSize(true);
        mRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);


                GridLayoutManager  llManager = (GridLayoutManager) recyclerView.getLayoutManager();
                if (dy > 0 && llManager.findLastCompletelyVisibleItemPosition() == (getAdapter().getItemCount()-1)) {


                    showLoading();

                }
            }
        });



    }

    public BaseAdapter getAdapter(){

        return adapter;
    }

    protected void initializeValues() {
        setMyTag(getString(R.string.action_search));

        db=new ModelDataBase(getContext());
        adapter=new RecentListAdapter(this,(AppCompatActivity) getActivity(),true);
        this.dataset=new RecentDataset(mRecycler,adapter);
        adapter.dataset=dataset;

        //loadSearchBar();
        refreshInfobar(getMyTag());

    }

    public void customView() {


        int nColumn=1;
        int spacing=Utils.pxTodp(8,getContext());


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


        HeaderItemDecoration headerItemDecoration=new HeaderItemDecoration(mRecycler,getAdapter());
        mRecycler.addItemDecoration(headerItemDecoration);
        mRecycler.setAdapter(getAdapter());


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        menu.clear();
    }

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
    public void onPreview(Object obj) {

    }

    @Override
    public void toRemove(Object obj) {

    }

    @Override
    public void toEdit(long[] position) {

    }

    @Override
    public void onClick(View v, int position, boolean isLongClick) {

    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {

        final String value=query;



        if(querySeachAsyncTask!=null){

            if(querySeachAsyncTask.getStatus().equals(AsyncTask.Status.RUNNING)){

                querySeachAsyncTask.cancel(true);
            }
        }
        if(query.trim().length()==0){


            return false;
        }



        new Handler().post(new Runnable() {
            @Override
            public void run() {

                querySeachAsyncTask=new QuerySeachAsyncTask(getContext(),value,getFrType(),mCursor){

                    @Override
                    protected void onPostExecute(Cursor cursor) {
                        cursorPos=0;
                        dataset.removeAll();
                        dismissQuerySearch();

                        newMoveCursorAsyncTask(cursor);

                    }

                    @Override
                    protected void onCancelled() {

                    }
                };


                querySeachAsyncTask.execute();

            }
        });



        return true;

    }

    public void showLoading() {
        if (isMoreLoading) {
            isMoreLoading = false;
            new Handler().post(new Runnable() {
                @Override
                public void run() {


                    if(mCursor!=null) {

                        newMoveCursorAsyncTask(mCursor);
                        dataset.add(new ProgressItem("+" + String.valueOf(mCursor.getCount() - cursorPos), 999));
                    }
                }
            });
        }
    }

    public void setMore(boolean isMore) {
        this.isMoreLoading = isMore;
    }

    public void dismissQuerySearch() {

    }

    public void dismissLoading() {
        if (dataset.size() > 0) {
            dataset.removeIndex(dataset.size() - 1);

        }
    }

    public void newMoveCursorAsyncTask(Cursor cursor){

        mCursor=cursor;

        if(moveCursorAsyncTask!=null){

            if(moveCursorAsyncTask.getStatus().equals(AsyncTask.Status.RUNNING)){

                moveCursorAsyncTask.cancel(false);
            }
        }



            moveCursorAsyncTask = new MoveCursorAsyncTask(cursor, cursorPos, maxItems, new MoveCursorAsyncTask.getObjectToLoad() {

                @Override
                public Object getObject(Cursor mCursor) {


                    switch (getFrType()) {


                        case MODEL:

                            return db.createPreviewModel(mCursor);


                        case MATERIAL:

                            return db.createCustomMaterial(mCursor, true);


                        case CUTNOTE:

                            return db.createCutNoteList(mCursor);


                        case RECENT:

                            return null;


                    }
                    return null;

                }


            }) {

                @Override
                protected void onPostExecute(List<Object> items) {
                    cursorPos=cursorPos+items.size();
                    dismissLoading();
                    setMore(true);
                    fillDataset(items);

                }

            };


            moveCursorAsyncTask.execute();

    }

    public void closeCursor(){

        if(mCursor!=null){
            mCursor.close();
            cursorPos=0;
        }
    }

    public BaseFragment.FragmentType getFrType() {
        return frType;
    }

    public void setFrType(BaseFragment.FragmentType frType) {
        this.frType = frType;
    }

    public String getMyTag() {
        return myTag;
    }

    public void setMyTag(String myTag) {
        this.myTag = myTag;
    }

    public void refreshInfobar(String title ) {
        int count=0;



        ActionBar actionBar =((AppCompatActivity) getActivity()).getSupportActionBar();
    /*    count= dataset.getItemCount();
        String aux =  myTag ;
        aux += " ";
        aux +=String.valueOf(count);*/

        actionBar.setTitle(title);


    }

    public static class QuerySeachAsyncTask extends AsyncTask<Object, Integer, Cursor>   {

         Context context;
        ModelDataBase db;
        String query;
        BaseFragment.FragmentType mFragmentType;
        Cursor mCursor;


        public QuerySeachAsyncTask(Context context, String query, BaseFragment.FragmentType fragmentType, Cursor mCursor) {
            this.context=context;
            db=new ModelDataBase(context);

            this.query=query;
            this.mFragmentType=fragmentType;
            this.mCursor=mCursor;

        }



        @Override
        protected Cursor doInBackground(Object [] params) {

            String COLUMN_ORDER;
            String selectQuery=null;

            switch (mFragmentType){


                case MODEL:


                    COLUMN_ORDER=MODEL_COLUMN_NAME;


                    selectQuery ="SELECT * FROM " + TABLE_MODEL +" WHERE " + KEY_ID +" IN " + "(SELECT docid FROM " + FTS_MODEL + " WHERE " + FTS_MODEL + " MATCH ?) ORDER BY " + COLUMN_ORDER+";";

              /*  selectQuery = "SELECT  * FROM " + TABLE_MODEL + " m, " + TABLE_CUSTUMER + " c, "
                        + TABLE_DIRECTORY + " d, " + TABLE_MODEL_DIRECTORY_RELATIONS + " tdm, "
                        + TABLE_MODEL_CUSTUMER_RELATIONS +" tcm"
                        + " WHERE d." + KEY_ID + " = " + "tdm." + KEY_DIRECTORY_ID
                        + " AND m." + KEY_ID + " = " + "tdm." + KEY_MODEL_ID
                        + " AND tcm." + KEY_CUSTUMER_ID + " = c." + KEY_ID
                        + " AND tcm." + KEY_MODEL_ID + " = m." + KEY_ID
                        + " OR d." + DIRECTORY_COLUMN_NAME +  " LIKE " +"'" + query + "'"
                        + " OR c." + CUSTUMER_COLUMN_NAME  + " LIKE " + "'" + query + "'"
                        + " OR m." + MODEL_COLUMN_NAME  +  " LIKE " + "'" + query + "';";*/




                    break;




                case MATERIAL:
                    COLUMN_ORDER=CUSTOM_MATERIAL_COLUMN_NAME;

                    selectQuery ="SELECT * FROM " + TABLE_CUSTOM_MATERIAL +" WHERE " + KEY_ID +" IN " + "(SELECT docid FROM " + FTS_CUSTOM_MATERIAL + " WHERE " + FTS_CUSTOM_MATERIAL + " MATCH ?) ORDER BY " + COLUMN_ORDER+";";

                    break;

                case CUTNOTE:
                    COLUMN_ORDER=KEY_ID;

                    selectQuery ="SELECT * FROM " + TABLE_CUNOTE_LIST +" WHERE " + KEY_ID +" IN " + "(SELECT docid FROM " + FTS_CUTNOTE_LIST + " WHERE " + FTS_CUTNOTE_LIST + " MATCH ?) ORDER BY " + COLUMN_ORDER+";";

                    break;

            }

            if(selectQuery!=null) {
                mCursor = db.getReadableDatabase().rawQuery(selectQuery
                        , new String[]{"''"+query+"*"});



            }

            return mCursor;
        }

        @Override
        protected void onPostExecute(Cursor cursor) {


        }

        @Override
        protected void onCancelled() {

            super.onCancelled();
        }
    }

    public static class MoveCursorAsyncTask extends AsyncTask<Object, Integer, List<Object>>   {


        public interface getObjectToLoad{

            public Object getObject(Cursor mCursor);

        }

        Cursor mCursor;
        int cursorPos=0;
        int maxItemLoad=0;
        getObjectToLoad getObjectListener;

        public MoveCursorAsyncTask(Cursor mCursor, int cursorPos, int maxItemLoad, @NonNull getObjectToLoad getObjectListener) {

            this.mCursor=mCursor;
            this.cursorPos=cursorPos;
            this.maxItemLoad=maxItemLoad;
            this.getObjectListener=getObjectListener;

        }



        @Override
        protected List<Object> doInBackground(Object [] params) {


            int start = cursorPos;
            int count=0;
            List<Object> list = new ArrayList<>();
            int millis= start==0 ? 0:1500;
            boolean notNull=false;

            if(mCursor!=null) {
                while ((count < maxItemLoad) && (mCursor.moveToNext())) {


                    if (mCursor.getPosition() < mCursor.getCount()) {
                        list.add(getObjectListener.getObject( mCursor));
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
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            /////////////////////////////////////////////////

            return list;


        }
        @Override
        protected void onPostExecute(List<Object> items) {


        }

        @Override
        protected void onCancelled() {

            super.onCancelled();
        }



    }



}
