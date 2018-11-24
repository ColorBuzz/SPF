package com.colorbuzztechgmail.spf.popup;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
 import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.colorbuzztechgmail.spf.CustomHeader;
import com.colorbuzztechgmail.spf.CustomMaterial;
import com.colorbuzztechgmail.spf.CustomMaterialDataset;
import com.colorbuzztechgmail.spf.CustomMaterialListAdapter;
import com.colorbuzztechgmail.spf.CustomSearchView;
import com.colorbuzztechgmail.spf.HeaderItemDecoration;
import com.colorbuzztechgmail.spf.ItemActionListener;
import com.colorbuzztechgmail.spf.Material;
import com.colorbuzztechgmail.spf.ModelDataBase;
import com.colorbuzztechgmail.spf.R;
import com.colorbuzztechgmail.spf.TitleGridItemDecoration;
import com.colorbuzztechgmail.spf.Utils;
import com.colorbuzztechgmail.spf.dataset.Dataset;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by PC01 on 18/06/2017.
 */
public class MaterialAsigmentoPopUp extends  DialogFragment implements SearchView.OnQueryTextListener,Toolbar.OnMenuItemClickListener,ItemActionListener {


    private static  final String MODEL_ID="modelid";
    private static  final String MATERIAL_ID="materialid";
    private static  final String MATERIAL_NAME="materialname";

     private String selectedCategory;
     RecyclerView mRecycler;
    ModelDataBase db;
    CustomMaterialDataset materialDataset;
    Toolbar toolbar;
    CustomMaterialListAdapter materialAdapter;
    Utils.onSavedInterface savedInterface;


    public void setSavedInterface(Utils.onSavedInterface onSavedInterface){

        this.savedInterface=onSavedInterface;
    }




