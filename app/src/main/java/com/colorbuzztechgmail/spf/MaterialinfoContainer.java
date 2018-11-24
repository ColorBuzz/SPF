package com.colorbuzztechgmail.spf;

import android.content.Context;

import android.databinding.DataBindingUtil;

import android.icu.lang.UCharacter;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
 import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


import com.colorbuzztechgmail.spf.databinding.CutNoteInfodBinding;
import com.colorbuzztechgmail.spf.databinding.MaterialInfoBinding;
 import com.colorbuzztechgmail.spf.databinding.PieceInfodBinding;
 import com.colorbuzztechgmail.spf.RecyclerSliderAdapter.Preview;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by PC01 on 28/01/2018.
 */



public class MaterialinfoContainer extends View implements View.OnClickListener,StopLoadPieces{

    private View v,assigmeBtn;
    MainActivity ma;
      boolean visible;

    public static final int MODE_MATERIAL = 1;
    public static final int MODE_CUSTOM_MATERIAL = 2;
    public static final int MODE_SUBMENU_PIECE = 3;
    public static final int MODE_CUTNOTE = 4;
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



        }else if(mode==MODE_CUSTOM_MATERIAL) {
            v = li.inflate(R.layout.animated_info_layout, null);

            recyclerPiece=new RecyclerView(getContext());
            final LinearLayoutManager lm = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
            recyclerPiece.setLayoutManager(lm);
            recyclerPiece.setHasFixedSize(true);



        }else if(mode==MODE_MATERIAL) {
            v = li.inflate(R.layout.animated_info_layout, null);
        }
       else if(mode==MODE_CUTNOTE) {
        v = li.inflate(R.layout.animated_info_cutnote_layout, null);
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

    public void startAnimation(boolean enabled,Object obj) {

        ///mode 1 Material
        ///mode 2 Custom Material
        ///mode 3 Model-subMenu Pieces


        if (enabled) {


            setVisible(true);

            if(obj instanceof CustomMaterial) {



                        final MaterialInfoBinding materialInfodBinding = DataBindingUtil.bind(v);

                        materialInfodBinding.setVariable(BR.obj,obj);


                        materialInfodBinding.notifyChange();




            }  else if(obj instanceof Material) {

             final Material material = (Material) obj;

             if (material != null) {

                 // showPieceSlider(material);


                 if (material.getCustomMaterialId()>0) {

                     v.findViewById(R.id.addmaterial).setVisibility(GONE);
                     v.findViewById(R.id.container).setVisibility(VISIBLE);

                     ModelDataBase db = new ModelDataBase(getContext());

                     final MaterialInfoBinding materialInfodBinding = DataBindingUtil.bind(v);

                     materialInfodBinding.setVariable(BR.obj, db.getCustomMaterial(material.getCustomMaterialId()));


                     materialInfodBinding.notifyChange();

                 } else {
                     v.findViewById(R.id.container).setVisibility(GONE);

                     v.findViewById(R.id.addmaterial).setVisibility(VISIBLE);


                 }


             }
         }else if(obj instanceof Piece) {



                 if (obj != null) {
                     final PieceInfodBinding pieceInfodBinding = DataBindingUtil.bind(v);

                     pieceInfodBinding.setVariable(BR.obj, obj);

                     pieceInfodBinding.notifyChange();


                 }


         }else if (obj instanceof CutNote){

                final CutNoteInfodBinding cutNoteInfodBinding = DataBindingUtil.bind(v);

                cutNoteInfodBinding.setVariable(BR.obj, obj);

                cutNoteInfodBinding.notifyChange();

                ((ImageView)v.findViewById(R.id.imageViewProfile)).setImageDrawable(Utils.getImageAtStatus(getContext(),((CutNote)obj).getStatus()));

                BuildEditableTable(((CutNote) obj).getSizeList(),cutNoteInfodBinding.tableLayout);







            }



            if (isVisible()) {

                if(!(obj instanceof CutNote)) {
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
            }

        } else {
            setVisible(false);
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


                for (Piece piece: db.getHighLightPiecesByMaterial(material1.getModelId(),material1.getId(),true)) {

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

    private void BuildEditableTable(Map<String,Integer> sizeValuesMap,TableLayout tableLayout) {


        final List<String>stringList= new ArrayList<>();
        int pairCount=0;

       ;



        for(Object obj:sizeValuesMap.keySet().toArray())
            stringList.add((String)obj);


        Collections.sort(stringList);





        for(String size: stringList) {

            TableRow row = new TableRow(getContext());
            row.setLayoutParams(new TableRow.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT,1f));
            row.setPadding(0,0,0,1);

            TextView tv1 = new TextView(getContext());
            tv1.setLayoutParams(new TableRow.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT,0.5f));
            tv1.setGravity(Gravity.CENTER);
            tv1.setBackgroundColor(getContext().getResources().getColor(R.color.colorLightPrimaryTransparent));
            tv1.setTextColor(getContext().getResources().getColor(R.color.textColorPrimary));
            tv1.setPadding(8,8,8,8);

            tv1.setTextSize(16);
            tv1.setText(size);

            row.addView(tv1);

            final TextView tv2 = new TextView(getContext());
            tv2.setInputType(UCharacter.NumericType.DIGIT);
            tv2.setLayoutParams(new TableRow.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT,0.5f));
            tv2.setGravity(Gravity.CENTER);
            tv2.setBackground(getContext().getResources().getDrawable(R.drawable.edit_txt_background1));

            tv2.setTextColor(getContext().getResources().getColor(R.color.textColorPrimary));
            tv2.setPadding(8,8,8,8);

            tv2.setTextSize(16);
            tv2.setText(String.valueOf(sizeValuesMap.get(size)));
            pairCount=sizeValuesMap.get(size)+pairCount;

            if(String.valueOf(sizeValuesMap.get(size)).equals("0")){
                tv2.setTextColor(getContext().getResources().getColor(R.color.textColorTerciary));
                tv1.setTextColor(getContext().getResources().getColor(R.color.textColorTerciary));

            }else{
                tv2.setTextColor(getContext().getResources().getColor(R.color.textColorPrimary));
                tv1.setTextColor(getContext().getResources().getColor(R.color.textColorPrimary));

            }



            row.addView(tv2);


            tableLayout.addView(row);
        }



        TableRow row = new TableRow(getContext());
        row.setLayoutParams(new TableRow.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT,1f));
        row.setPadding(0,0,0,1);

        TextView tv1 = new TextView(getContext());
        tv1.setLayoutParams(new TableRow.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT,0.5f));
        tv1.setGravity(Gravity.CENTER);
        tv1.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimary));
        tv1.setTextColor(getContext().getResources().getColor(R.color.textColorPrimary));
        tv1.setPadding(8,8,8,8);

        tv1.setTextSize(16);
        tv1.setText(getContext().getString(R.string.cutNotes_pairs));

        row.addView(tv1);

        final TextView tv2 = new EditText(getContext());
        tv2.setLayoutParams(new TableRow.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT,0.5f));
        tv2.setGravity(Gravity.CENTER);
        tv2.setBackground(getContext().getResources().getDrawable(R.drawable.edit_txt_background1));

        tv2.setTextColor(getContext().getResources().getColor(R.color.colorAccentLight));
        tv2.setPadding(8,8,8,8);

        tv2.setEnabled(false);
        tv2.setTextSize(16);
        tv2.setText(String.valueOf(pairCount));
        row.addView(tv2);

        tableLayout.addView(row);


    }







}
