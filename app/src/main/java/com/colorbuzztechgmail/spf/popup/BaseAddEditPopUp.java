package com.colorbuzztechgmail.spf.popup;

import android.app.Dialog;
import android.content.Intent;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayMap;
import android.databinding.ObservableMap;
import android.databinding.ViewDataBinding;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.colorbuzztechgmail.spf.Category;
import com.colorbuzztechgmail.spf.ImagePicker;
import com.colorbuzztechgmail.spf.ListPopUpMenuItem;
import com.colorbuzztechgmail.spf.ListPopupWindowAdapter;
import com.colorbuzztechgmail.spf.ModelDataBase;
import com.colorbuzztechgmail.spf.PreviewModelInfo;
import com.colorbuzztechgmail.spf.R;
import com.colorbuzztechgmail.spf.TextViewUndoRedo;
import com.colorbuzztechgmail.spf.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.colorbuzztechgmail.spf.BR.fragment;
import static com.colorbuzztechgmail.spf.BR.obj;

import static com.colorbuzztechgmail.spf.MainActivity.REQUEST_CODE_MATERIAL_IMAGE_GALLERY;


public abstract class BaseAddEditPopUp extends DialogFragment {

    public static String DATABASE_ITEM_ID ="mItemIds";
    public static String ADAPTER_ITEM_POSITION ="item_position";
    public static String EDITABLE_ST ="editable";


    public long[] mItemIds;

    public int[] mItemPos;
    public  View saveView;
    public Object mObject;
    Map<Integer,Long> mPositionIdMap;
    private Toolbar toolbar;


    public Utils.onSavedInterface mSavedInterface;
    public boolean editable=false;

    private ListPopupWindow popupWindow;
    
    private SparseBooleanArray sparseArray;


    public boolean start=false;

    public ModelDataBase db;

    private ViewDataBinding mBinding;

    private Map<Integer,ValueViewItem> mValueViewMap;
    public ObservableMap<Integer, Object> mObservableMap;
    public Map<Integer,Integer>  mViewIdMap;

    public String newValue,emptyValue,notEditable;


    public boolean resizeImage;
    public Map<Integer, ValueViewItem> getValueViewMap() {
        return mValueViewMap;
    }

    public ViewDataBinding getBinding() {
        return mBinding;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


            editable = getArguments().getBoolean(EDITABLE_ST);

            db = new ModelDataBase(getActivity().getBaseContext());

            sparseArray = new SparseBooleanArray();
            mValueViewMap = new HashMap<>();
            mViewIdMap = new HashMap<>();
            mObservableMap = new ObservableArrayMap<>();
            this.mItemPos = getArguments().getIntArray(ADAPTER_ITEM_POSITION);
            this.mItemIds = getArguments().getLongArray(DATABASE_ITEM_ID);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        final View v = setViewLayout(inflater);

        emptyValue = getActivity().getBaseContext().getString(R.string.importNoAsigned_Cat);
        newValue = getString(R.string.dialog_selection_value);
        notEditable = getString(R.string.dialog_not_editable_value);


                if (editable) {

                    if (!isMultipleEditable())
                        mObject = setEditableObject();


                }

                setupValues(v);



        bindView(getBindableView());
        return (v);


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        if(!editable)
        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public void onDestroyView() {
        if(!editable)
            if(toolbar!=null)
            toolbar.removeView(saveView);


        super.onDestroyView();
    }

    public void setupValues(View v){


        if(editable){
            final TextView titleText=(TextView) v.findViewById(R.id.titleText);

            titleText.setText(getTitle());

            saveView= v.findViewById(R.id.saveFrame);
            v.findViewById(R.id.closeFrame).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getDialog().cancel();

                }
            });



