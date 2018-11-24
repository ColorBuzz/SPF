package com.colorbuzztechgmail.spf.popup;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.colorbuzztechgmail.spf.DrawView;
import com.colorbuzztechgmail.spf.Material;
import com.colorbuzztechgmail.spf.ModelDataBase;
import com.colorbuzztechgmail.spf.Piece;
import com.colorbuzztechgmail.spf.R;
import com.colorbuzztechgmail.spf.Utils;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * Created by PC01 on 18/06/2017.
 */
public class PieceEditPopUp extends  DialogFragment  {

    public static final int CONVERT_FEETS=0;
    public static final int CONVERT_METERS=1;
    public static final int CONVERT_DECIMETERS=2;

    Piece piece;
    String newMaterial,newName,newAmountMaterial,newDescription,amountLeft,amountRight,amountType,newBaseSize;
    Utils.onSavedInterface savedInterface;

    int materialPos=0;
    int sizePos=0;
    int anteriorAmountType=0;
    Spinner mSpinnerMaterial,mSpinnerAmountType,mSpinnerSize;
    DrawView drawView;
    private static  String PIECE_NAME="piece";
    private static  String PIECE_ID="pieceId";
    private static  String MODEL_ID="modelId";
    private ModelDataBase db;
    ArrayAdapter spinnerAdapter,spinnerAmountAdapter,spinnerSizeAdapter;


    public void setSavedInterface(Utils.onSavedInterface savedInterface) {
        this.savedInterface = savedInterface;
    }

    public static PieceEditPopUp newInstance(@NonNull String pieceName, @NonNull int pieceId, @NonNull int modelId) {
        PieceEditPopUp f = new PieceEditPopUp();
        Bundle args = new Bundle();
        args.putString(PIECE_NAME, pieceName);
        args.putInt(PIECE_ID, pieceId);
        args.putInt(MODEL_ID, modelId);


        f.setArguments(args);


        return f;
    }



