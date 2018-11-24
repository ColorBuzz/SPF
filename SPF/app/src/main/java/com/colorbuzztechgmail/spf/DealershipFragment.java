package com.colorbuzztechgmail.spf;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;


/**
 * Created by PC01 on 21/06/2017.
 */
public class DealershipFragment extends Fragment implements View.OnClickListener {

    MenuItem shortItem;

    public static final int SORT_ALPHABET=0;

    public static final int SORT_TYPE=1;






    public List<Dealership> bufferDealer = new ArrayList<>();

    //Display
    public RecyclerView mRecycler;

    private GridSpacingItemDecoration itemDecoration;

    public int filterMode = SORT_ALPHABET;
    public MainActivity ma;

    DealerAdapter dealerAdapter;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public DealershipFragment() {
        super();


    }

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.model_fragment, container, false);
        setHasOptionsMenu(true);


         mRecycler=new RecyclerView(getContext());
        ((FrameLayout)view.findViewById(R.id.modelFragmentContainer)).addView(mRecycler);

        ((FloatingActionButton)view.findViewById(R.id.fabbutton)).setOnClickListener(this);

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


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void loadInit() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

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

        if (this.isVisible()) {
            AppCompatActivity activity = (AppCompatActivity) getActivity();
            ActionBar actionBar = activity.getSupportActionBar();

            String aux = String.valueOf(count);
            aux += " ";
            if(count==1){
                aux += getString(R.string.material_dealership);

            }else{
                aux += getString(R.string.action_sortDealer);


            }
            actionBar.setTitle(getString(R.string.action_sortDealer));

            actionBar.setSubtitle(aux);
        }
    }


    ////////////////////////////////////////////////////////////////////////////



/////////////////////////////////////////////////////

    private void showAddDealerPopUp(){

       final Dialog d=new Dialog(getContext());

        ActivityCompat.requestPermissions(ma,new String[]{Manifest.permission.READ_CONTACTS},ma.REQUEST_CODE_CONTACT_PHONE);
        LayoutInflater li = LayoutInflater.from(getContext());

        final View promptsView = li.inflate(R.layout.dealer_search__popup, null);
        ImageView contactphone=(ImageView)promptsView.findViewById(R.id.contacImageView);
        ImageView newContactphone=(ImageView)promptsView.findViewById(R.id.newContactImage);
        contactphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
               ma. calContctPickerFnc(d);
            }
        });


        newContactphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddDealerInfoPopUp();
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

    private void showAddDealerInfoPopUp(){

        AddDealerPopUp addDealerPopUp= AddDealerPopUp.newInstance(0,false);
        addDealerPopUp.show(ma.getSupportFragmentManager(),"addDealer");



            addDealerPopUp.setDimissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    if(((DealerAdapter)mRecycler.getAdapter()).contactlist.size()<ma.userData.getDealerShipCount()){


                        setCustomAdapter(filterMode);
                    }
                }
            });





/*        final Dialog d=new Dialog(getContext());

        ActivityCompat.requestPermissions(ma,new String[]{Manifest.permission.READ_CONTACTS},ma.REQUEST_CODE_CONTACT_PHONE);
        LayoutInflater li = LayoutInflater.from(getContext());

        final View promptsView = li.inflate(R.layout.add_dealer_popup, null);
        final Button backButton=(Button)promptsView.findViewById(R.id.backbtn);
        final Button closeButton=(Button) promptsView.findViewById(R.id.close_button);
        final Button saveButton=(Button) promptsView.findViewById(R.id.save_btn);

        backButton.setBackground(getContext().getDrawable(R.drawable.ic_person_grey_24dp));

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        d.show();*/

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
}