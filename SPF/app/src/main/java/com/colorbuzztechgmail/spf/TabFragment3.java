package com.colorbuzztechgmail.spf;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by PC01 on 21/06/2017.
 */
public class TabFragment3 extends Fragment {

    public final static String MODEL_ID = "modelId";
    private static final String TAB = "TabFragment1";
    private ImageView interImageView;
    private TextView data;
    private ArrayList<Integer> digitList;
    private ArrayList<ImageView> containerList;
    private RecyclerView mRecycler;
    private FrameLayout expandFrame, infoContainer;
    private Integer podomCount = 0;
    public ModelDataBase db;
    int modelId;
    MaterialAsigmentoPopUp poupMaterialAsigment;
    private FloatingActionButton addMaterial;


    public static TabFragment3 newInstance(@NonNull int modelId) {
        TabFragment3 f = new TabFragment3();
        Bundle args = new Bundle();

        args.putInt(MODEL_ID, modelId);


        f.setArguments(args);


        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        db = new ModelDataBase(getContext());

        final View view = inflater.inflate(R.layout.model_profile_layout, container, false);


        return view;

    }
}