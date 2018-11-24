package com.colorbuzztechgmail.spf.dataset;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.util.SortedListAdapterCallback;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.widget.Checkable;

import com.colorbuzztechgmail.spf.Accessories;
import com.colorbuzztechgmail.spf.Category;
import com.colorbuzztechgmail.spf.CustomHeader;
import com.colorbuzztechgmail.spf.CustomMaterial;
import com.colorbuzztechgmail.spf.CutNote;
import com.colorbuzztechgmail.spf.CutNoteList;
import com.colorbuzztechgmail.spf.Dealership;
import com.colorbuzztechgmail.spf.ObjectItem;
import com.colorbuzztechgmail.spf.Piece;
import com.colorbuzztechgmail.spf.PreviewModelInfo;
import com.colorbuzztechgmail.spf.ProgressItem;
import com.colorbuzztechgmail.spf.R;
import com.colorbuzztechgmail.spf.Utils;
import com.colorbuzztechgmail.spf.adapter.BaseAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public abstract class Dataset {



    public SparseArray<String> sparseHeader;
    public SparseBooleanArray sparsState;
    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;
    private Context context;


    public SortedList<Object> sortedList = null;

    public String sortType ;



    public Context getContext() {
        return context;
    }

    public int getItemCount() {

        int count=0;
        for(int i=0;i<sparseHeader.size();i++){

            if(isHeader(getObject(sparseHeader.keyAt(i))))
            count=count+((CustomHeader)getObject(sparseHeader.keyAt(i))).getBuffer().size();
        }


        return (count+size())-sparseHeader.size();
    }

    public Dataset(final RecyclerView recyclerView, final RecyclerView.Adapter adapter) {

        this.adapter=adapter;
        this.sparseHeader=new SparseArray();
        this.recyclerView=recyclerView;
        this.sparsState= new SparseBooleanArray();
        this.context=recyclerView.getContext();


        int count =0;

         SortedList.Callback<Object> mCallback = new SortedList.Callback<Object>() {

             @Override
             public int compare(Object a1, Object a2) {


                 if (a1 instanceof ProgressItem) {


                     return 1;
                 }else if(a2 instanceof ProgressItem){

                     return -1;

                 }



                 return calculateItemPos(a1,a2);
             }

             @Override
             public void onInserted(int position, int count) {
                 adapter.notifyItemRangeInserted(position,count);
                  searchPosHeaders();
                  //addSparseState(position,count);
                  recyclerView.scrollToPosition(position);


             }

             @Override
             public void onMoved(int fromPosition, int toPosition) {
                 adapter.notifyItemMoved(fromPosition, toPosition);
                 searchPosHeaders();

             }

             @Override
             public void onChanged(int position, int count) {
                 adapter.notifyItemRangeChanged(position, count);
                 searchPosHeaders();

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

    public void removeIndex(Integer position) {


        sortedList.removeItemAt(position);


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
        sparsState.clear();
        sparseHeader.clear();
        sortedList.endBatchedUpdates();


    }
    public void removeAll(boolean notify){

        sortedList.beginBatchedUpdates();
        sortedList.clear();
        sparsState.clear();
        sparseHeader.clear();
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

    public int getItemPositionByHash(int hash){

        if(size()>0){

            for(int i =0;i<size();i++){

                if(!isHeader(i)){

                    if(sortedList.get(i).hashCode()==hash){


                        return i;

                    }

                }

            }

        }

        return 0;

    }

    public abstract String getSortType();

    public  void setSortType(String sortType){

        this.sortType=sortType;
    };

    ///////Header


    public void AutomaticTitleGeneratorSortType(List<Object> list, boolean collapse) {


        final List<Object> headerList = new ArrayList<>();


        if (list.size() > 0) {
            Utils.IdMaker mIdMaker;
            mIdMaker = new Utils.IdMaker();
            for (int i = 0; i < list.size(); i++) {

                if (!(list.get(i) instanceof CustomHeader)) {
                    final String titleTxt = getTitleForItem(list.get(i));

                    boolean isPresent = false;
                    for (Object header : headerList) {

                        if (((CustomHeader) header).getTitle().equals(titleTxt)) {
                            ((CustomHeader) header).addObject(list.get(i));

                            isPresent = true;
                        }
                    }
                    if (!isPresent) {

                        final CustomHeader mTitle = new CustomHeader(titleTxt, mIdMaker.next()+1000, getTitleDrawable(list.get(i)));
                        mTitle.addObject(list.get(i));
                        headerList.add(mTitle);

                    }
                }

            }


        /*    for(Object header :headerList){


                Log.d("UNSORTED_LIST ", ((CustomHeader)header).getTitle());




            }
*/

             Collections.sort(headerList,SENSITIVE_COMPARATOR);

           /* for(Object header :headerList){


                Log.d("SORTED_LIST ", ((CustomHeader)header).getTitle());




            }*/

                    addAll(headerList);
            if (!collapse) {
                for (Object header : headerList) {

                    addAll(((CustomHeader) header).getBuffer());
                    ((CustomHeader) header).removeAll();
                    ((CustomHeader) header).setExpanded(true);
                }
            }



        } else {

            setEmptyTitle();
        }


    }

    public boolean isHeader(int position){

        return (getObject(position) instanceof CustomHeader);
    }

    public boolean isHeader(Object obj){

        return (obj instanceof CustomHeader);
    }

    public int getHeaderPosition(String title){

        return sparseHeader.keyAt(sparseHeader.indexOfValue(title));
    }

    public void addNewItem(Object obj){

        if(sparseHeader.size()>0) {

            if (isItemHaveHeader(obj)) {


                   add(obj);
            }else{


                final List<Object> objList = new ArrayList<>();

                objList.add(obj);

                AutomaticTitleGeneratorSortType(objList, true);
            }


           deleteEmptyHeaders();


        }else{

            removeAll();

            final List<Object>objList=new ArrayList<>();

            objList.add(obj);

            AutomaticTitleGeneratorSortType(objList,false);

        }


    }

    public void addNewItem(Object obj,boolean isCollapsed){

        if(sparseHeader.size()>0) {

            if (isItemHaveHeader(obj)) {


                add(obj);
            }else{


                final List<Object> objList = new ArrayList<>();

                objList.add(obj);

                AutomaticTitleGeneratorSortType(objList, isCollapsed);
            }


            deleteEmptyHeaders();


        }else{

            removeAll();

            final List<Object>objList=new ArrayList<>();

            objList.add(obj);

            AutomaticTitleGeneratorSortType(objList,false);

        }


    }


    public void editItem(Object obj,int position) {


        if (isItemHaveHeader(obj)) {

            replaceItem(obj,position);

        } else {

            CustomHeader mTitle = new CustomHeader(getTitleForItem(obj), Utils.IdMaker.next(), getTitleDrawable(obj));

            mTitle.setExpanded(true);


            ArrayList<Integer> hash = new ArrayList<>();

            hash.add( getObject(position).hashCode());

             removeByHash(hash);

             add(mTitle);
             add(obj);



        }
        recyclerView.scrollToPosition(position);
        deleteEmptyHeaders();

    }

    public  boolean isItemHaveHeader(Object obj){


        for(int i=0;i<sparseHeader.size();i++){

            if(sparseHeader.valueAt(i).equals(getTitleForItem(obj)))

            return true;
        }

        return false;

    }


    public void deleteEmptyHeaders(){


        ArrayList<Integer>removeIndex=new ArrayList<>();

        for (int i =  sparseHeader.size()-1; i >=0; i--) {

            if(sparseHeader.keyAt(i)==size()-1){

                if(((CustomHeader)getObject(sparseHeader.keyAt(i))).getBuffer().size()==0 && ((CustomHeader)getObject(sparseHeader.keyAt(i))).isExpanded() ){

                    removeIndex.add(sparseHeader.keyAt(i));
                    sparseHeader.removeAt(i);

                }



            }else {


                if(isHeader(sparseHeader.keyAt(i) +1)){

                    if(((CustomHeader)getObject(sparseHeader.keyAt(i))).isExpanded()){

                        removeIndex.add(sparseHeader.keyAt(i));
                        sparseHeader.removeAt(i);

                    }else{


                        if(((CustomHeader)getObject(sparseHeader.keyAt(i))).getBuffer().size()==0){

                            removeIndex.add(sparseHeader.keyAt(i));
                            sparseHeader.removeAt(i);

                        }
                    }

                }

            }


        }



        remove(removeIndex);
        if(size()==0)
            setEmptyTitle();

    }

    public void removeHeaders(){


        for(int i=sparseHeader.size()-1;i>=0;i--){

            if(isHeader(i)){

                sortedList.removeItemAt(sparseHeader.keyAt(i));

            }
        }
    }

    private void searchPosHeaders(){

        if(size()>0){

            sparseHeader.clear();

            for(int i=0;i<size();i++){

                if(isHeader(i)){
                    sparseHeader.append(i,((CustomHeader)getObject(i)).getTitle());
                    Log.d("Titles",((CustomHeader)getObject(i)).getTitle() + " POSITON: " + String.valueOf(i));

                }
            }

        }

    }

    public void setEmptyTitle(){

        removeAll();
        final CustomHeader mTitle = new CustomHeader(context.getString(R.string.emptyHeader), 0, recyclerView.getContext().getResources().getDrawable(R.drawable.ic_info_primary_dark_24dp),true);
        add(mTitle);
        sparseHeader.append(0,context.getString(R.string.emptyHeader));

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

    public abstract int getItemId(int position);

    protected  abstract boolean areItemsEquals(Object a1,Object b2);


    protected abstract int calculateItemPos(Object a1,Object a2);


    public abstract String getTitleForItem(Object obj);

    public   Drawable getTitleDrawable(Object obj){


        if(obj instanceof Category){

            return getContext().getDrawable(R.drawable.ic_folder_selector4);

        }else if(obj instanceof PreviewModelInfo){

            return getContext().getDrawable(R.drawable.ic_shoe_selector);


        }else if (obj instanceof CustomMaterial){


            return getContext().getDrawable(R.drawable.ic_leather_selector);

        }else if (obj instanceof Dealership) {


            return getContext().getDrawable(R.drawable.ic_person_selector);

        }else if (obj instanceof CutNote) {


            return getContext().getDrawable(R.drawable.ic_assignment_selector);
        }else if (obj instanceof Accessories) {


            return getContext().getDrawable(R.drawable.ic_more_selector);
        }else if (obj instanceof Piece) {


            return getContext().getDrawable(R.drawable.ic_leather_primary_dark_24dp);
        }





        return getContext().getDrawable(R.drawable.ic_menu_selector);


    };


    public static Comparator<Object> SENSITIVE_COMPARATOR = new Comparator<Object>() {
        public int compare(Object object1, Object object2) {
            int res = ((CustomHeader)object1).getTitle().compareTo (((CustomHeader)object2).getTitle());
            return res;
        }
    };

    public static Comparator<String> UpperLowerComparator = new Comparator<String>() {

        @Override
        public int compare(String lhs, String rhs) {


            boolean lhsStartsWithUpperLetter = Character.isUpperCase(lhs.charAt(0));
            boolean rhsStartsWithUpperLetter = Character.isUpperCase(rhs.charAt(0));


            char lhsChar = lhs.charAt(0);
            char rhsChar = lhs.charAt(0);

            if ((lhsStartsWithUpperLetter && rhsStartsWithUpperLetter) || (!lhsStartsWithUpperLetter && !rhsStartsWithUpperLetter)) {
                // they both start with letters or not-a-letters
                return lhs.compareTo(rhs);
            } else if (lhsStartsWithUpperLetter) {
                // the first string starts with letter and the second one is not
                return 1;
            } else {
                // the second string starts with letter and the first one is not
                return -1;
            }
        }
    };


}
