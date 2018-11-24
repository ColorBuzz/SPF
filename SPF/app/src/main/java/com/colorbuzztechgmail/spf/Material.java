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
        piecesId=new ArrayList<>();
    }

    private int modelId;
    private String name=null;
    private Drawable image=null;
    private String date=null;
    private String customMaterialId="0";
    private List<String> piecesId;
    private Color color;

    public Integer getPieceCount() {
        return piecesId.size();
    }

    public List<String> getPiecesId() {
        return piecesId;
    }

    public void setPiecesId(List<String> piecesId) {
        this.piecesId = piecesId;
    }

    public String getCustomMaterialId() {
        return customMaterialId;
    }

    public void setCustomMaterialId(String customMaterial) {
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
