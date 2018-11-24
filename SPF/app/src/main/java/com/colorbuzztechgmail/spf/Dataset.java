package com.colorbuzztechgmail.spf;

import android.os.Build;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.util.SortedListAdapterCallback;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.widget.Checkable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public abstract class Dataset {



    public SparseArray<String> sparseHeader;
    public SparseBooleanArray sparsState;
    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;



    SortedList<Object> sortedList = null;
    public String sortType ;

    public int getItemCount() {

        int count=0;
        for(int i=0;i<sparseHeader.size();i++){

            count=count+((CustomHeader)getObject(sparseHeader.keyAt(i))).getBuffer().size();
        }


        return (count+size())-sparseHeader.size();
    }

    public Dataset(final RecyclerView recyclerView, final RecyclerView.Adapter adapter) {

        this.adapter=adapter;
        this.sparseHeader=new SparseArray();
        this.recyclerView=recyclerView;
        this.sparsState= new SparseBooleanArray();

        int count =0;

         SortedList.Callback<Object> mCallback = new SortedList.Callback<Object>() {

             @Override
             public int compare(Object a1, Object a2) {

                 return calculateItemPos(a1,a2);
             }

             @Override
             public void onInserted(int position, int count) {
                 adapter.notifyItemRangeInserted(position,count);
                  searchPosHeaders();
                  addSparseState(position,count);

             }

             @Override
             public void onMoved(int fromPosition, int toPosition) {
                 adapter.notifyItemMoved(fromPosition, toPosition);

             }

             @Override
             public void onChanged(int position, int count) {
                 adapter.notifyItemRangeChanged(position, count);

             }

             @Override
             public void onRemoved(int position, int count) {
                 adapter.notifyItemRangeRemoved(position, count);

                  searchPosHeaders();
                 deleteSparseState(position,count);


             }




            @Override
            public boolean areContentsTheSame(Object oldItem, Object newItem) {
                return oldItem.equals(newItem);
            }

            @Override
            public boolean areItemsTheSame(Object item1, Object item2) {

                if(isHeader(item1) && !isHeader(item2)){

                    return false;
                }else if (!isHeader(item1) && isHeader(item2)){

                    return false;



                }
                return areItemsEquals(item1,item2);
            }
        };
        this.sortedList = new SortedList<>(Object.class,mCallback);
    }




    public int getTitleCount() {
        return sparseHeader.size();
    }

    public int size() {
        return sortedList.size();
    }

    public void add(Object obj) {

        sortedList.beginBatchedUpdates();

            sortedList.add(obj);


        sortedList.endBatchedUpdates();


    }

    public void addAll(List<Object> objs) {
        sortedList.beginBatchedUpdates();


        for(int i=0;i<objs.size();i++){
            sortedList.add( objs.get(i));


        }
        sortedList.endBatchedUpdates();



    }

    public void remove(List<Object> objs) {

        sortedList.beginBatchedUpdates();
        for (int i=0;i<sortedList.size();i++) {
            for (int j=0;j<objs.size();j++){

                if(sortedList.get(i).equals(objs.get(j))){

                    sortedList.remove(objs.get(j));
                    break;
                }

            }
        }
        sortedList.endBatchedUpdates();



    }

    public void remove(Integer position) {


        sortedList.removeItemAt(position);

        adapter.notifyItemRemoved(position);

    }

    public void remove(ArrayList<Integer> position) {

        Collections.sort(position);

        sortedList.beginBatchedUpdates();

        for(int i=position.size()-1;i>=0;i--) {

            sortedList.removeItemAt(position.get(i));
        }

        sortedList.endBatchedUpdates();



    }

    public void removeByHash(ArrayList<Integer> hashCodes) {

        Collections.sort(hashCodes);

        sortedList.beginBatchedUpdates();

        for(int i =size()-1;i>=0;i--){

            if(hashCodes.size()>0){

                for(int j=hashCodes.size()-1;j>=0;j--){

                    if(!isHeader(i)){

                        if(getObject(i).hashCode()==hashCodes.get(j)){

                            sortedList.removeItemAt(i);
                            hashCodes.remove(j);
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

    public void remove(Object obj){
        sortedList.beginBatchedUpdates();
        sortedList.remove( obj);
        sortedList.endBatchedUpdates();




    }

    public void removeAll(){

        sortedList.beginBatchedUpdates();
        sortedList.clear();
        sortedList.endBatchedUpdates();


    }

    public void replaceAll(List<Object> objs) {



        sortedList.beginBatchedUpdates();

        for (int i =sortedList.size() - 1; i >= 0; i--) {

            final Object obj =sortedList.get(i);
            if (!objs.contains(obj)) {
                sortedList.remove(obj);
            }
        }
        sortedList.addAll((ObjectItem) objs);

        sortedList.endBatchedUpdates();



    }

    public void replaceItem(Object obj,int position) {

        sortedList.updateItemAt(position,obj);
            }

    public void changeSortType() {
        if (!this.sortType.equals(getSortType())) {
            this.sortType =getSortType();




            List<Object> items = new ArrayList<>();
            for (int j = 0; j < sortedList.size(); j++) {
                if (!(sortedList.get(j) instanceof CustomHeader)){
                    items.add(sortedList.get(j));

                }else{
                    if ((((CustomHeader) sortedList.get(j)).getBuffer().size() > 0)) {

                        for (Object obj : (((CustomHeader) sortedList.get(j)).getBuffer())) {
                            items.add(obj);


                        }
                    }


                }
            }
            removeAll();
            AutomaticTitleGeneratorSortType( items,true);

            recyclerView.scrollToPosition(0);

        }


    }

    public Object getObject(int position) {
        if(position!=RecyclerView.NO_POSITION){
            return sortedList.get(position);

        }
        return sortedList.get(0);
    }

    public Object getObjectByHash(int hash) {
        if(size()>0){

            for(int i =0;i<size();i++){

                if(!isHeader(i)){

                    if(sortedList.get(i).hashCode()==hash){


                        return sortedList.get(i);

                    }

                }

            }

        }
        return sortedList.get(0);
    }

    protected abstract String getSortType();

    public  void setSortType(String sortType){

        this.sortType=sortType;
    };

    ///////Header

    public boolean isHeader(int position){

        return (getObject(position) instanceof CustomHeader);
    }

    public boolean isHeader(Object obj){

        return (obj instanceof CustomHeader);
    }

    public int getHeaderPosition(String title){

        return sparseHeader.keyAt(sparseHeader.indexOfValue(title));
    }

    public void updateHeaderForNewItem(Object obj){

        if(sparseHeader.size()>0) {

            if (isItemHaveHeader(obj)) {

                add(obj);

            }else{

                final List<Object> objList = new ArrayList<>();

                objList.add(obj);

                AutomaticTitleGeneratorSortType(objList, false);
            }


            deleteEmptyHeaders();


        }else{

            removeAll();

            final List<Object>objList=new ArrayList<>();

            objList.add(obj);

            AutomaticTitleGeneratorSortType(objList,false);

        }


    }

    private void deleteEmptyHeaders(){


        ArrayList<Integer>removeIndex=new ArrayList<>();

        for (int i =  sparseHeader.size()-1; i >=0; i--) {

            if(sparseHeader.keyAt(i)==size()-1){

                if(((CustomHeader)getObject(sparseHeader.keyAt(i))).isExpanded())
                    removeIndex.add(sparseHeader.keyAt(i));

            }else {


                if(isHeader(sparseHeader.keyAt(i) +1)){
                    if(((CustomHeader)getObject(sparseHeader.keyAt(i))).isExpanded()){

                        removeIndex.add(sparseHeader.keyAt(i));

                    }else {

                        if(((CustomHeader)getObject(sparseHeader.keyAt(i))).getBuffer().size()==0){

                            removeIndex.add(sparseHeader.keyAt(i));
                        }

                    }
                }

            }


        }


        remove(removeIndex);
    }

    private void searchPosHeaders(){

        if(size()>0){

            sparseHeader.clear();

            for(int i=0;i<size();i++){

                if(isHeader(i)){
                    sparseHeader.append(i,((CustomHeader)getObject(i)).getTitle());

                }
            }

        }

    }

    public void setEmptyTitle(){

        removeAll();
        final CustomHeader mTitle = new CustomHeader("Categoria vacia", 0, recyclerView.getContext().getResources().getDrawable(R.drawable.ic_info_grey_24dp));
        add(mTitle);
        sparseHeader.append(0,"Categoria vacia");

    }

    /////////Item States

    private void addSparseState(int position,int count){

        int i=0;
        do{

            final Object item=getObject(position+i);
            if(!isHeader(item)){
                sparsState.append(getItemId(position+i),false);

            }
            count--;

            i++;

        }while (count>0);


    }

    private void deleteSparseState(int position,int count){



    }

    public void updateSparseState(int position,boolean state){

        sparsState.put(getItemId(position),state);

    }

    public boolean getItemState(int id){

        return sparsState.get(id);

    }



    protected abstract int getItemId(int position);

    protected  abstract boolean areItemsEquals(Object a1,Object b2);

    protected abstract void AutomaticTitleGeneratorSortType(List<Object> list,boolean collapse );

    protected abstract int calculateItemPos(Object a1,Object a2);

    protected abstract  boolean isItemHaveHeader(Object obj);








}
