package com.colorbuzztechgmail.spf;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class CategoryEditPopUp extends Dialog   {

    private ModelDataBase db;
    private List<Category> editCategories;
    private List<Category> removeCategories;
    private List<Category> addCategories;
    Utils.onSavedInterface savedInterface;
  

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

    }




    public CategoryEditPopUp(@NonNull Context context, @Nullable Utils.onSavedInterface savedInterface) {
        super(context);

         

        this.savedInterface=savedInterface;
    }
 
    // Create a Dialog using default AlertDialog builder , if not inflate custom view in onCreateView
    @Override
    public void onCreate(Bundle savedInstanceState) {
        
        db=new ModelDataBase(getContext());

        LayoutInflater li = LayoutInflater.from(getContext());
        final View promptsView = li.inflate(R.layout.add_category_popup, null);
         

        final List<Category> categories = new ArrayList<>();
        removeCategories = new ArrayList<>();
        addCategories = new ArrayList<>();
        editCategories = new ArrayList<>();

        categories.addAll(db.getCategoryClass());
        final LinearLayout container = (LinearLayout) promptsView.findViewById(R.id.containertem);
          final Button cancelBtn=(Button)promptsView.findViewById(R.id.cancel_btn);
        final Button saveBtn=(Button)promptsView.findViewById(R.id.save_btn);
        final ImageView tilteImg=(ImageView)promptsView.findViewById(R.id.addImageView);
        final TextView titleText=(TextView) promptsView.findViewById(R.id.titleEditTextView);

        titleText.setText(getContext().getResources().getString(R.string.action_editCategory));
        tilteImg.setBackground(getContext().getDrawable(R.drawable.ic_mode_edit_black_24dp));
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!addCategories.isEmpty()) {

                    for (int i = 0; i < addCategories.size(); i++) {

                        db.insertCategory(addCategories.get(i).getName());

                    }

                }

                if (!editCategories.isEmpty()) {

                    for (int i = 0; i < editCategories.size(); i++) {
                        String name=db.SearchCategory(editCategories.get(i).getId()).getName();

                        db.updateCategory(editCategories.get(i).getId(), editCategories.get(i).getName());


                    }
                }

                if (!removeCategories.isEmpty()) {

                    for (int i = 0; i < removeCategories.size(); i++) {

                             db.updateAllModelCategory(removeCategories.get(i).getName(), getContext().getResources().getString(R.string.importNoAsigned_Cat));
                            db.deleteCategory(removeCategories.get(i).getId());

                    }

                }

               dismiss();
                savedInterface.onSaved(null,0,false);
            }
        });


        for (int i = 0; i < categories.size(); i++) {

            container.addView(generateCategoryView(categories.get(i), promptsView));

        }


        setContentView(promptsView);
                
    }

    @Override
    public void onStart() {
        super.onStart();


        int width = ViewGroup.LayoutParams.MATCH_PARENT;//(( MainActivity)getActivity()).getWindowManager().getDefaultDisplay().getWidth()-160;
        int height =ViewGroup.LayoutParams.MATCH_PARENT;
        ;
        getWindow().setLayout(width, height);

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));




        


        }
    private View generateCategoryView(Category category, View v) {

        final LinearLayout container = (LinearLayout) v.findViewById(R.id.containertem);
        final LayoutInflater li = LayoutInflater.from(getContext());
        final View cView = li.inflate(R.layout.categoryitem, null);
        final LinearLayout categoryContainer = (LinearLayout) cView.findViewById(R.id.containertem);

        categoryContainer.setDividerDrawable(getContext().getDrawable(R.drawable.linedrawable));
        categoryContainer.setShowDividers(LinearLayout.SHOW_DIVIDER_END);

        final Category mCategory = category;

        final EditText categorytxt = (EditText) cView.findViewById(R.id.text);
        final ImageView editImage = (ImageView) cView.findViewById(R.id.editImageView);
        final ImageView removeImage = (ImageView) cView.findViewById(R.id.removeImageView);
        final String aux = category.getName();
        categorytxt.setText(category.getName());

        if (!mCategory.getName().equals(getContext().getResources().getString(R.string.importRecent_Cat)) &&
                !mCategory.getName().equals(getContext().getResources().getString(R.string.importNoAsigned_Cat))) {

            editImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    categorytxt.setEnabled(true);
                    editImage.setVisibility(View.GONE);
                    removeImage.setVisibility(View.VISIBLE);

                    categorytxt.setTextColor(getContext().getResources().getColor(R.color.textColorPrimary));
                    categorytxt.requestFocus();
                }
            });
            categorytxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        v.setEnabled(false);
                        categorytxt.setTextColor(getContext().getResources().getColor(R.color.textColorSecondary));
                        removeImage.setVisibility(View.INVISIBLE);
                        editImage.setVisibility(View.VISIBLE);
                    }

                }
            });

            categorytxt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                    if (s.length() > 0) {
                        if (aux != s.toString()) {

                            if (addCategories.contains(mCategory)) {

                                addCategories.remove(mCategory);
                                mCategory.setName(s.toString());
                                addCategories.add(mCategory);
                            } else {

                                if (editCategories.contains(mCategory)) {
                                    editCategories.remove(mCategory);

                                }
                                mCategory.setName(s.toString());
                                editCategories.add(mCategory);

                            }
                        }
                    }
                }
            });
            removeImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(!(db.isCategoryAssigned(mCategory.getName()))){

                        removeCategories.add(mCategory);
                        container.removeView(cView);

                    }else {

                        Utils.toast(getContext(),"Esta categoria tiene modelos asignados");

                    }

                }
            });

        } else {

            editImage.setVisibility(View.GONE);
            removeImage.setVisibility(View.GONE);

        }

       Utils.setMargins(cView,16,2,16,0);


        return cView;
    }




}
