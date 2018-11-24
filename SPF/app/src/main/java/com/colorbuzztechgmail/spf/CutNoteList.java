package com.colorbuzztechgmail.spf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CutNoteList  extends CutNote {


    private List<CutNote> cutNoteList ;


    public CutNoteList(int cutNoteNumber) {
        super(cutNoteNumber);

        cutNoteList= new ArrayList<>();
        setStatus(cutNoteStatus.INDETERMINATE);

    }

    public void addNote(CutNote cutNote){


        cutNoteList.add(cutNote);


    }
    public void addNote(List<CutNote> cutNotes){

        cutNoteList.addAll(cutNotes);



    }


    public void removeNote(CutNote cutNote){

       for(int i=getCount()-1;i>=0;i--){

           if(cutNoteList.get(i).getNoteNumber()==cutNote.getNoteNumber()){
               cutNoteList.remove(i);
               break;
           }

        }

    }

    public int getCount(){

        return cutNoteList.size();

    }

    public List<CutNote> getCutNoteList() {

        return cutNoteList;
    }

    public boolean areContentsTheSame(CutNoteList newItem) {

        return this.equals(newItem);
    }

    public boolean areItemsTheSame(CutNoteList item) {

        return this.getId()==item.getId();
    }


}
