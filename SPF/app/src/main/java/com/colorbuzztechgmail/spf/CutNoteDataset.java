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

public class CutNoteDataset {


    private SparseArray<String> sparseHeader;
    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;
    private Context mContext;
    public enum SortType {
        STATUS,
        MODEL,
        LAST
    }


    SortedList<CutNote> sortedList = null;
    private SortType sortType = SortType.MODEL;

    public int getItemCount() {

        int count=0;
        for(int i=0;i<sparseHeader.size();i++){

            count=count+((CutNoteListTitle)getCutNote(sparseHeader.keyAt(i))).getCutNoteBuffer().size();
        }


        return (count+size())-sparseHeader.size();
    }

    public CutNoteDataset(final RecyclerView recyclerView, final RecyclerView.Adapter adapter) {

        this.adapter=adapter;
        this.sparseHeader=new SparseArray();
        this.recyclerView=recyclerView;
        this.mContext=recyclerView.getContext();
        int count =0;
        this.sortedList = new SortedList<>(CutNote.class,
                new SortedList.BatchedCallback<>(new SortedListAdapterCallback<CutNote>(adapter) {
                    @Override
                    public int compare(CutNote a1, CutNote a2) {

                      return calculateItemPos(a1,a2);
                    }

                    @Override
                    public boolean areContentsTheSame(CutNote oldItem, CutNote newItem) {
                        return oldItem.areContentsTheSame(newItem);
                    }

                    @Override
                    public boolean areItemsTheSame(CutNote item1, CutNote item2) {

                        if(isHeader(item1) && !isHeader(item2)){

                            return false;
                        }else if (!isHeader(item1) && isHeader(item2)){

                            return false;



                        }
                        return item1.areItemsTheSame(item2);
                    }

                    @Override
                    public void onInserted(int position, int count) {
                        super.onInserted(position, count);
                        searchPosHeaders();

                     }

                    @Override
                    public void onMoved(int fromPosition, int toPosition) {
                        super.onMoved(fromPosition, toPosition);
                     }

                    @Override
                    public void onChanged(int position, int count) {
                        super.onChanged(position, count);


                    }

                    @Override
                    public void onRemoved(int position, int count) {
                        super.onRemoved(position, count);
                        searchPosHeaders();

                    }
                }));
    }

    public  SortType getSortType() {
        return sortType;

    }

    public int getTitleCount() {
        return sparseHeader.size();
    }

