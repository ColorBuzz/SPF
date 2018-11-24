
package com.colorbuzztechgmail.spf;

import android.app.Dialog;
import android.content.Intent;
import android.databinding.ObservableMap;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.icu.lang.UCharacter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.colorbuzztechgmail.spf.popup.BaseAddEditPopUp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.colorbuzztechgmail.spf.MainActivity.REQUEST_CODE_MATERIAL_IMAGE_GALLERY;

public class EditCutNote extends BaseAddEditPopUp  implements View.OnClickListener {

    public static final int MODEL_NAME=1;
    public static final int WORKER_ITEM= 2;
    public static final int CUTNOTE_STATUS= 3;
   public static final int EDIT_MODE= 4;
    public static final String SIZE_LIST="sizeList";



    public CutNote cutnote;
    private View mConteView;

     private Spinner mSpinnerStatus;
    ObservableMap<Integer, Object> values;
    private TableLayout tableLayout;
    private List<String>sizeList;


    @Override
    protected View setViewLayout(LayoutInflater inflater) {
        mConteView=inflater.inflate(R.layout.edit_cutnote_container,null);


        return mConteView;
    }

    public static EditCutNote newInstanceEditCutNotePopUp(@NonNull int[] position,@NonNull long[] itemId,@Nullable Utils.onSavedInterface saveListener) {

        EditCutNote f= new EditCutNote();

        Bundle args=new Bundle();
        args.putLongArray(DATABASE_ITEM_ID,itemId);
        args.putIntArray(ADAPTER_ITEM_POSITION,position);
        args.putBoolean(EDITABLE_ST,true);
        f.setArguments(args);
        f.setSavedInterface(saveListener);
        return f;

    }
    public static EditCutNote newInstanceAddCutNotePopUp(@NonNull ArrayList sizeList, @Nullable Utils.onSavedInterface saveListener) {

        EditCutNote f= new EditCutNote();

        Bundle args=new Bundle();
        args.putBoolean(EDITABLE_ST,false);
        args.putStringArrayList(SIZE_LIST,sizeList);

        f.setArguments(args);
        f.setSavedInterface(saveListener);
        return f;

    }


    @Override
    public void setupValues(View v) {



        if(editable){
            final TextView titleText=(TextView) v.findViewById(R.id.titleText);

            titleText.setText(getTitle());




            if(isMultipleEditable()){


                setMultipleEditableValues(v);


            }else{


                setSingleEditableValues(v);

            }


        }else{
            setDefaultValues(v);

            if(getArguments().get(SIZE_LIST)!=null){

                sizeList=new ArrayList<>();
                sizeList.addAll(getArguments().getStringArrayList(SIZE_LIST));


            }



        }

        initializeViews(v);




        saveView= v.findViewById(R.id.saveFrame);

        saveView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
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

       mObservableMap.put(EDIT_MODE,editable);

        mSpinnerStatus = v.findViewById(R.id.spinnerStatus);

        tableLayout=v.findViewById(R.id.tableLayout);





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



        if(!editable) {
            if (sizeList != null) {

                BuildTable(sizeList);

            }
        }else{

            if(!isMultipleEditable()){


                BuildTable(cutnote.getSizeList());



            }


        }



    }

    @Override
    protected void setMultipleEditableValues(View v) {


        addValueItem(new ValueViewItem(v.findViewById(R.id.spinnerStatus),CUTNOTE_STATUS,newValue,v.findViewById(R.id.undo_dealer_frame)));
        addValueItem(new ValueViewItem(v.findViewById(R.id.workerName),WORKER_ITEM,"",v.findViewById(R.id.undo_worker_frame)));


        values=mObservableMap;


    }

    @Override
    protected Object setEditableObject() {

        if(!isMultipleEditable()){

           cutnote= db.getCutNote(Utils.toIntExact(getmItemIds()[0]));


        }

        return null;

    }

    @Override
    protected void setSingleEditableValues(View v) {

        addValueItem(new ValueViewItem(v.findViewById(R.id.modelName),MODEL_NAME,cutnote.getModel(),null));
        addValueItem(new ValueViewItem(v.findViewById(R.id.spinnerStatus),CUTNOTE_STATUS,Utils.convertStatusToString(getContext(),cutnote.getStatus()),v.findViewById(R.id.undo_status_frame)));
        addValueItem(new ValueViewItem(v.findViewById(R.id.workerName),WORKER_ITEM,cutnote.getWorker(),v.findViewById(R.id.undo_worker_frame)));


        values=mObservableMap;




    }

    @Override
    protected String getTitle() {



         if(!editable) {

            return getString(R.string.cutNotes_addCutnote);

        }else {
             if(isMultipleEditable()){
                 return getString(R.string.action_edit) + " " + getString(R.string.cutNotes);

             }else {

                 return getString(R.string.action_edit) + " " + getString(R.string.cutNote);
             }

        }

    }

    @Override
    protected void setDefaultValues(View v) {




        addValueItem(new ValueViewItem(v.findViewById(R.id.spinnerStatus),CUTNOTE_STATUS,newValue,v.findViewById(R.id.undo_status_frame)));

        addValueItem(new ValueViewItem(v.findViewById(R.id.workerName),WORKER_ITEM,"",v.findViewById(R.id.undo_worker_frame)));




        values=mObservableMap;



    }

