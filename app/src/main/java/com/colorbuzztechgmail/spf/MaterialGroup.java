package com.colorbuzztechgmail.spf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC01 on 07/12/2017.
 */

public class MaterialGroup {

    private String Name;


    private List<List<Piece>> pieceList;


    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public MaterialGroup(String name) {
        super();
        this.Name=name;
        this.pieceList=new ArrayList<>();

    }

    public void setPiece(Piece piece){

        boolean exist=false;

      if (!pieceList.isEmpty()){

            for(int i=0;i<pieceList.size();i++){

                if(pieceList.get(i).get(0).getName().equals(piece.getName())){
                    pieceList.get(i).add(piece);
                    exist=true;

                }

            }

            if (!exist){

                final List<Piece>  bufferList=new ArrayList<>();
                bufferList.add(piece);
                pieceList.add(bufferList);


            }

        }else{

             final List<Piece>  bufferList=new ArrayList<>();
              bufferList.add(piece);
              pieceList.add(bufferList);



      }


    }

    public void removePieceList(int index){

        this.pieceList.remove(index);

    }

    public void removePiece(Piece piece){

        this.pieceList.remove(piece);

    }

    public  int getTotalPieceCount(){

        int count=0;
        for (int i=0;i<pieceList.size();i++){
          count= count+pieceList.get(i).size();

        }

        return count;
    }

    public List<Piece> getPieceList( int index) {
        return pieceList.get(index);
    }

    public List<List<Piece>> getPieceList( ) {
        return pieceList;
    }

    public int getPieceListCount(){


        return pieceList.size();
    }
}
