package com.colorbuzztechgmail.spf;


import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.colorbuzztechgmail.spf.popup.CategoryEditPopUp;

import java.util.List;

public abstract class CategoryBaseFragment extends Fragment implements  Utils.onSavedInterface{

    public static String TAG="tag";
    private CategoryFragment.ChooserType type;
    private CategoryFragment.OnSelectedCategoryListener listener;

    private String myTag;


    public void setListener(CategoryFragment.OnSelectedCategoryListener listener) {
        this.listener = listener;
    }

    public CategoryFragment.OnSelectedCategoryListener getListener() {
        return listener;
    }

    public CategoryFragment.ChooserType getType() {
        return type;
    }

    public void setType(CategoryFragment.ChooserType type) {
        this.type = type;
    }

    public String getMyTag() {
        return myTag;
    }

    public void setMyTag(String myTag) {
        this.myTag = myTag;
    }

    protected abstract void init();

     public void showAddNewCategory(final CategoryFragment.CategoryAdapter adapter){


        final ModelDataBase db=new ModelDataBase(getContext());
        String title=getContext().getString(R.string.action_add);

        LayoutInflater li=LayoutInflater.from(getContext());

        final View promptsView = li.inflate(R.layout.add_category_popup, null);

        final TextView titleText=(TextView) promptsView.findViewById(R.id.titleText);






        ((LinearLayout)promptsView.findViewById(R.id.di)).removeAllViewsInLayout();

        final EditText nametxt=new EditText(getContext());
        nametxt.setHint(getContext().getResources().getString(R.string.importNoAsigned_Cat));
        nametxt.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        ((LinearLayout)promptsView.findViewById(R.id.di)).addView(nametxt);




        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getContext());
        alertDialogBuilder.setView(promptsView);

        switch (getType()){




            case MODEL_DIRECTORY:

                title+=" " + getString(R.string.action_directory);

                break;


            case MODEL_CUSTUMER:

                title+=" " + getString(R.string.action_custumers);

                break;

            case MATERIAL_TYPE:
                title+=" " + getString(R.string.material_type);

                break;

            case MATERIAL_DEALER:

                title+=" " + getString(R.string.material_dealership);


                break;

            case CUTNOTE_MODEL:

                title+=" " + getString(R.string.model);


                break;






        }
         titleText.setText(title);


        final Dialog alertDialog = alertDialogBuilder.create();

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();




        promptsView.findViewById(R.id.closeFrame).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();

            }
        });

        promptsView.findViewById(R.id.saveFrame).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String category =nametxt.getText().toString();
                category=category.trim();
                if(!category.equals("")){

                    switch (getType()){


                        case MODEL_DIRECTORY:

                            if(!db.existDirectory(category)){


                                final Category category1= db.getDirectory(db.insertCategory(category));

                                ((CategoryFragment.CategoryAdapter)adapter).add(category1);

                                alertDialog.dismiss();


                            }else{


                                Utils.toast(getContext(),getResources().getString(R.string.action_DuplicateCategory));


                            }

                            break;

                        case MATERIAL_TYPE:


                            if(!db.existMaterialType(category)){


                                final Category category1= db.getMaterialTypeCategory((db.insertMaterialTypes(category)));

                                ((CategoryFragment.CategoryAdapter)adapter).add(category1);

                                alertDialog.dismiss();


                            }else{


                                Utils.toast(getContext(),getResources().getString(R.string.action_DuplicateCategory));


                            }

                            break;

                        case MATERIAL_DEALER:


                            if(!db.existDealer(category)){

                                final Dealership dealership=new Dealership();
                                dealership.setName(category);

                                final Category category1= db.getDealerCategory((db.addDealership(dealership)));

                                ((CategoryFragment.CategoryAdapter)adapter).add(category1);

                                alertDialog.dismiss();


                            }else{


                                Utils.toast(getContext(),getResources().getString(R.string.action_DuplicateCategory));


                            }

                            break;

                        case MODEL_CUSTUMER:


                            if(!db.existCustumer(category)){

                                final Custumer custumer=new Custumer(category);


                                final Category category1= db.getCustumerCategory((db.addCustumer(custumer)));

                                ((CategoryFragment.CategoryAdapter)adapter).add(category1);

                                alertDialog.dismiss();


                            }else{


                                Utils.toast(getContext(),getResources().getString(R.string.action_DuplicateCategory));


                            }

                            break;

                    }



                }else {

                    Utils.toast(getContext(), getResources().getString(R.string.warning_emptyName));
                }


            }
        });

    }

    public  void showEditCategory(){

         new CategoryEditPopUp(getContext(),this,getType()).show();


    }

    @Override
    public void onSaved(Object obj, int position, boolean isEditable) {
        init();



        switch (getType()){




            case MODEL_DIRECTORY:


                break;


            case MODEL_CUSTUMER:


                break;

            case MATERIAL_TYPE:

                break;

            case MATERIAL_DEALER:



                break;

            case CUTNOTE_MODEL:



                break;






        }


    }












}
