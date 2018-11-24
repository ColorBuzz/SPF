package com.colorbuzztechgmail.spf;

import android.graphics.drawable.Drawable;
import android.util.SparseArray;
import android.util.SparseBooleanArray;

import java.util.ArrayList;
import java.util.List;

public class CustomHeader {


    private String title;
    private int id;
    private boolean expanded=false;
    private List<Object> objectBuffer;
    SparseBooleanArray sparseId;
    private Drawable image;

    public List<Object> getBuffer() {
        return objectBuffer;
    }

    public CustomHeader(String title , int id, Drawable image) {
        super();


        this.title=title;
        sparseId=new SparseBooleanArray();
       objectBuffer=new ArrayList<>();


        this.id=id;


        setImage(image);
    }


    public boolean isExpanded() {
        return expanded;
    }



    public void addObject(Object obj){

        if(objectBuffer!=null){

            objectBuffer.add(obj);
            sparseId.append(obj.hashCode(),false);

        }

    }

    public void removeObject(Object obj){

        if(objectBuffer!=null){

            objectBuffer.remove(sparseId.indexOfKey(obj.hashCode()));
            sparseId.delete(obj.hashCode());

        }


    }

    public void removeAll(){

       objectBuffer.clear();
       sparseId.clear();

    }

    public void setBuffer(List<Object> objBuffer) {
        this.objectBuffer =objBuffer;
        sparseId.clear();

        for(int j=0;j<objBuffer.size();j++){

            sparseId.append(objBuffer.get(j).hashCode(),false);


        }

    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public Drawable getImage() {
        return image;
    }


    public void setImage(Drawable image) {
        this.image = image;
    }


    public int getId() {
        return id;
    }

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
