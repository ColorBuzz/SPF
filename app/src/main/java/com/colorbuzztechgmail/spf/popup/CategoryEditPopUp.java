package com.colorbuzztechgmail.spf.popup;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.colorbuzztechgmail.spf.Category;
import com.colorbuzztechgmail.spf.CategoryBaseFragment;
import com.colorbuzztechgmail.spf.CategoryFragment;
import com.colorbuzztechgmail.spf.Dealership;
import com.colorbuzztechgmail.spf.ModelDataBase;
import com.colorbuzztechgmail.spf.R;
import com.colorbuzztechgmail.spf.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class CategoryEditPopUp extends Dialog    {

    private ModelDataBase db;
    private List<Category> editCategories;
    private List<Category> removeCategories;
    private List<Category> addCategories;
    Utils.onSavedInterface savedInterface;
    CategoryFragment.ChooserType editorType;
  

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

    }




    public CategoryEditPopUp(@NonNull Context context, @Nullable Utils.onSavedInterface savedInterface,@NonNull CategoryFragment.ChooserType type)
    {
        super(context);


        this.editorType=type;
        this.savedInterface=savedInterface;
    }
 
    // Create a Dialog using default AlertDialog builder , if not inflate custom view in onCreateView
    @Override
    public void onCreate(Bundle savedInstanceState) {
        
        db=new ModelDataBase(getContext());

        LayoutInflater li = LayoutInflater.from(getContext());
        final View promptsView = li.inflate(R.layout.edit_category_popup, null);

        removeCategories = new ArrayList<>();
        addCategories = new ArrayList<>();
        editCategories = new ArrayList<>();



        final TextView titleText=(TextView) promptsView.findViewById(R.id.titleText);

        titleText.setText(getTitle());

        promptsView.findViewById(R.id.closeFrame).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        promptsView.findViewById(R.id.saveFrame).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

           /*     if (!addCategories.isEmpty()) {

                    for (int i = 0; i < addCategories.size(); i++) {

                        addCategory(addCategories.get(i).getName());


                    }
                    savedInterface.onSaved(null,0,false);

                }*/

                if (!editCategories.isEmpty()) {

                    final List<Category> antCategory=new ArrayList<>();

                    for (int i = 0; i < editCategories.size(); i++) {
                        final String oldName = db.getMaterialTypeName(editCategories.get(i).getId());

                        if (!(oldName.equals(editCategories.get(i).getName()))) {

                            final Category category=new Category(oldName);
                            category.setId(editCategories.get(i).getId());
                            antCategory.add(category);

                            upDateCategory(editCategories.get(i).getId(), editCategories.get(i).getName());



                        }


                    }

                    savedInterface.onSaved(antCategory,0,true);

                }

                if (!removeCategories.isEmpty()) {

                    for (int i = 0; i < removeCategories.size(); i++) {

                            deleteCategory(removeCategories.get(i).getId());



                    }
                    savedInterface.onSaved(null,0,false);

                }

                db.closeDB();

               dismiss();

            }
        });



        setContentView(promptsView);
                
    }

    private void createCategoryTable(List<Category> categories){
        final LinearLayout container = (LinearLayout)findViewById(R.id.containertem);
        for (int i = 0; i < categories.size(); i++) {

            container.addView(generateCategoryView((Category) categories.get(i), getWindow().getDecorView()));

        }


    }

    @Override
    public void onStart() {
        super.onStart();


        int width = ViewGroup.LayoutParams.MATCH_PARENT;//(( MainActivity)getActivity()).getWindowManager().getDefaultDisplay().getWidth()-160;
        int height =ViewGroup.LayoutParams.MATCH_PARENT;
        ;
        getWindow().setLayout(width, height);

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        new SearchFilesAsync();


        


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
                        if(((TextView)v).getText().toString().trim().length()==0){

                            ((TextView)v).setText(aux);


                        }
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

                    if (s.toString().trim().length() > 0) {


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
                    }else{


                        categorytxt.setHint(aux);


                    }
                }
            });
            removeImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(!(isAssigned(mCategory.getId()))){

                        removeCategories.add(mCategory);
                        container.removeView(cView);

                    }else {

                        Utils.toast(getContext(),getContext().getString(R.string.warning_deleteCategory));

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

    private boolean isAssigned(long categoryId){

        switch (editorType){


            case MODEL_DIRECTORY:

                return   db.isDirectoryAssigned(categoryId) ;



            case MODEL_CUSTUMER:




              return db.isCustumerAssigned(categoryId);

            case MATERIAL_TYPE:

                return   db.isMaterialTypeAssigned(categoryId) ;


            case MATERIAL_DEALER:

           return db.isDealerAssigned(categoryId);


            case CUTNOTE_MODEL:



                break;



        }

        return false;
    }

    private void upDateCategory(int categoryId,String newCategory){

        switch (editorType){


            case MODEL_DIRECTORY:

                db.updateDirectory(categoryId, newCategory);

                break;


            case MODEL_CUSTUMER:

                db.updateCustumer(categoryId,newCategory,null,null,null,null,null);


                break;

            case MATERIAL_TYPE:
                db.updateMaterialType(categoryId, newCategory);


                break;

            case MATERIAL_DEALER:


                db.updateDealership(categoryId,newCategory,null,null,null,null,null,null);



                break;

            case CUTNOTE_MODEL:



                break;



        }




    }

    private void deleteCategory(int categoryId){

        switch (editorType){



            case MODEL_DIRECTORY:


                    db.deleteCategory(categoryId);




                break;


            case MODEL_CUSTUMER:
                db.deleteCustumer(categoryId);

                break;

            case MATERIAL_TYPE:

                break;

            case MATERIAL_DEALER:


                db.deleteDealership(categoryId);

                break;

            case CUTNOTE_MODEL:



                break;


        }




    }

    public class SearchFilesAsync extends AsyncTask<Object, Float, List<Category>> {

        ModelDataBase db;
        List<Category> buffer;

        public SearchFilesAsync() {
            super();


            db = new ModelDataBase(getContext());
            buffer = new ArrayList<>();
            execute();


        }

        protected void onPreExecute() {


        }

        protected List<Category> doInBackground(Object[] path) {

            try {


                switch (editorType) {



                    case MATERIAL_TYPE:


                        buffer.addAll(db.getMaterialsTypesCategory(true));
                        return buffer;



                    case MODEL_CUSTUMER:


                        buffer.addAll(db.getCustumerCategory(true));

                        return buffer;

                    case MATERIAL_DEALER:


                        buffer.addAll(db.getDealersCategory(true));

                        return buffer;

                    case MODEL_DIRECTORY:

                        buffer.addAll(db.getCategoryClass(true));

                        return buffer;


                }


            } catch (Exception e) {

                e.printStackTrace();
                Log.e("Background", e.toString());


            }
            return null;
        }

        protected void onProgressUpdate(Float... valores) {
            int p = Math.round(valores[0]);
            // ma.mProgressBar.setProgress(p);
        }

        @Override
        protected void onPostExecute(List<Category> headers) {


            if (headers != null) {



                createCategoryTable(headers);
            } else {

                Utils.toast(getContext(), "No se encontaron categorias");
            }


        }

    }

    private String getTitle(){


          String title=getContext().getString(R.string.action_edit);



        switch (editorType){




            case MODEL_DIRECTORY:

               return title+=" " + getContext().getString(R.string.action_directory);



            case MODEL_CUSTUMER:

              return title+=" " + getContext().getString(R.string.action_custumers);



            case MATERIAL_TYPE:

                return title+=" " + getContext().getString(R.string.material_type);



            case MATERIAL_DEALER:

                return title+=" " +getContext().getString(R.string.material_dealership);



            case CUTNOTE_MODEL:

               return title+=" " + getContext().getString(R.string.model);




             default:

                 return title;


        }
    }



}
