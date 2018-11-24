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
import android.widget.LinearLayout;

import com.colorbuzztechgmail.spf.databinding.ExternalWorksBinding;

import com.github.aakira.expandablelayout.ExpandableLayoutListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.colorbuzztechgmail.spf.Utils.setMargins;

/**
 * Created by PC01 on 12/01/2018.
 */

public class ExternalWorksAdapter extends  RecyclerView.Adapter<ViewHolderMultiChoice> implements MultiChoiceHelper.MultiChoiceModeListener, PopupMenu.OnMenuItemClickListener{


    private Context context;
     public int itemClick=0;
 

    private final Comparator<ExternalWorks> mComparator;
    private SparseBooleanArray expandState  ;
    private  ExternalWorksFragment mExternalFragment;
    public MultiChoiceHelper multiChoiceHelper;
    public boolean refreshInitiaPos=false;
    boolean selectAll=false;
    private ItemActionListener actionListener;


    public void setActionListener(ItemActionListener actionListener) {
        this.actionListener = actionListener;
    }

    private void refreshInfoBar(int count){

        if(mExternalFragment!=null) {
            mExternalFragment.refreshInfobar(count);
        }
    }

    public void add(ExternalWorks externalWorks) {
       externalList.add(externalWorks);

        expandState.append(externalWorks.getId(),false);
        refreshInfoBar(externalList.size());
    }

    public void add(List<ExternalWorks> auxExternalList) {
        for(int i=0;i<auxExternalList.size();i++){
            expandState.append(auxExternalList.get(i).getId(),false);


        }
       externalList.addAll(auxExternalList);
        refreshInfoBar(externalList.size());


    }

    public void remove(List<ExternalWorks> auxExternalList) {

       externalList.beginBatchedUpdates();
        for (int i=0;i<externalList.size();i++) {
            for (int j=0;j<auxExternalList.size();j++){

                if(externalList.get(i).equals(auxExternalList.get(j))){
                    expandState.delete(auxExternalList.get(j).getId());

                   externalList.remove(auxExternalList.get(j));
                    break;
                }

            }
        }
       externalList.endBatchedUpdates();
        refreshInfoBar(externalList.size());



    }

    public void remove(Integer position) {

       externalList.beginBatchedUpdates();
        expandState.delete(externalList.get(position).getId());

       externalList.removeItemAt(position);

       externalList.endBatchedUpdates();
        refreshInfoBar(externalList.size());


    }

    public void remove(ArrayList<Integer> position) {

        Collections.sort(position);

       externalList.beginBatchedUpdates();

        for(int i=position.size()-1;i>=0;i--) {
            expandState.delete(externalList.get(position.get(i)).getId());

           externalList.removeItemAt(position.get(i));
        }

       externalList.endBatchedUpdates();
        refreshInfoBar(externalList.size());



    }

    public void remove(ExternalWorks externalWorks){
       externalList.beginBatchedUpdates();
       externalList.remove(externalWorks);

        expandState.delete(externalWorks.getId());


       externalList.endBatchedUpdates();
        refreshInfoBar(externalList.size());


    }
    public void removeAll(){

        externalList.beginBatchedUpdates();
        externalList.clear();
        expandState.clear();
        externalList.endBatchedUpdates();
        refreshInfoBar(externalList.size());


    }

    public void replaceAll(List<ExternalWorks> auxExternalList) {
       externalList.beginBatchedUpdates();

        expandState.clear();
        for (int i =externalList.size() - 1; i >= 0; i--) {
            final ExternalWorks externalWorks =externalList.get(i);
            if (!auxExternalList.contains(externalWorks)) {
               externalList.remove(externalWorks);
            }
        }
       externalList.addAll(auxExternalList);

        for(int i=0;i<auxExternalList.size();i++){
            expandState.append(auxExternalList.get(i).getId(),false);


        }
       externalList.endBatchedUpdates();
        refreshInfoBar(externalList.size());



    }
    public void replaceItem(ExternalWorks externalWorks) {
        externalList.beginBatchedUpdates();

        int index=-1;

        for (int i =externalList.size() - 1; i >= 0; i--) {
            final ExternalWorks auxexternalWorks =externalList.get(i);
            if (externalWorks.getId()==(auxexternalWorks.getId())) {
              index=i;
                break;
            }
        }

        if (index > -1) {

            externalList.updateItemAt(index,externalWorks);
        }


        externalList.endBatchedUpdates();
        refreshInfoBar(externalList.size());



    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ExternalWorksAdapter(Context context, Comparator mComparator, ExternalWorksFragment externalWorksFragment) {
        this.context = context;
        this.mExternalFragment=externalWorksFragment;
        this.mComparator=mComparator;


        expandState= new SparseBooleanArray();
        multiChoiceHelper= new MultiChoiceHelper((AppCompatActivity) externalWorksFragment.getActivity(),this);
        multiChoiceHelper.setMultiChoiceModeListener(this);

    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolderMultiChoice onCreateViewHolder(ViewGroup parent,
                                                              int viewType) {





        return  ExternalCheckableListViewHolder.create(parent);

    }

    @Override
    public void onBindViewHolder(final ViewHolderMultiChoice holder,final int position) {

        holder.bind(multiChoiceHelper,position);

        final ExternalWorks externalWorks ;
        externalWorks = externalList.get(position);

        ((ExternalCheckableListViewHolder)holder).performBindExternal(externalWorks);

        holder.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                itemClick=position;

                if(!multiChoiceHelper.selectionMode) {


                    if (position != RecyclerView.NO_POSITION) {


                        multiChoiceHelper.setItemChecked(externalWorks.getId(), position, true, true);
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

                    multiChoiceHelper.toggleItemChecked(externalWorks.getId(),position, true);

                }
            }
        });
    }



