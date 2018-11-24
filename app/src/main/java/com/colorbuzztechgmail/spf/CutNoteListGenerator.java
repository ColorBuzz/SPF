package com.colorbuzztechgmail.spf;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CutNoteListGenerator {


    public static void create(int count,Context context,int maxPairCount) {


        new  CopyTask(context,count,maxPairCount).execute();

    }



    public static class CopyTask extends AsyncTask<Object, Integer, Void> {

        private ProgressDialog dialog;
        List<CutNoteList> cutNoteList;
        Context context;
        ModelDataBase db;
        Map<String,Integer> values;
        int maxPairCount;

        int count;

        public CopyTask(Context context,int count,int maxPairCount) {
            this.context=context;
            db=new ModelDataBase(context);

            dialog = new ProgressDialog(context);
            dialog.setTitle(context.getString(R.string.importText));
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
            this.maxPairCount=maxPairCount;


        }





        @Override
        protected void onPreExecute() {
            dialog.show();

        }

        @Override
        protected Void doInBackground(Object [] params) {

             dialog.setMax(count);

             cutNoteList = new ArrayList<>();
             PreviewModelInfo model;
             int id=1;

             try {
                 for (int i = 0; i < count; i++) {

                     if(db.getModelsCount()>0) {

                         if (id <= db.getModelsCount()) {

                             model = db.getPreviewModelById(id);
                             id++;
                         } else {

                             id = 1;
                             model = db.getPreviewModelById(id);

                         }

                         values=new HashMap<>();

                         for(int j=model.getSizeList().size()-1;j>=0;j--){



                             values.put(String.valueOf(model.getSizeList().get(j)),getRandomPairCount(5,10));


                         }

                         cutNoteList.add(CutNoteGenerator.create(values, maxPairCount,model, CutNoteGenerator.GeneratorType.AUTOMATIC,false));
                         db.addCutNoteList(cutNoteList.get(cutNoteList.size()-1));
                         publishProgress(i+1);

                     }else{


                         Utils.toast(context,"No hay Modelos");
                     }




                 }

             }catch (Exception e){

                 Log.e("CutNoteListGenerator",e.toString());

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
            dialog.setTitle("Creando Listados: " + '\n' + (cutNoteList.get(values[0]-1)).getModel());
            /*int max = (int) param.from.getLength();
            if(param.from.getLength() > Integer.MAX_VALUE) {
                max = (int) (param.from.getLength() / 1024);
            }*/
            dialog.setProgress(values[0]);
        }

    }


    private static int randomicePosition(int maxPosition){

        return  (int) (Math.random() * 10) % maxPosition;
    }


    private static int getRandomPairCount (int factor,int maxValue){

        return factor*randomicePosition(maxValue);

    }
    
    
    
    
}
