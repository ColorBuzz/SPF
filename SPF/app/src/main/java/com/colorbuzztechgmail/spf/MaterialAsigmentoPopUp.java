package com.colorbuzztechgmail.spf;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
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
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Created by PC01 on 18/06/2017.
 */
public class MaterialAsigmentoPopUp extends  DialogFragment implements SearchView.OnQueryTextListener,Toolbar.OnMenuItemClickListener,ItemActionListener {


    private static  final String MODEL_ID="modelid";
    private static  final String MATERIAL_ID="materialid";
    private static  final String MATERIAL_NAME="materialname";
    private static  final String MATERIAL_PIECE_COUNT="pieceCount";

    CustomMaterialFragment.FilterMode filterMode= CustomMaterialFragment.FilterMode.ALL;
    private String selectedCategory;
     RecyclerView mRecycler;
    public List<Object> bufferMaterial = new ArrayList<>();
    ModelDataBase db;
    CustomMaterialDataset materialDataset;
    Toolbar toolbar;
    CustomMaterialListAdapter materialAdapter;
    Utils.onSavedInterface savedInterface;

    public CustomMaterialFragment.FilterMode getFilterMode() {
        return filterMode;
    }

    public void setSavedInterface(Utils.onSavedInterface onSavedInterface){

        this.savedInterface=onSavedInterface;
    }

    public void setFilterMode(CustomMaterialFragment.FilterMode filterMode) {
        this.filterMode = filterMode;
    }



