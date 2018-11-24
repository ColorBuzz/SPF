package com.colorbuzztechgmail.spf;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by PC01 on 21/06/2017.
 */
public class TabFragment2 extends Fragment implements View.OnTouchListener{

    public final static String MODEL_ID="modelId";
    private static final String TAB="TabFragment1";
    private ImageView interImageView;
    private TextView data;
    private ArrayList<Integer> digitList;
    private ArrayList<ImageView> containerList;
    private RecyclerView mRecycler;
    private FrameLayout expandFrame,infoContainer;
    private Integer podomCount = 0;
    public ModelDataBase db;
    int modelId;
    MaterialAsigmentoPopUp poupMaterialAsigment;
    private FloatingActionButton addMaterial;


    public static TabFragment2 newInstance(@NonNull int modelId ) {
        TabFragment2 f = new TabFragment2();
        Bundle args = new Bundle();

        args.putInt(MODEL_ID,modelId);



        f.setArguments(args);


        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        db=new ModelDataBase(getContext());

        final View view=inflater.inflate(R.layout.tab_fragment1,container,false);

        view.setOnTouchListener(this);

         expandFrame=(FrameLayout) view.findViewById(R.id.frameExpand);
        infoContainer=(FrameLayout) view.findViewById(R.id.infoContainer);

        infoContainer.setOnTouchListener(this);

        loadMaterial();


        return view;

    }

    private static final Comparator<Material> ALPHABETICAL_COMPARATOR_MATERIAL = new Comparator<Material>() {
        @Override
        public int compare(Material a,  Material b) {
            return a.getName().toLowerCase().compareTo(b.getName().toLowerCase());
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onStart() {


        super.onStart();

    }


    public void loadMaterial(){

        modelId=((ModelActivity)getActivity()).model.getId();


        int screenWidth =getResources().getDisplayMetrics().widthPixels;

        int cardWidth = (int) getContext().getResources().getDimension(R.dimen.card_witdh);


        final List<Material> materialList = (db.getModelMaterial(modelId));



        for (int i = 0; i < materialList.size(); i++) {

            if(!(materialList.get(i).getCustomMaterialId()).equals("0")){

                final int cId=Integer.valueOf(materialList.get(i).getCustomMaterialId());

                materialList.get(i).setImage(db.getCustomMaterial(cId).getImage());
            }else{

                materialList.get(i).setImage(getContext().getDrawable(R.drawable.leather));


            }
        }
        mRecycler=new RecyclerView(getContext());
        mRecycler.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        MaterialinfoContainer materialinfoContainer = new MaterialinfoContainer(getContext(),MaterialinfoContainer.MODE_SUBMENU_MATERIAL);
        CardSliderLayoutManager cardSliderLayoutManager = new CardSliderLayoutManager((screenWidth / 2) - (cardWidth / 2), cardWidth, 12, 0, materialinfoContainer.getView());
        mRecycler.setLayoutManager(cardSliderLayoutManager);

    }


    public void showAddMaterialPopup(int mModelId) {




        final int modelId=mModelId;

        LayoutInflater li=LayoutInflater.from(getContext());

        final View promptsView = li.inflate(R.layout.editcategory_popup, null);
        final Button cancelBtn=(Button)promptsView.findViewById(R.id.cancel_btn);
        final Button saveBtn=(Button)promptsView.findViewById(R.id.save_btn);
        final TextView titleText=(TextView) promptsView.findViewById(R.id.titleEditTextView);



        final ImageView tilteImg=(ImageView)promptsView.findViewById(R.id.addImageView);

        ((LinearLayout)promptsView.findViewById(R.id.di)).removeAllViewsInLayout();

        final EditText nametxt=new EditText(getContext());
        nametxt.setHint(getContext().getResources().getString(R.string.material_Name));
        nametxt.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        ((LinearLayout)promptsView.findViewById(R.id.di)).addView(nametxt);



        titleText.setText(getContext().getResources().getString(R.string.addMaterial));
        tilteImg.setImageDrawable(getContext().getDrawable(R.drawable.ic_leather_black));

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getContext());
        alertDialogBuilder.setView(promptsView);


        final Dialog alertDialog = alertDialogBuilder.create();

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();




        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();

            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String materialName =nametxt.getText().toString();

                materialName=materialName.trim();
                if(!materialName.equals("")){

                    db.addMaterial(modelId,materialName,"0",null);


                }else{

                    Utils.toast(getContext(),"Añade un nombre");

                }


            }
        });





    }



    public   void loadPieceList(List<String> pieceList, int mode,int modelId,String material){


        PieceListPopUp pieceListPopUp = PieceListPopUp.newInstance(null,mode,modelId,material );
        pieceListPopUp.show( getActivity().getSupportFragmentManager(),"pieceListDialog");



    }


    public void updateDataChange(Object object) {




    }

    public  void showAddObjectDialog( ) {

        final PopupMenu popup = new PopupMenu(getContext(), addMaterial);
        MenuInflater inflater = popup.getMenuInflater();
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()){

                    case R.id.action_addMaterial:

                        showAddMaterialPopup(modelId);

                        return true;
                    case R.id.action_pieceList:
                        final List<String> piecesId=new ArrayList<String>();

                        piecesId.add(String.valueOf("1"));

                        loadPieceList(null,PieceListAdapter.MODE_SIZE,modelId,null);
                        return true;

                }
                return false;
            }
        });



                //menuClick=position;
                inflater.inflate(R.menu.submenu_adapter, popup.getMenu());
                popup.getMenu().getItem(0).setVisible(true);

                popup.show();




    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {




            ((WrapContentHeightViewPager) ((ModelActivity) getActivity()).sViewpager).setPaginEnabled(true);


        return false;
    }
}
