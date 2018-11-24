package com.colorbuzztechgmail.spf;


import android.graphics.drawable.AnimatedVectorDrawable;
import android.opengl.Visibility;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.TextView;

import com.colorbuzztechgmail.spf.Utils.ViewMode;

import java.util.ArrayList;

public class MaterialAdapter extends RecyclerView.Adapter<ViewHolderMultiChoice>implements MultiChoiceHelper.MultiChoiceModeListener ,HeaderItemDecoration.StickyHeaderInterface{
    public static final int TITLE=0;
    public static final int ITEM=1;
    private MaterialDataset dataset;
    public MultiChoiceHelper multiChoiceHelper;
    private AppCompatActivity ma;
    private int itemClick;
    private boolean selectAll=false;
    private ItemActionListener actionListener;
    private boolean enableMultiChoice;
    private ViewMode mViewMode=ViewMode.LIST;



    public void setActionListener(ItemActionListener actionListener){

        this.actionListener=actionListener;
    }

    public MaterialAdapter(@Nullable ItemActionListener itemClickListener, AppCompatActivity ma, boolean enableMultiChoice) {

        setActionListener(itemClickListener);
        this.ma=ma;


        multiChoiceHelper= new MultiChoiceHelper( ma,this);
        multiChoiceHelper.setMultiChoiceModeListener(this);
        this.enableMultiChoice=enableMultiChoice;



    }

    public ViewMode getViewMode() {
        return mViewMode;
    }

    public void setViewMode(ViewMode mViewMode) {
        this.mViewMode = mViewMode;
    }

    @Override
    public int getItemViewType(int position) {

        if (dataset.getObject(position) instanceof CustomHeader) {

            return TITLE;
        }


        return ITEM;
    }

    @Override
    public ViewHolderMultiChoice onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        switch (viewType){


            case TITLE:

                return CustomHeaderViewHolder.create(parent);


            case ITEM:


                return MaterialCheckableListViewHolder.create(parent,getViewMode());


        }

