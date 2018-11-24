package com.colorbuzztechgmail.spf.popup;

import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.colorbuzztechgmail.spf.CutNoteGenerator;
import com.colorbuzztechgmail.spf.ListPopUpMenuItem;
import com.colorbuzztechgmail.spf.R;

public class CreationNotesSelector extends DialogFragment {

    public  CutNoteGenerator.GeneratorType mGeneratorType= CutNoteGenerator.GeneratorType.MULTI;
    private CutNoteGeneratorTypeListener cutNoteGeneratorTypeListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    public void setCutNoteGeneratorTypeListener(CutNoteGeneratorTypeListener cutNoteGeneratorTypeListener) {
        this.cutNoteGeneratorTypeListener = cutNoteGeneratorTypeListener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {






        final ListPopUpMenuItem[] items = {
                new ListPopUpMenuItem(getResources().getString(R.string.cutNotes_addCutnote), R.drawable.ic_insert_drive_file_black_24dp),
                new ListPopUpMenuItem(getResources().getString(R.string.cutNotes_addCutnoteList), R.drawable.ic_assignment_black_24dp),
                new ListPopUpMenuItem(getResources().getString(R.string.cutNotes_addAutomaticCutnoteList), R.drawable.ic_computer_black_24dp),

        };

        final ListAdapter adapter = new ArrayAdapter<ListPopUpMenuItem>(
               getActivity(),
                android.R.layout.select_dialog_item,
                android.R.id.text1,
                items) {
            public View getView(final int position, View convertView, ViewGroup parent) {
                //Use super class to create the View
                View v = super.getView(position, convertView, parent);
                TextView tv = (TextView) v.findViewById(android.R.id.text1);
                tv.setText(items[position].getTitle());

                //Put the image on the TextView
                tv.setCompoundDrawablesWithIntrinsicBounds(items[position].getImageRes(), 0, 0, 0);
                tv.setCompoundDrawablePadding(8);
                //Add margin between image and text (support various screen densities)
                int dp5 = (int) (5 * getResources().getDisplayMetrics().density + 0.5f);
                tv.setCompoundDrawablePadding(dp5);
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (position) {

                            case 0:
                                mGeneratorType = CutNoteGenerator.GeneratorType.SINGLE;


                                break;

                            case 1:
                                mGeneratorType = CutNoteGenerator.GeneratorType.MULTI;



                                break;

                            case 2:
                                mGeneratorType = CutNoteGenerator.GeneratorType.AUTOMATIC;



                                break;
                        }

                        if(cutNoteGeneratorTypeListener!=null)
                            cutNoteGeneratorTypeListener.showCutNoteEditor(mGeneratorType);
                    }



                });

                return v;
            }
        };


        ListView listView=new ListView(getActivity());
        listView.setBackgroundColor(getResources().getColor(R.color.iconsColor));

       listView.setAdapter(adapter);

        return listView;

    }

    public interface CutNoteGeneratorTypeListener{


        void showCutNoteEditor(CutNoteGenerator.GeneratorType generatorType);


    }
}
