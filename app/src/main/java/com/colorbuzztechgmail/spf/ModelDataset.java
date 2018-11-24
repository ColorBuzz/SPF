package com.colorbuzztechgmail.spf;

import android.support.v7.widget.RecyclerView;

import com.colorbuzztechgmail.spf.dataset.Dataset;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static com.colorbuzztechgmail.spf.ModelDataset.ModelSortType.DIRECTORY;
import static com.colorbuzztechgmail.spf.ModelDataset.ModelSortType.NAME;

public class ModelDataset extends Dataset {


     public enum ModelSortType {
        NAME,
        CUSTUMER,
        DIRECTORY,
        LAST
    }
    private ModelSortType sortType=NAME;

    public ModelDataset(RecyclerView recyclerView, RecyclerView.Adapter adapter) {
        super(recyclerView, adapter);

        setSortType(sortType.name());
    }

    public void setSortType(ModelSortType sortType){

        this.sortType=sortType;
    }

    @Override
    public String getSortType() {
        return this.sortType.name();
    }

    @Override
    public int getItemId(int position) {
        return Utils.toIntExact(getPreviewModelInfo(position).getId());
    }

    @Override
    protected boolean areItemsEquals(Object a1,Object b2){





        if(a1 instanceof CustomHeader && b2 instanceof CustomHeader){



            return  (((CustomHeader) a1).getId()==((CustomHeader) b2).getId());


        }else if ((a1 instanceof PreviewModelInfo) && (b2 instanceof PreviewModelInfo)) {



            return  (((PreviewModelInfo) a1).getId()==((PreviewModelInfo) b2).getId());



        }else if ((a1 instanceof ProgressItem) || (b2 instanceof ProgressItem)) {


            return false;
        }else {

            return false;
        }
    };

    @Override
    protected int calculateItemPos(Object a1, Object a2) {
        
        switch (sortType) {

            case DIRECTORY:

                if (a1 instanceof CustomHeader && (!(a2 instanceof CustomHeader))) {

                    if (((CustomHeader) a1).getTitle().equals(getTitleForItem(a2))) {
                        return -1;

                    }

                } else if ((!(a1 instanceof CustomHeader)) && a2 instanceof CustomHeader) {

                    if (getTitleForItem(a1).equals(((CustomHeader) a2).getTitle())) {
                        return 1;

                    }

                }else if ((!(a1 instanceof CustomHeader)) && (!(a2 instanceof CustomHeader)) ){

                    if (getTitleForItem(a1).equals(getTitleForItem(a2))) {
                        return ((PreviewModelInfo)a1).getName().compareToIgnoreCase(((PreviewModelInfo)a2).getName());

                    }

                }


                return UpperLowerComparator.compare(getTitleForItem(a1),getTitleForItem(a2));


            case CUSTUMER:

                if (a1 instanceof CustomHeader && (!(a2 instanceof CustomHeader))) {

                    if (((CustomHeader) a1).getTitle().equals(getTitleForItem(a2))) {
                        return -1;

                    }

                } else if ((!(a1 instanceof CustomHeader)) && a2 instanceof CustomHeader) {

                    if (getTitleForItem(a1).equals(((CustomHeader) a2).getTitle())) {
                        return 1;

                    }

                }else if ((!(a1 instanceof CustomHeader)) && (!(a2 instanceof CustomHeader)) ){

                    if (getTitleForItem(a1).equals(getTitleForItem(a2))) {
                        return ((PreviewModelInfo)a1).getName().compareToIgnoreCase(((PreviewModelInfo)a2).getName());

                    }

                }


                return UpperLowerComparator.compare(getTitleForItem(a1),getTitleForItem(a2));


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
                        return   ((PreviewModelInfo)a1).getName().compareToIgnoreCase(((PreviewModelInfo)a2).getName());

                    }

                }
                return  getComparator().compare(a1, a2);



            case LAST:
                if (a1 instanceof CustomHeader && (!(a2 instanceof CustomHeader))) {

                    if (((CustomHeader) a1).getTitle().contains(((PreviewModelInfo)a2).getDate())) {
                        return -1;

                    }



                } else if ((!(a1 instanceof CustomHeader)) && a2 instanceof CustomHeader) {

                    if (((PreviewModelInfo)a1).getDate().contains(((CustomHeader) a2).getTitle())) {
                        return 1;

                    }

                } else if ((!(a1 instanceof CustomHeader)) && (!(a2 instanceof CustomHeader))) {

                    if (getTitleForItem(a1).equals(getTitleForItem(a2))) {

                        int i=(Long.compare(((PreviewModelInfo)a1).getId(),((PreviewModelInfo)a2).getId()));

                        return i;


                    }



                }

