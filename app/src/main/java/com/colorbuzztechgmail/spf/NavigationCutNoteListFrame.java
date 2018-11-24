package com.colorbuzztechgmail.spf;

import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

import com.colorbuzztechgmail.spf.popup.CreationNotesSelector;

public class NavigationCutNoteListFrame extends NavigationBaseFrame implements CreationNotesSelector.CutNoteGeneratorTypeListener {


    @Override
    protected boolean onBottomBarItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_add:

                final CreationNotesSelector creationNotesSelector=new CreationNotesSelector();
                creationNotesSelector.setCutNoteGeneratorTypeListener(this);
                getChildFragmentManager().beginTransaction()
                        .replace(R.id.container,creationNotesSelector )
                        .setTransition(FragmentTransaction.TRANSIT_ENTER_MASK)
                        .addToBackStack(null)
                        .commit();

                return true;


            case R.id.action_recent:

                getChildFragmentManager().beginTransaction()
                        .replace(R.id.container,BaseFragment.newInstance(BaseFragment.FragmentType.CUTNOTE.name(), getString(R.string.importRecent_Cat),null))
                        .setTransition(FragmentTransaction.TRANSIT_ENTER_MASK)
                        .addToBackStack(null)
                        .commit();

                return true;


            case R.id.action_status:

                getChildFragmentManager().beginTransaction()
                        .replace(R.id.container,CategoryFragment.newInstance("cutnoteList_status", CategoryFragment.ChooserType.CUTNOTE_STATUS.name(),0,this))
                        .setTransition(FragmentTransaction.TRANSIT_ENTER_MASK)
                        .addToBackStack(null)
                        .commit();

                return true;


            case R.id.action_search:
                getChildFragmentManager().beginTransaction()
                        .replace(R.id.container,QuerySearchFragment.newInstance(BaseFragment.FragmentType.CUTNOTE.name(), null,null))
                        .setTransition(FragmentTransaction.TRANSIT_ENTER_MASK)
                        .addToBackStack(null)
                        .commit();
               /* getChildFragmentManager().beginTransaction()
                        .replace(R.id.container,CategoryFragment.newInstance("cutNotes_model", CategoryFragment.ChooserType.CUTNOTE_MODEL.name(),0,this))
                        .setTransition(FragmentTransaction.TRANSIT_ENTER_MASK)
                        .addToBackStack(null)
                        .commit();
*/
                return true;
            case R.id.action_custumer:

                getChildFragmentManager().beginTransaction()
                        .replace(R.id.container,CategoryFragment.newInstance("cutNotes_custumer", CategoryFragment.ChooserType.CUTNOTE_CUSTUMER.name(),0,this))
                        .setTransition(FragmentTransaction.TRANSIT_ENTER_MASK)
                        .addToBackStack(null)
                        .commit();

                return true;



        }




        return false;
    }


    @Override
    protected int bottonNavigationMenu() {
        return (R.menu.menu_cutnoteslist);
    }



    @Override
    public void OnSelectedCategory(String category, CategoryFragment.ChooserType type) {

         showActivity(BaseItemActivity.activityType.CUTNOTELIST,category,type);

    }

    @Override
    public void showCutNoteEditor(CutNoteGenerator.GeneratorType generatorType) {


        EditCutNoteList f= EditCutNoteList.newInstanceAddCutNoteListPopUp(generatorType,null);

        f.show(getFragmentManager(),"Add_note");
      //  Dialog d= new AddCutNoteListPopUp(getContext(),generatorType,null);
       // d.setOwnerActivity(getActivity());
       // d.show();

    }

}
