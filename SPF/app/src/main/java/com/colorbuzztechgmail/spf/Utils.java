package com.colorbuzztechgmail.spf;

/**
 * Created by PC01 on 20/06/2017.
 */

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;


public class Utils implements Cloneable{

    public static void setMargins (View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);

            v.requestLayout();
        }
    }
    public static void toast(Context context, String string){


        Toast toast=Toast.makeText(context, string,Toast.LENGTH_SHORT);

        toast.setGravity(Gravity.CENTER|Gravity.BOTTOM,0,0);

        toast.show();

    }

    public static void addImageToGallery(final String filePath, final Context context) {

        ContentValues values = new ContentValues();

        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.MediaColumns.DATA, filePath);

        context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }

    public static String objectToJson(Object values){

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        return gson.toJson(values).toString();
    }

    public static Object jsonToObject(String json){

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        return gson.fromJson(json,Object.class);
    }

    public static String getDate(){
        SimpleDateFormat dateFormat= new SimpleDateFormat("dd/MM/yyyy\nHH:mm:ss");
        Date auxdate = new Date();

        return  dateFormat.format(auxdate);
    }


    public interface onSavedInterface{


        void onSaved(Object obj,int position,boolean isEditable);

    }

    public static void showInfoPopUp(Context context, final Object obj){


        final Dialog d = new Dialog(context);



        LayoutInflater li= LayoutInflater.from(context);
        View v=li.inflate(R.layout.piece_popup,null);

        final FrameLayout container=(FrameLayout) v.findViewById(R.id.di);

        int mode=-1;

        if(obj instanceof Piece){
            mode=MaterialinfoContainer.MODE_SUBMENU_PIECE;
        }else if (obj instanceof Material) {
            mode=MaterialinfoContainer.MODE_MATERIAL;
        }
        final MaterialinfoContainer infoContainer= new MaterialinfoContainer(context,mode);


       final Toolbar toolbar=(Toolbar)v.findViewById(R.id.toolbar2);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_selector);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });

        d.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                d.getWindow().getDecorView().setBackground(new ColorDrawable(Color.TRANSPARENT));
                if(obj instanceof Piece){
                    d.getWindow().setLayout(550,550);

                    infoContainer.startAnimation(MaterialinfoContainer.MODE_SUBMENU_PIECE,true,null,(Piece)obj);
                    toolbar.setLogo(((Piece)obj).getImage());

                }else if (obj instanceof Material){
                    d.getWindow().setLayout(550,650);

                    infoContainer.startAnimation(MaterialinfoContainer.MODE_MATERIAL,true,(Material)obj,null);
                    toolbar.setLogo(((Material)obj).getImage());

                }

            }
        });


        container.addView(infoContainer.getView());

        d.setContentView(v);
        d.create();
        d.getWindow().setLayout(infoContainer.getMeasuredWidth(),infoContainer.getMeasuredHeight()+toolbar.getHeight());
        d.show();


    }

    public static void showPieceList(AppCompatActivity activity,Object obj){
        PieceListPopUp pieceListPopUp;
        if(obj instanceof Piece){

          pieceListPopUp = PieceListPopUp.newInstance(String.valueOf(((Piece)obj).getId()),PieceListAdapter.MODE_SIZE,((Piece)obj).getModelId(),null );
          pieceListPopUp.show(  activity.getSupportFragmentManager(),"pieceListDialog");

        }else if (obj instanceof Material){


            pieceListPopUp = PieceListPopUp.newInstance(null,PieceListAdapter.MODE_MATERIAL,((Piece)obj).getModelId(),((Material)obj).getName() );
            pieceListPopUp.show(  activity.getSupportFragmentManager(),"pieceListDialog");

        }





    }

    public static void showEditPiece(AppCompatActivity activity,Piece piece,onSavedInterface savedInterface){



        PieceEditPopUp mpopUp= PieceEditPopUp.newInstance(piece.getName(),piece.getId(),piece.getModelId());
        mpopUp.setSavedInterface(savedInterface);

        mpopUp.show(activity.getSupportFragmentManager(),"editPiece");


    }

    public static void showPreview(AppCompatActivity activity,Object obj){

           if(obj instanceof Piece){

               PiecePreviewPopUp piecePopUp = PiecePreviewPopUp.newInstance(((Piece)obj).getModelId(), ((Piece)obj).getId());

               piecePopUp.show(activity.getSupportFragmentManager(), "piecePopUp");

           }else if(obj instanceof Material){

               byte[] image=ModelDataBase.imageViewtoByte(((Material)obj).getImage());

               MaterialPreviewPopUp materialPopUp= MaterialPreviewPopUp.newInstance(image,((Material)obj).getName());

               materialPopUp.show(activity.getSupportFragmentManager(),"materialPreview");
           }

    }

    public static void showMaterialAssigment(AppCompatActivity activity,Material material,onSavedInterface savedInterface){

        ArrayList<String>piecesId=new ArrayList<>();


        if(material.getPiecesId().size()>0){

            for (String id:material.getPiecesId()){

                piecesId.add(id);

            }


        }


       final MaterialAsigmentoPopUp poupMaterialAsigment=  MaterialAsigmentoPopUp.newInstance(material.getModelId(),material.getId(),material.getName(),piecesId);
        poupMaterialAsigment.setSavedInterface(savedInterface);

        poupMaterialAsigment.show( activity.getSupportFragmentManager(),"materialAssigment");
    }

    public static void deleteAssignedMaterial(Context context,Material material){

        ModelDataBase db= new ModelDataBase(context);
        material.setCustomMaterialId("0");
        material.setImage(new ColorDrawable(Color.TRANSPARENT));
         db.updateMaterial(material.getModelId(),material.getId(),null,"0",null);
    }






    public static int toIntExact(long value) {
        if ((int)value != value) {
            throw new ArithmeticException("integer overflow");
        }
        return (int)value;
    }

    public static int getRandomColor(){

        Random rnd=new Random();
        return  0xFF000000 | rnd.nextInt(0xFFFFFF);

    }

    public static class IdMaker {
        static final AtomicInteger atomicInteger = new AtomicInteger();

        static int next() {
            return atomicInteger.incrementAndGet();
        }
    }

    public static enum ViewMode{

        LIST,
        GRID


    }

    public static void sendImage(Context context,Piece piece){

        Piece[] pieces= new Piece[1];

        pieces[0]=piece;

        new saveBitmapToDisk(context,pieces);




    }

    private static class saveBitmapToDisk extends AsyncTask<Piece,Void, File> {
        Boolean toShare;
        private Context context ;

        public void setContext(Context context) {
            this.context = context;
        }

        public saveBitmapToDisk(Context context, Piece [] pieces) {
            this.context = context;
            execute(pieces);
        }

        @Override
        protected File doInBackground(Piece... piece) {
            try {
                File file = new File(context.getExternalFilesDir(null), piece[0].getName()+ "_"+piece[0].getSize()+"_"+ piece[0].getMaterial()+".png");
                if(file.exists()){

                    file.delete();
                }
                FileOutputStream fOut = new FileOutputStream(file);

                DrawView drawView= new DrawView(context,piece[0],null,0,0);
                Bitmap saveBitmap = drawView.getBitmap(600,600);
                saveBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                fOut.flush();
                fOut.close();
                return file;
            } catch (OutOfMemoryError e) {
                Log.e("MainActivity", "Out of Memory saving bitmap; bitmap is too large");
                return null;
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage());
                return null;
            }

        }

        @Override
        protected void onPostExecute(File file) {
            final Intent shareIntent = new Intent(Intent.ACTION_SEND);

            shareIntent.setType("image/png");



            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));

            context.startActivity(Intent.createChooser(shareIntent, ""));

        }
    }

}
