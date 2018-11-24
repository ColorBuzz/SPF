package com.colorbuzztechgmail.spf;

import android.content.Context;
import android.content.DialogInterface;
import android.databinding.ViewDataBinding;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.util.SortedList;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.colorbuzztechgmail.spf.databinding.AccessoriesBinding;
import com.colorbuzztechgmail.spf.databinding.MaterialCardBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by PC01 on 12/01/2018.
 */

public class AccessoriesAdapter extends  RecyclerView.Adapter<ViewHolderMultiChoice> implements MultiChoiceHelper.MultiChoiceModeListener, PopupMenu.OnMenuItemClickListener{


    private final Comparator<Accessories> mComparator;
    private final SortedList.Callback<Accessories> mCallback = new SortedList.Callback<Accessories>() {

        @Override
        public void onInserted(int position, int count) {
            notifyItemRangeInserted(position, count);
        }

        @Override
        public void onRemoved(int position, int count) {
            notifyItemRangeRemoved(position, count);
        }

        @Override
        public void onMoved(int fromPosition, int toPosition) {
            notifyItemMoved(fromPosition, toPosition);
        }

        @Override
        public void onChanged(int position, int count) {
            notifyItemRangeChanged(position, count);
        }

        @Override
        public int compare(Accessories a, Accessories b) {

            return mComparator.compare(a, b);
        }

        @Override
        public boolean areContentsTheSame(Accessories oldItem, Accessories newItem) {
            return oldItem.equals(newItem);
        }

        @Override
        public boolean areItemsTheSame(Accessories item1, Accessories item2) {
            return item1.getId() == item2.getId();
        }
    };
    final SortedList<Accessories> accesoriesList = new SortedList<>(Accessories.class, mCallback);
    public int itemClick=0;
    public MultiChoiceHelper multiChoiceHelper;
    public boolean refreshInitiaPos=false;
    boolean selectAll=false;
    private Context context;
    private LayoutInflater inflater;
    private SparseBooleanArray expandState  ;
    private  AccessoriesFragment mAccessoriesFragment;
    private ItemActionListener actionListener;


    public void setActionListener(ItemActionListener actionListener) {
        this.actionListener = actionListener;
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public AccessoriesAdapter(Context context, Comparator mComparator, AccessoriesFragment accesoriesFragment) {
        this.context = context;
        this.mAccessoriesFragment=accesoriesFragment;
        this.mComparator=mComparator;


        expandState= new SparseBooleanArray();
        multiChoiceHelper= new MultiChoiceHelper((AppCompatActivity) accesoriesFragment.getActivity(),this);
        multiChoiceHelper.setMultiChoiceModeListener(this);

    }

    private void refreshInfoBar(int count){

        if(mAccessoriesFragment!=null) {
            mAccessoriesFragment.refreshInfobar(count);
        }
    }

    public void add(Accessories accesories) {
       accesoriesList.add(accesories);

        expandState.append(accesories.getId(),false);
        refreshInfoBar(accesoriesList.size());
    }

    public void add(List<Accessories> auxAccesoriesList) {
        for(int i=0;i<auxAccesoriesList.size();i++){
            expandState.append(auxAccesoriesList.get(i).getId(),false);


        }
       accesoriesList.addAll(auxAccesoriesList);
        refreshInfoBar(accesoriesList.size());


    }

    public void remove(List<Accessories> auxAccesoriesList) {

       accesoriesList.beginBatchedUpdates();
        for (int i=0;i<accesoriesList.size();i++) {
            for (int j=0;j<auxAccesoriesList.size();j++){

                if(accesoriesList.get(i).equals(auxAccesoriesList.get(j))){
                    expandState.delete(auxAccesoriesList.get(j).getId());

                   accesoriesList.remove(auxAccesoriesList.get(j));
                    break;
                }

            }
        }
       accesoriesList.endBatchedUpdates();
        refreshInfoBar(accesoriesList.size());



    }

    public void remove(Integer position) {

       accesoriesList.beginBatchedUpdates();
        expandState.delete(accesoriesList.get(position).getId());

       accesoriesList.removeItemAt(position);

       accesoriesList.endBatchedUpdates();
        refreshInfoBar(accesoriesList.size());


    }

    public void remove(ArrayList<Integer> position) {

        Collections.sort(position);

       accesoriesList.beginBatchedUpdates();

        for(int i=position.size()-1;i>=0;i--) {
            expandState.delete(accesoriesList.get(position.get(i)).getId());

           accesoriesList.removeItemAt(position.get(i));
        }

       accesoriesList.endBatchedUpdates();
        refreshInfoBar(accesoriesList.size());



    }

    public void remove(Accessories accesories){
       accesoriesList.beginBatchedUpdates();
       accesoriesList.remove(accesories);

        expandState.delete(accesories.getId());


       accesoriesList.endBatchedUpdates();
        refreshInfoBar(accesoriesList.size());


    }

    public void removeAll(){

        accesoriesList.beginBatchedUpdates();
        accesoriesList.clear();
        expandState.clear();
        accesoriesList.endBatchedUpdates();
        refreshInfoBar(accesoriesList.size());


    }

    public void replaceAll(List<Accessories> auxAccesoriesList) {
       accesoriesList.beginBatchedUpdates();

        expandState.clear();
        for (int i =accesoriesList.size() - 1; i >= 0; i--) {
            final Accessories accesories =accesoriesList.get(i);
            if (!auxAccesoriesList.contains(accesories)) {
               accesoriesList.remove(accesories);
            }
        }
       accesoriesList.addAll(auxAccesoriesList);

        for(int i=0;i<auxAccesoriesList.size();i++){
            expandState.append(auxAccesoriesList.get(i).getId(),false);


        }
       accesoriesList.endBatchedUpdates();
        refreshInfoBar(accesoriesList.size());



    }

    public void replaceItem(Accessories accesories) {
        accesoriesList.beginBatchedUpdates();

        int index=-1;

        for (int i =accesoriesList.size() - 1; i >= 0; i--) {
            final Accessories auxaccesories =accesoriesList.get(i);
            if (accesories.getId()==(auxaccesories.getId())) {
              index=i;
                break;
            }
        }

        if (index > -1) {

            accesoriesList.updateItemAt(index,accesories);
        }


        accesoriesList.endBatchedUpdates();
        refreshInfoBar(accesoriesList.size());



    }

    // Return the size of your dataset (invoked by the layout manager)

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolderMultiChoice onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }



