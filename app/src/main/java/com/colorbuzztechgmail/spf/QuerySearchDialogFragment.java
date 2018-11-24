package com.colorbuzztechgmail.spf;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

public class QuerySearchDialogFragment extends DialogFragment {


    ItemActionListener listener;
    private static final String FRAGMENT_TYPE="fragment_type";

    public static QuerySearchDialogFragment newInstance(@NonNull BaseFragment.FragmentType type, ItemActionListener listener) {
        QuerySearchDialogFragment fr = new QuerySearchDialogFragment();

        Bundle args=new Bundle();

        args.putString(FRAGMENT_TYPE,type.name());
        fr.setArguments(args);

        fr.setListener(listener);


        return fr;
    }


    public void setListener(ItemActionListener listener) {
        this.listener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //read the int from args

        View view = inflater.inflate(R.layout.base_fragment, null);

        QuerySearchFragment searchFragment=QuerySearchFragment.newInstance(BaseFragment.FragmentType.valueOf(getArguments().getString(FRAGMENT_TYPE)).name(), null,listener);
        getChildFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer,searchFragment)
                .setTransition(FragmentTransaction.TRANSIT_ENTER_MASK)
                .addToBackStack(null)
                .commit();

        //here read the different parts of your layout i.e :
        //tv = (TextView) view.findViewById(R.id.yourTextView);
        //tv.setText("some text")

        return view;
    }


}
