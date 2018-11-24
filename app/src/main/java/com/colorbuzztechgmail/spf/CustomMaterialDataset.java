package com.colorbuzztechgmail.spf;

import android.support.v7.widget.RecyclerView;

import com.colorbuzztechgmail.spf.dataset.Dataset;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class CustomMaterialDataset extends Dataset {




    public enum SortType {
        NAME,
        TYPE,
        DEALER,
        SEASONS,
        LAST
    }

    private SortType sortType = SortType.NAME;

    public CustomMaterialDataset(RecyclerView recyclerView, RecyclerView.Adapter adapter) {
        super(recyclerView, adapter);
        setSortType(sortType.name());
    }

    
    public void setSortType(SortType sortType) {
         
        this.sortType=sortType;
    }


    @Override
    public String getSortType() {
        return sortType.name();
    }

    @Override
    public int getItemId(int position) {
        return getCustomMaterial(position).getId();
    }

    @Override
    protected boolean areItemsEquals(Object a1, Object b2) {
        int id1=0,id2=0;


        if(a1 instanceof CustomHeader && b2 instanceof CustomHeader){



            return  (((CustomHeader) a1).getId()==((CustomHeader) b2).getId());


        }else if ((a1 instanceof CustomMaterial) && (b2 instanceof CustomMaterial)) {



            return  (((CustomMaterial) a1).getId()==((CustomMaterial) b2).getId());



        }else if ((a1 instanceof ProgressItem) || (b2 instanceof ProgressItem)) {


            return false;
        }else {

            return false;
        }
    }




    @Override
    protected int calculateItemPos(Object a1, Object a2) {

        switch (sortType){

            case TYPE:

                if (a1 instanceof CustomHeader && (!(a2 instanceof CustomHeader))) {

                    if (((CustomHeader) a1).getTitle().equals(((CustomMaterial)a2).getType())) {
                        return  -1;

                    }

                } else if ((!(a1 instanceof CustomHeader)) && a2 instanceof CustomHeader) {

                    if (((CustomMaterial)a1).getType().equals(((CustomHeader) a2).getTitle())) {
                        return 1;

                    }

                }else if ((!(a1 instanceof CustomHeader)) && (!(a2 instanceof CustomHeader))) {

                    if (((CustomMaterial)a1).getType().equals(((CustomMaterial)a2).getType())) {
                        return ((CustomMaterial)a1).getName().compareToIgnoreCase(((CustomMaterial)a2).getName());

                    }

                }



                    return  getComparator().compare(a1, a2);



            case NAME:

            if (a1 instanceof CustomHeader && (!(a2 instanceof CustomHeader))) {

                if (((CustomHeader) a1).getTitle().equals(getTitleForItem(a2))) {
                    return  -1;

                }

            } else if ((!(a1 instanceof CustomHeader)) && a2 instanceof CustomHeader) {

                if (getTitleForItem(a1).equals(((CustomHeader) a2).getTitle())) {
                    return  1;

                }

            }else if ((!(a1 instanceof CustomHeader)) && (!(a2 instanceof CustomHeader))) {

                if (getTitleForItem(a1).equals(getTitleForItem(a2))) {
                    return  ((CustomMaterial)a1).getName().compareToIgnoreCase(((CustomMaterial)a2).getName());

                }

            }
            return  getComparator().compare(a1, a2);



            case LAST:
                if (a1 instanceof CustomHeader && (!(a2 instanceof CustomHeader))) {

                    if (((CustomHeader) a1).getTitle().contains(((CustomMaterial)a2).getDate())) {
                        return  -1;

                    }

                } else if ((!(a1 instanceof CustomHeader)) && a2 instanceof CustomHeader) {

                    if (((CustomMaterial)a1).getDate().contains(((CustomHeader) a2).getTitle())) {
                        return  1;

                    }

                }else if ((!(a1 instanceof CustomHeader)) && (!(a2 instanceof CustomHeader))) {

                    if (getTitleForItem(a1).equals(getTitleForItem(a2))) {
                        return (Integer.compare(((CustomMaterial)a1).getId(),((CustomMaterial)a2).getId()));


                    }

                }

                    return  getComparator().compare(a1, a2);


            case DEALER:

                if (a1 instanceof CustomHeader && (!(a2 instanceof CustomHeader))) {

                    if (((CustomHeader) a1).getTitle().equals(getTitleForItem(a2))) {
                        return  -1;

                    }

                } else if ((!(a1 instanceof CustomHeader)) && a2 instanceof CustomHeader) {

                    if (getTitleForItem(a1).equals(((CustomHeader) a2).getTitle())) {
                        return 1;

                    }
                }else if ((!(a1 instanceof CustomHeader)) && (!(a2 instanceof CustomHeader))) {

                    if (getTitleForItem(a1).equals(getTitleForItem(a2))) {
                        return  ((CustomMaterial)a1).getName().compareToIgnoreCase(((CustomMaterial)a2).getName());


                    }

                }

                    return  getComparator().compare(a1, a2);


            case SEASONS:

                if (a1 instanceof CustomHeader && (!(a2 instanceof CustomHeader))) {

                    if (((CustomHeader) a1).getTitle().equals(((CustomMaterial)a2).getSeasons())) {
                        return  -1;

                    }

                } else if ((!(a1 instanceof CustomHeader)) && a2 instanceof CustomHeader) {

                    if (((CustomMaterial)a1).getSeasons().equals(((CustomHeader) a2).getTitle())) {
                        return 1;

                    }
                }else if ((!(a1 instanceof CustomHeader)) && (!(a2 instanceof CustomHeader))) {

                    if (getTitleForItem(a1).equals(getTitleForItem(a2))) {
                        return  ((CustomMaterial)a1).getName().compareToIgnoreCase(((CustomMaterial)a2).getName());


                    }

                }

                    return  getComparator().compare(a1, a2);


            default:

                return getComparator().compare(a1,a2);


        }
    }



    @Override
    public String getTitleForItem(Object obj) {

        String title=null;

        if(isHeader(obj)){

            return ((CustomHeader)obj).getTitle();

        }else if(obj instanceof ProgressItem){


            return ((ProgressItem)obj).TAG;


        }else {

            switch (sortType) {

                case NAME:

                    String vocalName = ((CustomMaterial) obj).getName().substring(0, 1).toUpperCase();
                    if (!Character.isAlphabetic(Character.valueOf(vocalName.charAt(0)))) {

                        vocalName = "#";
                    }


                    return vocalName;

                case TYPE:

                    return ((CustomMaterial) obj).getType();


                case LAST:

                    return ((CustomMaterial) obj).getDate().split(String.valueOf('\n'))[0];


                case DEALER:

                    return ((CustomMaterial) obj).getDealership();


                case SEASONS:

                    return ((CustomMaterial) obj).getSeasons();


            }

            return title;
        }
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

    public CustomMaterial getCustomMaterialById(long id) {
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

    public void removeByIds(ArrayList<Long> Ids) {

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
    
    public  class CustomMaterialNameComparator implements Comparator<Object> {
        @Override public int compare(Object first, Object second) {
            return getTitleForItem(first).compareToIgnoreCase(getTitleForItem(second));
        }
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

            return value1.compareTo(value2);        }


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

            return value1.compareTo(value2);        }
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

    public  final CustomMaterialNameComparator nameComparator=new CustomMaterialNameComparator();
    public static final CustomMaterialTypeComparator typeComparator=new CustomMaterialTypeComparator();
    public static final CustomMaterialDealerComparator delaerComparator=new CustomMaterialDealerComparator();
    public static final CustomMaterialIdComparator lastComparator=new CustomMaterialIdComparator();
    public static final CustomMaterialSeasonsComparator seasonsComparator=new CustomMaterialSeasonsComparator();





}