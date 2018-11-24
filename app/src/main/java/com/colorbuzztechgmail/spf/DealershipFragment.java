package com.colorbuzztechgmail.spf;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.colorbuzztechgmail.spf.popup.AddDealerPopUp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;


/**
 * Created by PC01 on 21/06/2017.
 */
public class DealershipFragment extends Fragment implements View.OnClickListener ,Utils.onSavedInterface,ItemActionListener{

    MenuItem shortItem;
    public static final int SORT_ALPHABET=0;
    public static final int SORT_TYPE=1;
    private Dialog addcontactdialog;

    public static int REQUEST_CODE_CONTACT_PHONE =777;
    private ModelDataBase db;


    private FloatingActionButton importFab;

    public List<Dealership> bufferDealer = new ArrayList<>();

    //Display
    public RecyclerView mRecycler;

    private GridSpacingItemDecoration itemDecoration;

    public int filterMode = SORT_ALPHABET;
    public MainActivity ma;

    DealerAdapter dealerAdapter;




    public DealershipFragment() {
        super();


    }

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.base_fragment, container, false);
        setHasOptionsMenu(true);


         mRecycler=new RecyclerView(getContext());
        ((FrameLayout)view.findViewById(R.id.fragmentContainer)).addView(mRecycler);

        importFab=(FloatingActionButton)getActivity().findViewById(R.id.fabbutton);
        importFab.setVisibility(View.VISIBLE);
        importFab.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_plus_white_40dp));

        importFab.setOnClickListener(this);
        db=new ModelDataBase(getContext());

        loadInit();

        return view;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_dealer,menu);
        super.onCreateOptionsMenu(menu, inflater);

        shortItem = menu.findItem(R.id.action_short);

      loadInit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()){


            case R.id.action_sortAlphabetical:
           item.setChecked(true);


                setCustomAdapter(SORT_ALPHABET);


                break;
            case R.id.action_sortType:
             item.setChecked(true);

                setCustomAdapter(SORT_TYPE);


             break;





        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {

        final MenuItem sortAlphabet= menu.findItem(R.id.action_sortAlphabetical);
        final MenuItem sortType= menu.findItem(R.id.action_sortType);

        if(filterMode==0){


            sortAlphabet.setChecked(true);

        }else{

            sortType.setChecked(true);


        }

         setCustomAdapter(filterMode);
        super.onPrepareOptionsMenu(menu);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode== REQUEST_CODE_CONTACT_PHONE && data !=null) {

            final Dealership dealership = new Dealership();
            final getContactInfo contactInfo;
            String ContctUidVar = null;
            String ContctNamVar = getResources().getString(R.string.importNoAsigned_Cat);

            Uri contctDataVar = data.getData();


            //Contact Name and diferent phone number type
            Cursor contctCursorVar = getActivity().getContentResolver().query(contctDataVar, null,
                    null, null, null);
            if (contctCursorVar.getCount() > 0) {
                while (contctCursorVar.moveToNext()) {
                    ContctUidVar = contctCursorVar.getString(contctCursorVar.getColumnIndex(ContactsContract.Contacts._ID));

                    ContctNamVar = contctCursorVar.getString(contctCursorVar.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                    Log.i("Names", ContctNamVar);


                }
                contctCursorVar.close();

                contactInfo = new getContactInfo(ContctUidVar, ContctNamVar, getContext());


                dealership.setName(contactInfo.getName());
                dealership.setPhone(contactInfo.getPhone());
                dealership.setAdress(contactInfo.getAdress());
                dealership.setEmail(contactInfo.getEmail());
                dealership.setCategory(getResources().getString(R.string.importNoAsigned_Cat));
                dealership.setCompany(contactInfo.getCompany());

                if(!db.existDealer(dealership.getName())){
                    db.addDealership(dealership);


                }else{

                     showDialogElection(dealership);


                }

                addcontactdialog.dismiss();


            }

        }
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void loadInit() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setAutoMeasureEnabled(false);

        mRecycler.setLayoutManager(linearLayoutManager);





    }

    public void setActivity(MainActivity ma) {
        this.ma = ma;

    }

    public void setCustomAdapter(  int filterType) {

        filterMode=filterType;

           switch (filterMode){

               case 0:
                dealerAdapter = new DealerAdapter(getHeaderList(SORT_ALPHABET),ma.userData.getDealerShips(),getContext(),this);

                   break;

               case 1:
                dealerAdapter = new DealerAdapter(getHeaderList(SORT_TYPE),ma.userData.getDealerShips(),getContext(),this);

                   break;

           }



         mRecycler.setAdapter(dealerAdapter);
        refreshInfobar(dealerAdapter.contactlist.size());

    }

    public void refreshInfobar(int count) {


             ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();

            String aux = String.valueOf(count);
            aux += " ";
            if(count==1){
                aux += getString(R.string.material_dealership);

            }else{
                aux += getString(R.string.dealership_dealerships);


            }
            actionBar.setTitle(getString(R.string.material_dealership));

            actionBar.setSubtitle(aux);

    }


    ////////////////////////////////////////////////////////////////////////////



/////////////////////////////////////////////////////

    private void showAddDealerPopUp(){

       final Dialog d=new Dialog(getContext());

        ActivityCompat.requestPermissions(ma,new String[]{Manifest.permission.READ_CONTACTS},REQUEST_CODE_CONTACT_PHONE);
        LayoutInflater li = LayoutInflater.from(getContext());

        final View promptsView = li.inflate(R.layout.dealer_search__popup, null);
        ImageView contactphone=(ImageView)promptsView.findViewById(R.id.contacImageView);
        ImageView newContactphone=(ImageView)promptsView.findViewById(R.id.newContactImage);
        contactphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
               calContctPickerFnc(d);
            }
        });


        newContactphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddDealerInfoPopUp(0,false);
                d.dismiss();
            }
        });

        d.setContentView(promptsView);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        d.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

                if(((DealerAdapter)mRecycler.getAdapter()).contactlist.size()<ma.userData.getDealerShipCount()){


                    setCustomAdapter(filterMode);
                }

            }
        });
        d.show();






    }

    private boolean showDialogElection(final Dealership dealership){


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getContext());
        alertDialogBuilder
                .setTitle( getResources().getString(R.string.action_refresh))
                .setMessage( getResources().getString(R.string.warning_duplicate_dealer))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.action_refresh),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                //showAddDealerInfoPopUp(db.getDealerShipByName(dealerName).getId(),true);

                                db.updateDealership(db.getDealerShipByName(dealership.getName()).getId(),dealership.getName(),
                                        dealership.getPhone(),dealership.getAdress(),dealership.getEmail(),dealership.getCategory(),dealership.getCompany(),Utils.getDate());

                                onSaved(dealership,0,true);

                                dialog.dismiss();


                            }
                        }
                )
                .setNegativeButton(getResources().getString(R.string.dialog_cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {


                                dialog.dismiss();
                            }
                        }
                );



        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_black_line));
        alertDialog.show();



        return false;

    }

    private void showAddDealerInfoPopUp(long dealerId,boolean editable){

        AddDealerPopUp addDealerPopUp= AddDealerPopUp.newInstance(Utils.toIntExact(dealerId),editable);

        addDealerPopUp.setSavedInterface(this);
        addDealerPopUp.show(ma.getSupportFragmentManager(),"addDealer");




    }

    private void showRemoveDialog(final SparseBooleanArray checkedList){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getContext());
        alertDialogBuilder
                .setTitle( getContext().getResources().getString(R.string.dialogTitle_remove))
                .setMessage( getContext().getResources().getString(R.string.dialogMsg_remove))
                .setCancelable(false)
                .setPositiveButton(getContext().getResources().getString(R.string.dialog_remove),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {


                                List<Dealership> buffer=new ArrayList<>();
                                if(checkedList.size()>0) {
                                    for(int i=0;i<checkedList.size();i++) {

                                        if(checkedList.valueAt(i)) {
                                            buffer.add(dealerAdapter.contactlist.get(checkedList.keyAt(i)));
                                           db.deleteDealership(dealerAdapter.contactlist.get(checkedList.keyAt(i)).getId());
                                           }
                                    }

                                     setCustomAdapter(0);



                                }

                                if(dealerAdapter.choiceActionMode!=null){
                                    dealerAdapter.choiceActionMode.finish();

                                }


                                showUndoSnackbar(buffer, String.valueOf(buffer.size()) + " " +
                                        getResources().getString(R.string.dealership_dealerships) + " " +  getResources().getString(R.string.dialog_remove));

                            }
                        }
                )
                .setNegativeButton(getContext().getResources().getString(R.string.dialog_cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.cancel();
                            }
                        }
                );
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.bg_black_line));

        alertDialog.show();
    }


    void calContctPickerFnc(Dialog dialog)
    {
        Intent calContctPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);

        startActivityForResult(calContctPickerIntent, REQUEST_CODE_CONTACT_PHONE);
        this.addcontactdialog=dialog;
    }

    private void showUndoSnackbar(final Object obj,String mssg){

        CoordinatorLayout view = (CoordinatorLayout)(((MainActivity)getActivity()).findViewById(R.id.parentLayout));






        Snackbar snackbar = Snackbar.make(view, mssg, Snackbar.LENGTH_LONG)
                .setAction(getContext().getString(R.string.action_undo), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        for(Dealership dealer :((List<Dealership>)obj)){

                            db.addDealership(dealer);
                            setCustomAdapter(0);

                        }

                    }

                }).setCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar snackbar, int dismissType) {
                        super.onDismissed(snackbar, dismissType);

                        if(dismissType !=DISMISS_EVENT_ACTION){


                        }

                    }
                });


        snackbar.show();
    }



    public   ArrayList<Dealership> getHeaderList(int type) {
         ArrayList<Dealership> arrayList=new ArrayList<>();
         List<Dealership> dealerships=ma.userData.getDealerShips();


         final List<String> titleList=new ArrayList<>();


         try {

           if(type==SORT_ALPHABET){

             for (Dealership dealer:dealerships){
                 titleList.add(String.valueOf(dealer.getName().charAt(0)).toUpperCase());

             }


           } else if(type==SORT_TYPE){

               titleList.addAll(ma.userData.getDealersCategories());

               }


             HashSet hs = new HashSet();
             hs.addAll(titleList);
             titleList.clear();
             titleList.addAll(hs);
             Collections.sort(titleList);

             int count=0;
             for(int i=0;i<titleList.size();i++) {

                 Header header = new Header();
                 header.setHeader(titleList.get(i));
                 arrayList.add(header);

             }


         }catch (Exception e){

             Utils.toast(getContext(),e.toString());

         }




      return arrayList;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.fabbutton:

                showAddDealerPopUp();

                break;

        }
    }

    @Override
    public void onSaved(Object obj, int position, boolean isEditable) {

        if(!isEditable){

            setCustomAdapter(filterMode);


        }else{

         setCustomAdapter(filterMode);


        }

        if(dealerAdapter.choiceActionMode!=null){
            dealerAdapter.choiceActionMode.finish();
            dealerAdapter.choiceActionMode=null;

        }

    }

    @Override
    public void onPreview(Object obj) {

    }

    @Override
    public void toRemove(Object obj) {

        showRemoveDialog((SparseBooleanArray)obj);
    }

    @Override
    public void toEdit(long[] ids) {

        showAddDealerInfoPopUp(ids[0],true);
    }

    @Override
    public void onClick(View v, int position, boolean isLongClick) {

    }
}