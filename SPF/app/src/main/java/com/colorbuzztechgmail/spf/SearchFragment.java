package com.colorbuzztechgmail.spf;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.github.kimkevin.cachepot.CachePot;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by PC01 on 21/06/2017.
 */
public class SearchFragment extends Fragment implements View.OnClickListener ,SearchView.OnQueryTextListener {
    private static final Comparator<FileItem> ALPHABETICAL_COMPARATOR = new Comparator<FileItem>() {
        @Override
        public int compare(FileItem a,FileItem b) {
            return a.getName().toLowerCase().compareTo(b.getName().toLowerCase());
        }
    };
    private static final Comparator<Category> ALPHABETICAL_FOLDER_COMPARATOR = new Comparator<Category>() {
        @Override
        public int compare(Category a,Category b) {
            return a.getName().toLowerCase().compareTo(b.getName().toLowerCase());
        }
    };
    public List<FileItem> filesList;
    public List<FileItem> bufferList;
    public List<Category> folderlist;
    public HeaderAdapter headerAdapter;
    public FolderAdapter folderAdapter;
    //Display
    public RecyclerView mRecycler;
    public boolean OpenFolder=false;
    public int ScreenContainerWidth;
    public int ViewMode = 0;
    MenuItem viewItem;
    MenuItem searchItem;
    MenuItem selectAllItem;
    boolean importFile=false;
    ItemClickListener itemClickListener;
    waitPoup waitPoup;
    ActionMode choiceActionMode;
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
            onQueryTextChange("");
            choiceActionMode = null;
        }
    };
    private boolean all=false;
    private MainActivity ma;
    //private String filePath= "sdcard/Downloads";
    private String filePath= "storage/sdcard0/";
    private String path;
    private int SelectCount;
    private GridSpacingItemDecoration itemDecoration;
    private  int nColumn=0;
    private  int spaceItem=8;
    private  boolean edges=true;
    private FloatingActionButton backBtn;

    public SearchFragment( ) {
        super();


    }

    private static List<FileItem> filter(List<FileItem> filelist, String query) {
        final String lowerCaseQuery = query.toLowerCase();

        final List<FileItem> filteredModelList = new ArrayList<>();
        for (FileItem file : filelist) {
            final String text = file.getName().toLowerCase();
            if (text.contains(lowerCaseQuery)) {
                filteredModelList.add(file);
            }
        }
        return filteredModelList;
    }

    private static List<Category> folderFilter(List<Category> filelist, String query) {
        final String lowerCaseQuery = query.toLowerCase();

        final List< Category> filteredCategoryList = new ArrayList<>();
        for (Category file : filelist) {
            final String text = file.getName().toLowerCase();
            if (text.contains(lowerCaseQuery)) {
                filteredCategoryList.add(file);
            }
        }
        return filteredCategoryList;
    }

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view=inflater.inflate(R.layout.search_fragment,container,false);
        setHasOptionsMenu(true);
       setScreenContainerWidth(container.getWidth());




        Log.e("ScreenContainerWidth",String.valueOf(ScreenContainerWidth));

        mRecycler=(RecyclerView)view.findViewById(R.id.recyclerView);
        backBtn=(FloatingActionButton) getActivity().findViewById(R.id.fabbutton);
        backBtn.setOnClickListener(this);
        backBtn.setVisibility(View.GONE);
        LoadInit(view);
        return view;

    }

    public void LoadInit(View v) {

        customSearchView = new CustomSearchView(getContext());

        OpenFolder=false;
       resetToolbar();


        path =(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath());
//
       // File f=new File(path);

        folderlist = new ArrayList<>();
        filesList = new ArrayList<>();
        bufferList = new ArrayList<>();

                Object[] obj = new Object[1];
                obj[0] = path;


                new SearchFilesAsync().execute(obj);




    }

    public void loadFiles(){

        if (filesList.size() > 0) {
            folderAdapter = new FolderAdapter(getContext().getApplicationContext(), ALPHABETICAL_FOLDER_COMPARATOR, this);
            folderAdapter.add(folderlist);
            mRecycler.setAdapter(folderAdapter);


        } else {
            Utils.toast(getContext(), "NO SE ENCONTRARON ARCHIVOS");
           ;

        }
    }

    public void resetToolbar(){

        ActionBar actionBar = ma.getSupportActionBar();
        actionBar.setTitle(R.string.importText);
        actionBar.setSubtitle("");

    }

    public void setActivity(MainActivity ma) {
        this.ma = ma;
    }

    public void OpenFolder(String folderName){


        finishSupportActionMode();

        bufferList=new ArrayList<>();

        for(int i=0;i<filesList.size();i++){
            if(filesList.get(i).getParent().contains(folderName)){

                FileItem aux=new FileItem();

                aux.setFile(filesList.get(i).getPath());
                bufferList.add(aux);


            }
        }
        //Utils.toast(getContext(),String.valueOf(bufferList.size()));

        headerAdapter=new HeaderAdapter(getContext(),ALPHABETICAL_COMPARATOR,this);
        headerAdapter.add(bufferList);
        mRecycler.setAdapter(headerAdapter);


        backBtn.setVisibility(View.VISIBLE);
        selectAllItem.setVisible(true);

        OpenFolder=true;
        customView(ViewMode,OpenFolder,ScreenContainerWidth);
        updateBar(0,folderName);

    }

    public List<FileItem> BufferFiles(){

      ArrayList <FileItem> checkedFile=new ArrayList();

        try {
            for (int i = 0; i < headerAdapter.mSortedList.size(); i++) {

                if (headerAdapter.mSortedList.get(i).isCheck()) {
                     FileItem item=new FileItem();
                    item.setFile(headerAdapter.mSortedList.get(i).getPath());
                    checkedFile.add(item);

                }

            }
            all = false;

        }catch (Exception e){

            e.printStackTrace();
            Utils.toast(getContext(),e.toString());

        }
        return checkedFile;
    }

    public void dirSearch(File dir) {



        File[] listFile;
        listFile = dir.listFiles();

       // Utils.toast(getContext(),dir.getAbsolutePath());
        if (listFile != null) {
            for (int i = 0; i < listFile.length; i++) {
                if (listFile[i].isDirectory()) {
                    dirSearch(listFile[i]);


                } else {
                    if (listFile[i].getName().toLowerCase().endsWith(".spf")){
                        FileItem auxF=new FileItem();
                        auxF.setFile(listFile[i].getAbsolutePath());
                        filesList.add(auxF);

                        boolean duply=false;
                       if(!folderlist.isEmpty()){
                           for(Category folder:folderlist){

                               if(folder.getName().equals(auxF.getParent())){

                                   folder.setId(folder.getId()+1);
                                   duply=true;
                                   break;
                               }
                           }
                       }
                       if(!duply){

                            Category newCategory=new Category(auxF.getParent(),1);

                            folderlist.add(newCategory);

                       }
                    }
                }
            }
        }


    }

    public int getScreenContainerWidth() {
        return ScreenContainerWidth;
    }

    public void setScreenContainerWidth(int screenContainerWidth) {
        ScreenContainerWidth = screenContainerWidth;
    }

    public void customView(int mode,boolean openFolder,int ScreenWidth){

        //Utils.toast(getContext(),"N View:" + String.valueOf(nView));
        mRecycler.removeItemDecoration(itemDecoration);

        if(openFolder){

          nColumn=ScreenWidth/((int)getContext().getResources().getDimension(R.dimen.searchItem_Width)+spaceItem);

          } else{
            mRecycler.setAdapter(folderAdapter);
            nColumn=ScreenWidth/((int)getContext().getResources().getDimension(R.dimen.searchFolder_Width)+spaceItem);

        }
        if (mode==0) {




           final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);


            mRecycler.setLayoutManager(linearLayoutManager);



            viewItem.setIcon(R.drawable.ic_grid_white_24dp);

        }
        else if (mode==1) {





            Log.e("Column",String.valueOf(nColumn));

           final GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), nColumn, LinearLayoutManager.VERTICAL, false);

            itemDecoration=new GridSpacingItemDecoration(nColumn,spaceItem,true);
            mRecycler.setLayoutManager(gridLayoutManager);
            mRecycler.addItemDecoration(itemDecoration);

            viewItem.setIcon(R.drawable.ic_list_white_24dp);
        }



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //noinspection SimplifiableIfStatement
        switch (item.getItemId()){



            case R.id.action_selectAll:

                if(!all){



                    selectAll(true);

                }else{


                    selectAll(false);


                }
                return true;
            case R.id.action_viewBar:

                if (ViewMode<1){

                    ViewMode=ViewMode+1;

                }else if( ViewMode==1){
                    ViewMode=0;

                }
                customView(ViewMode,OpenFolder,ScreenContainerWidth);
                return true;

            case R.id.action_searchBar:

            startSupportActionModeIfNeeded();

                return true;


        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

           inflater.inflate(R.menu.menu_import,menu);

        searchItem=menu.findItem(R.id.action_searchBar);


        selectAllItem=menu.findItem(R.id.action_selectAll);


        viewItem=menu.findItem(R.id.action_viewBar);

        customSearchView.setOnQueryTextListener(this);
        customSearchView.setQueryHint("Busqueda...");


        customView(ViewMode,OpenFolder,ScreenContainerWidth);

        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);



