package com.colorbuzztechgmail.spf.popup;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayMap;
import android.databinding.ObservableMap;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.lang.UCharacter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.colorbuzztechgmail.spf.CustomHeader;
import com.colorbuzztechgmail.spf.CutNote;
import com.colorbuzztechgmail.spf.CutNoteAdapter;
import com.colorbuzztechgmail.spf.CutNoteDataset;
import com.colorbuzztechgmail.spf.CutNoteGenerator;
import com.colorbuzztechgmail.spf.CutNoteList;
import com.colorbuzztechgmail.spf.ItemActionListener;
import com.colorbuzztechgmail.spf.ModelDataBase;
import com.colorbuzztechgmail.spf.PreviewModelInfo;
import com.colorbuzztechgmail.spf.R;
import com.colorbuzztechgmail.spf.TitleListItemDecoration;
import com.colorbuzztechgmail.spf.Utils;
import com.colorbuzztechgmail.spf.Utils.onSavedInterface;
import com.colorbuzztechgmail.spf.dataset.Dataset;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.colorbuzztechgmail.spf.BR.fragment;
import static com.colorbuzztechgmail.spf.BR.obj;


public class AddCutNoteListPopUp extends  Dialog implements  View.OnClickListener ,PopupMenu.OnMenuItemClickListener{


    public static final int SEARCH_ST=1;
    public static final int OPTION_ST =2;
    public static final int TABLE_ST =3;
    public static final int CUTNOTELIST_ST =4;

    public static final int REMOVE_ST =5;
    public static final int ADD_ST =6;

    public static final int REFERENCE =7;
    public static final int NOTE_COUNT =8;
    public static final int PAIR_COUNT=9;




    private CutNoteList cutNoteList;
    private TableLayout tableLayout;
    private PreviewModelInfo model;
    private boolean mix=false;
    int modelCount=0;
    int itemPos=0;
    boolean editable=false;
    boolean start=false;
    private Map<String,Integer>oldSizeList;
    private String maxAmount="0";
    ModelDataBase db;
    public ObservableMap<Integer, Object> mObservableMap;
    ViewDataBinding mBinding;
    public CutNoteGenerator.GeneratorType mType= CutNoteGenerator.GeneratorType.MULTI;
    onSavedInterface savedInterface;
    private Switch mSwicht;
    private Deque<Object> cutNoteListBuffer = new ArrayDeque<Object>();
    private CutNoteDataset dataset;



    public AddCutNoteListPopUp(@NonNull Context context, @NonNull CutNoteGenerator.GeneratorType type, @Nullable onSavedInterface savedInterface) {
        super(context);


        this.mType=type;
        this.savedInterface=savedInterface;
        editable=false;
        if(mType.equals(CutNoteGenerator.GeneratorType.MULTI)){
            cutNoteListBuffer= new ArrayDeque<>();

        }



    }

