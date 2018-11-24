package com.colorbuzztechgmail.spf;

import android.content.Context;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.util.SortedListAdapterCallback;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CutNoteListDataset extends Dataset {


    private Context mContext;

    public enum SortType {
        STATUS,
        MODEL,
        LAST
    }

    private SortType sortType = SortType.MODEL;

    public CutNoteListDataset(RecyclerView recyclerView, RecyclerView.Adapter adapter) {
        super(recyclerView, adapter);
        mContext = recyclerView.getContext();
        setSortType(sortType.name());
    }

    @Override
    protected String getSortType() {
        return sortType.name();
    }

    @Override
    protected int getItemId(int position) {
        return getCutNoteList(position).getId();
    }

    public void setSortType(SortType sortType) {

        this.sortType=sortType;
    }

    @Override
    protected boolean areItemsEquals(Object a1, Object b2) {
        int id1=0,id2=0;

        if(a1 instanceof CustomHeader){

            id1=((CustomHeader)a1).getId();

        }else{
            id1=((CutNoteList)a1).getId();


        }

        if(b2 instanceof CustomHeader){

            id2=((CustomHeader)b2).getId();

        }else{
            id2=((CutNoteList)b2).getId();


        }

        return (id1==id2);
    }

    @Override
    protected void AutomaticTitleGeneratorSortType(List<Object> list, boolean collapse) {


        final List<Object> headerList = new ArrayList<>();


        if (list.size() > 0) {

            Utils.IdMaker mIdMaker;
            mIdMaker = new Utils.IdMaker();
            final ArrayList<String> buffer = new ArrayList<>();

            switch (sortType) {

                case STATUS:

                    for (int i = 0; i < list.size(); i++) {
                        if (!(list.get(i) instanceof CustomHeader)) {
                            final String status = convertStatusToString(((CutNoteList) list.get(i)).getStatus());

                            boolean isPresent = false;
                            for (Object header : headerList) {

                                if (((CustomHeader) header).getTitle().equals(status)) {
                                    ((CustomHeader) header).addObject(list.get(i));

                                    isPresent = true;
                                }
                            }

                            if (!isPresent) {

                                final CustomHeader mTitle = new CustomHeader(status, mIdMaker.next(), null);
                                mTitle.addObject(list.get(i));
                                headerList.add(mTitle);

                            }
                        }

                    }

                    break;


                case MODEL:
                    for (int i = 0; i < list.size(); i++) {
                        if (!(list.get(i) instanceof CustomHeader)) {
                            final String type = ((CutNoteList) list.get(i)).getModel();

                            boolean isPresent = false;
                            for (Object header : headerList) {

                                if (((CustomHeader) header).getTitle().equals(type)) {
                                    ((CustomHeader) header).addObject(list.get(i));

                                    isPresent = true;
                                }
                            }

                            if (!isPresent) {

                                final CustomHeader mTitle = new CustomHeader(type, mIdMaker.next(), null);
                                mTitle.addObject(list.get(i));
                                headerList.add(mTitle);

                            }
                        }

                    }

                    break;

                case LAST:
                    for (int i = 0; i < list.size(); i++) {
                        if (!(list.get(i) instanceof CustomHeader)) {
                            final String date = ((CutNoteList) list.get(i)).getDate().split(String.valueOf('\n'))[0];
                            boolean isPresent = false;
                            for (Object header : headerList) {

                                if (((CustomHeader) header).getTitle().equals(date)) {
                                    ((CustomHeader) header).addObject(list.get(i));

                                    isPresent = true;
                                }
                            }

                            if (!isPresent) {

                                final CustomHeader mTitle = new CustomHeader(date, mIdMaker.next(), null);
                                mTitle.addObject(list.get(i));
                                headerList.add(mTitle);

                            }

                        }
                    }

                    break;


            }

            if (!collapse) {
                for (Object header : headerList) {

                    addAll(((CustomHeader) header).getBuffer());
                    ((CustomHeader) header).removeAll();
                    ((CustomHeader) header).setExpanded(true);
                }
            }

            addAll(headerList);

        } else {

            setEmptyTitle();
        }


    }

    @Override
    protected int calculateItemPos(Object a1, Object a2){

        switch(sortType) {

        case MODEL:

            if (a1 instanceof CustomHeader && (!(a2 instanceof CustomHeader))) {

                if (((CustomHeader) a1).getTitle().equals(((CutNoteList)a2).getModel())) {
                    return -1;

                } else {

                    return getComparator().compare(a1, a2);

                }

            } else if ((!(a1 instanceof CustomHeader)) && a2 instanceof CustomHeader) {

                if (((CutNoteList)a1).getModel().equals(((CustomHeader) a2).getTitle())) {
                    return 1;

                } else {

                    return getComparator().compare(a1, a2);

                }

            } else if ((!(a1 instanceof CustomHeader)) && (!(a2 instanceof CustomHeader))) {

                if (((CutNoteList)a1).getModel().equals(((CutNoteList)a2).getModel())) {
                    return alphabeticalComparator.compare(a1, a2);

                } else {

                    return getComparator().compare(a1, a2);

                }

            } else {


                return getComparator().compare(a1, a2);
            }


        case STATUS:


            if (a1 instanceof CustomHeader && (!(a2 instanceof CustomHeader))) {

                if (((CustomHeader) a1).getTitle().equals(convertStatusToString(((CutNoteList)a2).getStatus()))) {
                    return -1;

                } else {

                    return getComparator().compare(a1, a2);

                }

            } else if ((!(a1 instanceof CustomHeader)) && a2 instanceof CustomHeader) {

                if (convertStatusToString(((CutNoteList)a1).getStatus()).equals(((CustomHeader) a2).getTitle())) {
                    return 1;

                } else {

                    return getComparator().compare(a1, a2);

                }

            } else {

                return getComparator().compare(a1, a2);
            }


        case LAST:
            if (a1 instanceof CustomHeader && (!(a2 instanceof CustomHeader))) {

                if (((CustomHeader) a1).getTitle().contains(((CutNoteList)a2).getDate())) {
                    return -1;

                } else {

                    return getComparator().compare(a1, a2);

                }

            } else if ((!(a1 instanceof CustomHeader)) && a2 instanceof CustomHeader) {

                if (((CutNoteList)a1).getDate().contains(((CustomHeader) a2).getTitle())) {
                    return 1;

                } else {

                    return getComparator().compare(a1, a2);

                }

            } else if ((!(a1 instanceof CustomHeader)) && (!(a2 instanceof CustomHeader))) {

                if (((CutNoteList)a1).getDate().contains(((CutNoteList)a2).getDate())) {
                    return alphabeticalComparator.compare(a1, a2);

                } else {

                    return getComparator().compare(a1, a2);

                }

            } else {

                return getComparator().compare(a1, a2);
            }


        default:

            return getComparator().compare(a1, a2);


    }

}


    @Override
    protected boolean isItemHaveHeader(Object obj) {
        return false;
    }

    private Comparator<Object> getComparator() {
        switch (sortType) {
            case STATUS:
                return statusComparator;
            case LAST:
                return lastComparator;

            case MODEL:
                return alphabeticalComparator;
                default:

                    return alphabeticalComparator;
        }

    }

  

    public void removeByIds(ArrayList<Integer> Ids) {

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

    public CutNoteList getCutNoteListById(int id) {
        if(size()>0){

            for(int i =0;i<size();i++){

                if(!isHeader(i)){

                    if(getCutNoteList(i).getId()==id){


                        return getCutNoteList(i);

                    }

                }

            }

        }
        return getCutNoteList(0);
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

            String value1 ,value2;

            if(first instanceof CustomHeader){
                value1=((CustomHeader) first).getTitle();

            }else{
                
                value1=((CutNoteList)first).getDate();
            }

            if(second instanceof CustomHeader){
                value2=((CustomHeader) second).getTitle();

            }else{

                value2=((CutNoteList)second).getDate();
            }

            return value1.toLowerCase().compareTo(value2.toLowerCase());


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

            return value1.toLowerCase().compareTo(value2.toLowerCase());
        }
    }

    public  final CutNoteListStatusComparator statusComparator=new CutNoteListStatusComparator();

    public static final CutNoteListIdComparator lastComparator=new CutNoteListIdComparator();

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
