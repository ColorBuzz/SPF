package com.colorbuzztechgmail.spf;

import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

public class NavigationMaterialFrame extends NavigationBaseFrame {


    private EditMaterial mEditMaterialFragment ;

    @Override
    protected boolean onBottomBarItemSelected(MenuItem item) {


        switch (item.getItemId()) {

            case R.id.action_add:

                    getChildFragmentManager().beginTransaction()

                            .replace(R.id.container, EditMaterial.newInstanceEditMaterialPopUp(new int[1],new long[1],false,null))
                            .setTransition(FragmentTransaction.TRANSIT_ENTER_MASK)
                            .addToBackStack(null)
                            .commit();



                return true;



            case R.id.action_recent:

                getChildFragmentManager().beginTransaction()
                        .replace(R.id.container,BaseFragment.newInstance(BaseFragment.FragmentType.MATERIAL.name(), null,null))
                        .setTransition(FragmentTransaction.TRANSIT_ENTER_MASK)
                        .addToBackStack(null)
                        .commit();

                return true;


            case R.id.action_leather:

                // showFilterChooser(FilterMode.DEALER);


                getChildFragmentManager().beginTransaction()
                        .replace(R.id.container,CategoryFragment.newInstance("material_type_Directory", CategoryFragment.ChooserType.MATERIAL_TYPE.name(),0,this))
                        .setTransition(FragmentTransaction.TRANSIT_ENTER_MASK)
                        .addToBackStack(null)
                        .commit();

                return true;

            case R.id.action_dealer:

                getChildFragmentManager().beginTransaction()
                        .replace(R.id.container,CategoryFragment.newInstance("material_type_Directory", CategoryFragment.ChooserType.MATERIAL_DEALER.name(),0,this))
                        .setTransition(FragmentTransaction.TRANSIT_ENTER_MASK)
                        .addToBackStack(null)
                        .commit();
                return true;

            case R.id.action_search:
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.container,QuerySearchFragment.newInstance(BaseFragment.FragmentType.MATERIAL.name(), null,null))
                    .setTransition(FragmentTransaction.TRANSIT_ENTER_MASK)
                    .addToBackStack(null)
                    .commit();
           return true;

        }



        return false;
    }

    @Override
    protected int bottonNavigationMenu() {
        return (R.menu.menu_custom_material);
    }


    @Override
    public void OnSelectedCategory(String category, CategoryFragment.ChooserType type) {


        showActivity(BaseItemActivity.activityType.MATERIAL,category,type);

    }



}
