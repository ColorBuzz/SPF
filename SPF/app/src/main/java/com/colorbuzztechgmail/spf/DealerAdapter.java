package com.colorbuzztechgmail.spf;

import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

import java.util.List;

/**
 * Created by PC01 on 15/02/2018.
 */

public class DealerAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    Context context;
    ActionMode choiceActionMode;
    int checkedItemCount=0;
    boolean multichoice=false;
    boolean selectAll=false;
    private View bufferView;

    //Header dealer_contact_header;
    List<Dealership>  headerlist;
    List<Dealership>  contactlist;
    SparseBooleanArray expandBoolean;
    SparseBooleanArray itemSparse;
    RecyclerView mRecycler;
    DealershipFragment dealershipFragment;

    public DealerAdapter(List<Dealership> headerItems, List<Dealership> contactItems, Context mcontext, DealershipFragment fragment)
    {
        this.context=mcontext;
        this.headerlist = headerItems;
        this.contactlist=contactItems;
       this.dealershipFragment=fragment;
        this.mRecycler=dealershipFragment.mRecycler;

        expandBoolean= new SparseBooleanArray();
        itemSparse=new SparseBooleanArray();


        for(int i=0;i<headerlist.size();i++){

            expandBoolean.append(i,true);



        }
        for(int i=0;i<contactlist.size();i++){

            itemSparse.append(i,false);



        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==TYPE_HEADER)
        {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.dealer_contact_header, parent, false);



            return  new VHHeader(v);
        }else{


            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.dealer_contact_item, parent, false);



            return  new VHItem(v);

        }


     }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position)      {
        holder.setIsRecyclable(false                                                              );
        if(holder instanceof VHHeader)
        {


            // VHHeader VHheader = (VHHeader)holder;
             Header  currentHeader = (Header) headerlist.get(position);
             final  VHHeader VHheader = (VHHeader)holder;
             VHheader.setIsRecyclable(false);
             VHheader.itemView.setOnDragListener(new MyDragListener());
            VHheader.txtTitle.setText(currentHeader.getHeader());
            updateCheckedHeader(VHheader.headerContainer,position,false);

            if(expandBoolean.get(position)){

                VHheader.expandFrame.setVisibility(View.VISIBLE);
                if(VHheader.expandFrame.getChildCount()==0){
                    loadConacts(VHheader.expandFrame,position);
                }

            }
            else{
                VHheader.expandFrame.setVisibility(View.GONE);

            }


            VHheader.setItemClickListener(new ItemClickListener() {
                @Override
                public void onClick(View v, int position, boolean isLongClick) {

                    switch (v.getId()){

                        case R.id.txtHeader:

                            if(expandBoolean.get(position)) {

                                expandBoolean.put(position,false);

                                VHheader.expandFrame.setVisibility(View.GONE);

                                updateCheckedHeader(VHheader.headerContainer,position,true);


                            }else{
                                expandBoolean.put(position,true);

                                updateCheckedHeader(VHheader.headerContainer,position,true);



                                VHheader.expandFrame.setVisibility(View.VISIBLE);

                                if (VHheader.expandFrame.getChildCount() == 0) {
                                    loadConacts(VHheader.expandFrame, position);
                                }

                                mRecycler.scrollToPosition(position);
                            }


                        break;



                    }

                }
            });


        }else if (holder instanceof VHItem){

            Dealership  currentItem = (Dealership) headerlist.get(position);
            final  VHItem VHitem = (VHItem) holder;
            VHitem.txtTitle.setText(currentItem.getName());


        }

    }

    private void loadConacts(View container, int position){

        String firstChar=  String.valueOf(((Header)headerlist.get(position)).getHeader().charAt(0));


        if(((LinearLayout) container).getChildCount()>0){

            ((LinearLayout) container).removeAllViews();
        }

        for(int i=0;i<contactlist.size();i++){


            if(dealershipFragment.filterMode==dealershipFragment.SORT_ALPHABET){

                if(String.valueOf(contactlist.get(i).getName().charAt(0)).toUpperCase().equals(firstChar)) {

                    addContact(i,container);

                }
            }

            if(dealershipFragment.filterMode==dealershipFragment.SORT_TYPE){

                if (contactlist.get(i).getCategory().equals(((Header) headerlist.get(position)).getHeader())) {

                    addContact(i,container);

                }
            }


        }






    }

    private void addContact(final int position, View container){

        final int itemPos=position;
        LayoutInflater li=LayoutInflater.from(context);
     final    View v=li.inflate(R.layout.dealer_contact_item,(ViewGroup) container,false);


        ((TextView)v.findViewById(R.id.txtName)).setText(contactlist.get(position).getName());
        ((TextView)v.findViewById(R.id.txtPhone)).setText(contactlist.get(position).getPhone());

         final CheckableLinearLayout checkableLinearLayout=  ((CheckableLinearLayout)v.findViewById(R.id.checkableContainer));
        v.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
/*

                if(!multichoice){
                    startSupportActionModeIfNeeded();
                    itemSparse.put(itemPos,!itemSparse.valueAt(itemPos));


                    checkedItemCount=1;

                    // Utils.toast(context,String.valueOf(itemSparse.get(itemPos) + " " + "pos: " + String.valueOf(itemPos)));
                    multichoice=true;

                    updateCheckedItem(checkableLinearLayout,itemPos,false);
                    updateMultichoiceTitle();
                }
*/


                // showPopup(v,position);

                ClipData data = ClipData.newPlainText("", "");

                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
                        v );
                v .startDrag(data, shadowBuilder, v  , 0);
                v .setVisibility(View.INVISIBLE);
                ViewGroup owner = (ViewGroup) v .getParent();
                owner.removeView(v);


                return true;
            }
        });

        checkableLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(multichoice){

                    itemSparse.put(itemPos,!itemSparse.valueAt(itemPos));

                    if(itemSparse.get(itemPos)){

                        checkedItemCount=checkedItemCount+1;
                    }else{

                        checkedItemCount=checkedItemCount-1;

                    }

                    //Utils.toast(context,String.valueOf(itemSparse.get(itemPos) + "pos: " + String.valueOf(itemPos)));
                    updateCheckedItem(checkableLinearLayout,itemPos,false);

                    if(checkedItemCount<=0){
                        multichoice=false;
                        if(choiceActionMode!=null){
                            choiceActionMode.finish();

                        }

                    }

                    updateMultichoiceTitle();
                }


            }
        });

        final ImageView image=(ImageView)v.findViewById(R.id.clickImageView);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOptionPopup(v);

            }
        });

        ((LinearLayout) container).addView(v);
        updateCheckedItem(checkableLinearLayout,itemPos, false);

    }

    public void showOptionPopup(View v) {
        PopupMenu popup = new PopupMenu(context, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.submenu_delaer, popup.getMenu());
        popup.show();
    }

    private void updateCheckedHeader(CheckableLinearLayout container,int position,boolean animated){
        final boolean isChecked = expandBoolean.get(position);
        if ( container instanceof Checkable) {
            container.setChecked(isChecked);
            if(isChecked){
                ((TextView)container.findViewById(R.id.txtHeader)).setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
                if(animated){
                    ((TextView)container.findViewById(R.id.txtHeader)).setCompoundDrawablesWithIntrinsicBounds(null,null,
                            context.getResources().getDrawable(R.drawable.ic_keyboard_arrow_down_white_animatable_clockwise),null);
                }else{


                    ((TextView)container.findViewById(R.id.txtHeader)).setCompoundDrawablesWithIntrinsicBounds(null,null,
                            context.getResources().getDrawable(R.drawable.ic_keyboard_arrow_up_white_24dp),null);
                }

            }else{
                ((TextView)container.findViewById(R.id.txtHeader)).setTextColor(context.getResources().getColor(R.color.iconsColor));

                 if(animated){

                     ((TextView)container.findViewById(R.id.txtHeader)).setCompoundDrawablesWithIntrinsicBounds(null,null,
                             context.getResources().getDrawable(R.drawable.ic_keyboard_arrow_down_white_animatable_counter_clockwise),null);

                 }else{

                     ((TextView)container.findViewById(R.id.txtHeader)).setCompoundDrawablesWithIntrinsicBounds(null,null,
                             context.getResources().getDrawable(R.drawable.ic_keyboard_arrow_up_white_24dp),null);
                 }

            }

            if(animated){
                (((AnimatedVectorDrawable)(((TextView)container.findViewById(R.id.txtHeader))).getCompoundDrawables()[2])).start();

            }


        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            container.setActivated(isChecked);
        }

    }

    private void updateCheckedItem(CheckableLinearLayout container,int position,boolean animated){

        final boolean isChecked = itemSparse.get(position);
        if ( container instanceof Checkable) {
            container.setChecked(isChecked);




        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            container.setActivated(isChecked);

        }



    }

    @Override
    public int getItemViewType(int position) {
        if(isPositionHeader(position))
            return TYPE_HEADER;
        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return headerlist.get(position) instanceof Header;

    }

    public static void setMargins (View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }

    public void showEditDealerPopUp(int dealerId){


        AddDealerPopUp addDealerPopUp= AddDealerPopUp.newInstance(dealerId,true);
        addDealerPopUp.show(dealershipFragment.ma.getSupportFragmentManager(),"editDealer");



        addDealerPopUp.setDimissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

                ((DealerAdapter)mRecycler.getAdapter()).choiceActionMode.finish();



            }
        });



    }

    private void showRemoveDialog(final SparseBooleanArray checkedList){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);
        alertDialogBuilder
                .setTitle( context.getResources().getString(R.string.dialogTitle_remove))
                .setMessage( context.getResources().getString(R.string.dialogMsg_remove))
                .setCancelable(false)
                .setPositiveButton(context.getResources().getString(R.string.dialog_remove),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {


                                if(checkedList.size()>0) {
                                    for(int i=0;i<checkedList.size();i++) {

                                        if(checkedList.valueAt(i)) {
                                           dealershipFragment.ma.userData.deleteDealership(contactlist.get(checkedList.keyAt(i)).getId());




                                        }
                                    }

                                   dealershipFragment.setCustomAdapter(0);

                                    if(choiceActionMode!=null){
                                        choiceActionMode.finish();

                                    }

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

    @Override
    public int getItemCount() {
        return headerlist.size();
    }

    class VHHeader extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView txtTitle;
        ItemClickListener itemClickListener;
        LinearLayout expandFrame;
        CheckableLinearLayout headerContainer;

        public VHHeader(View itemView) {
            super(itemView);
            this.txtTitle = (TextView)itemView.findViewById(R.id.txtHeader);
            this.txtTitle.setOnClickListener(this);
            this.expandFrame=(LinearLayout) itemView.findViewById(R.id.expanFrame);
            this.headerContainer=(CheckableLinearLayout) itemView.findViewById(R.id.headerContainer);
             this.setIsRecyclable(false);

        }

        public void setItemClickListener(ItemClickListener itemClickListener) {

            this.itemClickListener = itemClickListener;

        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition(), false);
        }
    }
    class VHItem extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView txtTitle;
        ItemClickListener itemClickListener;


        ExpandableRelativeLayout contactContainer;
        public VHItem(View itemView) {
            super(itemView);
            this.txtTitle = (TextView)itemView.findViewById(R.id.txtName);
            this.txtTitle.setOnClickListener(this);




        }


        public void setItemClickListener(ItemClickListener itemClickListener) {

            this.itemClickListener = itemClickListener;

        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition(), false);
        }
    }

    public void startSupportActionModeIfNeeded() {
        if (choiceActionMode == null) {
            if (mCallback == null) {
                throw new IllegalStateException("No callback set");
            }
            choiceActionMode = dealershipFragment.ma.startSupportActionMode(mCallback);


        }
    }

    private final ActionMode.Callback mCallback = new ActionMode.Callback() {



        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {

            MenuInflater inflater = mode.getMenuInflater();

            inflater.inflate(R.menu.multichoice_dealer_menu, menu);


            return true;

        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {



            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

            switch (item.getItemId()) {

                case R.id.action_removeModel:
                    showRemoveDialog(itemSparse);

                    return true;
                case R.id.action_edit:

                    if(checkedItemCount==1) {


                       showEditDealerPopUp(contactlist.get(itemSparse.indexOfValue(true)).getId());
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
            choiceActionMode = null;
            dealershipFragment.setCustomAdapter(0);
        }
    };

    private void updateMultichoiceTitle(){

        if(choiceActionMode!=null){

            if(checkedItemCount==1){
                choiceActionMode.setTitle(String.valueOf(checkedItemCount )+ " " + context.getResources().getString(R.string.checkedSinleFileText));
                ((MenuItem)choiceActionMode.getMenu().findItem(R.id.action_edit)).setEnabled(true);

            }else{
                choiceActionMode.setTitle(String.valueOf(checkedItemCount )+ " " + context.getResources().getString(R.string.checkedFileText));
                ((MenuItem)choiceActionMode.getMenu().findItem(R.id.action_edit)).setEnabled(false);

            }


        }


    }

    private final class MyTouchListener implements View.OnTouchListener {
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {


                return true;
            } else {
                return false;
            }
        }



    }

    public class MyDragListener implements View.OnDragListener {
        Drawable removeImage=context.getResources().getDrawable(
                R.drawable.bg_black_light_grey);

        Drawable enterShape = context.getResources().getDrawable(
                R.drawable.bg_accent_color);
        Drawable normalShape = new ColorDrawable(Color.TRANSPARENT);

        @Override
        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();
            switch (event.getAction()) {


                case DragEvent.ACTION_DRAG_STARTED:
                   // v.setBackgroundDrawable(enterShape);
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                  //  v.setBackgroundDrawable(enterShape);
                    Log.e("Drag","DRAG_ENTERED" );

                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    //v.setBackgroundDrawable(enterShape);

                    Log.e("Drag","DRAG_EXITED" );
                    break;
                case DragEvent.ACTION_DROP:
                    // Dropped, reassign View to ViewGroup
                    View view = (View) event.getLocalState();

                     LinearLayout container = (LinearLayout) v.findViewById(R.id.expanFrame) ;
                    ((ViewGroup) container).addView(view);


                  // showRemoveDialog(clickView);
                    Log.e("Drag","DRAG_DROP" );

                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                   //v.setBackgroundDrawable(normalShape);

                    View item = (View) event.getLocalState();
                    item.setVisibility(View.VISIBLE);

                    Log.e("Drag","DRAG_ENDED" );

                default:
                    break;
            }
            return true;
        }
    }

}