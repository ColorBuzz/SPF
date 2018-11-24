package com.colorbuzztechgmail.spf;

import android.graphics.drawable.Drawable;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC01 on 29/04/2018.
 */

public class CutNoteListTitle extends CutNoteList{

    private String title;
    private int id;
    private boolean expanded=false;
    private List<CutNoteList> cutNoteBuffer;
    SparseArray<String> sparseId;

    public List<CutNoteList> getCutNoteBuffer() {
        return cutNoteBuffer;
    }

    public CutNoteListTitle(String title , int id, Drawable image) {
        super(0);


        this.title=title;
        sparseId=new SparseArray<>();
        cutNoteBuffer=new ArrayList<>();


        this.id=id;

     }


    public boolean isExpanded() {
        return expanded;
    }


    public void addCutNoteList(CutNoteList cutNote){

        if(cutNoteBuffer!=null){

            cutNoteBuffer.add(cutNote);
            sparseId.append(cutNote.getId(),cutNote.getModel());

        }

    }

    public void removeCutNoteList(CutNoteList cutNote){

        if(cutNote!=null){

           cutNoteBuffer.remove(sparseId.indexOfKey(cutNote.getId()));
            sparseId.delete(cutNote.getId());

        }


    }

    public void setCutNoteBuffer(List<CutNoteList>cutNoteBuffer) {
        this.cutNoteBuffer =cutNoteBuffer;
        sparseId.clear();

        for(int j=0;j<cutNoteBuffer.size();j++){

         sparseId.append(cutNoteBuffer.get(j).getId(),cutNoteBuffer.get(j).getModel());


        }

    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }
    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
