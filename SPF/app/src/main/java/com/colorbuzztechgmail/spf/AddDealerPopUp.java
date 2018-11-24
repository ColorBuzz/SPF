package com.colorbuzztechgmail.spf;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.PopupMenu;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;


/**
 * Created by PC01 on 18/06/2017.
 */
public class AddDealerPopUp extends  DialogFragment implements View.OnClickListener ,PopupMenu.OnMenuItemClickListener {

    private static final String EDIT_MODE="editable";
    private static final String DEALER_ID="dealerId";

    EditText categorytxt;
    Dealership dealer;
    String newName,newAdress,newPhone,newCompany,newEmail,newCategory;
    Spinner mSpinnerCategory;

    Dialog.OnDismissListener dimisListner;

    @Override
    public void dismiss() {
        super.dismiss();
    }

    public void setDimissListener(Dialog.OnDismissListener listener){


        this.dimisListner=listener;


    }


    public static AddDealerPopUp newInstance(int dealerId, boolean editable) {
        AddDealerPopUp frag = new AddDealerPopUp();
        Bundle args = new Bundle();

        args.putInt(DEALER_ID,dealerId);

        args.putBoolean(EDIT_MODE,editable);


        frag.setArguments(args);

        return frag;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View dialogView = inflater.inflate(R.layout.add_dealer_popup, null);

        final Button closeBtn=(Button)dialogView.findViewById(R.id.close_button);

        final EditText nametxt=(EditText)dialogView.findViewById(R.id.txtName);
        final EditText phonetxt=(EditText)dialogView.findViewById(R.id.txtPhone);
        final EditText adresstxt=(EditText)dialogView.findViewById(R.id.txtAdress);
        final EditText companytxt=(EditText)dialogView.findViewById(R.id.txtCompany);
        final EditText emailtxt=(EditText)dialogView.findViewById(R.id.txtEmail);
        categorytxt=(EditText)dialogView.findViewById(R.id.txtCategory);
        final ImageView categoryPopup=(ImageView)dialogView.findViewById(R.id.categoryPopUp);

        final TextView titletxt=(TextView)dialogView.findViewById(R.id.titleText);



        if(getArguments().getBoolean(EDIT_MODE)){

            dealer=new Dealership();

            dealer=((MainActivity)getActivity()).userData.getDealerShip(getArguments().getInt(DEALER_ID));

            nametxt.setHint(dealer.getName());
            phonetxt.setHint(dealer.getPhone());
            adresstxt.setHint(dealer.getAdress());
            companytxt.setHint(dealer.getCompany());
            emailtxt.setHint(dealer.getEmail());
            newName=dealer.getName();
            newPhone=dealer.getPhone();
            newAdress=dealer.getAdress();
            newCompany=dealer.getCompany();
            newEmail=dealer.getEmail();
            newCategory=dealer.getCategory();

            titletxt.setText(getContext().getResources().getString(R.string.action_editDealership));

        }else{


            titletxt.setText(getContext().getResources().getString(R.string.action_newDealership));

        }

        nametxt.addTextChangedListener(new GenericTextWatcher(nametxt));
        phonetxt.addTextChangedListener(new GenericTextWatcher(phonetxt));
        adresstxt.addTextChangedListener(new GenericTextWatcher(adresstxt));
        companytxt.addTextChangedListener(new GenericTextWatcher(companytxt));
        emailtxt.addTextChangedListener(new GenericTextWatcher(emailtxt));
        categorytxt.addTextChangedListener(new GenericTextWatcher(categorytxt));

        categoryPopup.setOnClickListener(this);
        closeBtn.setOnClickListener(this);


        return dialogView;


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setStyle(DialogFragment.STYLE_NORMAL, R.style.MY_DIALOG);



    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog d = getDialog();
        d.setOnDismissListener(dimisListner);

        if (d!=null) {
            int width =ViewGroup.LayoutParams.MATCH_PARENT;//(( MainActivity)getActivity()).getWindowManager().getDefaultDisplay().getWidth()-160;
            int height =ViewGroup.LayoutParams.MATCH_PARENT;
            ;
            d.getWindow().setLayout(width, height);
            d .getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }

    }

    @Override
    public void onClick(View v) {


        switch (v.getId()){

            case R.id.close_button:

               this.dismiss();

            break;

            case R.id.save_Image:

                if(getArguments().getBoolean(EDIT_MODE)){

                    ((MainActivity)getActivity()).userData.updateDealership(dealer.getId(),newName,newPhone,
                            newAdress,newEmail,newCategory,newCompany,dealer.getDate());
                    this.dismiss();

                } else {

                    if(searchDuplicate(newName)){

                        Utils.toast(getContext(),"Nombre duplicado");

                    }else{

                        final Dealership dealer = new Dealership();

                        dealer.setName(newName);
                        dealer.setPhone(newPhone);
                        dealer.setAdress(newAdress);
                        dealer.setCompany(newCompany);
                        dealer.setEmail(newEmail);
                        dealer.setCategory(newCategory);

                        ((MainActivity)getActivity()).userData.addDealership(dealer);

                        showSnackBar();
                        this.dismiss();
                    }

                }
            break;

            case R.id.categoryPopUp:
                showCategories(v);

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

        final ImageView save=(ImageView) getDialog().findViewById(R.id.save_Image);
        save.setOnClickListener(this);


        if((newName!=null)){

            save.setVisibility(View.VISIBLE);

        }else{

            save.setVisibility(View.INVISIBLE);

        }


    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        newCategory=item.getTitle().toString();
        categorytxt.setText(newCategory);


          return false;
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
                case R.id.txtName:

                    newName=(text);

                    break;
                case R.id.txtPhone:

                    newPhone=(text);

                    break;
                case R.id.txtAdress:
                    newAdress=(text);

                    break;

                case R.id.txtCompany:
                    newCompany=(text);

                    break;
                case R.id.txtEmail:
                   newEmail=(text);

                    break;


                case R.id.txtCategory:

                    newCategory=(text);
            }

            setSaveVisible();
        }
    }

    private boolean searchDuplicate(String newName){






        return false;

    }

    private void showSnackBar(){
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) getActivity().findViewById(R.id.parentLayout);
        Snackbar.make(coordinatorLayout, getContext().getResources().getString(R.string.contact_Added),
                Snackbar.LENGTH_SHORT).setAction(getContext().getResources().getString(R.string.dialog_undo), new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        }).show();



    }


    private void showCategories(View v){

        ModelDataBase db= new ModelDataBase(getContext());

        if(db.getDealerShipCount()>0) {

            final List<String> dealerCategories = db.getDealersCategories();

            PopupMenu popup = new PopupMenu(getContext(), v);

            for(String category: dealerCategories) {
                popup.getMenu().add(category);

            }
            popup.setOnMenuItemClickListener(this);
            //menuClick=position;



            popup.show();



        }


    }



}










