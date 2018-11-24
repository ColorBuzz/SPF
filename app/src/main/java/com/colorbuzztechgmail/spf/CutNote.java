package com.colorbuzztechgmail.spf;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CutNote {

    private long id=0;
    private long reference=0;
    private  int noteNumber=0;
    private  String date;
    private String model;
    private Map<String,Integer> sizeList=new HashMap<>();
    private List<String> sizeValues;
    private cutNoteStatus status=cutNoteStatus.INDETERMINATE;
    private String worker="No Asignado";

   public enum cutNoteStatus{

        FINISHED,
        IN_PROGRESS,
        INDETERMINATE


    }


    public CutNote( int cutNoteNumber) {
        super();
        this.noteNumber=cutNoteNumber;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getReference() {
        return reference;
    }

    public void setReference(long reference) {
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

    public void setSizeList(Map<String, Integer> sizeList) {
        this.sizeList = sizeList;
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

    public void addSize(String size){



        if(!sizeList.containsKey(size)){


             sizeList.put(size,0);

        }


    }

    public void addSizeList(List<String> sizes){



      for(String size:sizes){
        if(!sizeList.containsKey(size)){


            sizeList.put(size,0);

        }
      }

    }

    public void addCount(String size, Integer count){


       if(size!=null){



        if(sizeList.containsKey(size)){


            sizeList.remove(size);
            sizeList.put(size,count);

        }else{

            sizeList.put(size,count);

        }

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

    public String getWorker() {
        return worker;
    }

    public void setWorker(String worker) {
        this.worker = worker;
    }

    public List<String> getSizeValues() {

       if(!sizeList.isEmpty()){

           sizeValues=new ArrayList<>();

           for (String size : sizeList.keySet()){

               sizeValues.add(size);

           }

           Collections.sort(sizeValues);
           return sizeValues;



       }
       return null;
       }
}
