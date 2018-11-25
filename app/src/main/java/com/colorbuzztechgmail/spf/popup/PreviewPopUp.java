package com.colorbuzztechgmail.spf.popup;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.colorbuzztechgmail.spf.CustomMaterial;
import com.colorbuzztechgmail.spf.DrawView;
import com.colorbuzztechgmail.spf.ModelDataBase;
import com.colorbuzztechgmail.spf.Piece;
import com.colorbuzztechgmail.spf.R;
import com.colorbuzztechgmail.spf.SPFencoder;
import com.colorbuzztechgmail.spf.Utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;


/**
 * Created by PC01 on 18/06/2017.
 */
public class PreviewPopUp extends  Dialog {



     Object obj;
      boolean start=true;
    Toolbar toolbar;
    DrawView drawView;
    waitPoup waitPoup;
    String title=null;
    String modelName;




    public PreviewPopUp(@NonNull Context context,String model,Object obj) {
        super(context);
        this.obj=obj;
        this.modelName=model;


        if(obj instanceof Piece){

            title=((Piece)obj).getName() + "-" + ((Piece)obj).getSize()+ "-" +  ((Piece)obj).getMaterial();


        }else{


            title=((CustomMaterial)obj).getName();
        }
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        LayoutInflater li = LayoutInflater.from(getContext());

        final View promptsView = li.inflate(R.layout.assigment_popup, null);


        ((LinearLayout)promptsView.findViewById(R.id.containertem)).removeViewAt(1);


        toolbar=new Toolbar(getContext());

        toolbar.inflateMenu(R.menu.menu_model);
        ((LinearLayout)promptsView.findViewById( R.id.container)).removeAllViewsInLayout();
        ((LinearLayout)promptsView.findViewById( R.id.container)).addView(toolbar);


        loadMenu();

        setContentView(promptsView);


    }

    @Override
    public void onStart() {
        super.onStart();



                 getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                int width =getWindow().getWindowManager().getDefaultDisplay().getWidth();
                int height =getWindow().getWindowManager().getDefaultDisplay().getHeight();



                LinearLayout imageContainer = (LinearLayout) findViewById(R.id.containertem);

                int tbHeight=(int)getContext().getResources().getDimension(R.dimen.actionBarHeight);
                int padding=(int)getContext().getResources().getDimension(R.dimen.padding);


                getWindow().setLayout(width,height-padding);

                if(obj instanceof Piece) {


                    drawView = new DrawView(getContext(),(Piece)obj, imageContainer, width, height - (tbHeight + (padding * 2)));

                }else{


                    ImageView imageView=new ImageView(getContext());

                    imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    imageContainer.addView(imageView);
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                    if(((CustomMaterial)obj).getImage()!=null){
                        imageView.setImageDrawable(((CustomMaterial)obj).getImage());



                    }





                }


               start=false;











    }

    private void loadMenu(){




        toolbar.setNavigationIcon(R.drawable.ic_close_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });



