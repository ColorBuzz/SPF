package com.colorbuzztechgmail.spf;

/**
 * Created by PC01 on 11/01/2018.
 */


import android.content.Context;
import android.databinding.ViewDataBinding;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.util.LongSparseArray;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.BoringLayout;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;


import java.util.ArrayList;
import java.util.Collections;

/**
 * Helper class to reproduce ListView's modal MultiChoice mode with a RecyclerView.
 * Compatible with API 7+.
 * Declare and use this class from inside your Adapter.
 *
 * @author Christophe Beyls
 */
public class MultiChoiceHelper {

    /**
     * A handy ViewHolder base class which works with the MultiChoiceHelper
     * and reproduces the default behavior of a ListView.
     */

    public interface MultiChoiceModeListener extends ActionMode.Callback {
        /**
         * Called when an item is checked or unchecked during selection mode.
         *
         * @param mode     The {@link ActionMode} providing the selection startSupportActionModemode
         * @param position Adapter position of the item that was checked or unchecked
         * @param id       Adapter ID of the item that was checked or unchecked
         * @param checked  <code>true</code> if the item is now checked, <code>false</code>
         *                 if the item is now unchecked.
         */
        void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked);
    }

    private static final int CHECK_POSITION_SEARCH_DISTANCE = 20;

    private final AppCompatActivity activity;
    private final RecyclerView.Adapter adapter;
    private LongSparseArray<Boolean> checkStates;
    private LongSparseArray<Integer> checkedIdStates;
    private int checkedItemCount = 0;
    private MultiChoiceModeWrapper multiChoiceModeCallback;
    ActionMode choiceActionMode;
   public Boolean selectionMode=false;

    public void EnableSelectionMode(boolean enabled){
        this.selectionMode=enabled;
    }


    /**
     * Make sure this constructor is called before setting the adapter on the RecyclerView
     * so this class will be notified before the RecyclerView in case of data set changes.
     */
    public MultiChoiceHelper(@NonNull AppCompatActivity activity, @NonNull RecyclerView.Adapter adapter) {
        this.activity = activity;
        this.adapter = adapter;

        adapter.registerAdapterDataObserver(new AdapterDataSetObserver());
        checkStates = new LongSparseArray();
        if (adapter.hasStableIds()) {
            checkedIdStates = new LongSparseArray<>(0);
        }

    }

    public Context getContext() {
        return activity;
    }

    public void setMultiChoiceModeListener(MultiChoiceModeListener listener) {
        if (listener == null) {
            multiChoiceModeCallback = null;
            return;
        }
        if (multiChoiceModeCallback == null) {
            multiChoiceModeCallback = new MultiChoiceModeWrapper();
        }
        multiChoiceModeCallback.setWrapped(listener);
    }

    public int getCheckedItemCount() {
        return checkedItemCount;
    }

    public boolean isItemChecked(long id) {

      if(checkStates.indexOfKey(id)>-1){


          return checkStates.get(id);

      }
            return false;

    }

    public LongSparseArray<Boolean> getCheckedItemPositions() {
        return checkStates;
    }

    public long[] getCheckedItemIds() {
        final LongSparseArray<Integer> idStates = checkedIdStates;
        if (idStates == null) {
            return new long[0];
        }

        final int count = idStates.size();
        final long[] ids = new long[count];

        for (int i = 0; i < count; i++) {
            ids[i] = idStates.keyAt(i);
        }

        return ids;
    }

    public void clearChoices() {
        if (checkedItemCount > 0) {
            selectionMode=false;
            checkStates.clear();

            if (checkedIdStates != null) {
                checkedIdStates.clear();
            }
            checkedItemCount = 0;

            adapter.notifyDataSetChanged();

            if (choiceActionMode != null) {
                choiceActionMode.finish();
            }
        }
    }


    public void setItemChecked(long itemId,int position, boolean value, boolean notifyChanged) {
        // Start selection mode if needed. We don't need to if we're unchecking something.
        if (value) {
            startSupportActionModeIfNeeded();
        }

        boolean oldValue = isItemChecked(itemId);
        checkStates.put(itemId, value);

        if (oldValue != value) {
            final long id = adapter.getItemId(position);

            if (checkedIdStates != null) {
                if (value) {
                    checkedIdStates.put(id, position);
                } else {
                    checkedIdStates.delete(id);
                }
            }

            if (value) {
                checkedItemCount++;
            } else {
                checkedItemCount--;
            }

            if (notifyChanged) {
                adapter.notifyItemChanged(position);
            }

            if (choiceActionMode != null) {
                multiChoiceModeCallback.onItemCheckedStateChanged(choiceActionMode, position, id, value);
                if (checkedItemCount == 0) {
                    selectionMode=false;
                    choiceActionMode.finish();

                }
            }


        }
    }

    public void toggleItemChecked(long itemId,int position, boolean notifyChanged) {
        setItemChecked(itemId,position, !isItemChecked(itemId), notifyChanged);
    }


    private static SparseBooleanArray clone(SparseBooleanArray original) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            return original.clone();
        }
        final int size = original.size();
        SparseBooleanArray clone = new SparseBooleanArray(size);
        for (int i = 0; i < size; ++i) {
            clone.append(original.keyAt(i), original.valueAt(i));
        }
        return clone;
    }



   public void startSupportActionModeIfNeeded() {
        if (choiceActionMode == null) {
            if (multiChoiceModeCallback == null) {
                throw new IllegalStateException("No callback set");


            }

            choiceActionMode = activity.startSupportActionMode(multiChoiceModeCallback);
            choiceActionMode.setTitle("Opciones");

        }else{




        }
    }



    void confirmCheckedPositions( ) {
        /*if (checkedItemCount == 0) {
            selectionMode=false;
            return;
        }

        final int totalCount = adapter.getItemCount();
        boolean checkedCountChanged = false;

        if (totalCount == 0) {
            // Optimized path for empty adapter: removeIndex all items.
            checkStates.clear();
            if (checkedIdStates != null) {
                checkedIdStates.clear();
            }
            checkedItemCount = 0;
            checkedCountChanged = true;
        } else if (checkedIdStates != null) {
            // Clear out the positional check states, we'll rebuild it below from IDs.
            checkStates.clear();

            for (int checkedIndex = 0; checkedIndex < checkedIdStates.size(); checkedIndex++) {
                final long id = checkedIdStates.keyAt(checkedIndex);
                final int lastPos = checkedIdStates.valueAt(checkedIndex);

                if ((lastPos >= totalCount) || (id != adapter.getItemId(lastPos))) {
                    // Look around to see if the ID is nearby. If not, uncheck it.
                    final int start = Math.max(0, lastPos - CHECK_POSITION_SEARCH_DISTANCE);
                    final int end = Math.min(lastPos + CHECK_POSITION_SEARCH_DISTANCE, totalCount);
                    boolean found = false;
                    for (int searchPos = start; searchPos < end; searchPos++) {
                        final long searchId = adapter.getItemId(searchPos);
                        if (id == searchId) {
                            found = true;
                            checkStates.put(searchPos, true);
                            checkedIdStates.setValueAt(checkedIndex, searchPos);
                            break;
                        }
                    }

                    if (!found) {
                        checkedIdStates.delete(id);
                        checkedIndex--;
                        checkedItemCount--;
                        checkedCountChanged = true;
                        if (choiceActionMode != null && multiChoiceModeCallback != null) {
                            multiChoiceModeCallback.onItemCheckedStateChanged(choiceActionMode, lastPos, id, false);
                        }
                    }
                } else {
                    checkStates.put(lastPos, true);
                }
            }
        } else {
            // If the total number of items decreased, removeIndex all out-of-range check indexes.
            for (int i = checkStates.size() - 1; (i >= 0) && (checkStates.keyAt(i) >= totalCount); i--) {
                if (checkStates.valueAt(i)) {
                    checkedItemCount--;
                    choiceActionMode.setTitle(String.valueOf(getCheckedItemCount()) + " " + activity.getResources().getString(R.string.checkedSinleFileText));

                    checkedCountChanged = true;
                }
                checkStates.delete(checkStates.keyAt(i));
            }
        }

        if (checkedCountChanged && choiceActionMode != null) {
            if (checkedItemCount == 0) {
                choiceActionMode.finish();
                selectionMode=false;
            } else {

                choiceActionMode.invalidate();
            }
        }*/
    }


    public void resetCheckPosition(ArrayList<Integer> ids){

        if(checkedItemCount>0) {
            Collections.sort(ids);

            for (Integer i : ids) {

                if (checkStates.get(i)) {

                    checkStates.delete(i);
                    checkedItemCount--;
                    choiceActionMode.setTitle(String.valueOf(getCheckedItemCount()) + " " + activity.getResources().getString(R.string.checkedSinleFileText));


                }
            }

            if (checkedItemCount == 0) {
                choiceActionMode.finish();
                selectionMode = false;
            }

        }
    }

    class AdapterDataSetObserver extends RecyclerView.AdapterDataObserver {

        @Override
        public void onChanged() {
            confirmCheckedPositions();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            confirmCheckedPositions();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            confirmCheckedPositions();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            confirmCheckedPositions();
        }
    }

    class MultiChoiceModeWrapper implements MultiChoiceModeListener {

        private MultiChoiceModeListener wrapped;

        public void setWrapped(@NonNull MultiChoiceModeListener wrapped) {
            this.wrapped = wrapped;
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            return wrapped.onCreateActionMode(mode, menu);
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return wrapped.onPrepareActionMode(mode, menu);
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            return wrapped.onActionItemClicked(mode, item);
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            wrapped.onDestroyActionMode(mode);
            choiceActionMode = null;

            clearChoices();
        }

        @Override
        public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
            wrapped.onItemCheckedStateChanged(mode, position, id, checked);
        }
    }
}