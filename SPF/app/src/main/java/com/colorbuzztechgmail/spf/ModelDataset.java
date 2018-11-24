package com.colorbuzztechgmail.spf;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.colorbuzztechgmail.spf.ModelDataset.ModelSortType.CATEGORY;

public class ModelDataset extends Dataset {

     public enum ModelSortType {
        NAME,
        CATEGORY,
        LAST
    }
    private ModelSortType sortType=CATEGORY;

    public ModelDataset(RecyclerView recyclerView, RecyclerView.Adapter adapter) {
        super(recyclerView, adapter);

        setSortType(sortType.name());
    }

    public void setSortType(ModelSortType sortType){

        this.sortType=sortType;
    }

    @Override
    protected String getSortType() {
        return this.sortType.name();
    }

    @Override
    protected int getItemId(int position) {
        return getPreviewModelInfo(position).getId();
    }

    @Override
    protected boolean areItemsEquals(Object a1,Object b2){

        int id1=0,id2=0;

        if(a1 instanceof CustomHeader){

            id1=((CustomHeader)a1).getId();

        }else{
            id1=((PreviewModelInfo)a1).getId();


        }

        if(b2 instanceof CustomHeader){

            id2=((CustomHeader)b2).getId();

        }else{
            id2=((PreviewModelInfo)b2).getId();


        }

        return (id1==id2);
    };

    
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
                            String vocalName =((PreviewModelInfo)list.get(i)).getName().substring(0,1).toUpperCase();
                            if(!Character.isAlphabetic(Character.valueOf(vocalName.charAt(0)))){

                                vocalName="#";
                            }

