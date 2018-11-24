package com.colorbuzztechgmail.spf;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by PC01 on 18/06/2017.
 */
public class PieceListPopUp extends  DialogFragment implements View.OnClickListener ,AdapterView.OnItemSelectedListener,DialogInterface.OnShowListener,Toolbar.OnMenuItemClickListener{


     private static final String PIECE_ID="pieces";
     private static final String MODE ="mode";
     private static final String MODEL_ID ="model";
    private static final String MATERIAL="material";

     public int position=0;
     private int modelID;

     public   Spinner mSpinner;

     private List<Piece> bufferPieces;
     public List< Category> titleValues;

    public int dialogMode=0;

    public static PieceListPopUp newInstance(@Nullable  String  pieceId, @NonNull int mode, @NonNull int modelId, String material) {
        PieceListPopUp f = new PieceListPopUp();
        Bundle args = new Bundle();
        args.putString (PIECE_ID,pieceId);
         args.putInt(MODE,mode);
         args.putInt(MODEL_ID,modelId);
        args.putString(MATERIAL,material);

        f.setArguments(args);


        return f;
    }



    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        dialogMode=getArguments().getInt(MODE);

        LayoutInflater li = LayoutInflater.from(getContext());

        final View promptsView = li.inflate(R.layout.assigment_popup, null);


        mSpinner  = (Spinner)promptsView.findViewById(R.id.spinner);

        final ArrayAdapter spinnerAdapter = new ArrayAdapter<String>(getContext(),
                R.layout.spinneritem1, new ArrayList<String>());
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        mSpinner .setOnItemSelectedListener(this);




         modelID=getArguments().getInt(MODEL_ID);
        //((FrameLayout)promptsView.findViewById(R.id.container)).addView(mSpinner);

        loadMenu(promptsView);
        LoadInfo(modelID,dialogMode,getArguments().getString(MATERIAL));


        return new AlertDialog.Builder(getActivity())

