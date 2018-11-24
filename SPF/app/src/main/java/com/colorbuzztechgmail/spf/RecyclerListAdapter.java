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

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecyclerListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ItemTouchHelperAdapter ,HeaderItemDecoration.StickyHeaderInterface {
    public static final int TITLE=0;
    public static final int PIECE=1;


    private Context context;
    public List<Piece> pieceList;
    public SparseArray<String> sparseArrayundo;
    private List<MaterialPositionList>matPos;


    @Override
    public int getItemViewType(int position) {

        if(pieceList.get(position) instanceof PieceTitle){

            return TITLE;
        }



        return PIECE;
    }



    public RecyclerListAdapter(Context context, List<Piece> pieces) {

        this.pieceList=pieces;
         sparseArrayundo=new SparseArray<>();
         matPos=new ArrayList<>();


        if(!pieceList.isEmpty()){

            for(int i=0;i<pieceList.size();i++){


                if(pieceList.get(i) instanceof Piece){

                      sparseArrayundo.append(pieceList.get(i).getId(),pieceList.get(i).getMaterial());

                }else{

                    MaterialPositionList mat= new MaterialPositionList(pieceList.get(i).getMaterial(),
                            i,false);
                      matPos.add(mat);



                }


            }



        }
         this.context=context;
    }

    public boolean isSameMaterial(int position){


        return (sparseArrayundo.get(pieceList.get(position).getId()))
                .equals(pieceList.get(position).getMaterial());

    }

    public boolean isSameMaterialPostion(int fromPosition, int toPosition){


        return   pieceList.get(fromPosition).getMaterial()
                .equals(pieceList.get(toPosition).getMaterial());


    }

    public int getTitlePosition(String title){


              int pos=0;

              for(int i=0;i<pieceList.size();i++){

                  if(pieceList.get(i).getMaterial().equals(title)) {

                      return i;
                  }

//                  }else if(pieceList.get(pieceList.size()-i).getMaterial().equals(title)){
//
//                      return i;
//
//
//                  }




              }



return pos;



        }

    public void collapseMaterial(String Material, boolean state){






    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType){

            case TITLE:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tile_main, parent, false);
                return new TitleViewHolder(view);


            case PIECE:
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.piece_listview, parent, false);
                return new ViewHolder(v);



        }

        return null;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        if(holder instanceof ViewHolder){

            ((ViewHolder) holder).textView.setText(pieceList.get(position).getName());


            if( !isSameMaterial(position)){

                ((ViewHolder) holder).undoImg.setVisibility(View.VISIBLE);

            }else{
                ((ViewHolder) holder).undoImg.setVisibility(View.INVISIBLE);

            }

            ((ViewHolder) holder).subTextView1.setText(pieceList.get(position).getMaterial());

            ((ViewHolder) holder).img.setImageDrawable( pieceList.get(position).getImage());



            ((ViewHolder) holder).setItemClickListener(new ItemClickListener() {
                @Override
                public void onClick(View v, int position, boolean isLongClick) {

                    switch (v.getId()){

                        case R.id.imgBack:

                            final Piece piece=pieceList.get(position);

                            String material=sparseArrayundo.get(pieceList.get(position).getId());
                            pieceList.remove(position);
                            notifyItemRemoved(position);

                            int titlepos=getTitlePosition(material);
                            piece.setMaterial(material);

                           pieceList.add(titlepos+1,piece);
                          notifyItemInserted(titlepos+1);


                            break;


                    }


                }
            });


        }


        if(holder instanceof TitleViewHolder){
            ((TitleViewHolder)holder).setItemClickListener(new ItemClickListener() {
                @Override
                public void onClick(View v, int position, boolean isLongClick) {


                }
            });

          bindHeaderData(holder.itemView,position);


        }

    }

    @Override
    public int getItemCount() {
        return pieceList.size();
    }

    @Override
    public void onItemDismiss(int position) {

        if(pieceList.get(position) instanceof Piece){
            pieceList.remove(position);
            notifyItemRemoved(position);

        }

    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {


            if(pieceList.get(toPosition) instanceof PieceTitle) {

                if (fromPosition > toPosition) {
                    if(!isSameMaterialPostion(fromPosition,toPosition-1)) {

                        pieceList.get(fromPosition).setMaterial(pieceList.get(toPosition-1).getMaterial());
                    }

                } else {
                    if (!isSameMaterialPostion(fromPosition, toPosition)) {
                        pieceList.get(fromPosition).setMaterial(pieceList.get(toPosition).getMaterial());

                    }
                }
            }else {
                    if (!isSameMaterialPostion(fromPosition, toPosition)) {
                        pieceList.get(fromPosition).setMaterial(pieceList.get(toPosition).getMaterial());

                    }
        }




        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(pieceList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(pieceList, i, i - 1);
            }
        }

        notifyItemMoved(fromPosition, toPosition);




    }

    @Override
    public void onRefreshItem(int position) {

        notifyItemChanged(position);

           }

    @Override
    public boolean onMoveState(int position) {

        return (pieceList.get(position) instanceof Piece);
    }

    @Override
    public int getHeaderPositionForItem(int itemPosition) {
        int headerPosition = 0;
        do {
            if (this.isHeader(itemPosition)) {
                headerPosition = itemPosition;
                break;
            }
            itemPosition -= 1;
        } while (itemPosition >= 0);
        return headerPosition;
    }


    @Override
    public int getHeaderLayout(int headerPosition) {
        return R.layout.tile_main;
    }

    @Override
    public void bindHeaderData(View header, int position) {

        ((TextView) header.findViewById(R.id.text)).setText(((PieceTitle)pieceList.get(position)).getMaterial());

        if(((PieceTitle)pieceList.get(position)).getImage() !=null){

            ((ImageView) header.findViewById(R.id.imgMaterial)).setImageDrawable(pieceList.get(position).getImage());

        }

    }

    @Override
    public boolean isHeader(int itemPosition) {
        return (pieceList.get(itemPosition) instanceof PieceTitle);
    }

    @Override
    public void ToggleTitle(View header,String title) {

    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        // each data item is just a string in this case


        private ItemClickListener itemClickListener;
        private TextView textView,subTextView1;
        private ImageView img,undoImg;

        public ViewHolder(View v) {
            super(v);

            textView = (TextView) v.findViewById(R.id.text);
            subTextView1 = (TextView) v.findViewById(R.id.text1);
            img = (ImageView) v.findViewById(R.id.imgPiece);
            undoImg = (ImageView) v.findViewById(R.id.imgBack);

            undoImg.setOnClickListener(this);
            v.setOnLongClickListener(this);
           // v.setOnClickListener(this);


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


    public static class TitleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        // each data item is just a string in this case


        private ItemClickListener itemClickListener;
        private TextView textView;
        private TextView subText;
        private ImageView img;

        public TitleViewHolder(View v) {
            super(v);

            textView = (TextView) v.findViewById(R.id.text);
            subText = (TextView) v.findViewById(R.id.text1);
            img = (ImageView) v.findViewById(R.id.imgMaterial);
            v.setOnLongClickListener(this);
            // v.setOnClickListener(this);


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

     class MaterialPositionList{

        String name;
        int pos;
        boolean expanded;

         public MaterialPositionList(String name,int pos,boolean expanded) {
             super();
             this.name=name;
             this.pos=pos;
             this.expanded=expanded;
         }

         public String getName() {
             return name;
         }

         public void setName(String name) {
             this.name = name;
         }

         public int getPos() {
             return pos;
         }

         public boolean isExpanded() {
             return expanded;
         }

         public void setExpanded(boolean expanded) {
             this.expanded = expanded;
         }

         public void setPos(int pos) {
             this.pos = pos;
         }

     }

}
