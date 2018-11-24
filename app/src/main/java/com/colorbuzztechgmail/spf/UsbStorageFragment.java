package com.colorbuzztechgmail.spf;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mjdev.libaums.UsbMassStorageDevice;
import com.github.mjdev.libaums.fs.FileSystem;
import com.github.mjdev.libaums.fs.UsbFile;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

public class UsbStorageFragment extends StorageFragment implements ItemClickListener {


    private static final String ACTION_USB_PERMISSION = "com.colorbuzztechgmail.spf.USB_PERMISSION";

    public static UsbStorageFragment newInstance(int deviceIndex,String type) {
        UsbStorageFragment f = new UsbStorageFragment();
        Bundle args = new Bundle();
        args.putInt( DEVICE_INDEX,deviceIndex);
        f.setType(LocationType.valueOf(type));
        f.setArguments(args);


        return f;
    }
    private final BroadcastReceiver usbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {

                UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {

                    if (device != null) {
                        massStorageDevices = UsbMassStorageDevice.getMassStorageDevices(getContext());
                        Utils.toast(getContext(),String.valueOf(massStorageDevices.length));
                        setupDevice();
                    }
                }

            } /*else if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)) {
                UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);

                Log.d("USB", "USB device attached");

                // determine if connected device is a mass storage devuce
                if (device != null) {
                    discoverDevice();
                }
            } */else if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
                UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);

                Log.d("USB", "USB device detached");

                // determine if connected device is a mass storage devuce
                if (device != null) {
                    if (currentDevice != -1) {
                        massStorageDevices[currentDevice].close();

                    }
                    resetData();
                    // check if there are other devices or set action bar title
                    // to no device if not
                    // discoverDevice();
                }
            }

        }
    };


    private RecyclerView mRecycler;
    /* package */ UsbAdapter adapter;
    private Deque<List<Object>> dirs = new ArrayDeque<List<Object>>();
    private FileSystem currentFs;
    int viewMode=0;
    int nColumn=0;
    GridSpacingItemDecoration itemDecoration;
    private  int spaceItem=8;


    UsbMassStorageDevice[] massStorageDevices;

    private int currentDevice = -1;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        //filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        getActivity().registerReceiver(usbReceiver, filter);
        currentDevice=getArguments().getInt(DEVICE_INDEX);
        try {
            adapter=new UsbAdapter(getContext(),null,viewMode);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Nullable
    @Override


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        final View view = inflater.inflate(R.layout.search_fragment, container, false);
        setHasOptionsMenu(true);

        setupRecycler(view);



        view.findViewById(R.id.pathBarContainer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePathBar(false, null);
                if(dirs.size()>0)
                    setupAdapter(dirs.pop());
                 updateBar();

            }
        });







        return view;
    }


    private void setupRecycler(View view){

        mRecycler=view.findViewById(R.id.recyclerView);


        customView(viewMode);



    }

    public void setupAdapter(List<Object> header) {


        try {
            mRecycler.setAdapter(adapter = new UsbAdapter(getContext(), header, viewMode));
            adapter.setOnClickListener(this);

        } catch (IOException e) {
            e.printStackTrace();
        }


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

            final GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), nColumn, LinearLayoutManager.VERTICAL, false);
            itemDecoration = new GridSpacingItemDecoration(nColumn, 8, true);

            mRecycler.addItemDecoration(itemDecoration);
            mRecycler.setLayoutManager(gridLayoutManager);



        } else if (mode==1) {///GridView




            nColumn = 2;


            final GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), nColumn);


            itemDecoration = new GridSpacingItemDecoration(nColumn, 8, true);

            mRecycler.addItemDecoration(itemDecoration);
            mRecycler.setLayoutManager(gridLayoutManager);





        }


    }


    private void selectDevice(int position) {
        currentDevice = position;
        setupDevice();
    }

    /**
     * Searches for connected mass storage devices, and initializes them if it
     * could find some.
     */
    private void discoverDevice() {
        UsbManager usbManager = (UsbManager) getContext().getSystemService(Context.USB_SERVICE);
        massStorageDevices = UsbMassStorageDevice.getMassStorageDevices(getContext());

        if (massStorageDevices.length == 0) {
            Log.w("USB", "no device found!");
            android.support.v7.app.ActionBar actionBar = ((MainActivity)getActivity()).getSupportActionBar();
            actionBar.setTitle("No device");
            mRecycler.setAdapter(null);
            return;
        }


        if(currentDevice==-1) {
            currentDevice = 0;
        }

        UsbDevice usbDevice = (UsbDevice) getActivity().getIntent().getParcelableExtra(UsbManager.EXTRA_DEVICE);

        if (usbDevice != null && usbManager.hasPermission(usbDevice)) {
            Log.d("USB", "received usb device via intent");
            // requesting permission is not needed in this case
            setupDevice();
        } else {
            // first request permission from user to communicate with the
            // underlying
            // UsbDevice
            PendingIntent permissionIntent = PendingIntent.getBroadcast(getContext() ,0, new Intent(
                    ACTION_USB_PERMISSION), 0);
            usbManager.requestPermission(massStorageDevices[(massStorageDevices.length-1)].getUsbDevice(), permissionIntent);
        }
    }
    /**
     * Sets the device up and shows the contents of the root directory.
     */
    private void setupDevice() {
        try {
            massStorageDevices[(massStorageDevices.length-1)].init();


            // we always use the first partition of the device
            currentFs = massStorageDevices[(massStorageDevices.length-1)].getPartitions().get(0).getFileSystem();
            Log.d("USB", "Capacity: " + currentFs.getCapacity());
            Log.d("USB", "Occupied Space: " + currentFs.getOccupiedSpace());
            Log.d("USB", "Free Space: " + currentFs.getFreeSpace());
            Log.d("USB", "Chunk size: " + currentFs.getChunkSize());
            UsbFile root = currentFs.getRootDirectory();


            setMyTag(currentFs.getVolumeLabel());

            Object[] obj = new Object[1];
            obj[0] = root;


            new SearchFilesAsync().execute(obj);

         } catch (IOException e) {
            Log.e("USB", "error setting up device", e);
        }

    }

    public List<Object> dirUsbSearch(UsbFile dir){

        List<Object> usbFileList=new ArrayList<>();
        Deque<UsbFile> dirs = new ArrayDeque<UsbFile>();


        try {
            List<UsbFile> listFile = new ArrayList<>();
            listFile.addAll(Arrays.asList(dir.listFiles()));
            // Utils.toast(getContext(),String.valueOf(listFile.size()));
            CustomHeader rootHeader=new CustomHeader("ROOT",999,null);

            if (listFile != null) {
                for (int i = 0; i < listFile.size(); i++) {

                    //Utils.toast(getContext(),listFile.get(i).getName());

                    if (listFile.get(i).isDirectory()) {


                        dirs.push(listFile.get(i));

                        for(int j=0;j<dirs.size();j++){

                            List<UsbFile> aux=new ArrayList<>();
                            UsbFile auxDir=dirs.pop();

                            aux.addAll(Arrays.asList(auxDir.listFiles()));
                            if(aux.size()>0) {
                                CustomHeader header= new CustomHeader(auxDir.getName(),j,null);

                                for (UsbFile usb : aux) {

                                    if (usb.isDirectory()) {

                                        dirs.add(usb);
                                        j--;

                                    } else {

                                        if (usb.getName().toLowerCase().endsWith(".spf")) {


                                            header.addObject(usb);
                                            //   Utils.toast(getContext(), "FILE " + listFile.get(i).getName());


                                        }
                                    }

                                }

                                if(header.getBuffer().size()>0)
                                    usbFileList.add(header);



                            }


                        }


                    } else {
                        if (listFile.get(i).getName().toLowerCase().endsWith(".spf")){

                            rootHeader.addObject(listFile.get(i));
                            // Utils.toast(getContext(),"FILE " + listFile.get(i).getName());


                        }
                    }
                }

                if(rootHeader.getBuffer().size()>0)
                    usbFileList.add(rootHeader);


            }
        }catch (Exception e){


            Utils.toast(getContext(),"Fallo  busqueda USB");

        }

        return usbFileList;


/*
        UsbFile newDir = root.createDirectory("foo");
        UsbFile file = newDir.createFile("bar.txt");

// write to a file
        OutputStream os = new UsbFileOutputStream(file);

        os.write("hello".getBytes());
        os.close();

// read from a file
        InputStream is = new UsbFileInputStream(file);
        byte[] buffer = new byte[currentFs.getChunkSize()];
        is.read(buffer);*/

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(usbReceiver);
        if (currentDevice != -1) {
            Log.d("USB", "Closing device");

            massStorageDevices[massStorageDevices.length-1].close();
        }

    }


    private void resetData(){
        currentFs=null;
        mRecycler.setAdapter(null);
        adapter.clear();
        dirs.clear();
        importButtonListener.showImportButton(false);
        updateBar();
        updatePathBar(false,null);


    }

    @Override
    public void onClick(View v, int position, boolean isLongClick) {
        if(adapter.isHeader(position)){


            dirs.push(adapter.getCurrentDir());
            CustomHeader header=(CustomHeader) adapter.getItem(position);
            updatePathBar(true,header.getTitle());
            setupAdapter(header.getBuffer());



        }else {


            updateBar();


        }


    }

    public class SearchFilesAsync extends AsyncTask<Object, Float, List<Object>> {

        com.colorbuzztechgmail.spf.popup.waitPoup waitPoup;


        protected void onPreExecute() {

            waitPoup = com.colorbuzztechgmail.spf.popup.waitPoup.newInstance(getString(R.string.dialog_searchFiles));


            waitPoup.show(getFragmentManager(), "waitPopup");
        }

        protected List<Object> doInBackground(Object[] device) {

            try {

                if (device[0] != null) {
                    UsbFile f =(UsbFile)device[0];


                    return dirUsbSearch(f);


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

            waitPoup.dismiss();

            if (headers != null) {

                setupAdapter(headers);
            } else {

                Utils.toast(getContext(), "No se encontaron archivos");
            }


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
    protected void PrepareDataToImport(String directory) {

    }

    @Override
    protected int getItemCount() {
        if(adapter!=null){

            adapter.getItemCount();
        }
        return 0;
    }

    @Override
    protected int getCheckedItemCount() {
        return adapter.checkedItemCount;
    }

    @Override
    protected void initSearch() {


        if(adapter.getItemCount()==0){

            discoverDevice();


        }

    }


    @Override
    protected void selectAllItems(boolean enable) {
        adapter.checkedItemCount=0;

        for(int i=0;i<adapter.sparseState.size();i++){


            adapter.sparseState.put(adapter.sparseState.keyAt(i),enable);
        }


        if(enable){


            adapter.checkedItemCount=adapter.sparseState.size();

        }
        updateBar();
    }
}
