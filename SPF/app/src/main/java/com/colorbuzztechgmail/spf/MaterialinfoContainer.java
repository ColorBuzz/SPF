package com.colorbuzztechgmail.spf;

import android.content.Context;

import android.databinding.DataBindingUtil;

import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
 import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
 import android.view.animation.AnimationUtils;
 import android.widget.LinearLayout;
import android.widget.ProgressBar;


import com.colorbuzztechgmail.spf.databinding.MaterialInfoBinding;
 import com.colorbuzztechgmail.spf.databinding.PieceInfodBinding;
 import com.colorbuzztechgmail.spf.RecyclerSliderAdapter.Preview;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by PC01 on 28/01/2018.
 */



public class MaterialinfoContainer extends View implements View.OnClickListener,StopLoadPieces{

    private View v,assigmeBtn;
    MainActivity ma;
      boolean visible;

    public static final int MODE_MATERIAL = 1;
    public static final int MODE_SUBMENU_MATERIAL = 2;
    public static final int MODE_SUBMENU_PIECE = 3;
    private SearchFilesAsync filesAsync;
    private RecyclerView recyclerPiece;


    public StopLoadPieces getStopListener(){


        return this;

    }


    public MaterialinfoContainer(Context context, int mode) {
        super(context);
        final LayoutInflater li = LayoutInflater.from(context);

        if(mode==MODE_SUBMENU_PIECE){

            v = li.inflate(R.layout.animated_info_piece_layout, null);



        }else if(mode==MODE_SUBMENU_MATERIAL) {
            v = li.inflate(R.layout.animated_info_layout, null);

            recyclerPiece=new RecyclerView(getContext());
            final LinearLayoutManager lm = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
            recyclerPiece.setLayoutManager(lm);
            recyclerPiece.setHasFixedSize(true);



        }else if(mode==MODE_MATERIAL) {
            v = li.inflate(R.layout.animated_info_layout, null);
        }


        assigmeBtn=li.inflate(R.layout.assigment_material_button,null);

       // v.findViewById(R.id.container).setVisibility( GONE);





        this.setView(v);
         v.setOnClickListener(this);


    }



    public View getView() {
        return v;
    }

    public void setView(View v) {
        this.v = v;
    }

