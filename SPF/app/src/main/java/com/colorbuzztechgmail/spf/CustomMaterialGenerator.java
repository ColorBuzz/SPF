package com.colorbuzztechgmail.spf;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class CustomMaterialGenerator {


    public static List<CustomMaterial> create(int count,Context context) {

        Utils.IdMaker mIdMaker;
        mIdMaker=new Utils.IdMaker();

        String[] typeList = context.getResources().getStringArray(R.array.default_materialsType);
        String[] dealerList = context.getResources().getStringArray(R.array.default_dealer);
        String[] seasonsList = context.getResources().getStringArray(R.array.default_seasons);


        ModelDataBase db=new ModelDataBase(context);


        List<CustomMaterial> materialList = new ArrayList<>();

        for (int i = 0; i < count; i++) {

            final CustomMaterial material = new CustomMaterial("Custom Material " + String.valueOf(mIdMaker.next()));
            material.setDealership(getRandomItemAtList(dealerList));
            material.setType(getRandomItemAtList(typeList));
            material.setFeets((10f*i));
            material.setSeasons(getRandomItemAtList(seasonsList));
            material.setImage(new ShapeGenerator(context).getDrawableShape(null,ShapeGenerator.MODE_ROUND_RECT,0,ShapeGenerator.SIZE_SMALL));
            material.setId(Utils.toIntExact(db.addCustomMaterial(material)));

            materialList.add(material);


        }

        return materialList;

    }



    private static int randomicePosition(int maxPosition){

        return  (int) (Math.random() * 10) % maxPosition;
    }


  private static String getRandomItemAtList (String[] items){

        return items[(randomicePosition(items.length))];

    }



}