// You can hide the state of the menu item here if you call getActivity().supportInvalidateOptionsMenu(); somewhere in your code

    }

    public void setAnimation(View v){


      final AnimationDrawable animationDrawable;
         final LinearLayout linearLayout;
         linearLayout = (LinearLayout) v.findViewById(R.id.animlayout);

        // initializing animation drawable by getting background from constraint layout
        animationDrawable = (AnimationDrawable) linearLayout.getBackground();

        Utils.toast(getContext(),String.valueOf(animationDrawable.isRunning()));
        // setting enter fade animation duration to 5 seconds
        animationDrawable.setEnterFadeDuration(300);

        // setting exit fade animation duration to 2 seconds
        animationDrawable.setExitFadeDuration(400);

        animationDrawable.setOneShot(true);
        if (animationDrawable != null && !animationDrawable.isRunning()) {
            // start the animation
            animationDrawable.start();
        }else if(animationDrawable != null && animationDrawable.isRunning()){
             linearLayout.clearAnimation();
        linearLayout.setBackground(getResources().getDrawable(R.drawable.drawable_gradient_animation_list));


        }

    }

    public void updateBar(int Count,String title){

         AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = ma.getSupportActionBar();



        if (Count==0){
            if(title!=null){

                actionBar.setTitle(title);

            }

            actionBar.setSubtitle("");
            all=false;
           importFile=false;
            backBtn.setImageResource( R.drawable.ic_arrow_white_24dp);
            selectAllItem.setTitle(getString(R.string.action_selectAll));



        }else{
            String string =String.valueOf(Count);
            string+=" ";
            string+=(getString(R.string.checkedFileText));
            actionBar.setSubtitle(string);
            importFile=true;
            all=true;
            backBtn.setImageResource(R.drawable.ic_open_in_browser_white_24dp);
            selectAllItem.setTitle(getString(R.string.action_cancelSelectAll));


        }


    }

    private void selectAll(boolean isCheck){

        for(int i=0;i<headerAdapter.getItemCount();i++){


            headerAdapter.mSortedList.get(i).setCheck(isCheck);

        }

        if(isCheck){

            headerAdapter.checkedViewCount=headerAdapter.getItemCount();
            updateBar(headerAdapter.getItemCount(),null);

        }else{

            headerAdapter.checkedViewCount=0;

            updateBar(0,null);


        }

        headerAdapter.notifyDataSetChanged();


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

    private void finishSupportActionMode(){


        if(choiceActionMode!=null){

            onQueryTextChange("");
            choiceActionMode.finish();


        }


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.fabbutton:

                finishSupportActionMode();

                if(!importFile){



                    headerAdapter.remove(bufferList);

                    backBtn.setImageResource( R.drawable.ic_arrow_white_24dp);


                    selectAllItem.setVisible(false);

                    v.setVisibility(View.GONE);

                    OpenFolder=false;
                    all=false;
                    resetToolbar();
                    selectAllItem.setTitle(getString(R.string.action_selectAll));
                    customView(ViewMode,OpenFolder,ScreenContainerWidth);
                }else{

                    /*ma.swipeLayout.setRefreshing(true);*/
                   showCategoryChooser();


                }

                break;

        }
    }

    public void showCategoryChooser(){

        LayoutInflater li=LayoutInflater.from(getContext());

        View v=li.inflate(R.layout.save_piece_popup,null);

        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        ((Toolbar)v.findViewById(R.id.pieceToolbar)).setTitle(getContext().getResources().getString(R.string.action_moveTo));
        ((Toolbar)v.findViewById(R.id.pieceToolbar)).setNavigationIcon(getResources().getDrawable(R.drawable.ic_close_black_24dp));

        builder.setView(v);


        final Dialog d=builder.create();

        ((Toolbar)v.findViewById(R.id.pieceToolbar)).setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d.dismiss();
                selectAll(false);

            }
        });

        d.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                ((ListView)d.getWindow().findViewById((R.id.lisview))).setAdapter(createCategoryAdapter(d));

                d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
        });
        d.show();
    }

    private  ListPopupWindowAdapter createCategoryAdapter(final Dialog d){

        final ModelDataBase db=new ModelDataBase(getContext());


        final List<String>categories=db.getCategoryString();


        final ArrayList<ListPopUpMenuItem> items =new ArrayList<>();
        items.add(new ListPopUpMenuItem(getString(R.string.action_newCategory),R.drawable.ic_add_plus_accent_24dp));



        for(String category:categories){


            if(!(category.equals(getString(R.string.importRecent_Cat)))){

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
                int dp8=(int) (8 *getContext().getResources().getDisplayMetrics().density + 0.5f);
                //Add margin between image and text (support various screen densities)
                int dp5 = (int) (5 *getContext().getResources().getDisplayMetrics().density + 0.5f);
                tv.setCompoundDrawablePadding(dp5);
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String category=((TextView)v.findViewById(R.id.text)).getText().toString();
                       // Utils.toast(getContext(),category);

                        final List<FileItem> bufferModel=new ArrayList<>();
                        bufferModel.addAll(BufferFiles());

                        if(!category.equals(getString(R.string.action_newCategory))){


                            for(FileItem item:bufferModel){

                                item.setCategory(category);


                            }


                            CachePot.getInstance().push(bufferModel);

                            ma.mModelFragment.importSpfFiles();

                            importFile=false;
                            selectAll(false);
                            d.dismiss();
                        }else{


                            showAddNewCategory(d);


                        }









                    }
                });
                return v;
            }
        };
