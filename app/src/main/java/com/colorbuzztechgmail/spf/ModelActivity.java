package com.colorbuzztechgmail.spf;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ModelActivity extends AppCompatActivity implements View.OnClickListener {



     public ViewPager sViewpager;
     public PreviewModelInfo model;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.model_profile_layout);


       ModelDataBase db=new ModelDataBase(this);
        final int modelId=Integer.valueOf(getIntent().getStringExtra("MODEL"));
        model=db.getPreviewModelById(modelId);

        findViewById(R.id.piece_layout).setOnClickListener(this);
        findViewById(R.id.material_layout).setOnClickListener(this);
        findViewById(R.id.accessories_layout).setOnClickListener(this);
        findViewById(R.id.cutNotes_layout).setOnClickListener(this);
        findViewById(R.id.externalWork_layout).setOnClickListener(this);

/*        nestedScrollView=(NestedScrollView)findViewById(R.id.nestedScroll);





        sViewpager=(ViewPager)findViewById(R.id.container);

        setupViewPage(sViewpager);



        tabGenerator();*/
        loadMenu();

        if(model.getImage()!=null){


            ((ImageView)findViewById(R.id.app_bar_image)).setImageDrawable(model.getImage());

        }


    }

    private void loadMenu(){



       Toolbar toolbar=(Toolbar)findViewById(R.id.appbar2);



        toolbar.setNavigationIcon(R.drawable.ic_arrow_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });



        toolbar.setTitle(model.getName());
        toolbar.setSubtitle("Piezas :" +String.valueOf(model.getPieceCount()));


        toolbar.inflateMenu(R.menu.menu_model);






    }

    private void tabGenerator(){
        TabLayout tabLayout;
        tabLayout=(TabLayout)findViewById(R.id.tabs);

        tabLayout.setupWithViewPager(sViewpager);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_shoe_selector);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_leather_selector);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_info_selector);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_person_selector);

    }


    private void showMenuListPopupWindow(View anchor) {
        final ListPopupWindow popupWindow = new ListPopupWindow(this);

        List<ListPopUpMenuItem> itemList = new ArrayList<>();
        itemList.add(new ListPopUpMenuItem(getString(R.string.action_save_as), R.drawable.ic_insert_drive_file_black_24dp));
        itemList.add(new ListPopUpMenuItem(getString(R.string.action_print), R.drawable.ic_print_black_24dp));
        itemList.add(new ListPopUpMenuItem(getString(R.string.action_send), R.drawable.ic_share_black_24dp));


        ListPopupWindowAdapter adapter = new ListPopupWindowAdapter(this, itemList);
        popupWindow.setAnchorView(anchor);
        popupWindow.setListSelector(getResources().getDrawable(R.drawable.checkedlayout));
        popupWindow.setContentWidth((int) (150 * getResources().getDisplayMetrics().density + 0.5f));
        popupWindow.setAdapter(adapter);
        popupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                popupWindow.dismiss();

                switch (i){

                    case 0://Guardar
                        //showSaveType();
                        showShare();
                        break;
                    case 1://Imprimir
                        Utils.toast(getBaseContext(),getString(R.string.action_print));
                        break;
                    case 2://Enviar

                        break;


                }


            }
        });
        popupWindow.show();
    }

    public void showShare(){


        LayoutInflater li=LayoutInflater.from(this);

        View v=li.inflate(R.layout.save_piece_popup,null);

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        ((Toolbar)v.findViewById(R.id.pieceToolbar)).setTitle(getResources().getString(R.string.action_save_as));
        builder.setView(v);


        final Dialog d=builder.create();


        final PiecePreviewPopUp.Item[] items = {
                new PiecePreviewPopUp.Item(getResources().getString(R.string.action_save_as_cut_file),  R.drawable.ic_insert_drive_file_black_24dp),

        };

        final ListAdapter adapter = new ArrayAdapter<PiecePreviewPopUp.Item>(
               this,
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


    public void sendImage(Piece piece){

        Piece[] pieces= new Piece[1];

        pieces[0]=piece;

       // new MainActivity.saveBitmapToDisk().execute(pieces);




    }

    @Override
    public void onClick(View view) {


        switch (view.getId()){


            case R.id.piece_layout:

              /*  Intent i = new Intent(this,Piece_Activity.class);
                i.putExtra("MODEL",String.valueOf(model.getId()));
                startActivity(i);*/
                 PiecelItemActivity.newInstancePieceActivity(this,model.getId(),getString(R.string.piece_Amount));

                break;

            case R.id.material_layout:

                Intent j = new Intent(this,MaterialActivity.class);
                j.putExtra("MODEL",String.valueOf(model.getId()));
                startActivity(j);
                break;

            case R.id.cutNotes_layout:


                Intent g = new Intent(this,CutNoteListActivity.class);
                g.putExtra("MODEL",String.valueOf(model.getId()));
                startActivity(g);
                break;



        }
    }

   /* private class saveBitmapToDisk extends AsyncTask<Piece,Void, File> {
        Boolean toShare;

        @Override
        protected File doInBackground(Piece... piece) {
            Context context = getBaseContext();
            try {
                File file = new File(context.getExternalFilesDir(null), piece[0].getName()+ "_"+piece[0].getSize()+"_"+ piece[0].getMaterial()+".png");
                if(file.exists()){

                    file.delete();
                }
                FileOutputStream fOut = new FileOutputStream(file);

                DrawView drawView= new DrawView(getBaseContext(),piece[0],null,0,0);
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

            startActivity(Intent.createChooser(shareIntent, ""));

        }
    }*/


}
