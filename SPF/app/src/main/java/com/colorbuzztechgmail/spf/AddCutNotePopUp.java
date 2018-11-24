package com.colorbuzztechgmail.spf;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.icu.lang.UCharacter;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.PopupMenu;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.autofill.AutofillValue;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AddCutNotePopUp extends  Dialog implements  View.OnClickListener ,PopupMenu.OnMenuItemClickListener{


    private CutNoteList cutNoteList;
    private String maxAmount="0";
    private TableLayout tableLayout;
    private PreviewModelInfo model;
    int count=0;


    private CutNoteGenerator.GeneratorType mType= CutNoteGenerator.GeneratorType.MULTI;

    onSavedInterface savedInterface;

    public AddCutNotePopUp(@NonNull Context context, @NonNull CutNoteGenerator.GeneratorType type, @Nullable onSavedInterface savedInterface) {
        super(context);


        this.mType=type;

        this.savedInterface=savedInterface;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.close_button:

                dismiss();
            break;

            case R.id.text:

                if(model!=null){

                    removeTable();



                }else {
                    getModel();
                }
                break;

            case R.id.save_Image:

                saveCutNote();
                break;


        }
    }

    private void getModel(){


              ModelDataBase db=new ModelDataBase(getContext());
              if(db.getModelsCount()>0){

                  if(count<db.getModelsCount()) {
                      count++;
                      model = db.getPreviewModel(count);
                      ((TextView) findViewById(R.id.text)).setText(model.getName());
                      ((TextView) findViewById(R.id.text)).setCompoundDrawablesRelativeWithIntrinsicBounds(getContext().getDrawable(R.drawable.ic_close_black_24dp), null, null, null);
                      BuildTable(model.getSizeList());
                  }else{

                      count=1;
                  }


        }

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        return false;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater li = LayoutInflater.from(getContext());
        final View promptsView = li.inflate(R.layout.cutnote_generator_layout, null);
        final TextView titleText=(TextView) promptsView.findViewById(R.id.titleText);

        tableLayout=(TableLayout)promptsView.findViewById(R.id.tableLayout);
        promptsView.findViewById(R.id.text).setOnClickListener(this);
        promptsView.findViewById(R.id.save_Image).setVisibility(View.GONE);
        promptsView.findViewById(R.id.close_button).setOnClickListener(this);

        promptsView.findViewById(R.id.save_Image).setOnClickListener(this);

        switch (mType){


            case SINGLE:

                titleText.setText(getContext().getResources().getString(R.string.cutNotes_addCutnote));
                break;

            case MULTI:
                titleText.setText(getContext().getResources().getString(R.string.cutNotes_addCutnoteList));

                break;

            case AUTOMATIC:

                break;




        }

        final EditText amountTxt=(EditText)promptsView.findViewById(R.id.amountTxt);

        amountTxt.addTextChangedListener(new GenericTextWatcher(amountTxt));
setCanceledOnTouchOutside(false);

        setContentView(promptsView);


    }

    // Create a Dialog using default AlertDialog builder , if not inflate custom view in onCreateView

    @Override
    public void onStart() {
        super.onStart();

        int width = ViewGroup.LayoutParams.MATCH_PARENT;//(( MainActivity)getActivity()).getWindowManager().getDefaultDisplay().getWidth()-160;
        int height =ViewGroup.LayoutParams.WRAP_CONTENT;
        ;
           getWindow().setLayout(width, height);

           getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));




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

                text="0";
            }
            switch(view.getId()){


                case R.id.amountTxt:

                    maxAmount=text;

                    break;


            }


        }
    }

    private  void saveCutNote(){

        cutNoteList=getCutNoteList();
        ModelDataBase db= new ModelDataBase(getContext());
       cutNoteList.setId(Utils.toIntExact(db.addCutNoteList(cutNoteList)));

            savedInterface.onSaved(cutNoteList,0, true);



                dismiss();



    }

    public interface onSavedInterface{


       void onSaved(Object obj, int position, boolean isEditable);

    }

    private void BuildTable(List<String> sizeList) {

       findViewById(R.id.tableContainer).setVisibility(View.VISIBLE);
        if(tableLayout.getChildCount()>0)
        tableLayout.removeAllViews();

        for(String size:sizeList) {


            TableRow row = new TableRow(getContext());
            row.setLayoutParams(new TableRow.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT,1f));
            row.setPadding(0,0,0,1);

            TextView tv1 = new TextView(getContext());
            tv1.setLayoutParams(new TableRow.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT,0.5f));
            tv1.setGravity(Gravity.CENTER);
            tv1.setBackgroundColor(getContext().getResources().getColor(R.color.colorLightPrimary));
            tv1.setTextColor(getContext().getResources().getColor(R.color.textColorPrimary));
            tv1.setPadding(8,8,8,8);

            tv1.setTextSize(16);
            tv1.setText(size);

            row.addView(tv1);

            final EditText tv2 = new EditText(getContext());
            tv2.setInputType(UCharacter.NumericType.DIGIT);
            tv2.setLayoutParams(new TableRow.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT,0.5f));
            tv2.setGravity(Gravity.CENTER);
            tv2.setBackground(getContext().getResources().getDrawable(R.drawable.edit_txt_background1));

            tv2.setTextColor(getContext().getResources().getColor(R.color.textColorPrimary));
            tv2.setPadding(8,8,8,8);

            tv2.setTextSize(16);
            tv2.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    setSaveVisible();


            }
            });
            tv2.setHint("0");
            row.addView(tv2);


            tableLayout.addView(row);
        }

    }

    private void removeTable(){


        model=null;

        if(tableLayout.getChildCount()>0)
            tableLayout.removeAllViews();
        ((EditText)findViewById(R.id.amountTxt)).setText("");
        findViewById(R.id.tableContainer).setVisibility(View.GONE);

        ((TextView) findViewById(R.id.text)).setText("Buscar Modelo");
        ((TextView) findViewById(R.id.text)).setCompoundDrawablesRelativeWithIntrinsicBounds(getContext().getDrawable(R.drawable.ic_search_black_24dp), null, null, null);



    }

    private CutNoteList getCutNoteList(){



        final Map<String,Integer > aux= new HashMap<>();

        for(int i=0;i<tableLayout.getChildCount();i++){

            TableRow t = (TableRow)tableLayout.getChildAt(i);
            TextView sizeTextView = (TextView) t.getChildAt(0);
            TextView amountTextView = (TextView) t.getChildAt(1);

             String value=amountTextView.getText().toString();
             value.trim();
            if(value.isEmpty()){

                value="0";
            }

            aux.put(sizeTextView.getText().toString(),Integer.valueOf(value));


        }



       return CutNoteGenerator.create(aux,Float.valueOf(maxAmount),model,mType);
    }

    private void setSaveVisible(){

        int count=0;

        for(int i=0;i<tableLayout.getChildCount();i++){

            TableRow t = (TableRow)tableLayout.getChildAt(i);
            TextView sizeTextView = (TextView) t.getChildAt(0);
            TextView amountTextView = (TextView) t.getChildAt(1);

            String value=amountTextView.getText().toString();
            value.trim();
            if(value.isEmpty()){

                value="0";
            }

           count= count+Integer.valueOf(value);


        }


       if(count>0){
           findViewById(R.id.save_Image).setVisibility(View.VISIBLE);

       }else{
           findViewById(R.id.save_Image).setVisibility(View.GONE);

       }
    }

}









