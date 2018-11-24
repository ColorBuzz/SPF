package com.colorbuzztechgmail.spf;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by PC01 on 21/06/2017.
 */
public class TabFragment1 extends Fragment implements View.OnTouchListener,ItemActionListener{

    public final static String MODEL_ID="modelId";
    private static final String TAB="TabFragment1";
    public RecyclerView recyclerPiece;
    private FrameLayout pieceFrame,infoContainer;
    MaterialinfoContainer materialinfoContainer;
    waitPoup waitPoup;
    Toolbar toolbar;

    int modelId;
    private FloatingActionButton addPiece;
     public static TabFragment1 newInstance(@NonNull int modelId ) {
        TabFragment1 f = new TabFragment1();
        Bundle args = new Bundle();

        args.putInt(MODEL_ID,modelId);



        f.setArguments(args);


        return f;
    }


    private static final Comparator<Piece> ALPHABETICAL_COMPARATOR_PIECE_MATERIAL = new Comparator<Piece>() {
        @Override
        public int compare(Piece a, Piece b) {
            return a.getMaterial().toLowerCase().compareTo(b.getMaterial().toLowerCase());
        }
    };

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        int screenWidth = getResources().getDisplayMetrics().widthPixels;

        int cardWidth = (int) getContext().getResources().getDimension(R.dimen.card_witdh);



