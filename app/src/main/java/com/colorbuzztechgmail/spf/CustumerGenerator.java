package com.colorbuzztechgmail.spf;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

public class CustumerGenerator {


    public static void create(Context context) {


        new CopyTask(context).execute();

    }



    private static int randomicePosition(int maxPosition){

        return  (int) (Math.random() * 10) % maxPosition;
    }


  private static String getRandomItemAtList (String[] items){

        return items[(randomicePosition(items.length))];

    }


    public static class CopyTask extends AsyncTask<Object, Integer, Void> {

        private ProgressDialog dialog;
         String[] custumerList;
        Context context;
         ModelDataBase db;



        public CopyTask(Context context) {
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
        }





        @Override
        protected void onPreExecute() {
            dialog.show();

        }

        @Override
        protected Void doInBackground(Object [] params) {
            long time = System.currentTimeMillis();

            custumerList = context.getResources().getStringArray(R.array.default_custumer);
            int count=0;
            dialog.setMax(custumerList.length);

            for (String name : custumerList) {
                final Custumer custumer=new Custumer(name);
                db.addCustumer(custumer);

                publishProgress(count++);

                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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
            dialog.setTitle("Creando "+ '\n' + custumerList[values[0]]);
            /*int max = (int) param.from.getLength();
            if(param.from.getLength() > Integer.MAX_VALUE) {
                max = (int) (param.from.getLength() / 1024);
            }*/
            dialog.setProgress(values[0]);
        }

    }

}