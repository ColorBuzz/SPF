package com.colorbuzztechgmail.spf.popup;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.colorbuzztechgmail.spf.BaseDialogFragment;
import com.colorbuzztechgmail.spf.CategorySpinner;
import com.colorbuzztechgmail.spf.R;

import java.util.ArrayList;


public class ChangeCategoryPopUp extends BaseDialogFragment<ChangeCategoryPopUp.OnDialogFragmentClickListener> implements AdapterView.OnItemSelectedListener  {

    private String newCategory="";

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

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        newCategory=parent.getItemAtPosition(position).toString();
       // Utils.toast(getContext(),newCategory);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    // interface to handle the dialog click back to the Activity
    public interface OnDialogFragmentClickListener {
        public void onOkClicked(ChangeCategoryPopUp dialog, String category);
        public void onCancelClicked(ChangeCategoryPopUp dialog);
    }



    // Create an instance of the Dialog with the input
    public static ChangeCategoryPopUp newInstance(CharSequence[] categories, ArrayList id) {
        ChangeCategoryPopUp frag = new ChangeCategoryPopUp();
        Bundle args = new Bundle();
        args.putCharSequenceArray("category",categories);
        args.putIntegerArrayList("index",id);

        frag.setArguments(args);
        return frag;
    }
    // Create a Dialog using default AlertDialog builder , if not inflate custom view in onCreateView
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater li = LayoutInflater.from(getContext());
        final View promptsView = li.inflate(R.layout.add_category_popup, null);
        final LinearLayout container=(LinearLayout)promptsView.findViewById(R.id.spinnerContainer);

        final TextView titleText=(TextView) promptsView.findViewById(R.id.titleText);

        titleText.setText(getContext().getResources().getString(R.string.action_moveTo));

        final CategorySpinner mSpinner;


        mSpinner=new CategorySpinner(getContext());
       final ArrayAdapter spinnerAdapter = new ArrayAdapter<String>(getContext(),
                 R.layout.spinneritem1, new ArrayList<String>( ));
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        final CharSequence[] categories=getArguments().getCharSequenceArray("category");
        mSpinner.setAdapter(spinnerAdapter);

        for(int i=0;i<categories.length;i++){

            spinnerAdapter.add(categories[i]);


        }



        mSpinner.setOnItemSelectedListener(this);

        promptsView.findViewById(R.id.closeFrame).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dialog d=getDialog();

                if(d!=null){

                    getActivityInstance().onCancelClicked(ChangeCategoryPopUp.this);


                }

            }
        });

        promptsView.findViewById(R.id.saveFrame).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog d=getDialog();

                if(d!=null){


                    getActivityInstance().onOkClicked(ChangeCategoryPopUp.this,newCategory);


                }

            }
        });

        container.addView(mSpinner);

        return new AlertDialog.Builder(getActivity())

                .setView(promptsView)
                .create();
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog d = getDialog();

        d .getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        }

}
