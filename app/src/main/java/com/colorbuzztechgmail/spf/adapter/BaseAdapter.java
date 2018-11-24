package com.colorbuzztechgmail.spf.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.ImageView;

import com.colorbuzztechgmail.spf.Accessories;
import com.colorbuzztechgmail.spf.BR;
import com.colorbuzztechgmail.spf.Category;
import com.colorbuzztechgmail.spf.CheckableLinearLayout;
import com.colorbuzztechgmail.spf.CustomHeader;
import com.colorbuzztechgmail.spf.CustomHeaderViewHolder;
import com.colorbuzztechgmail.spf.CustomMaterial;
import com.colorbuzztechgmail.spf.CutNote;
import com.colorbuzztechgmail.spf.Dealership;
import com.colorbuzztechgmail.spf.HeaderItemDecoration;
import com.colorbuzztechgmail.spf.ItemActionListener;
import com.colorbuzztechgmail.spf.ItemClickListener;
import com.colorbuzztechgmail.spf.ModelCheckableListViewHolder;
import com.colorbuzztechgmail.spf.MultiChoiceHelper;
import com.colorbuzztechgmail.spf.Piece;
import com.colorbuzztechgmail.spf.PreviewModelInfo;
import com.colorbuzztechgmail.spf.ProgressItem;
import com.colorbuzztechgmail.spf.ProgressViewHolder;
import com.colorbuzztechgmail.spf.R;
import com.colorbuzztechgmail.spf.Utils;
import com.colorbuzztechgmail.spf.ViewHolderMultiChoice;
import com.colorbuzztechgmail.spf.dataset.Dataset;

import java.util.ArrayList;

public abstract class BaseAdapter extends RecyclerView.Adapter<ViewHolderMultiChoice> implements MultiChoiceHelper.MultiChoiceModeListener ,HeaderItemDecoration.StickyHeaderInterface {

    public static final int TITLE = 0;
    public static final int MODEL_ITEM = 1;
    public static final int MATERIAL_ITEM = 2;
    public static final int DEALER_ITEM = 3;
    public static final int COMPLEMENT_ITEM  = 4;
    public static final int CUTNOTELIST_ITEM = 5;
    public static final int CATEGORY= 6;
    public static final int PIECE_ITEM= 7;
    public static final int PROGRESS_ITEM=8;


    public Dataset dataset;
    public MultiChoiceHelper multiChoiceHelper;
    public AppCompatActivity ma;
    private int itemClick;
    public boolean selectAll = false;
    public ItemActionListener actionListener;
    public boolean enableMultiChoice;
    private Utils.ViewMode mViewMode = Utils.ViewMode.LIST;





    public void setActionListener(ItemActionListener actionListener) {

        this.actionListener = actionListener;
    }

    public BaseAdapter(@Nullable ItemActionListener itemClickListener, AppCompatActivity ma, boolean enableMultiChoice) {

        setActionListener(itemClickListener);
        this.ma = ma;


        multiChoiceHelper = new MultiChoiceHelper(ma, this);
        multiChoiceHelper.setMultiChoiceModeListener(this);
        this.enableMultiChoice = enableMultiChoice;


    }

    public Utils.ViewMode getViewMode() {
        return mViewMode;
    }

    public void setViewMode(Utils.ViewMode mViewMode) {
        this.mViewMode = mViewMode;
    }

    @Override
    public int getItemViewType(int position) {

        if (dataset.getObject(position) instanceof CustomHeader) {

            return TITLE;
        }else if(dataset.getObject(position) instanceof Category){

            return CATEGORY;
        }else if(dataset.getObject(position) instanceof PreviewModelInfo){

            return MODEL_ITEM;
        }else if(dataset.getObject(position) instanceof CustomMaterial){

            return MATERIAL_ITEM;
        }else if(dataset.getObject(position) instanceof Dealership){

            return DEALER_ITEM;

        }else if(dataset.getObject(position) instanceof CutNote){

            return CUTNOTELIST_ITEM;
        }else if(dataset.getObject(position) instanceof Accessories){

            return COMPLEMENT_ITEM;
        }else if(dataset.getObject(position) instanceof Piece){

            return PIECE_ITEM;
        }
        else if(dataset.getObject(position) instanceof Piece){

            return PIECE_ITEM;
        }else if (dataset.getObject(position) instanceof ProgressItem){


            return PROGRESS_ITEM;
        }


        return 0;
    }

    @Override
    public ViewHolderMultiChoice onCreateViewHolder(ViewGroup parent, int viewType) {


        switch (viewType){

            case TITLE:

                return  CustomHeaderViewHolder.create(parent);


            case PROGRESS_ITEM:

                return ProgressViewHolder.create(parent);


                default:

                    return getViewHolder(parent,viewType);
        }


    }