return adapter;
    }

   private void showAddNewCategory(final Dialog d){


      final ModelDataBase db=new ModelDataBase(getContext());

       final List<String>categories=db.getCategoryString();

       LayoutInflater li=LayoutInflater.from(getContext());

       final View promptsView = li.inflate(R.layout.editcategory_popup, null);
       final Button cancelBtn=(Button)promptsView.findViewById(R.id.cancel_btn);
       final Button saveBtn=(Button)promptsView.findViewById(R.id.save_btn);
       final TextView titleText=(TextView) promptsView.findViewById(R.id.titleEditTextView);



       final ImageView tilteImg=(ImageView)promptsView.findViewById(R.id.addImageView);

       ((LinearLayout)promptsView.findViewById(R.id.di)).removeAllViewsInLayout();

       final EditText nametxt=new EditText(getContext());
       nametxt.setHint(getContext().getResources().getString(R.string.importNoAsigned_Cat));
       nametxt.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
       ((LinearLayout)promptsView.findViewById(R.id.di)).addView(nametxt);



       titleText.setText(getContext().getResources().getString(R.string.action_newCategory));
       tilteImg.setImageDrawable(getContext().getDrawable(R.drawable.ic_mode_edit_black_24dp));

       AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
               getContext());
       alertDialogBuilder.setView(promptsView);


       final Dialog alertDialog = alertDialogBuilder.create();

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

               String category =nametxt.getText().toString();
               category=category.trim();
            if(!category.equals("")){

                if(!db.existCategory(category)){

                        db.insertCategory(category);

                    ((ListPopupWindowAdapter)  ((ListView)d.getWindow().findViewById((R.id.lisview))).getAdapter())
                    .add(new ListPopUpMenuItem(category,R.drawable.ic_folder_black_24dp));

                    alertDialog.dismiss();


                }else{


                    Utils.toast(getContext(),getResources().getString(R.string.action_DuplicateCategory));


                }

            }else {

                Utils.toast(getContext(), getResources().getString(R.string.warning_emptyName));
            }


           }
       });

   }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {

        if(mRecycler.getAdapter() instanceof HeaderAdapter) {
            if (bufferList.size() > 0) {

                final List<FileItem> filteredModelList = filter(bufferList, query);
                headerAdapter.replaceAll(filteredModelList);
                mRecycler.scrollToPosition(0);
            }
        }else{

            if(mRecycler.getAdapter() instanceof FolderAdapter) {
                if (folderlist.size() > 0) {

                    final List<Category> folders = folderFilter(folderlist, query);
                    folderAdapter.replaceAll(folders);
                    mRecycler.scrollToPosition(0);
                }
            }

        }
        return false;
    }

    public class SearchFilesAsync extends AsyncTask<Object, Float, Boolean> {

        protected void onPreExecute() {

            waitPoup = com.colorbuzztechgmail.spf.waitPoup.newInstance(getString(R.string.dialog_searchFiles));


            waitPoup.show(getFragmentManager(), "waitPopup");
        }

        protected Boolean doInBackground(Object[] path) {





                try {

                    File f=new File((String)path[0]);



                        if (f.exists()) {



                            dirSearch(f);



                        }else {
                            Utils.toast(getContext(), "NO EXISTE LA RUTA");
                            return false;
                        }


                    if(filesList.isEmpty()){

                        return  false;
                    }else {
                        return true;

                    }

                } catch (Exception e) {

                    e.printStackTrace();
                    Log.e("Background", e.toString());
                    Utils.toast(getContext(), "Fallo Busqueda de archivos");


                }
            return false;
        }

        protected void onProgressUpdate(Float... valores) {
            int p = Math.round(valores[0]);
           // ma.mProgressBar.setProgress(p);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {

            waitPoup.dismiss();
            if(aBoolean){

                loadFiles();
            }else {

                Utils.toast(getContext(),"No se encontaron archivos");
            }

            super.onPostExecute(aBoolean);
        }
    }


}
