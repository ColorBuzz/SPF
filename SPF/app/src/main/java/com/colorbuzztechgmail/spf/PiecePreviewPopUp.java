package com.colorbuzztechgmail.spf;

import android.app.Dialog;
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
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by PC01 on 18/06/2017.
 */
public class PiecePreviewPopUp extends  DialogFragment {


     private static final String PIECE_ID="pieceId";

     private static final String MODEL_ID ="modelId";
     private int position=0;
      Piece piece;
      boolean start=true;
    Toolbar toolbar;
    DrawView drawView;
    waitPoup waitPoup;



    public static PiecePreviewPopUp newInstance(@NonNull int modelId, @NonNull int pieceId ) {
        PiecePreviewPopUp f = new PiecePreviewPopUp();
        Bundle args = new Bundle();
        args.putInt(PIECE_ID, pieceId);
         args.putInt(MODEL_ID,modelId);



        f.setArguments(args);


        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {



        super.onResume();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        LayoutInflater li = LayoutInflater.from(getContext());

        final View promptsView = li.inflate(R.layout.assigment_popup, null);


        ((LinearLayout)promptsView.findViewById(R.id.containertem)).removeViewAt(1);

        ModelDataBase db=new ModelDataBase(getContext());
        final int  modelID=getArguments().getInt(MODEL_ID);
        final int  pieceId=getArguments().getInt(PIECE_ID);
        toolbar=(Toolbar)promptsView.findViewById(R.id.pieceToolbar);
        toolbar.removeAllViews();

        toolbar.inflateMenu(R.menu.menu_model);


        piece=db.getPiece(modelID,pieceId,null,null,false);

        loadMenu();

        return new AlertDialog.Builder(getActivity())


                .setView(promptsView)
                .create();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NORMAL, R.style.MY_DIALOG);


    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog d = getDialog();



            if (d!=null && start) {


                d .getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                int width =(getActivity()).getWindowManager().getDefaultDisplay().getWidth();
                int height =(getActivity()).getWindowManager().getDefaultDisplay().getHeight();



                LinearLayout imageContainer = (LinearLayout) d.findViewById(R.id.containertem);

                int tbHeight=(int)getContext().getResources().getDimension(R.dimen.actionBarHeight);
                int padding=(int)getContext().getResources().getDimension(R.dimen.padding);


                d.getWindow().setLayout(width,height-padding);


              drawView = new DrawView(getContext(), piece,imageContainer, width, height-(tbHeight+(padding*2)));


               start=false;






            }





    }

    private void loadMenu(){




        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_selector);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        toolbar.setTitle(piece.getName() + "-" + piece.getSize()+ "-" +  piece.getMaterial());


        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()){

                    case R.id.action_save:

                        showShare();

                        return true;

                    case R.id.action_print:
                        Utils.toast(getContext(),getString(R.string.action_print));

                        return true;
                    case R.id.action_send://Enviar
                       Utils.sendImage(getContext(),piece);

                        return true;


                }
                return false;
            }
        });

        toolbar.getMenu().findItem(R.id.action).setIcon(R.drawable.ic_more_vert_black_24dp);

    }




    @Override
    public void onDismiss(DialogInterface dialog) {


        super.onDismiss(dialog);
    }

    public void showShare(){


        LayoutInflater li=LayoutInflater.from(getContext());

        View v=li.inflate(R.layout.save_piece_popup,null);

        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        ((Toolbar)v.findViewById(R.id.pieceToolbar)).setTitle(getResources().getString(R.string.action_save_as));
        builder.setView(v);


        final Dialog d=builder.create();


        final Item[] items = {
                new Item(getResources().getString(R.string.action_save_as_image), R.drawable.ic_image_black_24dp),
                new Item(getResources().getString(R.string.action_save_as_cut_file),  R.drawable.ic_insert_drive_file_black_24dp),

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

                //Put the image on the TextView
                tv.setCompoundDrawablesWithIntrinsicBounds(items[position].icon, 0, 0, 0);

                //Add margin between image and text (support various screen densities)
                int dp5 = (int) (5 * getResources().getDisplayMetrics().density + 0.5f);
                tv.setCompoundDrawablePadding(dp5);
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (position) {

                            case 0:
                                Object[] obj = new Object[1];
                                obj[0] = drawView.getBitmap(600, 600);
                                new importImageAsync().execute(obj);
                                d.dismiss();
                                break;

                            case 1:


                                Object[] obj1 = new Object[1];
                                obj1[0] = piece;
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
        final PreviewModelInfo model=db.getPreviewModel(getArguments().getInt(MODEL_ID));
        File myDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + "/" + model.getName());

        if(!myDir.exists()){

            myDir.mkdirs();
        }

       /* Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);*/
        String fname = piece.getName()+"_"+piece.getSize()+"_" + piece.getMaterial() + "_" + model.getName() + ".png";
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
            Utils.toast(getContext(),getString(R.string.dialog_saved_unsucessfully));

        }
        return false;

        // Tell the media scanner about the new file so that it is
        // immediately available to the user.


    }

    public class importImageAsync extends AsyncTask<Object, Float, Boolean> {

        protected void onPreExecute() {

            waitPoup = com.colorbuzztechgmail.spf.waitPoup.newInstance("Guardando");

            waitPoup.show(getFragmentManager(), "waitPopup");
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

            waitPoup = com.colorbuzztechgmail.spf.waitPoup.newInstance("Guardando");

            waitPoup.show(getFragmentManager(), "waitPopup");
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











