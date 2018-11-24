package com.colorbuzztechgmail.spf;

import android.os.Build;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.util.SortedListAdapterCallback;
import android.util.SparseBooleanArray;
import android.widget.Checkable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MaterialDataset  extends Dataset{






    public MaterialDataset(RecyclerView recyclerView, RecyclerView.Adapter adapter) {
        super(recyclerView, adapter);
    }

    public void replaceItem(Material material){



        int pos=getMaterialPosition(material.getId());

        replaceItem(material,pos);


    }


    @Override
    protected String getSortType() {
        return null;
    }

    @Override
    protected int getItemId(int position) {
        return getMaterial(position).getId();
    }

    @Override
    protected boolean areItemsEquals(Object a1, Object b2) {
        return ((Material)a1).getId()==((Material)b2).getId();
    }

    @Override
    protected void AutomaticTitleGeneratorSortType(List<Object> list, boolean collapse) {

    }

    @Override
    protected int calculateItemPos(Object a, Object b) {



        return ((Material)a).getName().toLowerCase().compareTo(((Material)b).getName().toLowerCase());

    }

    @Override
    protected boolean isItemHaveHeader(Object obj) {
        return false;
    }

    public Material getMaterial(int position) {
        if(position!=RecyclerView.NO_POSITION) {
            return (Material) sortedList.get(position);
        }

        return (Material) sortedList.get(0);
    }

    public Material getMaterialById(int id) {
        if(size()>0){

            for(int i =0;i<size();i++){

                if(!isHeader(i)){

                    if(((Material)sortedList.get(i)).getId()==id){


                        return (Material) sortedList.get(i);

                    }

                }

            }

        }
        return (Material) sortedList.get(0);
    }

    public int getMaterialPosition(int id){


        for(int i =0;i<size();i++){

            if(!isHeader(i)){

                if(((Material)sortedList.get(i)).getId()==id){


                    return (i);

                }

            }



    }
        return -1;
    }

    public void removeByIds(ArrayList<Integer> Ids) {

        Collections.sort(Ids);

        sortedList.beginBatchedUpdates();

        for(int i =size()-1;i>=0;i--){

            if(Ids.size()>0){

                for(int j=Ids.size()-1;j>=0;j--){

                    if(!isHeader(i)){

                        if(getMaterial(i).getId()==Ids.get(j)){

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