    public void startAnimation(int mode,boolean enabled,Material material,Piece piece) {

        ///mode 1 Custom Material
        ///mode 2 Model-subMenu Material
        ///mode 3 Model-subMenu Pieces

        if (enabled) {


            setVisible(true);


            switch (mode) {

                case MODE_MATERIAL:

                    if (material != null) {
                        v.findViewById(R.id.addmaterial).setVisibility( GONE);

                        final MaterialInfoBinding materialInfodBinding= DataBindingUtil.bind(v);

                        materialInfodBinding.setVariable(BR.obj,material);

                        materialInfodBinding.notifyChange();


                    }
                    break;


                case MODE_SUBMENU_MATERIAL:
                    if (material != null) {

                           // showPieceSlider(material);


                        if (!(material.getCustomMaterialId().equals("0"))) {

                            v.findViewById(R.id.addmaterial).setVisibility( GONE);
                            v.findViewById(R.id.container).setVisibility( VISIBLE);

                            ModelDataBase db = new ModelDataBase(getContext());


                            material = db.getCustomMaterial(Integer.valueOf(material.getCustomMaterialId()));

                          final MaterialInfoBinding materialInfodBinding= DataBindingUtil.bind(v);

                            materialInfodBinding.setVariable(BR.obj,material);


                            materialInfodBinding.notifyChange();

                        }else{
                            v.findViewById(R.id.container).setVisibility( GONE);

                            v.findViewById(R.id.addmaterial).setVisibility( VISIBLE);


                        }



                    }
                    break;

                case MODE_SUBMENU_PIECE:

                    if (piece != null ) {
                       final PieceInfodBinding pieceInfodBinding= DataBindingUtil.bind(v);

                        pieceInfodBinding.setVariable(BR.obj,piece);

                        pieceInfodBinding.notifyChange();


                    }
                    break;


            }


            if (isVisible()) {

                v.findViewById(R.id.textView1).startAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in));
                v.findViewById(R.id.textView2).startAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in));
                v.findViewById(R.id.textView3).startAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in));
                v.findViewById(R.id.textView4).startAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in));
                v.findViewById(R.id.textView5).startAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in));
                v.findViewById(R.id.textView6).startAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in));
                v.findViewById(R.id.subTextView1).startAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.slide_in_left));
                v.findViewById(R.id.subTextView2).startAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.slide_in_left));
                v.findViewById(R.id.subTextView3).startAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.slide_in_left));
                v.findViewById(R.id.subTextView4).startAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.slide_in_left));
                v.findViewById(R.id.subTextView5).startAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.slide_in_left));
                v.findViewById(R.id.subTextView6).startAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.slide_in_left));
            }

        } else {
            setVisible(false);
            switch (mode) {

                case MODE_SUBMENU_MATERIAL:


/*
                        showPieceSlider(material);




                    if(v.findViewById(R.id.addmaterial).getVisibility()!=View.VISIBLE) {

                        ((LinearLayout) v.findViewById(R.id.addmaterial)).setVisibility(VISIBLE);
                    }*/
                    break;

                    }




        }



    }



    @Override
    public void onClick(View v) {



    }

    public void setVisible(boolean enabled){

        visible=enabled;

        if(enabled){
            v.findViewById(R.id.container).setVisibility( VISIBLE);

        }else{


            v.findViewById(R.id.container).setVisibility( GONE);


        }

    }


    public boolean isVisible(){


        return visible;

    }

    @Override
    public void onStopLoad() {


        if(filesAsync!=null){
            filesAsync.cancel(true);




        }

    }


    public class SearchFilesAsync extends AsyncTask<Object, Float,List<Preview>> {

        protected void onPreExecute() {




        }

        protected List<Preview> doInBackground(Object[] material) {



            List<Preview> previews= new ArrayList<>();


            try {
               Material material1= (Material) material[0];

                ModelDataBase db = new ModelDataBase(getContext());


                for (String id : material1.getPiecesId()) {

                    final Piece  piece = db.getPiece(material1.getModelId(), Integer.valueOf(id), null, null,true);

                    final Preview preview= new Preview( piece.getId(), piece.getName(),piece.getImage());

                        previews.add(preview);



                }



                return previews;

            } catch (Exception e) {

                e.printStackTrace();
                Log.e("Background", e.toString());


            }
            return previews;
        }

        protected void onProgressUpdate(Float... valores) {
            int p = Math.round(valores[0]);
            // ma.mProgressBar.setProgress(p);
        }

        @Override
        protected void onPostExecute(List<Preview> previews) {
            ((LinearLayout) v.findViewById(R.id.imageContainer)).removeAllViews();

            if(!previews.isEmpty()){

                  RecyclerSliderAdapter previewAdapter= new RecyclerSliderAdapter(getContext(),null);
                 recyclerPiece.swapAdapter(previewAdapter,true);
                ((LinearLayout) v.findViewById(R.id.imageContainer)).addView(recyclerPiece);
                previewAdapter.addAll(previews);

            }else {

                Utils.toast(getContext(),"No se encontaron piezas");
            }


        }
    }


    private void showPieceSlider(Material material){
        if(((LinearLayout) v.findViewById(R.id.imageContainer)).getChildCount()>0){


            ((LinearLayout) v.findViewById(R.id.imageContainer)).removeAllViews();

        }
        if(material.getPieceCount()>0) {
            Object[] obj = new Object[1];

            obj[0] = material;

            LayoutInflater li = LayoutInflater.from(getContext());

            View view = li.inflate(R.layout.progress_layout, null);

            ((LinearLayout) v.findViewById(R.id.imageContainer)).addView(view);

            ((ProgressBar) v.findViewById(R.id.toolbarProgress)).setIndeterminate(true);

            if (obj != null) {

                if (filesAsync != null) {

                    filesAsync.cancel(true);

                }

                filesAsync = new SearchFilesAsync();
                filesAsync.execute(obj);
            }
        }

    }






}
