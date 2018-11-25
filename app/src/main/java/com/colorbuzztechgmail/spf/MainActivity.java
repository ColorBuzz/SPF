package com.colorbuzztechgmail.spf;

import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import android.widget.ImageView;
import android.widget.ProgressBar;


import com.github.mjdev.libaums.UsbMassStorageDevice;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.channels.FileChannel;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener{


MenuItem searchItem;
    public static final String ACTION_USB_PERMISSION = "com.colorbuzztechgmail.spf.USB_PERMISSION";



    public NavigationView nNavigationView;
    private DrawerLayout nDrawerLayout;
    private ActionBarDrawerToggle nToggle;
    public Toolbar myToolbar;




    ModelDataBase userData;
    public static int REQUEST_CODE_MATERIAL_IMAGE_GALLERY =888;


    public ProgressBar mProgressBar;


    public String dataFolder;
    private Object pickerObject;
    private View materialDialogImage;
    UsbMassStorageDevice[] massStorageDevices;





    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme_NoActionBar);
        setContentView(R.layout.navigation_activity1);
        LoadView();

        IntentFilter filter = new IntentFilter(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        registerReceiver(usbReceiver,filter);
        discoverDevice();


    }

    private final BroadcastReceiver usbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)) {
                UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);

                Log.d("USB", "USB device attached");

                // determine if connected device is a mass storage devuce
                if (device != null) {
                    discoverDevice();
                    Utils.toast(getBaseContext(), device.getProductName() + " Conectado");

                }
            } else if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
                UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);


                Log.d("USB", "USB device detached");

                // determine if connected device is a mass storage devuce
                if (device != null) {


                    Utils.toast(getBaseContext(),device.getProductName() + " Desconectado");
                }
            }

        }
    };

    private void discoverDevice() {
        UsbManager usbManager = (UsbManager)  getSystemService(Context.USB_SERVICE);
        massStorageDevices = UsbMassStorageDevice.getMassStorageDevices(this);

        if (massStorageDevices.length == 0) {
            Log.w("USB", "no device found!");

             return;
        }



        UsbDevice usbDevice = (UsbDevice)getIntent().getParcelableExtra(UsbManager.EXTRA_DEVICE);

        if (usbDevice != null && usbManager.hasPermission(usbDevice)) {
            Log.d("USB", "received usb device via intent");
            // requesting permission is not needed in this case
         } else {
            // first request permission from user to communicate with the
            // underlying
            // UsbDevice
            PendingIntent permissionIntent = PendingIntent.getBroadcast(this ,0, new Intent(
                    ACTION_USB_PERMISSION), 0);
            usbManager.requestPermission(massStorageDevices[massStorageDevices.length-1].getUsbDevice(), permissionIntent);
        }
    }


    @Nullable
    @Override
    public ActionBar getSupportActionBar() {
        return super.getSupportActionBar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       ;
        getMenuInflater().inflate(R.menu.menu_home, menu);





//
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id){


            case android.R.id.home:

               getSupportActionBar().setHomeButtonEnabled(false);
               getSupportActionBar().setDisplayHomeAsUpEnabled(false);
               findViewById(R.id.bottomNavigation).setVisibility(View.GONE);
                setupToolBar();
                onBackPressed();

                return true;


            case R.id.action_save:
                copyDBToSDCard();

                return true;


            case R.id.action_delete:
                userData.onUpgrade(userData.getWritableDatabase(),userData.Version,userData.Version+1);

                return true;
            case R.id.action_custumer_gen:

                CustumerGenerator.create(this);

                return true;

            case R.id.action_material_gen:

                CustomMaterialGenerator.create(1000,this);

                return true;


            case R.id.action_cutnoteList_gen:
                CutNoteListGenerator.create(10,this,10);


                return true;




        }

        return super.onOptionsItemSelected(item);

    }


    private void LoadView() {

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},000);

        userData= new ModelDataBase(this);

        createFolderData();

        File dbpath = this.getDatabasePath(ModelDataBase.DATABASE_NAME);
        if (dbpath.exists()){



           // Utils.toast(this,"DATABASE_EXIST" + this.getDatabasePath(ModelDataBase.DATABASE_NAME).toString());
        }else{

            Utils.toast(this,"DATABASE__NOT_EXIST");
        }

