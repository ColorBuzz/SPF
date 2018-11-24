package com.colorbuzztechgmail.spf;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.github.mjdev.libaums.UsbMassStorageDevice;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by PC01 on 21/06/2017.
 */
public class TabSearchFragment extends Fragment implements SearchView.OnQueryTextListener,StorageFragment.ImportButtonListener{

    public FloatingActionButton backBtn;
    MenuItem viewItem;
    MenuItem searchItem;
    MenuItem selectAllItem;
    ActionMode choiceActionMode;
    String phonePath=null;
    ArrayList<String>sdPath;
    UsbMassStorageDevice[] massStorageDevices;
    SectionPageAdapter sectionPageAdapter;
    boolean selectAll=false;
    private TabLayout tabLayout ;
    private CustomSearchView customSearchView;
    private final ActionMode.Callback mCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {



            return true;

        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {







            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {


            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {

            finishSupportActionMode();
        }
    };
    private OnFinishedImportTask OnFinishedImportTask;

    private boolean all=false;


    public void setOnFinishedImportTask(OnFinishedImportTask OnFinishedImportTask) {
        this.OnFinishedImportTask = OnFinishedImportTask;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar =((AppCompatActivity) getActivity()).getSupportActionBar();


        actionBar.setTitle(getString(R.string.importText));

    }

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view=inflater.inflate(R.layout.search_tab_fragment,container,false);
        setHasOptionsMenu(true);

        backBtn=((FloatingActionButton)getActivity().findViewById(R.id.fabbutton));
        backBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_open_in_browser_white_24dp));
        backBtn.setVisibility(View.GONE);
        setupPaths();
        setupTabLayout(view);
        backBtn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {

                if (!( getTab(getSelectedTab()).isImportFile())) {

                    selectAllItem.setVisible(false);

                    view.setVisibility(View.INVISIBLE);

                    all = false;
                    getTab(getSelectedTab()).updateBar();
                    selectAllItem.setTitle(getString(R.string.action_selectAll));

                } else {


                    getTab(getSelectedTab()).showDirectoryChooser();



                }
            }
        });

        view.findViewById(R.id.title).setVisibility(View.GONE);


        return view;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

           menu.clear();

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);



// You can hide the state of the menu item here if you call getActivity().supportInvalidateOptionsMenu(); somewhere in your code

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //noinspection SimplifiableIfStatement
        switch (item.getItemId()){

            case R.id.action_viewBar:



                return true;

            case R.id.action_searchBar:

                loadSearchBar();


                return true;


        }
        return super.onOptionsItemSelected(item);
    }

    private void setupPaths(){

        sdPath=new ArrayList<>();

        phonePath=Environment.getExternalStorageDirectory().getAbsolutePath();

        for (File f : getContext().getExternalFilesDirs("/"))
            if (Environment.isExternalStorageRemovable(f)) {
                Log.println(Log.DEBUG, "#", f.getParentFile().getParentFile().getParentFile().getParent());
                sdPath.add(f.getParentFile().getParentFile().getParentFile().getParent());

            }

        massStorageDevices=UsbMassStorageDevice.getMassStorageDevices(getContext());



    }

    private int getSelectedTab(){

        return tabLayout.getSelectedTabPosition();
    }

    /*@Override
    public void onStart() {
        super.onStart();

        int width = getDialog().getWindow().getWindowManager().getDefaultDisplay().getWidth();
        int height =ViewGroup.LayoutParams.MATCH_PARENT;
        float factor=width/(16f/9f);

        getDialog().getWindow().setLayout(width, height);

    }*/

    private void createFragments(){

        sectionPageAdapter=new SectionPageAdapter(getChildFragmentManager());

        if(phonePath!=null){

            sectionPageAdapter.AddFragment(PhoneStorageFragment.newInstance(phonePath, StorageFragment.LocationType.PHONE.name()),"TELEFONO");
            ((StorageFragment) sectionPageAdapter.getItem(sectionPageAdapter.getCount()-1)).setMyTag("TELEFONO");

        }

        if(sdPath.size()>0){
            int count=0;

            for (String path:sdPath){

                sectionPageAdapter.AddFragment(PhoneStorageFragment.newInstance(path,StorageFragment.LocationType.SDCARD.name()),"SDCARD " + String.valueOf(count));
                ((StorageFragment) sectionPageAdapter.getItem(sectionPageAdapter.getCount()-1)).setMyTag("SDCARD " + String.valueOf(count));

                count++;
            }


        }

        for(int i =0;i<massStorageDevices.length;i++){

            sectionPageAdapter.AddFragment(UsbStorageFragment.newInstance(i,StorageFragment.LocationType.USB.name()),"USB");
            ((StorageFragment) sectionPageAdapter.getItem(sectionPageAdapter.getCount()-1)).setMyTag("USB");

        }


    }

    private void setupTabLayout(View view) {

        createFragments();

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        viewPager.setAdapter(sectionPageAdapter);
        viewPager.setOffscreenPageLimit(3);

        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(0);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {




                getTab(tab.getPosition()).initSearch();
               // getTab(tab.getPosition()).updateBar();


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if(getTab(tab.getPosition()).isImportFile()){
                    getTab(tab.getPosition()).selectAllItems(false);

                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {


            }
        });


        for (int i = 0; i < tabLayout.getTabCount(); i++) {


            getTab(i).setImportButtonListener(this);
            getTab(i).setOnFinishedImportTask(OnFinishedImportTask);
            if ( getTab(i).getType().equals(StorageFragment.LocationType.PHONE)) {
                tabLayout.getTabAt(i).setIcon(R.drawable.ic_phone_selector);


            } else if (getTab(i).getType().equals(StorageFragment.LocationType.SDCARD)) {
                tabLayout.getTabAt(i).setIcon(R.drawable.ic_sd_storage_selector);


            } else {


                tabLayout.getTabAt(i).setIcon(R.drawable.ic_usb_selector);
            }



        }
    }

    private  StorageFragment getTab(int position){

        return  (StorageFragment) sectionPageAdapter.getItem(position);

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

    private void loadSearchBar(){
        startSupportActionModeIfNeeded();


    }

    public void startSupportActionModeIfNeeded() {
        if (choiceActionMode == null) {
            if (mCallback == null) {
                throw new IllegalStateException("No callback set");
            }

            customSearchView = new CustomSearchView(getContext());
            customSearchView.setOnQueryTextListener(this);
            customSearchView.setQueryHint("Buscar");

            choiceActionMode = ((MainActivity)getActivity()).startSupportActionMode(mCallback);


        }
    }

    private void finishSupportActionMode(){


        if(choiceActionMode!=null){

            choiceActionMode.finish();
            choiceActionMode=null;


        }


    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {

        return false;
    }


    @Override
    public void showImportButton(boolean state) {

        if(state){


            backBtn.setVisibility(View.VISIBLE);

        }else {


            backBtn.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }


    public interface OnFinishedImportTask {

        void finishedImportTask(String directory);


    }
}
