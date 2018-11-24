package com.colorbuzztechgmail.spf;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.PopupMenu;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class AddMaterialPopUp extends  Dialog implements  View.OnClickListener ,PopupMenu.OnMenuItemClickListener,AdapterView.OnItemClickListener,ColorPickerDialog.OnColorChangedListener {

    String seasons="No Asignado";
    String name="No Asignado";
    String type="No Asignado";
    String dealership="No Asignado";
    String newAmountMaterial="0.0";
    String description="";
    String amountType;
    int anteriorAmountType=0;
    Spinner mSpinnerAmountType;
    ArrayAdapter spinnerAmountAdapter;

    Float feets,meters;
    public Drawable image=null;
    public Boolean  editable=false;
    CustomMaterial customMaterial= new CustomMaterial("Custom");
    private CategorySpinner mSpinnerDealer;
    private static final String MATERIAL_DATA="customMaterialData";

    private static final String MATERIAL_EDIT ="editable";
    private EditText typeTxt,amounTxt;
    LinearLayout imageContainer;
    Utils.onSavedInterface savedInterface;
    int mItemPos=0;
    private ListPopupWindow popupWindow;
    public AddMaterialPopUp(@NonNull Context context, @Nullable CustomMaterial material, @Nullable int position, @Nullable Utils.onSavedInterface savedInterface) {
        super(context);

        if(material!=null){

            editable=true;
            customMaterial=material;
            this.mItemPos=position;
        }

        this.savedInterface=savedInterface;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.addImageLayout:

                showImageMenu(v);
            break;
            case R.id.categoryPopUp:
                showMaterialType(v);

                break;


        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {


        typeTxt.setText(item.getTitle().toString());


        return false;

    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater li = LayoutInflater.from(getContext());
        final View promptsView = li.inflate(R.layout.material_popup, null);
        final TextView titleText=(TextView) promptsView.findViewById(R.id.titleEditTextView);
        final ImageView tilteImg=(ImageView)promptsView.findViewById(R.id.addImageView);
        typeTxt=(EditText)promptsView.findViewById(R.id.txtMaterialType);
        amounTxt=(EditText)promptsView.findViewById(R.id.amountText);

        final ImageView categoryPopup=(ImageView)promptsView.findViewById(R.id.categoryPopUp);
        mSpinnerAmountType=(Spinner)promptsView.findViewById(R.id.spinnerAmountType);



        if (editable){
            titleText.setText(getContext().getResources().getString(R.string.ediMaterial));
            tilteImg.setImageDrawable(getContext().getDrawable(R.drawable.ic_mode_edit_black_24dp));

        }else{
            titleText.setText(getContext().getResources().getString(R.string.addMaterial));
            tilteImg.setImageDrawable(getContext().getDrawable(R.drawable.ic_leather_black));

        }



        categoryPopup.setOnClickListener(this);


        final EditText customMaterialtxt=(EditText)promptsView.findViewById(R.id.editMaterialNameTextView);
        final EditText descriptiontxt=(EditText)promptsView.findViewById(R.id.desciptionTxt);
        final LinearLayout container2=(LinearLayout)promptsView.findViewById(R.id.spinnerDealerContainer);
        final Button cancelBtn=(Button)promptsView.findViewById(R.id.cancel_btn);
        final Button saveBtn=(Button)promptsView.findViewById(R.id.save_btn);
        imageContainer=(LinearLayout)promptsView.findViewById(R.id.addImageLayout);


        imageContainer.setOnClickListener(this);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    dismiss();

            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    saveMaterial();



            }
        });


        customMaterialtxt.addTextChangedListener(new GenericTextWatcher(customMaterialtxt));
        descriptiontxt.addTextChangedListener(new GenericTextWatcher(descriptiontxt));
        typeTxt.addTextChangedListener(new GenericTextWatcher(typeTxt));
        amounTxt.addTextChangedListener(new GenericTextWatcher(amounTxt));



        ModelDataBase db= new ModelDataBase(getContext());



        mSpinnerDealer = new CategorySpinner(getContext());


        final ArrayAdapter spinnerAdapter = new ArrayAdapter<String>(getContext(),
                R.layout.spinneritem1, new ArrayList<String>());
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpinnerDealer.setAdapter(spinnerAdapter);

        spinnerAdapter.add(getContext().getResources().getString(R.string.importNoAsigned_Cat));

        if(db.getDealerShipCount()>0) {

            final List<Dealership> customMaterialDealers = db.getDealerShips();

            for (Dealership dealership : customMaterialDealers) {


                spinnerAdapter.add(dealership.getName());

            }

            mSpinnerDealer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    dealership=(String) mSpinnerDealer.getSelectedItem();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        container2.addView(mSpinnerDealer);

        String[] amountTypeList= getContext().getResources().getStringArray(R.array.default_amountType);

        spinnerAmountAdapter = new ArrayAdapter<String>(getContext(),
                R.layout.spinneritem1, amountTypeList);
        spinnerAmountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpinnerAmountType.setAdapter(spinnerAmountAdapter);


        mSpinnerAmountType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                amountType=(String)spinnerAmountAdapter.getItem(position);

                amounTxt.setText(String.valueOf(PieceEditPopUp.amountConversion(anteriorAmountType,position,newAmountMaterial)));

                anteriorAmountType=position;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if(editable){
            typeTxt.setText(customMaterial.getType());
            amounTxt.setText(String.valueOf(customMaterial.getFeets()));
            mSpinnerDealer.setSelectedCategory(customMaterial.getDealership());
            descriptiontxt.setText(customMaterial.getDescription());
            if(customMaterial.getImage()==null){
                imageContainer.setBackground(getContext().getResources().getDrawable(R.drawable.ic_leather_grey));

            }else{
                imageContainer.setBackground(customMaterial.getImage());

            }
            customMaterialtxt.setHint(customMaterial.getName());
            name=customMaterial.getName();
            titleText.setText(getContext().getResources().getString(R.string.action_editMaterial));

        }else {


            mSpinnerDealer.setSelection(0);
            typeTxt.setHint(getContext().getResources().getString(R.string.importNoAsigned_Cat));
        }

setContentView(promptsView);


    }


    // Create a Dialog using default AlertDialog builder , if not inflate custom view in onCreateView


    @Override
    public void onStart() {
        super.onStart();

        int width = ViewGroup.LayoutParams.MATCH_PARENT;//(( MainActivity)getActivity()).getWindowManager().getDefaultDisplay().getWidth()-160;
        int height =ViewGroup.LayoutParams.MATCH_PARENT;
        ;
           getWindow().setLayout(width, height);

           getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));




    }

    private void showMaterialType(View v){

        ModelDataBase db= new ModelDataBase(getContext());

        if(db.getCustomMaterialCount()>0) {

            final List<String> materialType= db.getMaterialsTypes();

            PopupMenu popup = new PopupMenu(getContext(), v);

            for(String type: materialType) {
                popup.getMenu().add(type);

            }
            popup.setOnMenuItemClickListener(this);
            //menuClick=position;



            popup.show();



        }


    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        switch (i) {
            //Añadir Imagen
            case 0:

                ((MainActivity) getOwnerActivity()).imagePicker(MainActivity.REQUEST_CODE_MATERIAL_IMAGE_GALLERY, customMaterial, imageContainer);

                break;


                //Elegir Color
            case 1:

                Dialog d=new ColorPickerDialog(getContext(),this,R.color.iconsColor);
                d.show();

                break;

            //Elimar
            case 2:

                customMaterial.setImage(null);
                imageContainer.setBackground(getContext().getDrawable(R.drawable.bg_black_light_grey));

                break;

        }

        popupWindow.dismiss();
    }


        @Override
        public void colorChanged(int color) {

           customMaterial.setImage(new ShapeGenerator(getContext()).getDrawableShape(null,ShapeGenerator.MODE_ROUND_RECT,color,ShapeGenerator.SIZE_BIG));
            imageContainer.setBackground(customMaterial.getImage());

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

                text=getContext().getString(R.string.importNoAsigned_Cat);
            }
            switch(view.getId()){
                case R.id.txtMaterialType:

                   type=(text);

                    break;

                case R.id.amountText:

                    newAmountMaterial=text;

                    break;
                case R.id.editMaterialNameTextView:

                   name=text;

                    break;

                case R.id.desciptionTxt:

                    description=text;
                    break;


            }


        }
    }

    private  void saveMaterial(){

        float finalAmount = 0.0f;


        if (amountType.equals(spinnerAmountAdapter.getItem(PieceEditPopUp.CONVERT_FEETS))) {

            finalAmount = Float.valueOf(newAmountMaterial);

        } else if (amountType.equals(spinnerAmountAdapter.getItem(PieceEditPopUp.CONVERT_METERS))) {

            float feets = 0.0f;
            float meters = Float.valueOf(newAmountMaterial);

            feets = meters * 10.764f;
            finalAmount = feets;

        } else if (amountType.equals(spinnerAmountAdapter.getItem(PieceEditPopUp.CONVERT_DECIMETERS))) {

            float feets = 0.0f;
            float decimeters = Float.valueOf(newAmountMaterial);

            feets = (decimeters / 100) * (10.764f);
            finalAmount = feets;
        }



        if (!(name.equals( getContext().getString(R.string.importNoAsigned_Cat)) )&& !(name.equals(""))) {
            customMaterial.setName(name);
            customMaterial.setDealership(dealership);
            customMaterial.setType(type);
            customMaterial.setSeasons(seasons);
            customMaterial.setFeets(finalAmount);
            customMaterial.setDescription(description);

            if(!editable){
                customMaterial.setDate(Utils.getDate());

            }

            if(savedInterface!=null){

                savedInterface.onSaved(customMaterial,mItemPos, editable);
                dismiss();
            }

        }else{


            Utils.toast(getContext(),getContext().getString(R.string.warning_emptyName));

        }


    }



    private void showImageMenu( View anchor){
       popupWindow = new ListPopupWindow(getContext());
        String[] option = getContext().getResources().getStringArray(R.array.image_menu);

        List<ListPopUpMenuItem> itemList = new ArrayList<>();
        itemList.add(new ListPopUpMenuItem(option[0], R.drawable.ic_photo_camera_black_24dp));
        itemList.add(new ListPopUpMenuItem(option[1], R.drawable.ic_color_lens_black_24dp));
        itemList.add(new ListPopUpMenuItem(option[2], R.drawable.ic_delete_black_24dp));


        ListPopupWindowAdapter adapter = new ListPopupWindowAdapter(getContext(), itemList);
        popupWindow.setAnchorView(anchor);
        popupWindow.setListSelector(getContext().getResources().getDrawable(R.drawable.checkedlayout));
        popupWindow.setContentWidth((int) (150 * getContext().getResources().getDisplayMetrics().density + 0.5f));
        popupWindow.setAdapter(adapter);
        popupWindow.setOnItemClickListener(this);
        popupWindow.show();




    }


}