                            boolean isPresent=false;
                            for(Object header: headerList){

                                if(((CustomHeader) header).getTitle().equals(vocalName)) {
                                    ((CustomHeader) header).addObject(list.get(i));

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

                case CATEGORY:
                    for (int i = 0; i < list.size(); i++) {
                        if (!(list.get(i) instanceof CustomHeader)) {
                            final String type = ((PreviewModelInfo)list.get(i)).getCategory();

                            boolean isPresent = false;
                            for(Object header: headerList){

                                if(((CustomHeader) header).getTitle().equals(type)) {
                                    ((CustomHeader) header).addObject(list.get(i));

                                    isPresent = true;
                                }
                            }

                            if(!isPresent){

                                final CustomHeader mTitle = new CustomHeader(type, mIdMaker.next(), null);
                                mTitle.addObject(list.get(i));
                                headerList.add(mTitle);

                            }
                        }

                    }

                    break;

                case LAST:
                    for (int i = 0; i < list.size(); i++) {
                        if (!(list.get(i) instanceof CustomHeader)) {
                            final String date = ((PreviewModelInfo) list.get(i)).getDate().split(String.valueOf('\n'))[0];
                            boolean isPresent = false;
                            for (Object header : headerList) {

                                if (((CustomHeader) header).getTitle().equals(date)) {
                                    ((CustomHeader) header).addObject(list.get(i));

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
        
        switch (sortType) {

            case CATEGORY:

                if (a1 instanceof CustomHeader && (!(a2 instanceof CustomHeader))) {

                    if (((CustomHeader) a1).getTitle().equals(((PreviewModelInfo)a2).getCategory())) {
                        return -1;

                    } else {

                        return getComparator().compare(a1, a2);

                    }

                } else if ((!(a1 instanceof CustomHeader)) && a2 instanceof CustomHeader) {

                    if (((PreviewModelInfo)a1).getCategory().equals(((CustomHeader) a2).getTitle())) {
                        return 1;

                    } else {

                        return getComparator().compare(a1, a2);

                    }

                } else if ((!(a1 instanceof CustomHeader)) && (!(a2 instanceof CustomHeader))) {

                    if (((PreviewModelInfo)a1).getCategory().equals(((PreviewModelInfo)a2).getCategory())) {
                        return nameComparator.compare(a1, a2);

                    } else {

                        return getComparator().compare(a1, a2);

                    }

                } else {


                    return getComparator().compare(a1, a2);
                }


            case NAME:


                if (a1 instanceof CustomHeader && (!(a2 instanceof CustomHeader))) {

                    if (((CustomHeader) a1).getTitle().toLowerCase().equals(((PreviewModelInfo)a2).getName().toLowerCase())) {
                        return -1;

                    } else {

                        return getComparator().compare(a1, a2);

                    }

                } else if ((!(a1 instanceof CustomHeader)) && a2 instanceof CustomHeader) {

                    if (((PreviewModelInfo)a1).getName().toLowerCase().equals(((CustomHeader) a2).getTitle().toLowerCase())) {
                        return 1;

                    } else {

                        return getComparator().compare(a1, a2);

                    }

                } else {

                    return getComparator().compare(a1, a2);
                }


            case LAST:
                if (a1 instanceof CustomHeader && (!(a2 instanceof CustomHeader))) {

                    if (((CustomHeader) a1).getTitle().contains(((PreviewModelInfo)a2).getDate())) {
                        return -1;

                    } else {

                        return getComparator().compare(a1, a2);

                    }

                } else if ((!(a1 instanceof CustomHeader)) && a2 instanceof CustomHeader) {

                    if (((PreviewModelInfo)a1).getDate().contains(((CustomHeader) a2).getTitle())) {
                        return 1;

                    } else {

                        return getComparator().compare(a1, a2);

                    }

                } else if ((!(a1 instanceof CustomHeader)) && (!(a2 instanceof CustomHeader))) {

                    if (((PreviewModelInfo)a1).getDate().equals(((PreviewModelInfo)a2).getDate())) {
                        return nameComparator.compare(a1, a2);

                    } else {

                        return getComparator().compare(a1, a2);

                    }

                } else {

                    return getComparator().compare(a1, a2);
                }


            default:

                return getComparator().compare(a1, a2);


        }
    }

    @Override
    protected boolean isItemHaveHeader(Object obj) {

        final PreviewModelInfo model=(PreviewModelInfo)obj;


        if(sparseHeader.size()>0) {

            for (int i = 0; i < sparseHeader.size(); i++) {

                final String header = ((CustomHeader) getObject(sparseHeader.keyAt(i))).getTitle();

                switch (sortType) {

                    case NAME:
                        String vocalName = model.getName();
                        if (!Character.isAlphabetic(Character.valueOf(vocalName.charAt(0)))) {

                            if (header.equals("#")) {

                                return true;
                            }
                        } else {

                            if (vocalName.equals(header)) {

                                return true;
                            }


                        }

                        break;

                    case CATEGORY:

                        if (model.getCategory().equals(header))
                            return true;

                        break;

                    case LAST:

                        if (model.getDate().contains(header))
                            return true;


                }

            }

        }

        return false;
    }

    public void removeByIds(ArrayList<Integer> Ids) {

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

    public PreviewModelInfo getPreviewModelInfoById(int id) {
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

            case CATEGORY:
                return categoryComparator;
            default:

                return nameComparator;
        }

    }

    public static class PreviewModelInfoNameComparator implements Comparator<Object> {
        @Override
        public int compare(Object first, Object second) {

            String value1,value2;
            if (first instanceof CustomHeader) {
                value1 = ((CustomHeader) first).getTitle();

            }else{
                value1 = ((PreviewModelInfo )first).getName();


            }

            if (second instanceof CustomHeader) {
                value2 = ((CustomHeader) second).getTitle();

            }else{
                value2 = ((PreviewModelInfo )second).getName();



            }

            return value1.toLowerCase().compareTo(value2.toLowerCase());

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

            return value1.toLowerCase().compareTo(value2.toLowerCase());


        }
    }

    public static class PreviewModelInfoCategoryomparator implements Comparator<Object> {
        @Override
        public int compare(Object first, Object second) {
            String value1,value2;
            if (first instanceof CustomHeader) {
                value1 = ((CustomHeader) first).getTitle();

            }else{
                value1 = ((PreviewModelInfo )first).getCategory();


            }

            if (second instanceof CustomHeader) {
                value2 = ((CustomHeader) second).getTitle();

            }else{
                value2 = ((PreviewModelInfo )second).getCategory();



            }

            return value1.toLowerCase().compareTo(value2.toLowerCase());

        }
    }

    public static final PreviewModelInfoNameComparator nameComparator = new PreviewModelInfoNameComparator();

    public static final PreviewModelInfoIdComparator lastComparator = new PreviewModelInfoIdComparator();

    public static final PreviewModelInfoCategoryomparator categoryComparator = new PreviewModelInfoCategoryomparator();

}





