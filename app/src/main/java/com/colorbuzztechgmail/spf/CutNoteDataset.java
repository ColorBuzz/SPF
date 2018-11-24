package com.colorbuzztechgmail.spf;

import android.support.v7.widget.RecyclerView;

import com.colorbuzztechgmail.spf.dataset.Dataset;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.colorbuzztechgmail.spf.Utils.getSizeListString;

public class CutNoteDataset extends Dataset {




    public enum SortType {
        STATUS,
        POSITION,
        SIZE,
        PAIRCOUNT
    }

    private SortType sortType = SortType.SIZE;




    public CutNoteDataset(RecyclerView recyclerView, RecyclerView.Adapter adapter) {
        super(recyclerView, adapter);
         setSortType(sortType.name());
    }

    @Override
    public String getSortType() {
        return sortType.name();
    }

    @Override
    public int getItemId(int position) {
        return Utils.toIntExact(getCutNote(position).getId());
    }

    public void setSortType(SortType sortType) {

        this.sortType=sortType;
    }

    @Override
    protected boolean areItemsEquals(Object a1, Object b2) {




        if(a1 instanceof CustomHeader && b2 instanceof CustomHeader){



            return  (((CustomHeader) a1).getId()==((CustomHeader) b2).getId());


        }else if (!(a1 instanceof CustomHeader) && !(b2 instanceof CustomHeader)) {



            return  (((CutNote) a1).getId()==((CutNote) b2).getId());



        }else {

            return false;
        }


    }


