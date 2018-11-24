package com.colorbuzztechgmail.spf;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PieceDataset extends Dataset {


    public PieceDataset(RecyclerView recyclerView, RecyclerView.Adapter adapter) {
        super(recyclerView, adapter);
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

    @Override
    public void remove(Integer position) {
      }

    @Override
    public void remove(ArrayList<Integer> position) {
     }

    @Override
    protected String getSortType() {
        return null;
    }

    @Override
    protected int getItemId(int position) {
        return getPiece(position).getId();
    }

    @Override
    protected boolean areItemsEquals(Object a1, Object b2) {
        return ((Piece)a1).getId()==((Piece)b2).getId();
    }

    @Override
    protected void AutomaticTitleGeneratorSortType(List<Object> list, boolean collapse) {

    }

    @Override
    protected int calculateItemPos(Object a, Object b) {

        if(((Piece)a).getMaterial().equals(((Piece)b).getMaterial())){

            return ((Piece)a).getName().toLowerCase().compareTo(((Piece)b).getName().toLowerCase());
        }

        return ((Piece)a).getMaterial().toLowerCase().compareTo(((Piece)b).getMaterial().toLowerCase());

    }

    @Override
    protected boolean isItemHaveHeader(Object obj) {
        return false;
    }

    public Piece getPiece(int position) {
        if(position!=RecyclerView.NO_POSITION) {
            return (Piece) sortedList.get(position);
        }

        return (Piece) sortedList.get(0);
    }

    public Piece getPieceById(int id) {
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

    public void removeByIds(ArrayList<Integer> Ids) {

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
}
