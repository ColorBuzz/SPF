package com.colorbuzztechgmail.spf;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.view.ActionMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * Created by PC01 on 21/06/2017.
 *
 *
 */


public class TabCategoryFragment extends DialogFragment implements CategoryFragment.OnSelectedCategoryListener {

         private static String CATEGORY_TYPE="categoryType";
         ViewPager viewPager;
         ActionMode choiceActionMode;
         SectionPageAdapter sectionPageAdapter;
         private TabLayout tabLayout ;
         private CategoryType categoryType;
         private CategoryFragment.OnSelectedCategoryListener categotyListener;


    public  enum CategoryType{

        MODEL,
        MATERIAL,
        CUTNOTE

    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog d=getDialog();

        if(d!=null){

            int width =ViewGroup.LayoutParams.MATCH_PARENT;//d.getWindow().getWindowManager().getDefaultDisplay().getWidth();
            int height =ViewGroup.LayoutParams.MATCH_PARENT;
            float factor=width/(16f/9f);

            d.getWindow().setLayout(width, height);


            d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        }

    }

    @Override
    public void OnSelectedCategory(String category, CategoryFragment.ChooserType type) {


        if(categotyListener!=null){

            categotyListener.OnSelectedCategory(category,type);
            this.dismissAllowingStateLoss();

        }

    }


    public static TabCategoryFragment newInstance(@NonNull String type, @NonNull CategoryFragment.OnSelectedCategoryListener listener){


        TabCategoryFragment tabModelFragment=new TabCategoryFragment();

        tabModelFragment.setCategoryType(CategoryType.valueOf(type));
        tabModelFragment.setCategotyListener(listener);



        return tabModelFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);




        }


    public void setCategotyListener(CategoryFragment.OnSelectedCategoryListener categotyListener) {
        this.categotyListener = categotyListener;
    }

    public CategoryType getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(CategoryType categoryType) {
        this.categoryType = categoryType;
    }

    @Nullable
        @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view=inflater.inflate(R.layout.search_tab_fragment,container,false);
        setHasOptionsMenu(true);

        view.findViewById(R.id.saveFrame).setVisibility(View.INVISIBLE);
        view.findViewById(R.id.closeFrame).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        ((TextView)view.findViewById(R.id.titleText)).setText(getContext().getString(R.string.action_directory));
        ((TextView)view.findViewById(R.id.closeTxt)).setText(getContext().getString(R.string.dialog_cancel));


        setupTabLayout(view);

        return view;

    }

    private int getSelectedTab(){

        return tabLayout.getSelectedTabPosition();
    }

    private void createFragments(){

        sectionPageAdapter=new SectionPageAdapter(getChildFragmentManager());


        switch (getCategoryType()){


                case MODEL:
                    sectionPageAdapter.AddFragment(  CategoryFragment.newInstance("model_Directory", CategoryFragment.ChooserType.MODEL_DIRECTORY.name(),0,this)
                            ,getString(R.string.directory));
                    sectionPageAdapter.AddFragment(  CategoryFragment.newInstance("model_custumer_Directory", CategoryFragment.ChooserType.MODEL_CUSTUMER.name(),1,this)
                            ,getString(R.string.model_custumer));

                    break;



                case MATERIAL:
                    sectionPageAdapter.AddFragment(  CategoryFragment.newInstance("material_type_Directory", CategoryFragment.ChooserType.MATERIAL_TYPE.name(),0,this)
                            ,getString(R.string.materialsTxt));
                    sectionPageAdapter.AddFragment(  CategoryFragment.newInstance("material_dealer_Directory", CategoryFragment.ChooserType.MATERIAL_DEALER.name(),1,this)
                            ,getString(R.string.dealership_dealerships));


                    break;



                case CUTNOTE:

                    sectionPageAdapter.AddFragment(  CategoryFragment.newInstance("cutnote_status_Directory", CategoryFragment.ChooserType.CUTNOTE_STATUS.name(),0,this)
                            ,getString(R.string.cutNotes_status));
                    sectionPageAdapter.AddFragment(  CategoryFragment.newInstance("cutnote_model_Directory", CategoryFragment.ChooserType.CUTNOTE_MODEL.name(),1,this)
                            ,getString(R.string.modelosText));
                    break;






            }





        }

        private void setupTabLayout(View view) {

            createFragments();




            viewPager = (ViewPager) view.findViewById(R.id.viewpager);
            viewPager.setAdapter(sectionPageAdapter);
            viewPager.setOffscreenPageLimit(3);

            tabLayout = (TabLayout) view.findViewById(R.id.tabs);
            tabLayout.setTabMode(TabLayout.MODE_FIXED);
            tabLayout.setupWithViewPager(viewPager);



            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {


                        getTab(tab.getPosition()).init();


                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {


                }
            });


            switch (getCategoryType()){



                case MODEL:
                     tabLayout.getTabAt(0).setIcon(R.drawable.ic_folder_selector2);
                     tabLayout.getTabAt(1).setIcon(R.drawable.ic_person_selector);

                    break;


                case MATERIAL:
                    tabLayout.getTabAt(0).setIcon(R.drawable.ic_leather_selector);
                    tabLayout.getTabAt(1).setIcon(R.drawable.ic_person_selector);

                    break;


                case CUTNOTE:
                    tabLayout.getTabAt(0).setIcon(R.drawable.ic_check_selector);
                    tabLayout.getTabAt(1).setIcon(R.drawable.ic_shoe_selector);
                    break;





            }


        }


    private CategoryBaseFragment getTab(int position){

            return  (CategoryBaseFragment) sectionPageAdapter.getItem(position);

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


        @Override
        public void onDestroy() {
            super.onDestroy();

        }



}