                    return getComparator().compare(a1, a2);



            default:

                return getComparator().compare(a1, a2);


        }
    }


    @Override
     public String getTitleForItem(Object obj) {


        if(isHeader(obj)) {

            return ((CustomHeader) obj).getTitle();


        }else if(obj instanceof ProgressItem){


            return ((ProgressItem)obj).TAG;


        }else {

            switch (sortType) {

                case NAME:


                    String vocalName = ((PreviewModelInfo) obj).getName().substring(0, 1).toUpperCase();
                    if (!Character.isAlphabetic(Character.valueOf(vocalName.charAt(0)))) {

                        vocalName = "#";
                    }

                    return vocalName;
                case DIRECTORY:

                    return ((PreviewModelInfo) obj).getDirectory();

                case CUSTUMER:

                    return ((PreviewModelInfo) obj).getCustumer();


                case LAST:

                    return ((PreviewModelInfo) obj).getDate().split(String.valueOf('\n'))[0];

            }
        }

        return null;
    }

    public void removeByIds(ArrayList<Long> Ids) {

        Collections.sort(Ids);

        sortedList.beginBatchedUpdates();

        for(int i =size()-1;i>=0;i--){

            if(Ids.size()>0){

                for(int j=Ids.size()-1;j>=0;j--){

                    if(!isHeader(i)){

                        if(((PreviewModelInfo)getObject(i)).getId()==Ids.get(j)){

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

    public PreviewModelInfo getPreviewModelInfo(int position) {
        if(position!=RecyclerView.NO_POSITION){
            return (PreviewModelInfo)getObject(position);

        }
        return (PreviewModelInfo)getObject(0);
    }

    public PreviewModelInfo getPreviewModelInfoById(long id) {
        if(size()>0){

            for(int i =0;i<size();i++){

                if(!isHeader(i)){

                    if(((PreviewModelInfo)(Object)sortedList.get(i)).getId()==id){


                        return ((PreviewModelInfo)(Object)sortedList.get(i));

                    }

                }

            }

        }
        return ((PreviewModelInfo)(Object)sortedList.get(0));
    }

    private Comparator<Object> getComparator() {
        switch (sortType) {
            case NAME:
                return nameComparator;
            case LAST:
                return lastComparator;

            case DIRECTORY:
                return categoryComparator;

            case CUSTUMER:

                return nameComparator;
            default:

                return nameComparator;
        }

    }

    public class PreviewModelInfoNameComparator implements Comparator<Object> {
        @Override
        public int compare(Object first, Object second) {


            return getTitleForItem(first).compareToIgnoreCase(getTitleForItem(second));

        }
    }

    public static class PreviewModelInfoIdComparator implements Comparator<Object> {
        @Override
        public int compare(Object first, Object second) {

            String value1 ,value2;


            if (first instanceof CustomHeader) {
                value1 = ((CustomHeader) first).getTitle();

            }else{
                value1 = ((PreviewModelInfo) first).getDate();


            }

            if (second instanceof CustomHeader) {
                value2 = ((CustomHeader) second).getTitle();

            }else{

                value2= ((PreviewModelInfo) second).getDate();

            }

            return value1 .compareTo(value2);


        }
    }

    public static class PreviewModelInfoCategoryomparator implements Comparator<Object> {
        @Override
        public int compare(Object first, Object second) {
            String value1,value2;

            if (first instanceof CustomHeader) {
                value1 = ((CustomHeader) first).getTitle();

            }else{
                value1 = ((PreviewModelInfo )first).getDirectory();


            }

            if (second instanceof CustomHeader) {
                value2 = ((CustomHeader) second).getTitle();

            }else{
                value2 = ((PreviewModelInfo )second).getDirectory();



            }

            return value1.compareTo(value2);

        }
    }

    public  final PreviewModelInfoNameComparator nameComparator = new PreviewModelInfoNameComparator();

    public static final PreviewModelInfoIdComparator lastComparator = new PreviewModelInfoIdComparator();

    public static final PreviewModelInfoCategoryomparator categoryComparator = new PreviewModelInfoCategoryomparator();



}