        return null;

    }


    public void toggleItem(View v ,int position){

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

    private void expandTitle(boolean expand, int headerPos) {



        CustomHeader header=((CustomHeader)dataset.getObject(headerPos));
        ArrayList<Integer> deleteBuffer=new ArrayList<>();
        ArrayList<Integer>deleteIdBuffer=new ArrayList<>();

        if (!expand) {

            for (int i = dataset.size() - 1; i >= headerPos; i--) {

                if (!isHeader(i)) {


                    switch (dataset.getSortType()) {

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

    }

    public boolean isHeaderCheck(int position){
        if(isHeader(position)) {

            return  ((CustomHeader) dataset.getObject(position)).isExpanded();
        }
        return false;
    }

    private void SetHeaderCheck(int position,boolean value,boolean notify){

        if(isHeader(position)){

            ((CustomHeader)  dataset.getObject(position)).setExpanded(value);

            if(notify){
                notifyItemChanged(position);


            }

        }



    }

    private void updateCheckedHeader(View container,int position,boolean animated){
        final boolean isChecked = isHeaderCheck(position);
        if ( container instanceof Checkable) {

            ((CheckableLinearLayout)container).setChecked(isChecked);
            if(isChecked){
                ((TextView)container.findViewById(R.id.text)).setTextColor(ma.getResources().getColor(R.color.colorPrimaryDark));
                if(animated){
                    ((TextView)container.findViewById(R.id.text)).setCompoundDrawablesWithIntrinsicBounds(null,null,
                            ma.getResources().getDrawable(R.drawable.ic_keyboard_arrow_down_white_animatable_clockwise),null);
                }else{


                    ((TextView)container.findViewById(R.id.text)).setCompoundDrawablesWithIntrinsicBounds(null,null,
                            ma.getResources().getDrawable(R.drawable.ic_keyboard_arrow_up_white_24dp),null);
                }

            }else{
                ((TextView)container.findViewById(R.id.text)).setTextColor(ma.getResources().getColor(R.color.iconsColor));

                if(animated){

                    ((TextView)container.findViewById(R.id.text)).setCompoundDrawablesWithIntrinsicBounds(null,null,
                            ma.getResources().getDrawable(R.drawable.ic_keyboard_arrow_down_white_animatable_counter_clockwise),null);

                }else{

                    ((TextView)container.findViewById(R.id.text)).setCompoundDrawablesWithIntrinsicBounds(null,null,
                            ma.getResources().getDrawable(R.drawable.ic_keyboard_arrow_down_white_24dp),null);
                }

            }

            if(animated){
                (((AnimatedVectorDrawable)(((TextView)container.findViewById(R.id.text))).getCompoundDrawables()[2])).start();

            }


        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ((CheckableLinearLayout)container).setActivated(isChecked);
        }

    }




    @Override
    public int getItemCount() {
        return dataset == null ? 0 : dataset.size();
    }

    public void dataset(MaterialDataset dataset) {
        this.dataset = dataset;
    }

    @Override
    public void onBindViewHolder(final ViewHolderMultiChoice holder, final int position) {

        if(holder instanceof CustomHeaderViewHolder){
            final CustomHeader title =(CustomHeader) dataset.getObject(position);
            ((CustomHeaderViewHolder) holder).performBindTitle(title);
            updateCheckedHeader(((CustomHeaderViewHolder) holder).getCheckableContainer(),position,false);

            ((CustomHeaderViewHolder) holder).setItemClickListener(new ItemClickListener() {
                @Override
                public void onClick(View v, int position, boolean isLongClick) {
                    if(isLongClick){

                    }else{

                        switch (v.getId()){


                            case R.id.titleCheckLayout:

                                if (position != RecyclerView.NO_POSITION) {
                                    toggleItem(v, position);
                                }
                                break;

                        }

                    }

                }
            });






        }else{

            final Material material = dataset.getMaterial(position);
            ((MaterialCheckableListViewHolder)holder).performBind(material);


            if(!(material.getCustomMaterialId().equals("0"))){

                ((MaterialCheckableListViewHolder) holder).toolbar.getMenu()  .findItem(R.id.action_customMaterial).setVisible(false);
                ((MaterialCheckableListViewHolder) holder).toolbar.getMenu()  .findItem(R.id.action_deleteCustomMaterial).setVisible(true);

            }else{
                ((MaterialCheckableListViewHolder) holder).toolbar.getMenu()  .findItem(R.id.action_customMaterial).setVisible(true);
                ((MaterialCheckableListViewHolder) holder).toolbar.getMenu()  .findItem(R.id.action_deleteCustomMaterial).setVisible(false);
            }


            ((MaterialCheckableListViewHolder) holder).toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                      switch (item.getItemId()){


                          case R.id.action_preview:

                              Utils.showPreview(ma,material);

                              return true;

                          case R.id.action_customMaterial:

                              Utils.showMaterialAssigment(ma, dataset.getMaterial(position), new Utils.onSavedInterface() {
                                                     @Override
                                                     public void onSaved(Object obj, int index, boolean isEditable) {

                                                         if(isEditable){


                                                             dataset.replaceItem(obj,position);

                                                         }

                                                     }
                                                 });

                              return true;

                          case R.id.action_deleteCustomMaterial:

                           Utils.deleteAssignedMaterial(ma,material);
                           dataset.replaceItem(material,position);

                              return true;

                          case R.id.action_pieces:

                              Utils.showPieceList(ma,material);


                              return true;



                      }

                    return false;
                }
            });


            if(enableMultiChoice) {

                holder.bind(multiChoiceHelper, material.getId());

                holder.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {

                        itemClick = position;

                        if (!holder.isMultiChoiceActive()) {


                            if (position != RecyclerView.NO_POSITION) {
                                if(((MaterialCheckableListViewHolder) holder).flipView.isBackSide())
                                    ((MaterialCheckableListViewHolder) holder).flipView.flipTheView();

                                multiChoiceHelper.setItemChecked(material.getId(), position, true, true);
                                holder.updateCheckedState(material.getId());

                                multiChoiceHelper.EnableSelectionMode(true);

                            }
                        }else{


                            multiChoiceHelper.toggleItemChecked(material.getId(),position, true);



                        }

                        return false;
                    }
                });

            }
            holder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    itemClick=position;

                    if(holder.isMultiChoiceActive()){

                        if(!multiChoiceHelper.isItemChecked(material.getId())){

                            if(((MaterialCheckableListViewHolder) holder).flipView.isBackSide())
                                ((MaterialCheckableListViewHolder) holder).flipView.flipTheView();


                        }
                        multiChoiceHelper.toggleItemChecked(material.getId(),position, true);


                    }else{

                        ((MaterialCheckableListViewHolder) holder).flipView.flipTheView();

                    }

                }
            });


        }
    }

    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
        if(multiChoiceHelper.getCheckedItemCount()>0) {
            if(multiChoiceHelper.getCheckedItemCount()==1){
                mode.setTitle(String.valueOf(multiChoiceHelper.getCheckedItemCount()) + " " + ma.getResources().getString(R.string.checkedSinleFileText));

            }else{
                mode.setTitle(String.valueOf(multiChoiceHelper.getCheckedItemCount() )+ " " + ma.getResources().getString(R.string.checkedFileText));
            }
        }
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {

        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.multichoice_menu, menu);
        MenuItem shareItem = menu.findItem(R.id.action_share);

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

                selectAll=!selectAll;
                if (selectAll){

                    item.setIcon(ma.getResources().getDrawable(R.drawable.ic_select_all_white_24dp));
                }else{

                    item.setIcon(ma.getResources().getDrawable(R.drawable.ic_unselect_all_white_24dp));

                }
                selectAll(selectAll);
                //deleteSelectedItems();
                // Action picked, so close the CAB
                return true;
            case R.id.action_removeModel:

                if(actionListener!=null){

                    actionListener.toRemove(multiChoiceHelper.getCheckedItemPositions());

                }

                return true;
            case R.id.action_edit:

                if(multiChoiceHelper.getCheckedItemCount()==1) {
                    int index= multiChoiceHelper.getCheckedItemPositions().indexOfValue(true);
                    int pos=multiChoiceHelper.getCheckedItemPositions().keyAt(index);

                    if(actionListener!=null) {
                        actionListener.toEdit(pos);
                    }
                }else{

                    Utils.toast(ma.getApplicationContext(),ma.getResources().getString(R.string.warning_edit));

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

        if(isHeader(headerPosition)) {
            ((TextView) header.findViewById(R.id.text)).setText(((CustomHeader)dataset.getObject(headerPosition)).getTitle());
            SetHeaderCheck(headerPosition, isHeaderCheck(headerPosition),false);
            updateCheckedHeader(header.findViewById(R.id.titleCheckLayout),headerPosition,false);
        }
    }

    @Override
    public boolean isHeader(int itemPosition) {
        return (dataset.getObject(itemPosition) instanceof CustomHeader);
    }

    @Override
    public void ToggleTitle(View header,String title) {

        toggleItem(header,dataset.getHeaderPosition(title));


    }


    public void selectAll(boolean enabled){

        if(multiChoiceHelper!=null){
            if (dataset.size() > 0) {
                for(int i=0;i<dataset.size();i++) {

                    if( !isHeader(i)){
                        multiChoiceHelper.setItemChecked(dataset.getMaterial(i).getId(),i,enabled,true);

                    }
                }
            }
        }
    }


}