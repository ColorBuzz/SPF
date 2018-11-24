package com.colorbuzztechgmail.spf;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * Created by PC01 on 14/04/2018.
 */



public class SPFencoder {

    List<String> data=new ArrayList<>();
     Context context;

    public SPFencoder(Context context, Piece piece,boolean sizeList) {

         data=new ArrayList<>();

        this.context=context;
        List<String>materials=new ArrayList<>();
        List<Piece> pieceList=new ArrayList<>();

        if(sizeList){
           pieceList=getSizeList(piece);

            for(Piece auxpiece: pieceList ){
                materials.add(auxpiece.getMaterial());
            }

        }else{

            pieceList.add(piece);
            materials.add(piece.getMaterial());

        }


        HashSet    hs = new HashSet();
        hs.addAll(materials); // demoArrayList= name of arrayList from which u want to remove duplicates
        materials.clear();
        materials.addAll(hs);
        Collections.sort(materials);

        data.addAll(createHeader(materials,getPreviewModel(pieceList.get(0).getModelId()).getName(),pieceList.size()));
        data.addAll(createBody(pieceList));
        data.add("END_SPF"+'\r');

    }
    public SPFencoder(Context context,PreviewModelInfo previewModelInfo) {
        this.context=context;


    }
    public SPFencoder(Model model) {


    }
    public SPFencoder(Context context,int modelId) {
        this.context=context;

    }

    public List<String> getData() {
        return data;
    }

    private List<String> createHeader(List<String> materialList,String modelName,int pieceCount){

        List<String>header=new ArrayList<>();


        header.add("SPF_INESCOP"+'\r');
        header.add("VERSION 3.0"+'\r');
        header.add("MATERIALS " + String.valueOf(materialList.size())+'\r');

        for(String material:materialList){

            header.add("MATERIAL"+'\r');
            header.add("NAME " + material+'\r');
            header.add("COLOR N"+'\r');

        }

        header.add("MODELS 1"+'\r');
        header.add("MODEL"+'\r');
        header.add("NAME " + modelName+'\r');
        header.add("DESC NODESCRIPTION"+'\r');
        header.add("PIECES " + String.valueOf(pieceCount)+'\r');

        return header;
    }

    private List<String> createBody(List<Piece> pieceList){


         List<String> body=new ArrayList<>();


        for(Piece piece:pieceList){


            body.add("PIECE"+'\r');
            body.add("NAME " + piece.getName()+'\r');
            body.add("SIZE " + piece.getSize()+'\r');
            body.add("DEFQUALITY 1"+'\r');
            body.add("NESTMAXANGLE 180"+'\r');
            body.add("0,0"+'\r');
            body.add("0,0"+'\r');
            body.add("AMOUNT " + String.valueOf(piece.getAmount())+'\r');
            body.add("AMOUNT_MIRROR " + String.valueOf(piece.getAmount_mirror())+'\r');
            body.add("CUT 0 "+'\r');
            body.add("CUT_MIRROR 0 "+'\r');
            body.add("MATERIAL "+ piece.getMaterial()+'\r');
            body.add("COLOR N"+'\r');
            body.add("REFENTITIES 0"+'\r');

            body.addAll(piece.getRawData().subList(14,piece.getRawData().size()));

        }




       return body;
    }


   public PreviewModelInfo getPreviewModel(int modelId){
        ModelDataBase db=new ModelDataBase(getContext());

        return db.getPreviewModel(modelId);
    }

    private List<Piece> getSizeList(Piece piece){



        ModelDataBase db=new ModelDataBase(getContext());

        return db.getPizeSizeList(piece,false);
    }


    public Context getContext() {
        return context;
    }
}
