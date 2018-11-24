package com.colorbuzztechgmail.spf;

/*
 * Copyright (C) 2015 Paul Burke
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecyclerSliderAdapter extends RecyclerView.Adapter<RecyclerSliderAdapter.ViewHolder> {
 

 

    private Context context;
     private ItemActionListener clickListener;
     SparseBooleanArray expandState;

    public RecyclerSliderAdapter(Context context,ItemActionListener listener  ) {
        
        this.clickListener=listener;
        this.context = context;
        expandState= new SparseBooleanArray();
         
    }

    
    public void addItem(int id,String name,Drawable drawable){
        
        Preview preview = new Preview(id,name,drawable);
        
        itemList.add(preview);
        
        expandState.append(id,false);
        
      
        
        
    }

    public void addAll(List<Preview> previews){

        itemList.beginBatchedUpdates();

        itemList.addAll(previews);

        itemList.endBatchedUpdates();

        for(int i =0; i<itemList.size();i++){

            expandState.append(itemList.get(i).getId(),false);


        }

    }

    public void removeAll(){

        itemList.beginBatchedUpdates();

        itemList.clear();
        expandState.clear();

        itemList.endBatchedUpdates();



    }


    public void toggleItem(int position){



        for(int i=0;i<itemList.size();i++){

            if(i!=position){
                if(expandState.get(itemList.get(i).getId())){

                    setItemCheck(i,false,true);

                }

            }else{

                if(!(expandState.get(itemList.get(i).getId()))){
                    setItemCheck(position,true,true);

                }


            }

        }



    }

    public boolean isCheck(int id){

        return expandState.get(id);
    }

    private void setItemCheck(int position,boolean value,boolean notify){

        expandState.put(itemList.get(position).getId(),value);

        if(notify){
            notifyItemChanged(position);


        }

    }

    void updateCheckedState(int position,CheckableLinearLayout layout) {
        final boolean isChecked =  isCheck(itemList.get(position).getId());
        if ( layout instanceof Checkable) {
            layout.setChecked(isChecked);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            layout.setActivated(isChecked);
        }
    }

    public void  setHeaderDataStyle(View header,Object obj) {

        final TextView title=(TextView) header.findViewById(R.id.text);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) title.getLayoutParams();
        params.width=FrameLayout.LayoutParams.WRAP_CONTENT;
        params.height=FrameLayout.LayoutParams.WRAP_CONTENT;

        if(((Preview)obj).getImage() !=null) {

            params.gravity= Gravity.CENTER_HORIZONTAL|Gravity.TOP;

            title.setBackground(context.getDrawable(R.drawable.drawable_transparent_gradient));

        }else{



            params.gravity=Gravity.CENTER;

            title.setBackground(new ColorDrawable(Color.TRANSPARENT));
        }
        title.setLayoutParams(params);





    }


    @Override
    public RecyclerSliderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {





                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_slider_layout, parent, false);
                return new ViewHolder(v);





    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final Preview preview=itemList.get(position);

        holder.textView.setText(preview.getName());
        if (preview.getImage() != null) {

            holder.image.setImageDrawable(new ShapeGenerator(context).getDrawableShape(preview.getImage(), ShapeGenerator.MODE_ROUND_RECT, 1,ShapeGenerator.SIZE_BIG));
            setHeaderDataStyle(holder.itemView,preview);

        }else{

            holder.image.setImageDrawable(null);
        }
        updateCheckedState(position,holder.checkLinearLyt);

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View v, int position, boolean isLongClick) {

              if(!isCheck(preview.getId())){

                  if(clickListener!=null){
                      clickListener.onPreview(preview);

                  }

                }
                toggleItem(position);


            }
        });

    }




    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{



        private ItemClickListener itemClickListener;
        private TextView textView;
        private CheckableLinearLayout checkLinearLyt;
        private ImageView image;


        public ViewHolder(View v) {
            super(v);


            textView = (TextView) v.findViewById(R.id.text);
            image = (ImageView) v.findViewById(R.id.image);

            checkLinearLyt = (CheckableLinearLayout) v.findViewById(R.id.checkLayout);
            v.setOnClickListener(this);


        }

        public void setItemClickListener(ItemClickListener itemClickListener) {

            this.itemClickListener = itemClickListener;

        }


        @Override
        public void onClick(View v) {

            itemClickListener.onClick(v, getAdapterPosition(), false);
        }




    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    
     public static class Preview{
        
        
        private int id=0;
        private String name="";
        private Drawable image=null;

         public Preview(int id, String name, Drawable image) {
             this.id = id;
             this.name = name;
             this.image = image;
         }

         public int getId() {
             return id;
         }

         public void setId(int id) {
             this.id = id;
         }

         public String getName() {
             return name;
         }

         public void setName(String name) {
             this.name = name;
         }

         public Drawable getImage() {
             return image;
         }

         public void setImage(Drawable image) {
             this.image = image;
         }
     } 



    private final SortedList.Callback<Preview> mCallback = new SortedList.Callback<Preview>() {

        @Override
        public void onInserted(int position, int count) {
            notifyItemRangeInserted(position, count);
        }

        @Override
        public void onRemoved(int position, int count) {
            notifyItemRangeRemoved(position, count);
        }

        @Override
        public void onMoved(int fromPosition, int toPosition) {
            notifyItemMoved(fromPosition, toPosition);
        }

        @Override
        public void onChanged(int position, int count) {
            notifyItemRangeChanged(position, count);
        }

        @Override
        public int compare(Preview a, Preview b) {

            if((!(a.getName().equals(context.getResources().getString(R.string.action_sortAll))) && b.getName().equals(context.getResources().getString(R.string.action_sortAll) ))){

                return 1;
            }else if((!(b.getName().equals(context.getResources().getString(R.string.action_sortAll))) && a.getName().equals(context.getResources().getString(R.string.action_sortAll) ))){

                return -1;
            }

            return a.getName().toLowerCase().compareTo(b.getName().toLowerCase());
        }

        @Override
        public boolean areContentsTheSame(Preview oldItem, Preview newItem) {
            return oldItem.equals(newItem);
        }

        @Override
        public boolean areItemsTheSame(Preview item1, Preview item2) {
            return item1.getId() == item2.getId();
        }
    };

    public    SortedList<Preview> itemList = new SortedList<>(Preview.class, mCallback);

}
