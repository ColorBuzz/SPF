
package com.colorbuzztechgmail.spf;

import android.databinding.ObservableMap;
import android.graphics.drawable.Drawable;
import android.icu.lang.UCharacter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.PopupMenu;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.colorbuzztechgmail.spf.popup.BaseAddEditPopUp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditPiece extends BaseAddEditPopUp  implements View.OnClickListener {

    public static final int PIECE_NAME=1;
    public static final int PIECE_AMOUNT= 2;
    public static final int PIECE_AMOUNT_MIRROR= 3;
    public static final int PIECE_MATERIAL= 4;
    public static final int PIECE_BASE_SIZE= 5;
    public static final int PIECE_AMOUNT_MATERIAL= 6;
    public static final int PIECE_UNITS= 7;
    public static final int PIECE_DESCRIPTION= 8;
    public static final int PIECE_IMAGE= 9;

     public static final String MODEL_ID="  MODEL_ID";



    public Piece piece;
    private View mConteView;

     private Spinner mSpinnerMaterial;
    private Spinner mSpinnerUnits;
    private Spinner mSpinnerBaseSize;

    private long modelId=0;

    ObservableMap<Integer, Object> values;



    @Override
    protected View setViewLayout(LayoutInflater inflater) {
        mConteView=inflater.inflate(R.layout.editpiece,null);


        return mConteView;
    }

    public static EditPiece newInstanceEditPiecePopUp(@NonNull long modelId, @NonNull int[] position, @NonNull long[] itemId, @Nullable Utils.onSavedInterface saveListener) {

        EditPiece f= new EditPiece();

        Bundle args=new Bundle();
        args.putLongArray(DATABASE_ITEM_ID,itemId);
        args.putIntArray(ADAPTER_ITEM_POSITION,position);
        args.putBoolean(EDITABLE_ST,true);
        args.putLong(MODEL_ID,modelId);
        f.setArguments(args);
        f.setSavedInterface(saveListener);
        return f;

    }



    @Override
    public void setupValues(View v) {



        if(editable){
            modelId=getArguments().getLong(MODEL_ID);


            final TextView titleText=(TextView) v.findViewById(R.id.titleText);

            titleText.setText(getTitle());





            if(isMultipleEditable()){


                setMultipleEditableValues(v);


            }else{


                setSingleEditableValues(v);


            }


        }else{
            setDefaultValues(v);





        }

        initializeViews(v);




        saveView= v.findViewById(R.id.saveFrame);

        saveView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });
        saveView.setVisibility(View.INVISIBLE);


        v.findViewById(R.id.closeFrame).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().cancel();

            }
        });


    }

    @Override
    protected void initializeViews(View v) {


        mSpinnerMaterial= v.findViewById(R.id.spinnerMaterial);
        mSpinnerUnits= v.findViewById(R.id.spinnerUnits);
        mSpinnerBaseSize= v.findViewById(R.id.spinnerBaseSize);



        final String[] aux = getContext().getResources().getStringArray(R.array.default_amountType);

        final List<String> amountType = new ArrayList<>();

               amountType.addAll(Arrays.asList(aux));
               amountType.add(newValue);


        final ArrayAdapter spinnerUnitsAdapter = new ArrayAdapter<String>(getActivity().getBaseContext(),
                    R.layout.spinneritem1, amountType){


                @Override
                public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

                    View v = null;
                    parent.setVerticalScrollBarEnabled(true);

                    // If this is the initial dummy entry, make it hidden
                if ( ((String)amountType.get(position)).equals(newValue)) {
                        TextView tv = new TextView(getContext());
                        tv.setHeight(0);
                        tv.setVisibility(View.GONE);
                        v = tv;

                        return v;

                    }else {

                    v = super.getDropDownView(position, null, parent);

                    return v;

                }

            }
        };
            spinnerUnitsAdapter.setDropDownViewResource(R.layout.dropdown_spinneritem);

           mSpinnerUnits.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                    if(!((String)mSpinnerUnits.getSelectedItem()).equals((String) values.get(PIECE_UNITS))){

                        getValueViewMap().get(R.id.spinnerUnits).setNewValue((String)mSpinnerUnits.getSelectedItem());

                    };


                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

           mSpinnerUnits.setAdapter(spinnerUnitsAdapter);
           mSpinnerUnits.setPrompt(getString(R.string.piece_units));



        List<Material> materials = new ArrayList<>();

        materials=db.getModelMaterial(Utils.toIntExact(modelId));

        final Map<String,Drawable> materialMap=new HashMap<>();


        final List<String>materialsName=new ArrayList<>();

        materialsName.add(newValue);
        materialMap.put(newValue,null);


        for(Material material:materials){

            final Drawable drawable=db.getCustomMaterialImage(material.getCustomMaterialId());
            materialMap.put(material.getName(),ShapeGenerator.getImageForSpinner(getContext(),drawable,ShapeGenerator.MODE_ROUND_RECT));
            materialsName.add(material.getName());
        }





        final ArrayAdapter spinnerMaterialAdapter = new ArrayAdapter<String>(getActivity().getBaseContext(),
                R.layout.spinneritem1, materialsName){


            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

                View v = null;
                parent.setVerticalScrollBarEnabled(true);

                // If this is the initial dummy entry, make it hidden
                if ( ((String)materialsName.get(position)).equals(newValue)) {
                    TextView tv = new TextView(getContext());
                    tv.setHeight(0);
                    tv.setVisibility(View.GONE);
                    v = tv;

                    return v;

                }else {



                    View customView = LayoutInflater.from(getContext()).inflate(R.layout.dropdown_spinneritem,null);
                    TextView tv = customView.findViewById(R.id.spinnerItem);
                    tv.setText(materialsName.get(position));
                    tv.setCompoundDrawablesWithIntrinsicBounds(materialMap.get(materialsName.get(position)), null, null, null);
                    // Pass convertView as null to prevent reuse of special case view

                    return tv;
                }
                   /*
                    // Hide scroll bar because it appears sometimes unnecessarily, this does not prevent scrolling
                    v = super.getDropDownView(position, null, parent);
  */

            }

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

                if ( ((String)materialsName.get(position)).equals(newValue)) {
                    return super.getView(position, null, parent);


                }else {



                    View customView = LayoutInflater.from(getContext()).inflate(R.layout.spinneritem1,null);
                    TextView tv = customView.findViewById(R.id.spinnerItem);
                    tv.setText(materialsName.get(position));
                     tv.setCompoundDrawablesWithIntrinsicBounds(materialMap.get(materialsName.get(position)), null, getContext().getDrawable(R.drawable.ic_keyboard_arrow_down_primary_dark_24dp), null);
                    // Pass convertView as null to prevent reuse of special case view

                    return tv;
                }
            }
        }


                ;
        spinnerMaterialAdapter.setDropDownViewResource(R.layout.dropdown_spinneritem);



        mSpinnerMaterial.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                if(!((String)mSpinnerMaterial.getSelectedItem()).equals((String) values.get(PIECE_MATERIAL))){

                    getValueViewMap().get(R.id.spinnerMaterial).setNewValue((String)mSpinnerMaterial.getSelectedItem());

                };


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mSpinnerMaterial.setAdapter(spinnerMaterialAdapter);
        mSpinnerMaterial.setPrompt(getString(R.string.materialsTxt));




    }

    @Override
    protected void setMultipleEditableValues(View v) {

        addValueItem(new ValueViewItem(v.findViewById(R.id.spinnerMaterial),PIECE_MATERIAL,newValue,v.findViewById(R.id.undo_material_frame)));
        addValueItem(new ValueViewItem(v.findViewById(R.id.spinnerUnits),PIECE_UNITS,getContext().getString(R.string.units_feet),v.findViewById(R.id.undo_units_frame)));



        values=mObservableMap;


    }

    @Override
    protected Object setEditableObject() {

        if(!isMultipleEditable()){
            modelId=getArguments().getLong(MODEL_ID);

           piece= db.getPiece(Utils.toIntExact(modelId),Utils.toIntExact(getmItemIds()[0]),null,null,true);


        }

        return null;

    }

    @Override
    protected void setSingleEditableValues(View v) {

        addValueItem(new ValueViewItem(v.findViewById(R.id.pieceNameText),PIECE_NAME,piece.getName(),v.findViewById(R.id.undo_name_frame)));
        addValueItem(new ValueViewItem(v.findViewById(R.id.piece_amount),PIECE_AMOUNT,String.valueOf(piece.getAmount()),v.findViewById(R.id.undo_amount_frame)));
        addValueItem(new ValueViewItem(v.findViewById(R.id.piece_amount_mirror),PIECE_AMOUNT_MIRROR,String.valueOf(piece.getAmount_mirror()),v.findViewById(R.id.undo_amount_mirror_frame)));
        addValueItem(new ValueViewItem(v.findViewById(R.id.spinnerMaterial),PIECE_MATERIAL,piece.getMaterial(),v.findViewById(R.id.undo_material_frame)));
        addValueItem(new ValueViewItem(v.findViewById(R.id.spinnerBaseSize),PIECE_BASE_SIZE,piece.getSize(),v.findViewById(R.id.undo_baseSize_frame)));
        addValueItem(new ValueViewItem(v.findViewById(R.id.amountMaterialText),PIECE_AMOUNT_MATERIAL,String.valueOf(piece.getAmount_material()),v.findViewById(R.id.undo_amountMaterial_frame)));
        addValueItem(new ValueViewItem(v.findViewById(R.id.spinnerUnits),PIECE_UNITS,getContext().getString(R.string.units_feet),v.findViewById(R.id.undo_units_frame)));
        addValueItem(new ValueViewItem(v.findViewById(R.id.descriptionTxt),PIECE_DESCRIPTION,piece.getDescription(),v.findViewById(R.id.undo_description_frame)));
        addValueItem(new ValueViewItem(v.findViewById(R.id.imageContainer),PIECE_IMAGE,piece.getImage(),null));


        values=mObservableMap;




    }

    @Override
    protected String getTitle() {



         if(!editable) {

            return getString(R.string.action_addpiece);

        }else {
             if(isMultipleEditable()){
                 return getString(R.string.action_edit) + " " + getString(R.string.piece_Amount);

             }else {

                 return  getString(R.string.piece_edit);
             }

        }

    }

    @Override
    protected void setDefaultValues(View v) {




        addValueItem(new ValueViewItem(v.findViewById(R.id.pieceNameText),PIECE_NAME,"",v.findViewById(R.id.undo_name_frame)));
        addValueItem(new ValueViewItem(v.findViewById(R.id.piece_amount),PIECE_AMOUNT,"0",v.findViewById(R.id.undo_amount_frame)));
        addValueItem(new ValueViewItem(v.findViewById(R.id.piece_amount_mirror),PIECE_AMOUNT_MIRROR,"0",v.findViewById(R.id.undo_amount_mirror_frame)));
        addValueItem(new ValueViewItem(v.findViewById(R.id.spinnerMaterial),PIECE_MATERIAL,newValue,v.findViewById(R.id.undo_material_frame)));
        addValueItem(new ValueViewItem(v.findViewById(R.id.spinnerBaseSize),PIECE_BASE_SIZE,newValue,v.findViewById(R.id.undo_baseSize_frame)));
        addValueItem(new ValueViewItem(v.findViewById(R.id.amountMaterialText),PIECE_AMOUNT_MATERIAL,"0",v.findViewById(R.id.undo_amountMaterial_frame)));
        addValueItem(new ValueViewItem(v.findViewById(R.id.spinnerUnits),PIECE_UNITS,getContext().getString(R.string.units_feet),v.findViewById(R.id.undo_baseSize_frame)));
        addValueItem(new ValueViewItem(v.findViewById(R.id.desciptionTxt),PIECE_DESCRIPTION,"NO DESCRIPTION",v.findViewById(R.id.undo_description_frame)));
        addValueItem(new ValueViewItem(v.findViewById(R.id.imageContainer),PIECE_IMAGE,null,null));



        values=mObservableMap;



    }

    @Override
    protected void saveData() {




            if(editable){


                if(!isMultipleEditable()) {

                    List<Piece> pieceList =  db.getPizeSizeList(this.piece,false);
                    float area=0.0f;

                    final float amountMaterial= getConvertedAmount( (String) getValueForIdentifier(PIECE_AMOUNT_MATERIAL));



                    for (Piece basePiece : pieceList) {

                        if (basePiece.getSize().equals((String) getValueForIdentifier(PIECE_BASE_SIZE))) {

                            area= basePiece.getBoxArea();
                            break;
                        }

                    }


                    for (Piece piece : pieceList) {

                        if (ifValueChangeOfView(PIECE_NAME))
                            piece.setName((String) getValueForIdentifier(PIECE_NAME));

                        if (ifValueChangeOfView(PIECE_AMOUNT))
                            piece.setAmount(Integer.valueOf((String) getValueForIdentifier(PIECE_AMOUNT)));

                        if (ifValueChangeOfView(PIECE_AMOUNT_MIRROR))
                            piece.setAmount_mirror(Integer.valueOf((String) getValueForIdentifier(PIECE_AMOUNT_MIRROR)));

                        if (ifValueChangeOfView(PIECE_MATERIAL))
                            piece.setMaterial( (String) getValueForIdentifier(PIECE_MATERIAL));

                        if (ifValueChangeOfView(PIECE_DESCRIPTION))
                            piece.setDescription( (String) getValueForIdentifier(PIECE_DESCRIPTION));

                        if ((ifValueChangeOfView(PIECE_BASE_SIZE)) ||  (ifValueChangeOfView(PIECE_AMOUNT_MATERIAL))) {

                            if (area > 0.0f) {

                                piece.setAmount_material(Utils.calculateProporcionalArea(area, piece.getBoxArea(), amountMaterial));
                            }
                        }

                        if(piece.getSize().equals((String)getValueForIdentifier(PIECE_BASE_SIZE))){
                            piece.setImage((Drawable)getValueForIdentifier(PIECE_IMAGE));
                            this.piece=piece;



                        }

                        db.updateSinglePiece(piece);


                    }


                    if (mSavedInterface != null) {

                        mSavedInterface.onSaved(piece, mItemPos[0], true);
                    }

                } else {

                    final Map<Integer, Long> pieceMap = new HashMap();


                    List<Integer> sortedPos = new ArrayList<>();

                    for (int i = 0; i < mItemIds.length; i++) {

                        pieceMap.put(mItemPos[i], mItemIds[i]);
                        sortedPos.add(mItemPos[i]);

                    }

                    Collections.sort(sortedPos, new Comparator<Integer>() {
                        @Override
                        public int compare(Integer integer, Integer t1) {

                            return Integer.compare(integer, t1) * -1;
                        }
                    });

                    ///////updateValues////

                    for (Integer pos : sortedPos) {


                        final Piece piece=db.getPiece(Utils.toIntExact(modelId),Utils.toIntExact(pieceMap.get(pos)),null,null,true);

                        if (ifValueChangeOfView(PIECE_MATERIAL)){

                            db.updateMaterialAtPiece(piece,(String)getValueForIdentifier(PIECE_MATERIAL));
                            piece.setMaterial((String)getValueForIdentifier(PIECE_MATERIAL));

                        }


                        if (mSavedInterface != null) {


                            mSavedInterface.onSaved(piece, pos, true);

                        }
                    }
                }



            }else{




                if(mSavedInterface!=null) {

                }


            }

            db.closeDB();
            dismiss();



    }

    @Override
    protected View getBindableView() {

        return mConteView;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {



        }
    }


    @Override
    public void setSaveVisible() {


        if(start) {

            for(int i=0;i<getValueViewMap().keySet().size();i++){

                if ( (! getValueViewMap().get(getValueViewMap().keySet().toArray()[i]).isValueChanged())){

                    saveView.setVisibility(View.INVISIBLE);

                } else {

                    saveView.setVisibility(View.VISIBLE);
                    break;

                }


            }

        }


    }


    private Float getConvertedAmount(String amount) {


        float finalAmount = 0.0f;


        if (((String)getValueForIdentifier(PIECE_UNITS)).equals(getContext().getString(R.string.units_feet))) {

            return  Float.valueOf(amount);

        } else if (((String)getValueForIdentifier(PIECE_UNITS)).equals(getContext().getString(R.string.units_meter))) {

            float feets = 0.0f;
            float meters = Float.valueOf(amount);

            feets = meters * 10.764f;
            return  feets;

        } else if (((String)getValueForIdentifier(PIECE_UNITS)).equals(getContext().getString(R.string.units_decimeter))) {

            float feets = 0.0f;
            float decimeters = Float.valueOf(amount);

            feets = (decimeters / 100) * (10.764f);
            return  feets;
        }


        return 0f;
    }

}