    protected abstract ViewHolderMultiChoice getViewHolder(ViewGroup parent,int viewType);




    public void toggleItem(View v, int position) {

        if (isHeader(position)) {
            // final int pos= dataset.getHeaderPosition(((CustomHeader)dataset.getObject(position)).getTitle());

            //Utils.toast(ma.getApplicationContext(),String.valueOf(pos) + " " + ((CustomHeader)dataset.getCustomMaterial(position)).getTitle());
            boolean value = !isHeaderCheck(position);
            SetHeaderCheck(position, value, false);
            expandTitle(value, position);
            updateCheckedHeader(v, position, false);
            //dataset.recyclerView.scrollToPosition(position);

        }

    }

    private void expandTitle(boolean expand, int headerPos){

          CustomHeader header=((CustomHeader)dataset.getObject(headerPos));
          ArrayList<Integer> deleteBuffer=new ArrayList<>();
          ArrayList<Integer>deleteIdBuffer=new ArrayList<>();

          if (!expand) {

              for (int i = dataset.size() - 1; i >= headerPos; i--) {

                  Object obj=dataset.getObject(i);

                  if (!isHeader(i)) {

                      if (dataset.getTitleForItem(obj).equals(header.getTitle())) {

                          deleteIdBuffer.add(dataset.getItemId(i));

                          header.addObject(dataset.getObject(i));

                          deleteBuffer.add(i);

                      }

                  }

              }

              int size = header.getBuffer().size();

              if(deleteBuffer.size()>0){
                  if(multiChoiceHelper.selectionMode){
                      multiChoiceHelper.resetCheckPosition(deleteIdBuffer);

                  }
                  dataset.remove(deleteBuffer);
              }





          } else {
/////

              final int size = header.getBuffer().size();

              if(size>0){

                  dataset.addAll(header.getBuffer());
                  header.removeAll();



              }




          }


      };

    public boolean isHeaderCheck(int position) {
        if (isHeader(position)) {

            return ((CustomHeader) dataset.getObject(position)).isExpanded();
        }
        return false;
    }

    private void SetHeaderCheck(int position, boolean value, boolean notify) {

        if (isHeader(position)) {

            ((CustomHeader) dataset.getObject(position)).setExpanded(value);

            if (notify) {
                notifyItemChanged(position);


            }

        }


    }

    public void  updateCheckedHeader(View container, int position, boolean animated){
        final boolean isChecked = isHeaderCheck(position);
        Drawable drwEnd=null;

        if ( container instanceof Checkable) {

            ((CheckableLinearLayout)container).setChecked(isChecked);
             container.findViewById(R.id.text).setActivated(isChecked);
           // container.findViewById(R.id.image).setActivated(isChecked);


           if(isChecked){

                if(animated){
                    drwEnd=ma.getResources().getDrawable(R.drawable.ic_keyboard_arrow_down_primary_dark_animatable_clockwise);

                }else{

                    drwEnd=ma.getResources().getDrawable(R.drawable.ic_keyboard_arrow_up_primary_dark_24dp);
                }

            }else{

                if(animated){

                    drwEnd= ma.getResources().getDrawable(R.drawable.ic_keyboard_arrow_down_primary_dark_animatable_counter_clockwise);

                }else{
                    drwEnd=ma.getResources().getDrawable(R.drawable.ic_keyboard_arrow_down_primary_dark_24dp);


                }

            }

            ((ImageView)container.findViewById(R.id.imageView1)).setImageDrawable(drwEnd);
            if(animated){
                    (((AnimatedVectorDrawable)((ImageView)container.findViewById(R.id.imageView1)).getDrawable())).start();

            }


        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ((CheckableLinearLayout)container).setActivated(isChecked);
        }
    };

    @Override
    public int getItemCount() {
        return dataset == null ? 0 : dataset.size();
    }

    public void dataset(Dataset dataset) {
        this.dataset = dataset;
    }

    @Override
    public void onBindViewHolder(final ViewHolderMultiChoice holder, final int position) {

        if (holder instanceof CustomHeaderViewHolder) {
            final CustomHeader title = (CustomHeader) dataset.getObject(position);
            ((CustomHeaderViewHolder) holder).performBindTitle(title);
            updateCheckedHeader(((CustomHeaderViewHolder) holder).getCheckableContainer(), position, false);

            ((CustomHeaderViewHolder) holder).setItemClickListener(new ItemClickListener() {
                @Override
                public void onClick(View v, int position, boolean isLongClick) {
                    if (isLongClick) {

                    } else {

                        switch (v.getId()) {


                            case R.id.titleCheckLayout:

                                if (position != RecyclerView.NO_POSITION) {
                                    toggleItem(v, position);
                                }
                                break;

                        }

                    }

                }
            });

        }else if ( holder instanceof ProgressViewHolder){

            holder.performBind(dataset.getObject(position));

        }else{
            bindItem(holder,position);
        }
    }

