package com.colorbuzztechgmail.spf;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.channels.FileChannel;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener,
        ChangeCategoryPopUp.OnDialogFragmentClickListener{


MenuItem searchItem;

    public CategorySpinner mSpinner;

    LinearLayout frame;

    public NavigationView nNavigationView;
    private DrawerLayout nDrawerLayout;
    private ActionBarDrawerToggle nToggle;
    public Toolbar myToolbar;

    public SearchFragment mSearchFragment;
    public ModelFragment mModelFragment;
    public CustomMaterialFragment mMaterialFragment;
    public DealershipFragment mDealerFragment;

    ModelDataBase userData;
    public static int REQUEST_CODE_MODEL_IMAGE_GALLERY =999;
    public static int REQUEST_CODE_MATERIAL_IMAGE_GALLERY =888;
    public static int REQUEST_CODE_CONTACT_PHONE =777;


    public ProgressBar mProgressBar;


    public String dataFolder;
    private Object pickerObject;
    private View materialDialogImage;
    private Dialog addcontactdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_activity);
        LoadView();




    }

    @Nullable
    @Override
    public ActionBar getSupportActionBar() {
        return super.getSupportActionBar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);





//
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Utils.toast(this, "back");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);

    }


    private void LoadView() {

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},000);

        userData= new ModelDataBase(this);

        createFolderData();

        frame = (LinearLayout) findViewById(R.id.frame);
        mSpinner=(CategorySpinner)findViewById(R.id.spinnerCategory);
        mSpinner=new CategorySpinner(this);
        mSpinner.setBackground(getDrawable(R.drawable.spinner_bakcground1));


        mSearchFragment = new SearchFragment();
        mSearchFragment.setActivity(this);
        mModelFragment = new ModelFragment();
        mModelFragment.setActivity(this);
        mMaterialFragment= new CustomMaterialFragment();
        mMaterialFragment.setActivity(this);
        /*mModelFragment=
                ModelFragment.newInstance(gson.toJson(piece));*/

        // mModelFragment.modelAdapter.add(userData.loadData());






        File dbpath = this.getDatabasePath(ModelDataBase.DATABASE_NAME);
        if (dbpath.exists()){



           // Utils.toast(this,"DATABASE_EXIST" + this.getDatabasePath(ModelDataBase.DATABASE_NAME).toString());
        }else{

            Utils.toast(this,"DATABASE__NOT_EXIST");
        }





        nNavigationView = (NavigationView) findViewById(R.id.navigationMenu);
        nNavigationView.setNavigationItemSelectedListener(this);

        myToolbar = (Toolbar) findViewById(R.id.appbar);
        setSupportActionBar(myToolbar);

        myToolbar.setTitleTextColor(getResources().getColor(R.color.iconsColor));
        myToolbar.setSubtitleTextColor(getResources().getColor(R.color.iconsColor));

        mProgressBar=(ProgressBar)findViewById(R.id.progressBar);



        nDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        // nDrawerLayout.setScrimColor(Color.parseColor("#22000000"));
        // nDrawerLayout.setScrimColor(Color.parseColor("#99000000"));
        nDrawerLayout.setScrimColor(Color.TRANSPARENT);
        setToggle();


    setFirstItemNavigationView();
    }

    public void setToggle(){

        nToggle = new ActionBarDrawerToggle(this, nDrawerLayout, myToolbar, R.string.nav_open, R.string.nav_closed);
        nDrawerLayout.addDrawerListener(nToggle);
        nToggle.syncState();
    }



    private void createFolderData(){


      dataFolder= this.getExternalFilesDir(null).getAbsolutePath()+"/SPF+";

        File f=new File(dataFolder);
        if (!f.exists()) {
            Utils.toast(this, "Directory not exist, creating...");

            f=new File(this.getExternalFilesDir(null).getAbsolutePath(),"SPF+");

            if(!f.mkdir()){

                Utils.toast(this, "Directory not created");
            }

        }else{


        }




    }

    @Override
    public void onClick(View v) {

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        nDrawerLayout.closeDrawer(nNavigationView, true);


        switch (item.getItemId()) {

            case R.id.import_nav:



                final Handler mDrawerActionHandler = new Handler();
                final long DRAWER_CLOSE_DELAY_MS = 165;


                mDrawerActionHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frame, mSearchFragment)
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                .addToBackStack(null)
                                .commit();


                    }
                }, DRAWER_CLOSE_DELAY_MS);
                break;


            case R.id.model_nav:
                nDrawerLayout.closeDrawer(nNavigationView, true);


                final Handler mDrawerActionHandler0 = new Handler();
                final long DRAWER_CLOSE_DELAY_MS0 = 165;


                mDrawerActionHandler0.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frame, mModelFragment)
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                .addToBackStack(null)
                                .commit();

                    }
                }, DRAWER_CLOSE_DELAY_MS0);



                break;

            case R.id.material_nav:
                nDrawerLayout.closeDrawer(nNavigationView, true);


                final Handler mDrawerActionHandler1 = new Handler();
                final long DRAWER_CLOSE_DELAY_MS1 = 165;


                mDrawerActionHandler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frame, mMaterialFragment)
                                .setTransition(FragmentTransaction.TRANSIT_ENTER_MASK)
                                .addToBackStack(null)
                                .commit();

                    }
                }, DRAWER_CLOSE_DELAY_MS1);



                break;



            case R.id.accessories_nav:
                nDrawerLayout.closeDrawer(nNavigationView, true);


                final Handler mDrawerActionHandler2 = new Handler();
                final long DRAWER_CLOSE_DELAY_MS2 = 165;

                final AccessoriesFragment accessoriesFragment  = new AccessoriesFragment();
                accessoriesFragment.setActivity(this);

                mDrawerActionHandler2.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frame,accessoriesFragment)
                                .setTransition(FragmentTransaction.TRANSIT_ENTER_MASK)
                                .addToBackStack(null)
                                .commit();

                    }
                }, DRAWER_CLOSE_DELAY_MS2);



                break;

            case R.id.externalworks_nav:
                nDrawerLayout.closeDrawer(nNavigationView, true);


                final Handler mDrawerActionHandler3 = new Handler();
                final long DRAWER_CLOSE_DELAY_MS3 = 165;

                final ExternalWorksFragment externalWorksFragment  = new ExternalWorksFragment();
               externalWorksFragment.setActivity(this);

                mDrawerActionHandler3.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frame, externalWorksFragment)
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                .addToBackStack(null)
                                .commit();

                    }
                }, DRAWER_CLOSE_DELAY_MS3);




                break;

            case R.id.dealership_nav:
                nDrawerLayout.closeDrawer(nNavigationView, true);


                final Handler mDrawerActionHandler4 = new Handler();
                final long DRAWER_CLOSE_DELAY_MS4 = 165;

               mDealerFragment  = new DealershipFragment();
                mDealerFragment.setActivity(this);

                mDrawerActionHandler4.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frame, mDealerFragment)
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                .addToBackStack(null)
                                .commit();

                    }
                }, DRAWER_CLOSE_DELAY_MS4);




                break;

            case R.id.cutNotes_nav:
                nDrawerLayout.closeDrawer(nNavigationView, true);

                final CutNotesListFragment cutNotesFragment= new CutNotesListFragment();
                cutNotesFragment.setActivity(this);

                final Handler mDrawerActionHandler5 = new Handler();
                final long DRAWER_CLOSE_DELAY_MS5 = 165;


                mDrawerActionHandler5.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frame, cutNotesFragment)
                                .setTransition(FragmentTransaction.TRANSIT_ENTER_MASK)
                                .addToBackStack(null)
                                .commit();

                    }
                }, DRAWER_CLOSE_DELAY_MS5);



                break;

            case R.id.database_nav:

                copyDBToSDCard();
                break;
            case R.id.deleteDatabase_nav:
                userData.onUpgrade(userData.getWritableDatabase(),userData.Version,userData.Version+1);

      /*         if( userData.deleteAllData()){

                   Utils.toast(this,"Database reset");
                   mModelFragment.modelList.clear();

               }else{
                   Utils.toast(this,"Database not reset");

               }*/
                 break;
        }

        return true;
    }

    public void removeFragment(Fragment fragment){
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.remove(fragment);
        fragmentTransaction.commit();
    }


    private void setFirstItemNavigationView() {
        onNavigationItemSelected(nNavigationView.getMenu().getItem(1));
    }


    public void  setItemNavigationView(int index) {
        onNavigationItemSelected(nNavigationView.getMenu().getItem(index));
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

            if (mSearchFragment.isVisible()) {

                searchItem.setVisible(true);
                mSearchFragment.setScreenContainerWidth(this.getWindowManager().getDefaultDisplay().getWidth());
                mSearchFragment.customView(mSearchFragment.ViewMode,mSearchFragment.OpenFolder,mSearchFragment.getScreenContainerWidth());



            }
           else if (mModelFragment.isVisible()){
                mModelFragment.setScreenContainerHeight(this.getWindowManager().getDefaultDisplay().getHeight());
                mModelFragment.setScreenContainerWidth(this.getWindowManager().getDefaultDisplay().getWidth());
                mModelFragment.customView(mModelFragment.ViewMode,mModelFragment.getScreenContainerWidth(),mModelFragment.getScreenContainerHeight());
           }else if (mMaterialFragment.isVisible()){

                mMaterialFragment.setScreenContainerWidth(this.getWindowManager().getDefaultDisplay().getWidth());
                mMaterialFragment.setScreenContainerHeight(this.getWindowManager().getDefaultDisplay().getHeight());

            mMaterialFragment.customView(mMaterialFragment.viewMode,mMaterialFragment.getScreenContainerWidth(),mMaterialFragment.getScreenContainerHeight());
        }
            //do something...
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {

            if (mSearchFragment.isVisible()) {

                searchItem.setVisible(true);
                mSearchFragment.setScreenContainerWidth(this.getWindowManager().getDefaultDisplay().getWidth());
                mSearchFragment.customView(mSearchFragment.ViewMode,mSearchFragment.OpenFolder,mSearchFragment.getScreenContainerWidth());



            }
            else if (mModelFragment.isVisible()){

                mModelFragment.setScreenContainerHeight(this.getWindowManager().getDefaultDisplay().getHeight());
                mModelFragment.setScreenContainerWidth(this.getWindowManager().getDefaultDisplay().getWidth());
                mModelFragment.customView(mModelFragment.ViewMode,mModelFragment.getScreenContainerWidth(),mModelFragment.getScreenContainerHeight());
            }else if (mMaterialFragment.isVisible()){
                mMaterialFragment.setScreenContainerWidth(this.getWindowManager().getDefaultDisplay().getWidth());
                mMaterialFragment.setScreenContainerHeight(this.getWindowManager().getDefaultDisplay().getHeight());
                mMaterialFragment.customView(mMaterialFragment.viewMode,mMaterialFragment.getScreenContainerWidth(),mMaterialFragment.getScreenContainerHeight());



            }
        }
    }


    public void copyDBToSDCard() {
        File dbpath = this.getDatabasePath(ModelDataBase.DATABASE_NAME);
       String filePath= "storage/sdcard0/";
        try {
            File sd = new File(filePath);
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String currentDBPath =dbpath.getAbsolutePath(); //"//data//"+getPackageName()+"//databases//"+ModelDataBase.DATABASE_NAME+"";
                String backupDBPath = "backup";
                backupDBPath+=ModelDataBase.DATABASE_NAME;

                File backupDB = new File(sd, backupDBPath);
                Log.e("DataBase","Guardado Permitido");

                if (dbpath.exists()) {
                    FileChannel src = new FileInputStream(dbpath).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                    Log.e("DataBase","Base de Datos Importada a SD");
                    Utils.toast(this,
                            "Database Saved");
                }

            }

        }  catch (Exception e) {
           Utils.toast(this,
                    "Error="+e);
            Log.i("FO","exception="+e);
        }


      }

    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode== REQUEST_CODE_MODEL_IMAGE_GALLERY && data !=null){

            Uri uri=data.getData();

            try {
                InputStream   inputStream=getContentResolver().openInputStream(uri);

                Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
                Drawable drawable = new BitmapDrawable(getResources(), bitmap);

              /*  mModelFragment.modelAdapter.mSortedList.get(mModelFragment.modelAdapter.clickView).setBmp(drawable);
                mModelFragment.modelAdapter.notifyItemChanged(mModelFragment.modelAdapter.clickView);

                userData.updatePreviewData(mModelFragment.modelAdapter.mSortedList.get(mModelFragment.modelAdapter.clickView));*/



            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


        }else if(requestCode== REQUEST_CODE_MATERIAL_IMAGE_GALLERY && data !=null){

            Bitmap bitmap = ImagePicker.getImageFromResult(this, resultCode, data);
            Drawable drawable = new BitmapDrawable(getResources(), bitmap);

            if(pickerObject instanceof Material)
                ((Material)pickerObject).setImage(drawable);
            if(pickerObject instanceof PreviewModelInfo)
                ((PreviewModelInfo)pickerObject).setImage(drawable);
            if(pickerObject instanceof Model)
                ((Model)pickerObject).setImage(drawable);
            materialDialogImage.setBackground(drawable);



        } else if (requestCode== REQUEST_CODE_CONTACT_PHONE && data !=null)
        {

            final Dealership dealership=new Dealership();
            final getContactInfo contactInfo;
            String ContctUidVar=null;
            String ContctNamVar=getResources().getString(R.string.importNoAsigned_Cat);

            Uri contctDataVar = data.getData();



            //Contact Name and diferent phone number type
            Cursor contctCursorVar = getContentResolver().query(contctDataVar, null,
                    null, null, null);
            if (contctCursorVar.getCount() > 0) {
                while (contctCursorVar.moveToNext()) {
                    ContctUidVar = contctCursorVar.getString(contctCursorVar.getColumnIndex(ContactsContract.Contacts._ID));

                    ContctNamVar = contctCursorVar.getString(contctCursorVar.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                    Log.i("Names", ContctNamVar);


                }
                contctCursorVar.close();

                  contactInfo=new getContactInfo(ContctUidVar,ContctNamVar,this);


                dealership.setName(contactInfo.getName());
                dealership.setPhone(contactInfo.getPhone());
                dealership.setAdress(contactInfo.getAdress());
                dealership.setEmail(contactInfo.getEmail());
                dealership.setCategory(getResources().getString(R.string.importNoAsigned_Cat));
                dealership.setCompany(contactInfo.getCompany());
                dealership.setDate(userData.getDate());

                userData.addDealership(dealership);


            }

            addcontactdialog.dismiss();

        }



        super.onActivityResult(requestCode, resultCode, data);
    }

    public void showProgress(boolean enabled,int Max){

        if(enabled){

            ((FrameLayout)findViewById(R.id.frameProgress)).setVisibility(View.VISIBLE);
            mProgressBar.setProgress(1);
            mProgressBar.setMax(Max);
        }else{

            ((FrameLayout)findViewById(R.id.frameProgress)).setVisibility(View.GONE);


        }



    }

    public void showSnackbar(String mssg) {

        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.parentLayout);
        Snackbar.make(coordinatorLayout, mssg, Snackbar.LENGTH_SHORT).setAction("VER", new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                setFirstItemNavigationView()
                 ;
            }
        }).show();

    }

    public void imagePicker(int RequestCode, Object obj, View imageContainer){

        this.pickerObject=obj;
        this.materialDialogImage=imageContainer;
        Intent chooseImageIntent = ImagePicker.getPickImageIntent(this);
        startActivityForResult(chooseImageIntent,RequestCode);

    }






    void calContctPickerFnc(Dialog dialog)
    {
        Intent calContctPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);

        startActivityForResult(calContctPickerIntent, REQUEST_CODE_CONTACT_PHONE);
        this.addcontactdialog=dialog;
    }


   //Move Model To Category Dialog
    @Override
    public void onOkClicked(ChangeCategoryPopUp dialog, String newCategory) {

      /*  final SparseBooleanArray checkedList=mModelFragment.modelAdapter.multiChoiceHelper.getCheckedItemPositions();
        ArrayList<Integer>removeIndex=new ArrayList<>();
        if(checkedList.size()>0) {
            for(int i=0;i<checkedList.size();i++) {

                if(checkedList.valueAt(i)) {

                    userData.updatePreviewData(String.valueOf(mModelFragment.modelAdapter.mSortedList.get(checkedList.keyAt(i)).getId()),null,null,null,null,null,newCategory,null,null,null,null,null);
                    removeIndex.add(checkedList.keyAt(i));
                }

            }
        }


        if(mModelFragment.ma.mSpinner.getFirstVisiblePosition()>0){
            mModelFragment.modelAdapter.remove(removeIndex);

            mModelFragment.bufferModel=ModelFragment.filterByParameter(mModelFragment.ma.userData.loadData(),
                    mModelFragment.ma.mSpinner.getSelectedCategory(), mModelFragment.SEARCH_IN_CATEGORY);
        }
*/

        dialog.dismiss();
        mModelFragment.modelAdapter.multiChoiceHelper.clearChoices();

    }

    @Override
    public void onCancelClicked(ChangeCategoryPopUp dialog) {
        dialog.dismiss();
        mModelFragment.modelAdapter.multiChoiceHelper.clearChoices();
    }

    //Add Custom Material Dialog



}


