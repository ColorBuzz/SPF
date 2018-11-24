package com.colorbuzztechgmail.spf;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.colorbuzztechgmail.spf.popup.waitPoup;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by PC01 on 18/06/2017.
 */
public class PieceMaterialAsigmentPopUp extends  DialogFragment{


    private static  final String MODEL_ID="modelid";
    public static  final String UPDATED="update";
    waitPoup waitPoup;

    RecyclerView mRecycler;
    ModelDataBase db;

    public static PieceMaterialAsigmentPopUp newInstance(int modelId ) {
        PieceMaterialAsigmentPopUp f = new PieceMaterialAsigmentPopUp();
        Bundle args = new Bundle();
        args.putInt(MODEL_ID,modelId);



        f.setArguments(args);


        return f;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View dialogView = inflater.inflate(R.layout.assigment_popup, null);
        mRecycler = (RecyclerView) dialogView.findViewById(R.id.materialRecycler);

         mRecycler.setHasFixedSize(true);
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));


        db= new ModelDataBase(getContext());



        loadInfo(dialogView);
        return dialogView;

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
            int width =  ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;

            d.getWindow().setLayout(width, height);
            d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        }


    }



    private void loadInfo(View v){

        ModelDataBase db=new ModelDataBase(getContext());


        List<Piece>pieceTable=new ArrayList<>();
        List<Piece>pieces=new ArrayList<>();
        List<Material>materials=new ArrayList<>();


        int modelId=getArguments().getInt(MODEL_ID);

        materials=db.getModelMaterial(modelId);

        pieces=db.getHighLightPieces(modelId,true);
        final TextView titletxt=(TextView)v.findViewById(R.id.titleText);
        titletxt.setText(String.valueOf(materials.size()) +" "  + getString(R.string.materialsTxt));

        for(Material material:materials) {

            Drawable img=db.getCustomMaterialImage( material.getCustomMaterialId());

            final PieceTitle mTitle=new PieceTitle(material.getName(), material.getId(),modelId,img);

             Drawable drawable;
            if(mTitle.getImage() !=null) {

                  drawable = (new ShapeGenerator(getContext()).getDrawableShape(mTitle.getImage(),
                        ShapeGenerator.MODE_CIRCLE, android.R.color.transparent, ShapeGenerator.SIZE_SMALL));
            }else{

                  drawable = (new ShapeGenerator(getContext()).getDrawableShape(getContext().getDrawable(R.drawable.leather_white),
                        ShapeGenerator.MODE_CIRCLE, android.R.color.transparent, ShapeGenerator.SIZE_SMALL));


            }
            mTitle.setImage(drawable);

            pieceTable.add(mTitle);

                for(int i=pieces.size()-1;i>=0;i--){

                    if (pieces.get(i).getMaterial().equals(material.getName())) {
                        pieceTable.add(pieces.get(i));

                        pieces.remove(i);

                    }

                }

            }

        RecyclerListAdapter adapter = new RecyclerListAdapter(getContext(),pieceTable);
        mRecycler.addItemDecoration(new HeaderItemDecoration(mRecycler,adapter));
        mRecycler.setAdapter(adapter);



        ItemTouchHelper.Callback callback =
                new SimpleItemTouchHelperCallback(adapter,LinearLayout.VERTICAL);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(mRecycler);

        v.findViewById(R.id.closeFrame).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle args = new Bundle();
                args.putBoolean(UPDATED,false);
                setArguments(args);
                dismiss();
            }
        });


        v.findViewById(R.id.saveFrame).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle args = new Bundle();
                args.putBoolean(UPDATED,true);
                setArguments(args);
                new  updateDatabase().execute(new Object());

            }
        });


    }



    private void updatePieces(){

        final ModelDataBase db=new ModelDataBase(getContext());

        RecyclerListAdapter adapter=((RecyclerListAdapter)mRecycler.getAdapter());


       final SparseArray<String> sparseArray=adapter.sparseArrayundo;

       for(int i=sparseArray.size()-1;i>=0;i--){

         for(int j=adapter.pieceList.size()-1;j>0;j--){

            if(adapter.pieceList.get(j) instanceof Piece){

               if(sparseArray.keyAt(i)==adapter.pieceList.get(j).getId()){

                   if(!sparseArray.valueAt(i).equals(adapter.pieceList.get(j).getMaterial())){

                       final Piece oldPiece=db.getPiece(adapter.pieceList.get(j).getModelId(),adapter.pieceList.get(j).getId(),null,null,false);

                       db.updateAllSamePieceData(oldPiece,adapter.pieceList.get(j));

                       Log.d("Material Piece Update", oldPiece.getName() +"_"+ oldPiece.getMaterial() + " UPDATED TO " + oldPiece.getName() +"_"+adapter.pieceList.get(j).getMaterial());

                       sparseArray.removeAt(i);

                       adapter.pieceList.remove(j);

                       break;
                   }


               }

            }
         }


       }




    }

    public void showSaveButton( ){

    }


    private class updateDatabase extends AsyncTask<Object, Float,Boolean> {

        protected void onPreExecute() {



            waitPoup = com.colorbuzztechgmail.spf.popup.waitPoup.newInstance(getString(R.string.dialog_updateData));

            waitPoup.show(getActivity().getSupportFragmentManager(), "waitPopup");


        }

        protected Boolean doInBackground(Object[] ids) {


            //pos 0 -modelID



            try {


                updatePieces();


                return true;

            } catch (Exception e) {

                e.printStackTrace();
                Log.e("Background", e.toString());


            }
            return false;
        }

        protected void onProgressUpdate(Float... valores) {
            int p = Math.round(valores[0]);
            // ma.mProgressBar.setProgress(p);
        }

        @Override
        protected void onPostExecute(Boolean state) {

            if(waitPoup!=null){
                waitPoup.dismiss();

            }


            if(state){

                getDialog().dismiss();
                ((Piece_Activity)getActivity()).loadData();

            }else {

                Utils.toast(getContext(),getContext().getString(R.string.dialog_saved_unsucessfully));
            }


        }
    }


}