/*

        nNavigationView = (NavigationView) findViewById(R.id.navigationMenu);
        nNavigationView.setNavigationItemSelectedListener(this);
*/

        myToolbar = (Toolbar) findViewById(R.id.appbar);
        setSupportActionBar(myToolbar);
        setupToolBar();



/*
        nDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        // nDrawerLayout.setScrimColor(Color.parseColor("#22000000"));
        // nDrawerLayout.setScrimColor(Color.parseColor("#99000000"));
        nDrawerLayout.setScrimColor(Color.TRANSPARENT);
        setToggle();
        setFirstItemNavigationView();*/
    }

    public void setToggle(){

        nToggle = new ActionBarDrawerToggle(this, nDrawerLayout, myToolbar, R.string.nav_open, R.string.nav_closed);
         nDrawerLayout.addDrawerListener(nToggle);
        nToggle.syncState();

    }


    private void setupToolBar(){

        myToolbar.setTitleTextColor(getResources().getColor(R.color.iconsColor));
        myToolbar.setSubtitleTextColor(getResources().getColor(R.color.iconsColor));
        myToolbar.setTitle("Inicio");



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




        switch (v.getId()) {


            case R.id.card_recent:

                final Handler mDrawerActionHandler9 = new Handler();
                final long DRAWER_CLOSE_DELAY_MS9 = 165;


                mDrawerActionHandler9.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frame, BaseFragment.newInstance(BaseFragment.FragmentType.RECENT.name(),getString(R.string.action_fast_access),null))
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                .addToBackStack(null)
                                .commit();


                    }
                }, DRAWER_CLOSE_DELAY_MS9);
                break;


            case R.id.import_nav:

                final Handler mDrawerActionHandler = new Handler();
                final long DRAWER_CLOSE_DELAY_MS = 165;


                mDrawerActionHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frame, new TabSearchFragment())
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                .addToBackStack(null)
                                .commit();


                    }
                }, DRAWER_CLOSE_DELAY_MS);
                break;


            case R.id.card_model:


                final Handler mDrawerActionHandler0 = new Handler();
                final long DRAWER_CLOSE_DELAY_MS0 = 165;


                mDrawerActionHandler0.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frame, new NavigationModelFrame())
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                .addToBackStack(null)
                                .commit();

                    }
                }, DRAWER_CLOSE_DELAY_MS0);



                break;

            case R.id.card_material:


                final Handler mDrawerActionHandler1 = new Handler();
                final long DRAWER_CLOSE_DELAY_MS1 = 165;


                mDrawerActionHandler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frame,new NavigationMaterialFrame())
                                .setTransition(FragmentTransaction.TRANSIT_ENTER_MASK)
                                .addToBackStack(null)
                                .commit();

                    }
                }, DRAWER_CLOSE_DELAY_MS1);



                break;



            case R.id.card_complement:


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

            case R.id.card_external:


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

            case R.id.card_dealer:


                final Handler mDrawerActionHandler4 = new Handler();
                final long DRAWER_CLOSE_DELAY_MS4 = 165;

                final DealershipFragment mDealerFragment  = new DealershipFragment();
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

            case R.id.card_list:


                final Handler mDrawerActionHandler5 = new Handler();
                final long DRAWER_CLOSE_DELAY_MS5 = 165;


                mDrawerActionHandler5.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frame, new NavigationCutNoteListFrame())
                                .setTransition(FragmentTransaction.TRANSIT_ENTER_MASK)
                                .addToBackStack(null)
                                .commit();

                    }
                }, DRAWER_CLOSE_DELAY_MS5);



                break;



        }



    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        nDrawerLayout.closeDrawer(nNavigationView, true);





return false;

    }


    private void setFirstItemNavigationView() {
        onNavigationItemSelected(nNavigationView.getMenu().getItem(0));
    }


    public void  setItemNavigationView(int index) {
        onNavigationItemSelected(nNavigationView.getMenu().getItem(index));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

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


    public void imagePicker(int RequestCode, Object obj, View imageContainer){

        this.pickerObject=obj;
        this.materialDialogImage=imageContainer;
        Intent chooseImageIntent = ImagePicker.getPickImageIntent(this);
        startActivityForResult(chooseImageIntent,RequestCode);

    }




    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(usbReceiver);
    }
}


