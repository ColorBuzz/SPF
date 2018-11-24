package com.colorbuzztechgmail.spf;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

public class CutNote {

    private int id=0;
    private int reference=0;
    private  int noteNumber=0;
    private  String date;
    private int modelId;
    private String model;
    private Map<String,Integer> sizeList=new HashMap<>();
    private cutNoteStatus status=cutNoteStatus.INDETERMINATE;

   public enum cutNoteStatus{

        FINISHED,
        IN_PROGRESS,
        INDETERMINATE


    }


    public CutNote( int cutNoteNumber) {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReference() {
        return reference;
    }

    public void setReference(int reference) {
        this.reference = reference;
    }

    public int getNoteNumber() {
        return noteNumber;
    }

    public void setNoteNumber(int noteNumber) {
        this.noteNumber = noteNumber;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Map<String, Integer> getSizeList() {
        return sizeList;
    }

    public int getPairCount() {

        Object[] values=sizeList.values().toArray();
        int count=0;

        if(sizeList.values().size()>0){

            for (int i=0;i<values.length;i++){

                count=count+(int)values[i];

            }

        }

        return count;
    }

    public void addCount(String size, Integer count){



        if(sizeList.containsKey(size)){


            sizeList.remove(size);
            sizeList.put(size,count);

        }else{

            sizeList.put(size,count);

        }


    }

    public void deleteSize(String size){

        sizeList.remove(size);


    }

    public Integer getCountAtSize(String size){


        if(sizeList.containsKey(size)){


            return (int)sizeList.get(size);
        }


        return 0;
    }

    public int getModelId() {
        return modelId;
    }

    public void setModelId(int modelId) {
        this.modelId = modelId;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public cutNoteStatus getStatus() {
        return status;
    }

    public void setStatus(cutNoteStatus status) {
        this.status = status;
    }

    public boolean areContentsTheSame(CutNote newItem) {

        return this.equals(newItem);
    }

    public boolean areItemsTheSame(CutNote item) {

        return this.getId()==item.getId();
    }

}
