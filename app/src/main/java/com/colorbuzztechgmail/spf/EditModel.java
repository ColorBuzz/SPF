package com.colorbuzztechgmail.spf;

import android.content.Intent;
import android.databinding.BindingAdapter;
import android.databinding.ObservableArrayMap;
import android.databinding.ObservableMap;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.colorbuzztechgmail.spf.MainActivity.REQUEST_CODE_MATERIAL_IMAGE_GALLERY;

public class EditModel extends BaseAddEditPopUp implements View.OnClickListener {

    public static final int MODEL_NAME= 1;
    public static final int MODEL_DIRECTORY= 2;
    public static final int MODEL_BASE_SIZE= 3;
    public static final int MODEL_CUSTUMER= 4;
    public static final int MODEL_DESCRIPTION= 5;
    public static final int MODEL_IMAGE= 6;

    public PreviewModelInfo model;
    private View mConteView;

    private ImageView imageContainer;
    private Spinner mSpinnerDirectory,mSpinnersSize,mSpinnerCustumer;
    ObservableMap<Integer, Object> values;


    @Override
    protected View setViewLayout(LayoutInflater inflater) {
        mConteView=inflater.inflate(R.layout.edit_model_popup,null);


        return mConteView;
    }

    public static EditModel newInstanceEditModelPopUp(int[] position, long[] modelId, boolean editable, Utils.onSavedInterface saveListener) {

        EditModel f= new EditModel();

        Bundle args=new Bundle();
        args.putLongArray(DATABASE_ITEM_ID,modelId);
        args.putIntArray(ADAPTER_ITEM_POSITION,position);
        args.putBoolean(EDITABLE_ST,editable);
        f.setArguments(args);
        f.setSavedInterface(saveListener);
        return f;

    }