    // Return the size of your dataset (invoked by the layout manager)

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
                mExternalFragment.getContext());
        alertDialogBuilder
                .setTitle( mExternalFragment.getResources().getString(R.string.dialogTitle_remove))
                .setMessage( mExternalFragment.getResources().getString(R.string.dialogMsg_remove))
                .setCancelable(false)
                .setPositiveButton( mExternalFragment.getResources().getString(R.string.dialog_remove),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                final ArrayList<Integer> removeIndex=new ArrayList<Integer>();
                                if(checkedList.size()>0) {

                                    final List<ExternalWorks>auxExternalList=new ArrayList<>();
                                    for(int i=0;i<checkedList.size();i++) {

                                        if(checkedList.valueAt(i)) {
                                            mExternalFragment.ma.userData.deleteExternalWorks(externalList.get(checkedList.keyAt(i)).getId());


                                            final ExternalWorks externalWorks=externalList.get(checkedList.keyAt(i));

                                            auxExternalList.add(externalWorks);

                                            removeIndex.add(checkedList.keyAt(i));
                                        }
                                    }

                                    remove(removeIndex);
                                    multiChoiceHelper.clearChoices();
                                    showUndoSnackbar(auxExternalList);
                                }
                            }
                        }
                )
                .setNegativeButton(mExternalFragment.getResources().getString(R.string.dialog_cancel),
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

    private void showUndoSnackbar(final List<ExternalWorks> auxExternalList){

        CoordinatorLayout view = (CoordinatorLayout) mExternalFragment.ma.findViewById(R.id.parentLayout);

        String mssg;

        if(auxExternalList.size()==1){

            mssg = auxExternalList.get(0).getExternal() + "eliminados";

        }else {

            mssg = String.valueOf(auxExternalList.size()) + context.getResources().getString(R.string.externalActivity) + "eliminados";

        }

        Snackbar snackbar = Snackbar.make(view, mssg, Snackbar.LENGTH_LONG)
                .setAction(context.getString(R.string.action_undo), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        for(ExternalWorks externalWorks: auxExternalList){

                            externalList.add(externalWorks);

                        }
                    }

                }).setCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar snackbar, int dismissType) {
                        super.onDismissed(snackbar, dismissType);

                        if(dismissType !=DISMISS_EVENT_ACTION){


                            for(ExternalWorks externalWorks: auxExternalList){

                                ModelDataBase db= new ModelDataBase(context);

                                db.deleteExternalWorks(externalWorks.getId());


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
        return externalList.size();
    }

    @Override
    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
        if(multiChoiceHelper.getCheckedItemCount()>0) {
            if(multiChoiceHelper.getCheckedItemCount()==1){
                mode.setTitle(String.valueOf(multiChoiceHelper.getCheckedItemCount()) + " " + mExternalFragment.getResources().getString(R.string.checkedSinleFileText));
                ((MenuItem)mode.getMenu().findItem(R.id.action_edit)).setEnabled(true);

            }else{
                mode.setTitle(String.valueOf(multiChoiceHelper.getCheckedItemCount() )+ " " + mExternalFragment.getResources().getString(R.string.checkedFileText));
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

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {


        switch (item.getItemId()) {
            case R.id.action_selectAll:

                selectAll=!selectAll;
                if (selectAll){

                    item.setIcon(mExternalFragment.getResources().getDrawable(R.drawable.ic_select_all_white_24dp));
                }else{

                    item.setIcon(mExternalFragment.getResources().getDrawable(R.drawable.ic_unselect_all_white_24dp));

                }
                //multiChoiceHelper.selectAll(selectAll);
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


                    mExternalFragment.editInfo(externalList.get(pos).getId());
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

    private final SortedList.Callback<ExternalWorks> mCallback = new SortedList.Callback<ExternalWorks>() {

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
        public int compare(ExternalWorks a, ExternalWorks b) {

            return mComparator.compare(a, b);
        }

        @Override
        public boolean areContentsTheSame(ExternalWorks oldItem, ExternalWorks newItem) {
            return oldItem.equals(newItem);
        }

        @Override
        public boolean areItemsTheSame(ExternalWorks item1, ExternalWorks item2) {
            return item1.getId() == item2.getId();
        }
    };

    final SortedList<ExternalWorks> externalList = new SortedList<>(ExternalWorks.class, mCallback);
}