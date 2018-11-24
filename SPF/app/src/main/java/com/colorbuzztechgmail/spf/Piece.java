package com.colorbuzztechgmail.spf;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC01 on 19/11/2017.
 */



public class Piece implements Cloneable {

    private int modelId;
    private int id;
    private String name=null;
    private String material;
    private String description="NO DESCRIPTION";
    private String size;

    private int amount;
    private int amount_mirror;
    private float amount_material;
    private Drawable image=null;

    private List<String> rawData;
    private List<String> ToolList;

    private float boxArea=0;


    public Piece() {
        super();
        rawData=new ArrayList<>();
        ToolList=new ArrayList<>();
    }

    public float getBoxArea() {
        return boxArea;
    }

    public void setBoxArea(float boxArea) {
        this.boxArea = boxArea;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAmount_mirror() {
        return amount_mirror;
    }

    public void setAmount_mirror(int amount_mirror) {
        this.amount_mirror = amount_mirror;
    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getRawData() {
        return rawData;
    }

    public void setRawData(List<String> rawData) {
        this.rawData .addAll( rawData);
    }

    public List<String> getToolList() {
        return ToolList;
    }

    public void setToolList(List<String> toolList) {
        ToolList = toolList;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getModelId() {
        return modelId;
    }

    public void setModelId(int modelId) {
        this.modelId = modelId;
    }

    public float getAmount_material() {
        return amount_material;
    }

    public void setAmount_material(float amount_material) {
        this.amount_material = amount_material;
    }

    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
