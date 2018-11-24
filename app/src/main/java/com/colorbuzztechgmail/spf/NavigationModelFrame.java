package com.colorbuzztechgmail.spf;

import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

public class NavigationModelFrame extends NavigationBaseFrame  implements TabSearchFragment.OnFinishedImportTask {


    @Override
    protected boolean onBottomBarItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_add:

                final TabSearchFragment fr=new TabSearchFragment();
                fr.setOnFinishedImportTask(this);

                        getChildFragmentManager().beginTransaction()
                                .replace(R.id.container,fr )
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                .addToBackStack(null)
                                .commit();




                return true;



            case R.id.action_recent:

                getChildFragmentManager().beginTransaction()
                        .replace(R.id.container,BaseFragment.newInstance(BaseFragment.FragmentType.MODEL.name(), getString(R.string.importRecent_Cat),null))
                        .setTransition(FragmentTransaction.TRANSIT_ENTER_MASK)
                        .addToBackStack(null)
                        .commit();


                return true;



            case R.id.action_category:

                // showDirectoryChooser();




                getChildFragmentManager().beginTransaction()
                        .replace(R.id.container,CategoryFragment.newInstance("model_Directory", CategoryFragment.ChooserType.MODEL_DIRECTORY.name(),0,this))
                        .setTransition(FragmentTransaction.TRANSIT_ENTER_MASK)
                        .addToBackStack(null)
                        .commit();




                return true;


            case R.id.action_custumer:





               getChildFragmentManager().beginTransaction()
                        .replace(R.id.container,CategoryFragment.newInstance("model_Custumer", CategoryFragment.ChooserType.MODEL_CUSTUMER.name(),0,this))
                        .setTransition(FragmentTransaction.TRANSIT_ENTER_MASK)
                        .addToBackStack(null)
                        .commit();

                return true;


            case R.id.action_search:
                getChildFragmentManager().beginTransaction()
                        .replace(R.id.container,QuerySearchFragment.newInstance(BaseFragment.FragmentType.MODEL.name(), null))
                        .setTransition(FragmentTransaction.TRANSIT_ENTER_MASK)
                        .addToBackStack(null)
                        .commit();
                return true;
        }



        return false;
    }



    @Override
    protected int bottonNavigationMenu() {
        return (R.menu.menu_hub);
    }


    @Override
    public void OnSelectedCategory(String category, CategoryFragment.ChooserType type) {


        showActivity(BaseItemActivity.activityType.MODEL,category,type);
    }


    ////----------IMPORTACION DE MODELOS

    @Override
    public void finishedImportTask(String directory) {

       showActivity(BaseItemActivity.activityType.MODEL,directory,CategoryFragment.ChooserType.MODEL_DIRECTORY);

    }
}
