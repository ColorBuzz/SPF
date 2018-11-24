package com.colorbuzztechgmail.spf;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.icu.lang.UCharacter;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.style.TextAppearanceSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.colorbuzztechgmail.spf.databinding.CutNoteBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CutNoteCheckableListViewHolder extends ViewHolderMultiChoice{


    public TextView titleText;
    public ImageView image;
    private  ItemClickListener itemClickListener;


    public CutNoteCheckableListViewHolder(ViewDataBinding binding) {

        super(binding,0);

        setCheckableContainer(binding.getRoot().findViewById(R.id.checkLayout));
        image=(ImageView)binding.getRoot().findViewById(R.id.imageViewProfile);
        image.setOnClickListener(this);

    }

    @Override
    public void performBind(Object obj) {
        super.performBind(obj);
        setStatus((CutNote)obj);
       // BuildTable();

    }

    public static CutNoteCheckableListViewHolder create(ViewGroup parent) {


        ViewDataBinding viewDataBinding = CutNoteBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new CutNoteCheckableListViewHolder(viewDataBinding);

    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        if(itemClickListener!=null)
            this.itemClickListener.onClick(view, getAdapterPosition(), false);

    }

    @Override
    public boolean onLongClick(View v) {
        if(itemClickListener!=null)
            this.itemClickListener.onClick(v,getAdapterPosition(),true);
        return false;
    }


    public void setStatus(CutNote  cutNote ) {


        image.setImageDrawable(Utils.getImageAtStatus(getBinding().getRoot().getContext(),cutNote.getStatus()));


    }

    private void BuildTable() {

        Map<String,Integer> sizeList=((CutNoteBinding)getBinding()).getObj().getSizeList();

        TableLayout tableLayout;
        Context context=getBinding().getRoot().getContext();

       tableLayout= getBinding().getRoot().findViewById(R.id.tableLayout);
        if(tableLayout.getChildCount()>0)
            tableLayout.removeAllViews();




            TableRow row = new TableRow(context);

            row.setLayoutParams(new TableRow.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT,1f));
            row.setPadding(0,0,0,1);
            row.setDividerDrawable(context.getDrawable(R.drawable.horizontal_divider));
            row.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);

        TableRow row1 = new TableRow(context);
        row1.setLayoutParams(new TableRow.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT,1f));
        row1.setPadding(0,0,0,1);
        row1.setDividerDrawable(context.getDrawable(R.drawable.horizontal_divider));
        row1.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        final List<String>stringList= new ArrayList<>();

        for(Object obj:sizeList.keySet().toArray())
            stringList.add((String)obj);


         Collections.sort(stringList);

        for(String size: stringList) {
            final TextView tv1 = new TextView(context);
            final EditText tv2 = new EditText(context);


            if(sizeList.get(size)>0){
                tv1.setTextColor(context.getResources().getColor(R.color.iconsColor));
                tv1.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));

                tv2.setTextColor(context.getResources().getColor(R.color.textColorPrimary));

            }else{
                tv1.setTextColor(context.getResources().getColor(R.color.textColorTerciary));
                tv1.setBackgroundColor(context.getResources().getColor(R.color.colorLightPrimaryTransparent));


                tv2.setTextColor(context.getResources().getColor(R.color.textColorTerciary));

            }


            tv1.setLayoutParams(new TableRow.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT, 0.5f));
            tv1.setGravity(Gravity.CENTER);
            tv1.setPadding(8, 8, 8, 8);

            tv1.setTextSize(16);
            tv1.setText(size);

            row.addView(tv1);

            tv2.setInputType(UCharacter.NumericType.DIGIT);
            tv2.setLayoutParams(new TableRow.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT,0.5f));
            tv2.setGravity(Gravity.CENTER);
            tv2.setBackground(context.getResources().getDrawable(R.drawable.edit_txt_background1));


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



                   // setSaveVisible();


                }
            });
            tv2.setText(String.valueOf(sizeList.get(size)));
            row1.addView(tv2);
        }

        tableLayout.addView(row);
        tableLayout.addView(row1);

    }
}






