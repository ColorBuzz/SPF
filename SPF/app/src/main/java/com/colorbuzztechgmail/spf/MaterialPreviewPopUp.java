package com.colorbuzztechgmail.spf;

import android.app.Dialog;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


/**
 * Created by PC01 on 18/06/2017.
 */
public class MaterialPreviewPopUp extends  DialogFragment implements Toolbar.OnMenuItemClickListener{




     private static final String IMAGE ="image";
     private static final String MATERIAL_NAME ="name";
     private Drawable drawable;


    public static MaterialPreviewPopUp newInstance(@NonNull byte[] image, @NonNull String name) {
        MaterialPreviewPopUp f = new MaterialPreviewPopUp();
        Bundle args = new Bundle();

        args.putByteArray(IMAGE,image);
        args.putString(MATERIAL_NAME,name);



        f.setArguments(args);


        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        LayoutInflater li = LayoutInflater.from(getContext());

        final View promptsView = li.inflate(R.layout.material_preview_popup, null);




        byte[] image=getArguments().getByteArray(IMAGE);



        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
         drawable = new BitmapDrawable(Resources.getSystem(), bitmap);

        ImageView imageView= (ImageView)promptsView.findViewById(R.id.materialpreview);
        imageView.setImageDrawable(drawable);





     //   ((FrameLayout)promptsView.findViewById(R.id.di)).addView(imageView);

      loadMenu(promptsView);

        return new AlertDialog.Builder(getActivity())

                .setView(promptsView)
                .create();
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



            if (d!=null) {
                d .getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                int width =ViewGroup.LayoutParams.WRAP_CONTENT;
                int height =ViewGroup.LayoutParams.WRAP_CONTENT;
                d.getWindow().setLayout(width, height);










            }





    }





    private void loadMenu(View v){

        Toolbar toolbar=(Toolbar)v.findViewById(R.id.pieceToolbar);

        toolbar.removeAllViews();


        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_selector);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        toolbar.setTitle(getArguments().getString(MATERIAL_NAME));
        toolbar.setOnMenuItemClickListener(this);


        toolbar.inflateMenu(R.menu.menu_piece);







    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId()) {


        }

        return false;
    }

}











