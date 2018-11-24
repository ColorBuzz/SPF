package com.colorbuzztechgmail.spf;

import android.support.v7.widget.RecyclerView;

import com.colorbuzztechgmail.spf.dataset.Dataset;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PieceDataset extends Dataset {


    public enum SortType {
        NAME,
        MATERIAL,
     }
    private  SortType sortType =  SortType.MATERIAL;

    public PieceDataset(RecyclerView recyclerView, RecyclerView.Adapter adapter) {

        super(recyclerView, adapter);
         setSortType(sortType.name());
    }

    @Override
    public String getTitleForItem(Object obj) {

        if(isHeader(obj)){

            return ((CustomHeader)obj).getTitle();

        }

        switch (sortType){

            case MATERIAL:

                return ((Piece)obj).getMaterial();



            case NAME:

                String vocalName = ((Piece) obj).getName().substring(0, 1).toUpperCase();
                if (!Character.isAlphabetic(Character.valueOf(vocalName.charAt(0)))) {

                    vocalName = "#";
                }

                return vocalName;





        }




        return null;
    }

    @Override
    public void add(Object obj) {
        super.add(obj);
    }

    @Override
    public void addAll(List<Object> objs) {
        super.addAll(objs);
    }

    @Override
    public void remove(List<Object> objs) {
     }


    public void removeByIds(ArrayList<Long> Ids) {

        Collections.sort(Ids);

        sortedList.beginBatchedUpdates();

        for(int i =size()-1;i>=0;i--){

            if(Ids.size()>0){

                for(int j=Ids.size()-1;j>=0;j--){

                    if(!isHeader(i)){

                        if(getPiece(i).getId()==Ids.get(j)){

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
    public String getSortType() {
        return null;
    }

    @Override
    public int getItemId(int position) {
        return getPiece(position).getId();
    }

    @Override
    protected boolean areItemsEquals(Object a1, Object b2) {

        if(a1 instanceof CustomHeader && b2 instanceof CustomHeader){



            return  (((CustomHeader) a1).getId()==((CustomHeader) b2).getId());


        }else if (!(a1 instanceof CustomHeader) && !(b2 instanceof CustomHeader)) {



            return  (((Piece) a1).getId()==((Piece) b2).getId());



        }else {

            return false;
        }
    }


    @Override
    public void removeIndex(Integer position) {
        super.removeIndex(position);
    }

    @Override
    protected int calculateItemPos(Object a1, Object a2) {



                if (a1 instanceof CustomHeader && (!(a2 instanceof CustomHeader))) {

                    if (((CustomHeader) a1).getTitle().equals(getTitleForItem(a2))) {
                        return -1;

                    }

                } else if ((!(a1 instanceof CustomHeader)) && a2 instanceof CustomHeader) {

                    if (getTitleForItem(a1).equals(((CustomHeader) a2).getTitle())) {
                        return 1;

                    }

                } else if ((!(a1 instanceof CustomHeader)) && (!(a2 instanceof CustomHeader))) {

                    if (getTitleForItem(a1).equals(getTitleForItem(a2))) {
                        return ((Piece) a1).getName().compareToIgnoreCase(((Piece) a2).getName());

                    }

                }

                return nameComparator.compare(a1, a2);





    }


    public Piece getPiece(int position) {
        if(position!=RecyclerView.NO_POSITION) {
            return (Piece) sortedList.get(position);
        }

        return (Piece) sortedList.get(0);
    }

    public Piece getPieceById(long id) {
        if(size()>0){

            for(int i =0;i<size();i++){

                if(!isHeader(i)){

                    if(((Piece)sortedList.get(i)).getId()==id){


                        return (Piece) sortedList.get(i);

                    }

                }

            }

        }
        return (Piece) sortedList.get(0);
    }


    public class PieceNameComparator implements Comparator<Object> {
        @Override
        public int compare(Object first, Object second) {


            return getTitleForItem(first).compareToIgnoreCase(getTitleForItem(second));

        }
    }

    public  final PieceNameComparator nameComparator = new PieceNameComparator();

}
