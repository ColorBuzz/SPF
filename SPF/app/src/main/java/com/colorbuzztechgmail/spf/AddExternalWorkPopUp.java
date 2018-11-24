package com.colorbuzztechgmail.spf;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
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


public class AddExternalWorkPopUp extends BaseDialogFragment<AddExternalWorkPopUp.OnDialogFragmentClickListener> implements  View.OnClickListener ,PopupMenu.OnMenuItemClickListener {

    String seasons;
    String name="No Asignado";
    String type="No Asignado";
    String dealership="No Asignado";
    String newAmountMaterial="0.0";
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




    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        boolean jj=hasFocus;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.addImageLayout:

                ((MainActivity)getActivity()).imagePicker(MainActivity.REQUEST_CODE_MATERIAL_IMAGE_GALLERY,customMaterial,imageContainer);
            /*    Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , MainActivity.REQUEST_CODE_MATERIAL_IMAGE_GALLERY);//one can be replaced with any action code*/
            break;
            case R.id.categoryPopUp:
                showMaterialType(v);

                break;


        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        type=item.getTitle().toString();
        typeTxt.setText(type);


        return false;

    }

    // interface to handle the dialog click back to the Activity
    public interface OnDialogFragmentClickListener {
        public void onOkClicked(AddExternalWorkPopUp dialog, Material customMaterial, boolean editable);
        public void onCancelClicked(AddExternalWorkPopUp dialog);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    // Create an instance of the Dialog with the input
    public static AddExternalWorkPopUp newInstance(Integer Id, boolean editable) {
        AddExternalWorkPopUp frag = new AddExternalWorkPopUp();
        Bundle args = new Bundle();


        args.putInt(MATERIAL_DATA,Id);
        args.putBoolean(MATERIAL_EDIT,editable);


        frag.setArguments(args);

        return frag;
    }

    // Create a Dialog using default AlertDialog builder , if not inflate custom view in onCreateView
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater li = LayoutInflater.from(getContext());
        final View promptsView = li.inflate(R.layout.material_popup, null);
        final TextView titleText=(TextView) promptsView.findViewById(R.id.titleEditTextView);
        final ImageView tilteImg=(ImageView)promptsView.findViewById(R.id.addImageView);
        typeTxt=(EditText)promptsView.findViewById(R.id.txtMaterialType);
        amounTxt=(EditText)promptsView.findViewById(R.id.amountText);

        final ImageView categoryPopup=(ImageView)promptsView.findViewById(R.id.categoryPopUp);
        mSpinnerAmountType=(Spinner)promptsView.findViewById(R.id.spinnerAmountType);


        editable=getArguments().getBoolean(MATERIAL_EDIT);

        if (editable){
            titleText.setText(getContext().getResources().getString(R.string.ediMaterial));
            tilteImg.setImageDrawable(getContext().getDrawable(R.drawable.ic_mode_edit_black_24dp));
            customMaterial= ((MainActivity)getActivity()).userData.getCustomMaterial(getArguments().getInt(MATERIAL_DATA));

        }else{
            titleText.setText(getContext().getResources().getString(R.string.addMaterial));
            tilteImg.setImageDrawable(getContext().getDrawable(R.drawable.ic_leather_black));

        }



        categoryPopup.setOnClickListener(this);


        final EditText customMaterialtxt=(EditText)promptsView.findViewById(R.id.editMaterialNameTextView);
        final LinearLayout container2=(LinearLayout)promptsView.findViewById(R.id.spinnerDealerContainer);
        final Button cancelBtn=(Button)promptsView.findViewById(R.id.cancel_btn);
        final Button saveBtn=(Button)promptsView.findViewById(R.id.save_btn);
        imageContainer=(LinearLayout)promptsView.findViewById(R.id.addImageLayout);


        imageContainer.setOnClickListener(this);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dialog d=getDialog();

                if(d!=null){

                    getActivityInstance().onCancelClicked(AddExternalWorkPopUp.this);

                }

            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Dialog d=getDialog();
             if(d!=null) {


                 saveMaterial();

             }

            }
        });


        customMaterialtxt.addTextChangedListener(new GenericTextWatcher(customMaterialtxt));
        typeTxt.addTextChangedListener(new GenericTextWatcher(typeTxt));
         amounTxt.addTextChangedListener(new GenericTextWatcher(amounTxt));



        ModelDataBase db= new ModelDataBase(getContext());



            mSpinnerDealer = new CategorySpinner(getContext());


            final ArrayAdapter spinnerAdapter = new ArrayAdapter<String>(getContext(),
                    R.layout.spinneritem1, new ArrayList<String>());
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpinnerDealer.setAdapter(spinnerAdapter);

        spinnerAdapter.add(getResources().getString(R.string.importNoAsigned_Cat));

        if(db.getDealerShipCount()>0) {

            final List<Dealership> customMaterialDealers = db.getDealerShips();

            for (Dealership dealership : customMaterialDealers) {


                spinnerAdapter.add(dealership.getName());

            }

            mSpinnerDealer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    dealership=mSpinnerDealer.getSelectedCategory();
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
                if(customMaterial.getImage()==null){
                    imageContainer.setBackground(((MainActivity)getActivity()).getResources().getDrawable(R.drawable.ic_leather_grey));

                }else{
                    imageContainer.setBackground(customMaterial.getImage());

                }
                customMaterialtxt.setHint(customMaterial.getName());
                name=customMaterial.getName();
                titleText.setText(getResources().getString(R.string.action_editMaterial));

            }else {


                mSpinnerDealer.setSelection(0);
                typeTxt.setText(getResources().getString(R.string.importNoAsigned_Cat));
            }




        return new AlertDialog.Builder(getActivity())

                .setView(promptsView)
                .create();
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog d = getDialog();
        if(d!=null){

            d .getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        }

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
                case R.id.txtMaterialType:

                   type=(text);

                    break;

                case R.id.amountText:

                    newAmountMaterial=text;

                    break;
                case R.id.editMaterialNameTextView:

                   name=text;

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



        if (name != null) {
            customMaterial.setName(name);
            customMaterial.setDealership(dealership);
            customMaterial.setType(type);
            customMaterial.setFeets(finalAmount);

            getActivityInstance().onOkClicked(AddExternalWorkPopUp.this, customMaterial, editable);
        }else{


            Utils.toast(getContext(),getContext().getString(R.string.warning_emptyName));

        }


    }

}
