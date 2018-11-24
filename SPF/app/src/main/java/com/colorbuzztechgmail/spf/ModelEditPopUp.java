package com.colorbuzztechgmail.spf;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.ListPopupWindow;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class ModelEditPopUp extends Dialog implements AdapterView.OnItemSelectedListener ,TextWatcher,View.OnClickListener,AdapterView.OnItemClickListener{


    private String newCategory="";
    private String newName="";
    private String oldName="";
    private String oldCategory="";
    private String oldBaseSize="";
    private String newBaseSize="";
    private PreviewModelInfo model;
    private CategorySpinner mSpinner,mSpinnersSize;
    private static final int CATEGORY_ID=0;
    private static final int SIZE_ID=1;
    private Utils.onSavedInterface mSavedInterface;
     private boolean editable=false;
     private Drawable image=null;
     ImageView imageView;

    private ListPopupWindow popupWindow;





    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if(parent.getId()==CATEGORY_ID){
            newCategory=parent.getItemAtPosition(position).toString();


        }else if(parent.getId()==SIZE_ID){


            newBaseSize=parent.getItemAtPosition(position).toString();


        }
      // Utils.toast(getActivity().getBaseContext(),newCategory);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String text = s.toString();
        text=text.trim();
        if(text.equals("")){

            text=null;
        }
        if(text==null){
            newName=oldName;


        }else{
            newName=text;


        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ModelDataBase db=new ModelDataBase(getContext());

        LayoutInflater li = LayoutInflater.from(getContext());
        final View promptsView = li.inflate(R.layout.editmodel_popup, null);
        final LinearLayout container=(LinearLayout)promptsView.findViewById(R.id.spinnerContainer);
        final LinearLayout containerSize=(LinearLayout)promptsView.findViewById(R.id.spinnerSizeContainer);

        final Button cancelBtn=(Button)promptsView.findViewById(R.id.cancel_btn);
        final Button saveBtn=(Button)promptsView.findViewById(R.id.save_btn);
        final EditText nameModeltxt=(EditText)promptsView.findViewById(R.id.editModelNameTextView);
        final ImageView tilteImg=(ImageView)promptsView.findViewById(R.id.addImageView);
        final TextView titleText=(TextView) promptsView.findViewById(R.id.titleEditTextView);
        imageView=(ImageView)promptsView.findViewById(R.id.imageView);

        tilteImg.setBackground(getContext().getDrawable(R.drawable.ic_mode_edit_black_24dp));
        nameModeltxt.addTextChangedListener(this);

        imageView.setOnClickListener(this);

        if(editable) {

            oldCategory = model.getCategory();
            nameModeltxt.setHint(model.getName());
            oldName = model.getName();
            newName = oldName;
            newCategory = oldCategory;
            titleText.setText(oldName);
        }else{
            ((TextView)promptsView.findViewById(R.id.text)).setText(getContext().getString(R.string.piece_Name));
            titleText.setText(getContext().getString(R.string.model_addNewModel));

            model=new PreviewModelInfo();
        }


        if(model.getImage()!=null){

            imageView.setBackground(model.getImage());

        }

        mSpinner=new CategorySpinner(getContext());
        mSpinnersSize=new CategorySpinner(getContext());

        mSpinner.setId(CATEGORY_ID);
        mSpinnersSize.setId(SIZE_ID);


        final ArrayAdapter spinnerSizeAdapter = new ArrayAdapter<String>(getContext(),
                R.layout.spinneritem1, new ArrayList<String>( ));
        spinnerSizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerSizeAdapter.add(getContext().getResources().getString(R.string.importNoAsigned_Cat));
        if(model.getSizeList()!=null)
            spinnerSizeAdapter.addAll(model.getSizeList());
        mSpinnersSize.setAdapter(spinnerSizeAdapter);
        if(model.getBaseSize()!=null){

            mSpinnersSize.setSelectedCategory(model.getBaseSize());
        }

        final ArrayAdapter spinnerAdapter = new ArrayAdapter<String>(getContext(),
                R.layout.spinneritem1, new ArrayList<String>( ));
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpinner.setAdapter(spinnerAdapter);
        spinnerAdapter.addAll(db.getCategoryString());

        mSpinner.setSelectedCategory(model.getCategory());

        mSpinner.setOnItemSelectedListener(this);
        mSpinnersSize.setOnItemSelectedListener(this);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();

            }
        });


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if(!oldCategory.equals(newCategory) || !oldName.equals(newName) || !oldBaseSize.equals(newBaseSize)){

                    model.setName(newName);
                    model.setCategory(newCategory);
                    model.setBaseSize(newBaseSize);
                    if(mSavedInterface!=null){
                        mSavedInterface.onSaved(model,0,editable);


                    }
                    dismiss();

                }else{

                    dismiss();
                }




            }
        });

        containerSize.addView(mSpinnersSize);
        container.addView(mSpinner);



        setContentView(promptsView);

    }

    @Override
    public void onStart() {
        super.onStart();

          int width = ViewGroup.LayoutParams.MATCH_PARENT;//(( MainActivity)getActivity()).getWindowManager().getDefaultDisplay().getWidth()-160;
          int height = ViewGroup.LayoutParams.WRAP_CONTENT;

          getWindow().setLayout(width, height);
          getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }

    @Override
    public void onClick(View view) {


        switch (view.getId()){

            case R.id.imageView:

               showImageMenu(view);


                break;


        }
    }

    private void showImageMenu( View anchor){
            popupWindow = new ListPopupWindow(getContext());
        String[] option = getContext().getResources().getStringArray(R.array.image_menu);

            List<ListPopUpMenuItem> itemList = new ArrayList<>();
            itemList.add(new ListPopUpMenuItem(option[0], R.drawable.ic_photo_camera_black_24dp));
            itemList.add(new ListPopUpMenuItem(option[2], R.drawable.ic_delete_black_24dp));


            ListPopupWindowAdapter adapter = new ListPopupWindowAdapter(getContext(), itemList);
            popupWindow.setAnchorView(anchor);
            popupWindow.setListSelector(getContext().getResources().getDrawable(R.drawable.checkedlayout));
            popupWindow.setContentWidth((int) (150 * getContext().getResources().getDisplayMetrics().density + 0.5f));
            popupWindow.setAdapter(adapter);
            popupWindow.setOnItemClickListener(this);
            popupWindow.show();



    }






    public ModelEditPopUp(@NonNull Context context, PreviewModelInfo model, boolean editable, Utils.onSavedInterface saveListener) {
        super(context);

        this.model=model;
        this.editable=editable;
        this.mSavedInterface=saveListener;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


        switch (i) {
            //Añadir Imagen
            case 0:

                ((MainActivity) getOwnerActivity()).imagePicker(MainActivity.REQUEST_CODE_MATERIAL_IMAGE_GALLERY, model, imageView);

               break;

            //Elimar
            case 2:

                model.setImage(null);
                imageView.setBackground(getContext().getDrawable(R.drawable.bg_black_light_grey));

                break;

        }

          popupWindow.dismiss();
    }
}