    @Override
    protected void saveData() {




            if(editable){


                if(!isMultipleEditable()) {
                    cutnote.setSizeList(getValuesOfTable(tableLayout));

                    if (ifValueChangeOfView(WORKER_ITEM))
                        cutnote.setWorker((String) getValueForIdentifier(WORKER_ITEM));

                    if (ifValueChangeOfView(CUTNOTE_STATUS))
                        cutnote.setStatus(Utils.convertStringToStatus(getContext(), (String) getValueForIdentifier(CUTNOTE_STATUS)));


                    db.updateCutNote(cutnote);


                    if (mSavedInterface != null) {

                        mSavedInterface.onSaved(cutnote, mItemPos[0], true);
                    }

                } else {

                    final Map<Integer, Long> notesMap = new HashMap();


                    List<Integer> sortedPos = new ArrayList<>();

                    for (int i = 0; i < mItemIds.length; i++) {

                        notesMap.put(mItemPos[i], mItemIds[i]);
                        sortedPos.add(mItemPos[i]);

                    }

                    Collections.sort(sortedPos, new Comparator<Integer>() {
                        @Override
                        public int compare(Integer integer, Integer t1) {

                            return Integer.compare(integer, t1) * -1;
                        }
                    });

                    ///////updateValues////

                    for (Integer pos : sortedPos) {

                        final CutNote mCutNote = db.getCutNote(notesMap.get(pos));



                        if (ifValueChangeOfView(WORKER_ITEM))
                            mCutNote.setWorker((String) getValueForIdentifier(WORKER_ITEM));

                        if (ifValueChangeOfView(CUTNOTE_STATUS))
                            mCutNote.setStatus(Utils.convertStringToStatus(getContext(), (String) getValueForIdentifier(CUTNOTE_STATUS)));


                        db.updateCutNote(mCutNote);


                        ///////////////////////

                        if (mSavedInterface != null) {


                            mSavedInterface.onSaved(mCutNote, pos, editable);

                        }
                    }
                }



            }else{


                final CutNote note=new CutNote(0);
                note.setSizeList(getValuesOfTable(tableLayout));

                if(ifValueChangeOfView(WORKER_ITEM))
                note.setWorker((String) getValueForIdentifier(WORKER_ITEM));

                if(ifValueChangeOfView(CUTNOTE_STATUS))
                    note.setStatus(Utils.convertStringToStatus(getContext(),(String)getValueForIdentifier(CUTNOTE_STATUS)));


                if(mSavedInterface!=null) {

                    mSavedInterface.onSaved(note, 0, false);
                }


            }

            db.closeDB();
            dismiss();



    }

    @Override
    protected View getBindableView() {

        return mConteView;
    }


    private void showWorker(View v){

        final String[] worker = getContext().getResources().getStringArray(R.array.default_dealer);

       final PopupMenu popup = new PopupMenu((getActivity()), v);

        for(String type: worker) {
            popup.getMenu().add(type);
        }


        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if(!((String)getValueViewMap().get(R.id.workerName).getDefaultValue()).equals(item.getTitle().toString())){
                    getValueViewMap().get(R.id.workerName).setNewValue(item.getTitle().toString());
                    return true;
                }
                return false;
            }
        });

            //menuClick=position;

            popup.show();



    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.workerPopUp:

                showWorker(view);

                break;


        }
    }


    @Override
    public void setSaveVisible() {


        if(start) {

            for(int i=0;i<getValueViewMap().keySet().size();i++){

                if ( (! getValueViewMap().get(getValueViewMap().keySet().toArray()[i]).isValueChanged())){

                    saveView.setVisibility(View.INVISIBLE);

                } else {

                    saveView.setVisibility(View.VISIBLE);
                    break;

                }


            }

        }


    }


    private boolean setSaveVisible(TableLayout tableLayout){


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



                        if (count > 0) {


                            return true;
                        } else {

                            return false;
                        }




            }
        }
        return false;

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




        for(Object obj:sizeValuesMap.keySet().toArray()){
            stringList.add((String)obj);

        }



        Collections.sort(stringList);



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


        for(String size:stringList) {


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
            tv4.setId(tv4.hashCode());
            tv4.setInputType(UCharacter.NumericType.DIGIT);
            tv4.setLayoutParams(new TableRow.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT,0.5f));
            tv4.setGravity(Gravity.CENTER);
            tv4.setBackground(getContext().getResources().getDrawable(R.drawable.edit_txt_background1));

            tv4.setTextColor(getContext().getResources().getColor(R.color.textColorPrimary));
            tv4.setPadding(8,8,8,8);

            tv4.setText(String.valueOf(sizeValuesMap.get(size)));
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


                    if(!editable.toString().trim().isEmpty())
                    getValueViewMap().get(tv4.hashCode()).setNewValue(editable.toString());


                }
            });
               addValueItem(new ValueViewItem(tv4,tv4.hashCode(),String.valueOf(sizeValuesMap.get(size)),null));
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

}

