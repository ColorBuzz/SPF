package com.colorbuzztechgmail.spf;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.colorbuzztechgmail.spf.databinding.ItemsBinding;
import com.github.mjdev.libaums.fs.UsbFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by PC01 on 25/11/2017.
 */

public class UsbAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private static int LIST=0;
    private static int GRID=1;
    private static int ITEM_GRID=2;
    private static int ITEM_LIST=3;
    private static int FOLDER_GRID=4;
    private static int FOLDER_LIST=5;

    SparseBooleanArray sparseState;

    private int vieMode=LIST;
    private List<Object> files;
    private Context context;
    public int checkedItemCount=0;

    private Comparator<Object> mComparator = new Comparator<Object>() {

        @Override
        public int compare(Object lhs,Object rhs) {

            String value1=null,value2=null;

            if(lhs instanceof CustomHeader){
                value1=((CustomHeader)lhs).getTitle();


            }else{


                value1=((UsbFile)lhs).getName();

            }

            if( rhs instanceof CustomHeader){
                value2=((CustomHeader)rhs).getTitle();


            }else{


                value2=((UsbFile)rhs).getName();

            }

            if (lhs instanceof CustomHeader && !(rhs instanceof CustomHeader)) {
                return -1;
            }

            if (rhs instanceof CustomHeader && !(lhs instanceof  CustomHeader)) {
                return 1;
            }

            return value1.compareToIgnoreCase(value2);
        }
    };

    private ItemClickListener itemActionListener;

    public void setOnClickListener(ItemClickListener listener) {
        this.itemActionListener = listener;
    }



    public void setViewMode(int viewMode){
        this.vieMode=viewMode;


    }

    public UsbAdapter(Context context, List<Object> dir,int viewMode) throws IOException {

        this.checkedItemCount=0;
        this.context = context;
        setViewMode(viewMode);
        this.files=new ArrayList<>();
        if(dir!=null){
            this.files.addAll(dir);
            refresh();
        };

          }


    
    private void refresh(){

        sparseState=new SparseBooleanArray();

        Collections.sort(files,mComparator);
        for(int i=0;i<files.size();i++){

            sparseState.append(i,false);

        }
        notifyDataSetChanged();
        
        
    }

    public void clear(){


        if(files.size()>0){
            files.clear();

        }
        if(sparseState.size()>0){

            sparseState.clear();
        }
        checkedItemCount=0;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                    int viewType) {

        View v=null;

       // Log.e("VIEWTYPE",String.valueOf(viewType));

        if(viewType==FOLDER_GRID){
              v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.folder_item, parent, false);

            // set the view's size, margins, paddings and layout parameters

            return new FolderViewHolder(v);

        }else if(viewType==FOLDER_LIST) {
              v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.folder_file_listview, parent, false);
            // set the view's size, margins, paddings and layout parameters
            return new FolderViewHolder(v);

        }else{

            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.file_listview, parent, false);

            return new ItemViewHolder(v);

        }


    }

    @Override
    public int getItemViewType(int position) {

        if(vieMode==LIST){


            if(isHeader(position)){

                return FOLDER_LIST;

            }else{

                return ITEM_LIST;

            }

        }else{



            if(isHeader(position)){

                return FOLDER_GRID;

            }else{

                return ITEM_GRID;

            }

        }



    }



    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element


        if(files.get(position) instanceof CustomHeader){
         final   CustomHeader customHeader = (CustomHeader) files.get(position);


            ( (FolderViewHolder)holder).folderText.setText(customHeader.getTitle());

                String aux=context.getResources().getString(R.string.checkedSinleFileText);
                if(customHeader.getBuffer().size()>1) {
                    aux = context.getResources().getString(R.string.checkedFileText);
                }
                    ( (FolderViewHolder)holder).countText.setText(String.valueOf(customHeader.getBuffer().size()) + " " + aux);

                    ((FolderViewHolder)holder).setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View v, int position, boolean isLongClick) {
                        if (isLongClick) {
                            // Utils.toast(context, "LONGCLICK" + String.valueOf(position));

                        } else {

                            // Utils.toast(context, "SHORTCLICK" + String.valueOf(position));

                            if(itemActionListener!=null){
                                itemActionListener.onClick(v,position,isLongClick);

                            }

                        }
                    }
                });

 
        
        }else{


            if(sparseState.size()>0){
                if(sparseState.indexOfKey(position)!=-1){

                    ((ItemViewHolder)holder).checkBox.setChecked(sparseState.get(position));

                }

            }
            final UsbFile file=(UsbFile) files.get(position);

            ((ItemViewHolder)holder).checkBox.setText(file.getName());
            ((ItemViewHolder)holder).dataTxt.setText(Utils.getDate(file.lastModified()));

            ((ItemViewHolder)holder).setItemClickListener(new ItemClickListener() {
                @Override
                public void onClick(View v, int position, boolean isLongClick) {
                    if (isLongClick) {
                        // Utils.toast(context, "LONGCLICK" + String.valueOf(position));

                    } else {
                        ((ItemViewHolder)holder).checkBox.setSelected(true);


                        switch (v.getId()){


                            case R.id.checkBox4:


                                if(sparseState.indexOfKey(position)!=-1){

                                    sparseState.put(position,((ItemViewHolder)holder).checkBox.isChecked());




                                }else{

                                    sparseState.append(position,((ItemViewHolder)holder).checkBox.isChecked());

                                }



                                if(((ItemViewHolder)holder).checkBox.isChecked()){

                                    checkedItemCount++;


                                }else{


                                    checkedItemCount--;
                                }

                                if(itemActionListener!=null){

                                    itemActionListener.onClick(v,position,false);

                                }

                                break;
                        }






                        // Utils.toast(context, "SHORTCLICK" + String.valueOf(position));



                    }
                }
            });



        }
        


    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return files.size();
    }


       public Object getItem(int position) {
        return files.get(position);
    }

    /**
     *
     * @return the directory which is currently be shown.
     */
    public List<Object> getCurrentDir() {


        return files;
    }
    public boolean isHeader(int position){


        return files.get(position) instanceof CustomHeader;

    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    public static class FolderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        // each data item is just a string in this case


        private ItemClickListener itemClickListener;
        private TextView folderText;
        private TextView countText;
        public FolderViewHolder(View v) {
            super(v);

            folderText = (TextView) v.findViewById(R.id.pathText);
            countText = (TextView) v.findViewById(R.id.countTxt);
            v.setOnLongClickListener(this);
            v.setOnClickListener(this);


        }

        public void setItemClickListener(ItemClickListener itemClickListener) {

            this.itemClickListener = itemClickListener;

        }


        @Override
        public void onClick(View v) {

            itemClickListener.onClick(v, getAdapterPosition(), false);
        }

        @Override
        public boolean onLongClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition(), true);
            return false;
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        // each data item is just a string in this case

        ItemsBinding mBinding;
        CheckBox checkBox;
        ImageView imageView;
        TextView dataTxt;
        private ItemClickListener itemClickListener;

        public ItemViewHolder(View v) {
            super(v);

            checkBox=v.findViewById(R.id.checkBox4);
             dataTxt=v.findViewById(R.id.dataText);
           v.setOnLongClickListener(this);
            // checkBox.setOnLongClickListener(this);


            v.setOnClickListener(this);
            checkBox.setOnClickListener(this);

        }

        public void performBind(FileItem File) {

            this.mBinding.setFileName(File);
        }

        public ItemsBinding getBinding() {

            return mBinding;
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {

            this.itemClickListener = itemClickListener;

        }


        @Override
        public void onClick(View v) {

            itemClickListener.onClick(v, getAdapterPosition(), false);
        }

        @Override
        public boolean onLongClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition(), true);
            return false;
        }
    }


//////////////////////////////////////////////////
 
 
}