    @Override
    protected void initializeViews(View v) {


        mSpinnerCustumer=(Spinner)v.findViewById(R.id.spinnerCustumer);
        mSpinnersSize = v.findViewById(R.id.spinnerBaseSize);
        mSpinnerDirectory=(Spinner)v.findViewById(R.id.spinnerDirectory);
        imageContainer=(ImageView)v.findViewById(R.id.imageContainer);





        if(db.getDirectoryCount()>0) {

            final List<String> customDirectories = db.getCategoryString();
            if(isMultipleEditable())
                customDirectories.add(newValue);

            final ArrayAdapter spinnerDirectoryAdapter = new ArrayAdapter<String>(getActivity().getBaseContext(),
                    R.layout.spinneritem1, customDirectories) {
                @Override
                public View getDropDownView(int position, View convertView, ViewGroup parent)
                {
                    View v = null;

                    // If this is the initial dummy entry, make it hidden
                    if ( customDirectories.get(position).equals(newValue)) {
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
            };
            spinnerDirectoryAdapter.setDropDownViewResource(R.layout.dropdown_spinneritem);

            mSpinnerDirectory.setPrompt(getString(R.string.action_directory));
            mSpinnerDirectory.setAdapter(spinnerDirectoryAdapter);
            mSpinnerDirectory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    if(!(((String)mSpinnerDirectory.getSelectedItem()).equals((String)values.get(MODEL_DIRECTORY)))){
                        getValueViewMap().get(R.id.spinnerDirectory).setNewValue((String) mSpinnerDirectory.getSelectedItem());
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }

        if(!isMultipleEditable()) {

            final ArrayAdapter spinnerSizeAdapter = new ArrayAdapter<String>(getActivity().getBaseContext(),
                    R.layout.spinneritem1, model.getSizeList());
            spinnerSizeAdapter.setDropDownViewResource(R.layout.dropdown_spinneritem);

            if (model.getSizeList().isEmpty()) {

                spinnerSizeAdapter.add(getString(R.string.importNoAsigned_Cat));

            }

            mSpinnersSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if(!(((String)mSpinnersSize.getSelectedItem()).equals((String)values.get(MODEL_BASE_SIZE)))){
                        getValueViewMap().get(R.id.spinnerBaseSize).setNewValue((String) mSpinnersSize.getSelectedItem());
                    }

            }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            mSpinnersSize.setPrompt(getString(R.string.piece_SizeList));
            mSpinnersSize.setAdapter(spinnerSizeAdapter);

        }

        if(db.getCustumerCount()>0) {

            final List<Category> customCustumer = db.getCustumerCategory(true);

            if(isMultipleEditable())
                customCustumer.add(new Category(newValue));

            final ArrayAdapter spinnerCustumerdapter = new ArrayAdapter<String>(getActivity().getBaseContext(),
                    R.layout.spinneritem1, new ArrayList<String>()){
                @Override
                public View getDropDownView(int position, View convertView, ViewGroup parent)
                {
                    View v = null;

                    // If this is the initial dummy entry, make it hidden
                    if ( customCustumer.get(position).getName().equals(newValue)) {
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
            };
            spinnerCustumerdapter.setDropDownViewResource(R.layout.dropdown_spinneritem);

            mSpinnerCustumer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    if(!(((String)mSpinnerCustumer.getSelectedItem()).equals((String)values.get(MODEL_CUSTUMER)))){
                        getValueViewMap().get(R.id.spinnerCustumer).setNewValue((String) mSpinnerCustumer.getSelectedItem());
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            for (Category custumer : customCustumer) {

                spinnerCustumerdapter.add(custumer.getName());

            }
            mSpinnerCustumer.setAdapter(spinnerCustumerdapter);
            mSpinnerCustumer.setPrompt(getString(R.string.action_custumers));

        }



    }



    @Override
    protected String getTitle() {

        if(isMultipleEditable()){
            return getString(R.string.action_edit) +  " " +getString(R.string.modelosText);

        }else {

            return getString(R.string.action_editModel);

        }

    }

    @Override
    protected void setMultipleEditableValues(View v) {


         addValueItem(new ValueViewItem(v.findViewById(R.id.spinnerDirectory),MODEL_DIRECTORY,newValue,v.findViewById(R.id.undo_directory_frame)));
         addValueItem(new ValueViewItem(v.findViewById(R.id.spinnerCustumer),MODEL_CUSTUMER,newValue,v.findViewById(R.id.undo_custumer_frame)));

        values=mObservableMap;


    }

    @Override
    protected Object setEditableObject() {

        if(!isMultipleEditable()){

           model= db.getPreviewModelById(getmItemIds()[0]);


        }

        return null;

    }

    @Override
    protected void setSingleEditableValues(View v) {






        addValueItem(new ValueViewItem(v.findViewById(R.id.editModelNameTextView),MODEL_NAME,model.getName(),v.findViewById(R.id.undo_name_frame)));
        addValueItem(new ValueViewItem(v.findViewById(R.id.spinnerDirectory),MODEL_DIRECTORY,model.getDirectory(),v.findViewById(R.id.undo_directory_frame)));
        addValueItem(new ValueViewItem(v.findViewById(R.id.spinnerBaseSize),MODEL_BASE_SIZE,model.getBaseSize(),v.findViewById(R.id.undo_baseSize_frame)));
        addValueItem(new ValueViewItem(v.findViewById(R.id.spinnerCustumer),MODEL_CUSTUMER,model.getCustumer(),v.findViewById(R.id.undo_custumer_frame)));
        addValueItem(new ValueViewItem(v.findViewById(R.id.desciptionTxt),MODEL_DESCRIPTION,model.getDescription(),v.findViewById(R.id.undo_description_frame)));
        addValueItem(new ValueViewItem(v.findViewById(R.id.imageContainer),MODEL_IMAGE,model.getImage(),v.findViewById(R.id.undo_image_frame)));


        values=mObservableMap;




    }

    @Override
    protected void setDefaultValues(View v) {


        addValueItem(new ValueViewItem(v.findViewById(R.id.editModelNameTextView),MODEL_NAME,"",v.findViewById(R.id.undo_name_frame)));
        addValueItem(new ValueViewItem(v.findViewById(R.id.spinnerDirectory),MODEL_DIRECTORY,"",v.findViewById(R.id.undo_directory_frame)));
        addValueItem(new ValueViewItem(v.findViewById(R.id.spinnerBaseSize),MODEL_BASE_SIZE,"",v.findViewById(R.id.undo_baseSize_frame)));
        addValueItem(new ValueViewItem(v.findViewById(R.id.spinnerCustumer),MODEL_CUSTUMER,"",v.findViewById(R.id.undo_custumer_frame)));
        addValueItem(new ValueViewItem(v.findViewById(R.id.desciptionTxt),MODEL_DESCRIPTION,"",v.findViewById(R.id.undo_description_frame)));
        addValueItem(new ValueViewItem(v.findViewById(R.id.imageContainer),MODEL_IMAGE,null,v.findViewById(R.id.undo_image_frame)));


    }

    @Override
    protected void saveData() {





        if(editable) {

            if (!isMultipleEditable()) {


                if(((String)getValueForIdentifier(MODEL_NAME)).trim().length()<=1) {

                    Utils.toast(getContext(), getString(R.string.warning_emptyName), Gravity.CENTER);
                    return;
                }

                model.setName((String) mObservableMap.get(MODEL_NAME));
                model.setDirectory((String) mObservableMap.get(MODEL_DIRECTORY));
                model.setCustumer((String) mObservableMap.get(MODEL_CUSTUMER));
                model.setImage((Drawable) mObservableMap.get(MODEL_IMAGE));
                model.setBaseSize((String) mObservableMap.get(MODEL_BASE_SIZE));
                model.setDescription((String) mObservableMap.get(MODEL_DESCRIPTION));

                db.updatePreviewData(model);


                if (mSavedInterface != null) {


                    mSavedInterface.onSaved(model, mItemPos[0], editable);

                }

            } else {

                final Map<Integer, Long> modelMap = new HashMap();


                List<Integer> sortedPos = new ArrayList<>();

                for (int i = 0; i < mItemIds.length ; i++) {

                    modelMap.put(mItemPos[i], mItemIds[i]);
                    sortedPos.add(mItemPos[i]);

                }

                Collections.sort(sortedPos, new Comparator<Integer>() {
                    @Override
                    public int compare(Integer integer, Integer t1) {

                        return Integer.compare(integer, t1) * -1;
                    }
                });

                for (Integer pos : sortedPos) {

                    final PreviewModelInfo model = db.getPreviewModelById(modelMap.get(pos));

                    if(ifValueChangeOfView(MODEL_DIRECTORY))

                       model.setDirectory((String) mObservableMap.get(MODEL_DIRECTORY));

                    if(ifValueChangeOfView(MODEL_CUSTUMER))

                        model.setCustumer((String) mObservableMap.get(MODEL_CUSTUMER));


                    db.updatePreviewData(model);

                    if (mSavedInterface != null) {


                        mSavedInterface.onSaved(model, pos, editable);

                    }

                }

            }

        }
            db.closeDB();
            dismiss();





    }

    @Override
    protected View getBindableView() {

        return mConteView.findViewById(R.id.content);
    }

    private void showImageMenu( View anchor){
      final ListPopupWindow popupWindow = new ListPopupWindow(getContext());
        String[] option = getContext().getResources().getStringArray(R.array.image_menu);

        List<ListPopUpMenuItem> itemList = new ArrayList<>();
        itemList.add(new ListPopUpMenuItem(option[0], R.drawable.ic_photo_camera_black_24dp));
        itemList.add(new ListPopUpMenuItem(option[2], R.drawable.ic_delete_black_24dp));


        ListPopupWindowAdapter adapter = new ListPopupWindowAdapter(getContext(), itemList);
        popupWindow.setAnchorView(anchor);
        popupWindow.setListSelector(getContext().getResources().getDrawable(R.drawable.checkedlayout));
        popupWindow.setContentWidth((int) (150 * getContext().getResources().getDisplayMetrics().density + 0.5f));
        popupWindow.setAdapter(adapter);
        popupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                switch (i) {
                    //Añadir Imagen
                    case 0:

                        imagePicker(REQUEST_CODE_MATERIAL_IMAGE_GALLERY);

                        break;



                    //Elimar
                    case 1:

                        getValueViewMap().get(R.id.imageContainer).setNewValue(null);

                        break;

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
            Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                        getValueViewMap().get(R.id.imageContainer).setNewValue(drawable);



        }


    }

   /* private void resizeImageView(boolean isEmpty){
        int width=0;
        int height=0;
        float factor=0;
        if(!isEmpty) {
            width = getResources().getDisplayMetrics().widthPixels;
             height = ViewGroup.LayoutParams.MATCH_PARENT;
           factor = width / (16f / 9f);
            imageContainer.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,Utils.pxTodp ((int) factor,getActivity().getBaseContext())));

        }else{

            width=(int)getResources().getDimension(R.dimen.piece_pic_detail_size);
            height=(int)getResources().getDimension(R.dimen.piece_pic_detail_size);


            imageContainer.setLayoutParams(new FrameLayout.LayoutParams(width,height));


        }

    }*/


    @Override
    public void onClick(View view) {


        switch (view.getId()){

            case R.id.imageContainer:


                showImageMenu(view);
                break;


        }
    }

    public boolean isImageEmpty() {

        if(mViewIdMap.containsKey(MODEL_IMAGE)){

            return !(getValueForIdentifier(MODEL_IMAGE)!=null);


        }

        return true;


    }


}
