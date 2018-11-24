package com.colorbuzztechgmail.spf;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.colorbuzztechgmail.spf.dataset.Dataset;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class CutNoteListDataset extends Dataset {


    private Context mContext;

    public enum SortType {
        STATUS,
        MODEL,
        REFERENCE
    }

    private SortType sortType = SortType.STATUS;

    public CutNoteListDataset(RecyclerView recyclerView, RecyclerView.Adapter adapter) {
        super(recyclerView, adapter);
        mContext = recyclerView.getContext();
        setSortType(sortType.name());
    }

    @Override
    public String getSortType() {
        return sortType.name();
    }

    @Override
    public int getItemId(int position) {
        return Utils.toIntExact(getCutNoteList(position).getId());
    }

    public void setSortType(SortType sortType) {

        this.sortType=sortType;
    }

    @Override
    protected boolean areItemsEquals(Object a1, Object b2) {


        if(a1 instanceof CustomHeader && b2 instanceof CustomHeader){



            return  (((CustomHeader) a1).getId()==((CustomHeader) b2).getId());


        }else if ((a1 instanceof PreviewModelInfo) && (b2 instanceof PreviewModelInfo)) {



            return  (((CutNoteList) a1).getId()==((CutNoteList) b2).getId());



        }else if ((a1 instanceof ProgressItem) || (b2 instanceof ProgressItem)) {


            return false;
        }else {

            return false;
        }
    }


    @Override
    protected int calculateItemPos(Object a1, Object a2){

        switch(sortType) {

        case MODEL:

            if (a1 instanceof CustomHeader && (!(a2 instanceof CustomHeader))) {

                if (((CustomHeader) a1).getTitle().equals(((CutNoteList)a2).getModel())) {
                    return -1;

                }
            } else if ((!(a1 instanceof CustomHeader)) && a2 instanceof CustomHeader) {

                if (((CutNoteList)a1).getModel().equals(((CustomHeader) a2).getTitle())) {
                    return 1;

                }

            } else if ((!(a1 instanceof CustomHeader)) && (!(a2 instanceof CustomHeader))) {

                if (((CutNoteList)a1).getModel().equals(((CutNoteList)a2).getModel())) {
                    return Long.compare(((CutNoteList)a1).getId(), ((CutNoteList)a2).getId());

                }

            }


                return getComparator().compare(a1, a2);



        case STATUS:


            if (a1 instanceof CustomHeader && (!(a2 instanceof CustomHeader))) {

                if (((CustomHeader) a1).getTitle().equals(convertStatusToString(((CutNoteList)a2).getStatus()))) {
                    return -1;

                }

            } else if ((!(a1 instanceof CustomHeader)) && a2 instanceof CustomHeader) {

                if (convertStatusToString(((CutNoteList)a1).getStatus()).equals(((CustomHeader) a2).getTitle())) {
                    return 1;

                }

            } else if ((!(a1 instanceof CustomHeader)) && (!(a2 instanceof CustomHeader))) {

                if(convertStatusToString(((CutNoteList)a1).getStatus()).equals(convertStatusToString(((CutNoteList)a2).getStatus()))){

                    return Long.compare(((CutNoteList)a1).getId(), ((CutNoteList)a2).getId());

                }
            }

            return getComparator().compare(a1, a2);





            default:

            return getComparator().compare(a1, a2);


    }

}

    @Override
    public String getTitleForItem(Object obj) {

        if(isHeader(obj)){
            return ((CustomHeader)obj).getTitle();

        }else if(obj instanceof ProgressItem){


            return ((ProgressItem)obj).TAG;


        }else {
            switch (sortType) {

                case STATUS:

                    return convertStatusToString(((CutNoteList) obj).getStatus());

                case MODEL:

                    return ((CutNoteList) obj).getModel();


            }
        }
        return null;
    }

    private Comparator<Object> getComparator() {
        switch (sortType) {
            case STATUS:
                return statusComparator;


            case MODEL:
                return alphabeticalComparator;
                default:

                    return alphabeticalComparator;
        }

    }

  

    public void removeByIds(ArrayList<Long> Ids) {

        Collections.sort(Ids);

        sortedList.beginBatchedUpdates();

        for(int i =size()-1;i>=0;i--){

            if(Ids.size()>0){

               for(int j=Ids.size()-1;j>=0;j--){

                   if(!isHeader(i)){

                       if(getCutNoteList(i).getId()==Ids.get(j)){

                           sortedList.removeItemAt(i);
                           Ids.remove(j);
                           break;

                       }

                   }
               }
            }else {

                break;
            }

        }

        sortedList.endBatchedUpdates();

    }

    

    public CutNoteList getCutNoteList(int position) {
        if(position!=RecyclerView.NO_POSITION){
            return (CutNoteList) sortedList.get(position);

        }
        return(CutNoteList) sortedList.get(0);
    }

    public CutNoteList getCutNoteListById(long id) {
        if(size()>0){


                for(int i =0;i<size();i++){

                    if(!isHeader(i)){

                        if(((CutNoteList)sortedList.get(i)).getId()==id){


                            return (CutNoteList) sortedList.get(i);

                        }

                    }

                }

            }
            return (CutNoteList) sortedList.get(0);

    }

    public  class CutNoteListStatusComparator implements Comparator<Object> {
        @Override public int compare(Object first, Object second) {

            String value1,value2;
             
           if(first instanceof CustomHeader){
                value1=((CustomHeader) first).getTitle();

            }else{

               value1=convertStatusToString(((CutNoteList)first).getStatus());

           }

            if(second instanceof CustomHeader){
                value2=((CustomHeader) second).getTitle();

            }else{

                value2=convertStatusToString(((CutNoteList)second).getStatus());



            }

            return value1.toLowerCase().compareTo(value2.toLowerCase());

        }
    }

    public static class CutNoteListIdComparator implements Comparator<Object> {
        @Override public int compare(Object first,Object second) {

            long value1 ,value2;

            if(first instanceof CustomHeader){
                value1=((CustomHeader) first).getId();

            }else{
                
                value1=((CutNoteList)first).getId();
            }

            if(second instanceof CustomHeader){
                value2=((CustomHeader) second).getId();

            }else{

                value2=((CutNoteList)second).getId();
            }

           if   (value1>value2){

                return 1;

           }
          return -1;
        }
    }

    public static class CutNoteListAlphabeticalComparator implements Comparator<Object> {
        @Override public int compare(Object first, Object second) {

            String value1,value2;
            if(first instanceof CustomHeader){
                value1=((CustomHeader) first).getTitle();

            }else{
                
                value1=((CutNoteList)first).getModel();
            }

            if(second instanceof CustomHeader){
                value2=((CustomHeader) second).getTitle();

            }else{

                value2=((CutNoteList)second).getModel();
            }

            return value1.compareTo(value2);
        }
    }


    public  final CutNoteListStatusComparator statusComparator=new CutNoteListStatusComparator();

    public static final CutNoteListIdComparator positionComparator=new CutNoteListIdComparator();

    public static final CutNoteListAlphabeticalComparator alphabeticalComparator=new CutNoteListAlphabeticalComparator();


    public String convertStatusToString(CutNote.cutNoteStatus status){


        switch (status){

            case FINISHED:

                return mContext.getResources().getString(R.string.cutNotes_status_finished);
            case IN_PROGRESS:
                return mContext.getResources().getString(R.string.cutNotes_status_in_progress);
            case INDETERMINATE:
                return mContext.getResources().getString(R.string.cutNotes_status_indeterminated);

                default:
                    return mContext.getResources().getString(R.string.cutNotes_status_indeterminated);






        }


    }




}