    public AddCutNoteListPopUp(@NonNull Context context,@NonNull  onSavedInterface savedInterface,@NonNull  CutNoteList editCutNote, int position) {
        super(context);


         this.savedInterface=savedInterface;
         this.mType= CutNoteGenerator.GeneratorType.AUTOMATIC;

           this.itemPos=position;
            editable=true;
            cutNoteList=editCutNote;


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.closeFrame:

                dismiss();
            break;

            case R.id.text:

                if(model!=null){

                    removeTable();



                }else {
                    getModel();
                }
                break;

            case R.id.saveFrame:

                switch (mType){

                    case SINGLE:

                        saveCutNote();
                        break;

                    case MULTI:

                        saveCutNote();
                        break;


                    case AUTOMATIC:
                        if(Integer.valueOf(maxAmount)>0){

                            saveCutNote();

                        }else{

                            Utils.toast(getContext(),getContext().getString(R.string.warning_emptyPairCount));
                        }

                        break;


                }

                break;

            case R.id.add_frame:

             showNewCutNote();

                break;


            case R.id.remove_frame:

            removeCutNote();

                break;


        }
    }

    private void getModel(){


              if(db.getModelsCount()>0){

                  if(modelCount<db.getModelsCount()) {
                      modelCount++;
                      model = db.getPreviewModelById(modelCount);
                      ((TextView) findViewById(R.id.text)).setText(model.getName());
                      ((TextView) findViewById(R.id.text)).setCompoundDrawablesRelativeWithIntrinsicBounds(getContext().getDrawable(R.drawable.ic_close_black_24dp), null, null, null);
                      switch (mType){

                          case MULTI:

                              mObservableMap.put(CUTNOTELIST_ST,true);
                              setupRecycler(getWindow().getDecorView());


                              break;



                          default:
                              new createTable();
                              mObservableMap.put(TABLE_ST,true);

                              break;

                      }
                  }else{

                      modelCount=1;
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
         db=new ModelDataBase(getContext());


        LayoutInflater li = LayoutInflater.from(getContext());
        final View promptsView = li.inflate(R.layout.cutnote_generator_layout, null);
        final TextView titleText=(TextView) promptsView.findViewById(R.id.titleText);

        tableLayout=(TableLayout)promptsView.findViewById(R.id.tableLayout);
        promptsView.findViewById(R.id.text).setOnClickListener(this);
        promptsView.findViewById(R.id.saveFrame).setVisibility(View.INVISIBLE);
        promptsView.findViewById(R.id.closeFrame).setOnClickListener(this);
        mSwicht= promptsView.findViewById(R.id.switch1);
        mSwicht.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                mix=b;
            }
        });

        promptsView.findViewById(R.id.saveFrame).setOnClickListener(this);
        final EditText amountTxt=(EditText)promptsView.findViewById(R.id.amountTxt);

        amountTxt.addTextChangedListener(new GenericTextWatcher(amountTxt));

        mObservableMap=new ObservableArrayMap<>();
        mObservableMap.put(SEARCH_ST,false);
        mObservableMap.put(OPTION_ST,false);
        mObservableMap.put(TABLE_ST,false);
        mObservableMap.put(CUTNOTELIST_ST,false);
        mObservableMap.put(REMOVE_ST,false);
        mObservableMap.put(ADD_ST,true);
        mObservableMap.put(NOTE_COUNT,"0");
        mObservableMap.put(REFERENCE,"0");
        mObservableMap.put(PAIR_COUNT,"0");



        if(!editable) {

            switch (mType) {


                case SINGLE:

                    titleText.setText(getContext().getResources().getString(R.string.cutNote));
                    mObservableMap.put(OPTION_ST,false);


                    break;

                case MULTI:
                    titleText.setText(getContext().getResources().getString(R.string.cutNotesList));
                    mObservableMap.put(OPTION_ST,false);
                    mObservableMap.put(REFERENCE, String.valueOf(db.getReference()+1));


                    break;

                case AUTOMATIC:

                    titleText.setText(getContext().getResources().getString(R.string.cutNotes_AutomaticCutnoteList));

                    break;


            }
            mObservableMap.put(SEARCH_ST,true);
            amountTxt.setText("0");

        }else{

            mObservableMap.put(OPTION_ST,true);
            mObservableMap.put(TABLE_ST,true);
            titleText.setText(  getContext().getResources().getString(R.string.action_edit) + " " + getContext().getResources().getString(R.string.cutNotesList)
                    + '\n'  + "Ref:" + String.valueOf(cutNoteList.getReference()));
            amountTxt.setText(String.valueOf(cutNoteList.getMaxPairCount()));
            new createTable();



        }
        setCanceledOnTouchOutside(false);

        bindView(promptsView);


        setContentView(promptsView);


    }

    // Create a Dialog using default AlertDialog builder , if not inflate custom view in onCreateView

    private void bindView(View v){



        if( mBinding==null)
            mBinding = DataBindingUtil.bind(v);


        mBinding.setVariable(fragment,this);
        mBinding.setVariable(obj,mObservableMap);
        mBinding.notifyChange();
        mBinding.executePendingBindings();

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

                    setSaveVisible(tableLayout);

                    break;


            }


        }
    }

    private  void saveCutNote(){


        final CutNoteList  buffer=getCutNoteList();
        buffer.setReference(db.getReference()+1);





        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(
                getContext());
        alertDialogBuilder

                .setTitle( getContext().getString(R.string.cutNotesList) + " " +  (editable ?  cutNoteList.getReference() : buffer.getReference()))
                .setMessage(  getContext().getString(R.string.model) + " : " + buffer.getModel() +  '\n' + getContext().getResources().getString(R.string.cutNotes) + " : " + String.valueOf(buffer.getNoteCount())
                        +  '\n' + getContext().getResources().getString(R.string.cutNotes_pairs) + " : " + String.valueOf(buffer.getTotalPairCount()))
                .setCancelable(false)
                .setPositiveButton( getContext().getResources().getString(R.string.dialog_save),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {



                                if(!editable){
                                    buffer.setId(Utils.toIntExact(db.addCutNoteList(buffer)));
                                }else{


                                    buffer.setReference(cutNoteList.getReference());
                                    buffer.setId(cutNoteList.getId());

                                    db.updateCutNoteList(buffer);

                                }

                                buffer.removeCutNoteList();

                                db.closeDB();

                                if(savedInterface!=null)
                                    savedInterface.onSaved(buffer,itemPos, editable);



                                dismiss();

                            }
                        }
                )
                .setNegativeButton( getContext().getResources().getString(R.string.dialog_cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.cancel();
                            }
                        }
                );
        android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable( getContext().getResources().getDrawable(R.drawable.bg_black_line));

        alertDialog.show();





    }

    private View BuildTable(List<String> sizeList) {

        start=false;


        final TableLayout mtableLayout= new TableLayout(getContext());
        mtableLayout.setLayoutParams(tableLayout.getLayoutParams());


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
        tv1.setText(getContext().getString(R.string.piece_SizeList));
        tv1.setEnabled(false);

        row.addView(tv1);

        final TextView tv2 = new EditText(getContext());
        tv2.setLayoutParams(new TableRow.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT,0.5f));
        tv2.setGravity(Gravity.CENTER);
        tv2.setBackgroundColor(getContext().getResources().getColor(R.color.colorLightPrimary));

        tv2.setTextColor(getContext().getResources().getColor(R.color.textColorPrimary));
        tv2.setPadding(8,8,8,8);

        tv2.setEnabled(false);
        tv2.setTextSize(16);
        tv2.setText(getContext().getString(R.string.cutNotes_pairs));
        row.addView(tv2);

        mtableLayout.addView(row);

        for(String size:sizeList) {


            TableRow row1 = new TableRow(getContext());
            row1.setLayoutParams(new TableRow.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT,1f));
            row1.setPadding(0,0,0,1);

            TextView tv3 = new TextView(getContext());
            tv3.setLayoutParams(new TableRow.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT,0.5f));
            tv3.setGravity(Gravity.CENTER);
            tv3.setBackgroundColor(getContext().getResources().getColor(R.color.colorLightPrimary));
            tv3.setTextColor(getContext().getResources().getColor(R.color.textColorPrimary));
            tv3.setPadding(8,8,8,8);

            tv3.setTextSize(16);
            tv3.setText(size);

            row1.addView(tv3);

            final EditText tv4 = new EditText(getContext());
            tv4.setInputType(UCharacter.NumericType.DIGIT);
            tv4.setLayoutParams(new TableRow.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT,0.5f));
            tv4.setGravity(Gravity.CENTER);
            tv4.setBackground(getContext().getResources().getDrawable(R.drawable.edit_txt_background1));

            tv4.setTextColor(getContext().getResources().getColor(R.color.textColorPrimary));
            tv4.setPadding(8,8,8,8);

            tv4.setTextSize(16);
            tv4.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    setSaveVisible(mtableLayout);


            }
            });
            tv4.setHint("0");
            row1.addView(tv4);


           mtableLayout.addView(row1);
        }


        TableRow row2 = new TableRow(getContext());
        row2.setLayoutParams(new TableRow.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT,1f));
        row2.setPadding(0,0,0,1);

        TextView tv5 = new TextView(getContext());
        tv5.setLayoutParams(new TableRow.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT,0.5f));
        tv5.setGravity(Gravity.CENTER);
        tv5.setBackgroundColor(getContext().getResources().getColor(R.color.colorLightPrimary));
        tv5.setTextColor(getContext().getResources().getColor(R.color.textColorPrimary));
        tv5.setPadding(8,8,8,8);

        tv5.setTextSize(16);
        tv5.setText(getContext().getString(R.string.cutNotes_pairs));
        tv5.setEnabled(false);

        row2.addView(tv5);

        final TextView tv6 = new EditText(getContext());
        tv6.setLayoutParams(new TableRow.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT,0.5f));
        tv6.setGravity(Gravity.CENTER);
        tv6.setBackground(getContext().getResources().getDrawable(R.drawable.edit_txt_background1));

        tv6.setTextColor(getContext().getResources().getColor(R.color.textColorPrimary));
        tv6.setPadding(8,8,8,8);

        tv6.setEnabled(false);
        tv6.setTextSize(16);
        tv6.setText("0");
        row2.addView(tv6);

        mtableLayout.addView(row2);
        start=true;

        return mtableLayout;


    }

    private void BuildEditableTable(Map<String,Integer> sizeValuesMap) {


        start=false;

        if(mType.equals(CutNoteGenerator.GeneratorType.AUTOMATIC))
            mObservableMap.put(OPTION_ST,true);


        if(tableLayout.getChildCount()>0)
            tableLayout.removeAllViews();

        final List<String>stringList= new ArrayList<>();
        int pairCount=0;

        oldSizeList=new HashMap<>();

        oldSizeList.putAll(sizeValuesMap);



        for(Object obj:oldSizeList.keySet().toArray())
            stringList.add((String)obj);


        Collections.sort(stringList);





        for(String size: stringList) {

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



                       setSaveVisible(tableLayout);


                }
            });

            tv2.setHint("0");
            if(oldSizeList.get(size)>0){

                tv2.setText(String.valueOf(oldSizeList.get(size)));

            }

            pairCount=oldSizeList.get(size)+pairCount;

            if(String.valueOf(oldSizeList.get(size)).equals("0")){
                tv2.setTextColor(getContext().getResources().getColor(R.color.textColorTerciary));
                tv1.setTextColor(getContext().getResources().getColor(R.color.textColorTerciary));

            }else{
                tv2.setTextColor(getContext().getResources().getColor(R.color.textColorPrimary));
                tv1.setTextColor(getContext().getResources().getColor(R.color.textColorPrimary));

            }



            row.addView(tv2);


            tableLayout.addView(row);
        }



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
        tv1.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimary));
        tv1.setTextColor(getContext().getResources().getColor(R.color.textColorPrimary));
        tv1.setPadding(8,8,8,8);

        tv1.setTextSize(16);
        tv1.setText(getContext().getString(R.string.cutNotes_pairs));

        row.addView(tv1);

        final TextView tv2 = new EditText(getContext());
        tv2.setLayoutParams(new TableRow.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT,0.5f));
        tv2.setGravity(Gravity.CENTER);
        tv2.setBackground(getContext().getResources().getDrawable(R.drawable.edit_txt_background1));

        tv2.setTextColor(getContext().getResources().getColor(R.color.colorAccentLight));
        tv2.setPadding(8,8,8,8);

        tv2.setEnabled(false);
        tv2.setTextSize(16);
        tv2.setText(String.valueOf(pairCount));
        row.addView(tv2);

        tableLayout.addView(row);

        start=true;

    }

    private void setupRecycler(View v){

        RecyclerView mRecycler=v.findViewById(R.id.recycler);
        mRecycler.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        CutNoteAdapter cutNoteAdapter= new CutNoteAdapter(new ItemActionListener() {
            @Override
            public void onPreview(Object obj) {

            }

            @Override
            public void toRemove(Object obj) {

            }

            @Override
            public void toEdit(long[] position) {

            }

            @Override
            public void onClick(View v, int position, boolean isLongClick) {

            }
        },(AppCompatActivity) getOwnerActivity(),true);
        dataset=new CutNoteDataset(mRecycler, cutNoteAdapter);
        cutNoteAdapter.dataset(dataset);
        mRecycler.setAdapter(cutNoteAdapter);

        int spacing=Utils.pxTodp(8,getContext());



        TitleListItemDecoration decoration= new TitleListItemDecoration(spacing,cutNoteAdapter,1,false);
        mRecycler.addItemDecoration(decoration);
        final LinearLayoutManager lm = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        lm.setAutoMeasureEnabled(false);

        mRecycler.setLayoutManager(lm);

        dataset.setEmptyTitle();
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


       return CutNoteGenerator.create(getValuesOfTable(tableLayout),Float.valueOf(maxAmount),model,mType,mix);
    }


    private Map<String,Integer> getValuesOfTable(TableLayout tableLayout){


        final Map<String,Integer > aux= new HashMap<>();

        for(int i=0;i<tableLayout.getChildCount()-1;i++){

            if(mType.equals(CutNoteGenerator.GeneratorType.MULTI)){

                if(i==0){
                    i=1;
                }
            }

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

        return aux;
    }

    private boolean setSaveVisible(TableLayout tableLayout){


        if(start) {

            int count = 0;
            if (tableLayout.getChildCount() > 0) {

                for (int i = 0; i < tableLayout.getChildCount() - 1; i++) {
                    if(mType.equals(CutNoteGenerator.GeneratorType.MULTI)){

                        if(i==0){
                            i=1;
                        }
                    }

                    TableRow t = (TableRow) tableLayout.getChildAt(i);
                    TextView sizeTextView = (TextView) t.getChildAt(0);
                    TextView amountTextView = (TextView) t.getChildAt(1);

                    String value = amountTextView.getText().toString();
                    value.trim();
                    if (value.isEmpty()) {

                        value = "0";

                    }

                    if (value.equals("0")) {
                        amountTextView.setTextColor(getContext().getResources().getColor(R.color.textColorTerciary));
                        sizeTextView.setTextColor(getContext().getResources().getColor(R.color.textColorTerciary));

                    } else {
                        amountTextView.setTextColor(getContext().getResources().getColor(R.color.textColorPrimary));
                        sizeTextView.setTextColor(getContext().getResources().getColor(R.color.textColorPrimary));


                    }


                    count = count + Integer.valueOf(value);


                }


                TableRow t = (TableRow) tableLayout.getChildAt(tableLayout.getChildCount() - 1);
                TextView amountTextView = (TextView) t.getChildAt(1);

                amountTextView.setText(String.valueOf(count));

                switch (mType){



                    case MULTI:


                        if (count > 0) {


                            return true;
                        } else {

                            return false;
                        }




                    default:
                        if (count > 0 && Integer.valueOf(maxAmount) > 0 && isValuesChange()) {

                            findViewById(R.id.saveFrame).setVisibility(View.VISIBLE);
                            mObservableMap.put(ADD_ST,true);
                            return true;
                        } else {

                            findViewById(R.id.saveFrame).setVisibility(View.INVISIBLE);
                            mObservableMap.put(ADD_ST,false);
                            return false;
                        }





                }




            }
        }
        return false;

    }

    private boolean isValuesChange(){

        if(start) {

            for (int i = 0; i < tableLayout.getChildCount(); i++) {

                TableRow t = (TableRow) tableLayout.getChildAt(i);
                TextView sizeTextView = (TextView) t.getChildAt(0);
                TextView amountTextView = (TextView) t.getChildAt(1);

                String value = amountTextView.getText().toString();
                String size = sizeTextView.getText().toString();

                value.trim();

                if (value.isEmpty()) {

                    value = "0";
                }

                if (oldSizeList.get(size) != Integer.valueOf(value)) {

                    return true;

                }

            }



            if(Integer.valueOf(maxAmount)!=cutNoteList.getMaxPairCount()){

                return true;
            }
        }

   return false;
    }

    public class createTable extends AsyncTask<Object, Integer, Map<String,Integer>> {

        Map<String,Integer> sizeMap;

        public createTable() {

            execute();

        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Map doInBackground(Object... params) {

            sizeMap=new HashMap<>();

            try {


                if(editable){

                    model=db.getPreviewModelByCutNoteList(cutNoteList.getId());

                    final List<CutNote> cutNotes=db.getCutNoteByCuteNoteListId(cutNoteList.getId());
                    for(CutNote cutNote:cutNotes){


                        for(Object obj:cutNote.getSizeList().keySet().toArray())
                            cutNoteList.addCount((String)obj,cutNoteList.getCountAtSize((String)obj)+cutNote.getCountAtSize((String)obj));


                    }

                    return cutNoteList.getSizeList();

                }else {

                    for(String size:model.getSizeList())
                                sizeMap.put(size,0);


                            return sizeMap;
                }




                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("Fallo Tabla", "Error:" +
                            e.toString() + "Modelo: " +(model.getName()));



            }

            return null;
        }

        @Override
        protected void onPostExecute(Map<String,Integer> result) {


            BuildEditableTable(result);


        }

        @Override
        protected void onProgressUpdate(Integer... values) {

        }

    }

    private void showNewCutNote(){



       if((boolean)mObservableMap.get(ADD_ST)){

           createAddCutNoteDialog();
       }






    }

    private void removeCutNote(){



        mObservableMap.put(NOTE_COUNT,String.valueOf(cutNoteListBuffer.size()));

    }

    private void createAddCutNoteDialog() {

        final TableLayout tableLayout= (TableLayout) BuildTable(model.getSizeList());

        Utils.setMargins(tableLayout,8,8,8,8);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getContext());

        alertDialogBuilder

                .setCancelable(false)
                .setPositiveButton(getContext().getString(R.string.dialog_save),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                if(setSaveVisible(tableLayout)){


                                    final CutNote note=CutNoteGenerator.createSingleCutNote(getValuesOfTable(tableLayout),model.getName());
                                    note.setNoteNumber(dataset.getItemCount()+1);
                                    note.setReference(Integer.valueOf((String) mObservableMap.get(REFERENCE)));
                                    mObservableMap.put(NOTE_COUNT, String.valueOf(dataset.getItemCount()));
                                    final int pairCount= Integer.valueOf((String)mObservableMap.get(PAIR_COUNT));
                                    mObservableMap.put(PAIR_COUNT,String.valueOf(pairCount+note.getPairCount()));
                                    dataset.add(note);


                                }

                            }


                        })

        .setNegativeButton(getContext().getString(R.string.dialog_cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }


                })

                .setTitle(getContext().getString(R.string.cutNotes_addCutnote))
                .setView(tableLayout);





        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable( getContext().getDrawable(R.drawable.bg_black_line));


        alertDialog.show();
    }


}