    public static MaterialAsigmentoPopUp newInstance(int modelId, int materialId, String name) {
        MaterialAsigmentoPopUp f = new MaterialAsigmentoPopUp();
        Bundle args = new Bundle();
        args.putInt(MODEL_ID,modelId);
        args.putInt(MATERIAL_ID,materialId);
        args.putString(MATERIAL_NAME,name);



        f.setArguments(args);


        return f;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View dialogView = inflater.inflate(R.layout.assigment_popup, null);
        mRecycler = (RecyclerView) dialogView.findViewById(R.id.materialRecycler);

        materialAdapter = new CustomMaterialListAdapter(this,(AppCompatActivity) getActivity(),false);
        materialDataset=new CustomMaterialDataset(mRecycler, materialAdapter);
        materialAdapter.dataset(materialDataset);

        setupRecycler();
        db= new ModelDataBase(getContext());
        toolbar=new Toolbar(getContext());
        toolbar.setTitleTextColor(getResources().getColor(R.color.textColorPrimary));
        toolbar.setSubtitleTextColor(getResources().getColor(R.color.textColorPrimary));
        toolbar.setBackgroundColor(getResources().getColor( R.color.colorLightPrimary));
        ((LinearLayout)dialogView.findViewById( R.id.container)).removeAllViewsInLayout();
        ((LinearLayout)dialogView.findViewById( R.id.container)).addView(toolbar);

        loadMenu();
        return dialogView;

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NORMAL, R.style.MY_DIALOG);
         setSelectedCategory(getResources().getString(R.string.action_sortAll));



    }

    @Override
    public void onStart() {
        super.onStart();



        Dialog d = getDialog();

        if (d!=null) {
            int width =  ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            ;
            d.getWindow().setLayout(width, height);
            d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            setupRecycler();

            selectCategory(getSelectedCategory());
            customView();
         }


    }

    private void loadSearchBar(){


        final CustomSearchView customSearchView=new CustomSearchView(getContext());

        toolbar.getMenu().setGroupVisible(R.id.group_options,false);
        toolbar.setNavigationIcon(null);

        customSearchView.setOnQueryTextListener(this);
        customSearchView.setQueryHint("Buscar nombre, tipo, proveedor");

        ImageView img=new ImageView(getContext());
        img.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        img.setImageDrawable(getResources().getDrawable(R.drawable.ic_keyboard_arrow_left_black_24dp));


        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customSearchView.setQuery("",false);
                toolbar.removeView(customSearchView);
                toolbar.removeView(v);
                toolbar.setNavigationIcon(R.drawable.ic_arrow_back_selector);
                toolbar.getMenu().setGroupVisible(R.id.group_options,true);


                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                setSelectedCategory(getSelectedCategory());


            }
        });

        customSearchView.onActionViewExpanded();

        toolbar.addView(img);
        toolbar.addView(customSearchView);


    }


    private void loadMenu(){


        toolbar.inflateMenu(R.menu.menu_material_assigment);


        toolbar.setNavigationIcon(R.drawable.ic_close_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        toolbar.setOnMenuItemClickListener(this);






    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

       switch (item.getItemId()) {



           case R.id.action_search:

               loadSearchBar();

               return false;

           case R.id.action_sortName:
               item.setChecked(true);
               materialDataset.setSortType(CustomMaterialDataset.SortType.NAME);

               materialDataset.changeSortType( );

               return false;

           case R.id.action_sortDate:
               item.setChecked(true);
               materialDataset.setSortType(CustomMaterialDataset.SortType.LAST);
               materialDataset.changeSortType( );

               return false;

           case R.id.action_sortType:
               item.setChecked(true);
               materialDataset.setSortType(CustomMaterialDataset.SortType.TYPE);

               materialDataset.changeSortType( );

               return false;

           case R.id.action_sortDealer:
               item.setChecked(true);
               materialDataset.setSortType(CustomMaterialDataset.SortType.DEALER);

               materialDataset.changeSortType( );

               return false;
           case R.id.action_sortSeasons:
               item.setChecked(true);
               materialDataset.setSortType(CustomMaterialDataset.SortType.SEASONS);

               materialDataset.changeSortType( );

               return false;
       }

        return false;
    }

    public void refreshInfobar( ) {
        int count=0;



            count=materialDataset.getItemCount();
            String aux = String.valueOf(count);
            aux += " ";
            aux += getString(R.string.materialsTxt);

            toolbar.setTitle(getSelectedCategory());
            toolbar.setSubtitle(aux);

    }

    public String getSelectedCategory() {
        return selectedCategory;
    }

    public void setSelectedCategory(String selectedCategory) {
        this.selectedCategory = selectedCategory;
    }

    private void setupRecycler(){

        mRecycler.setHasFixedSize(true);
        materialAdapter.setViewMode(Utils.ViewMode.LIST);



        int nColumn=1;

        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), nColumn);
        TitleGridItemDecoration titleItemDecoration=new TitleGridItemDecoration(8,materialAdapter,nColumn,true);
        mRecycler.addItemDecoration( titleItemDecoration);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {

                return materialAdapter.isHeader(position) ? gridLayoutManager.getSpanCount() : 1;
            }
        });

        mRecycler.setLayoutManager(gridLayoutManager);

        HeaderItemDecoration headerItemDecoration=new HeaderItemDecoration(mRecycler,materialAdapter);
        mRecycler.addItemDecoration(headerItemDecoration);
        mRecycler.setAdapter(materialAdapter);


    }

    public void customView() {



    }

    public void selectCategory(String category) {

        setSelectedCategory(category);


        fillMateralDataset(  filterType());

        mRecycler.scrollToPosition(0);

        refreshInfobar();

    }

    private  List<Object> filterType( ) {


        final List<Object> filteredModelList = new ArrayList<>();



                filteredModelList.addAll(db.getAllCustomMaterial());


        return filteredModelList;
    }

    private void fillMateralDataset(List<Object>materials){

            materialDataset.removeAll();
            materialDataset.AutomaticTitleGeneratorSortType(materials,true);

        mRecycler.scrollToPosition(0);




    }

    @Override
    public void onPreview(Object obj) {
        updateMaterial((CustomMaterial)obj);

    }

    @Override
    public void toRemove(Object obj) {

    }

    @Override
    public void toEdit(long[] ids) {

    }

    @Override
    public void onClick(View v, int position, boolean isLongClick) {

    }


    private void updateMaterial(Material customMaterial){

     final   int modelId=getArguments().getInt(MODEL_ID);
     final   int materialId=getArguments().getInt(MATERIAL_ID);


         ModelDataBase db= new ModelDataBase(getContext());
        db.updateMaterial(modelId,materialId,null, customMaterial.getId());



        Material newMaterial= db.getMaterialById(modelId,materialId);
        newMaterial.setImage(customMaterial.getImage());



        if(savedInterface!=null){

            savedInterface.onSaved(newMaterial,0,true);

        }




        dismiss();

    }
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {





            final List<Object> filteredMaterialList = filterByParamater(query);
            if(filteredMaterialList.size()>0){

                materialDataset.removeAll();
                materialDataset.AutomaticTitleGeneratorSortType(filteredMaterialList,false);
                return true;


            }else{
                materialDataset.removeAll();

                materialDataset.add( new CustomHeader("No hay coincidencias ",0,getResources().getDrawable(R.drawable.ic_info_black_24dp)));
            }


        return false;
    }

    private   List<Object> filterByParamater(String query) {


        final List<Object> filteredList = new ArrayList<>();

        filteredList.addAll(db.getCustomMaterialByValue(query));

        return filteredList;
    }
}