        final View view=inflater.inflate(R.layout.tab_fragment1,container,false);
        toolbar=(Toolbar)view.findViewById(R.id.pieceToolbar);
        toolbar.inflateMenu(R.menu.menu_piece);
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_up_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pieceFrame.getVisibility()==View.VISIBLE){

                    pieceFrame.setVisibility(View.GONE);

                }else{
                    pieceFrame.setVisibility(View.VISIBLE);

                }
             }
        });

        LayoutInflater li=LayoutInflater.from(getContext());

        View v=li.inflate(R.layout.spinneritem,null);

        ImageView imp= new ImageView(getContext());

        imp.setLayoutParams(new LinearLayout.LayoutParams( 80, ViewGroup.LayoutParams.MATCH_PARENT));

        imp.setPadding(0,0,16,0);

        imp.setId(R.id.action);
        imp.setImageDrawable(getResources().getDrawable(R.drawable.ic_more_vert_black_24dp));
        imp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenuListPopupWindow(v);
            }
        });
        ((MenuItem)toolbar.getMenu().findItem(R.id.action_menu)).setActionView(imp);

        pieceFrame=(FrameLayout)view.findViewById(R.id.previewFrame);
        recyclerPiece=(RecyclerView)view.findViewById(R.id.recyclerPiece);
        materialinfoContainer = new MaterialinfoContainer(getContext(), MaterialinfoContainer.MODE_SUBMENU_PIECE);
        final LinearLayoutManager lm = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        lm.setSmoothScrollbarEnabled(true);
        recyclerPiece.setLayoutManager(lm);


        infoContainer=(FrameLayout) view.findViewById(R.id.infoContainer);
   /*     addPiece=(FloatingActionButton)view.findViewById(R.id.addFloating);
        addPiece.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddObjectDialog();
            }
        });

        addPiece.setOnDragListener(new MyDragListener());*/


        infoContainer.setOnTouchListener(this);




        infoContainer.removeAllViews();
        infoContainer.addView(materialinfoContainer.getView());


        modelId = ((ModelActivity) getActivity()).model.getId();




        Object[] obj=new Object[1];

        obj[0]=modelId;


        new loadPiecesDetailAsync().execute(obj);


        return view;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onStart() {


        super.onStart();

    }

    private void loadMenu(Piece piece){





        toolbar.setTitle(piece.getName() + "-" + piece.getSize()+ "-" +  piece.getMaterial());







    }

    public void loadPieces (List<Piece> pieces) {



/*


        RecyclerSliderAdapter mAdapter = new RecyclerSliderAdapter(getContext(),pieces,this);
        mAdapter.setListInterface(this);
        recyclerItems.addItemDecoration(new HeaderHorizontalItemDecoration(recyclerItems,  mAdapter));
        recyclerItems.setAdapter(mAdapter);

        ItemTouchHelper.Callback callback =
                new SimpleItemTouchHelperCallback(mAdapter,LinearLayout.HORIZONTAL);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerItems);
        //Utils.toast(mModelFragment.getContext(),String.valueOf(view.findViewById(R.id.frameExpand).getWidth()));





         recyclerItems.getLayoutManager().scrollToPosition(0);

*/


    }

    public void showEditPiece(String name ,int pieceId,int modelId){



        PieceEditPopUp mpopUp= PieceEditPopUp.newInstance(name,pieceId,modelId);

        mpopUp.show(getFragmentManager(),"editPiece");

    }

    public   void loadPieceList(String pieceId, int mode,int modelId,String material){


        PieceListPopUp pieceListPopUp = PieceListPopUp.newInstance(pieceId,mode,modelId,material );

        pieceListPopUp.show( getActivity().getSupportFragmentManager(),"pieceListDialog");



    }

    public void updateDataChange(Object object) {

        Boolean enabled=true;
/*
        final PieceCardAdapter adapter = (PieceCardAdapter) mRecycler.getAdapter();
        ((Piece) object).setSize(adapter.getPiece( 0).getSize());
        adapter.replaceItem((Piece) object);

        View v = ((CardSliderLayoutManager) adapter.getLayoutManager()).getInfoContainer();
        final MaterialinfoContainer materialinfoContainer = new MaterialinfoContainer(getContext(),MaterialinfoContainer.MODE_SUBMENU_PIECE);
        materialinfoContainer.setView(v);
        materialinfoContainer.startAnimation(MaterialinfoContainer.MODE_SUBMENU_PIECE, enabled, null, (Piece) object);*/

    }

    public  void showAddObjectDialog( ) {

        final PopupMenu popup = new PopupMenu(getContext(), addPiece);
        MenuInflater inflater = popup.getMenuInflater();
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()){

                    case R.id.action_addpiece:

                       // showAddMaterialPopup(modelId);

                        return true;

                    case R.id.action_movepiece:

                       PieceMaterialAsigmentPopUp pieceMaterialAsigment = PieceMaterialAsigmentPopUp.newInstance(modelId);

                       pieceMaterialAsigment.show(getActivity().getSupportFragmentManager(),"movePiece");
                        return true;
                }
                return false;
            }
        });



        //menuClick=position;
        inflater.inflate(R.menu.submenu_adapter, popup.getMenu());
        popup.getMenu().getItem(1).setVisible(true);
        popup.getMenu().getItem(2).setVisible(true);

        popup.show();




    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {





          //  ((WrapContentHeightViewPager) ((ModelActivity) getActivity()).sViewpager).setPaginEnabled(true);

        return true;
    }


    @Override
    public void onPreview(Object obj) {


        loadMenu((Piece)obj);
        pieceFrame.removeAllViews();
        int padding=(int)getContext().getResources().getDimension(R.dimen.padding);
        final DrawView drawView = new DrawView(getContext(), (Piece)obj,pieceFrame, getResources().getDisplayMetrics().widthPixels,
                pieceFrame.getHeight()- (padding*2) );


        materialinfoContainer.startAnimation(MaterialinfoContainer.MODE_SUBMENU_PIECE, true,null, (Piece)obj);

    }

    @Override
    public void toRemove(Object obj) {

    }

    @Override
    public void toEdit(int position) {

    }

    @Override
    public void onClick(View v, int position, boolean isLongClick) {

    }

    public class loadPiecesDetailAsync extends AsyncTask<Object, Float,List<Piece>> {

        protected void onPreExecute() {

           if( ((ModelActivity)getActivity()).sViewpager.getCurrentItem()==0) {

               waitPoup = com.colorbuzztechgmail.spf.waitPoup.newInstance(getString(R.string.dialog_loadModel));

               waitPoup.show(getFragmentManager(), "waitPopup");
           }

        }

        protected List<Piece> doInBackground(Object[] ids) {


            //pos 0 -modelID
            //pos 1 -pieces size list

             int modelId=(Integer)ids[0];

            ModelDataBase db = new ModelDataBase(getContext());
            List<Piece>pieceTable=new ArrayList<>();
            List<Piece>pieces=new ArrayList<>();
            List<Material>materials=new ArrayList<>();

            try {




                final PreviewModelInfo model = db.getPreviewModel(modelId);
                final String size=model.getMinSize() +"-"+model.getMaxSize();

                materials=db.getModelMaterial(modelId);

                pieces=db.getHighLightPieces(modelId,true);

                // toolbar.setTitle(String.valueOf(materials.size()) +" "  + getString(R.string.materialsTxt));

                for(Material material:materials) {

                    Drawable img=db.getCustomMaterialImage(Integer.valueOf(material.getCustomMaterialId()));

                    final PieceTitle mTitle=new PieceTitle(material.getName(), material.getId(),modelId,img);
                    if(mTitle.getImage() !=null) {

                        final Drawable drawable = (new ShapeGenerator(getContext()).getDrawableShape(mTitle.getImage(),
                                ShapeGenerator.MODE_ROUND_RECT, R.color.colorLightPrimary, ShapeGenerator.SIZE_SMALL));
                        mTitle.setImage(drawable);
                    }

                    for(int i=pieces.size()-1;i>=0;i--){

                        if (pieces.get(i).getMaterial().equals(material.getName())) {
                            pieces.get(i).setSize(size);

                            mTitle.addPiece(pieces.get(i));
                            pieces.remove(i);

                        }

                    }
                    pieceTable.add(mTitle);


                }




                return pieceTable;

            } catch (Exception e) {

                e.printStackTrace();
                Log.e("Background", e.toString());


            }
            return pieceTable;
        }

        protected void onProgressUpdate(Float... valores) {
            int p = Math.round(valores[0]);
            // ma.mProgressBar.setProgress(p);
        }

        @Override
        protected void onPostExecute(List<Piece> pieces) {

            if(waitPoup!=null){
                waitPoup.dismiss();

            }


            if(!pieces.isEmpty()){

                loadPieces(pieces);



            }else {

                Utils.toast(getContext(),"No se encontaron piezas");
            }


        }
    }

    public class MyDragListener implements View.OnDragListener {
        Drawable removeImage=getContext().getResources().getDrawable(
                R.drawable.ic_delete_white_24dp);

        Drawable enterShape = getContext().getResources().getDrawable(
                R.drawable.ic_delete_up_white_24dp);
        Drawable normalShape = getContext().getResources().getDrawable(R.drawable.ic_menu_white_24dp);

        @Override
        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();
            switch (event.getAction()) {


                case DragEvent.ACTION_DRAG_STARTED:
                    ((FloatingActionButton)v).setImageDrawable(removeImage);
                    Log.e("Drag","DRAG_START" );

                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    ((FloatingActionButton)v).setImageDrawable(enterShape);
                    Log.e("Drag","DRAG_ENTERED" );

                    //v.setBackgroundDrawable(enterShape);
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    ((FloatingActionButton)v).setImageDrawable(removeImage);

                    Log.e("Drag","DRAG_EXITED" );
                    break;
                case DragEvent.ACTION_DROP:
                    // Dropped, reassign View to ViewGroup
                    /*View view = (View) event.getLocalState();
                    ViewGroup owner = (ViewGroup) view.getParent();
                    owner.removeView(view);
                    LinearLayout container = (LinearLayout) v;
                    container.addView(view);
                    view.setVisibility(View.VISIBLE);
                    */

                    Log.e("Drag","DRAG_DROP" );

                    break;

                case DragEvent.ACTION_DRAG_LOCATION: {
                    Log.e("Drag","DRAG_LOCATION" );

                        int x = Math.round(event.getX());
                        int translatedX = x;
                        int threshold = 50;
                        // make a scrolling up due the y has passed the threshold
                        if (translatedX < threshold) {
                            // make a scroll up by 30 px
                         }
                        // make a autoscrolling down due y has passed the 500 px border
                        if (translatedX >(500)) {
                            // make a scroll down by 30 px
                         }


                    // listen for more actions here
                    // ...
                }

                break;
                case DragEvent.ACTION_DRAG_ENDED:
                    ((FloatingActionButton)v).setImageDrawable(normalShape);
                    View item = (View) event.getLocalState();
                    item.setVisibility(View.VISIBLE);

                    Log.e("Drag","DRAG_ENDED" );

                default:
                    break;
            }
            return true;
        }
    }

    private void showMenuListPopupWindow(View anchor) {
        final ListPopupWindow popupWindow = new ListPopupWindow(getContext());

        List<ListPopUpMenuItem> itemList = new ArrayList<>();
        itemList.add(new ListPopUpMenuItem(getString(R.string.action_save_as), R.drawable.ic_insert_drive_file_black_24dp));
        itemList.add(new ListPopUpMenuItem(getString(R.string.action_print), R.drawable.ic_print_black_24dp));
        itemList.add(new ListPopUpMenuItem(getString(R.string.action_send), R.drawable.ic_share_black_24dp));


        ListPopupWindowAdapter adapter = new ListPopupWindowAdapter(getContext(), itemList);
        popupWindow.setAnchorView(anchor);
        popupWindow.setListSelector(getResources().getDrawable(R.drawable.checkedlayout));
        popupWindow.setContentWidth((int) (150 * getResources().getDisplayMetrics().density + 0.5f));
        popupWindow.setAdapter(adapter);
        popupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                popupWindow.dismiss();

                switch (i){

                    case 0://Guardar
                        //showSaveType();
                        //showShare();
                        break;
                    case 1://Imprimir
                        Utils.toast(getContext(),getString(R.string.action_print));
                        break;
                    case 2://Enviar
                       // ((MainActivity)getActivity()).sendImage(piece);
                        break;


                }


            }
        });
        popupWindow.show();
    }


}
