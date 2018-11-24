package com.colorbuzztechgmail.spf;


import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.TextView;

import com.colorbuzztechgmail.spf.Utils.ViewMode;

import java.util.ArrayList;

public class CustomMaterialListAdapter extends RecyclerView.Adapter<ViewHolderMultiChoice>implements MultiChoiceHelper.MultiChoiceModeListener ,HeaderItemDecoration.StickyHeaderInterface {
    public static final int TITLE=0;
    public static final int ITEM=1;
    private CustomMaterialDataset dataset;
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

    public CustomMaterialListAdapter(@Nullable ItemActionListener itemClickListener, AppCompatActivity ma, boolean enableMultiChoice) {

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


                return  CustomMaterialCheckableListViewHolder.create(parent,getViewMode());


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


                    switch (dataset.getSortType()){

                        case "LAST":
                            if (dataset.getCustomMaterial(i).getDate().contains(header.getTitle())) {

                                deleteIdBuffer.add(dataset.getCustomMaterial(i).getId());

                                header.addObject(dataset.getCustomMaterial(i));

                                deleteBuffer.add(i);

                            }
                            break;

                        case "NAME":

                            final String first=dataset.getCustomMaterial(i).getName().toLowerCase();
                            if(!Character.isAlphabetic(Character.valueOf(first.charAt(0)))){

                                if(!Character.isAlphabetic(Character.valueOf(header.getTitle().charAt(0)))){

                                    deleteIdBuffer.add(dataset.getCustomMaterial(i).getId());

                                    header.addObject(dataset.getCustomMaterial(i));

                                    deleteBuffer.add(i);

                                }


                            }else if ((dataset.getCustomMaterial(i).getName().substring(0,1)).toLowerCase().equals(header.getTitle().toLowerCase())) {


                                deleteIdBuffer.add(dataset.getCustomMaterial(i).getId());

                                header.addObject(dataset.getCustomMaterial(i));

                                deleteBuffer.add(i);

                            }
                            break;

                        case "TYPE":
                            if ((dataset.getCustomMaterial(i).getType()).toLowerCase().equals(header.getTitle().toLowerCase())) {

                                deleteIdBuffer.add(dataset.getCustomMaterial(i).getId());

                                header.addObject(dataset.getCustomMaterial(i));

                                deleteBuffer.add(i);

                            }

                            break;

                        case "DEALER":
                            if ((dataset.getCustomMaterial(i).getDealership()).toLowerCase().equals(header.getTitle().toLowerCase())) {

                                deleteIdBuffer.add(dataset.getCustomMaterial(i).getId());

                                header.addObject(dataset.getCustomMaterial(i));

                                deleteBuffer.add(i);

                            }

                            break;


                        case "SEASONS":
                            if ((dataset.getCustomMaterial(i).getSeasons()).toLowerCase().equals(header.getTitle().toLowerCase())) {

                                deleteIdBuffer.add(dataset.getCustomMaterial(i).getId());

                                header.addObject(dataset.getCustomMaterial(i));

                                deleteBuffer.add(i);

                            }

                            break;
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

    private void updateItemState(ViewHolderMultiChoice holder,int id){

        switch (((CustomMaterialCheckableListViewHolder) holder).flipView.getCurrentFlipState()){


            case FRONT_SIDE:

                if(dataset.getItemState(id)){

                    ((CustomMaterialCheckableListViewHolder) holder).flipView.flipTheView();

                }

                break;

            case BACK_SIDE:


                if(!dataset.getItemState(id)){

                    ((CustomMaterialCheckableListViewHolder) holder).flipView.flipTheView();

                }
                break;




        }


    }


    @Override
    public int getItemCount() {
        return dataset == null ? 0 : dataset.size();
    }

    public void dataset(CustomMaterialDataset dataset) {
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
            final CustomMaterial material = dataset.getCustomMaterial(position);
           holder.performBind(material);

           switch (getViewMode()){

               case GRID:
                   updateItemState(holder,material.getId());


                   break;

               case LIST:

                   break;


           }

            if(enableMultiChoice) {

            holder.bind(multiChoiceHelper, material.getId());

            holder.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    itemClick = position;

                    if (!multiChoiceHelper.selectionMode) {


                        if (position != RecyclerView.NO_POSITION) {

                            resetFlipview(holder,material.getId());

                            multiChoiceHelper.setItemChecked(material.getId(), position, true, true);
                            holder.updateCheckedState(position);

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
                itemClick=position;

                if(holder.isMultiChoiceActive()){

                    if(!multiChoiceHelper.isItemChecked(material.getId())){

                        resetFlipview(holder,position);


                    }
                    multiChoiceHelper.toggleItemChecked(material.getId(),position, true);


                }else{

                    switch (getViewMode()){

                        case LIST:
                            if (actionListener != null) {

                                actionListener.onClick(view, position, false);


                            }
                            break;

                        case GRID:

                            ((CustomMaterialCheckableListViewHolder) holder).flipView.flipTheView();
                            dataset.updateSparseState(position, ((CustomMaterialCheckableListViewHolder) holder).flipView.isBackSide());

                            break;

                    }


                }

            }
        });


      }
    }

    private void resetFlipview(ViewHolderMultiChoice holder,int position){
        if(getViewMode()==ViewMode.GRID){




        if (((CustomMaterialCheckableListViewHolder) holder).flipView.isBackSide()) {
            dataset.updateSparseState(position, false);
            ((CustomMaterialCheckableListViewHolder) holder).flipView.flipTheView();



        }
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
        inflater.inflate(R.menu.multichoice_material_menu, menu);
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
                        multiChoiceHelper.setItemChecked(dataset.getCustomMaterial(i).getId(),i,enabled,true);

                    }
                }
            }
        }
    }
}