package com.colorbuzztechgmail.spf;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public abstract class NavigationBaseFrame extends Fragment implements BottomNavigationView.OnNavigationItemSelectedListener,CategoryFragment.OnSelectedCategoryListener {
    private BottomNavigationView bottomNavigationView;
    private AppBarLayout appBarLayout;
    private MenuItem actualMenuItem;


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.navigation_frame, container, false);
        setHasOptionsMenu(true);



        appBarLayout=(AppBarLayout)getActivity().findViewById(R.id.appBarLayout);
        bottomNavigationView=(BottomNavigationView)getActivity().findViewById(R.id.bottomNavigation);
        bottomNavigationView.setVisibility(View.VISIBLE);

         onCreateBottonNavigationMenu(bottonNavigationMenu());


         bottomNavigationView.setSelectedItemId(R.id.action_recent);



        return view;

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        actualMenuItem=item;
         collapseToolbar();
        updateTitleBar(item.getTitle().toString());
        return onBottomBarItemSelected(item);


    }

    private void onCreateBottonNavigationMenu(int menu) {

        if(bottomNavigationView.getChildCount()>0){
            bottomNavigationView.getMenu().clear();


        }
        bottomNavigationView.inflateMenu(menu);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        Utils.disableShiftMode(bottomNavigationView);

    }

    protected abstract boolean onBottomBarItemSelected(MenuItem item);

    protected abstract int bottonNavigationMenu();

     public void updateTitleBar(String title){


             int count=0;

             ActionBar actionBar =((AppCompatActivity) getActivity()).getSupportActionBar();

             actionBar.setTitle(title);
             actionBar.setSubtitle(  "");



     }

    public void collapseToolbar(){
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
        AppBarLayout.Behavior  behavior =(AppBarLayout.Behavior)  params.getBehavior();
        if(behavior!=null) {
            behavior.setTopAndBottomOffset(0);
            behavior.onNestedPreScroll((CoordinatorLayout) getActivity().findViewById(R.id.parentLayout), appBarLayout, null, 0, 1, new int[2]);
        }
    }


    public void showActivity(BaseItemActivity.activityType activityType,String category, CategoryFragment.ChooserType type){

        switch (activityType){


            case MODEL:
              // ModelItemActivity.newInstaceItemActivity(BaseItemActivity.activityType.MODEL,getContext(),category, type,Utils.ViewMode.GRID,category);

                ModelItemActivity.newInstaceItemActivity(BaseItemActivity.activityType.MODEL,getContext(),category, type,Utils.ViewMode.LIST,category);


                break;


            case MATERIAL:
                CustomMaterialItemActivity.newInstaceItemActivity(BaseItemActivity.activityType.MATERIAL,getContext(),category, type,Utils.ViewMode.LIST,category);


                break;


            case CUTNOTELIST:
               CutNotesListItemActivity.newInstaceItemActivity(BaseItemActivity.activityType.CUTNOTELIST,getContext(),category, type,Utils.ViewMode.GRID,category);

                break;



        }




    }


    @Override
    public void onStart() {
         if(actualMenuItem!=null)
             if(actualMenuItem.getItemId()!=R.id.action_add)
             onNavigationItemSelected(actualMenuItem);
        super.onStart();
    }



}