 /*   @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View dialogView = inflater.inflate(R.layout.editpiece, null);

        final FrameLayout closeFrame=(FrameLayout) dialogView.findViewById(R.id.closeFrame);

        final EditText nametxt=(EditText)dialogView.findViewById(R.id.subTextView1);
        final EditText amountMaterialtxt=(EditText)dialogView.findViewById(R.id.subTextView3);
        final EditText descriptxt=(EditText)dialogView.findViewById(R.id.subTextView4);
        final EditText amountLefttxt=(EditText)dialogView.findViewById(R.id.piece_amount);
        final EditText amountRighttxt=(EditText)dialogView.findViewById(R.id.piece_amount_mirror);
        final TextView titletxt=(TextView)dialogView.findViewById(R.id.titleText);
        final TextView previewtxt=(TextView)dialogView.findViewById(R.id.previewTxt);



        titletxt.setText(getResources().getString(R.string.piece_edit));
        nametxt.setHint(piece.getName());
        amountMaterialtxt.setText(String.valueOf(piece. getAmount_material()));
        amountLefttxt.setText(String.valueOf(piece.getAmount()));
        amountRighttxt.setText(String.valueOf(piece.getAmount_mirror()));
        amountLefttxt.setHint(String.valueOf(piece.getAmount()));
        amountRighttxt.setHint(String.valueOf(piece.getAmount_mirror()));
        descriptxt.setText(piece.getDescription());
        descriptxt.setHint(getResources().getString(R.string.piece_noObservation));



        nametxt.addTextChangedListener(new GenericTextWatcher(nametxt));
        amountMaterialtxt.addTextChangedListener(new GenericTextWatcher(amountMaterialtxt));
        descriptxt.addTextChangedListener(new GenericTextWatcher(descriptxt));
        descriptxt.addTextChangedListener(new GenericTextWatcher(descriptxt));
        amountLefttxt.addTextChangedListener(new GenericTextWatcher(amountLefttxt));
        amountRighttxt.addTextChangedListener(new GenericTextWatcher(amountRighttxt));


        mSpinnerMaterial =(Spinner)dialogView.findViewById(R.id.spinnerMaterial);
        mSpinnerSize =(Spinner)dialogView.findViewById(R.id.spinnerSize);
        mSpinnerAmountType=(Spinner)dialogView.findViewById(R.id.spinnerUnits);

        closeFrame.setOnClickListener(this);
        previewtxt.setOnClickListener(this);

        mSpinnerMaterial.setPrompt(getString(R.string.materialsTxt));
        mSpinnerAmountType.setPrompt(getString(R.string.piece_units));
        mSpinnerSize.setPrompt(getString(R.string.piece_SizeList));




        mSpinnerSize.setAdapter(spinnerSizeAdapter);
        mSpinnerMaterial.setAdapter(spinnerAdapter);
        mSpinnerAmountType.setAdapter(spinnerAmountAdapter);

        mSpinnerSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    amountMaterialtxt.setText("");
                    amountMaterialtxt.setEnabled(false);
                    mSpinnerAmountType.setSelection(0);
                    mSpinnerAmountType.setEnabled(false);


                }else{

                    amountMaterialtxt.setEnabled(true);
                    amountMaterialtxt.setText("");

                    mSpinnerAmountType.setEnabled(true);
                }

                newBaseSize=(String)spinnerSizeAdapter.getItem(position);




            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSpinnerMaterial.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {



                    newMaterial=(String)spinnerAdapter.getItem(position);
                    setSaveVisible();




            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSpinnerAmountType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                amountType=(String)spinnerAmountAdapter.getItem(position);

               amountMaterialtxt.setText(String.valueOf(amountConversion(anteriorAmountType,position,newAmountMaterial)));

               anteriorAmountType=position;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        mSpinnerMaterial.setSelection(materialPos);
        mSpinnerSize.setSelection(sizePos);
        return dialogView;


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL,R.style.MY_DIALOG);

     //   setStyle(DialogFragment.STYLE_NORMAL, R.style.MY_DIALOG);
        db=new ModelDataBase(getContext());
        final String pieceName = getArguments().getString(PIECE_NAME);
        final int pieceId = getArguments().getInt(PIECE_ID);
        final int modelId = getArguments().getInt(MODEL_ID);
       final String baseSize= db.getPreviewModelById(modelId).getBaseSize();

        piece =  db.getPiece(modelId, pieceId, pieceName, null,true);



        spinnerAdapter = new ArrayAdapter<String>(getContext(),
                R.layout.spinneritem1, new ArrayList<String>());
        spinnerAdapter.setDropDownViewResource(R.layout.dropdown_spinneritem);

        List<Material> materials=(db.getModelMaterial(piece.getModelId()));

        for (int i =0;i<materials.size();i++){

            if(materials.get(i).getName().equals(piece.getMaterial())){
                materialPos=i;
            }

            spinnerAdapter.add(materials.get(i).getName());

        }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////

        String[] amountTypeList= getContext().getResources().getStringArray(R.array.default_amountType);

        spinnerAmountAdapter = new ArrayAdapter<String>(getContext(),
                R.layout.spinneritem1, amountTypeList);
        spinnerAmountAdapter.setDropDownViewResource(R.layout.dropdown_spinneritem);

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
        spinnerSizeAdapter = new ArrayAdapter<String>(getContext(),
                R.layout.spinneritem1, new ArrayList<String>( ));
        spinnerSizeAdapter.setDropDownViewResource (R.layout.dropdown_spinneritem);

        spinnerSizeAdapter.add(getResources().getString(R.string.importNoAsigned_Cat));

        final  List<String> sizeList =(db.getPreviewModelById(modelId).getSizeList());

        for(int i=0;i<sizeList.size();i++){

            spinnerSizeAdapter.add(sizeList.get(i));

            if(baseSize!=null){

                 if(sizeList.get(i).equals(baseSize)){
                    sizePos=spinnerSizeAdapter.getCount()-1;
                    newBaseSize=sizeList.get(i);
                 }

            }else{

                sizePos = 0;


            }

        }


        newName=piece.getName();
        newMaterial=piece.getMaterial();
        newAmountMaterial=String.valueOf(piece.getAmount_material());
        newDescription=piece.getDescription();
        amountRight=String.valueOf(piece.getAmount_mirror());
        amountLeft=String.valueOf(piece.getAmount());


    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog d = getDialog();

        if (d!=null) {
            int width =( getActivity()).getWindowManager().getDefaultDisplay().getWidth();
            int height =ViewGroup.LayoutParams.MATCH_PARENT;

            d.getWindow().setLayout(width, height);
            d .getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            LinearLayout imageContainer = (LinearLayout) d.findViewById(R.id.imageContainer);


            float factor = 1.5f;
            float h = width/factor;


            imageContainer.setLayoutParams(new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT));

            drawView = new DrawView(getContext(),piece,null, width, ((int) h));
            final TextView previewtxt=(TextView)d.findViewById(R.id.previewTxt);

            previewtxt.setCompoundDrawablesWithIntrinsicBounds(piece.getImage(),null,getContext().getDrawable(R.drawable.ic_keyboard_arrow_down_grey_24dp),null);







        }

    }

    @Override
    public void onClick(View v) {


        switch (v.getId()){

            case R.id.closeFrame:

                this.dismiss();

                break;

            case R.id.saveFrame:

                if(searchDuplicate(newName)){

                    Utils.toast(getContext(),"Nombre duplicado");

                }else{

                 savePiece();

                }

                break;
            case R.id.previewTxt:

                if(((LinearLayout) getDialog().findViewById(R.id.imageContainer)).getChildCount()>0){
                    animated(((TextView)v.findViewById(v.getId())),false);
                    ((LinearLayout) getDialog().findViewById(R.id.imageContainer)).removeView(drawView);

                }else{
                     animated(((TextView)v.findViewById(v.getId())),true);
                    ((LinearLayout) getDialog().findViewById(R.id.imageContainer)).addView(drawView);
                }

                break;

        }

    }

    public static void setMargins (View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }

    private void setSaveVisible(){

        final FrameLayout saveFrame=(FrameLayout) getDialog().findViewById(R.id.saveFrame);
        saveFrame.setOnClickListener(this);

        if(!(piece.getName().equals(newName))
                ||!(piece.getMaterial().equals(newMaterial))
                ||!(String.valueOf(piece.getAmount_material()).equals( newAmountMaterial))
                ||(piece.getAmount()!=Integer.valueOf(amountLeft))
                ||(piece.getAmount_mirror()!=Integer.valueOf(amountRight))
                ||!(piece.getDescription().equals(newDescription))){


            saveFrame.setVisibility(View.VISIBLE);

        }else{

            saveFrame.setVisibility(View.INVISIBLE);

        }


    }

    private class GenericTextWatcher implements TextWatcher{

        private View view;
        private GenericTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        public void afterTextChanged(Editable editable) {
            String text = editable.toString();
            text=text.trim();
            if(text.equals("")){

                text=null;
            }
            switch(view.getId()){
                case R.id.subTextView1:
                   newName=(text);
                   if(text==null){
                       newName=piece.getName();

                   }
                    break;
                case R.id.subTextView3:
                    newAmountMaterial=(text);
                    if(text==null){
                        newAmountMaterial=String.valueOf(piece.getAmount_material());

                    }
                    break;
                case R.id.subTextView4:
                    newDescription=(text);
                    if(text==null){
                        newDescription=piece.getDescription();

                    }
                    break;

                case R.id.subTextView2:
                    amountLeft=(text);
                    if(text==null){
                        amountLeft=String.valueOf(piece.getAmount());

                    }
                    break;
                case R.id.subTextView2b:
                    amountRight=(text);
                    if(text==null){
                        amountRight=String.valueOf(piece.getAmount_mirror());

                    }
                    break;

            }
            setSaveVisible();

        }
    }

    private boolean searchDuplicate(String newName){



          for(Piece piece: db.getHighLightPieces(piece.getModelId(),false)){


            if(newName.equals(piece.getName()) && (!piece.getName().equals(piece.getName()))){


                return true;

            }

            if(newName.equals(piece.getName()) && (piece.getMaterial().equals(newMaterial)) && (piece.getId()!=piece.getId())){


                  return true;

            }


          }


        return false;

    }

    private void savePiece() {

        NumberFormat formatter = NumberFormat.getInstance(Locale.US);
        formatter.setMaximumFractionDigits(3);
        formatter.setMinimumFractionDigits(3);
        formatter.setRoundingMode(RoundingMode.UP);


        float finalAmount = 0.0f;


        if (amountType.equals(spinnerAmountAdapter.getItem(CONVERT_FEETS))) {

            finalAmount = Float.valueOf(newAmountMaterial);

        } else if (amountType.equals(spinnerAmountAdapter.getItem(CONVERT_METERS))) {

            float feets = 0.0f;
            float meters = Float.valueOf(newAmountMaterial);

            feets = meters * 10.764f;
            finalAmount = feets;

        } else if (amountType.equals(spinnerAmountAdapter.getItem(CONVERT_DECIMETERS))) {

            float feets = 0.0f;
            float decimeters = Float.valueOf(newAmountMaterial);

            feets = (decimeters / 100) * (10.764f);
            finalAmount = feets;
        }

        finalAmount=new Float(formatter.format(finalAmount));

        List<Piece> pieceList =  db.getPizeSizeList(this.piece,false);
         float area=0.0f;
        for (Piece basePiece : pieceList) {

            if (basePiece.getSize().equals(newBaseSize)) {

                area= basePiece.getBoxArea();
                 break;
            }

        }



            for (Piece piece : pieceList) {


                piece.setName(newName);
                piece.setMaterial(newMaterial);
                piece.setAmount(Integer.valueOf(amountLeft));
                piece.setAmount_mirror(Integer.valueOf(amountRight));
                piece.setDescription(newDescription);

                if(area>0.0f){

                    piece.setAmount_material(calculateProporcionalArea(area, piece.getBoxArea(), finalAmount));
                }

               db.updateSinglePiece(piece);


            }



        this.dismiss();
        if(savedInterface!=null){
            savedInterface.onSaved(db.getPiece(piece.getModelId(),piece.getId(),null,null,true),0,true);

        }



        Utils.toast(getContext(),"Datos actualizados");


    }

    public static float amountConversion(int anteriorType,int selectType,String amount){





        float result=0.0f;
        float feets=0.0f;
        float meters=0.0f;
        float decimeters=0.0f;
      if(Float.valueOf(amount)>0) {

          if (anteriorType != selectType) {

              if (anteriorType == CONVERT_FEETS) {


                  feets = Float.valueOf(amount);

                  switch (selectType) {

                      case CONVERT_METERS:

                          meters = (float) (feets * (1 / 10.764));

                          result = meters;

                          break;

                      case CONVERT_DECIMETERS:

                          meters = (float) (feets * (1 / 10.764));

                          decimeters = (float) (meters * 100);

                          result = decimeters;

                          break;

                  }


              } else if (anteriorType == CONVERT_METERS) {


                  meters = Float.valueOf(amount);

                  switch (selectType) {

                      case CONVERT_DECIMETERS:

                          decimeters =   (meters * 100);

                          result = decimeters;

                          break;
                      case CONVERT_FEETS:

                          feets = meters * 10.764f;

                          result = feets;


                          break;

                  }

              } else if (anteriorType == CONVERT_DECIMETERS) {


                  decimeters = Float.valueOf(amount);

                  switch (selectType) {

                      case CONVERT_METERS:

                          meters = (float) (decimeters / 100);

                          result = meters;

                          break;

                      case CONVERT_FEETS:
                          meters = (float) (decimeters / 100);
                          feets = meters * 10.764f;

                          result = feets;

                          break;

                  }
              }

          }else{

              result=Utils.FormatDecimal(Float.valueOf(amount));
          }




          return   new Float(Utils.FormatDecimal(result));


      }
         return new Float(Utils.FormatDecimal(result));
    }


    public void animated(TextView text, boolean enable) {



        if (enable){

            text.setCompoundDrawablesWithIntrinsicBounds(piece.getImage(),null,getContext().getDrawable(R.drawable.ic_keyboard_arrow_down_grey_animatable_clockwise),null);



        }else{
            text.setCompoundDrawablesWithIntrinsicBounds(piece.getImage(),null,getContext().getDrawable(R.drawable.ic_keyboard_arrow_down_grey_animatable_counter_clockwise),null);




        }


        ((AnimatedVectorDrawable)text.getCompoundDrawables()[2]).start();


    }

    private Float calculateProporcionalArea(float baseArea,float targetArea,float amount){
        NumberFormat decimalFormat= new DecimalFormat("#0.00") ;
        NumberFormat formatter = NumberFormat.getInstance(Locale.US);
        formatter.setMaximumFractionDigits(3);
        formatter.setMinimumFractionDigits(3);
        formatter.setRoundingMode(RoundingMode.HALF_EVEN);
     float area=0f;

     area=(targetArea*amount)/baseArea;


        return new Float(formatter.format(area));
    }
*/
}










