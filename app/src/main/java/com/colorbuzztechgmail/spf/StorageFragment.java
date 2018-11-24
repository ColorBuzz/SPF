package com.colorbuzztechgmail.spf;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class StorageFragment extends Fragment {


    public static String STORAGE_TYPE="storageType";
    public static String PATH="path";
    public static String DEVICE_INDEX="deviceIndex";
    public ImportButtonListener importButtonListener;
     private TabSearchFragment.OnFinishedImportTask OnFinishedImportTask;

    public enum LocationType{

        PHONE,
        SDCARD,
        USB


    }

    private String myTag;
    private boolean importFile=false;
    boolean selectAll;
    ActionMode choiceActionMode;

    private LocationType type;

    private RecyclerView.Adapter<RecyclerView.ViewHolder> adapter;


    private final ActionMode.Callback checkCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {


            mode.getMenuInflater().inflate(R.menu.menu_import,menu);



            return true;

        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {






            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {



            switch (item.getItemId()){


                case R.id.action_selectAll:

                    selectAll=!selectAll;

                    selectAllItems(selectAll);

                    if (!selectAll){
                       /* item.setTitle(getString(R.string.action_selectAll));
                        item.setIcon(getResources().getDrawable(R.drawable.ic_select_all_white_24dp))*/;

                        onDestroyActionMode(choiceActionMode);
                    }else {

                        item.setIcon(getResources().getDrawable(R.drawable.ic_unselect_all_white_24dp));
                        item.setTitle(getString(R.string.action_cancelSelectAll));

                    }


                    return true;





            }


            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {

            finishCheckableActionMode();
        }
    };

    public void startCheckableActionModeIfNeeded() {
        if (choiceActionMode == null) {
            if (checkCallback == null) {
                throw new IllegalStateException("No callback set");
            }
            choiceActionMode = ((MainActivity)getActivity()).startSupportActionMode(checkCallback);




        }
    }

    private void finishCheckableActionMode(){


        if(choiceActionMode!=null){

            choiceActionMode.finish();
            choiceActionMode=null;

            selectAllItems(false);
            selectAll=false;

        }


    }


    public void setImportButtonListener(ImportButtonListener importButtonListener) {
        this.importButtonListener = importButtonListener;
    }

    public void setOnFinishedImportTask(TabSearchFragment.OnFinishedImportTask OnFinishedImportTask) {
        this.OnFinishedImportTask = OnFinishedImportTask;
    }

    public LocationType getType() {
        return type;
    }

    public void setType(LocationType type) {
        this.type = type;
    }

    public void setMyTag(String value) {
        if("".equals(value))
            return;
        myTag = value;
    }

    public String getMyTag() {
        return myTag;
    }


    public boolean isImportFile() {
        return importFile;
    }

    public void setImportFile(boolean importFile) {
        this.importFile = importFile;
    }

    //----------Imports------------------------------////


    public void showDirectoryChooser(){

        LayoutInflater li=LayoutInflater.from(getContext());

        View v=li.inflate(R.layout.save_piece_popup,null);

        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());


        builder.setView(v);


        ((LinearLayout)v.findViewById(R.id.container)).removeAllViewsInLayout();
        final Dialog d=builder.create();

        final Toolbar toolbar=new Toolbar(getContext());
        toolbar.setTitle(getResources().getString(R.string.action_select));

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_close_black_24dp));



        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d.dismiss();

            }
        });

        d.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                ((ListView)d.getWindow().findViewById((R.id.lisview))).setAdapter(createCategoryAdapter(d));

                d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
        });

        ((LinearLayout)v.findViewById(R.id.container)).addView(toolbar);


        d.show();
    }

    private  ListPopupWindowAdapter createCategoryAdapter(final Dialog d){

        final ModelDataBase db=new ModelDataBase(getContext());


        final List<String> categories=db.getCategoryString();


        final ArrayList<ListPopUpMenuItem> items =new ArrayList<>();
        items.add(new ListPopUpMenuItem(getString(R.string.action_newDirectory),R.drawable.ic_add_plus_accent_24dp));



        for(String category:categories){


            if(!(category.equals(getString(R.string.importRecent_Cat)))){

                items.add(new ListPopUpMenuItem(category,R.drawable.ic_folder_black));

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

                        String directory=((TextView)v.findViewById(R.id.text)).getText().toString();
                        // Utils.toast(getContext(),category);

                        if(!directory.equals(getString(R.string.action_newDirectory))){

                            PrepareDataToImport(directory);
                            importFile=false;
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

        final View promptsView = li.inflate(R.layout.add_category_popup, null);

        final TextView titleText=(TextView) promptsView.findViewById(R.id.titleText);




        ((LinearLayout)promptsView.findViewById(R.id.di)).removeAllViewsInLayout();

        final EditText nametxt=new EditText(getContext());
        nametxt.setHint(getContext().getResources().getString(R.string.importNoAsigned_Cat));
        nametxt.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        ((LinearLayout)promptsView.findViewById(R.id.di)).addView(nametxt);



        titleText.setText(getContext().getResources().getString(R.string.action_newDirectory));

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getContext());
        alertDialogBuilder.setView(promptsView);


        final Dialog alertDialog = alertDialogBuilder.create();

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();




        promptsView.findViewById(R.id.closeFrame).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();

            }
        });

        promptsView.findViewById(R.id.saveFrame).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String category =nametxt.getText().toString();
                category=category.trim();
                if(!category.equals("")){

                    if(!db.existDirectory(category)){

                        db.insertCategory(category);

                        ((ListPopupWindowAdapter)  ((ListView)d.getWindow().findViewById((R.id.lisview))).getAdapter())
                                .add(new ListPopUpMenuItem(category,R.drawable.ic_folder_black));

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

    public void updateBar() {


        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();




        if(myTag!=null){

            actionBar.setTitle(myTag);

        }

      if (getCheckedItemCount()==0){

          finishCheckableActionMode();

          if(importButtonListener !=null){

                    importButtonListener.showImportButton(false);
                }

            setImportFile(false);




        }else{
            startCheckableActionModeIfNeeded();
            String string =String.valueOf(getCheckedItemCount());
            string+=" ";
            string+=(getString(R.string.checkedFileText));
            choiceActionMode.setTitle(string);

                            if(importButtonListener !=null){

                                importButtonListener.showImportButton(true);
                            }

                            setImportFile(true);

        }

    }

    public class CopyTask extends AsyncTask<Object, Integer, String> {

        private ProgressDialog dialog;
        List<Object> files;

        public CopyTask( ) {
            dialog = new ProgressDialog(getContext());
            dialog.setTitle(getResources().getString(R.string.importText));
            dialog.setIndeterminate(false);
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setCancelable(false);
         }

        @Override
        protected void onPreExecute() {
            finishCheckableActionMode();

            dialog.show();

        }

        @Override
        protected String doInBackground(Object [] params) {
            long time = System.currentTimeMillis();
            List<Object> bufferList = new ArrayList<>();
            String text = "";

            files = new ArrayList<>();
            final ModelDataBase db= new ModelDataBase(getContext());

            files.addAll(Arrays.asList(params));
            dialog.setMax(files.size());


            for (int i = 0; i < files.size(); i++) {
                publishProgress(i+1 );
                try {
                    FileInputStream fis = new FileInputStream(((FileItem) files.get(i)).getPath());
                    int size = fis.available();
                    byte[] buffer = new byte[size];
                    fis.read(buffer);
                    fis.close();
                    text = new String(buffer,"ISO-8859-1");

                    final SPFdecoder decoder = new SPFdecoder(text,getContext());
                    decoder.getSpfModel().setParent(((FileItem) files.get(i)).getParent());
                    decoder.getSpfModel().setDate(Utils.getDate());


                    decoder.getSpfModel().setDirectory(((FileItem) files.get(i)).getCategory());

                    db.addNewModel(decoder.getSpfModel());



                   // bufferList.add(db.getPreviewModelById(db.addNewModel(decoder.getSpfModel())));

                    //saveModelToFile(decoder.getSpfModel());

                    // bufferList.add(decoder.getSpfModel());

                    Log.e("Importacion", ((FileItem) files.get(i)).getPath());

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("Fallo importacion", "Error:" +
                            e.toString() + "Modelo: " +((FileItem) files.get(i)).getName() );


                }
            }

            return ((FileItem)files.get(0)).getCategory();
        }

        @Override
        protected void onPostExecute(String directory) {
            dialog.dismiss();

            showSnackbar(String.valueOf(files.size()) + " " + "Modelos" + " importados",directory);


        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            dialog.setTitle("Importando " + '\n'+ ((FileItem)files.get(values[0]-1)).getName());
            /*int max = (int) param.from.getLength();
            if(param.from.getLength() > Integer.MAX_VALUE) {
                max = (int) (param.from.getLength() / 1024);
            }*/
            dialog.setProgress(values[0]);
        }



    }

    public void showSnackbar(String mssg,final String directory) {

        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) (((MainActivity)getActivity()).findViewById(R.id.parentLayout));
        Snackbar.make(coordinatorLayout, mssg, Snackbar.LENGTH_LONG).setAction("VER", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(OnFinishedImportTask !=null){


                    OnFinishedImportTask.finishedImportTask(directory);

                }else{
                    ((MainActivity)getActivity()).setItemNavigationView(1);


                }

            }
        }).show();

    }

    protected abstract void PrepareDataToImport(String directory);

    protected abstract int getItemCount();

    protected abstract int getCheckedItemCount();

    protected abstract void initSearch();

    public interface ImportButtonListener {


        void showImportButton(boolean state);


    }


    protected abstract void selectAllItems(boolean enable);

}
