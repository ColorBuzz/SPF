package com.colorbuzztechgmail.spf;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CustomHeader {


    private String title;
    private int id;
    private boolean expanded=false;
    private List<Object> objectBuffer;
    SparseBooleanArray sparseId;
    private Drawable image;
    public boolean isEmpty=false;

    public List<Object> getBuffer() {
        return objectBuffer;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public void setEmpty(boolean empty) {
        isEmpty = empty;
    }

    public CustomHeader(String title , int id, Drawable image) {
        super();


        this.title=title;
        sparseId=new SparseBooleanArray();
       objectBuffer=new ArrayList<>();


        this.id=id;
        this.image=image;
    }

    public CustomHeader(String title , int id, Drawable image,  boolean isEmpty) {
        super();


        this.title=title;
        this.isEmpty=isEmpty;
        sparseId=new SparseBooleanArray();
        objectBuffer=new ArrayList<>();


        this.id=id;
        this.image=image;
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

    @BindingAdapter("android:textSize")
    public static void setTextSize(View view, Boolean value) {
        ((TextView)view).setTextSize(value ? 24 : 16);
    }


    @BindingAdapter("android:textAlignment")
    public static void setTextAlignment(View view, Boolean value) {
        ((TextView)view).setTextAlignment(value ? View.TEXT_ALIGNMENT_CENTER : View.TEXT_ALIGNMENT_TEXT_START);
    }

}