            if(isMultipleEditable()){


               setMultipleEditableValues(v);


            }else{


                setSingleEditableValues(v);

            }


        }else{
            setDefaultValues(v);
           v.findViewById(R.id.title).setVisibility(View.GONE);
            toolbar = (Toolbar) getActivity().findViewById(R.id.appbar);

            saveView = getLayoutInflater().inflate(R.layout.button_layout, null);

            ((TextView) saveView.findViewById(R.id.closeTxt)).setText(getResources().getString(R.string.dialog_save));

            toolbar.addView(saveView, new Toolbar.LayoutParams(Gravity.RIGHT));



        }

        initializeViews(v);

        saveView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });
        saveView.setVisibility(View.INVISIBLE);
        
    }

    @Override
    public void onStart() {
        super.onStart();

        start=true;

        Dialog d=getDialog();
        if (d != null) {

            int width = getDialog().getWindow().getWindowManager().getDefaultDisplay().getWidth();
            int height =ViewGroup.LayoutParams.MATCH_PARENT;
            float factor=width/(16f/9f);

            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            //imageContainer.setLayoutParams(new FrameLayout.LayoutParams(width,Utils.pxTodp ((int) factor,getActivity().getBaseContext())));

            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }


    }

    public void setSavedInterface(Utils.onSavedInterface mSavedInterface) {
        this.mSavedInterface = mSavedInterface;
    }

    private void bindView(View mContentView ){

        if( mBinding==null)
            mBinding = DataBindingUtil.bind(mContentView);


        mBinding.setVariable(fragment,this);
        mBinding.setVariable(obj,mObservableMap);
        mBinding.notifyChange();
        mBinding.executePendingBindings();




    }

    private class GenericTextWatcher implements TextWatcher {


        private View view;
        

        private GenericTextWatcher( View view) {
            this.view = view;

            }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            String text = editable.toString();
            text = text.trim();
            if (!text.isEmpty()) {


                    mValueViewMap.get(view.getId()).setNewValue(text);
                    sparseArray.put(view.getId(), true);


            }else{
                sparseArray.put(view.getId(),false);

                getView().findViewById(R.id.saveFrame).setVisibility(View.INVISIBLE);

            }

        }
    }

    public void addValueItem(ValueViewItem valueViewItem){

        mValueViewMap.put(valueViewItem.getView().getId(),valueViewItem);
        mViewIdMap.put(valueViewItem.getIdentifier(),valueViewItem.getView().getId());

        if( valueViewItem.getView() instanceof TextView){

            ((TextView)(valueViewItem.getView())).addTextChangedListener(new GenericTextWatcher(valueViewItem.getView()));
            sparseArray.append(valueViewItem.getView().getId(),false);


        }

    }

    public long[] getmItemIds() {
        return mItemIds;
    }

    public void setSaveVisible(){

        if(start) {

            for(int i=0;i<mValueViewMap.keySet().size();i++){

                if ( ! mValueViewMap.get(mValueViewMap.keySet().toArray()[i]).isValueChanged()) {

                    saveView.setVisibility(View.INVISIBLE);

                } else {

                    saveView.setVisibility(View.VISIBLE);
                    break;

                }


            }

        }
    }

    public  void imagePicker( int RequestCode){

        Intent chooseImageIntent = ImagePicker.getPickImageIntent(getContext());
        startActivityForResult(chooseImageIntent,RequestCode);

    }

    public boolean isMultipleEditable(){


       if(mItemIds!=null) {
           if (mItemIds.length > 1) {
               return true;
           }
       }
        return false;
    }


    public float getPanoramicHeight() {
        int width=0;
        int height=0;
        float factor=0;

        width = getResources().getDisplayMetrics().widthPixels;
        height = ViewGroup.LayoutParams.MATCH_PARENT;
        factor = width / (16f / 9f);
        return factor;
    }

    public float getScreenWidth() {


        return  getResources().getDisplayMetrics().widthPixels;


    }

    @BindingAdapter("android:visibility")
    public static void setVisibility(View view, Boolean value) {
        view.setVisibility(value ? View.GONE : View.VISIBLE);
    }

    @BindingAdapter("android:enabled")
    public static void setEnabled(View view, Boolean value) {
        view.setEnabled(value);
    }

    @BindingAdapter("android:selectedItemPosition")
    public static void setSelection(Spinner spinner, String selectItem) {


        if(spinner.isEnabled()){

            final SpinnerAdapter adapter=spinner.getAdapter();

            if(adapter!=null){

                for(int i=0;i<adapter.getCount();i++) {

                    if (((String) adapter.getItem(i)).equals(selectItem)) {

                    spinner.setSelection(i);
                    break;

                     }

                }

            }


        }

    }

    @BindingAdapter("android:layout_width")
    public static void setLayoutWidth(View view,float width) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();


            layoutParams.width =(int)width;

        view.setLayoutParams(layoutParams);
    }
    @BindingAdapter("android:layout_height")
    public static void setLayoutHeight(View view,float height) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();

        layoutParams.height =(int)height;

        view.setLayoutParams(layoutParams);


    }


    protected abstract void initializeViews(View v);
    protected abstract View setViewLayout(LayoutInflater inflater);
    protected abstract void setMultipleEditableValues(View view);
    protected abstract void setSingleEditableValues(View view);
    protected abstract void setDefaultValues(View view);
    protected abstract void saveData();
    protected abstract View getBindableView();
    protected abstract Object setEditableObject();
    protected abstract String getTitle();

    public class ValueViewItem{
        
       private View mView;
       private int mIdentifier;
       private Object mNewValue;
       private Object mDefaultValue;
       private View mUndoRedoView;
        
        public ValueViewItem (View mView, final int mIdentifier, final Object mDefaultValue, View mUndoRedoView ){
            
            setView(mView);
            setDefaultValue(mDefaultValue);
            setNewValue(mDefaultValue);
            setIdentifier(mIdentifier);
            this.mUndoRedoView=mUndoRedoView;

            if(mUndoRedoView!=null){
                mUndoRedoView.setVisibility(View.GONE);

                mUndoRedoView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        resetChange();
                    }
                });


            }

            mObservableMap.put(mIdentifier,mDefaultValue);
            
        }

        public View getView() {
            return mView;
        }

        public void setView(View mView) {
            this.mView = mView;
        }

        public int getIdentifier() {
            return mIdentifier;
        }

        public void setIdentifier(int identifier) {
            this.mIdentifier = identifier;
        }

        public Object getNewValue() {
            return mNewValue;

        }

        public void setNewValue(Object mNewValue) {

            this.mNewValue = mNewValue;
            mObservableMap.put(mIdentifier,mNewValue);

            updateUndoRedoView();
            setSaveVisible();
        }

        public void resetChange(){


            setNewValue(mDefaultValue);
            updateUndoRedoView();
            setSaveVisible();

        }

        public Object getDefaultValue() {
            return mDefaultValue;
        }

        public void setDefaultValue(Object mDefaultValue) {
            this.mDefaultValue = mDefaultValue;
            mObservableMap.put(mIdentifier,mDefaultValue);


        }
        
        public boolean isValueChanged(){



            if(mDefaultValue instanceof String){

              return  !((String)mDefaultValue).equals((String) mNewValue);

            }



            return mDefaultValue!=mNewValue;
            
            
             
        }

        private void updateUndoRedoView(){

            if(mUndoRedoView!=null){



                if(isValueChanged()){

                    mUndoRedoView.setVisibility(View.VISIBLE);


                }else{
                    mUndoRedoView.setVisibility(View.GONE);

                }
            }



        }
    }

    public Object getValueForIdentifier(int identifier){

        return getValueViewMap().get(mViewIdMap.get(identifier)).getNewValue();

    }

    public boolean ifValueChangeOfView(int identifier){

        return getValueViewMap().get(mViewIdMap.get(identifier)).isValueChanged();

    }

}