                .setView(promptsView)
                .create();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NORMAL, R.style.MY_DIALOG);


    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog d = getDialog();

        if (d!=null) {




            d .getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
       }

    }

    @Override
    public void onClick(View v) {


        switch (v.getId()){


        }

    }

    public void LoadInfo( int modelID, int mode,String material) {


        final ArrayAdapter spinnerAdapter = new ArrayAdapter<String>(getContext(),
                R.layout.spinneritem1, new ArrayList<String>());
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        titleValues=new ArrayList<>();

        final ModelDataBase db = new ModelDataBase(getContext());
         spinnerAdapter.add("Todos");
        final Category aux1 = new Category();
        aux1.setName("Todos");
        titleValues.add(aux1);

        if (mode == PieceListAdapter.MODE_SIZE) {
            position = 0;
            List<String> pieceId=db.getPreviewModel(modelID).getPiecesId();
            for (String id : pieceId ) {
                final Piece auxpiece = db.getPiece(modelID, Integer.valueOf(id), null, null,true);

                final Category aux = new Category();
                aux.setName(auxpiece.getName());
                aux.setId(auxpiece.getId());

                titleValues.add(aux);
                spinnerAdapter.add(aux.getName() + " (" + auxpiece.getMaterial() + ") ");

                if(getArguments().getString(PIECE_ID)!=null){

                   if(getArguments().getString(PIECE_ID).equals(String.valueOf(auxpiece.getId()))){

                     position = titleValues.size()-1;

                   }

                }
            }
        }else if (mode == PieceListAdapter.MODE_ALL_PIECE) {
            position = 0;
            List<String> pieceId = db.getPreviewModel(modelID).getPiecesId();
            for (String id : pieceId) {
                final Piece auxpiece = db.getPiece(modelID, Integer.valueOf(id), null, null,true);

                final Category aux = new Category();
                aux.setName(auxpiece.getName());
                aux.setId(auxpiece.getId());

                titleValues.add(aux);
                spinnerAdapter.add(aux.getName() + " (" + auxpiece.getMaterial() + ") ");

            }

        }else if (mode == PieceListAdapter.MODE_MATERIAL) {

            final List<Material> materialList = db.getModelMaterial(modelID);
            position=0;
            for (Material materialModel : materialList) {

                final Category aux = new Category();
                aux.setName(materialModel.getName());
                aux.setId(materialModel.getId());

                titleValues.add(aux);
                spinnerAdapter.add(aux.getName() + "(" + materialModel.getPieceCount() + ")");

                if(material!=null){
                    if (material.equals(aux.getName())) {

                        position = titleValues.size()-1 ;

                    }
                }

            }

        }else if (mode == PieceListAdapter.MODE_ALL_MATERIAL) {

            final List<Material> materialList = db.getModelMaterial(modelID);
            position=0;
            for (Material materialModel : materialList) {

                final Category aux = new Category();
                aux.setName(materialModel.getName());
                aux.setId(materialModel.getId());

                titleValues.add(aux);
                spinnerAdapter.add(aux.getName() + "(" + materialModel.getPieceCount() + ")");



            }

        }

        mSpinner.setAdapter(spinnerAdapter);
       // updateView(getPieceList(position,dialogMode),dialogMode);
        mSpinner.setSelection(position);

    }

    private List<Piece> getPieceList( int position, int mode){

        List<Piece> pieceList=new ArrayList<>();

        final ModelDataBase db= new ModelDataBase(getContext());

      if(position>0){


        if(mode==PieceListAdapter.MODE_SIZE) {

         final Piece piece=db.getPiece(modelID,   titleValues.get(position).getId() , null, null,true);

         pieceList = db.getPizeSizeList(piece,true);

        } else if(mode==PieceListAdapter.MODE_MATERIAL){


            List<String> piecesId= db.getMaterialByName(modelID,titleValues.get(position).getName()).getPiecesId();
            for(String id:piecesId){

                final Piece auxpiece=db.getPiece(modelID,Integer.valueOf(id),null,null,true);

                pieceList.add(auxpiece);


            }

        }

      }else{

            List<String> piecesId= db.getPreviewModel(modelID).getPiecesId();

            for (String id : piecesId) {

                final Piece auxpiece = db.getPiece(modelID, Integer.valueOf(id), null, null,true);

                    pieceList.add(auxpiece);


            }
      }

      return pieceList;

    }

    private void updateView(List<Piece> pieces,int mode){

        ((FrameLayout) getDialog().findViewById(R.id.di)).removeAllViews();

        if(pieces.size()>0) {
            final RecyclerView mrecycler = new RecyclerView(getContext());

            PieceListAdapter pieceListAdapter = new PieceListAdapter(getContext(), pieces, mode,  getActivity(), this);

/*
            final GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, LinearLayoutManager.VERTICAL, false);
            RecyclerView.ItemDecoration itemDecoration = new GridSpacingItemDecoration(2, 1, true);
            mrecycler.addItemDecoration(itemDecoration);*/
            mrecycler.setHasFixedSize(true);
            mrecycler.setLayoutManager(new LinearLayoutManager(getContext()));

            mrecycler.setAdapter(pieceListAdapter);
            mrecycler.setPadding(0, 0, 0, 8);

            ((FrameLayout) getDialog().findViewById(R.id.di)).addView(mrecycler);
        }else{
            LayoutInflater li = LayoutInflater.from(getContext());

            final View alertView = li.inflate(R.layout.assigment_material_button, null);

            ((TextView)alertView).setText(getString(R.string.warning_emptyMaterial));

            ((FrameLayout) getDialog().findViewById(R.id.di)).addView(alertView);
        }


    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

          //TOdos
        if(position==0){
            if(dialogMode==PieceListAdapter.MODE_SIZE){
                updateView(getPieceList(position,PieceListAdapter.MODE_ALL_PIECE),PieceListAdapter.MODE_ALL_PIECE);
            } else if(dialogMode==PieceListAdapter.MODE_MATERIAL){
                updateView(getPieceList(position,PieceListAdapter.MODE_ALL_PIECE),PieceListAdapter.MODE_ALL_MATERIAL);
            }



        } else {
            updateView(getPieceList(position,dialogMode),dialogMode);


        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onShow(DialogInterface dialog) {

    }

    private void loadMenu(View v){

        Toolbar toolbar=(Toolbar)v.findViewById(R.id.pieceToolbar);

       // toolbar.inflateMenu(R.menu.menu_material_assigment);


        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_selector);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        toolbar.setOnMenuItemClickListener(this);

        Menu menu=toolbar.getMenu();










    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId()) {


        }

        return false;
    }

}