    public static MaterialAsigmentoPopUp newInstance(int modelId, int materialId, String name, ArrayList<String> piecesId ) {
        MaterialAsigmentoPopUp f = new MaterialAsigmentoPopUp();
        Bundle args = new Bundle();
        args.putInt(MODEL_ID,modelId);
        args.putInt(MATERIAL_ID,materialId);
        args.putString(MATERIAL_NAME,name);
        args.putStringArrayList(MATERIAL_PIECE_COUNT,piecesId);



        f.setArguments(args);


        return f;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View dialogView = inflater.inflate(R.layout.assigment_popup, null);
        mRecycler = (RecyclerView) dialogView.findViewById(R.id.materialRecycler);

        materialAdapter = new CustomMaterialListAdapter(this,(AppCompatActivity) getActivity(),false);
        materialDataset=new CustomMaterialDataset(mRecycler,materialAdapter);
        materialAdapter.dataset(materialDataset);

        setupRecycler();
        db= new ModelDataBase(getContext());
        toolbar=(Toolbar)dialogView.findViewById(R.id.pieceToolbar);

        loadMenu(toolbar);
        return dialogView;

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setStyle(DialogFragment.STYLE_NORMAL, R.style.MY_DIALOG);
        setFilterMode(CustomMaterialFragment.FilterMode.ALL);
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

        toolbar.getMenu().findItem(R.id.action_search).setVisible(false);
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
                toolbar.getMenu().findItem(R.id.action_search).setVisible(true);


                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);


            }
        });

        customSearchView.onActionViewExpanded();

        toolbar.addView(img);
        toolbar.addView(customSearchView);


    }


    private void loadMenu(View v){

        toolbar.removeAllViews();

        toolbar.inflateMenu(R.menu.menu_material_assigment);


        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_selector);
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


           case R.id.action_filter:

               showFilterChooser(CustomMaterialFragment.FilterMode.DEALER);

               return false;

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

        bufferMaterial.clear();


        bufferMaterial = filterType( category, getFilterMode());

        fillMateralDataset(bufferMaterial);

        mRecycler.scrollToPosition(0);

        refreshInfobar();

    }

    private  List<Object> filterType( String query, CustomMaterialFragment.FilterMode filterMode) {


        final ModelDataBase db=new ModelDataBase(getContext());

        final List<Object> filteredModelList = new ArrayList<>();


        switch (filterMode) {
            case TYPE:// tipo

                filteredModelList.addAll(db.getCustomMaterialByType(query));

                break;

            case DEALER:
                filteredModelList.addAll(db.getCustomMaterialByDealer(query));

                break;

            case ALL:

                filteredModelList.addAll(db.getCustomMaterilas());
                break;
        }

        return filteredModelList;
    }

    private void fillMateralDataset(List<Object>materials){

            materialDataset.removeAll();
            materialDataset.AutomaticTitleGeneratorSortType(materials,true);

        mRecycler.scrollToPosition(0);




    }

    @Override
    public void onPreview(Object obj) {

    }

    @Override
    public void toRemove(Object obj) {

    }

    @Override
    public void toEdit(int position) {

    }

    @Override
    public void onClick(View v, int position, boolean isLongClick) {
        updateMaterial(materialDataset.getCustomMaterial(position));

    }

    public void showFilterChooser(final CustomMaterialFragment.FilterMode filterMode){

        setFilterMode(filterMode);

        final ModelDataBase db=new ModelDataBase(getContext());

        List<String> values= new ArrayList<>();

        switch (getFilterMode()){


            case DEALER:

                if(db.getCustomMaterialCount()>0){

                   values=db.getCustomMaterialsDealersNames();



                }

                break;


        }




        Collections.sort(values);


        LayoutInflater li=LayoutInflater.from(getContext());

        View v=li.inflate(R.layout.save_piece_popup,null);

        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());

        builder.setView(v);


        final Dialog d=builder.create();

        ((Toolbar)v.findViewById(R.id.pieceToolbar)).setTitle(getResources().getString(R.string.action_sortDealer) + " (" + String.valueOf(values.size()) + ")");
        ((Toolbar)v.findViewById(R.id.pieceToolbar)).setNavigationIcon(getResources().getDrawable(R.drawable.ic_keyboard_arrow_left_white_24dp));

        ((Toolbar)v.findViewById(R.id.pieceToolbar)).setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d.dismiss();
            }
        });
        final ArrayList<ListPopUpMenuItem> items =new ArrayList<>();

        items.add(new ListPopUpMenuItem(getResources().getString(R.string.action_sortAll),R.drawable.ic_supervisor_account_accent_24dp));

        for(String dealer: values){


            items.add(new ListPopUpMenuItem(dealer,R.drawable.ic_person_black_24dp));

        }

        final  ListPopupWindowAdapter adapter = new ListPopupWindowAdapter(getContext(),items){
            public View getView(final int position, View convertView, ViewGroup parent) {
                //Use super class to create the View
                View v = super.getView(position, convertView, parent);
                TextView tv = (TextView)v.findViewById(R.id.text);

                //Put the image on the TextView
                // tv.setCompoundDrawablesWithIntrinsicBounds(items.get(position).getImageDrawable(), null, null, null);
                int dp8=(int) (8 * getResources().getDisplayMetrics().density + 0.5f);
                //Add margin between image and text (support various screen densities)
                int dp5 = (int) (5 * getResources().getDisplayMetrics().density + 0.5f);
                tv.setCompoundDrawablePadding(dp5);
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String text=((TextView)v.findViewById(R.id.text)).getText().toString();

                        if(text.equals(getResources().getString(R.string.action_sortAll))){

                            setFilterMode(CustomMaterialFragment.FilterMode.ALL);
                        }
                        // Utils.toast(getContext(),category);
                        d.dismiss();
                        selectCategory(text);

                    }
                });
                return v;
            }
        };


        d.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                ((ListView)d.getWindow().findViewById((R.id.lisview))).setAdapter(adapter);


                d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
        });
        d.show();
    }

    private void updateMaterial(Material customMaterial){

     final   int modelId=getArguments().getInt(MODEL_ID);
     final   int materialId=getArguments().getInt(MATERIAL_ID);
     final   ArrayList<String> materialPieceCount=getArguments().getStringArrayList(MATERIAL_PIECE_COUNT);
     final   String materialName=getArguments().getString(MATERIAL_NAME);


         ModelDataBase db= new ModelDataBase(getContext());
        db.updateMaterial(modelId,materialId,null,String.valueOf(customMaterial.getId()),null);



        Material newMaterial= new Material(materialName);

        newMaterial.setModelId(modelId);
        newMaterial.setId(materialId);
        newMaterial.setCustomMaterialId(String.valueOf(customMaterial.getId()));
        newMaterial.setImage(customMaterial.getImage());
        newMaterial.setPiecesId(materialPieceCount);



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

        if(bufferMaterial.size()>0) {

            final List<Object> filteredMaterialList = filterByParamater(bufferMaterial, query);
            if(filteredMaterialList.size()>0){

                materialDataset.removeAll();
                materialDataset.AutomaticTitleGeneratorSortType(filteredMaterialList,false);

            }else{
                materialDataset.removeAll();

                materialDataset.add( new CustomHeader("No hay coincidencias ",0,getResources().getDrawable(R.drawable.ic_info_black_24dp)));
            }


        }
        return false;
    }

    private static List<Object> filterByParamater(List<Object> filelist, String query) {
        final String lowerCaseQuery = query.toLowerCase();

        final List<Object> filteredList = new ArrayList<>();
        for (Object obj : filelist) {

            final CustomMaterial material=(CustomMaterial)obj;

            final String text1 = material.getName().toLowerCase();
            final String text2 = material.getType().toLowerCase();
            final String text3 = material.getDealership().toLowerCase();
            final String text4 = material.getSeasons().toLowerCase();

            if(text1.contains(lowerCaseQuery)|| text2.contains(lowerCaseQuery)
                    || text3.contains(lowerCaseQuery) || text4.contains(lowerCaseQuery)){

                filteredList.add(material);


            }




        }
        return filteredList;
    }
}










