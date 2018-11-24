package com.colorbuzztechgmail.spf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CutNoteList  extends CutNote {


    private List<CutNote> cutNoteList ;
    private int count=0;
    private  int pairCount=0;
    private  int maxPairCount=0;

    private long modelId;

    public long getModelId() {
        return modelId;
    }

    public void setModelId(long modelId) {
        this.modelId = modelId;
    }

    public CutNoteList(int cutNoteNumber) {
        super(cutNoteNumber);

        cutNoteList= new ArrayList<>();
        setStatus(cutNoteStatus.INDETERMINATE);

    }

    public void addCutNote(List<CutNote> cutNotes){

        cutNoteList.addAll(cutNotes);



    }

    public void addCutNote(CutNote cutNote){

        cutNoteList.add(cutNote);


    }

    public void setCutNoteList(List<CutNote> cutNotes){

        cutNoteList=cutNotes;



    }

    public void removeNote(CutNote cutNote){

       for(int i=getCutNoteList().size()-1;i>=0;i--){

           if(cutNoteList.get(i).getNoteNumber()==cutNote.getNoteNumber()){
               cutNoteList.remove(i);
               break;
           }

        }

    }

    public int getNoteCount(){

        return count;

    }

    public void setTotalPairCount(int pairCount) {
        this.pairCount = pairCount;
    }

    public void setNoteCount(int count) {

        this.count = count;
    }

    public List<CutNote> getCutNoteList() {

        return cutNoteList;
    }

    public void removeCutNoteList(){

        if(!cutNoteList.isEmpty()){

            cutNoteList.clear();
        }




    }

    public int getTotalPairCount() {


        return pairCount;
    }

    public int getMaxPairCount() {
        return this.maxPairCount;
    }

    public void setMaxPairCount(int maxPairCount) {
        this.maxPairCount = maxPairCount;
    }
}
