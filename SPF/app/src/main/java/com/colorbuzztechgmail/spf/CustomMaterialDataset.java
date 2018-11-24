package com.colorbuzztechgmail.spf;

import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.util.SortedListAdapterCallback;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CustomMaterialDataset extends Dataset {




    public enum SortType {
        NAME,
        TYPE,
        DEALER,
        SEASONS,
        LAST
    }

    private SortType sortType = SortType.TYPE;

    public CustomMaterialDataset(RecyclerView recyclerView, RecyclerView.Adapter adapter) {
        super(recyclerView, adapter);
        setSortType(sortType.name());
    }

    
    public void setSortType(SortType sortType) {
         
        this.sortType=sortType;
    }




    @Override
    protected String getSortType() {
        return sortType.name();
    }

    @Override
    protected int getItemId(int position) {
        return getCustomMaterial(position).getId();
    }

    @Override
    protected boolean areItemsEquals(Object a1, Object b2) {
        int id1=0,id2=0;

        if(a1 instanceof CustomHeader){

            id1=((CustomHeader)a1).getId();

        }else{
            id1=((CustomMaterial)a1).getId();


        }

        if(b2 instanceof CustomHeader){

            id2=((CustomHeader)b2).getId();

        }else{
            id2=((CustomMaterial)b2).getId();


        }

        return (id1==id2);
    }

    @Override
    protected void AutomaticTitleGeneratorSortType(List<Object> list, boolean collapse) {

        final List<Object> headerList = new ArrayList<>();


        if (list.size() > 0) {

            Utils.IdMaker mIdMaker;
            mIdMaker=new Utils.IdMaker();
 
            switch (sortType) {

                case NAME:

                    for (int i = 0; i < list.size(); i++) {
                        if (!(list.get(i) instanceof CustomHeader)){
                            String vocalName =((CustomMaterial) list.get(i)).getName().substring(0,1).toUpperCase();
                            if(!Character.isAlphabetic(Character.valueOf(vocalName.charAt(0)))){

                                vocalName="#";
                            }

                            boolean isPresent=false;
                            for(Object header: headerList){

                                if(((CustomHeader)header).getTitle().equals(vocalName)) {
                                    ((CustomHeader)header).addObject(list.get(i));

                                    isPresent = true;
                                }
                            }

                            if(!isPresent){

                                final CustomHeader mTitle = new CustomHeader(vocalName, mIdMaker.next(), null);
                                mTitle.addObject(list.get(i));
                                headerList.add(mTitle);

                            }
                        }
                    }
                    break;

                case TYPE:
                    for (int i = 0; i < list.size(); i++) {
                        if (!(list.get(i) instanceof CustomHeader)) {
                            final String type = ((CustomMaterial)list.get(i)).getType();

                            boolean isPresent = false;
                            for (Object header : headerList) {

                                if (((CustomHeader)header).getTitle().equals(type)) {
                                    ((CustomHeader)header).addObject(list.get(i));

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
                        if (!(list.get(i) instanceof CustomHeader)){
                            final String date =((CustomMaterial) list.get(i)).getDate().split(String.valueOf('\n'))[0];
                            boolean isPresent = false;
                            for (Object header : headerList) {

                                if (((CustomHeader)header).getTitle().equals(date)) {
                                    ((CustomHeader)header).addObject(list.get(i));

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

                case DEALER:
                    for (int i = 0; i < list.size(); i++) {
                        if (!(list.get(i) instanceof CustomHeader)){
                            final String dealer=((CustomMaterial)list.get(i)).getDealership();
                            boolean isPresent = false;
                            for (Object header : headerList) {

                                if (((CustomHeader)header).getTitle().equals(dealer)) {
                                    ((CustomHeader)header).addObject(list.get(i));

                                    isPresent = true;
                                }
                            }

                            if (!isPresent) {

                                final CustomHeader mTitle = new CustomHeader(dealer, mIdMaker.next(), null);
                                mTitle.addObject(list.get(i));

                                headerList.add(mTitle);

                            }
                        }
                    }

                    break;

                case SEASONS:
                    for (int i = 0; i < list.size(); i++) {
                        if (!(list.get(i) instanceof CustomHeader)){

                            final String seasons=((CustomMaterial)list.get(i)).getSeasons();
                            boolean isPresent = false;
                            for (Object header : headerList) {

                                if (((CustomHeader)header).getTitle().equals(seasons)) {

                                    ((CustomHeader)header).addObject(list.get(i));

                                    isPresent=true;
                                }
                            }

                            if (!isPresent) {

                                final CustomHeader mTitle = new CustomHeader(seasons, mIdMaker.next(), null);
                                mTitle.addObject(list.get(i));

                                headerList.add(mTitle);

                            }

                        }
                    }
                    break;

            }
            if(!collapse) {

                for (Object header : headerList) {

                    addAll(((CustomHeader) header).getBuffer());
                    ((CustomHeader) header).removeAll();
                    ((CustomHeader) header).setExpanded(true);
                }
            }

            addAll(headerList);

        }else {

            setEmptyTitle();
        }

    }



    @Override
    protected int calculateItemPos(Object a1, Object a2) {

        switch (sortType){

            case TYPE:

                if (a1 instanceof CustomHeader && (!(a2 instanceof CustomHeader))) {

                    if (((CustomHeader) a1).getTitle().equals(((CustomMaterial)a2).getType())) {
                        return  -1;

                    } else {

                        return  getComparator().compare(a1, a2);

                    }

                } else if ((!(a1 instanceof CustomHeader)) && a2 instanceof CustomHeader) {

                    if (((CustomMaterial)a1).getType().equals(((CustomHeader) a2).getTitle())) {
                        return 1;

                    } else {

                        return getComparator().compare(a1, a2);

                    }

                }else if ((!(a1 instanceof CustomHeader)) && (!(a2 instanceof CustomHeader))) {

                    if (((CustomMaterial)a1).getType().equals(((CustomMaterial)a2).getType())) {
                        return nameComparator.compare(a1,a2);

                    } else {

                        return getComparator().compare(a1, a2);

                    }

                }else {



                    return  getComparator().compare(a1, a2);
                }




            case NAME:


                if (a1 instanceof CustomHeader && (!(a2 instanceof CustomHeader))) {

                    if (((CustomHeader) a1).getTitle().toLowerCase().equals(((CustomMaterial)a2).getName().toLowerCase())) {
                        return  -1;

                    } else {

                        return getComparator().compare(a1, a2);

                    }

                } else if ((!(a1 instanceof CustomHeader)) && a2 instanceof CustomHeader) {

                    if (((CustomMaterial)a1).getName().toLowerCase().equals(((CustomHeader) a2).getTitle().toLowerCase())) {
                        return  1;

                    } else {

                        return  getComparator().compare(a1, a2);

                    }

                }else {

                    return  getComparator().compare(a1, a2);
                }


            case LAST:
                if (a1 instanceof CustomHeader && (!(a2 instanceof CustomHeader))) {

                    if (((CustomHeader) a1).getTitle().contains(((CustomMaterial)a2).getDate())) {
                        return  -1;

                    } else {

                        return getComparator().compare(a1, a2);

                    }

                } else if ((!(a1 instanceof CustomHeader)) && a2 instanceof CustomHeader) {

                    if (((CustomMaterial)a1).getDate().contains(((CustomHeader) a2).getTitle())) {
                        return  1;

                    } else {

                        return  getComparator().compare(a1, a2);

                    }

                }else if ((!(a1 instanceof CustomHeader)) && (!(a2 instanceof CustomHeader))) {

                    if (((CustomMaterial)a1).getDate().contains(((CustomMaterial)a2).getDate())) {
                        return nameComparator.compare(a1,a2);

                    } else {

                        return getComparator().compare(a1, a2);

                    }

                }else {

                    return  getComparator().compare(a1, a2);
                }

            case DEALER:

                if (a1 instanceof CustomHeader && (!(a2 instanceof CustomHeader))) {

                    if (((CustomHeader) a1).getTitle().equals(((CustomMaterial)a2).getDealership())) {
                        return  -1;

                    } else {

                        return  getComparator().compare(a1, a2);

                    }

                } else if ((!(a1 instanceof CustomHeader)) && a2 instanceof CustomHeader) {

                    if (((CustomMaterial)a1).getDealership().equals(((CustomHeader) a2).getTitle())) {
                        return 1;

                    } else {

                        return getComparator().compare(a1, a2);

                    }
                }

            case SEASONS:

                if (a1 instanceof CustomHeader && (!(a2 instanceof CustomHeader))) {

                    if (((CustomHeader) a1).getTitle().equals(((CustomMaterial)a2).getSeasons())) {
                        return  -1;

                    } else {

                        return  getComparator().compare(a1, a2);

                    }

                } else if ((!(a1 instanceof CustomHeader)) && a2 instanceof CustomHeader) {

                    if (((CustomMaterial)a1).getSeasons().equals(((CustomHeader) a2).getTitle())) {
                        return 1;

                    } else {

                        return getComparator().compare(a1, a2);

                    }
                }

            default:

                return getComparator().compare(a1,a2);


        }
    }

    @Override
    protected boolean isItemHaveHeader(Object obj) {


        CustomMaterial material=(CustomMaterial)obj;

        boolean isPresent=false;

        if(sparseHeader.size()>0) {

            for (int i = 0; i < sparseHeader.size(); i++) {

                final String header = ((CustomHeader) getObject(sparseHeader.keyAt(i))).getTitle();

                switch (sortType) {

                    case NAME:
                        String vocalName = material.getName();
                        if (!Character.isAlphabetic(Character.valueOf(vocalName.charAt(0)))) {

                            if (header.equals("#")) {

                                isPresent = true;
                            }
                        } else {

                            if (vocalName.equals(header)) {

                                isPresent = true;
                            }


                        }

                        break;

                    case TYPE:

                        if (material.getType().equals(header))
                            isPresent = true;

                        break;
                    case DEALER:
                        if (material.getDealership().equals(header))
                            isPresent = true;
                        break;

                    case LAST:

                        if (material.getDate().contains(header))
                            isPresent = true;
                        break;

                    case SEASONS:

                        if (material.getSeasons().equals(header)) {

                            isPresent = true;

                        }
                        break;

                        default:
                            return false;
                }

            }
        }
        return isPresent;
    }


    

    private Comparator<Object> getComparator() {
        switch (sortType) {
            case NAME:
                return nameComparator;
            case TYPE:
                return typeComparator;
            case DEALER:
                return delaerComparator;
            case LAST:
                return lastComparator;

            case SEASONS:
                return seasonsComparator;

                default:

                    return nameComparator;
        }

    }

    public CustomMaterial getCustomMaterial(int position) {
        if(position!=RecyclerView.NO_POSITION) {
            return (CustomMaterial) sortedList.get(position);
        }

        return (CustomMaterial) sortedList.get(0);
    }

    public CustomMaterial getCustomMaterialById(int id) {
        if(size()>0){

            for(int i =0;i<size();i++){

                if(!isHeader(i)){

                    if(((CustomMaterial)sortedList.get(i)).getId()==id){


                        return (CustomMaterial) sortedList.get(i);

                    }

                }

            }

        }
        return (CustomMaterial) sortedList.get(0);
    }

    public void removeByIds(ArrayList<Integer> Ids) {

        Collections.sort(Ids);

        sortedList.beginBatchedUpdates();

        for(int i =size()-1;i>=0;i--){

            if(Ids.size()>0){

                for(int j=Ids.size()-1;j>=0;j--){

                    if(!isHeader(i)){

                        if(getCustomMaterial(i).getId()==Ids.get(j)){

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


    public static class CustomMaterialSeasonsComparator implements Comparator<Object> {
        @Override public int compare(Object first, Object second) {
            String value1,value2;
      

            if(first instanceof CustomHeader){
                value1=((CustomHeader) first).getTitle();

            }else{
                value1=((CustomMaterial) first).getSeasons();


            }

            if(second instanceof CustomHeader){
                value2=((CustomHeader) second).getTitle();

            }else{
                value2=((CustomMaterial) second).getSeasons();


            }

            return value1.toLowerCase().compareTo(value2.toLowerCase());



        }
    }
    
    public static class CustomMaterialNameComparator implements Comparator<Object> {
        @Override public int compare(Object first, Object second) {
            String value1,value2;
            

            if(first instanceof CustomHeader){
                value1=((CustomHeader) first).getTitle();

            }else{
                
                value1=((CustomMaterial)first).getName();
            }

            if(second instanceof CustomHeader){
                value2=((CustomHeader) second).getTitle();

            }else{

                value2=((CustomMaterial)second).getName();
            }

            return value1.toLowerCase().compareTo(value2.toLowerCase());        }
    }
    
    public static class CustomMaterialTypeComparator implements Comparator<Object> {
        @Override public int compare(Object first, Object second) {
            String value1,value2;
            

            if(first instanceof CustomHeader){
                value1=((CustomHeader) first).getTitle();

            }else{
                value1=((CustomMaterial) first).getType();



            }

            if(second instanceof CustomHeader){
                value2=((CustomHeader) second).getTitle();

            }else{
                value2=((CustomMaterial)second).getType();



            }

            return value1.toLowerCase().compareTo(value2.toLowerCase());        }


    }
    
    public static class CustomMaterialDealerComparator implements Comparator<Object> {
        @Override public int compare(Object first, Object second) {
            String value1,value2;
         

            if(first instanceof CustomHeader){
                value1=((CustomHeader) first).getTitle();

            }else{

                value1=((CustomMaterial) first).getDealership();


            }

            if(second instanceof CustomHeader){
                value2=((CustomHeader) second).getTitle();

            }else{

                value2=((CustomMaterial) second).getDealership();


            }

            return value1.toLowerCase().compareTo(value2.toLowerCase());        }
    }
    
    public static class CustomMaterialIdComparator implements Comparator<Object> {
        @Override public int compare(Object first, Object second) {
            String value1,value2;
             

            if(first instanceof CustomHeader){
                value1=((CustomHeader) first).getTitle();

            }else{

                value1=((CustomMaterial) first).getDate();
            }

            if(second instanceof CustomHeader){
                value2=((CustomHeader) second).getTitle();

            }else{

                value2=((CustomMaterial)second).getDate();
            }

            return value1.toLowerCase().compareTo(value2.toLowerCase());



        }
    }

    public static final CustomMaterialNameComparator nameComparator=new CustomMaterialNameComparator();
    public static final CustomMaterialTypeComparator typeComparator=new CustomMaterialTypeComparator();
    public static final CustomMaterialDealerComparator delaerComparator=new CustomMaterialDealerComparator();
    public static final CustomMaterialIdComparator lastComparator=new CustomMaterialIdComparator();
    public static final CustomMaterialSeasonsComparator seasonsComparator=new CustomMaterialSeasonsComparator();


/*

    public void AutomaticTitleGeneratorSortType(List<CustomMaterial> list,boolean collapse ) {



      final List<CustomMaterial> headerList = new ArrayList<>();


        if (list.size() > 0) {

            Utils.IdMaker mIdMaker;
            mIdMaker=new Utils.IdMaker();
            final ArrayList<String> buffer = new ArrayList<>();

            switch (getSortType()) {

                case NAME:

                    for (int i = 0; i < list.size(); i++) {
                        if (!(list.get(i) instanceof CustomHeader)){
                            String vocalName =list.get(i).getName().substring(0,1).toUpperCase();
                            if(!Character.isAlphabetic(Character.valueOf(vocalName.charAt(0)))){

                                vocalName="#";
                            }

                                boolean isPresent=false;
                                for(CustomMaterial header: headerList){

                                    if(((CustomHeader)header).getTitle().equals(vocalName)) {
                                        ((CustomHeader)header).addObject(list.get(i));

                                        isPresent = true;
                                    }
                                }

                                if(!isPresent){

                                    final CustomHeader mTitle = new CustomHeader(vocalName, mIdMaker.next(), null);
                                    mTitle.addObject(list.get(i));
                                    headerList.add(mTitle);

                                }
                            }
                        }
                    break;

                case TYPE:
                    for (int i = 0; i < list.size(); i++) {
                        if (!(list.get(i) instanceof CustomHeader)) {
                            final String type = (list.get(i).getType());

                             boolean isPresent = false;
                                for (CustomMaterial header : headerList) {

                                    if (((CustomHeader)header).getTitle().equals(type)) {
                                        ((CustomHeader)header).addObject(list.get(i));

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
                        if (!(list.get(i) instanceof CustomHeader)){
                            final String date =(list.get(i).getDate().split(String.valueOf('\n'))[0]);
                            boolean isPresent = false;
                                for (CustomMaterial header : headerList) {

                                    if (((CustomHeader)header).getTitle().equals(date)) {
                                        ((CustomHeader)header).addObject(list.get(i));

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

                case DEALER:
                    for (int i = 0; i < list.size(); i++) {
                        if (!(list.get(i) instanceof CustomHeader)){
                           final String dealer=(list.get(i).getDealership());
                           boolean isPresent = false;
                                for (CustomMaterial header : headerList) {

                                    if (((CustomHeader)header).getTitle().equals(dealer)) {
                                        ((CustomHeader)header).addObject(list.get(i));

                                        isPresent = true;
                                    }
                                }

                                if (!isPresent) {

                                    final CustomHeader mTitle = new CustomHeader(dealer, mIdMaker.next(), null);
                                    mTitle.addObject(list.get(i));

                                    headerList.add(mTitle);

                                }
                            }
                        }

                    break;

                case SEASONS:
                    for (int i = 0; i < list.size(); i++) {
                        if (!(list.get(i) instanceof CustomHeader)){

                            final String seasons=list.get(i).getSeasons();
                             boolean isPresent = false;
                                for (CustomMaterial header : headerList) {

                                    if (((CustomHeader)header).getTitle().equals(seasons)) {

                                        ((CustomHeader)header).addObject(list.get(i));

                                        isPresent=true;
                                    }
                                }

                                if (!isPresent) {

                                    final CustomHeader mTitle = new CustomHeader(seasons, mIdMaker.next(), null);
                                    mTitle.addObject(list.get(i));

                                    headerList.add(mTitle);

                                }

                            }
                        }
                        break;

            }

            for (CustomMaterial header : headerList) {
                if(!collapse) {
                    add(((CustomHeader) header).getMaterialBuffer());
                    ((CustomHeader) header).getMaterialBuffer().clear();
                    ((CustomHeader) header).setExpanded(true);
                }
            }

            add(headerList);

        }else {

            setEmptyTitle();
        }

    }

    private int calculateItemPos(CustomMaterial a1,CustomMaterial a2){


        switch (getSortType()){

            case TYPE:

                if (a1 instanceof CustomHeader && (!(a2 instanceof CustomHeader))) {

                    if (((CustomHeader) a1).getTitle().equals(a2.getType())) {
                        return  -1;

                    } else {

                        return  getComparator().compare(a1, a2);

                    }

                } else if ((!(a1 instanceof CustomHeader)) && a2 instanceof CustomHeader) {

                    if (a1.getType().equals(((CustomHeader) a2).getTitle())) {
                        return 1;

                    } else {

                        return getComparator().compare(a1, a2);

                    }

                }else if ((!(a1 instanceof CustomHeader)) && (!(a2 instanceof CustomHeader))) {

                    if (a1.getType().equals(a2.getType())) {
                        return nameComparator.compare(a1,a2);

                    } else {

                        return getComparator().compare(a1, a2);

                    }

                }else {



                    return  getComparator().compare(a1, a2);
                }




            case NAME:


                if (a1 instanceof CustomHeader && (!(a2 instanceof CustomHeader))) {

                    if (((CustomHeader) a1).getTitle().toLowerCase().equals(a2.getName().toLowerCase())) {
                        return  -1;

                    } else {

                        return getComparator().compare(a1, a2);

                    }

                } else if ((!(a1 instanceof CustomHeader)) && a2 instanceof CustomHeader) {

                    if (a1.getName().toLowerCase().equals(((CustomHeader) a2).getTitle().toLowerCase())) {
                        return  1;

                    } else {

                        return  getComparator().compare(a1, a2);

                    }

                }else {

                    return  getComparator().compare(a1, a2);
                }


            case LAST:
                if (a1 instanceof CustomHeader && (!(a2 instanceof CustomHeader))) {

                    if (((CustomHeader) a1).getTitle().contains(a2.getDate())) {
                        return  -1;

                    } else {

                        return getComparator().compare(a1, a2);

                    }

                } else if ((!(a1 instanceof CustomHeader)) && a2 instanceof CustomHeader) {

                    if (a1.getDate().contains(((CustomHeader) a2).getTitle())) {
                        return  1;

                    } else {

                        return  getComparator().compare(a1, a2);

                    }

                }else if ((!(a1 instanceof CustomHeader)) && (!(a2 instanceof CustomHeader))) {

                    if (a1.getDate().contains(a2.getDate())) {
                        return nameComparator.compare(a1,a2);

                    } else {

                        return getComparator().compare(a1, a2);

                    }

                }else {

                    return  getComparator().compare(a1, a2);
                }

            case DEALER:

                if (a1 instanceof CustomHeader && (!(a2 instanceof CustomHeader))) {

                    if (((CustomHeader) a1).getTitle().equals(a2.getDealership())) {
                        return  -1;

                    } else {

                        return  getComparator().compare(a1, a2);

                    }

                } else if ((!(a1 instanceof CustomHeader)) && a2 instanceof CustomHeader) {

                    if (a1.getDealership().equals(((CustomHeader) a2).getTitle())) {
                        return 1;

                    } else {

                        return getComparator().compare(a1, a2);

                    }
                }

            case SEASONS:

                if (a1 instanceof CustomHeader && (!(a2 instanceof CustomHeader))) {

                    if (((CustomHeader) a1).getTitle().equals(a2.getSeasons())) {
                        return  -1;

                    } else {

                        return  getComparator().compare(a1, a2);

                    }

                } else if ((!(a1 instanceof CustomHeader)) && a2 instanceof CustomHeader) {

                    if (a1.getSeasons().equals(((CustomHeader) a2).getTitle())) {
                        return 1;

                    } else {

                        return getComparator().compare(a1, a2);

                    }
                }
                
            default:

                return getComparator().compare(a1,a2);


        }




    }*/






}