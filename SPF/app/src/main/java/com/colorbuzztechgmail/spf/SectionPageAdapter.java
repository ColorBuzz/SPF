package com.colorbuzztechgmail.spf;

        import android.support.v4.app.Fragment;

        import android.support.v4.app.FragmentManager;
        import android.support.v4.app.FragmentPagerAdapter;
        import android.view.ViewGroup;

        import java.util.ArrayList;
        import java.util.List;

/**
 * Created by PC01 on 21/06/2017.
 */
public class SectionPageAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragmentList= new ArrayList<>();

    private List<String> mFragmentTitleList= new ArrayList<>();

    AdapterFinishUpdateCallbacks listener;


    AdapterStartUpdateCallbacks startListener;

    public void AddFragment(Fragment fragment,String Title){

        mFragmentList.add(fragment);
        mFragmentTitleList.add(Title);



    }
    public void setUpdateFinishListener(AdapterFinishUpdateCallbacks itemClickListener) {

        this.listener = itemClickListener;

    }
    public void setUpdateStarListener(AdapterStartUpdateCallbacks itemClickListener) {

        this.startListener = itemClickListener;

    }

    @Override
    public void startUpdate(ViewGroup container) {
        super.startUpdate(container);
        if (this.startListener != null)
        {
            this.startListener.onStartUpdate();
        }
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        super.finishUpdate(container);


            if (this.listener != null)
            {
                this.listener.onFinishUpdate();
            }

    }

    @Override
    public CharSequence getPageTitle(int position) {



        return mFragmentTitleList.get(position);
    }

    public SectionPageAdapter(FragmentManager fm){
        super(fm);


    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }
}