    @Override
    protected int calculateItemPos(Object a1, Object a2){

        switch(sortType) {

        case STATUS:


            if (a1 instanceof CustomHeader && (!(a2 instanceof CustomHeader))) {

                if (((CustomHeader) a1).getTitle().equals(Utils.convertStatusToString(getContext(),((CutNote)a2).getStatus()))) {
                    return -1;

                } else {

                    return getComparator().compare(a1, a2);

                }

            } else if ((!(a1 instanceof CustomHeader)) && a2 instanceof CustomHeader) {

                if (Utils.convertStatusToString(getContext(),((CutNote)a1).getStatus()).equals(((CustomHeader) a2).getTitle())) {


                    return 1;

                } else {

                    return getComparator().compare(a1, a2);

                }

            }else if ((!(a1 instanceof CustomHeader)) && (!(a2 instanceof CustomHeader))) {

                if(Utils.convertStatusToString(getContext(),((CutNote)a1).getStatus()).equals(Utils.convertStatusToString(getContext(),((CutNote)a2).getStatus()))){

                    return positionComparator.compare(a1,a2);



                }

                return getComparator().compare(a1, a2);
            }


            case POSITION:
                          if (a1 instanceof CustomHeader && (!(a2 instanceof CustomHeader))) {



                        return -1;



                } else if ((!(a1 instanceof CustomHeader)) && a2 instanceof CustomHeader) {



                        return 1;



                } else if ((!(a1 instanceof CustomHeader)) && (!(a2 instanceof CustomHeader))) {

                    if (((CutNote)a1).getNoteNumber()==((CutNote)a2).getNoteNumber()) {
                        return alphabeticalComparator.compare(a1, a2);

                    } else {

                        return positionComparator.compare(a1, a2);

                    }

                } else {

                    return getComparator().compare(a1, a2);
                }


            case SIZE:
                if (a1 instanceof CustomHeader && (!(a2 instanceof CustomHeader))) {

                    if (((CustomHeader) a1).getTitle().equals(getTitleForItem(a2))) {
                        return -1;

                    } else {

                        return getComparator().compare(a1, a2);

                    }

                } else if ((!(a1 instanceof CustomHeader)) && a2 instanceof CustomHeader) {

                    if (getTitleForItem(a1).equals(((CustomHeader) a2).getTitle())) {
                        return 1;

                    } else {

                        return getComparator().compare(a1, a2);

                    }

                } else if ((!(a1 instanceof CustomHeader)) && (!(a2 instanceof CustomHeader))) {

                    if (getSizeListString((CutNote)a1).equals(getTitleForItem(a2))) {
                        return positionComparator.compare(a1, a2);

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




    private Comparator<Object> getComparator() {
        switch (sortType) {
            case STATUS:
                return statusComparator;
            case POSITION:
                return positionComparator;

            case SIZE:

                return sizeComparator;


                default:

                    return positionComparator;
        }

    }


    public void removeByIds(ArrayList<Long> Ids) {

        Collections.sort(Ids);

        sortedList.beginBatchedUpdates();

        for(int i =size()-1;i>=0;i--){

            if(Ids.size()>0){

                for(int j=Ids.size()-1;j>=0;j--){

                    if(!isHeader(i)){

                        if(getCutNote(i).getId()==Ids.get(j)){

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


    @Override
    public String getTitleForItem(Object obj) {
        String title=null;

        if(isHeader(obj)){

            return ((CustomHeader)obj).getTitle();

        }

        switch (sortType){

            case POSITION:

               return getContext().getString(R.string.cutNote_number);



            case PAIRCOUNT:

                return  String.valueOf(((CutNote)obj).getPairCount());



            case STATUS:


                return  Utils.convertStatusToString(getContext(),((CutNote)obj).getStatus());


            case SIZE:

                return Utils.getSizeListString((CutNote)obj);




        }

        return title;

    }

    public CutNote getCutNote(int position) {
        if(position!=RecyclerView.NO_POSITION){

            if(!isHeader(position))
            return (CutNote) sortedList.get(position);

        }
        return null;
    }

    public CutNote getCutNoteById(long id) {
        if(size()>0){

            for(int i =0;i<size();i++){

                if(!isHeader(i)){

                    if(getCutNote(i).getId()==id){


                        return getCutNote(i);

                    }

                }

            }

        }
        return getCutNote(0);
    }

    public List<CutNote> getCutNoteItems() {


        final List<CutNote> cutNoteList = new ArrayList<>();


        for (int i = 0; i < size(); i++) {

            if (isHeader(getObject(i))) {

                CustomHeader header = ((CustomHeader) getObject(i));
                if (!header.isExpanded()) {
                    if (!header.isEmpty()) {

                        for (Object obj : header.getBuffer())
                            cutNoteList.add((CutNote) obj);

                    }
                }

            } else {

                cutNoteList.add(getCutNote(i));

            }
        }

        return cutNoteList;



    }

    public  class CutNoteStatusComparator implements Comparator<Object> {
        @Override public int compare(Object first, Object second) {

            String value1,value2;
             
           if(first instanceof CustomHeader){
                value1=((CustomHeader) first).getTitle();

            }else{

               value1=Utils.convertStatusToString(getContext(),((CutNote)first).getStatus());

           }

            if(second instanceof CustomHeader){
                value2=((CustomHeader) second).getTitle();

            }else{

                value2=Utils.convertStatusToString(getContext(),((CutNote)second).getStatus());



            }

            return value1.toLowerCase().compareTo(value2.toLowerCase());

        }
    }

    public static class CutNoteIdComparator implements Comparator<Object> {
        @Override public int compare(Object first,Object second) {


            int value1 ,value2;

            if(first instanceof CustomHeader){
                value1=((CustomHeader) first).getId();

            }else{

                value1=((CutNote)first).getNoteNumber();
            }

            if(second instanceof CustomHeader){
                value2=((CustomHeader) second).getId();

            }else{

                value2=((CutNote)second).getNoteNumber();
            }

            if   (value1>value2){

                return 1;

            }
            return -1;


        }
    }

    public static class CutNoteAlphabeticalComparator implements Comparator<Object> {
        @Override public int compare(Object first, Object second) {

            String value1,value2;
            if(first instanceof CustomHeader){
                value1=((CustomHeader) first).getTitle();

            }else{
                
                value1=((CutNote)first).getModel();
            }

            if(second instanceof CustomHeader){
                value2=((CustomHeader) second).getTitle();

            }else{

                value2=((CutNote)second).getModel();
            }

            return value1.toLowerCase().compareTo(value2.toLowerCase());
        }
    }

    public static class CutNoteSizeComparator implements Comparator<Object> {
        @Override public int compare(Object first, Object second) {

            String value1,value2;

            if(first instanceof CustomHeader){
                value1=((CustomHeader) first).getTitle();

            }else{

                value1=getSizeListString((CutNote)first);
            }

            if(second instanceof CustomHeader){
                value2=((CustomHeader) second).getTitle();

            }else{

                value2=getSizeListString((CutNote)second);
            }

            return value1.toLowerCase().compareTo(value2.toLowerCase());
        }
    }


    public  final CutNoteStatusComparator statusComparator=new CutNoteStatusComparator();

    public static final CutNoteIdComparator positionComparator =new CutNoteIdComparator();

    public static final CutNoteAlphabeticalComparator alphabeticalComparator=new CutNoteAlphabeticalComparator();

    public static final CutNoteSizeComparator sizeComparator=new CutNoteSizeComparator();








}
