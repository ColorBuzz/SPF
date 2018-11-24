package com.colorbuzztechgmail.spf;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class CustomMaterialGenerator {


    public static void create(int count,Context context) {


        new CopyTask(context,count).execute();

    }



    private static int randomicePosition(int maxPosition){

        return  (int) (Math.random() * 10) % maxPosition;
    }


  private static String getRandomItemAtList (String[] items){

        return items[(randomicePosition(items.length))];

    }


    public static class CopyTask extends AsyncTask<Object, Integer, Void> {

        private ProgressDialog dialog;
         List<CustomMaterial> materialList;
        Context context;
         ModelDataBase db;

        int count;

        public CopyTask(Context context,int count) {
            this.context=context;
            db=new ModelDataBase(context);

            dialog = new ProgressDialog(context);
             dialog.setIndeterminate(false);
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setCancelable(true);
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {

                    cancel(true);
                }
            });
            this.count=count;
        }


        private void setDefaultDealers() {



            String[] categoryList = context.getResources().getStringArray(R.array.default_dealer);
            for (String category : categoryList) {
                final Dealership dealership=new Dealership();
                dealership.setName(category);
                 db.addDealership(dealership);

            }
        }


        @Override
        protected void onPreExecute() {
            dialog.show();

        }

        @Override
        protected Void doInBackground(Object [] params) {
            long time = System.currentTimeMillis();

            dialog.setMax(count);

            Utils.IdMaker mIdMaker;
            mIdMaker=new Utils.IdMaker();

            String[] typeList = context.getResources().getStringArray(R.array.default_materialsType);
            String[] dealerList = context.getResources().getStringArray(R.array.default_dealer);
            String[] seasonsList = context.getResources().getStringArray(R.array.default_seasons);
            String[] radomNameList=new String[]{"A","B","C","D",
                    "a","b","c","d"};
        //"D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z" "d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};

        setDefaultDealers();

        materialList = new ArrayList<>();




            for (int i = 0; i < count; i++) {

            final CustomMaterial material = new CustomMaterial(getRandomItemAtList(radomNameList)+getRandomItemAtList(radomNameList) +"_MAT_"+ String.valueOf(mIdMaker.next()));
            material.setDealership(getRandomItemAtList(dealerList));
            material.setType(getRandomItemAtList(typeList));
            material.setFeets((10f*i));
            material.setSeasons(getRandomItemAtList(seasonsList));
            material.setImage(new ShapeGenerator(context).getDrawableShape(null,ShapeGenerator.MODE_ROUND_RECT,0,ShapeGenerator.SIZE_SMALL));
            material.setId(Utils.toIntExact(db.addCustomMaterial(material)));

            materialList.add(material);


            publishProgress(i+1);


        }


            return null;
    }

        @Override
        protected void onPostExecute(Void result) {
            db.closeDB();
            dialog.dismiss();




        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            dialog.setTitle("Creando "+ '\n' + (materialList.get(values[0]-1)).getName());
            /*int max = (int) param.from.getLength();
            if(param.from.getLength() > Integer.MAX_VALUE) {
                max = (int) (param.from.getLength() / 1024);
            }*/
            dialog.setProgress(values[0]);
        }

    }

}