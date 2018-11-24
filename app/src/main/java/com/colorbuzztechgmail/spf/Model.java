package com.colorbuzztechgmail.spf;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC01 on 07/12/2017.
 */

public class Model  implements SortedListAdapter.ViewModel{


    private int id;
    private String name="";


    private int pieceCount=0;
    private String description="NO DESCRIPTION" ;
    private String parent="";
    private Drawable image=null;
    private List<String> SizeList;
    private String minSize="1000";
    private String maxSize="0";
    private boolean middleSize;
    private boolean quarterSize;
    private boolean threeQuarterSize;
    private String custumer;

    public String getCustumer() {
        return custumer;
    }

    public void setCustumer(String custumer) {
        this.custumer = custumer;
    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

    private List<MaterialGroup> Materials;
     private String date;
     private String directory= "No Asignado";
    private String baseSize;
    private List<String> piecesId=null;

    public Model() {
        super();
        Materials=new ArrayList<>();
        SizeList=new ArrayList<>();
        piecesId=new ArrayList<>();


    }

    public String getBaseSize() {
        return baseSize;
    }

    public void setBaseSize(String baseSize) {
        this.baseSize = baseSize;
    }

    public List<String> getPiecesId() {
        return piecesId;
    }

    public void setPiecesId(List<String> piecesId) {
        this.piecesId = piecesId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<MaterialGroup> getMaterials() {
        return Materials;
    }

    public void setMaterials(List<MaterialGroup> materials) {
        Materials = materials;

    }

    public void setMaterials( MaterialGroup materials) {
        Materials.add(materials);


    }

    public int getMaterialCount() {
        return Materials.size();
    }



   public int getPieceCount() {

        if (pieceCount==0) {
            for (int i = 0; i < Materials.size(); i++) {
                pieceCount = pieceCount + Materials.get(i).getTotalPieceCount();


            }
        }

        return pieceCount;
    }

    public void setPieceCount(int pieceCount) {
        this.pieceCount = pieceCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getSize() {
        return SizeList;
    }

    public void setSizeList(List<String> size) {
        this.SizeList.addAll(size);

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

    public boolean areContentsTheSame(Model newItem) {

        return this.equals(newItem);
    }

    public boolean areItemsTheSame(Model item) {

        return this.getId()==item.getId();
    }

    public Drawable getBmp() {
        return image;
    }

    public void setBmp(Drawable bmp) {


          this.image=bmp;
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


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;


    }
    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String category) {
        this.directory = category;
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
}