        toolbar.setTitle(title);


        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()){

                    case R.id.action_save:

                        showShare();

                        return true;

                    case R.id.action_print:
                        Utils.toast(getContext(),getContext().getString(R.string.action_print));

                        return true;
                    case R.id.action_send://Enviar
                       Utils.sendImage(getContext(),obj);

                        return true;


                }
                return false;
            }
        });

        toolbar.getMenu().findItem(R.id.action).setIcon(R.drawable.ic_more_vert_black_24dp);

    }



    public void showShare(){


        LayoutInflater li=LayoutInflater.from(getContext());

        View v=li.inflate(R.layout.save_piece_popup,null);

        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());


        final TextView titleText=(TextView) v.findViewById(R.id.titleText);

        titleText.setText(getContext().getResources().getString(R.string.action_save_as));
        v.findViewById(R.id.saveFrame).setVisibility(View.INVISIBLE);

        builder.setView(v);


        final Dialog d=builder.create();

        v.findViewById(R.id.closeFrame).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               d.dismiss();

            }
        });

        final Item[] items = {
                new Item(getContext().getResources().getString(R.string.action_save_as_image), R.drawable.ic_image_black_24dp),
                new Item(getContext().getResources().getString(R.string.action_save_as_cut_file),  R.drawable.ic_insert_drive_file_black_24dp),

        };

      final  ListAdapter adapter = new ArrayAdapter<Item>(
                getContext(),
                android.R.layout.select_dialog_item,
                android.R.id.text1,
                items){
            public View getView(final int position, View convertView, ViewGroup parent) {
                //Use super class to create the View
                View v = super.getView(position, convertView, parent);
                TextView tv = (TextView)v.findViewById(android.R.id.text1);

                //Put the oldImage on the TextView
                tv.setCompoundDrawablesWithIntrinsicBounds(items[position].icon, 0, 0, 0);

                //Add margin between oldImage and text (support various screen densities)
                int dp5 = (int) (5 * getContext().getResources().getDisplayMetrics().density + 0.5f);
                tv.setCompoundDrawablePadding(dp5);
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (position) {

                            case 0:
                                Object[] obj0 = new Object[1];
                                obj0[0] = drawView.getBitmap(600, 600);
                                new importImageAsync().execute(obj0);
                                d.dismiss();
                                break;

                            case 1:


                                Object[] obj1 = new Object[1];
                                obj1[0] = obj;
                                new importCutFileAsync().execute(obj1);
                                d.dismiss();
                                break;
                        }
                    }
                });
                return v;
            }
        };


        d.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                ((ListView)d.getWindow().findViewById((R.id.lisview))).setAdapter(adapter);


                d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
        });
        d.show();
    }

    private boolean saveImageToExternalStorage(Bitmap finalBitmap) {

        ModelDataBase db=new ModelDataBase(getContext());
         File myDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + "/" + modelName);

        if(!myDir.exists()){

            myDir.mkdirs();
        }

       /* Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);*/
        String fname = title + "_" + modelName + ".png";
        File file = new File(myDir, fname);
        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();

            MediaScannerConnection.scanFile(getContext(), new String[]{file.toString()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                            Log.i("ExternalStorage", "Scanned " + path + ":");
                            Log.i("ExternalStorage", "-> uri=" + uri);
                        }
                    });
           return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            Utils.toast(getContext(),getContext().getString(R.string.dialog_saved_unsucessfully));

        }
        return false;

        // Tell the media scanner about the new file so that it is
        // immediately available to the user.


    }

    public class importImageAsync extends AsyncTask<Object, Float, Boolean> {

        protected void onPreExecute() {

            waitPoup = com.colorbuzztechgmail.spf.popup.waitPoup.newInstance("Guardando");

            waitPoup.show(((AppCompatActivity)getOwnerActivity()).getSupportFragmentManager(), "waitPopup");
        }

        protected Boolean doInBackground(Object[] data) {





            try {

              return   saveImageToExternalStorage((Bitmap) data[0]);


            } catch (Exception e) {

                e.printStackTrace();
                Log.e("Background", e.toString());
                Utils.toast(getContext(), "Fallo Busqueda de archivos");


            }
            return false;
        }

        protected void onProgressUpdate(Float... valores) {
            int p = Math.round(valores[0]);
            // ma.mProgressBar.setProgress(p);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {

            final Handler delay = new Handler();
            final long DRAWER_CLOSE_DELAY_MS = 1000;


           delay.postDelayed(new Runnable() {
                @Override
                public void run() {

                    waitPoup.dismiss();


                }
            }, DRAWER_CLOSE_DELAY_MS);


            super.onPostExecute(aBoolean);
        }
    }
    public class importCutFileAsync extends AsyncTask<Object, Float, Boolean> {

        protected void onPreExecute() {

            waitPoup = com.colorbuzztechgmail.spf.popup.waitPoup.newInstance("Guardando");

            waitPoup.show(((AppCompatActivity)getOwnerActivity()).getSupportFragmentManager(), "waitPopup");
        }

        protected Boolean doInBackground(Object[] data) {





            try {


                Piece piece=(Piece) data[0];

                SPFencoder encoder=new SPFencoder(getContext(),piece,true);
                String folder=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/" + encoder.getPreviewModel(piece.getModelId()).getName();
                createFile(folder,piece.getName(),encoder.getData());



            } catch (Exception e) {

                e.printStackTrace();
                Log.e("Cutfile", e.toString());
                Utils.toast(getContext(), "Fallo en el guardado del archivo de corte");


            }
            return false;
        }

        protected void onProgressUpdate(Float... valores) {
            int p = Math.round(valores[0]);
            // ma.mProgressBar.setProgress(p);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {

            final Handler delay = new Handler();
            final long DRAWER_CLOSE_DELAY_MS = 1000;


            delay.postDelayed(new Runnable() {
                @Override
                public void run() {

                    waitPoup.dismiss();


                }
            }, DRAWER_CLOSE_DELAY_MS);


            super.onPostExecute(aBoolean);
        }
    }

    private boolean createFile(String folder,String fileName, List<String> data) {

        File myDir = new File(folder);

        try {

            if(!myDir.exists()){

                myDir.mkdirs();
            }

            Writer output = null;
            File file = new File(myDir, fileName + ".spf");
            if (file.exists())
                file.delete();
            output = new BufferedWriter(new FileWriter(file));
            if (data != null) {

                for(String line :data)
                output.write(line+'\n');
            }
            output.close();
            return true;
        } catch (IOException ioException) {

            Log.e("Import", ioException.toString());
            return false;
        }

    }



    public static class Item{
        public final String text;
        public final int icon;
        public Item(String text, Integer icon) {
            this.text = text;
            this.icon = icon;
        }
        @Override
        public String toString() {
            return text;
        }
    }

}











