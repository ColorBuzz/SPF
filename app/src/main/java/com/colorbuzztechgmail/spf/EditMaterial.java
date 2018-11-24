package com.colorbuzztechgmail.spf;

import android.app.Dialog;
import android.content.Intent;
import android.databinding.BindingAdapter;
import android.databinding.ObservableMap;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.PopupMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.colorbuzztechgmail.spf.popup.BaseAddEditPopUp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.colorbuzztechgmail.spf.MainActivity.REQUEST_CODE_MATERIAL_IMAGE_GALLERY;

public class EditMaterial extends BaseAddEditPopUp  implements View.OnClickListener {

    public static final int MATERIAL_NAME=1;
    public static final int MATERIAL_TYPE= 2;
    public static final int MATERIAL_DEALER= 3;
    public static final int MATERIAL_STOCK= 4;
    public static final int MATERIAL_DESCRIPTION= 5;
    public static final int MATERIAL_UNITS=6;
    public static final int MATERIAL_SEASONS= 7;
    public static final int MATERIAL_IMAGE=8;


    public CustomMaterial material;
    private View mConteView;

    private ImageView imageContainer;
    private Spinner mSpinnerDealer,mSpinnerUnits;
    ObservableMap<Integer, Object> values;


    @Override
    protected View setViewLayout(LayoutInflater inflater) {
        mConteView=inflater.inflate(R.layout.edit_material_popup,null);


        return mConteView;
    }

    public static EditMaterial newInstanceEditMaterialPopUp(int[] position, long[] itemId, boolean editable, Utils.onSavedInterface saveListener) {

        EditMaterial f= new EditMaterial();

        Bundle args=new Bundle();
        args.putLongArray(DATABASE_ITEM_ID,itemId);
        args.putIntArray(ADAPTER_ITEM_POSITION,position);
        args.putBoolean(EDITABLE_ST,editable);
        f.setArguments(args);
        f.setSavedInterface(saveListener);
        return f;

    }

    @Override
    protected void initializeViews(View v) {


        mSpinnerDealer=(Spinner)v.findViewById(R.id.spinnerDealer);
        mSpinnerUnits = v.findViewById(R.id.spinnerUnits);
        imageContainer=(ImageView)v.findViewById(R.id.imageContainer);




        if(db.getDealerShipCount()>0) {

            final List<Dealership> customMaterialDealers = db.getDealerShips();

            final ArrayAdapter spinnerAdapter = new ArrayAdapter<String>(getActivity().getBaseContext(),
                    R.layout.spinneritem1, new ArrayList<String>()){


                @Override
                public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

                    View v = null;

                    // If this is the initial dummy entry, make it hidden
                    if ( customMaterialDealers.get(position).getName().equals(newValue)) {
                        TextView tv = new TextView(getContext());
                        tv.setHeight(0);
                        tv.setVisibility(View.GONE);
                        v = tv;
                    }
                    else {
                        // Pass convertView as null to prevent reuse of special case views
                        v = super.getDropDownView(position, null, parent);
                    }

                    // Hide scroll bar because it appears sometimes unnecessarily, this does not prevent scrolling
                    parent.setVerticalScrollBarEnabled(true);
                    return v;

                }
            } ;
            spinnerAdapter.setDropDownViewResource(R.layout.dropdown_spinneritem);

            final Dealership defaultDealership= new Dealership();
                defaultDealership.setName(newValue);
                customMaterialDealers.add(defaultDealership);

            for (Dealership dealer : customMaterialDealers) {

                spinnerAdapter.add(dealer.getName());

            }

            mSpinnerDealer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                    if(!((String)mSpinnerDealer.getSelectedItem()).equals((String) values.get(MATERIAL_DEALER))){

                        getValueViewMap().get(R.id.spinnerDealer).setNewValue((String)mSpinnerDealer.getSelectedItem());

                    };


                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            mSpinnerDealer.setAdapter(spinnerAdapter);
            mSpinnerDealer.setPrompt(getString(R.string.dealership_dealerships));

        }

