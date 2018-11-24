package com.colorbuzztechgmail.spf;

import android.content.Context;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.colorbuzztechgmail.spf.popup.waitPoup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CategoryFragment  extends CategoryBaseFragment {

    public static String CHOOSER_TYPE ="fragmentType";
    public static String FRAGMENT_POSITION ="fragmentPosition";

    public enum ChooserType {


        MODEL_CUSTUMER,
        MODEL_DIRECTORY,
        MATERIAL_DEALER,
        MATERIAL_TYPE,
        CUTNOTE_MODEL,
        CUTNOTE_STATUS,
        CUTNOTE_CUSTUMER,
        CUTNOTE_CUTNOTELIST,
        CUTNOTE_REFERENCE



    }
    private RecyclerView mRecycler;
    private CategoryAdapter adapter;
    private int viewMode=0;
    private GridSpacingItemDecoration itemDecoration;
    private int position;
    private boolean viewAll=false;


    public static CategoryFragment newInstance(@NonNull String tag,@NonNull String chooserType,@NonNull int position,OnSelectedCategoryListener listener){


        CategoryFragment f=new CategoryFragment();

        Bundle args = new Bundle();
        args.putString( TAG,tag);
        args.putString(CHOOSER_TYPE,chooserType);
        args.putInt(FRAGMENT_POSITION,position);
        f.setMyTag(tag);
        f.setType(ChooserType.valueOf(chooserType));
        f.setListener(listener);
        f.setArguments(args);


        return f;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter=new CategoryAdapter(getContext(),null,0,null,getListener());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.search_fragment, container, false);
        setHasOptionsMenu(true);

        setupRecycler(view);

        position=getArguments().getInt(FRAGMENT_POSITION);

        if(position==0){

            init();

        }

        view.findViewById(R.id.pathBarContainer).setVisibility(View.VISIBLE);
        ((ImageView)view.findViewById(R.id.imageView6)).setImageDrawable(getContext().getDrawable(R.drawable.ic_keyboard_arrow_down_primary_dark_24dp));
        ((TextView) view.findViewById(R.id.pathTxt)).setText("VER TODO");

        view.findViewById(R.id.pathBarContainer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                viewAll=!viewAll;
                new SearchFilesAsync(viewAll).execute();
                updatePathBar();
            }
        });

        return view;



    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

       menu.clear();



        if(!getType().equals(ChooserType.CUTNOTE_STATUS) && !getType().equals(ChooserType.CUTNOTE_MODEL) &&  !getType().equals(ChooserType.CUTNOTE_CUSTUMER)){

            inflater.inflate(R.menu.menu_add_edit,menu);

        switch (getType()) {

            case MATERIAL_DEALER:
                menu.getItem(0).setIcon(R.drawable.ic_person_add_white_24dp);
                break;



            case MODEL_CUSTUMER:
                menu.getItem(0).setIcon(R.drawable.ic_person_add_white_24dp);

                break;

            case MODEL_DIRECTORY:
                menu.getItem(0).setIcon(R.drawable.ic_create_new_folder_white_24dp);

                break;

            case MATERIAL_TYPE:


                break;
        }

        }
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()){


            case R.id.action_add:

                showAddNewCategory(adapter);

                return true;

            case R.id.action_editCategory:

               showEditCategory();

                return true;



        }

        return false;
    }

    private void setupRecycler(View v){

        mRecycler=v.findViewById(R.id.recyclerView);

        customView(viewMode);


    }

    public void setupAdapter(List<Object> header) {

        if(adapter.getItemCount()>0 && adapter!=null){

            adapter.removeAll();

        }

        mRecycler.setAdapter(adapter = new CategoryAdapter(getContext(),header, viewMode,getType().name(),getListener()));


    }

    public void customView(int mode){

        //Utils.toast(getContext(),"N View:" + String.valueOf(nView));

        int nColumn=1;

        final int scrWidth=getContext().getResources().getDisplayMetrics().widthPixels;
        if(itemDecoration!=null) {
            mRecycler.removeItemDecoration(itemDecoration);

        }


        if (mode==0) {  ///ListView






            nColumn = 1;

            final LinearLayoutManager lm = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            lm.setAutoMeasureEnabled(false);
            mRecycler.setLayoutManager(lm);



        } else if (mode==1) {///GridView




            nColumn = 2;


            final GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), nColumn);


            itemDecoration = new GridSpacingItemDecoration(nColumn, 8, true);

            mRecycler.addItemDecoration(itemDecoration);
            mRecycler.setLayoutManager(gridLayoutManager);





        }


    }

    @Override
    protected void init() {

        new SearchFilesAsync(viewAll).execute();

    }

    public static class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ItemViewHolder> {


        private static int LIST = 0;
        private static int GRID = 1;
        private OnSelectedCategoryListener listener;
        List<Object> files;
        int viewMode = 0;
        Context context;
        ChooserType type;

        private static final Comparator<Object> ALPHABETICAL_COMPARATOR = new Comparator<Object>() {


            @Override
            public int compare(Object lhs, Object rhs) {


                return (((Category) lhs).getName()).compareToIgnoreCase(((Category) rhs).getName());


            }
        };

        public void add(Object object){


            files.add(object);
            refresh();
        }

        public int getViewMode() {
            return viewMode;
        }

        public void setViewMode(int viewMode) {
            this.viewMode = viewMode;
        }


        public CategoryAdapter(Context context, List<Object> files, int viewMode, String type, OnSelectedCategoryListener listener) {
            super();

            setViewMode(viewMode);

            this.listener = listener;

            this.files = new ArrayList<>();
            if (files != null) {
                this.type = ChooserType.valueOf(type);

                this.files.addAll(files);
                refresh();

            }
            this.context = context;


        }

        private void refresh() {


            Collections.sort(files, ALPHABETICAL_COMPARATOR);
            notifyDataSetChanged();


        }

        private void removeAll(){
            notifyItemRangeRemoved(0,files.size()-1);

            files.clear();

        }

        public void clear() {


            if (files.size() > 0) {
                files.clear();

            }


        }


        @Override
        public int getItemViewType(int position) {

            return viewMode;


        }

        @Override
        public int getItemCount() {
            return files.size();
        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
            View v;
            if (viewType == GRID) {
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.folder_item, parent, false);

                // set the view's size, margins, paddings and layout parameters

                return new ItemViewHolder(v);

            } else if (viewType == LIST) {
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.folder_file_listview, parent, false);
                // set the view's size, margins, paddings and layout parameters
                return new ItemViewHolder(v);
            }

            return null;

        }

        @Override
        public void onBindViewHolder(ItemViewHolder holder, int position) {


            final Category value = (Category) files.get(position);
            holder.image.setImageDrawable(getImage(position));
            Utils.setMargins(holder.image, 10, 10, 10, 10);
            holder.folderText.setText(value.getName());
            holder.countText.setText(String.valueOf(value.getItemCount() + " " + getSubText(value.getItemCount())));
            holder.itemView.findViewById(R.id.imageView).setVisibility(View.INVISIBLE);
            holder.setItemClickListener(new ItemClickListener() {
                @Override
                public void onClick(View v, int position, boolean isLongClick) {

                    if (!isLongClick) {

                        if (listener != null) {
                            listener.OnSelectedCategory(value.getName(), type);

                        }

                    }

                }
            });

        }

        public static class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
            // each data item is just a string in this case

            private ItemClickListener itemClickListener;
            private TextView folderText;
            private TextView countText;
            private ImageView image;

            public ItemViewHolder(View v) {
                super(v);

                folderText = (TextView) v.findViewById(R.id.pathText);
                countText = (TextView) v.findViewById(R.id.countTxt);
                image = (ImageView) v.findViewById(R.id.imageView2);
                v.setOnLongClickListener(this);
                v.setOnClickListener(this);


            }

            public void setItemClickListener(ItemClickListener itemClickListener) {

                this.itemClickListener = itemClickListener;

            }

            @Override
            public void onClick(View v) {
                if (itemClickListener != null)

                    itemClickListener.onClick(v, getAdapterPosition(), false);
            }

            @Override
            public boolean onLongClick(View v) {
                if (itemClickListener != null)
                    itemClickListener.onClick(v, getAdapterPosition(), true);
                return false;
            }
        }

        private Drawable getImage(int pos) {

            switch (type) {


                case MODEL_DIRECTORY:

                    return context.getDrawable(R.drawable.ic_folder_selector);


                case MATERIAL_DEALER:

                    return context.getDrawable(R.drawable.ic_person_selector);


                case MODEL_CUSTUMER:
                    return context.getDrawable(R.drawable.ic_person_selector);


                case CUTNOTE_STATUS:

                    return Utils.getImageAtStatus(context, Utils.convertStringToStatus(context, ((Category) files.get(pos)).getName()));


                case MATERIAL_TYPE:
                    return context.getDrawable(R.drawable.ic_leather_selector);


                case CUTNOTE_MODEL:

                    return context.getDrawable(R.drawable.ic_shoe_selector);


                case CUTNOTE_CUSTUMER:
                    return context.getDrawable(R.drawable.ic_person_selector);

            }

            return null;

        }

        private String getSubText(int count) {


            switch (type) {


                case MODEL_DIRECTORY:


                    if (count != 1) {

                        return context.getString(R.string.modelosText);

                    }

                    return context.getString(R.string.model);


                case MATERIAL_DEALER:


                    if (count != 1) {

                        return context.getString(R.string.materialsTxt);

                    }

                    return context.getString(R.string.materialTxt);


                case MODEL_CUSTUMER:

                    if (count != 1) {

                        return context.getString(R.string.modelosText);

                    }

                    return context.getString(R.string.model);


                case CUTNOTE_STATUS:


                    if (count != 1) {

                        return context.getString(R.string.cutNotesLists);

                    }

                    return context.getString(R.string.cutNotesList);

                case MATERIAL_TYPE:

                    if (count != 1) {

                        return context.getString(R.string.materialsTxt);

                    }

                    return context.getString(R.string.materialTxt);


                case CUTNOTE_MODEL:

                    if (count != 1) {

                        return context.getString(R.string.cutNotesLists);

                    }

                    return context.getString(R.string.cutNotesList);

                case CUTNOTE_CUSTUMER:


                    if (count != 1) {

                        return context.getString(R.string.cutNotesLists);

                    }

                    return context.getString(R.string.cutNotesList);



                default:

                    return "Archivos";


            }


        }

    }

    public class SearchFilesAsync extends AsyncTask<Void, Float, List<Object>> {

            waitPoup waitPoup;
            ModelDataBase db;
            List<Object> buffer;
            boolean all;

            public SearchFilesAsync(boolean all) {
                super();
                waitPoup = com.colorbuzztechgmail.spf.popup.waitPoup.newInstance(getString(R.string.dialog_searchFiles) /*+ '\n' + getType().name().toLowerCase()*/);

                waitPoup.show(getFragmentManager(), "waitPopup");

                db = new ModelDataBase(getContext());
                buffer = new ArrayList<>();
                this.all=all;


            }

            protected void onPreExecute() {


            }

            protected List<Object> doInBackground(Void... params) {

                try {


                    switch (getType()) {


                        case CUTNOTE_MODEL:

                            buffer.addAll(db.getCutNoteListModelCategory(all));
                            return buffer;

                        case CUTNOTE_CUSTUMER:

                            buffer.addAll(db.getCutNoteListCustumerCategory(all));
                            return buffer;


                        case MATERIAL_TYPE:


                            buffer.addAll(db.getMaterialsTypesCategory(all));
                            return buffer;


                        case CUTNOTE_STATUS:


                            buffer.addAll(db.getCutNoteStatusCategory());


                            return buffer;

                        case MODEL_CUSTUMER:


                            buffer.addAll(db.getCustumerCategory(all));

                            return buffer;

                        case MATERIAL_DEALER:


                            buffer.addAll(db.getDealersCategory(all));

                            return buffer;

                        case MODEL_DIRECTORY:

                            buffer.addAll(db.getCategoryClass(all));

                            return buffer;


                    }


                } catch (Exception e) {

                    e.printStackTrace();
                    Log.e("Background", e.toString());


                }
                return null;
            }

            protected void onProgressUpdate(Float... valores) {
                int p = Math.round(valores[0]);
                // ma.mProgressBar.setProgress(p);
            }

            @Override
            protected void onPostExecute(List<Object> headers) {

                waitPoup.dismiss();

                if (headers != null) {

                    setupAdapter(headers);
                } else {

                    Utils.toast(getContext(), "No se encontaron archivos");
                }


            }

        }

    public interface OnSelectedCategoryListener {


            void OnSelectedCategory(String category, ChooserType type);
        }

    private void updatePathBar() {

        Drawable drawable=null;

        if (!viewAll) {
            ((TextView) getView().findViewById(R.id.pathTxt)).setText("VER TODO");
             drawable=getContext().getDrawable(R.drawable.ic_keyboard_arrow_down_primary_dark_animatable_counter_clockwise);

        } else {

            ((TextView) getView().findViewById(R.id.pathTxt)).setText("VER MENOS");
            drawable=getContext().getDrawable(R.drawable.ic_keyboard_arrow_down_primary_dark_animatable_clockwise);


        }
        ((ImageView)getView().findViewById(R.id.imageView6)).setImageDrawable(drawable);

        (((AnimatedVectorDrawable)((ImageView)getView().findViewById(R.id.imageView6)).getDrawable())).start();



    }


}