    private Comparator<CutNote> getComparator() {
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

    public int size() {
        return sortedList.size();
    }

    public void add(CutNote cutNote) {

         sortedList.beginBatchedUpdates();

        sortedList.add(cutNote);
        sortedList.endBatchedUpdates();


    }

    public void add(List<CutNote> cutNotes) {
        sortedList.beginBatchedUpdates();


        for(int i=0;i<cutNotes.size();i++){
             sortedList.add(cutNotes.get(i));


        }
        sortedList.endBatchedUpdates();



    }

    public void remove(List<CutNote> cutNotes) {

        sortedList.beginBatchedUpdates();
        for (int i=0;i<sortedList.size();i++) {
            for (int j=0;j<cutNotes.size();j++){

                if(sortedList.get(i).equals(cutNotes.get(j))){

                    sortedList.remove(cutNotes.get(j));
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

    public void removeByIds(ArrayList<Integer> Ids) {

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

    public void remove(CutNote cutNote){
        sortedList.beginBatchedUpdates();
        sortedList.remove(cutNote);
         sortedList.endBatchedUpdates();




    }

    public void removeAll(){

        sortedList.beginBatchedUpdates();
        sortedList.clear();
         sortedList.endBatchedUpdates();


    }

    public void replaceAll(List<CutNote> cutNotes) {



        sortedList.beginBatchedUpdates();

         for (int i =sortedList.size() - 1; i >= 0; i--) {

            final CutNote cutNote =sortedList.get(i);
            if (!cutNotes.contains(cutNote)) {
                sortedList.remove(cutNote);
            }
        }
        sortedList.addAll(cutNotes);

        sortedList.endBatchedUpdates();



    }

    public void replaceItem(CutNote cutNote,int position) {



        sortedList.updateItemAt(position,cutNote);






    }

    public void replaceItem(CutNote cutNote) {

        int index=0;

        for(int i=0;i<size();i++){

            if(!isHeader(i)){

                if(getCutNote(i).getId()==cutNote.getId()){
                    index=i;
                    break;
                }

            }

        }

         sortedList.removeItemAt(index);
        add(cutNote);





    }

    public void changeSortType(SortType sortType) {
        if (!this.sortType.equals(sortType)) {
            this.sortType = sortType;
            fillDataset();
        }


    }

    public CutNote getCutNote(int position) {
        if(position!=RecyclerView.NO_POSITION){
            return sortedList.get(position);

        }
        return sortedList.get(0);
    }

    public CutNote getCutNoteById(int id) {
        if(size()>0){

            for(int i =0;i<size();i++){

                if(!isHeader(i)){

                    if(sortedList.get(i).getId()==id){


                        return sortedList.get(i);

                    }

                }

            }

        }
        return sortedList.get(0);
    }

    public static class CutNoteStatusComparator implements Comparator<CutNote> {
        @Override public int compare(CutNote first, CutNote second) {

            String value1= String.valueOf((first.getStatus()));
            String value2=String.valueOf((second.getStatus()));
           if(first instanceof CutNoteListTitle){
                value1=((CutNoteListTitle) first).getTitle();

            }

            if(second instanceof CutNoteListTitle){
                value2=((CutNoteListTitle) second).getTitle();

            }

            return value1.toLowerCase().compareTo(value2.toLowerCase());

        }
    }

    public static class CutNoteIdComparator implements Comparator<CutNote> {
        @Override public int compare(CutNote first, CutNote second) {

            String value1 =String.valueOf(first.getDate());
            String value2=String.valueOf(second.getDate());

            if(first instanceof CutNoteListTitle){
                value1=((CutNoteListTitle) first).getTitle();

            }

            if(second instanceof CutNoteListTitle){
                value2=((CutNoteListTitle) second).getTitle();

            }

            return value1.toLowerCase().compareTo(value2.toLowerCase());


        }
    }

    public static class CutNoteAlphabeticalComparator implements Comparator<CutNote> {
        @Override public int compare(CutNote first, CutNote second) {

            String value1=first.getModel();
            String value2=second.getModel();
            if(first instanceof CutNoteListTitle){
                value1=((CutNoteListTitle) first).getTitle();

            }

            if(second instanceof CutNoteListTitle){
                value2=((CutNoteListTitle) second).getTitle();

            }

            return value1.toLowerCase().compareTo(value2.toLowerCase());
        }
    }

    public static final CutNoteStatusComparator statusComparator=new CutNoteStatusComparator();

    public static final CutNoteIdComparator lastComparator=new CutNoteIdComparator();

    public static final CutNoteAlphabeticalComparator alphabeticalComparator=new CutNoteAlphabeticalComparator();

    private boolean isHeader(int position){

    return (getCutNote(position) instanceof CutNoteListTitle);
}

    private boolean isHeader(Object obj){

        return (obj instanceof CutNoteListTitle);
    }

    public int getHeaderPosition(String title){

    return sparseHeader.keyAt(sparseHeader.indexOfValue(title));
    }

    public void fillDataset(){

      List<CutNote> items = new ArrayList<>();
        for (int j = 0; j < sortedList.size(); j++) {
            if (!(sortedList.get(j) instanceof CutNoteListTitle)){
                items.add(sortedList.get(j));

            }else{
                if ((((CutNoteListTitle) sortedList.get(j))).getCutNoteBuffer().size() > 0) {

                    for (CutNote cutNote : (((CutNoteListTitle) sortedList.get(j))).getCutNoteBuffer()) {
                        items.add(cutNote);


                    }
                }


            }
        }

      removeAll();

        AutomaticTitleGeneratorSortType(items,true);

        recyclerView.scrollToPosition(0);
    }

    public void AutomaticTitleGeneratorSortType(List<CutNote> list,boolean collapse ) {



        final List<CutNote> headerList = new ArrayList<>();


        if (list.size() > 0) {

            Utils.IdMaker mIdMaker;
            mIdMaker=new Utils.IdMaker();
            final ArrayList<String> buffer = new ArrayList<>();

            switch (getSortType()) {

                case STATUS:

                    for (int i = 0; i < list.size(); i++) {
                        if (!(list.get(i) instanceof CutNoteListTitle)) {
                            final String status = convertStatusToString(list.get(i).getStatus());

                            boolean isPresent = false;
                            for (CutNote header : headerList) {

                                if (((CutNoteListTitle)header).getTitle().equals(status)) {
                                    ((CutNoteListTitle)header).addNote(list.get(i));

                                    isPresent = true;
                                }
                            }

                            if (!isPresent) {

                                final CutNoteListTitle mTitle = new CutNoteListTitle(status, mIdMaker.next(), null);
                                mTitle.addNote(list.get(i));
                                headerList.add(mTitle);

                            }
                        }

                    }

                    break;


                case MODEL:
                    for (int i = 0; i < list.size(); i++) {
                        if (!(list.get(i) instanceof CutNoteListTitle)) {
                            final String type = (list.get(i).getModel());

                            boolean isPresent = false;
                            for (CutNote header : headerList) {

                                if (((CutNoteListTitle)header).getTitle().equals(type)) {
                                    ((CutNoteListTitle)header).addNote(list.get(i));

                                    isPresent = true;
                                }
                            }

                            if (!isPresent) {

                                final CutNoteListTitle mTitle = new CutNoteListTitle(type, mIdMaker.next(), null);
                                mTitle.addNote(list.get(i));
                                headerList.add(mTitle);

                            }
                        }

                    }

                    break;

                case LAST:
                    for (int i = 0; i < list.size(); i++) {
                        if (!(list.get(i) instanceof CutNoteListTitle)){
                            final String date =(list.get(i).getDate().split(String.valueOf('\n'))[0]);
                            boolean isPresent = false;
                            for (CutNote header : headerList) {

                                if (((CutNoteListTitle)header).getTitle().equals(date)) {
                                    ((CutNoteListTitle)header).addNote(list.get(i));

                                    isPresent = true;
                                }
                            }

                            if (!isPresent) {

                                final CutNoteListTitle mTitle = new CutNoteListTitle(date, mIdMaker.next(), null);
                                mTitle.addNote(list.get(i));
                                headerList.add(mTitle);

                            }

                        }
                    }

                    break;

               

            }

            for (CutNote header : headerList) {
                if(!collapse) {
                    add(((CutNoteListTitle) header).getCutNoteList());
                    ((CutNoteListTitle) header).getCutNoteList().clear();
                    ((CutNoteListTitle) header).setExpanded(true);
                }
            }

            add(headerList);

        }else {

            setEmptyTitle();
        }

    }

    private int calculateItemPos(CutNote a1,CutNote a2){


        switch (sortType){

            case MODEL:

                    if (a1 instanceof CutNoteListTitle && (!(a2 instanceof CutNoteListTitle))) {

                        if (((CutNoteListTitle) a1).getTitle().equals(a2.getModel())) {
                            return  -1;

                        } else {

                            return  getComparator().compare(a1, a2);

                        }

                    } else if ((!(a1 instanceof CutNoteListTitle)) && a2 instanceof CutNoteListTitle) {

                        if (a1.getModel().equals(((CutNoteListTitle) a2).getTitle())) {
                            return 1;

                        } else {

                            return getComparator().compare(a1, a2);

                        }

                    }else if ((!(a1 instanceof CutNoteListTitle)) && (!(a2 instanceof CutNoteListTitle))) {

                            if (a1.getModel().equals(a2.getModel())) {
                                return alphabeticalComparator.compare(a1,a2);

                            } else {

                                return getComparator().compare(a1, a2);

                            }

                    }else {



                        return  getComparator().compare(a1, a2);
                    }




            case STATUS:


               if (a1 instanceof CutNoteListTitle && (!(a2 instanceof CutNoteListTitle))) {

                    if (((CutNoteListTitle) a1).getTitle().equals(convertStatusToString(a2.getStatus()))) {
                        return  -1;

                    } else {

                        return getComparator().compare(a1, a2);

                    }

                } else if ((!(a1 instanceof CutNoteListTitle)) && a2 instanceof CutNoteListTitle) {

                    if (convertStatusToString(a1.getStatus()).equals(((CutNoteListTitle) a2).getTitle())) {
                        return  1;

                    } else {

                        return  getComparator().compare(a1, a2);

                    }

                }else {

                    return  getComparator().compare(a1, a2);
                }


            case LAST:
                if (a1 instanceof CutNoteListTitle && (!(a2 instanceof CutNoteListTitle))) {

                    if (((CutNoteListTitle) a1).getTitle().contains(a2.getDate())) {
                        return  -1;

                    } else {

                        return getComparator().compare(a1, a2);

                    }

                } else if ((!(a1 instanceof CutNoteListTitle)) && a2 instanceof CutNoteListTitle) {

                    if (a1.getDate().contains(((CutNoteListTitle) a2).getTitle())) {
                        return  1;

                    } else {

                        return  getComparator().compare(a1, a2);

                    }

                }else if ((!(a1 instanceof CutNoteListTitle)) && (!(a2 instanceof CutNoteListTitle))) {

                    if (a1.getDate().contains(a2.getDate())) {
                        return statusComparator.compare(a1,a2);

                    } else {

                        return getComparator().compare(a1, a2);

                    }

                }else {

                    return  getComparator().compare(a1, a2);
                }


            default:

              return getComparator().compare(a1,a2);


        }




    }

    public void updateHeaderForNewItem(CutNote cutNote){

        boolean isPresent=false;

        if(sparseHeader.size()>0) {

            for (int i = 0; i < sparseHeader.size(); i++) {

                final String header = ((CutNoteListTitle) getCutNote(sparseHeader.keyAt(i))).getTitle();

                switch (getSortType()) {

                    case STATUS:
                        if (String.valueOf(cutNote.getStatus()).equals(header))
                            isPresent = true;

                        break;



                    case MODEL:

                        if (cutNote.getModel().equals(header))
                            isPresent = true;

                        break;

                    case LAST:

                        if (cutNote.getDate().contains(header))
                            isPresent = true;
                        break;
                        
                }

                if (isPresent) {
                    add(cutNote);
                    break;
                }
            }



            if(!isPresent) {

                final List<CutNote> cutNoteList = new ArrayList<>();

                cutNoteList.add(cutNote);

                AutomaticTitleGeneratorSortType(cutNoteList, false);
            }


            ArrayList<Integer>removeIndex=new ArrayList<>();

            for (int i =  sparseHeader.size()-1; i >=0; i--) {

                if(sparseHeader.keyAt(i)==size()-1){

                    if(((CutNoteListTitle)getCutNote(sparseHeader.keyAt(i))).isExpanded())
                        removeIndex.add(sparseHeader.keyAt(i));

                }else {


                    if(isHeader(sparseHeader.keyAt(i) +1)){
                        if(((CutNoteListTitle)getCutNote(sparseHeader.keyAt(i))).isExpanded()){

                            removeIndex.add(sparseHeader.keyAt(i));

                        }else {

                            if(((CutNoteListTitle)getCutNote(sparseHeader.keyAt(i))).getCutNoteBuffer().size()==0){

                                removeIndex.add(sparseHeader.keyAt(i));
                            }

                        }
                    }

                }


            }


            remove(removeIndex);



        }else{

            removeAll();

            final List<CutNote>cutNoteList=new ArrayList<>();

            cutNoteList.add(cutNote);

            AutomaticTitleGeneratorSortType(cutNoteList,false);

        }


    }

    private void searchPosHeaders(){

        if(size()>0){

            sparseHeader.clear();

            for(int i=0;i<size();i++){

                if(isHeader(i)){
                    sparseHeader.append(i,((CutNoteListTitle)getCutNote(i)).getTitle());

                }
            }

        }

    }

    public void setEmptyTitle(){

        removeAll();
        final CutNoteListTitle mTitle = new CutNoteListTitle("Categoria vacia", 0, recyclerView.getContext().getResources().getDrawable(R.drawable.ic_info_grey_24dp));
        add(mTitle);
       sparseHeader.append(0,"Categoria vacia");

    }

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