            final String[] amountTypeList = getResources().getStringArray(R.array.default_amountType);

            Collections.sort(Arrays.asList(amountTypeList));
            ArrayAdapter spinnerAmountAdapter = new ArrayAdapter<String>(getActivity().getBaseContext(),
                    R.layout.spinneritem1, amountTypeList);
            spinnerAmountAdapter.setDropDownViewResource(R.layout.dropdown_spinneritem);


            mSpinnerUnits.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                if(!((String)mSpinnerUnits.getSelectedItem()).equals((String) values.get(MATERIAL_UNITS))){

                    getValueViewMap().get(R.id.spinnerUnits).setNewValue((String)mSpinnerUnits.getSelectedItem());

                };


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
            mSpinnerUnits.setAdapter(spinnerAmountAdapter);
            mSpinnerUnits.setPrompt(getString(R.string.piece_units));



    }

    @Override
    protected void setMultipleEditableValues(View v) {


         addValueItem(new ValueViewItem(v.findViewById(R.id.spinnerDealer),MATERIAL_DEALER,newValue,v.findViewById(R.id.undo_dealer_frame)));
         addValueItem(new ValueViewItem(v.findViewById(R.id.txtMaterialType),MATERIAL_TYPE,newValue,v.findViewById(R.id.undo_type_frame)));



        values=mObservableMap;


    }

    @Override
    protected Object setEditableObject() {

        if(!isMultipleEditable()){

           material= db.getCustomMaterial(getmItemIds()[0]);


        }

        return null;

    }

    @Override
    protected void setSingleEditableValues(View v) {

        addValueItem(new ValueViewItem(v.findViewById(R.id.editMaterialNameTextView),MATERIAL_NAME,material.getName(),v.findViewById(R.id.undo_name_frame)));
        addValueItem(new ValueViewItem(v.findViewById(R.id.spinnerDealer),MATERIAL_DEALER,material.getDealership(),v.findViewById(R.id.undo_dealer_frame)));
        addValueItem(new ValueViewItem(v.findViewById(R.id.txtMaterialType),MATERIAL_TYPE,material.getType(),v.findViewById(R.id.undo_type_frame)));
        addValueItem(new ValueViewItem(v.findViewById(R.id.desciptionTxt),MATERIAL_DESCRIPTION,material.getDescription(),v.findViewById(R.id.undo_description_frame)));
        addValueItem(new ValueViewItem(v.findViewById(R.id.imageContainer),MATERIAL_IMAGE,material.getImage(),v.findViewById(R.id.undo_image_frame)));
        addValueItem(new ValueViewItem(v.findViewById(R.id.spinnerUnits),MATERIAL_UNITS,getString(R.string.units_feet),v.findViewById(R.id.undo_units_frame)));
        addValueItem(new ValueViewItem(v.findViewById(R.id.amountText),MATERIAL_STOCK,String.valueOf(material.getFeets()),v.findViewById(R.id.undo_stock_frame)));



        values=mObservableMap;




    }

    @Override
    protected String getTitle() {

        if(isMultipleEditable()){
            return getString(R.string.action_edit) + " " + getString(R.string.materialsTxt);

        }else {

            return getString(R.string.ediMaterial);

        }

    }

    @Override
    protected void setDefaultValues(View v) {



        addValueItem(new ValueViewItem(v.findViewById(R.id.editMaterialNameTextView),MATERIAL_NAME,"",v.findViewById(R.id.undo_name_frame)));
        addValueItem(new ValueViewItem(v.findViewById(R.id.spinnerDealer),MATERIAL_DEALER,newValue,v.findViewById(R.id.undo_dealer_frame)));
        addValueItem(new ValueViewItem(v.findViewById(R.id.txtMaterialType),MATERIAL_TYPE,"",v.findViewById(R.id.undo_type_frame)));
        addValueItem(new ValueViewItem(v.findViewById(R.id.desciptionTxt),MATERIAL_DESCRIPTION,"",v.findViewById(R.id.undo_description_frame)));
        addValueItem(new ValueViewItem(v.findViewById(R.id.imageContainer),MATERIAL_IMAGE,null,v.findViewById(R.id.undo_image_frame)));
        addValueItem(new ValueViewItem(v.findViewById(R.id.spinnerUnits),MATERIAL_UNITS,getString(R.string.units_feet),v.findViewById(R.id.undo_units_frame)));
        addValueItem(new ValueViewItem(v.findViewById(R.id.amountText),MATERIAL_STOCK,"0.0",v.findViewById(R.id.undo_stock_frame)));

        values=mObservableMap;



    }

    @Override
    protected void saveData() {




        if(editable) {





            if (!isMultipleEditable()) {

                if(((String)getValueForIdentifier(MATERIAL_NAME)).trim().length()<=1) {

                    Utils.toast(getContext(), getString(R.string.warning_emptyName), Gravity.CENTER);
                    return;
                }

                material.setName((String) getValueForIdentifier(MATERIAL_NAME));
                material.setType((String) getValueForIdentifier(MATERIAL_TYPE));
                material.setDealership((String) getValueForIdentifier(MATERIAL_DEALER));
                material.setFeets(getConvertedAmount ((String) getValueForIdentifier(MATERIAL_STOCK)));
                material.setDescription((String) getValueForIdentifier(MATERIAL_DESCRIPTION));
                material.setImage((Drawable) getValueForIdentifier(MATERIAL_IMAGE));

                db.updateCustomMaterial(material);

                if (mSavedInterface != null) {


                    mSavedInterface.onSaved(material, mItemPos[0], editable);

                }


            } else {

                final Map<Integer, Long> materialMap = new HashMap();


                List<Integer> sortedPos = new ArrayList<>();

                for (int i = 0; i < mItemIds.length ; i++) {

                    materialMap.put(mItemPos[i], mItemIds[i]);
                    sortedPos.add(mItemPos[i]);

                }

                Collections.sort(sortedPos, new Comparator<Integer>() {
                    @Override
                    public int compare(Integer integer, Integer t1) {

                        return Integer.compare(integer, t1) * -1;
                    }
                });

                for (Integer pos : sortedPos) {

                    final CustomMaterial material = db.getCustomMaterial(materialMap.get(pos));

                    ///////updateValues////

                   if(ifValueChangeOfView(MATERIAL_DEALER))
                    material.setDealership((String) getValueForIdentifier(MATERIAL_DEALER));

                    if(ifValueChangeOfView(MATERIAL_TYPE))
                        material.setType((String) getValueForIdentifier(MATERIAL_TYPE));


                    ///////////////////////


                    db.updateCustomMaterial(material);

                    if (mSavedInterface != null) {


                        mSavedInterface.onSaved(material, pos, editable);

                    }

                }



            }
            db.closeDB();
            dismiss();


        }else{


            material=new CustomMaterial((String)getValueForIdentifier(MATERIAL_NAME));
            material.setType((String) getValueForIdentifier(MATERIAL_TYPE));
            material.setDealership((String) getValueForIdentifier(MATERIAL_DEALER));
            material.setFeets(getConvertedAmount ((String) getValueForIdentifier(MATERIAL_STOCK)));
            material.setDescription((String) getValueForIdentifier(MATERIAL_DESCRIPTION));
            material.setImage((Drawable) getValueForIdentifier(MATERIAL_IMAGE));

           material.setId(Utils.toIntExact(db.addCustomMaterial(material)));

            if (mSavedInterface != null) {


                mSavedInterface.onSaved(material, mItemPos[0], editable);

            }
            db.closeDB();

            mViewIdMap.clear();
            mObservableMap.clear();
            getValueViewMap().clear();
            setDefaultValues(getBindableView());

            Utils.toast(getContext(), getString(R.string.materialTxt) + " " + material.getName() + " " +  getString(R.string.dialog_saved_sucessfully),Gravity.CENTER);


        }






    }

    @Override
    protected View getBindableView() {

        return mConteView.findViewById(R.id.content);
    }


    private void showMaterialType(View v){

        final List<String> materialType= db.getMaterialsTypes();

       final PopupMenu popup = new PopupMenu((getActivity()), v);

        for(String type: materialType) {
            popup.getMenu().add(type);
        }


        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if(!((String)getValueViewMap().get(R.id.txtMaterialType).getDefaultValue()).equals(item.getTitle().toString())){
                    getValueViewMap().get(R.id.txtMaterialType).setNewValue(item.getTitle().toString());
                    return true;
                }
                return false;
            }
        });

            //menuClick=position;

            popup.show();



    }

    private void showImageMenu(View anchor){
       final ListPopupWindow popupWindow = new ListPopupWindow(getActivity().getBaseContext());
        String[] option =  getResources().getStringArray(R.array.image_menu);

        List<ListPopUpMenuItem> itemList = new ArrayList<>();
        itemList.add(new ListPopUpMenuItem(option[0], R.drawable.ic_photo_camera_black_24dp));
        itemList.add(new ListPopUpMenuItem(option[1], R.drawable.ic_color_lens_black_24dp));
        itemList.add(new ListPopUpMenuItem(option[2], R.drawable.ic_delete_black_24dp));


        ListPopupWindowAdapter adapter = new ListPopupWindowAdapter(getActivity().getBaseContext(), itemList);
        popupWindow.setAnchorView(anchor);
        popupWindow.setListSelector( getResources().getDrawable(R.drawable.checkedlayout));
        popupWindow.setContentWidth((int) (150 *  getResources().getDisplayMetrics().density + 0.5f));
        popupWindow.setAdapter(adapter);

        popupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                        switch (i) {
                            //Añadir Imagen
                            case 0:

                                imagePicker(REQUEST_CODE_MATERIAL_IMAGE_GALLERY);

                                break;


                            //Elegir Color
                            case 1:

                                Dialog d=new ColorPickerDialog(getContext(), new ColorPickerDialog.OnColorChangedListener() {
                                    @Override
                                    public void colorChanged(int color) {
                                        getValueViewMap().get(R.id.imageContainer).setNewValue(new ShapeGenerator(getContext()).getDrawableShape(null,ShapeGenerator.MODE_ROUND_RECT,color,ShapeGenerator.SIZE_BIG));

                                    }
                                }, R.color.iconsColor);
                                d.show();

                                break;

                            //Elimar
                            case 2:

                                getValueViewMap().get(R.id.imageContainer).setNewValue(null);




                        }

                        popupWindow.dismiss();
            }
        });
        popupWindow.show();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        if(requestCode== REQUEST_CODE_MATERIAL_IMAGE_GALLERY && data !=null){

            Bitmap bitmap = ImagePicker.getImageFromResult(getContext(), resultCode, data);
           final Drawable drawable = new BitmapDrawable(getResources(), bitmap);

            getValueViewMap().get(R.id.imageContainer).setNewValue(drawable);


        }


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.imageContainer:


                showImageMenu(view);
                break;
            case R.id.categoryPopUp:
                showMaterialType(view);

                break;


        }
    }


    private Float getConvertedAmount(String amount) {


        float finalAmount = 0.0f;


        if (((String)getValueForIdentifier(MATERIAL_UNITS)).equals(getContext().getString(R.string.units_feet))) {

            return  Float.valueOf(amount);

        } else if (((String)getValueForIdentifier(MATERIAL_UNITS)).equals(getContext().getString(R.string.units_meter))) {

            float feets = 0.0f;
            float meters = Float.valueOf(amount);

            feets = meters * 10.764f;
            return  feets;

        } else if (((String)getValueForIdentifier(MATERIAL_UNITS)).equals(getContext().getString(R.string.units_decimeter))) {

            float feets = 0.0f;
            float decimeters = Float.valueOf(amount);

            feets = (decimeters / 100) * (10.764f);
            return  feets;
        }


        return 0f;
    }

}
