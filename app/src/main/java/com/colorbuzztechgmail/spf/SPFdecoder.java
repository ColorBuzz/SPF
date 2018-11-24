package com.colorbuzztechgmail.spf;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by PC01 on 05/12/2017.
 */

public class SPFdecoder{


    private Model spfModel;
    private String pieces;
    private String model;
    private List<String> nameList;
    private List<String> materialsList;
    private List<Integer> pieceIndex;
    private List<String> sizeList;
    private List<String> data;
    private Context mContext;



    String separator = System.getProperty("line.separator");


    public SPFdecoder(String List, Context context) {
        super();

        this.mContext=context;
        FilterData(Arrays.asList(List.split(separator)));

    }

    public Model getSpfModel() {
        return spfModel;
    }


    private void FilterData(List<String> StringList) {
        spfModel = new Model();
        nameList = new ArrayList<>();
        materialsList = new ArrayList<>();
        pieceIndex = new ArrayList<>();
        sizeList = new ArrayList<>();


        int endIndex = 0;

        data = new ArrayList<>();
       data.addAll(StringList);


        for (int i = 0; i < data.size(); i++) {


            if (data.get(i).contains("MODEL")) { //Empezamos a buscar por el archivo desde la palabra MODEL (Para Saltarnos la morralla)
                if (!data.get(i).contains("MODELS")) {
                    model = data.get(i + 1).substring(5);
                    Log.e("Model", "Modelo " + data.get(i + 1).substring(5));
                    spfModel.setName(model.trim());
                }
            }
             /*    'Busqueda de Texto
                  If texto(i).ToString.Contains("TEXT") Then
                If Not texto(i + 1).ToString.Contains("ELEM_TYPE 6") Then

                'ListBox_ASCII_Texto.Items.Add(texto(i + 1))
                Numero_TXT = Numero_TXT + 1

                  End If
                       End If*/
            //Total Piezas
            //
            if (data.get(i).contains("PIECES")) {
                pieces = data.get(i).substring(7);

                Log.e("Import", "N.Piezas: " + pieces);
            }
            //Busqueda del Inicio de las Piezas

            if (data.get(i).contains("PIECE")) {
                if (data.get(i + 1).contains("NAME")) {
                    pieceIndex.add(i);

                    nameList.add(data.get(i + 1).substring(5).trim());
                    sizeList.add(data.get(i + 2).substring(5).trim());
                    materialsList.add(data.get(i + 11).substring(9).trim());
                }

                // Log.e("Import","Pieza: " + data.get(i + 1).substring(5));

            }

            if (data.get(i).contains("END_SPF")) {
                endIndex = i;
                //Log.e("Index",String.valueOf(endIndex));

            }
        }


        HashSet hs = new HashSet();
        hs.addAll(materialsList);
        materialsList.clear();
        materialsList.addAll(hs);
        // demoArrayList= name of arrayList from which u want to removeIndex duplicates


        hs = new HashSet();
        hs.addAll(sizeList); // demoArrayList= name of arrayList from which u want to removeIndex duplicates
        sizeList.clear();
        sizeList.addAll(hs);

        Collections.sort(materialsList);
        Collections.sort(sizeList);

        spfModel.setSizeList(sizeList);

        for (int i = 0; i < materialsList.size(); i++) {

            final MaterialGroup materialGroup = new MaterialGroup(materialsList.get(i));
            spfModel.setMaterials(materialGroup);

            // Log.e("Import","MaterialGroup: " + materialsList.get(i));
        }

        for (int i = 0; i < pieceIndex.size(); i++) {
            final Piece axuPiece = new Piece();
            axuPiece.setName(data.get(pieceIndex.get(i) + 1).substring(5).trim());
            axuPiece.setSize(data.get(pieceIndex.get(i) + 2).substring(5).trim());
            axuPiece.setAmount(Integer.valueOf(data.get(pieceIndex.get(i) + 7).substring(7).trim()));
            axuPiece.setAmount_mirror(Integer.valueOf(data.get(pieceIndex.get(i) + 8).substring(14).trim()));
            axuPiece.setMaterial(data.get(pieceIndex.get(i) + 11).substring(9).trim());
            ;





            if (i < pieceIndex.size() - 1) {
                axuPiece.setRawData(data.subList(pieceIndex.get(i), pieceIndex.get(i + 1)));
               // extraData(axuPiece);
            } else  {
                axuPiece.setRawData(data.subList(pieceIndex.get(i), endIndex));
                //extraData(axuPiece);                //  Log.e("Model", (axuPiece.getRawData().get(axuPiece.getRawData().size()-1)));

            }

            for (int j = 0; j < spfModel.getMaterials().size(); j++) {

                if (axuPiece.getMaterial().equals(spfModel.getMaterials().get(j).getName())) {

                    spfModel.getMaterials().get(j).setPiece(axuPiece);


                }

            }

             }


    }

    private String ListToJson(List<String> values){

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        return gson.toJson(values);
    }

   public  static  void extraData(Piece piece,Context mContext){

        int size = (int) mContext.getResources().getDimension(R.dimen.piece_pic_size);
        final DrawView drawView = new DrawView(mContext, piece, null, size, size);
        piece.setImage(drawView.getDrawable(size, size));
        Float area = (drawView.decoder.getRectagleArea() / 1000000);
        piece.setBoxArea(area * 10.764f);
        piece.setAmount_material(Utils.FormatDecimal(drawView.decoder.calculateArea()/1000000)*10.746f);
    }

}



/*

        Start_SPF(count_long) = texto(count_long) 'Almacenamos el codigo de inicio del SPF para cuando borremos piezas poder volver a añadirlo en su posicion inicial.

*/
