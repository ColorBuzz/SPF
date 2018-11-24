package com.colorbuzztechgmail.spf;

import android.support.v7.widget.RecyclerView;

import com.colorbuzztechgmail.spf.dataset.Dataset;

import java.util.Comparator;

public class RecentDataset  extends Dataset{

    public enum RecentSortType{

        NAME,
        LAST

    }

    public RecentSortType sortType=RecentSortType.LAST;

    public RecentDataset(RecyclerView recyclerView, RecyclerView.Adapter adapter) {
        super(recyclerView, adapter);
        setSortType(sortType.name());
    }

    public void setSortType(RecentSortType sortType){

        this.sortType=sortType;
    }
    @Override
    public String getSortType() {
        return sortType.name();
    }

    @Override
    public int getItemId(int position) {

        return 0;
    }

    @Override
    protected boolean areItemsEquals(Object a1, Object b2) {


        if(a1 instanceof Category  && b2 instanceof Category){


            return (((Category)a1).getId()==((Category)b2).getId());
        }else if(a1 instanceof PreviewModelInfo  && b2 instanceof PreviewModelInfo){

          return   (((PreviewModelInfo)a1).getId())==(((PreviewModelInfo)b2).getId());
        }
        else if(a1 instanceof CustomHeader  && b2 instanceof CustomHeader) {

            return (((CustomHeader) a1).getId()) == (((CustomHeader) b2).getId());
        }else{

            return false;
        }


     }

    @Override
    protected int calculateItemPos(Object a1, Object a2) {



        switch (sortType){



            case LAST:
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
                        return (getComparator().compare(a1,a2))*-1;


                    }

                }

                return  new alphabeticalComparator().compare(a1, a2);



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
                        return   getValueAtItem(a1).compareToIgnoreCase(getValueAtItem(a2));

                    }

                }
                return  getComparator().compare(a1, a2);

        }

        return 0;
    }



    @Override
    public String getTitleForItem(Object obj) {


         if(obj instanceof Category){

             return getContext().getString(R.string.action_directory);

         }else if(obj instanceof PreviewModelInfo){

             return getContext().getString(R.string.modelosText);

         }else if (obj instanceof CustomHeader){


             return ((CustomHeader) obj).getTitle();

         }else if (obj instanceof CustomMaterial){


             return getContext().getString(R.string.materialsTxt);

         }else if (obj instanceof Dealership) {


             return getContext().getString(R.string.dealership_dealerships);

         }else if (obj instanceof CutNoteList) {


             return getContext().getString(R.string.cutNotesList);
         }else if (obj instanceof Accessories) {


             return getContext().getString(R.string.action_complements);
         }else if(obj instanceof ProgressItem){


            return  ((ProgressItem) obj).TAG;

         }


             return null;
    }

    private String getValueAtItem(Object obj){



        if(obj instanceof Category){

            return ((Category) obj).getName();

        }else if(obj instanceof PreviewModelInfo){

            return ((PreviewModelInfo) obj).getName();

        }else if (obj instanceof CustomHeader){


            return ((CustomHeader) obj).getTitle();

        }else if (obj instanceof CustomMaterial){


            return ((CustomMaterial) obj).getName();

        }else if (obj instanceof Dealership) {


            return ((Dealership) obj).getName();

        }else if (obj instanceof CutNoteList) {


            return String.valueOf(((CutNoteList) obj).getReference());
        }else if (obj instanceof Accessories) {


            return ((Accessories) obj).getName();
        }else if (obj instanceof ProgressItem) {


            return ((ProgressItem) obj).getTAG();
        }


        return null;



    }

    private Comparator<Object> getComparator() {
        switch (sortType) {
            case NAME:
                return new alphabeticalComparator();


            case LAST:
                return new idComparator() ;
            default:

                return new alphabeticalComparator();
        }

    }

    public static class  idComparator implements Comparator<Object> {
        @Override public int compare(Object first,Object second) {

            long value1=0 ,value2=0;

            if(first instanceof CustomHeader){
                value1=((CustomHeader) first).getId();

            }else if (first instanceof Category){

                value1=((Category)first).getId();

            }else if (first instanceof CustomMaterial){

                value1=((CustomMaterial)first).getId();

            }else if (first instanceof PreviewModelInfo){

                value1=((PreviewModelInfo)first).getId();

            }else if (first instanceof CutNote){

                value1=((CutNote)first).getId();

            }else if (first instanceof Dealership){

                value1=((Dealership)first).getId();
            }

            if(second instanceof CustomHeader){
                value2=((CustomHeader) second).getId();

            }else if (second instanceof Category){

                value2=((Category)second).getId();

            }else if (second instanceof PreviewModelInfo) {

                value2 = ((PreviewModelInfo) second).getId();

            } else if (second instanceof CustomMaterial){

                value2=((CustomMaterial)second).getId();

            }else if (second instanceof CutNote){

                value2=((CutNote)second).getId();

            }else if (second instanceof Dealership){

                value2=((Dealership)second).getId();
            }



            if   (value1>value2){

                return 1;

            }
            return -1;
        }
    }

    public  class alphabeticalComparator implements Comparator<Object> {
        @Override public int compare(Object first, Object second) {

            String value1="",value2="";

            return getTitleForItem(first).compareTo(getTitleForItem(second));
        }
    }



}
