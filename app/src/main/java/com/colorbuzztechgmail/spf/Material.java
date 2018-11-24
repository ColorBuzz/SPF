package com.colorbuzztechgmail.spf;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by PC01 on 12/01/2018.
 */

public class Material implements SortedListAdapter.ViewModel{

    private int Id;

    public Material(String name) {
        super();

        this.name=name;

    }

    private int modelId;
    private String name=null;
    private Drawable image=null;
    private String date=null;
    private long customMaterialId=-1;
    private int pieceCount=0;
    private Color color;

    public Integer getPieceCount() {
        return pieceCount;
    }

    public void setPieceCount(int pieceCount) {
        this.pieceCount = pieceCount;
    }

    public long getCustomMaterialId() {
        return customMaterialId;
    }

    public void setCustomMaterialId(long customMaterial) {
        this.customMaterialId = customMaterial;
    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

    public int getModelId() {
        return modelId;
    }

    public void setModelId(int modelId) {
        this.modelId = modelId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean areContentsTheSame(Material newItem) {

        return this.equals(newItem);
    }

    public boolean areItemsTheSame(Material item) {

        return this.getId()==item.getId();
    }


}
