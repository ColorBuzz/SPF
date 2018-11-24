package com.colorbuzztechgmail.spf;

import android.graphics.drawable.Drawable;

import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;

import java.util.List;

/**
 * Created by PC01 on 30/12/2017.
 */

public class PreviewModelInfo  implements SortedListAdapter.ViewModel{

    private long id;
    private String name;


    private int pieceCount=0;
    private String description ;
    private String parent;
    private Drawable image=null;
    private List<String> SizeList;
    private String date;
    private String directory;
    private String custumer;

    private int materialCount=0;
    private String minSize="1000";
    private String maxSize="0";
    private boolean middleSize;
    private boolean quarterSize;
    private boolean threeQuarterSize;
    private String baseSize;



    public String getBaseSize() {
        return baseSize;
    }

    public void setBaseSize(String baseSize) {
        this.baseSize = baseSize;
    }



    public int getMaterialCount() {
        return materialCount;
    }

    public void setMaterialCount(int materialCount) {
        this.materialCount = materialCount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {

        this.name=name;
    }

    public int getPieceCount() {
        return pieceCount;
    }

    public void setPieceCount(int pieceCount) {
        this.pieceCount = pieceCount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public Drawable getBmp() {
        return image;
    }

    public void setBmp(Drawable image) {
        this.image = image;
    }

    public List<String> getSizeList() {
        return SizeList;
    }

    public void setSizeList(List<String> size) {
        SizeList = size;


        for(int i=0;i<size.size();i++){

            if(Float.valueOf(size.get(i))>Float.valueOf(maxSize)){
                maxSize=size.get(i);

            }
            if(Float.valueOf(size.get(i))<Float.valueOf(minSize)){
                minSize=size.get(i);

            }


            if(size.get(i).contains(".25")){


                quarterSize=true;

            }

            if(size.get(i).contains(".5") ||size.get(i).contains(".50")){


                middleSize=true;

            }
            if(size.get(i).contains(".75")){


                threeQuarterSize=true;

            }

        }


    }

    public boolean areContentsTheSame(PreviewModelInfo newItem) {

        return this.equals(newItem);
    }

    public boolean areItemsTheSame(PreviewModelInfo item) {

        return this.getId()==item.getId();
    }

    public String getCustumer() {
        return custumer;
    }

    public void setCustumer(String custumer) {
        this.custumer = custumer;
    }

    public String getMinSize() {
        return minSize;
    }

    public String getMaxSize() {
        return maxSize;
    }

    public boolean isMiddleSize() {
        return middleSize;
    }

    public boolean isQuarterSize() {
        return quarterSize;
    }

    public boolean isThreeQuarterSize() {
        return threeQuarterSize;
    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }
}
