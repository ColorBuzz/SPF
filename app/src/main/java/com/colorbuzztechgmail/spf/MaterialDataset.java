package com.colorbuzztechgmail.spf;

import android.support.v7.widget.RecyclerView;

import com.colorbuzztechgmail.spf.dataset.Dataset;

import java.util.ArrayList;
import java.util.Collections;

public class MaterialDataset  extends Dataset {






    public MaterialDataset(RecyclerView recyclerView, RecyclerView.Adapter adapter) {
        super(recyclerView, adapter);
    }

    public void replaceItem(Material material){



        int pos=getMaterialPosition(material.getId());

        replaceItem(material,pos);


    }


    @Override
    public String getSortType() {
        return null;
    }

    @Override
    public int getItemId(int position) {
        return getMaterial(position).getId();
    }

    @Override
    protected boolean areItemsEquals(Object a1, Object b2) {
        return ((Material)a1).getId()==((Material)b2).getId();
    }

    @Override
    protected int calculateItemPos(Object a, Object b) {



        return ((Material)a).getName().toLowerCase().compareTo(((Material)b).getName().toLowerCase());

    }



    @Override
    public String getTitleForItem(Object obj) {
        return null;
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