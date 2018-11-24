package com.colorbuzztechgmail.spf;

import android.graphics.Color;
import android.graphics.drawable.Drawable;

import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC01 on 12/01/2018.
 */

public class CustomMaterial extends Material implements SortedListAdapter.ViewModel{


    public CustomMaterial(String name) {
        super(name);
        modelIdRelation= new ArrayList<>();


    }


    private String dealership=null;
    private String seasons=null;
    private String type=null;
    private Float feets=0f;
    private Float meters=0f;
    private List<String>modelIdRelation;
    private String description="NO DESCRIPTION";

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDealership() {
        return dealership;
    }

    public void setDealership(String dealership) {
        this.dealership = dealership;
    }

    public String getSeasons() {
        return seasons;
    }

    public void setSeasons(String seasons) {
        this.seasons = seasons;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Float getFeets() {
        return feets;
    }

    public void setFeets(Float feets) {
        this.feets = feets;
    }

    public Float getMeters() {
        return meters;
    }

    public void setMeters(Float meters) {
        this.meters = meters;
    }

    public List<String> getModelIdRelation() {
        return modelIdRelation;
    }

    public void setModelIdRelation(List<String> modelIdRelation) {
        this.modelIdRelation = modelIdRelation;
    }
}
