package com.colorbuzztechgmail.spf;

import android.content.Context;
import android.databinding.adapters.SearchViewBindingAdapter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class PhoneStorageFragment extends StorageFragment implements  ItemClickListener,SearchView.OnQueryTextListener{

    public static PhoneStorageFragment newInstance(String path,String type) {

        PhoneStorageFragment f = new PhoneStorageFragment();
        Bundle args = new Bundle();
        args.putString (PATH,path);
        args.putString(STORAGE_TYPE,type);
        f.setType(LocationType.valueOf(type));



        f.setArguments(args);


        return f;
    }



    private Deque<List<Object>> dirs = new ArrayDeque<List<Object>>();

    public int viewMode=0;
    public RecyclerView mRecycler;
    public FolderAdapter folderAdapter;
    com.colorbuzztechgmail.spf.popup.waitPoup waitPoup;

    private String  path=null;
    private boolean all=false;
    private GridSpacingItemDecoration itemDecoration;
    private  int nColumn=0;
    private  int spaceItem=8;
    private SearchFilesAsync searchFilesAsync;
    private SearchView mSearchView;
    private boolean start=false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        folderAdapter = new FolderAdapter(getContext(),null,viewMode);

    }


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)  {
        start=false;

        setHasOptionsMenu(true);
        final View view=inflater.inflate(R.layout.search_fragment,container,false);

        mSearchView=(SearchView)view.findViewById(R.id.searchView);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.onActionViewExpanded();

        mRecycler=view.findViewById(R.id.recyclerView);



        if(mSearchView.isFocused()) {
            mSearchView.clearFocus();
        }


        ImageView closeButton = (ImageView) mSearchView.findViewById(R.id.search_close_btn);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hideKeyboard();
                mSearchView.setQuery("",false);
                mSearchView.clearFocus();
            }
        });



        view.findViewById(R.id.pathBarContainer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                updatePathBar(false,null);
                if(dirs.size()>0)
                    setupAdapter(dirs.pop());
                     updateBar();

            }
        });





        setImportFile(false);

        if(getType().equals(LocationType.PHONE)) {



            initSearch();


        }
        customView(viewMode);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {



        switch (item.getItemId()){


            case R.id.action_search:



                return true;



        }



        return false;
    }



    public void setupAdapter(List<Object> header) {

        mRecycler.setAdapter(folderAdapter = new FolderAdapter(getContext(), header, viewMode));
        folderAdapter.setOnClickListener(this);


    }


    public void customView(int mode){

        //Utils.toast(getContext(),"N View:" + String.valueOf(nView));

        final int scrWidth=getContext().getResources().getDisplayMetrics().widthPixels;
        if(itemDecoration!=null) {
            mRecycler.removeItemDecoration(itemDecoration);

        }

        if(mRecycler.getAdapter() instanceof FolderAdapter){

            nColumn=scrWidth/((int)getContext().getResources().getDimension(R.dimen.searchItem_Width)+spaceItem);

        } else{
            //mRecycler.setAdapter(phoneFolderAdapter);
            nColumn=scrWidth/((int)getContext().getResources().getDimension(R.dimen.searchFolder_Width)+spaceItem);

        }
        if (mode==0) {  ///ListView






            nColumn = 1;

            final LinearLayoutManager lm = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
             lm.setAutoMeasureEnabled(false);
             mRecycler.setLayoutManager(lm);



        } else if (mode==1) {///GridView




            nColumn = 2;


            final GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), nColumn);


            itemDecoration = new GridSpacingItemDecoration(nColumn, 8, true);

            mRecycler.addItemDecoration(itemDecoration);
            mRecycler.setLayoutManager(gridLayoutManager);





        }


    }


    private List<File> getCheckedFiles(){

        final List<File> checkedFiles= new ArrayList<>();


        for(int i=0;i<folderAdapter.sparseState.size();i++){


            if(folderAdapter.sparseState.valueAt(i)){

                checkedFiles.add((File) folderAdapter.getItem(folderAdapter.sparseState.keyAt(i)));


            }


        }


        return checkedFiles;

    }


    @Override
    protected void PrepareDataToImport(String directory) {

        final List<FileItem> bufferModel=new ArrayList<>();


            for(File file:getCheckedFiles()){

                final FileItem fileItem=new FileItem();
                fileItem.setFile(file.getAbsolutePath());
                fileItem.setCategory(directory);
                bufferModel.add(fileItem);

            }
        new ImportModelAsyncTask().execute(bufferModel.toArray());

/*

            CachePot.getInstance().push(bufferModel);
            if(((MainActivity)getActivity()).mModelItemActivity!=null){
                ((MainActivity)getActivity()).mModelItemActivity.importSpfFiles();

                setImportFile(false);

            }

*/

    }

    @Override
    protected int getItemCount() {

        if(folderAdapter!=null){
            return  folderAdapter.getItemCount();

        }
        return 0;
    }

    @Override
    protected int getCheckedItemCount() {
        return folderAdapter.checkedItemCount;
    }

    @Override
    protected void initSearch() {

        path=getArguments().getString(PATH);


        if (folderAdapter.getItemCount() == 0) {
            switch (getType()) {

                case PHONE:
                    if (path != null) {





                        //  path =(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath());
//
                        // File f=new File(path);


                        Object[] obj = new Object[1];
                        obj[0] = path;


                        searchFilesAsync= new SearchFilesAsync();
                        searchFilesAsync.execute(obj);


                    }


                    break;


                case SDCARD:
                    if (path != null) {




                        //  path =(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath());
//
                        // File f=new File(path);


                        Object[] obj = new Object[1];
                        obj[0] = path;



                        searchFilesAsync= new SearchFilesAsync();
                        searchFilesAsync.execute(obj);


                    }

                    break;


            }

        }
    }


    public List<Object> dirSearch(File dir) {


        List<Object> HeaderList=new ArrayList<>();
        Deque<File> dirs = new ArrayDeque<File>();

        List<File> listFile=new ArrayList<>();
        listFile = Arrays.asList(dir.listFiles());

        CustomHeader rootHeader=new CustomHeader(dir.getName(),Utils.IdMaker.next(),null);

        if (listFile != null) {
            for (int i = 0; i < listFile.size(); i++) {


                if (listFile.get(i).isDirectory()) {


                    dirs.push(listFile.get(i));

                    for(int j=0;j<dirs.size();j++){

                        List<File> aux=new ArrayList<>();
                        final File auxDir=dirs.pop();

                        aux.addAll(Arrays.asList(auxDir.listFiles()));

                        if(aux.size()>0) {

                            CustomHeader header= new CustomHeader(auxDir.getName(),Utils.IdMaker.next(),null);

                            for (File file : aux) {

                                if (file.isDirectory()) {

                                    dirs.add(file);
                                    j--;

                                } else {

                                    if (file.getName().toLowerCase().endsWith(".spf")) {


                                        header.addObject(file);


                                    }
                                }

                            }

                            if(header.getBuffer().size()>0)
                                HeaderList.add(header);



                        }


                    }


                } else {
                    if (listFile.get(i).getName().toLowerCase().endsWith(".spf")){

                        rootHeader.addObject(listFile.get(i));


                    }
                }
            }

            if(rootHeader.getBuffer().size()>0)
                HeaderList.add(rootHeader);


        }

        return HeaderList;



    }

    public List<Object> dirSearch(File dir,String query) {


        List<Object> HeaderList=new ArrayList<>();
        Deque<File> dirs = new ArrayDeque<File>();

        List<File> listFile=new ArrayList<>();
        listFile = Arrays.asList(dir.listFiles());

        CustomHeader rootHeader=new CustomHeader(dir.getName(),Utils.IdMaker.next(),null);

        if (listFile != null) {
            for (int i = 0; i < listFile.size(); i++) {


                if (listFile.get(i).isDirectory()) {


                    dirs.push(listFile.get(i));

                    for(int j=0;j<dirs.size();j++){

                        List<File> files=new ArrayList<>();
                        final File auxDir=dirs.pop();

                        files.addAll(Arrays.asList(auxDir.listFiles()));

                        if(files.size()>0) {

                            CustomHeader header= new CustomHeader(auxDir.getName(),Utils.IdMaker.next(),null);

                            for (File file : files) {

                                if (file.isDirectory()) {

                                    dirs.add(file);
                                    j--;

                                } else {

                                    if (file.getName().toLowerCase().endsWith(".spf")) {

                                        if(file.getName().toLowerCase().contains(query.toLowerCase())){

                                            HeaderList.add(file);
                                        }



                                    }
                                }

                            }


                        }


                    }


                } else {
                    if (listFile.get(i).getName().toLowerCase().endsWith(".spf")){
                        if(listFile.get(i).getName().toLowerCase().contains(query.toLowerCase())){

                            HeaderList.add(listFile.get(i));
                        }


                    }
                }
            }



        }

        return HeaderList;



    }



    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(final String query) {


      if  (searchFilesAsync!=null){

            if(searchFilesAsync.getStatus().equals(AsyncTask.Status.RUNNING)){

                searchFilesAsync.cancel(true);
                folderAdapter.clear();
                if(waitPoup!=null){
                    waitPoup.dismiss();

                }
            }
        }
        if(query.trim().length()==0){

            if(folderAdapter!=null && start){
                folderAdapter.clear();
                initSearch();

            }
            return true;
        }


        new Handler().post(new Runnable() {
            @Override
            public void run() {

                Object[] obj = new Object[2];
                obj[0] = path;
                obj[1] = query;


                searchFilesAsync = new SearchFilesAsync();
                searchFilesAsync.execute(obj);
                setImportFile(false);
            }
        });


        return true;
    }

    public class SearchFilesAsync extends AsyncTask<Object, Float, List<Object>> {

        protected void onPreExecute() {

            List<Object> list=new ArrayList<>();

            list.add(new ProgressItem("",0));
            folderAdapter.clear();
            setupAdapter(list);


        }

        protected List<Object> doInBackground(Object[] path) {

            try {

                if(path.length>1){


                    File f = new File((String) path[0]);

                    if (f.exists()) {
                        return dirSearch(f,(String) path[1]);

                    }

                }else{

                    if (path[0] != null) {
                        File f = new File((String) path[0]);

                        if (f.exists()) {
                            return dirSearch(f);

                        }
                    }
                }




            } catch (Exception e) {

                e.printStackTrace();
                Log.e("Background", e.toString());


            }
            return null;
        }

        protected void onProgressUpdate(Float... valores) {
            int p = Math.round(valores[0]);
            // ma.mProgressBar.setProgress(p);
        }

        @Override
        protected void onPostExecute(List<Object> headers) {

            folderAdapter.clear();

            if (headers != null) {

                setupAdapter(headers);
            } else {

                Utils.toast(getContext(), "No se encontaron archivos");
            }


        }

    }

    @Override
    public void onClick(View v, int position, boolean isLongClick) {
        if(folderAdapter.isHeader(position)){

            dirs.push(folderAdapter.getFiles());
            CustomHeader header=(CustomHeader) folderAdapter.getItem(position);
            updatePathBar(true,header.getTitle());
            setupAdapter(header.getBuffer());


        }else{

            updateBar();

        }
    }


    private void updatePathBar(boolean visible,String path){
        if(visible){

            getView().findViewById(R.id.pathBarContainer).setVisibility(View.VISIBLE);

        }else{
            getView().findViewById(R.id.pathBarContainer).setVisibility(View.GONE);

        }


        ((TextView)getView().findViewById(R.id.pathTxt)).setText(path);




    }

    @Override
    protected void selectAllItems(boolean enable) {
        folderAdapter.checkedItemCount=0;



        for(int i=0;i<folderAdapter.sparseState.size();i++) {


            folderAdapter.sparseState.put(folderAdapter.sparseState.keyAt(i), enable);
        }
        if(enable){


                folderAdapter.checkedItemCount=folderAdapter.sparseState.size();

            }




        folderAdapter.notifyDataSetChanged();

        updateBar();

    }

    private void hideKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    @Override
    public void onStart() {

        start=true;
        super.onStart();
    }
}