    protected abstract void bindItem(ViewHolderMultiChoice holder,int position);

    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
        if (multiChoiceHelper.getCheckedItemCount() > 0) {
            if (multiChoiceHelper.getCheckedItemCount() == 1) {
                mode.setTitle(String.valueOf(multiChoiceHelper.getCheckedItemCount()) + " " + ma.getResources().getString(R.string.checkedSinleFileText));

            } else {
                mode.setTitle(String.valueOf(multiChoiceHelper.getCheckedItemCount()) + " " + ma.getResources().getString(R.string.checkedFileText));
            }
        }
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {

        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.multichoice_menu, menu);
        MenuItem shareItem = menu.findItem(R.id.action_share);
        selectAll=false;

        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {




        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {


        switch (item.getItemId()) {
            case R.id.action_selectAll:

                selectAll = !selectAll;
                if (selectAll) {

                   item.setIcon(ma.getResources().getDrawable(R.drawable.ic_select_all_white_24dp));
                   item.setTitle(ma.getResources().getString(R.string.action_cancelSelectAll));

                } else {

                    item.setIcon(ma.getResources().getDrawable(R.drawable.ic_unselect_all_white_24dp));
                    item.setTitle(ma.getResources().getString(R.string.action_selectAll));

                }
                selectAll(selectAll);
                //deleteSelectedItems();
                // Action picked, so close the CAB
                return true;
            case R.id.action_removeModel:

                if (actionListener != null) {

                    actionListener.toRemove(multiChoiceHelper.getCheckedItemPositions());

                }

                return true;
            case R.id.action_edit:


                   long[] ids  = new long[multiChoiceHelper.getCheckedItemCount()];
                   int count=0;
                    for(int i=0;i<multiChoiceHelper.getCheckedItemPositions().size();i++){

                        if(multiChoiceHelper.getCheckedItemPositions().valueAt(i)) {
                            ids[count++] = (multiChoiceHelper.getCheckedItemPositions().keyAt(i));

                        }

                    }


                    if (actionListener != null) {
                        actionListener.toEdit(ids);
                    }

                return true;
            default:
                return false;
        }

    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {


    }

    public int getHeaderPositionForItem(int itemPosition) {
        int headerPosition = 0;
        do {
            if (this.isHeader(itemPosition)) {
                headerPosition = itemPosition;
                break;
            }
            itemPosition -= 1;
        } while (itemPosition >= 0);
        return headerPosition;
    }

    @Override
    public int getHeaderLayout(int headerPosition) {


        return R.layout.custom_header;
    }

    @Override
    public void bindHeaderData(View header, int headerPosition) {

        if (isHeader(headerPosition)) {


           final ViewDataBinding dataBinding = DataBindingUtil.bind(header);

          dataBinding.setVariable(BR.obj,dataset.getObject(headerPosition));

           dataBinding.notifyChange();
           dataBinding.executePendingBindings();

          //  SetHeaderCheck(headerPosition, isHeaderCheck(headerPosition), false);*/
            updateCheckedHeader(header.findViewById(R.id.titleCheckLayout), headerPosition, false);
        }
    }

    @Override
    public boolean isHeader(int itemPosition) {
        return (dataset.getObject(itemPosition) instanceof CustomHeader);
    }

    @Override
    public void ToggleTitle(View header, String title) {

        toggleItem(header, dataset.getHeaderPosition(title));


    }

    protected abstract void selectAll(boolean enabled);

    public void resizeView(View view){

        DisplayMetrics displaymetrics = new DisplayMetrics();
        (ma).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);


        //if you need three fix imageview in width
        int devicewidth = displaymetrics.widthPixels / 2;

        float factor=devicewidth/(16f/9f);

        //if you need 4-5-6 anything fix imageview in height
        int deviceheight = (int)factor;

       //view.getLayoutParams().width = devicewidth;

        //if you need same height as width you can set devicewidth in holder.image_view.getLayoutParams().height
        view.getLayoutParams().height = deviceheight;

    }

    public void setCollapsedAllTitle(boolean value){


        for(int i =dataset.sparseHeader.size()-1;i>=0;i--){

            SetHeaderCheck(dataset.sparseHeader.keyAt(i),!value,true);
            expandTitle(!value,dataset.sparseHeader.keyAt(i));

        }




    }


}