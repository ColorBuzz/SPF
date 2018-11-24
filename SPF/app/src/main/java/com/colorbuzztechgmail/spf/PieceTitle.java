package com.colorbuzztechgmail.spf;

import android.graphics.drawable.Drawable;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC01 on 29/04/2018.
 */

public class PieceTitle extends Piece {

    private int modelId;
    private String material=null;
    private int id;
    private boolean expanded=false;
    private List<Piece> bufferPiece;
    SparseArray<String> sparseId;
    private Drawable image;



    public PieceTitle(String name , int id, int modelId, Drawable image) {
        super();


        sparseId=new SparseArray<>();
        bufferPiece=new ArrayList<>();
        this.modelId=modelId;
        this.id=id;
         setMaterial(name);
         setImage(image);
    }


    public boolean isExpanded() {
        return expanded;
    }

    public List<Piece> getBufferPiece() {
        return bufferPiece;
    }

    public void addPiece(Piece piece){

        if(bufferPiece!=null){


            bufferPiece.add(piece);
            sparseId.append(piece.getId(),piece.getName());

        }

    }

    public void removePiece(Piece piece){




    }

    public void setBufferPiece(List<Piece> bufferPiece) {
        this.bufferPiece = bufferPiece;


        for(int j=0;j<bufferPiece.size();j++){

         sparseId.append(bufferPiece.get(j).getId(),bufferPiece.get(j).getName());


        }

    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    @Override
    public Drawable getImage() {
        return image;
    }

    @Override
    public void setImage(Drawable image) {
        this.image = image;
    }

    @Override
    public int getModelId() {
        return modelId;
    }

    @Override
    public void setModelId(int modelId) {
        this.modelId = modelId;
    }

    @Override
    public String getMaterial() {
        return material;
    }

    @Override
    public void setMaterial(String material) {
        this.material = material;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }


}
