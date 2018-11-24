package com.colorbuzztechgmail.spf;

import android.graphics.drawable.Drawable;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC01 on 29/04/2018.
 */

public class MaterialTitle extends CustomMaterial {

    private String title;
    private int id;
    private boolean expanded=false;
    private List<CustomMaterial> materialBuffer;
    SparseArray<String> sparseId;
    private Drawable image;
    private int count;

    public List<CustomMaterial> getMaterialBuffer() {
        return materialBuffer;
    }

    public MaterialTitle(String title , int id, Drawable image) {
        super(title);


        this.title=title;
        sparseId=new SparseArray<>();
        materialBuffer=new ArrayList<>();
        this.id=id;
        setImage(image);
    }


    public boolean isExpanded() {
        return expanded;
    }


    public void addMaterial(CustomMaterial material){

        if(materialBuffer!=null){

           materialBuffer.add(material);
            sparseId.append(material.getId(),material.getName());

        }

    }

    public void removeModel(CustomMaterial material){

        if(materialBuffer!=null){

            materialBuffer.remove(sparseId.indexOfKey(material.getId()));
            sparseId.delete(material.getId());

        }


    }

    public void setMaterialBuffer(List<CustomMaterial> materialBuffer) {
        this.materialBuffer= materialBuffer;
         sparseId.clear();

        for(int j=0;j<materialBuffer.size();j++){

         sparseId.append(materialBuffer.get(j).getId(),materialBuffer.get(j).getName());


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
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
