
package com.colorbuzztechgmail.spf;

import android.content.DialogInterface;
import android.databinding.ObservableMap;
import android.graphics.drawable.Drawable;
import android.icu.lang.UCharacter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.colorbuzztechgmail.spf.dataset.Dataset;
import com.colorbuzztechgmail.spf.popup.BaseAddEditPopUp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditCutNoteList extends BaseAddEditPopUp  implements View.OnClickListener {

    public static final String EDITABLE_ST="editable";
    public static final String GENERATOR_TYPE="generator_type";



    public static final int REMOVE_ST =3;
    public static final int ADD_ST =4;

    public static final int MODEL_NAME =5;

    public static final int REFERENCE =6;
    public static final int NOTE_COUNT =7;
    public static final int PAIR_COUNT=8;
    public static final int EDIT_MODE= 9;
    public static final int CUTNOTE_STATUS= 10;
    public static final int MAX_PAIR_COUNT =11;

    public static final int MODEL_IN=12;



    private CutNoteDataset dataset;

    int modelCount=0;
    public CutNoteList cutNoteList;
    private View mConteView;

     private Spinner mSpinnerStatus;
    ObservableMap<Integer, Object> values;
    private TableLayout tableLayout;
    private PreviewModelInfo model;
    private boolean mix=false;
    public CutNoteGenerator.GeneratorType mType= CutNoteGenerator.GeneratorType.MULTI;
    private Switch mSwicht;



    @Override
    protected View setViewLayout(LayoutInflater inflater) {
        mConteView=inflater.inflate(R.layout.cutnote_generator_layout,null);


        return mConteView;
    }

    public static EditCutNoteList newInstanceEditListCutNotePopUp(@NonNull int[] position, @NonNull long[] itemId, @Nullable Utils.onSavedInterface saveListener) {

        EditCutNoteList f= new EditCutNoteList();

        Bundle args=new Bundle();
        args.putLongArray(DATABASE_ITEM_ID,itemId);
        args.putIntArray(ADAPTER_ITEM_POSITION,position);
        args.putBoolean(EDITABLE_ST,true);
        f.setArguments(args);
        f.setSavedInterface(saveListener);
        return f;

    }


    public static EditCutNoteList newInstanceAddCutNoteListPopUp(@NonNull CutNoteGenerator.GeneratorType type, @Nullable Utils.onSavedInterface saveListener) {

        EditCutNoteList f= new EditCutNoteList();

        Bundle args=new Bundle();
        args.putBoolean(EDITABLE_ST,false);
        args.putString(GENERATOR_TYPE,type.name());

        f.setArguments(args);
        f.setSavedInterface(saveListener);
        return f;

    }


    @Override
    public void setupValues(View v) {
        if(getArguments().getString(GENERATOR_TYPE)!=null)
        mType= CutNoteGenerator.GeneratorType.valueOf(getArguments().getString(GENERATOR_TYPE));

        mObservableMap.put(EDIT_MODE,editable);
        mObservableMap.put(MODEL_IN,false);
        mObservableMap.put(REMOVE_ST,false);
        mObservableMap.put(ADD_ST,true);
        mObservableMap.put(NOTE_COUNT,"0");
        mObservableMap.put(REFERENCE,"0");
        mObservableMap.put(PAIR_COUNT,"0");
        mObservableMap.put(MODEL_NAME,null);

        final TextView titleText=(TextView) v.findViewById(R.id.titleText);

        titleText.setText(getTitle());
        if(editable){


            if(isMultipleEditable()){


                setMultipleEditableValues(v);


            }else{



                setEditableObject();

                setSingleEditableValues(v);
                setupRecycler(v);
                new loadCutNotes();


            }


        }else{
            setDefaultValues(v);




        }

        initializeViews(v);




        saveView= v.findViewById(R.id.saveFrame);

        saveView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSaveDialog();
            }
        });
        saveView.setVisibility(View.INVISIBLE);


        v.findViewById(R.id.closeFrame).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().cancel();

            }
        });


    }

    @Override
    protected void initializeViews(View v) {


        mSpinnerStatus = v.findViewById(R.id.spinnerStatus);

        tableLayout=v.findViewById(R.id.tableLayout);



        mSwicht= v.findViewById(R.id.switch1);
        mSwicht.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                mix=b;
            }
        });




        final String[] aux = getContext().getResources().getStringArray(R.array.cutNotes_status);

        final List<String> status = new ArrayList<>();

               status.addAll(Arrays.asList(aux));
               status.add(newValue);





        final ArrayAdapter spinnerAdapter = new ArrayAdapter<String>(getActivity().getBaseContext(),
                    R.layout.spinneritem1, status){


                @Override
                public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

                    View v = null;
                    parent.setVerticalScrollBarEnabled(true);

                    // If this is the initial dummy entry, make it hidden
                if ( ((String)status.get(position)).equals(newValue)) {
                        TextView tv = new TextView(getContext());
                        tv.setHeight(0);
                        tv.setVisibility(View.GONE);
                        v = tv;

                        return v;

                    }else {



                    View customView = LayoutInflater.from(getContext()).inflate(R.layout.dropdown_spinneritem,null);
                    TextView tv = customView.findViewById(R.id.spinnerItem);
                    tv.setText(status.get(position));
                    Drawable drawable=Utils.getImageAtStatus(getContext(), Utils.convertStringToStatus(getContext(), status.get(position)));
                    tv.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
                    // Pass convertView as null to prevent reuse of special case view

                    return tv;
                }
                   /*
                    // Hide scroll bar because it appears sometimes unnecessarily, this does not prevent scrolling
                    v = super.getDropDownView(position, null, parent);
  */

                }

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

                if ( ((String)status.get(position)).equals(newValue)) {
                    return super.getView(position, null, parent);


                }else {



                    View customView = LayoutInflater.from(getContext()).inflate(R.layout.spinneritem1,null);
                    TextView tv = customView.findViewById(R.id.spinnerItem);
                    tv.setText(status.get(position));
                    Drawable drawable=Utils.getImageAtStatus(getContext(), Utils.convertStringToStatus(getContext(), status.get(position)));
                    tv.setCompoundDrawablesWithIntrinsicBounds(drawable, null, getContext().getDrawable(R.drawable.ic_keyboard_arrow_down_primary_dark_24dp), null);
                    // Pass convertView as null to prevent reuse of special case view

                    return tv;
                }
            }
        }


        ;
            spinnerAdapter.setDropDownViewResource(R.layout.dropdown_spinneritem);



           mSpinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                    if(!((String)mSpinnerStatus.getSelectedItem()).equals((String) values.get(CUTNOTE_STATUS))){

                        getValueViewMap().get(R.id.spinnerStatus).setNewValue((String)mSpinnerStatus.getSelectedItem());

                    };


                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

           mSpinnerStatus.setAdapter(spinnerAdapter);
           mSpinnerStatus.setPrompt(getString(R.string.cutNotes_status));







    }

    @Override
    protected void setMultipleEditableValues(View v) {


        addValueItem(new ValueViewItem(v.findViewById(R.id.spinnerStatus),CUTNOTE_STATUS,newValue,v.findViewById(R.id.undo_status_frame)));

        values=mObservableMap;


    }

    @Override
    protected Object setEditableObject() {

        if(!isMultipleEditable()){

           cutNoteList = db.getCutNoteListById(Utils.toIntExact(getmItemIds()[0]));
            mObservableMap.put(MODEL_NAME,cutNoteList.getModel());
            mObservableMap.put(REFERENCE,String.valueOf(cutNoteList.getReference()));
            mObservableMap.put(NOTE_COUNT,String.valueOf(cutNoteList.getNoteCount()));
            mObservableMap.put(PAIR_COUNT,String.valueOf(cutNoteList.getTotalPairCount()));




        }

        return null;

    }

    @Override
    protected void setSingleEditableValues(View v) {


        addValueItem(new ValueViewItem(v.findViewById(R.id.spinnerStatus),CUTNOTE_STATUS, Utils.convertStatusToString(getContext(),cutNoteList.getStatus()),v.findViewById(R.id.undo_status_frame)));





        values=mObservableMap;




    }

    @Override
    protected String getTitle() {



        if(isMultipleEditable()){
            return getString(R.string.action_edit) + " " + getString(R.string.cutNotesLists);

        }else if(!editable) {


            switch (mType){

                case AUTOMATIC:

                    return getContext().getResources().getString(R.string.cutNotes_AutomaticCutnoteList);

                case MULTI:


                    return getContext().getResources().getString(R.string.cutNotesList);


            }

            return getString(R.string.cutNotes_addCutnoteList);

        }else {

            return getString(R.string.action_edit) + " " + getString(R.string.cutNotesList);

        }

    }

    @Override
    protected void setDefaultValues(View v) {




        addValueItem(new ValueViewItem(v.findViewById(R.id.spinnerStatus),CUTNOTE_STATUS,newValue,v.findViewById(R.id.undo_status_frame)));
        addValueItem(new ValueViewItem(v.findViewById(R.id.amountTxt), MAX_PAIR_COUNT,"0",v.findViewById(R.id.undo_maxpair_frame)));




        mObservableMap.put(REFERENCE,String.valueOf(db.getReference()+1));








        values=mObservableMap;



    }

    @Override
    protected void saveData() {



            if(editable){

                if(isMultipleEditable()){

                    final Map<Integer, Long> cutnoteListMap = new HashMap();


                    List<Integer> sortedPos = new ArrayList<>();

                    for (int i = 0; i < mItemIds.length ; i++) {

                        cutnoteListMap.put(mItemPos[i], mItemIds[i]);
                        sortedPos.add(mItemPos[i]);

                    }

                    Collections.sort(sortedPos, new Comparator<Integer>() {
                        @Override
                        public int compare(Integer integer, Integer t1) {

                            return Integer.compare(integer, t1) * -1;
                        }
                    });

                    for (Integer pos : sortedPos) {

                        final CutNoteList cutNoteList = db.getCutNoteListById(Utils.toIntExact(cutnoteListMap.get(pos)));

                        ///////updateValues////


                        if (getValueViewMap().get(mViewIdMap.get(CUTNOTE_STATUS)).isValueChanged())
                            cutNoteList.setStatus(Utils.convertStringToStatus(getContext(), (String) getValueForIdentifier(CUTNOTE_STATUS)));


                        ///////////////////////


                        db.updateCutNoteList(cutNoteList);

                        if (mSavedInterface != null) {


                            mSavedInterface.onSaved(cutNoteList, pos,true);

                        }
                    }


                }else{




                    this.cutNoteList.addCutNote(dataset.getCutNoteItems());
                    cutNoteList.setNoteCount(Integer.valueOf((String)mObservableMap.get(NOTE_COUNT)));

                    cutNoteList.setTotalPairCount(Integer.valueOf((String)mObservableMap.get(PAIR_COUNT)));
                    cutNoteList.setReference(Long.valueOf ((String)mObservableMap.get(REFERENCE)));
                    cutNoteList.setModel((String)mObservableMap.get(MODEL_NAME));
                    cutNoteList.setTotalPairCount(Integer.valueOf((String)mObservableMap.get(PAIR_COUNT)));

                    if(getValueViewMap().get(mViewIdMap.get(CUTNOTE_STATUS)).isValueChanged())
                        cutNoteList.setStatus(Utils.convertStringToStatus(getContext(),(String)getValueForIdentifier(CUTNOTE_STATUS)));


                    db.updateCutNoteList(cutNoteList);



                   cutNoteList.removeCutNoteList();

                    if(mSavedInterface!=null)
                        mSavedInterface.onSaved(cutNoteList,mItemPos[0], true);


                }




            }else{


                    CutNoteList buffer=getCutNoteList();

                    buffer.setId(Utils.toIntExact(db.addCutNoteList(buffer)));

                if(mSavedInterface!=null)
                    mSavedInterface.onSaved(buffer,0, false);


            }


            db.closeDB();
            dismiss();



    }

    @Override
    protected View getBindableView() {

        return mConteView;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.modelFrame:



                if(model!=null){

                    removeTable();
                    mObservableMap.put(MODEL_NAME,null);
                    mObservableMap.put(MODEL_IN,false);



                }else {
                    getModel();
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


    @Override
    public void setSaveVisible() {


        if(start) {

            switch (mType){


                case AUTOMATIC:
                    for(int i=0;i<getValueViewMap().keySet().size();i++){

                        if ( (! getValueViewMap().get(getValueViewMap().keySet().toArray()[i]).isValueChanged()) || isEmptyTable(tableLayout)
                                || Integer.valueOf((String)getValueForIdentifier(MAX_PAIR_COUNT))==0){

                            saveView.setVisibility(View.INVISIBLE);

                        } else {

                            saveView.setVisibility(View.VISIBLE);
                            break;

                        }


                    }

                    break;


                case MULTI:


                    if(!(boolean)mObservableMap.get(EDIT_MODE)) {

                        for (int i = 0; i < getValueViewMap().keySet().size(); i++) {

                            if ((!getValueViewMap().get(getValueViewMap().keySet().toArray()[i]).isValueChanged()) || dataset.size() == 0) {

                                saveView.setVisibility(View.INVISIBLE);

                            } else {

                                saveView.setVisibility(View.VISIBLE);
                                break;

                            }


                        }
                    }else{


                        for (int i = 0; i < getValueViewMap().keySet().size(); i++) {

                            if ((!getValueViewMap().get(getValueViewMap().keySet().toArray()[i]).isValueChanged())) {

                                saveView.setVisibility(View.INVISIBLE);

                            } else {

                                saveView.setVisibility(View.VISIBLE);
                                break;

                            }


                        }


                    }


                    break;





            }







        }


    }


    private boolean isEmptyTable(TableLayout tableLayout){


        if(start) {

            int count = 0;
            if (tableLayout.getChildCount() > 0) {

                for (int i = 1; i < tableLayout.getChildCount() - 1; i++) {


                    TableRow t = (TableRow) tableLayout.getChildAt(i);
                    TextView sizeTextView = (TextView) t.getChildAt(0);
                    TextView amountTextView = (TextView) t.getChildAt(1);

                    String value = amountTextView.getText().toString();
                    value.trim();
                    if (value.isEmpty()) {

                        value = "0";

                    }

                    count = count + Integer.valueOf(value);


                }


                TableRow t = (TableRow) tableLayout.getChildAt(tableLayout.getChildCount() - 1);
                TextView amountTextView = (TextView) t.getChildAt(1);

                amountTextView.setText(String.valueOf(count));
                mObservableMap.put(PAIR_COUNT,String.valueOf(count));

                        if (count > 0) {


                            return false;
                        } else {

                            return true;
                        }




            }
        }
        return true;

    }

    private View BuildTable(List<String> sizeList) {

        start=false;





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

        tableLayout.addView(row);

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
                    setSaveVisible();


                }
            });
            tv4.setHint("0");
            row1.addView(tv4);


            tableLayout.addView(row1);
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

        tableLayout.addView(row2);
        start=true;

        return tableLayout;


    }

    private void BuildTable(Map<String,Integer> sizeValuesMap) {


        start=false;




        if(tableLayout.getChildCount()>0)
            tableLayout.removeAllViews();

        final List<String>stringList= new ArrayList<>();
        int pairCount=0;




        for(Object obj:sizeValuesMap.keySet().toArray())
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



                    setSaveVisible();


                }
            });

            tv2.setHint("0");
            if(sizeValuesMap.get(size)>0){

                tv2.setText(String.valueOf(sizeValuesMap.get(size)));

            }

            pairCount=sizeValuesMap.get(size)+pairCount;





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



    private Map<String,Integer> getValuesOfTable(TableLayout tableLayout){


        final Map<String,Integer > aux= new HashMap<>();

        for(int i=1;i<tableLayout.getChildCount()-1;i++){


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

    private void getModel() {


        if(db.getModelsCount()>0){

            if(modelCount<db.getModelsCount()) {
                modelCount++;
                model = db.getPreviewModelById(modelCount);

                mObservableMap.put(MODEL_NAME,model.getName());
                mObservableMap.put(MODEL_IN,true);

                //((TextView) findViewById(R.id.text)).setCompoundDrawablesRelativeWithIntrinsicBounds(getContext().getDrawable(R.drawable.ic_close_black_24dp), null, null, null);
                switch (mType){

                    case MULTI:


                        setupRecycler(getView());


                        break;



                    case AUTOMATIC:

                        BuildTable(model.getSizeList());

                        break;

                }
            }else{

                modelCount=1;
            }


        }

    }


    private void setupRecycler(View v){

        RecyclerView mRecycler=v.findViewById(R.id.recycler);
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

                if(!isLongClick){


                    Utils.showInfoPopUp(getContext(),dataset.getCutNote(position));




                }

            }
        },(AppCompatActivity) getActivity(),true);
        dataset=new CutNoteDataset(mRecycler, cutNoteAdapter);
        cutNoteAdapter.dataset(dataset);
        mRecycler.setAdapter(cutNoteAdapter);

        int spacing=Utils.pxTodp(8,getContext());



        TitleListItemDecoration decoration= new TitleListItemDecoration(spacing,cutNoteAdapter,1,false);
        mRecycler.addItemDecoration(decoration);
        final LinearLayoutManager lm = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        lm.setAutoMeasureEnabled(false);

        mRecycler.setLayoutManager(lm);

        //dataset.setEmptyTitle();
    }

    private void removeTable(){


        model=null;

        if(tableLayout.getChildCount()>0)
            tableLayout.removeAllViews();
        ((EditText)getView().findViewById(R.id.amountTxt)).setText("");
        getView().findViewById(R.id.tableContainer).setVisibility(View.GONE);




    }

    private void showNewCutNote(){



        if((boolean)mObservableMap.get(ADD_ST)){

            createAddCutNoteDialog();
        }






    }

    private void removeCutNote(){


    }

    private void createAddCutNoteDialog() {

        final ArrayList<String> sizeList = new ArrayList<>();

        if(editable){

            if(!isMultipleEditable()){

                sizeList.addAll(db.getPreviewModelByCutNoteList(mItemIds[0]).getSizeList());

            }

        }else {

            sizeList.addAll(model.getSizeList());

        }

        final EditCutNote editCutNote = EditCutNote.newInstanceAddCutNotePopUp(sizeList, new Utils.onSavedInterface() {
            @Override
            public void onSaved(Object obj, int position, boolean isEditable) {

                int count = Integer.valueOf((String) mObservableMap.get(NOTE_COUNT)) + 1;

                final CutNote note = (CutNote) obj;

                note.setModel((String)mObservableMap.get(MODEL_NAME));
                note.setNoteNumber(count);
                note.setId(note.getNoteNumber());
                note.setReference(Integer.valueOf((String) mObservableMap.get(REFERENCE)));
                final int pairCount = Integer.valueOf((String) mObservableMap.get(PAIR_COUNT));
                dataset.addNewItem(note);
                mObservableMap.put(NOTE_COUNT, String.valueOf(count));
                mObservableMap.put(PAIR_COUNT, String.valueOf(pairCount + note.getPairCount()));

                saveView.setVisibility(View.VISIBLE);


            }
        });

        editCutNote.show(getChildFragmentManager(), "add_cutnote");
    }

    private void showSaveDialog(){


        if(!isMultipleEditable()) {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    getContext());
            alertDialogBuilder

                    .setTitle(getContext().getString(R.string.cutNotesList) + " " + (String) mObservableMap.get(REFERENCE))
                    .setMessage(getContext().getString(R.string.model) + " : " + (String) mObservableMap.get(MODEL_NAME) + '\n'
                            + getContext().getResources().getString(R.string.cutNotes) + " : " + (String) mObservableMap.get(NOTE_COUNT) + '\n'
                            + getContext().getResources().getString(R.string.cutNotes_pairs) + " : " + (String) mObservableMap.get(PAIR_COUNT))
                    .setCancelable(false)
                    .setPositiveButton(getContext().getResources().getString(R.string.dialog_save),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    saveData();


                                    dismiss();

                                }
                            }
                    )
                    .setNegativeButton(getContext().getResources().getString(R.string.dialog_cancel),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    dialog.cancel();
                                }
                            }
                    );
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.getWindow().setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.bg_black_line));

            alertDialog.show();

        }else{


            saveData();
        }





    }

    private CutNoteList getCutNoteList(){

        switch (mType){


            case MULTI:


                final CutNoteList cutNoteList= new CutNoteList(0);


                cutNoteList.addCutNote(dataset.getCutNoteItems());

                cutNoteList.setReference(Long.valueOf ((String)mObservableMap.get(REFERENCE)));
                cutNoteList.setModel((String)mObservableMap.get(MODEL_NAME));
                cutNoteList.setTotalPairCount(Integer.valueOf((String)mObservableMap.get(PAIR_COUNT)));



                return  cutNoteList;


            case AUTOMATIC:

                return   CutNoteGenerator.create(getValuesOfTable(tableLayout),Float.valueOf((String) getValueForIdentifier(MAX_PAIR_COUNT)),model,mType,mix);



            case SINGLE:


                break;


        }


        return null;
    }


    public class loadCutNotes extends AsyncTask<Object, Integer, List<Object>> {


        public loadCutNotes() {

            execute();

        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected List<Object> doInBackground(Object... params) {


            try {




                    final List<Object> cutNotes;

                    cutNotes=new ArrayList<>();
                    cutNotes.addAll(db.getCutNoteByCuteNoteListId(cutNoteList.getId()));


                    return cutNotes;



            } catch (Exception e) {
                e.printStackTrace();
                Log.e("Fallo Tabla", "Error:" +
                        e.toString() + "Modelo: " +(cutNoteList.getModel()));



            }

            return null;
        }

        @Override
        protected void onPostExecute(List<Object> result) {


            if(dataset!=null){

                dataset.AutomaticTitleGeneratorSortType(result,false);

            }


        }

        @Override
        protected void onProgressUpdate(Integer... values) {

        }

    }


}

