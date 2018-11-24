package com.colorbuzztechgmail.spf.popup;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.colorbuzztechgmail.spf.R;

public class waitPoup extends DialogFragment
{

    private static final String TEXT_DATA="text";

    public static waitPoup newInstance(@NonNull String text ) {
        waitPoup f = new waitPoup();
        Bundle args = new Bundle();

        args.putString(TEXT_DATA,text);



        f.setArguments(args);


        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {



        return super.onCreateView(inflater, container, savedInstanceState);


    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog d = getDialog();
        d.setCanceledOnTouchOutside(false);



        if (d!=null ) {


            d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {

        LayoutInflater li=LayoutInflater.from(getContext());
        View v=li.inflate(R.layout.waitpoup,null);

        final TextView text=(TextView) v.findViewById(R.id.dialogText);

        text.setText(getArguments().getString(TEXT_DATA));

        AlertDialog.Builder builder =
                new AlertDialog.Builder( getContext());

        builder

                .setView(v);


                //.setMessage("Scanning Devices...");




        return builder.create();
    }
}