        return  AccesoryCheckableListViewHolder.create(parent);

    }

    @Override
    public void onBindViewHolder(final  ViewHolderMultiChoice holder,final int position) {

        holder.bind(multiChoiceHelper,position);

        final Accessories accesory = accesoriesList.get(position);


        ((AccesoryCheckableListViewHolder)holder).performBindAccessories(accesory);

        holder.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                itemClick=position;
                if(!multiChoiceHelper.selectionMode) {


                    if (position != RecyclerView.NO_POSITION) {


                        multiChoiceHelper.setItemChecked(accesory.getId(), position, true, true);
                        holder.updateCheckedState(position);

                        multiChoiceHelper.EnableSelectionMode(true);

                    }
                }

                return false;
            }
        });

        holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClick=position;

                if (multiChoiceHelper.getCheckedItemCount()==0) {

                    if (actionListener != null) {

                        actionListener.onClick(view, position, false);


                    }

                }else{

                    multiChoiceHelper.toggleItemChecked(accesory.getId(),position, true);

                }
            }
        });
    }

    public void showPopup(View v,int position) {


            itemClick=position;
            PopupMenu popup = new PopupMenu(context, v);
            MenuInflater inflater = popup.getMenuInflater();
            popup.setOnMenuItemClickListener(this);

            inflater.inflate(R.menu.submenu_external_option, popup.getMenu());
            popup.show();


    }

    public void animated(ImageView image, boolean enable) {



        if (enable){
            image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_keyboard_arrow_down_grey_animatable_clockwise));

        }else{
            image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_keyboard_arrow_down_grey_animatable_counter_clockwise));



        }


        ((AnimatedVectorDrawable)image.getDrawable()).start();


    }

    private void showRemoveDialog(final SparseBooleanArray checkedList){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);
        alertDialogBuilder
                .setTitle(context.getResources().getString(R.string.dialogTitle_remove))
                .setMessage( context.getResources().getString(R.string.dialogMsg_remove))
                .setCancelable(false)
                .setPositiveButton( context.getResources().getString(R.string.dialog_remove),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                final ArrayList<Integer> removeIndex=new ArrayList<Integer>();
                                if(checkedList.size()>0) {

                                    final List<Accessories>auxAccesoriesList=new ArrayList<>();
                                    for(int i=0;i<checkedList.size();i++) {

                                        if(checkedList.valueAt(i)) {
                                            mAccessoriesFragment.ma.userData.deleteAccessory(accesoriesList.get(checkedList.keyAt(i)).getId());


                                            final Accessories accesories=accesoriesList.get(checkedList.keyAt(i));

                                            auxAccesoriesList.add(accesories);

                                            removeIndex.add(checkedList.keyAt(i));
                                        }
                                    }

                                    remove(removeIndex);
                                    multiChoiceHelper.clearChoices();
                                    showUndoSnackbar(auxAccesoriesList);
                                }
                            }
                        }
                )
                .setNegativeButton(context.getResources().getString(R.string.dialog_cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.cancel();
                            }
                        }
                );
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bg_black_line));

        alertDialog.show();
    }

    private void showUndoSnackbar(final List<Accessories> auxAccesoriesList){

        CoordinatorLayout view = (CoordinatorLayout) mAccessoriesFragment.ma.findViewById(R.id.parentLayout);

        String mssg;

        if(auxAccesoriesList.size()==1){

            mssg = auxAccesoriesList.get(0).getName() + "eliminados";

        }else {

            mssg = String.valueOf(auxAccesoriesList.size()) + context.getResources().getString(R.string.action_complements) + "eliminados";

        }

        Snackbar snackbar = Snackbar.make(view, mssg, Snackbar.LENGTH_LONG)
                .setAction(context.getString(R.string.action_undo), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        for(Accessories accesories: auxAccesoriesList){

                            accesoriesList.add(accesories);

                        }
                    }

                }).setCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar snackbar, int dismissType) {
                        super.onDismissed(snackbar, dismissType);

                        if(dismissType !=DISMISS_EVENT_ACTION){


                            for(Accessories accesories: auxAccesoriesList){

                                ModelDataBase db= new ModelDataBase(context);

                                db.deleteAccessory(accesories.getId());


                            }


                        }

                    }
                });


        snackbar.show();
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {

            default:
                return false;
        }
    }

    @Override
    public int getItemCount() {
        return accesoriesList.size();
    }

    @Override
    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
        if(multiChoiceHelper.getCheckedItemCount()>0) {
            if(multiChoiceHelper.getCheckedItemCount()==1){
                mode.setTitle(String.valueOf(multiChoiceHelper.getCheckedItemCount()) + " " + mAccessoriesFragment.getResources().getString(R.string.checkedSinleFileText));
                ((MenuItem)mode.getMenu().findItem(R.id.action_edit)).setEnabled(true);

            }else{
                mode.setTitle(String.valueOf(multiChoiceHelper.getCheckedItemCount() )+ " " + mAccessoriesFragment.getResources().getString(R.string.checkedFileText));
                ((MenuItem)mode.getMenu().findItem(R.id.action_edit)).setEnabled(false);
            }
        }
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {

        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.multichoice_external_menu, menu);

        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {


        switch (item.getItemId()) {
            case R.id.action_selectAll:

                selectAll=!selectAll;
                if (selectAll){

                    item.setIcon(mAccessoriesFragment.getResources().getDrawable(R.drawable.ic_select_all_white_24dp));
                }else{

                    item.setIcon(mAccessoriesFragment.getResources().getDrawable(R.drawable.ic_unselect_all_white_24dp));

                }
               // multiChoiceHelper.selectAll(selectAll);
                //deleteSelectedItems();
                // Action picked, so close the CAB
                return true;
            case R.id.action_removeModel:
                showRemoveDialog(multiChoiceHelper.getCheckedItemPositions());

                return true;
            case R.id.action_edit:

                if(multiChoiceHelper.getCheckedItemCount()==1) {
                    int index= multiChoiceHelper.getCheckedItemPositions().indexOfValue(true);
                    int pos=multiChoiceHelper.getCheckedItemPositions().keyAt(index);


                    mAccessoriesFragment.editInfo(accesoriesList.get(pos).getId());
                    //showEditDialog(mSortedList.get( multiChoiceHelper.lastTruePositionClick));
                }else{

                    Utils.toast(context,context.getString(R.string.warning_edit));

                }
                return true;
            default:
                return false;
        }

    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {


    }

}