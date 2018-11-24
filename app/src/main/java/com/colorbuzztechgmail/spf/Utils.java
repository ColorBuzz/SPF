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
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.colorbuzztechgmail.spf.popup.MaterialAsigmentoPopUp;
import com.colorbuzztechgmail.spf.popup.PieceEditPopUp;
import com.colorbuzztechgmail.spf.popup.PieceListPopUp;
import com.colorbuzztechgmail.spf.popup.PreviewPopUp;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;


public class Utils implements Cloneable{



    ///------------- Views Utils------------------------/////////////

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

    public static void toast(Context context, String string,int gravity){


        Toast toast=Toast.makeText(context, string,Toast.LENGTH_SHORT);

        toast.setGravity(gravity,0,0);

        toast.show();

    }

    public static void disableShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                item.setShiftingMode(false);
                // set once again checked value, so view will be updated
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {
            //Timber.e(e, "Unable to get shift mode field");
        } catch (IllegalAccessException e) {
            //Timber.e(e, "Unable to change value of shift mode");
        }
    }

    public static enum ViewMode{

        LIST,
        GRID


    }

    //----------------Pop Up Widnows--------------------------------------///////

    public static void showInfoPopUp(final Context context, final Object obj){


        final Dialog d = new Dialog(context);
        d.setCanceledOnTouchOutside(true);



        LayoutInflater li= LayoutInflater.from(context);
        View v=li.inflate(R.layout.animated_info_container,null);

        final FrameLayout container=(FrameLayout) v.findViewById(R.id.di);

        int mode=-1;

        if(obj instanceof Piece){
            mode=MaterialinfoContainer.MODE_SUBMENU_PIECE;
        }else if (obj instanceof Material) {
        mode=MaterialinfoContainer.MODE_MATERIAL;
        }else if (obj instanceof CutNote) {
        mode=MaterialinfoContainer.MODE_CUTNOTE;
        }
        final MaterialinfoContainer infoContainer= new MaterialinfoContainer(context,mode);


       final ImageView imageBar=(ImageView) v.findViewById(R.id.app_bar_image);
        final Toolbar toolbar=(Toolbar) v.findViewById(R.id.appbar2);
         final AppBarLayout appBarLayout=(AppBarLayout)v.findViewById(R.id.appbar);


        View closeButton=li.inflate(R.layout.button_layout,null);

        toolbar.addView(closeButton,new Toolbar.LayoutParams(Gravity.RIGHT));

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d.dismiss();
            }
        });

        d.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                d.getWindow().getDecorView().setBackground(new ColorDrawable(Color.TRANSPARENT));
                if(obj instanceof Piece){

                    infoContainer.startAnimation(true,obj);
                    imageBar.setImageDrawable(((Piece)obj).getImage());
                    toolbar.setTitle(((Piece)obj).getName());
                    toolbar.setLogo(context.getDrawable(R.drawable.ic_piece_black_small));



                }else if (obj instanceof Material){

                    infoContainer.startAnimation(true,obj);
                    imageBar.setImageDrawable(((Material)obj).getImage());
                    toolbar.setTitle(((Material)obj).getName());
                    toolbar.setLogo(context.getDrawable(R.drawable.ic_leather_black_24dp));

                }else if (obj instanceof CutNote){

                    infoContainer.startAnimation(true,obj);

                    toolbar.setTitle(String.valueOf(((CutNote)obj).getReference()) + "/" +String.valueOf(((CutNote)obj).getNoteNumber()));
                    toolbar.setLogo(context.getDrawable(R.drawable.ic_assignment_black_24dp));
                    appBarLayout.setExpanded(false);
                    appBarLayout.canScrollVertically(0);




                }

            }
        });


        container.addView(infoContainer.getView());

        d.setContentView(v);
        d.create();
        int width =(d.getWindow().getWindowManager().getDefaultDisplay().getWidth()-((int)context.getResources().getDimension(R.dimen.padding)*2));


        float factor=width*(16f/9f);
        float factor1=width/(16f/9f);

        int height =width;

        d.getWindow().setLayout(width,Utils.pxTodp ((int) factor,context));
        imageBar.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,Utils.pxTodp ((int) factor1,context)));

        d.show();


    }

    public static void showPieceList(AppCompatActivity activity,Object obj){
        PieceListPopUp pieceListPopUp;
        if(obj instanceof Piece){

          pieceListPopUp = PieceListPopUp.newInstance(String.valueOf(((Piece)obj).getId()),PieceListAdapter.MODE_SIZE,((Piece)obj).getModelId(),null );
          pieceListPopUp.show(  activity.getSupportFragmentManager(),"pieceListDialog");

        }else if (obj instanceof Material){


            pieceListPopUp = PieceListPopUp.newInstance(null,PieceListAdapter.MODE_MATERIAL,((Material)obj).getModelId(),((Material)obj).getName() );
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

               PreviewPopUp materialPopUp= new PreviewPopUp(activity,null,obj);

               materialPopUp.setOwnerActivity(activity);
               materialPopUp.show();
           }

    }

    public static void showMaterialAssigment(AppCompatActivity activity,Material material,onSavedInterface savedInterface){



       final MaterialAsigmentoPopUp poupMaterialAsigment=  MaterialAsigmentoPopUp.newInstance(material.getModelId(),material.getId(),material.getName());
        poupMaterialAsigment.setSavedInterface(savedInterface);

        poupMaterialAsigment.show( activity.getSupportFragmentManager(),"materialAssigment");
    }

    public static void deleteAssignedMaterial(Context context,Material material){

        ModelDataBase db= new ModelDataBase(context);
        material.setCustomMaterialId(0);
        material.setImage(new ColorDrawable(Color.TRANSPARENT));
         db.updateMaterial(material.getModelId(),material.getId(),null,0);
    }

    public interface onSavedInterface{


        void onSaved(Object obj,int position,boolean isEditable);

    }




    ///---------------- Data Utils--------------------////////////

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

    public static byte[] prepareObjectToByte(Object object) {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = null;
        byte[] container = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(object);
            out.flush();
            container = bos.toByteArray();

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Import", "FALLO EN LA CONVERISON A BYTE");
            Log.e("Import", e.toString());

        } finally {
            try {
                bos.close();
            } catch (IOException ex) {


                // ignore close exception
            }
        }
        return container;

    }

    public static void sendImage(Context context,Object obj){

       Object[] arrayObjs= new Object[2];

       if(obj instanceof Piece){

           arrayObjs[0]=  ShapeGenerator.DrawableToBitmap(((Piece)obj).getImage());
           arrayObjs[1]=((Piece)obj).getName()+ "_"+((Piece)obj).getSize()+"_"+((Piece)obj).getMaterial();


       }else  if(obj instanceof CustomMaterial){


           arrayObjs[0]=ShapeGenerator.DrawableToBitmap(((CustomMaterial)obj).getImage());
           arrayObjs[1]=((CustomMaterial)obj).getName()+ "_"+((CustomMaterial)obj).getDealership()+"_"+String.valueOf(((CustomMaterial)obj).getSeasons());


       }





        new saveBitmapToDisk(context,arrayObjs);




    }

    private static class saveBitmapToDisk extends AsyncTask<Object,Void, File> {
        Boolean toShare;
        private Context context ;

        public void setContext(Context context) {
            this.context = context;
        }

        public saveBitmapToDisk(Context context, Object objs) {
            this.context = context;
            execute(objs);
        }

        @Override
        protected File doInBackground(Object[] objs) {
            try {
                File file = new File(context.getExternalFilesDir(null), objs[1]+".png");
                if(file.exists()){

                    file.delete();
                }
                FileOutputStream fOut = new FileOutputStream(file);


                Bitmap saveBitmap =(Bitmap)objs[0];
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

            shareIntent.setType("oldImage/png");



            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));

            context.startActivity(Intent.createChooser(shareIntent, ""));

        }
    }

    public static void addImageToGallery(final String filePath, final Context context) {

        ContentValues values = new ContentValues();

        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "oldImage/jpeg");
        values.put(MediaStore.MediaColumns.DATA, filePath);

        context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }

    ///------------- Conversion Data Utils--------------///////////

    public static float FormatDecimal (float value){


        DecimalFormat dec = new DecimalFormat("##0.00", DecimalFormatSymbols.getInstance(Locale.UK));
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setDecimalSeparator('.');
        dec.setDecimalFormatSymbols(dfs);
        dec.setMaximumFractionDigits(3);

        return new Float(dec.format(value));

    }

    public static String getSizeListString(CutNote cutNote ){

        final List<String> sizeList=new ArrayList<>();

        for(String size :cutNote.getSizeList().keySet()){

            if(cutNote.getCountAtSize(size)>0){

                sizeList.add(size);

            }

        }

        Collections.sort(sizeList);

        if(sizeList.size()>1){

            return "SURTIDO" + sizeList.toString();
        }

        return sizeList.toString();

    }

    public static String convertStatusToString(Context mContext, CutNote.cutNoteStatus status){


        switch (status){

            case FINISHED:

                return mContext.getResources().getString(R.string.cutNotes_status_finished);
            case IN_PROGRESS:
                return mContext.getResources().getString(R.string.cutNotes_status_in_progress);
            case INDETERMINATE:
                return mContext.getResources().getString(R.string.cutNotes_status_indeterminated);

            default:
                return mContext.getResources().getString(R.string.cutNotes_status_indeterminated);






        }


    }

    public static CutNote.cutNoteStatus convertStringToStatus(Context mContext, String value){


        if (value.equals(mContext.getResources().getString(R.string.cutNotes_status_finished))) {

            return CutNote.cutNoteStatus.FINISHED ;
        }else if(value.equals(mContext.getResources().getString(R.string.cutNotes_status_in_progress))) {

            return CutNote.cutNoteStatus.IN_PROGRESS ;

        }else if( value.equals(mContext.getResources().getString(R.string.cutNotes_status_indeterminated))) {

            return CutNote.cutNoteStatus.INDETERMINATE ;



        }else {

            return CutNote.cutNoteStatus.INDETERMINATE ;

        }


    }

    public static int dpToPx(int dp,Context context) {
        Resources r = context.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public static int pxTodp(int px,Context context) {
        Resources r = context.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, px, r.getDisplayMetrics()));
    }

    public static int toIntExact(long value) {
        if ((int)value != value) {
            throw new ArithmeticException("integer overflow");
        }
        return (int)value;
    }

    public static Float calculateProporcionalArea(float baseArea,float targetArea,float amount){

        NumberFormat decimalFormat= new DecimalFormat("#0.00") ;
        NumberFormat formatter = NumberFormat.getInstance(Locale.US);
        formatter.setMaximumFractionDigits(3);
        formatter.setMinimumFractionDigits(3);
        formatter.setRoundingMode(RoundingMode.HALF_EVEN);
        float area=0f;

        area=(targetArea*amount)/baseArea;


        return new Float(formatter.format(area));
    }

    ///----------------- Generator Data Utils--------///////////



    public static String getDate(){
        SimpleDateFormat dateFormat= new SimpleDateFormat("dd/MM/yyyy\nHH:mm:ss");
        Date auxdate = new Date();

        return  dateFormat.format(auxdate);
    }

    public static String getDate(long value){


        SimpleDateFormat dateFormat= new SimpleDateFormat("dd/MM/yyyy\nHH:mm:ss");
        Date lastModified = new Date(value);

        return dateFormat.format(lastModified);

    }

    public static int getRandomColor(){

        Random rnd=new Random();
        return  0xFF000000 | rnd.nextInt(0xFFFFFF);

    }

    public static class IdMaker {
        static final AtomicInteger atomicInteger = new AtomicInteger();


        public  static boolean reset(){

            atomicInteger.set(0);
            return true;

        }


        public static int next() {
            return atomicInteger.incrementAndGet();
        }
    }

    public static Drawable getImageAtStatus (Context context,CutNote.cutNoteStatus status ) {

        int color=0;

        switch (status){

            case INDETERMINATE:

                color=context.getResources().getColor(R.color.colorRed_A400);
                break;

            case IN_PROGRESS:
                color=context.getResources().getColor(R.color.colorAmber_A400);

                break;

            case FINISHED:
                color=context.getResources().getColor(R.color.colorGreen_A400);

                break;


        }

      return (ShapeGenerator.getShape(context,ShapeGenerator.MODE_ROUND_RECT,(int)context.getResources().getDimension(R.dimen.profile_pic_small_size),(int)context.getResources().getDimension(R.dimen.profile_pic_small_size),color));


    }

    public static void imagePicker(Activity activity, int RequestCode){

         Intent chooseImageIntent = ImagePicker.getPickImageIntent(activity);
        activity.startActivityForResult(chooseImageIntent,RequestCode);

    }

}
