package com.colorbuzztechgmail.spf;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import com.colorbuzztechgmail.spf.CutNote.cutNoteStatus;

/**
 * Created by PC01 on 29/11/2017.
 */

public class ModelDataBase extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "blog.db";
    public static final int DATABASE_VERSION = 1;

    public enum RelationType{
        DEALER,
        CUTNOTE,
        EXTERNAL,
        ACCESSORY
    }


    //// MODEL TABLE
    public static final String TABLE_NAME = "model";
    public static final String MODEL_COLUMN_ID = "_id"; //0
    public static final String MODEL_COLUMN_NAME = "name";//1
    public static final String MODEL_COLUMN_PIECES = "pieces";//2
    public static final String MODEL_COLUMN_COUNT = "piececount";//3
    public static final String MODEL_COLUMN_SIZES = "sizes";//4
    public static final String MODEL_COLUMN_BASE_SIZE = "base_size";//5
    public static final String MODEL_COLUMN_MATERIAL_COUNT = "materialcount";//6
    public static final String MODEL_COLUMN_PARENT = "parent";//7
    public static final String MODEL_COLUMN_IMAGE = "image";//8
    public static final String MODEL_COLUMN_CATEGORY = "category";//9
    public static final String MODEL_COLUMN_DATE = "date";//10
    public static final String MODEL_COLUMN_DESCRIPT = "description";//11

    //// PIECE MODEL TABLE
    public static final String TABLE_PIECE = "pieces_";
    public static final String PIECE_COLUMN_ID = "_id"; //0
    public static final String PIECE_COLUMN_MODEL_ID = "modelId"; //1
    public static final String PIECE_COLUMN_NAME = "name";//2
    public static final String PIECE_COLUMN_AMOUNT = "amount";//3
    public static final String PIECE_COLUMN_AMOUNT_MIRROR = "amount_mirror";//4
    public static final String PIECE_COLUMN_SIZE = "size";//5
    public static final String PIECE_COLUMN_MATERIAL = "material";//6
    public static final String PIECE_COLUMN_AMOUNT_MATERIAL = "amount_material";//7
    public static final String PIECE_COLUMN_TOOLS = "tools";//8
    public static final String PIECE_COLUMN_IMAGE = "image";//9
    public static final String PIECE_COLUMN_DESCRIPT = "description";//11
    public static final String PIECE_COLUMN_RAWDATA = "rawdata";//12


    ///MATERIAL TABLE


    public static final String TABLE_MATERIAL = "materials_";
    public static final String MATERIAL_COLUMN_ID = "_id"; //0
    public static final String MATERIAL_COLUMN_MODEL_ID = "modelId"; //1
    public static final String MATERIAL_COLUMN_NAME = "name";//2
    public static final String MATERIAL_COLUMN_ASIGNED = "custom";//3
    public static final String MATERIAL_COLUMN_COLOR = "color";//4
    public static final String MATERIAL_COLUMN_PIECES_ID = "pieces_id";//5



    ///  MATERIAL CUSTOM TABLE


    public static final String TABLE_CUSTOM_MATERIAL = "custom_material";
    public static final String CUSTOM_MATERIAL_COLUMN_ID = "_id";//0
    public static final String CUSTOM_MATERIAL_COLUMN_NAME = "name";//1
    public static final String CUSTOM_MATERIAL_COLUMN_TYPE = "type";//2
    public static final String CUSTOM_MATERIAL_COLUMN_FEETS = "feets";//3
    public static final String CUSTOM_MATERIAL_COLUMN_METERS = "meters";//4
    public static final String CUSTOM_MATERIAL_COLUMN_DEALERSHIP = "dealership";//5
    public static final String CUSTOM_MATERIAL_COLUMN_SEASONS = "seasons";//6
    public static final String CUSTOM_MATERIAL_COLUMN_IMAGE = "image";//7
    public static final String CUSTOM_MATERIAL_COLUMN_DATE = "date";//8
    public static final String CUSTOM_MATERIAL_COLUMN_DESCRIPTION = "description";//9
    public static final String CUSTOM_MATERIAL_COLUMN_MODEL_RELATION="modelsRelation";//10


    ///  DEALERSHIP TABLE

    public static final String TABLE_DEALERSHIP = "dealership";
    public static final String DEALERSHIP_COLUMN_ID = "_id";//0
    public static final String DEALER_COLUMN_NAME = "name";//1
    public static final String DEALER_COLUMN_PHONE = "phone";//2
    public static final String DEALER_COLUMN_ADRESS = "adress";//3
    public static final String DEALER_COLUMN_EMAIL = "email";//4
    public static final String DEALER_COLUMN_CATEGORY = "category";//5
    public static final String DEALER_COLUMN_COMPANY = "company";//6
    public static final String DEALER_COLUMN_DATE = "date";//7

    /// EXTERNALWORKS  TABLE

    public static final String TABLE_EXTERNALWORKS = "external_works";
    public static final String EXTERNALWORKS_COLUMN_ID = "_id";//0
    public static final String EXTERNALWORKS_COLUMN_MODEL_ID = "modelId";//1
    public static final String EXTERNALWORKS_COLUMN_PIECE_ID = "pieceId";//2
    public static final String EXTERNALWORKS_COLUMN_NAME = "external";//3
    public static final String EXTERNALWORKS_COLUMN_TYPE = "type";//4
    public static final String EXTERNALWORKS_COLUMN_DESCRIPTION = "description";//5
    public static final String EXTERNALWORKS_COLUMN_DATE = "date";//6
    public static final String EXTERNALWORKS_COLUMN_COMPLETED = "completed";//7

    /// CUTNOTES TABLE


    public static final String TABLE_CUTNOTE = "cutnotes";
    public static final String CUTNOTE_COLUMN_ID = "_id";//0
    public static final String CUTNOTE_COLUMN_MODEL_ID = "modelId";//1
    public static final String CUTNOTE_COLUMN_MODEL_NAME = "modelName";//2
    public static final String CUTNOTE_COLUMN_REFERENCE = "reference";//3
    public static final String CUTNOTE_COLUMN_NOTES = "notes";//4
    public static final String CUTNOTE_COLUMN_STATUS = "status";//4
    public static final String CUTNOTE_COLUMN_DATE = "date";//5





    /// ACCESSORIES  TABLE

    public static final String TABLE_ACCESSORIES = "accessories";
    public static final String ACCESSORIES_COLUMN_ID = "_id";//0
    public static final String ACCESSORIES_COLUMN_NAME = "_name";//1
    public static final String ACCESSORIES_COLUMN_MODEL_ID = "modelId";//2
    public static final String ACCESSORIES_COLUMN_DEALER = "dealer";//3
    public static final String ACCESSORIES_COLUMN_TYPE = "type";//4
    public static final String ACCESSORIES_COLUMN_DESCRIPTION = "description";//5
    public static final String ACCESSORIES_COLUMN_DATE = "date";//6
    public static final String ACCESSORIES_COLUMN_IMAGE = "image";//7


    /// RELATIONS  TABLE

    public static final String TABLE_RELATIONS= "relations";
    public static final String RELATIONS_COLUMN_ID = "_id";//0
    public static final String RELATIONS_COLUMN_MODEL_ID = "modelId";//1
    public static final String RELATIONS_COLUMN_DEALERS = "dealersId";//2
    public static final String RELATIONS_COLUMN_CUTNOTES = "cutnotesId";//3
    public static final String RELATIONS_COLUMN_EXTERNALS = "externalsId";//4
    public static final String RELATIONS_COLUMN_ACCESSORIES = "accessoriesId";//5



    ///CATEGORY TABLE

    public static final String TABLE_CATEGORY = "category";

    private Context mContext;
    public int Version;


    public ModelDataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context;

    }


    @Override
    public void onOpen(SQLiteDatabase db) {

        super.onOpen(db);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        Version = db.getVersion();

        db.execSQL("CREATE TABLE " + TABLE_NAME + "(" +
                MODEL_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                MODEL_COLUMN_NAME + " TEXT, " +
                MODEL_COLUMN_PIECES + " BLOB, " +
                MODEL_COLUMN_COUNT + " INTEGER, " +
                MODEL_COLUMN_SIZES + " BLOB, " +
                MODEL_COLUMN_BASE_SIZE + " TEXT, " +
                MODEL_COLUMN_MATERIAL_COUNT + " INTEGER, " +
                MODEL_COLUMN_PARENT + " TEXT, " +
                MODEL_COLUMN_IMAGE + " BLOB, " +
                MODEL_COLUMN_CATEGORY + " TEXT, " +
                MODEL_COLUMN_DATE + " TEXT, " +
                MODEL_COLUMN_DESCRIPT + " TEXT)"
        );

        db.execSQL("CREATE TABLE " + TABLE_CATEGORY + "(" +
                MODEL_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                MODEL_COLUMN_NAME + " TEXT)"
        );

        db.execSQL("CREATE TABLE " + TABLE_CUSTOM_MATERIAL + "(" +
                CUSTOM_MATERIAL_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                CUSTOM_MATERIAL_COLUMN_NAME + " TEXT, " +
                CUSTOM_MATERIAL_COLUMN_TYPE + " TEXT, " +
                CUSTOM_MATERIAL_COLUMN_FEETS + " FLOAT, " +
                CUSTOM_MATERIAL_COLUMN_METERS + " FLOAT, " +
                CUSTOM_MATERIAL_COLUMN_DEALERSHIP + " TEXT, " +
                CUSTOM_MATERIAL_COLUMN_SEASONS + " TEXT, " +
                CUSTOM_MATERIAL_COLUMN_IMAGE + " BLOB, " +
                CUSTOM_MATERIAL_COLUMN_DATE + " TEXT, " +
                CUSTOM_MATERIAL_COLUMN_DESCRIPTION + " TEXT, " +
                CUSTOM_MATERIAL_COLUMN_MODEL_RELATION + " BLOB)"

        );


        db.execSQL("CREATE TABLE " + TABLE_DEALERSHIP + "(" +
                DEALERSHIP_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                DEALER_COLUMN_NAME + " TEXT, " +
                DEALER_COLUMN_PHONE + " TEXT, " +
                DEALER_COLUMN_ADRESS + " TEXT, " +
                DEALER_COLUMN_EMAIL + " TEXT, " +
                DEALER_COLUMN_CATEGORY + " TEXT, " +
                DEALER_COLUMN_COMPANY + " TEXT, " +
                DEALER_COLUMN_DATE + " TEXT)");


        db.execSQL("CREATE TABLE " + TABLE_EXTERNALWORKS + "(" +
                EXTERNALWORKS_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                EXTERNALWORKS_COLUMN_MODEL_ID + " INTEGER, " +
                EXTERNALWORKS_COLUMN_PIECE_ID + " INTEGER, " +
                EXTERNALWORKS_COLUMN_NAME + " TEXT, " +
                EXTERNALWORKS_COLUMN_TYPE + " TEXT, " +
                EXTERNALWORKS_COLUMN_DESCRIPTION + " TEXT, " +
                EXTERNALWORKS_COLUMN_DATE + " TEXT, " +
                EXTERNALWORKS_COLUMN_COMPLETED + " INTEGER)");


        db.execSQL("CREATE TABLE " + TABLE_ACCESSORIES + "(" +
                ACCESSORIES_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                ACCESSORIES_COLUMN_MODEL_ID + " INTEGER, " +
                ACCESSORIES_COLUMN_NAME + " TEXT, " +
                ACCESSORIES_COLUMN_DEALER + " TEXT, " +
                ACCESSORIES_COLUMN_TYPE + " TEXT, " +
                ACCESSORIES_COLUMN_DESCRIPTION + " TEXT, " +
                ACCESSORIES_COLUMN_DATE + " TEXT, " +
                ACCESSORIES_COLUMN_IMAGE + " BLOB)");

        db.execSQL("CREATE TABLE " + TABLE_CUTNOTE + "(" +
               CUTNOTE_COLUMN_ID + " INTEGER PRIMARY KEY, " +
               CUTNOTE_COLUMN_MODEL_ID + " INTEGER, " +
               CUTNOTE_COLUMN_MODEL_NAME + " TEXT, " +
               CUTNOTE_COLUMN_REFERENCE + " INTEGER, " +
               CUTNOTE_COLUMN_NOTES + " BLOB, " +
                CUTNOTE_COLUMN_STATUS + " TEXT, " +
                CUTNOTE_COLUMN_DATE + " TEXT)");

        db.execSQL("CREATE TABLE " + TABLE_RELATIONS + "(" +
                RELATIONS_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                RELATIONS_COLUMN_MODEL_ID + " INTEGER, " +
                RELATIONS_COLUMN_DEALERS + " BLOB, " +
                RELATIONS_COLUMN_CUTNOTES + " BLOB, " +
                RELATIONS_COLUMN_EXTERNALS + " BLOB, " +
                RELATIONS_COLUMN_ACCESSORIES + " BLOB)");



        setDefaultCategory(db);

        // setDefaultDealership(db);


    }

    @Override

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        deletaAllTablePieces();
        deletaAllTableMAterial();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CUSTOM_MATERIAL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEALERSHIP);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXTERNALWORKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCESSORIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CUTNOTE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RELATIONS);


        onCreate(db);
        Utils.toast(mContext, "DATABASE UPDATED-AUTORESTART");

        restart(mContext, 10);
    }

    /////////CATEGORY///////////////////////////////////////////////////////////////////////

    private void setDefaultCategory(SQLiteDatabase db) {

        String[] categoryList = mContext.getResources().getStringArray(R.array.default_categories);
        for (String category : categoryList) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(MODEL_COLUMN_NAME, category);
            db.insert(TABLE_CATEGORY, null, contentValues);

        }
    }


    private void setDefaultDealership(SQLiteDatabase db) {

        String[] dealershipList = mContext.getResources().getStringArray(R.array.default_dealership);
        for (String dealer : dealershipList) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DEALER_COLUMN_NAME, dealer);
            db.insert(TABLE_DEALERSHIP, null, contentValues);

        }
    }


    public boolean insertCategory(String name) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MODEL_COLUMN_NAME, name);
        db.insert(TABLE_CATEGORY, null, contentValues);
        return true;
    }

    public boolean deleteCategory(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String filter = "_id=";
        return db.delete(TABLE_CATEGORY, filter + "=" + id, null) > 0;
    }

    public boolean deleteCategory(String name) {
        SQLiteDatabase db = this.getWritableDatabase();


        Cursor row = null;
        String filter = "name";

        final List<Integer> index = new ArrayList<>();

        Cursor c = db.rawQuery(
                "select * from category where " + name + "= ?", new String[]{name});

        while (c.moveToNext()) {

            deleteCategory(c.getInt(0));


        }


        return false;


    }

    public List<String> getCategoryString() {
        List<String> buffer = new ArrayList<>();
        HashSet hs=new HashSet();

        String selectQuery = "SELECT  * FROM " + TABLE_CATEGORY;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        try {
            if (cursor.moveToFirst()) {
                do {


                    buffer.add(cursor.getString(1));


                } while (cursor.moveToNext());
            }
            cursor.close();


        } catch (Exception e) {
            Utils.toast(mContext, e.toString());
        }


        Collections.sort(buffer.subList(2,buffer.size()));


        return buffer;
    }

    public List<Category> getCategoryClass() {
        List<Category> buffer = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_CATEGORY;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        try {
            if (cursor.moveToFirst()) {
                do {

                    final Category category = new Category();
                    category.setId(cursor.getInt(0));
                    category.setName(cursor.getString(1));
                    buffer.add(category);


                } while (cursor.moveToNext());
            }
            cursor.close();

        } catch (Exception e) {
            Utils.toast(mContext, e.toString());
        }
        return buffer;
    }

    public Category SearchCategory(String name) {

        Category category = null;
        SQLiteDatabase db = this.getWritableDatabase();
        String filter = "name=";

        Cursor c = db.rawQuery(
                "select * from " + TABLE_CATEGORY + " where " + filter + " ?", new String[]{name});


        try {

            category = new Category();
            while (c.moveToNext()) {


                category.setId(c.getInt(0));
                category.setName(c.getString(1));


            }
            c.close();

        } catch (Exception e) {
            Utils.toast(mContext, e.toString());
        }
        return category;
    }

    public Category SearchCategory(int id) {

        Category category = null;
        SQLiteDatabase db = this.getWritableDatabase();
        String filter = "_id";

        Cursor c = db.rawQuery(
                "select * from " + TABLE_CATEGORY + " where " + filter + " = ?", new String[]{String.valueOf(id)});


        try {

            category = new Category();
            while (c.moveToNext()) {


                category.setId(c.getInt(0));
                category.setName(c.getString(1));


            }
            c.close();

        } catch (Exception e) {
            Utils.toast(mContext, e.toString());
        }
        return category;
    }

    public boolean existCategory(@NonNull String category) {


        String selectQuery = "SELECT  * FROM " + TABLE_CATEGORY;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        try {
            if (cursor.moveToFirst()) {
                do {


                    if (category.toLowerCase().equals(cursor.getString(1).toLowerCase())) {

                        return true;


                    }

                } while (cursor.moveToNext());
            }
            cursor.close();

        } catch (Exception e) {
            Utils.toast(mContext, e.toString());
        }


        return false;
    }


    public boolean updateCategory(int id, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String filter = "_id=";
        ContentValues contentValues = new ContentValues();
        contentValues.put(MODEL_COLUMN_NAME, name);

        Cursor c = db.rawQuery(
                "select * from " + TABLE_CATEGORY + " where " + filter + " ?", new String[]{String.valueOf(id)});

        while (c.moveToNext()) {

            String oldcategory = c.getString(1);
            updateAllModelCategory(oldcategory, name);

        }


        return db.update(TABLE_CATEGORY, contentValues, filter + "=" + id, null) > 0;
    }

    public boolean isCategoryAssigned(String category) {
        SQLiteDatabase db = this.getWritableDatabase();


        Cursor row = null;
        String filter = MODEL_COLUMN_CATEGORY;

        final List<Integer> index = new ArrayList<>();

        Cursor c = db.rawQuery(
                "select * from " + TABLE_NAME + " where " + filter + "= ?", new String[]{category});

        while (c.moveToNext()) {

         return true;

        }

        return false;
    }


    public boolean updateAllModelCategory(String oldCategory, String newCategory) {
        SQLiteDatabase db = this.getWritableDatabase();


        Cursor row = null;
        String filter = "category";

        final List<Integer> index = new ArrayList<>();

        Cursor c = db.rawQuery(
                "select * from " + TABLE_NAME + " where " + filter + "= ?", new String[]{oldCategory});

        while (c.moveToNext()) {


            String filter1 = "_id=";
            ContentValues contentValues = new ContentValues();
            contentValues.put(MODEL_COLUMN_CATEGORY, newCategory);

            db.update(TABLE_NAME, contentValues, filter1 + "=" + c.getInt(0), null);


        }

        return false;
    }

    /////////////PREVIEW MODEL///////////////////////////////////////////////////////////////


    public List<PreviewModelInfo> loadData() {
        List<PreviewModelInfo> modelList = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        try {
            if (cursor.moveToFirst()) {
                do {


                    PreviewModelInfo model = new PreviewModelInfo();
                    model.setId(cursor.getInt(0));
                    model.setName(cursor.getString(1));
                    if (cursor.getBlob(2) != null) {
                        final List<String> piecesId = (List<String>) byteArrayToObject(cursor.getBlob(2));
                        model.setPiecesId(piecesId);

                    }
                    model.setPieceCount(cursor.getInt(3));

                    if (cursor.getBlob(4) != null) {
                        final List<String> Sizes = (List<String>) byteArrayToObject(cursor.getBlob(4));
                        model.setSizeList(Sizes);

                    }

                    model.setBaseSize(cursor.getString(5));

                    model.setMaterialCount(cursor.getInt(6));

                    model.setParent(cursor.getString(7));

                    if (cursor.getBlob(8) != null) {
                        byte[] blob = cursor.getBlob(8);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(blob, 0, blob.length);
                        Drawable drawable = new BitmapDrawable(Resources.getSystem(), bitmap);
                        model.setBmp(drawable);
                    }
                    model.setCategory(cursor.getString(9));
                    model.setDate(cursor.getString(10));
                    model.setDescription(cursor.getString(11));
                    modelList.add(model);
                    // get the data into array, or class variable
                } while (cursor.moveToNext());
            }
            cursor.close();

        } catch (Exception e) {
            Utils.toast(mContext, e.toString());
        }

        return modelList;
    }

    public void addNewModel(Model model) {


        ContentValues contentValues;

        SQLiteDatabase db = this.getWritableDatabase();


        final String Name, Parent, BaseSize, Description, Date, Category;
        final int PieceCount, MaterialCount;
        byte[] image = null;
        List<String> piecesIdList = new ArrayList<>();
        List<String> piecesMaterialIdList = new ArrayList<>();

        byte[] Sizes = null;


        Name = model.getName();
        PieceCount = model.getPieceCount();
        Parent = model.getParent();
        Description = model.getDescription();
        Category = model.getCategory();
        MaterialCount = model.getMaterialCount();
        BaseSize = model.getBaseSize();

        if (model.getBmp() != null) {
            image = imageViewtoByte(model.getBmp());

        }
        Date = getDate();


        if (!model.getSize().isEmpty()) {

            Sizes = prepareObjectToByte(model.getSize());

        }


        final long id = insertModel(Name, null, PieceCount, Parent, Date, Category, Description, BaseSize, image, Sizes, MaterialCount);

        addModelAtRelationTable(Utils.toIntExact(id));
        createPieceTable(String.valueOf(id), db);
        createMaterialTable(String.valueOf(id), db);

        for (MaterialGroup material : model.getMaterials()) {


            for (List<Piece> pieceList : material.getPieceList()) {


                for (int i = 0; i < pieceList.size(); i++) {

                    contentValues = new ContentValues();
                    contentValues.put(PIECE_COLUMN_MODEL_ID, String.valueOf(id));
                    contentValues.put(PIECE_COLUMN_NAME, pieceList.get(i).getName());
                    contentValues.put(PIECE_COLUMN_AMOUNT, pieceList.get(i).getAmount());
                    contentValues.put(PIECE_COLUMN_AMOUNT_MIRROR, pieceList.get(i).getAmount_mirror());
                    contentValues.put(PIECE_COLUMN_SIZE, pieceList.get(i).getSize());
                    contentValues.put(PIECE_COLUMN_MATERIAL, pieceList.get(i).getMaterial());
                    contentValues.put(PIECE_COLUMN_AMOUNT_MATERIAL, pieceList.get(i).getAmount_material());

                    if (!pieceList.get(i).getToolList().isEmpty()) {

                        contentValues.put(PIECE_COLUMN_TOOLS, prepareObjectToByte(pieceList.get(i).getToolList()));

                    }

                    if (pieceList.get(i).getImage() != null) {
                        contentValues.put(PIECE_COLUMN_IMAGE, imageViewtoByte(pieceList.get(i).getImage()));
                    }

                    contentValues.put(PIECE_COLUMN_DESCRIPT, pieceList.get(i).getDescription());

                    contentValues.put(PIECE_COLUMN_RAWDATA, prepareObjectToByte(pieceList.get(i).getRawData()));

                    long pieceId = db.insert(TABLE_PIECE + String.valueOf(id), null, contentValues);

                    if (i == 0) {

                        piecesIdList.add(String.valueOf(Utils.toIntExact(pieceId)));

                    }

                }


            }


            contentValues = new ContentValues();
            contentValues.put(MATERIAL_COLUMN_MODEL_ID, String.valueOf(id));
            contentValues.put(MATERIAL_COLUMN_NAME, material.getName());
            contentValues.put(MATERIAL_COLUMN_ASIGNED, "0");
            contentValues.put(MATERIAL_COLUMN_PIECES_ID, prepareObjectToByte(piecesMaterialIdList));

            db.insert(TABLE_MATERIAL + String.valueOf(id), null, contentValues);
        }

        updatePreviewData(String.valueOf(id), null, prepareObjectToByte(piecesIdList), null, null, null, null, null, null, null, null, null);


    }

    private long insertModel(String name, byte[] piecesId, int count, String parent, String date, String category, String description, String baseSize, byte[] image, byte[] sizes, int materialcount) {

        if (baseSize == null) {

            baseSize = mContext.getResources().getString(R.string.importNoAsigned_Cat);

        }

        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MODEL_COLUMN_NAME, name);
        contentValues.put(MODEL_COLUMN_PIECES, piecesId);
        contentValues.put(MODEL_COLUMN_COUNT, count);
        contentValues.put(MODEL_COLUMN_PARENT, parent);
        contentValues.put(MODEL_COLUMN_IMAGE, image);
        contentValues.put(MODEL_COLUMN_BASE_SIZE, baseSize);
        contentValues.put(MODEL_COLUMN_SIZES, sizes);
        contentValues.put(MODEL_COLUMN_MATERIAL_COUNT, materialcount);
        contentValues.put(MODEL_COLUMN_CATEGORY, category);
        contentValues.put(MODEL_COLUMN_DESCRIPT, description);
        contentValues.put(MODEL_COLUMN_DATE, date);

        return db.insert(TABLE_NAME, null, contentValues);
    }

    public void updatePreviewData(PreviewModelInfo model) {

        SQLiteDatabase db = this.getWritableDatabase();
        String filter = "_id=";
        filter += model.getId();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MODEL_COLUMN_NAME, model.getName());
        contentValues.put(MODEL_COLUMN_COUNT, model.getPieceCount());
        contentValues.put(MODEL_COLUMN_PARENT, model.getParent());

        if (model.getPiecesId() != null) {
            contentValues.put(MODEL_COLUMN_PIECES, prepareObjectToByte(model.getPiecesId()));
        }
        if (model.getSizeList() != null) {
            contentValues.put(MODEL_COLUMN_SIZES, prepareObjectToByte(model.getSizeList()));
        }

        contentValues.put(MODEL_COLUMN_MATERIAL_COUNT, model.getMaterialCount());

        if (model.getBmp() != null) {
            contentValues.put(MODEL_COLUMN_IMAGE, imageViewtoByte(model.getBmp()));
        }

        contentValues.put(MODEL_COLUMN_BASE_SIZE, model.getBaseSize());


        contentValues.put(MODEL_COLUMN_CATEGORY, model.getCategory());
        contentValues.put(MODEL_COLUMN_DATE, model.getDate());


        contentValues.put(MODEL_COLUMN_DESCRIPT, model.getDescription());


        db.update(TABLE_NAME, contentValues, filter, null);

        updateMaterialPieceCount(model.getId());


    }

    public void updatePreviewData(@NonNull String id, String name, byte[] piecesId, String pieceCount, String parent, Drawable image, String category, String date, String description
            , String baseSize, byte[] sizes, String materialcount) {
        SQLiteDatabase db = this.getWritableDatabase();
        String filter = "_id=";
        filter += id;

        ContentValues contentValues = new ContentValues();
        if (name != null) {
            contentValues.put(MODEL_COLUMN_NAME, name);

        }

        if (piecesId != null) {
            contentValues.put(MODEL_COLUMN_PIECES, piecesId);

        }

        if (pieceCount != null) {
            contentValues.put(MODEL_COLUMN_COUNT, Integer.valueOf(pieceCount));

        }

        if (parent != null) {
            contentValues.put(MODEL_COLUMN_PARENT, parent);

        }

        if (image != null) {
            contentValues.put(MODEL_COLUMN_IMAGE, imageViewtoByte(image));
        }

        if (baseSize != null) {

            contentValues.put(MODEL_COLUMN_BASE_SIZE, baseSize);


        }

        if (sizes != null) {
            contentValues.put(MODEL_COLUMN_SIZES, sizes);
        }

        if (materialcount != null) {
            contentValues.put(MODEL_COLUMN_MATERIAL_COUNT, materialcount);
        }


        if (category != null) {
            contentValues.put(MODEL_COLUMN_CATEGORY, category);

        }

        if (date != null) {

            contentValues.put(MODEL_COLUMN_DATE, date);

        }

        if (description != null) {
            contentValues.put(MODEL_COLUMN_DESCRIPT, description);

        }

        db.update(TABLE_NAME, contentValues, filter, null);
        updateMaterialPieceCount(Integer.valueOf(id));

    }

    public boolean deleteModelData(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String filter = "_id=";

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PIECE + String.valueOf(id));//Borrado de tabla de piezas ,asociadas al modelo



        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MATERIAL + String.valueOf(id));//Borrado de tabla de materiales,asociadas al modelo

        deleteModelAtRelationTable(id);

        return db.delete(TABLE_NAME, filter + "=" + id, null) > 0;
    }

    public boolean deleteAllData() {

        SQLiteDatabase db = this.getReadableDatabase();

        this.onUpgrade(db, db.getVersion(), db.getVersion() + 1);


        return true;
    }

    public PreviewModelInfo getPreviewModel(int id) {

        PreviewModelInfo model = new PreviewModelInfo();

        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        try {
            if (cursor.moveToFirst()) {
                do {


                    if (id == cursor.getInt(0)) {


                        model.setId(cursor.getInt(0));
                        model.setName(cursor.getString(1));
                        if (cursor.getBlob(2) != null) {
                            final List<String> piecesId = (List<String>) byteArrayToObject(cursor.getBlob(2));
                            model.setPiecesId(piecesId);

                        }
                        model.setPieceCount(cursor.getInt(3));

                        if (cursor.getBlob(4) != null) {
                            final List<String> Sizes = (List<String>) byteArrayToObject(cursor.getBlob(4));
                            model.setSizeList(Sizes);

                        }

                        model.setBaseSize(cursor.getString(5));
                        model.setMaterialCount(cursor.getInt(6));

                        model.setParent(cursor.getString(7));

                        if (cursor.getBlob(8) != null) {
                            byte[] blob = cursor.getBlob(8);
                            Bitmap bitmap = BitmapFactory.decodeByteArray(blob, 0, blob.length);
                            Drawable drawable = new BitmapDrawable(Resources.getSystem(), bitmap);
                            model.setBmp(drawable);
                        }
                        model.setCategory(cursor.getString(9));
                        model.setDate(cursor.getString(10));
                        model.setDescription(cursor.getString(11));
                        cursor.close();
                        // get the data into array, or class variable
                        break;
                    }

                } while (cursor.moveToNext());
            }
            cursor.close();

        } catch (Exception e) {
            Utils.toast(mContext, e.toString());
        }


        return model;
    }

    public int getLastIndexModel() {

        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToLast();
        final int lastIndex = cursor.getInt(0);
        cursor.close();
        return lastIndex;
    }

    public int getModelsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        final int count = cursor.getCount();

        cursor.close();

        // return count
        return count;
    }

    public List<PreviewModelInfo> getLastesModel(int count) {

        int i = 0;

        List<PreviewModelInfo> modelList = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        try {
            if (cursor.moveToLast()) {

                do {


                    if (i < count) {
                        PreviewModelInfo model = new PreviewModelInfo();
                        model.setId(cursor.getInt(0));
                        model.setName(cursor.getString(1));
                        if (cursor.getBlob(2) != null) {
                            final List<String> piecesId = (List<String>) byteArrayToObject(cursor.getBlob(2));
                            model.setPiecesId(piecesId);

                        }
                        model.setPieceCount(cursor.getInt(3));

                        if (cursor.getBlob(4) != null) {
                            final List<String> Sizes = (List<String>) byteArrayToObject(cursor.getBlob(4));
                            model.setSizeList(Sizes);

                        }

                        model.setBaseSize(cursor.getString(5));

                        model.setMaterialCount(cursor.getInt(6));

                        model.setParent(cursor.getString(7));

                        if (cursor.getBlob(8) != null) {
                            byte[] blob = cursor.getBlob(8);
                            Bitmap bitmap = BitmapFactory.decodeByteArray(blob, 0, blob.length);
                            Drawable drawable = new BitmapDrawable(Resources.getSystem(), bitmap);
                            model.setBmp(drawable);
                        }
                        model.setCategory(cursor.getString(9));
                        model.setDate(cursor.getString(10));
                        model.setDescription(cursor.getString(11));
                        modelList.add(model);
                        i = i + 1;
                    } else {

                        break;
                    }


                    // get the data into array, or class variable
                } while (

                        cursor.moveToPrevious());
            }
            cursor.close();

        } catch (Exception e) {
            Utils.toast(mContext, e.toString());
        }

        return modelList;
    }

    //////////// PIECES////////////////////////////////////////////////////////////

    public void createPieceTable(String modelId, SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + TABLE_PIECE + modelId + "(" +
                PIECE_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                PIECE_COLUMN_MODEL_ID + " TEXT, " +
                PIECE_COLUMN_NAME + " TEXT, " +
                PIECE_COLUMN_AMOUNT + " INTEGER, " +
                PIECE_COLUMN_AMOUNT_MIRROR + " INTEGER, " +
                PIECE_COLUMN_SIZE + " TEXT, " +
                PIECE_COLUMN_MATERIAL + " TEXT, " +
                PIECE_COLUMN_AMOUNT_MATERIAL + " FLOAT, " +
                PIECE_COLUMN_TOOLS + " BLOB, " +
                PIECE_COLUMN_IMAGE + " BLOB, " +
                PIECE_COLUMN_DESCRIPT + " TEXT, " +
                PIECE_COLUMN_RAWDATA + " BLOB)"
        );

    }

    private Piece createPiece (Cursor cursor,boolean withImage) {

        final Piece piece = new Piece();

        piece.setId(cursor.getInt(0));
        piece.setModelId(Integer.valueOf(cursor.getString(1)));
        piece.setName(cursor.getString(2));
        piece.setAmount(cursor.getInt(3));
        piece.setAmount_mirror(cursor.getInt(4));
        piece.setSize(cursor.getString(5));
        piece.setMaterial(cursor.getString(6));
        piece.setAmount_material(cursor.getFloat(7));
        if (cursor.getBlob(8) != null) {

            piece.setToolList((List<String>) byteArrayToObject(cursor.getBlob(8)));

        }


        piece.setDescription(cursor.getString(10));


        if (cursor.getBlob(11) != null) {
            piece.setRawData((List<String>) byteArrayToObject(cursor.getBlob(11)));

        }


if(withImage){

    if (cursor.getBlob(9) != null) {

        piece.setImage(byteToDrawable(cursor.getBlob(9)));

    }

    else{

        int size = (int) mContext.getResources().getDimension(R.dimen.piece_pic_size);
        final DrawView drawView = new DrawView(mContext, piece, null, size, size);
        piece.setImage(drawView.getDrawable(size, size));

        //Float area = (drawView.decoder.getRectagleArea() / 1000000);
        //piece.setBoxArea(area * 10.764f);

        //  piece.setAmount_material((drawView.calculateArea()/1000000)*10.746f);

    }

}



        return piece;
    }

    public boolean addPiece(String model_id, Piece piece) {


        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        String TABLE_PIECE = null;
        boolean state = false;
        try {
            TABLE_PIECE = ModelDataBase.TABLE_PIECE + model_id;

            contentValues.put(PIECE_COLUMN_NAME, piece.getName());
            contentValues.put(PIECE_COLUMN_AMOUNT, piece.getAmount());
            contentValues.put(PIECE_COLUMN_AMOUNT_MIRROR, piece.getAmount_mirror());
            contentValues.put(PIECE_COLUMN_SIZE, piece.getSize());
            contentValues.put(PIECE_COLUMN_MATERIAL, piece.getMaterial());
            contentValues.put(PIECE_COLUMN_AMOUNT_MATERIAL, piece.getAmount_material());

            contentValues.put(PIECE_COLUMN_TOOLS, piece.getToolList().toString());

            if (piece.getImage() != null) {
                contentValues.put(PIECE_COLUMN_IMAGE, imageViewtoByte(piece.getImage()));
            }

            contentValues.put(PIECE_COLUMN_DESCRIPT, prepareObjectToByte(piece.getDescription()));

            contentValues.put(PIECE_COLUMN_RAWDATA, (piece.getRawData().toString()));

            db.insert(TABLE_PIECE, null, contentValues);

            state = true;


        } catch (Exception e) {
            Utils.toast(mContext, "INSERT PIECE FAILED" + e.toString());
            state = false;
        }

        return state;
    }

    public boolean deletePiece(@NonNull Piece piece) {

        boolean delete = false;

        SQLiteDatabase db = this.getWritableDatabase();


        String pieceName = PIECE_COLUMN_NAME;
        String pieceMaterial = PIECE_COLUMN_MATERIAL;
        String pieceModelId = String.valueOf(piece.getModelId());


        Cursor row = null;
        String query;

        List<String> piecesid = getPreviewModel(piece.getModelId()).getPiecesId();

        Cursor c = db.rawQuery(
                "select * from " + TABLE_PIECE + pieceModelId + " where " + pieceName + " = ? AND " + pieceMaterial + " = ? ", new String[]{piece.getName(), piece.getMaterial()});

        while (c.moveToNext()) {


            String filter = "_id=";

            delete = db.delete(TABLE_PIECE + pieceModelId, filter + "=" + c.getInt(0), null) > 0;


        }


        for (int i = piecesid.size() - 1; i >= 0; i--) {

            if (piecesid.get(i).equals(String.valueOf(piece.getId()))) {


                piecesid.remove(i);
                break;
            }


        }




        updatePreviewData(pieceModelId, null, prepareObjectToByte(piecesid), String.valueOf(piecesid.size()), null, null, null, null, null, null, null, null);


        return delete;
    }

    public Piece getPiece(@NonNull int modelId, @NonNull int pieceId, String name, String material,boolean withImage) {

        Piece piece = new Piece();


        String selectQuery = "SELECT  * FROM " + TABLE_PIECE + String.valueOf(modelId);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        try {
            if (cursor.moveToFirst()) {
                do {


                    if (pieceId == cursor.getInt(0)) {

                       piece=createPiece(cursor,withImage);

                        cursor.close();
                        break;

                    }

                } while (cursor.moveToNext());
            }


        } catch (Exception e) {
            Utils.toast(mContext, "No se puede ver la pieza" + e.toString());
        }


        return piece;
    }

    public Piece getPieceBySize(@NonNull int modelId, @NonNull String size, @NonNull String name, @NonNull String material,boolean withImage) {

        Piece piece = new Piece();


        String selectQuery = "SELECT  * FROM " + TABLE_PIECE + String.valueOf(modelId);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        try {
            if (cursor.moveToFirst()) {
                do {


                    if (name.equals(cursor.getString(2))) {

                        if (size.equals(cursor.getString(5))) {

                            if (material.equals(cursor.getString(6))) {

                                piece=createPiece(cursor,withImage);

                                cursor.close();

                                break;

                            }
                        }
                    }

                } while (cursor.moveToNext());
            }
            cursor.close();

        } catch (Exception e) {
            Utils.toast(mContext, "No se puede ver la pieza" + e.toString());
        }


        return piece;
    }

    public List<Piece> getHighLightPieces(@NonNull int modelId,boolean withImage){

        List<Piece>pieceList=new ArrayList<>();

        List<String>ids=getPreviewModel(modelId).getPiecesId();


        for(String id:ids){


            pieceList.add(getPiece(modelId,Integer.valueOf(id),null,null,withImage));


        }


        return pieceList;
    }

    public String getPieceId(@NonNull int modelId, @NonNull String size, @NonNull String name, @NonNull String material) {


        String id = null;
        String selectQuery = "SELECT  * FROM " + TABLE_PIECE + String.valueOf(modelId);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        try {
            if (cursor.moveToFirst()) {
                do {


                    if (name.equals(cursor.getString(2))) {

                        if (size.equals(cursor.getString(5))) {

                            if (material.equals(cursor.getString(6))) {


                                id = String.valueOf(cursor.getInt(0));

                                cursor.close();
                                break;

                            }
                        }
                    }

                } while (cursor.moveToNext());
            }
            cursor.close();

        } catch (Exception e) {
            Utils.toast(mContext, "Error buscando Id de pieza" + e.toString());
        }
        return id;
    }

    public List<Piece> getPizeSizeList(Piece auxpiece,boolean withImage) {

        final List<Piece> pieceList = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_PIECE + String.valueOf(auxpiece.getModelId());
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        try {
            if (cursor.moveToFirst()) {
                do {


                    if (auxpiece.getName().equals(cursor.getString(2))) {

                        if (auxpiece.getMaterial().equals(cursor.getString(6))) {

                           pieceList.add(createPiece(cursor,withImage));
                        }
                    }

                } while (cursor.moveToNext());
                cursor.close();

            }


        } catch (Exception e) {
            Utils.toast(mContext, "No se puede ver la pieza" + e.toString());
        }


        return pieceList;
    }

    public boolean deletaAllTablePieces() {

        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        try {
            if (cursor.moveToFirst()) {
                do {


                    String TABLE_PIECE = ModelDataBase.TABLE_PIECE + String.valueOf(cursor.getInt(0));
                    db.execSQL("DROP TABLE IF EXISTS " + TABLE_PIECE);

                } while (cursor.moveToNext());
            }
            cursor.close();

        } catch (Exception e) {
            Utils.toast(mContext, "DELETE PIECE TABLE FAILED " + e.toString());
        }


        return true;

    }

    public boolean updateSinglePiece(Piece piece) {

        SQLiteDatabase db = this.getWritableDatabase();
        String filter = "_id=";
        filter += piece.getId();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PIECE_COLUMN_NAME, piece.getName());
        contentValues.put(PIECE_COLUMN_MATERIAL, piece.getMaterial());
        contentValues.put(PIECE_COLUMN_AMOUNT_MATERIAL, piece.getAmount_material());
        contentValues.put(PIECE_COLUMN_AMOUNT, piece.getAmount());
        contentValues.put(PIECE_COLUMN_AMOUNT_MIRROR, piece.getAmount_mirror());
        contentValues.put(PIECE_COLUMN_TOOLS, prepareObjectToByte(piece.getToolList()));

        contentValues.put(PIECE_COLUMN_DESCRIPT, piece.getDescription());

        return db.update(TABLE_PIECE + piece.getModelId(), contentValues, filter, null) > 0;


    }

    public void updateAllSamePieceData(Piece oldPiece, Piece newPiece) {
        String selectQuery = "SELECT  * FROM " + TABLE_PIECE + String.valueOf(oldPiece.getModelId());
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        try {
            if (cursor.moveToFirst()) {
                do {

                    if (cursor.getString(2).equals(oldPiece.getName())) {

                        if (cursor.getString(6).equals(oldPiece.getMaterial())) {

                            String filter = "_id=";
                            filter += cursor.getInt(0);
                            ContentValues contentValues = new ContentValues();
                            contentValues.put(PIECE_COLUMN_NAME, newPiece.getName());
                            contentValues.put(PIECE_COLUMN_MATERIAL, newPiece.getMaterial());
                            contentValues.put(PIECE_COLUMN_AMOUNT, newPiece.getAmount());
                            contentValues.put(PIECE_COLUMN_AMOUNT_MIRROR, newPiece.getAmount_mirror());
                            contentValues.put(PIECE_COLUMN_TOOLS, prepareObjectToByte(newPiece.getToolList()));

                            contentValues.put(PIECE_COLUMN_DESCRIPT, newPiece.getDescription());

                            db.update(TABLE_PIECE + oldPiece.getModelId(), contentValues, filter, null);


                        }

                    }


                } while (cursor.moveToNext());

            }
            cursor.close();
            updateMaterialPieceCount(oldPiece.getModelId());

        } catch (Exception e) {
            Utils.toast(mContext, e.toString());
        }

    }

    public int getPiecesCount(int modelId) {

        String countQuery = "SELECT  * FROM " + TABLE_PIECE + String.valueOf(modelId);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);


        final int count = cursor.getCount();
        cursor.close();
        // return count
        return count;
    }


    ///////////MATERIALS/////////////////////////////////////////////////////////////////

    public void updateMaterialPieceCount(int modelId) {


        List<Piece> piecesId = getHighLightPieces(modelId,false);

        List<Material> materials = getModelMaterial(modelId);


          for (Material material : materials) {

              List<String> pieceMaterialId=new ArrayList<>();


                for(int i=piecesId.size()-1;i>=0;i--){



                    if (piecesId.get(i).getMaterial().equals(material.getName())) {

                        pieceMaterialId.add(String.valueOf(piecesId.get(i).getId()));

                       piecesId.remove(i);



                    }

                }


              updateMaterial(modelId, material.getId(), null, material.getCustomMaterialId(), pieceMaterialId);


          }




    }

    public void createMaterialTable(String modelId, SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + TABLE_MATERIAL + modelId + "(" +
                MATERIAL_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                MATERIAL_COLUMN_MODEL_ID + " TEXT, " +
                MATERIAL_COLUMN_NAME + " TEXT, " +
                MATERIAL_COLUMN_ASIGNED + " TEXT, " +
                MATERIAL_COLUMN_COLOR + " INTEGER, " +
                MATERIAL_COLUMN_PIECES_ID + " BLOB)"
        );


    }

    public List<Material> getModelMaterial(Integer modelId) {

        List<Material> bufferMaterial = null;


        String selectQuery = "SELECT  * FROM " + TABLE_MATERIAL + modelId;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        try {

            bufferMaterial = new ArrayList<>();
            if (cursor.moveToFirst()) {
                do {


                    Material material = new Material("");
                    material.setId(cursor.getInt(0));
                    material.setModelId(Integer.valueOf(cursor.getString(1)));
                    material.setName(cursor.getString(2));
                    material.setCustomMaterialId(cursor.getString(3));


                    if (cursor.getBlob(5) != null) {
                        final List<String> piecesId = (List<String>) byteArrayToObject(cursor.getBlob(5));
                        material.setPiecesId(piecesId);

                    }

                    bufferMaterial.add(material);

                } while (cursor.moveToNext());
            }
            cursor.close();

        } catch (Exception e) {
            Utils.toast(mContext, e.toString());
        }


        return bufferMaterial;

    }

    public void addMaterial(@NonNull Integer modelId, @NonNull String name, @Nullable String customMaterialId, @Nullable List<String> piecesId) {


        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MATERIAL_COLUMN_MODEL_ID, String.valueOf(modelId));
        contentValues.put(MATERIAL_COLUMN_NAME, name);
        byte[] ids = null;

        if (customMaterialId != null) {

            contentValues.put(MATERIAL_COLUMN_ASIGNED, customMaterialId);


        } else {

            contentValues.put(MATERIAL_COLUMN_ASIGNED, "0");
        }
        if (piecesId != null) {


            contentValues.put(MATERIAL_COLUMN_PIECES_ID, prepareObjectToByte(piecesId));
        }

        db.insert(TABLE_MATERIAL + String.valueOf(modelId), null, contentValues);

    }

    public boolean deleteMaterial(@NonNull Integer modelId, @NonNull Integer materialId) {


        SQLiteDatabase db = this.getWritableDatabase();
        String filter = "_id=";


         deleteRelationWithCustomMaterial(getMaterialById(modelId, materialId));


        return db.delete(TABLE_MATERIAL + String.valueOf(modelId), filter + "=" + materialId, null) > 0;
    }

    public Material getMaterialByName(@NonNull int modelId, @NonNull String name) {

        Material material = null;


        String selectQuery = "SELECT  * FROM " + TABLE_MATERIAL + String.valueOf(modelId);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        try {


            if (cursor.moveToFirst()) {
                do {

                    if (cursor.getString(2).equals(name)) {


                        material = new Material(name);
                        material.setId(cursor.getInt(0));
                        material.setModelId(Integer.valueOf(cursor.getString(1)));
                        material.setCustomMaterialId(cursor.getString(3));
                        if (cursor.getBlob(4) != null) {
                            final List<String> piecesId = (List<String>) byteArrayToObject(cursor.getBlob(4));
                            material.setPiecesId(piecesId);

                        }
                        cursor.close();

                        break;
                    }


                } while (cursor.moveToNext());
            }
            cursor.close();

        } catch (Exception e) {
            Utils.toast(mContext, e.toString());
        }


        return material;
    }

    public Material getMaterialById(@NonNull int modelId, @NonNull int materialId) {

        Material material = null;


        String selectQuery = "SELECT  * FROM " + TABLE_MATERIAL + String.valueOf(modelId);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        try {


            if (cursor.moveToFirst()) {
                do {

                    if (cursor.getString(0).equals(String.valueOf(materialId))) {


                        material = new Material(cursor.getString(2));
                        material.setId(cursor.getInt(0));
                        material.setModelId(Integer.valueOf(cursor.getString(1)));
                        material.setCustomMaterialId(cursor.getString(3));
                        if (cursor.getBlob(4) != null) {
                            final List<String> piecesId = (List<String>) byteArrayToObject(cursor.getBlob(4));
                            material.setPiecesId(piecesId);

                        }
                        cursor.close();

                        break;
                    }


                } while (cursor.moveToNext());
            }
            cursor.close();

        } catch (Exception e) {
            Utils.toast(mContext, e.toString());
        }


        return material;
    }

    public boolean updateMaterial(@NonNull int modelId, @NonNull int materialId, @Nullable String name, @NonNull String customMaterialId, @Nullable List<String> piecesId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String filter = "_id=";
        ContentValues contentValues = new ContentValues();
        if (name != null) {

            contentValues.put(MATERIAL_COLUMN_NAME, name);

        }

        if (!customMaterialId.equals("0")) {


            contentValues.put(MATERIAL_COLUMN_ASIGNED, customMaterialId);
            addRelationWithCustomMaterial(modelId, customMaterialId);

        } else if ((customMaterialId.equals("0")) || (customMaterialId == null)) {

           deleteRelationWithCustomMaterial(getMaterialById(modelId, materialId));
            customMaterialId = "0";
            contentValues.put(MATERIAL_COLUMN_ASIGNED, customMaterialId);


        }


        if (piecesId != null) {


            contentValues.put(MATERIAL_COLUMN_PIECES_ID, prepareObjectToByte(piecesId));

        }


        return db.update(TABLE_MATERIAL + String.valueOf(modelId), contentValues, filter + "=" + String.valueOf(materialId), null) > 0;
    }

    public void updateAllPieceInMaterial(Piece piece) {

    }

    public boolean deletaAllTableMAterial() {

        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        try {
            if (cursor.moveToFirst()) {
                do {


                    String TABLE_MATERIAL = this.TABLE_MATERIAL + String.valueOf(cursor.getInt(0));
                    db.execSQL("DROP TABLE IF EXISTS " + TABLE_MATERIAL);

                } while (cursor.moveToNext());
            }
            cursor.close();

        } catch (Exception e) {
            Utils.toast(mContext, "DELETE MATERIAL TABLE FAILED " + e.toString());
        }


        return true;

    }

    ///////////CUSTOM MATERIALS////////////////////////////////////////////////////

    public List<CustomMaterial> getCustomMaterilas() {

        List<CustomMaterial> bufferMaterial = null;


        String selectQuery = "SELECT  * FROM " + TABLE_CUSTOM_MATERIAL;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        try {

            bufferMaterial = new ArrayList<>();
            if (cursor.moveToFirst()) {
                do {


                    CustomMaterial material = new CustomMaterial(cursor.getString(1));
                    material.setId(cursor.getInt(0));
                    material.setName(cursor.getString(1));
                    material.setType(cursor.getString(2));
                    material.setFeets(cursor.getFloat(3));
                    material.setMeters(cursor.getFloat(4));
                    material.setDealership(cursor.getString(5));
                    material.setSeasons(cursor.getString(6));


                    if (cursor.getBlob(7) != null) {
                        byte[] blob = cursor.getBlob(7);
                      final  Bitmap bitmap = BitmapFactory.decodeByteArray(blob, 0, blob.length);
                       final Drawable drawable = new BitmapDrawable(Resources.getSystem(), bitmap);
                        material.setImage(drawable);
                    }

                    material.setDate(cursor.getString(8));
                    material.setDescription(cursor.getString(9));


                    if (cursor.getBlob(10) != null) {
                        final List<String> modelsId = (List<String>) byteArrayToObject(cursor.getBlob(10));
                        material.setModelIdRelation(modelsId);

                    }


                    bufferMaterial.add(material);

                } while (cursor.moveToNext());
            }
            cursor.close();

        } catch (Exception e) {
            Utils.toast(mContext, e.toString());
        }


        return bufferMaterial;
    }

    public int getCustomMaterialCount() {

        String countQuery = "SELECT  * FROM " + TABLE_CUSTOM_MATERIAL;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        final int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    private long insertCustomMaterial(@NonNull String name, String type, float feets, float meters, String dealership, String seasons, byte[] image, String date,String description, byte[] modelsIdRelation) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(CUSTOM_MATERIAL_COLUMN_NAME, name);
        contentValues.put(CUSTOM_MATERIAL_COLUMN_TYPE, type);
        contentValues.put(CUSTOM_MATERIAL_COLUMN_FEETS, feets);
        contentValues.put(CUSTOM_MATERIAL_COLUMN_METERS, meters);
        contentValues.put(CUSTOM_MATERIAL_COLUMN_DEALERSHIP, dealership);
        contentValues.put(CUSTOM_MATERIAL_COLUMN_SEASONS, seasons);
        contentValues.put(CUSTOM_MATERIAL_COLUMN_IMAGE, image);
        contentValues.put(CUSTOM_MATERIAL_COLUMN_DATE, date);
        contentValues.put(CUSTOM_MATERIAL_COLUMN_DESCRIPTION,description);

        if (modelsIdRelation != null) {
            contentValues.put(CUSTOM_MATERIAL_COLUMN_MODEL_RELATION, modelsIdRelation);

        }

        return db.insert(TABLE_CUSTOM_MATERIAL, null, contentValues);
    }

    public long  addCustomMaterial(CustomMaterial customMaterial) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy\nHH:mm:ss");
        Date auxdate = new Date();

        String name, type, dealership, seasons, date,description;
        Float feets, meters;
        byte[] image = null;

        byte[] modelsIdRelation;

        name = customMaterial.getName();

        type = customMaterial.getType();
        description=customMaterial.getDescription();

        if (type == null) {

            type = mContext.getResources().getString(R.string.importNoAsigned_Cat);
        }
        feets = customMaterial.getFeets();
        meters = customMaterial.getMeters();
        dealership = customMaterial.getDealership();
        if (dealership == null) {

            dealership = mContext.getResources().getString(R.string.importNoAsigned_Cat);

        }
        seasons = customMaterial.getSeasons();

        if (seasons == null) {

            seasons = mContext.getResources().getString(R.string.importNoAsigned_Cat);

        }
        date = dateFormat.format(auxdate);

        if (customMaterial.getImage() != null) {

            image = imageViewtoByte(customMaterial.getImage());

        } else {

            Drawable drawable = mContext.getDrawable(R.drawable.leather);
            image = imageViewtoByte(drawable);


        }

        if(description==null){


            description = mContext.getResources().getString(R.string.importNoAsigned_Cat);



        }


        if (!customMaterial.getModelIdRelation().isEmpty()) {


            modelsIdRelation = prepareObjectToByte(customMaterial.getModelIdRelation());
        } else {

            modelsIdRelation = null;
        }

       return insertCustomMaterial(name, type, feets, meters, dealership, seasons, image, date,description, modelsIdRelation);


    }

    public Drawable getCustomMaterialImage(int id){

        Drawable img=null;



        String selectQuery = "SELECT  * FROM " + TABLE_CUSTOM_MATERIAL;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        try {
            if (cursor.moveToFirst()) {
                do {


                    if (id == cursor.getInt(0)) {


                        if (cursor.getBlob(7) != null) {

                           final byte[] blob = cursor.getBlob(7);
                           final Bitmap bitmap = BitmapFactory.decodeByteArray(blob, 0, blob.length);
                            img = new BitmapDrawable(Resources.getSystem(), bitmap);

                        }


                        cursor.close();
                        // get the data into array, or class variable
                        break;
                    }

                } while (cursor.moveToNext());
            }
            cursor.close();

        } catch (Exception e) {
            Utils.toast(mContext, "No se encuentra el material" + e.toString());
        }

        return  img;
    }

    public List<String> getCustomMaterialsDealersNames(){

        List<String>dealerList=new ArrayList<>();
        HashSet hs=new HashSet();

        String selectQuery = "SELECT  * FROM " + TABLE_CUSTOM_MATERIAL;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        try {
            if (cursor.moveToFirst()) {
                do {

                    if (cursor.getString(5) != null) {

                       hs.add(cursor.getString(5));
                    }

                } while (cursor.moveToNext());
            }
            cursor.close();

        } catch (Exception e) {
            Utils.toast(mContext, "No se encuentra el material" + e.toString());
        }


        dealerList.addAll(hs);

        Collections.sort(dealerList);

        return dealerList;
    }

    public boolean deleteCustomMaterial(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String filter = "_id=";


        List<String> modelsId = getCustomMaterial(id).getModelIdRelation();

        if (!modelsId.isEmpty()) {

            for (int i = 0; i < modelsId.size(); i++) {


                List<Material> materialList = getModelMaterial(Integer.valueOf(modelsId.get(i)));

                for (Material material : materialList) {


                    if (material.getCustomMaterialId().equals(String.valueOf(id))) {

                        updateMaterial(Integer.valueOf(modelsId.get(i)), material.getId(), null, "0", null);

                    }

                }


            }
        }




        return db.delete(TABLE_CUSTOM_MATERIAL, filter + "=" + id, null) > 0;
    }

    public boolean updateCustomMAterial(CustomMaterial material) {
        SQLiteDatabase db = this.getWritableDatabase();
        String filter = "_id=";
        ContentValues contentValues = new ContentValues();
        contentValues.put(CUSTOM_MATERIAL_COLUMN_NAME, material.getName());
        contentValues.put(CUSTOM_MATERIAL_COLUMN_TYPE, material.getType());
        contentValues.put(CUSTOM_MATERIAL_COLUMN_FEETS, material.getFeets());
        contentValues.put(CUSTOM_MATERIAL_COLUMN_METERS, material.getMeters());
        contentValues.put(CUSTOM_MATERIAL_COLUMN_DEALERSHIP, material.getDealership());
        contentValues.put(CUSTOM_MATERIAL_COLUMN_SEASONS, material.getSeasons());
        contentValues.put(CUSTOM_MATERIAL_COLUMN_DATE, material.getDate());
        contentValues.put(CUSTOM_MATERIAL_COLUMN_DESCRIPTION,material.getDescription());

        if (material.getImage() != null) {
            contentValues.put(CUSTOM_MATERIAL_COLUMN_IMAGE, imageViewtoByte(material.getImage()));
        }

        contentValues.put(CUSTOM_MATERIAL_COLUMN_MODEL_RELATION, prepareObjectToByte(material.getModelIdRelation()));


        return db.update(TABLE_CUSTOM_MATERIAL, contentValues, filter + "=" + String.valueOf(material.getId()), null) > 0;
    }

    public CustomMaterial getCustomMaterial(int materialId) {

        final CustomMaterial material = new CustomMaterial("");


        String selectQuery = "SELECT  * FROM " + TABLE_CUSTOM_MATERIAL;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        try {
            if (cursor.moveToFirst()) {
                do {


                    if (materialId == cursor.getInt(0)) {

                        material.setId(cursor.getInt(0));

                        material.setName(cursor.getString(1));
                        material.setType(cursor.getString(2));
                        material.setFeets(cursor.getFloat(3));
                        material.setMeters(cursor.getFloat(4));
                        material.setDealership(cursor.getString(5));
                        material.setSeasons(cursor.getString(6));

                        if (cursor.getBlob(7) != null) {

                            byte[] blob = cursor.getBlob(7);
                            Bitmap bitmap = BitmapFactory.decodeByteArray(blob, 0, blob.length);
                            Drawable drawable = new BitmapDrawable(Resources.getSystem(), bitmap);
                            material.setImage(drawable);

                        }

                        material.setDate(cursor.getString(8));

                        material.setDescription(cursor.getString(9));

                        if (cursor.getBlob(10) != null) {
                            final List<String> modelsId = (List<String>) byteArrayToObject(cursor.getBlob(10));
                            material.setModelIdRelation(modelsId);

                        }
                        cursor.close();
                        // get the data into array, or class variable
                        break;
                    }

                } while (cursor.moveToNext());
            }
            cursor.close();

        } catch (Exception e) {
            Utils.toast(mContext, "No se encuentra el material" + e.toString());
        }


        return material;
    }

    public List<CustomMaterial>getCustomMaterialByDealer(String dealer){

        final List<CustomMaterial>materialBuffer=new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();


        Cursor row = null;
        String filter = CUSTOM_MATERIAL_COLUMN_DEALERSHIP;

        final List<Integer> index = new ArrayList<>();

        Cursor cursor = db.rawQuery(
                "select * from " + TABLE_CUSTOM_MATERIAL + " where " + filter + "= ?", new String[]{dealer});

        while (cursor.moveToNext()) {

            final CustomMaterial material=new CustomMaterial(cursor.getString(1));

            material.setId(cursor.getInt(0));
            material.setType(cursor.getString(2));
            material.setFeets(cursor.getFloat(3));
            material.setMeters(cursor.getFloat(4));
            material.setDealership(cursor.getString(5));
            material.setSeasons(cursor.getString(6));

            if (cursor.getBlob(7) != null) {

                byte[] blob = cursor.getBlob(7);
               final Bitmap bitmap = BitmapFactory.decodeByteArray(blob, 0, blob.length);
               final Drawable drawable = new BitmapDrawable(Resources.getSystem(), bitmap);
                material.setImage(drawable);

            }

            material.setDate(cursor.getString(8));

            material.setDescription(cursor.getString(9));

            if (cursor.getBlob(10) != null) {
                final List<String> modelsId = (List<String>) byteArrayToObject(cursor.getBlob(10));
                material.setModelIdRelation(modelsId);

            }

            materialBuffer.add(material);


        }
        cursor.close();

        return materialBuffer;



    }

    public List<CustomMaterial>getCustomMaterialByType(String type){

        final List<CustomMaterial>materialBuffer=new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();


         String filter = CUSTOM_MATERIAL_COLUMN_TYPE;


        Cursor cursor = db.rawQuery(
                "select * from " + TABLE_CUSTOM_MATERIAL + " where " + filter + "= ?", new String[]{type});

        while (cursor.moveToNext()) {

            final CustomMaterial material=new CustomMaterial(cursor.getString(1));

            material.setId(cursor.getInt(0));
            material.setType(cursor.getString(2));
            material.setFeets(cursor.getFloat(3));
            material.setMeters(cursor.getFloat(4));
            material.setDealership(cursor.getString(5));
            material.setSeasons(cursor.getString(6));

            if (cursor.getBlob(7) != null) {

                byte[] blob = cursor.getBlob(7);
             final    Bitmap bitmap = BitmapFactory.decodeByteArray(blob, 0, blob.length);
              final   Drawable drawable = new BitmapDrawable(Resources.getSystem(), bitmap);
                material.setImage(drawable);

            }

            material.setDate(cursor.getString(8));

            material.setDescription(cursor.getString(9));

            if (cursor.getBlob(10) != null) {
                final List<String> modelsId = (List<String>) byteArrayToObject(cursor.getBlob(10));
                material.setModelIdRelation(modelsId);

            }

            materialBuffer.add(material);


        }
        cursor.close();

        return materialBuffer;



    }

    public boolean addRelationWithCustomMaterial(@NonNull int modelId, @NonNull String customId) {


        final CustomMaterial customMaterial = getCustomMaterial(Integer.valueOf(customId));

        final List<String> modelIdRelation = customMaterial.getModelIdRelation();

        boolean duplicated = false;
        if (!modelIdRelation.isEmpty()) {

            for (String id : modelIdRelation) {

                if (id.equals(String.valueOf(modelId))) {

                    duplicated = true;
                    break;
                }

            }

        }

        if (!duplicated) {
            customMaterial.getModelIdRelation().add(String.valueOf(modelId));
            updateCustomMAterial(customMaterial);

            return true;
        }


        return false;
    }

    public boolean deleteRelationWithCustomMaterial(@NonNull Material material) {
        boolean relationIn = false;

        if (!material.getCustomMaterialId().equals("0")) {
            final List<Material> materialList = getModelMaterial(material.getModelId());

            for (Material material1 : materialList) {

                if ((material1.getCustomMaterialId().equals(material.getCustomMaterialId())) &&
                        (material1.getId() != material.getId())) {

                    relationIn = true;

                    break;
                }
            }

            if (!relationIn) {

                final CustomMaterial custommaterial = getCustomMaterial(Integer.valueOf(material.getCustomMaterialId()));

                final List<String> modelsId = custommaterial.getModelIdRelation();


                for (int i = modelsId.size() - 1; i >= 0; i--) {
                    if (modelsId.get(i).equals(String.valueOf(material.getModelId()))) {


                        modelsId.remove(i);
                        break;
                    }

                }

                custommaterial.setModelIdRelation(modelsId);

                updateCustomMAterial(custommaterial);
                return true;
            }


        }


        return false;


    }


    ////////////CUTNOTES//////////////////////

    public List<CutNoteList> getAllCutNotes() {

        List<CutNoteList> cutNotesBuffer = null;


        String selectQuery = "SELECT  * FROM " + TABLE_CUTNOTE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        try {

            cutNotesBuffer = new ArrayList<>();
            if (cursor.moveToFirst()) {
                do {


                    CutNoteList cutNoteList=new CutNoteList(cursor.getPosition());
                    cutNoteList.setId(cursor.getInt(0));
                    cutNoteList.setModelId(cursor.getInt(1));
                    cutNoteList.setModel(cursor.getString(2));
                    cutNoteList.setReference(cursor.getInt(3));
                    if (cursor.getBlob(4) != null) {

                        final List<CutNote> cutNotes=(List<CutNote>) Utils.jsonToObject((String)byteArrayToObject(cursor.getBlob(4)));
                        cutNoteList.addNote(cutNotes);
                    }
                    cutNoteList.setStatus(cutNoteStatus.valueOf(cursor.getString(5)));

                    cutNoteList.setDate(cursor.getString(6));

                    cutNotesBuffer.add(cutNoteList);

                } while (cursor.moveToNext());
            }
            cursor.close();

        } catch (Exception e) {
            Utils.toast(mContext, e.toString());
        }


        return cutNotesBuffer;
    }

    public long addCutNoteList(CutNoteList cutNoteList){




        return insertCutNoteList(cutNoteList.getModelId(),cutNoteList.getModel(),cutNoteList.getReference(),
                prepareObjectToByte(Utils.objectToJson(cutNoteList.getCutNoteList())),cutNoteList.getStatus().name(),cutNoteList.getDate());



    }

    private long insertCutNoteList(@NonNull int modelId,String modelName,@NonNull int reference,byte[] cutNotes,String status,String date){



        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(CUTNOTE_COLUMN_MODEL_ID,modelId);
        contentValues.put(CUTNOTE_COLUMN_MODEL_NAME, modelName);
        contentValues.put(CUTNOTE_COLUMN_REFERENCE, reference);
        contentValues.put(CUTNOTE_COLUMN_NOTES, cutNotes);
        contentValues.put(CUTNOTE_COLUMN_STATUS, status);
        contentValues.put(CUTNOTE_COLUMN_DATE, date);



        return db.insert(TABLE_CUTNOTE, null, contentValues);



    }

    public CutNoteList getCutNoteListById(int cutNoteId){

        CutNoteList cutNoteList=null;

        SQLiteDatabase db = this.getWritableDatabase();


        String filter = CUTNOTE_COLUMN_ID;


        Cursor cursor = db.rawQuery(
                "select * from " + TABLE_CUTNOTE + " where " + filter + "= ?", new String[]{String.valueOf(cutNoteId)});

        while (cursor.moveToNext()) {

            cutNoteList=new CutNoteList(cursor.getPosition());
            cutNoteList.setId(cursor.getInt(0));
            cutNoteList.setModelId(cursor.getInt(1));
            cutNoteList.setModel(cursor.getString(2));
            cutNoteList.setReference(cursor.getInt(3));
            if (cursor.getBlob(4) != null) {

                final List<CutNote> cutNotes=(List<CutNote>) byteArrayToObject(cursor.getBlob(4));
                cutNoteList.addNote(cutNotes);
            }
            cutNoteList.setStatus(cutNoteStatus.valueOf(cursor.getString(5)));

            cutNoteList.setDate(cursor.getString(6));

        }
        cursor.close();



        return  cutNoteList;
    }

    public List<CutNoteList> getCutNoteListByModel(int modelId){

        List<CutNoteList> cutNoteList=new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();


        String filter = CUTNOTE_COLUMN_MODEL_ID;


        Cursor cursor = db.rawQuery(
                "select * from " + TABLE_CUTNOTE + " where " + filter + "= ?", new String[]{String.valueOf(modelId)});

        while (cursor.moveToNext()) {

           CutNoteList cutNotes=new CutNoteList(cursor.getPosition());
            cutNotes.setId(cursor.getInt(0));
            cutNotes.setModelId(cursor.getInt(1));
            cutNotes.setModel(cursor.getString(2));
            cutNotes.setReference(cursor.getInt(3));
            if (cursor.getBlob(4) != null) {

                final List<CutNote> cutNote=(List<CutNote>) byteArrayToObject(cursor.getBlob(4));
                cutNotes.addNote(cutNotes);
            }
            cutNotes.setStatus(cutNoteStatus.valueOf(cursor.getString(5)));

            cutNotes.setDate(cursor.getString(6));

            cutNoteList.add(cutNotes);

        }
        cursor.close();



        return  cutNoteList;
    }

    public boolean deleteCutNoteList(int cutNoteId){
        SQLiteDatabase db = this.getWritableDatabase();
        String filter = "_id=";

        return db.delete(TABLE_CUTNOTE, filter + "=" + String.valueOf(cutNoteId), null) > 0;




    }

    public List<CutNoteList> getLastesCutNoteList(int count) {

        int i = 0;

        List<CutNoteList> cutNotesBuffer = new ArrayList<>();


        String selectQuery = "SELECT  * FROM " + TABLE_CUTNOTE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        try {
            if (cursor.moveToLast()) {

                do {


                    if (i < count) {
                        CutNoteList cutNoteList=new CutNoteList(cursor.getPosition());
                        cutNoteList.setId(cursor.getInt(0));
                        cutNoteList.setModelId(cursor.getInt(1));
                        cutNoteList.setModel(cursor.getString(2));
                        cutNoteList.setReference(cursor.getInt(3));
                        if (cursor.getBlob(4) != null) {

                            final List<CutNote> cutNotes=(List<CutNote>) Utils.jsonToObject((String)byteArrayToObject(cursor.getBlob(4)));
                            cutNoteList.addNote(cutNotes);
                        }
                        cutNoteList.setStatus(cutNoteStatus.valueOf(cursor.getString(5)));

                        cutNoteList.setDate(cursor.getString(6));

                        cutNotesBuffer.add(cutNoteList);
                        i = i + 1;
                    } else {

                        break;
                    }


                    // get the data into array, or class variable
                } while (

                        cursor.moveToPrevious());
            }
            cursor.close();

        } catch (Exception e) {
            Utils.toast(mContext, e.toString());
        }

        return cutNotesBuffer;
    }


    ////////////MATERIALS TYPES//////////////

    public List<String> getMaterialsTypes() {

        List<String> types = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_CUSTOM_MATERIAL;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        HashSet hs = new HashSet();

        try {
            if (cursor.moveToFirst()) {
                do {


                    hs.add(cursor.getString(2));

                } while (cursor.moveToNext());
            }
            cursor.close();

        } catch (Exception e) {
            Utils.toast(mContext, e.toString());
        }

        types.addAll(hs);
        Collections.sort(types);

        return types;
    }

    ///////////DEALERSHIPS//////////////////////

    public List<Dealership> getDealerShips() {

        List<Dealership> dealers = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_DEALERSHIP;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    final Dealership dealer = new Dealership();


                    dealer.setId(cursor.getInt(0));
                    dealer.setName(cursor.getString(1));
                    dealer.setPhone(cursor.getString(2));
                    dealer.setAdress(cursor.getString(3));
                    dealer.setEmail(cursor.getString(4));
                    dealer.setCategory(cursor.getString(5));
                    dealer.setCompany(cursor.getString(6));
                    dealer.setDate(cursor.getString(7));

                    dealers.add(dealer);

                } while (cursor.moveToNext());
            }
            cursor.close();

        } catch (Exception e) {
            Utils.toast(mContext, e.toString());
        }


        return dealers;
    }

    public boolean addDealership(Dealership dealership) {


        if (dealership.getCategory() == null) {


            dealership.setCategory(mContext.getResources().getString(R.string.importNoAsigned_Cat));

        }


        insertDealership(dealership.getName(), dealership.getPhone(), dealership.getAdress(), dealership.getEmail(), dealership.getCategory(), dealership.getCompany(), getDate());

        return true;
    }

    public boolean insertDealership(String name, String phone, String adress, String email, String category, String company, String date) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DEALER_COLUMN_NAME, name);
        contentValues.put(DEALER_COLUMN_PHONE, phone);
        contentValues.put(DEALER_COLUMN_ADRESS, adress);
        contentValues.put(DEALER_COLUMN_EMAIL, email);
        contentValues.put(DEALER_COLUMN_CATEGORY, category);
        contentValues.put(DEALER_COLUMN_COMPANY, company);
        contentValues.put(DEALER_COLUMN_DATE, date);
        db.insert(TABLE_DEALERSHIP, null, contentValues);
        return true;
    }

    public boolean updateDealership(int id, String name, @Nullable String phone, @Nullable String adress, @Nullable String email, @Nullable String category, @Nullable String company, @Nullable String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        String filter = "_id=";
        ContentValues contentValues = new ContentValues();
        contentValues.put(DEALER_COLUMN_NAME, name);
        if (phone != null) {
            contentValues.put(DEALER_COLUMN_PHONE, phone);
        }
        if (adress != null) {

            contentValues.put(DEALER_COLUMN_ADRESS, adress);
        }
        if (email != null) {

            contentValues.put(DEALER_COLUMN_EMAIL, email);
        }
        if (category != null) {

            contentValues.put(DEALER_COLUMN_CATEGORY, category);
        }
        if (company != null) {

            contentValues.put(DEALER_COLUMN_COMPANY, company);
        }
        if (date != null) {

            contentValues.put(DEALER_COLUMN_DATE, date);
        }
        return db.update(TABLE_DEALERSHIP, contentValues, filter + "=" + id, null) > 0;
    }

    public boolean deleteDealership(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String filter = "_id=";
        return db.delete(TABLE_DEALERSHIP, filter + "=" + id, null) > 0;
    }

    public boolean deleteDealership(String name) {
        SQLiteDatabase db = this.getWritableDatabase();


        Cursor row = null;
        String query;

        final List<Integer> index = new ArrayList<>();

        Cursor c = db.rawQuery(
                "select * from " + TABLE_DEALERSHIP + " where name = ?", new String[]{name});

        while (c.moveToNext()) {

            deleteCategory(c.getInt(0));


        }


        return false;


    }

    public int getDealerShipCount() {

        String countQuery = "SELECT  * FROM " + TABLE_DEALERSHIP;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        final int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    public Dealership getDealerShip(int id) {

        final Dealership dealer = new Dealership();

        String selectQuery = "SELECT  * FROM " + TABLE_DEALERSHIP;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        try {
            if (cursor.moveToFirst()) {
                do {


                    if (id == cursor.getInt(0)) {

                        dealer.setId(cursor.getInt(0));
                        dealer.setName(cursor.getString(1));
                        dealer.setPhone(cursor.getString(2));
                        dealer.setAdress(cursor.getString(3));
                        dealer.setEmail(cursor.getString(4));
                        dealer.setCategory(cursor.getString(5));
                        dealer.setCompany(cursor.getString(6));
                        dealer.setDate(cursor.getString(7));

                        cursor.close();

                        break;
                    }

                } while (cursor.moveToNext());
            }
            cursor.close();

        } catch (Exception e) {
            Utils.toast(mContext, "No se puede obtener el proveedor" + e.toString());
        }


        return dealer;
    }

    public boolean updateAllMaterialDealership(String oldDealer, String newDealer) {
        SQLiteDatabase db = this.getWritableDatabase();


        Cursor row = null;
        String query;

        final List<Integer> index = new ArrayList<>();

        Cursor c = db.rawQuery(
                "select * from " + TABLE_CUSTOM_MATERIAL + " where category = ?", new String[]{oldDealer});

        while (c.moveToNext()) {


            String filter = "_id=";
            ContentValues contentValues = new ContentValues();
            contentValues.put(CUSTOM_MATERIAL_COLUMN_DEALERSHIP, newDealer);

            db.update(TABLE_CUSTOM_MATERIAL, contentValues, filter + "=" + c.getInt(0), null);


        }

        return false;
    }

    public List<String> getDealersCategories() {

        final List<String> titleList = new ArrayList<>();


        String selectQuery = "SELECT  * FROM " + TABLE_DEALERSHIP;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        try {
            if (cursor.moveToFirst()) {
                do {


                    titleList.add(cursor.getString(5));

                } while (cursor.moveToNext());
            }
            cursor.close();

        } catch (Exception e) {
            Utils.toast(mContext, e.toString());
        }

        HashSet hs = new HashSet();
        hs.addAll(titleList);
        titleList.clear();
        titleList.addAll(hs);
        Collections.sort(titleList);

        return titleList;

    }

    ///////////ACCESSORIES/////////////////////////////////////////////////////////

    public List<Accessories> getAccessories() {

        List<Accessories> accessoriesList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_ACCESSORIES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    final Accessories accessory = new Accessories();


                    accessory.setId(cursor.getInt(0));
                    accessory.setModelId(cursor.getInt(1));
                    accessory.setName(cursor.getString(2));
                    accessory.setDealer(cursor.getString(3));
                    accessory.setType(cursor.getString(4));
                    accessory.setDescription(cursor.getString(5));
                    accessory.setDate(cursor.getString(6));

                    if(cursor.getBlob(7)!=null){
                        accessory.setImage((Drawable)byteArrayToObject(cursor.getBlob(7)));

                    }

                    accessoriesList.add(accessory);

                } while (cursor.moveToNext());
            }
            cursor.close();

        } catch (Exception e) {
            Utils.toast(mContext, e.toString());
        }


        return accessoriesList;
    }

    public boolean deleteAccessory(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String filter = "_id=";
        return db.delete(TABLE_ACCESSORIES, filter + "=" + id, null) > 0;
    }

    ///////////EXTERNALWORKS/////////////////////////////

    public List<ExternalWorks> getExternalWorks() {

        List<ExternalWorks> externalWorksList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_EXTERNALWORKS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    final ExternalWorks external = new ExternalWorks();


                    external.setId(cursor.getInt(0));
                    external.setModelId(cursor.getInt(1));
                    external.setPieceId(cursor.getInt(2));
                    external.setExternal(cursor.getString(3));
                    external.setType(cursor.getString(4));
                    external.setDescription(cursor.getString(5));
                    external.setDate(cursor.getString(6));
                    external.setCompleted(cursor.getInt(7));

                    externalWorksList.add(external);

                } while (cursor.moveToNext());
            }
            cursor.close();

        } catch (Exception e) {
            Utils.toast(mContext, e.toString());
        }


        return externalWorksList;
    }

    public boolean deleteExternalWorks(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String filter = "_id=";
        return db.delete(TABLE_EXTERNALWORKS, filter + "=" + id, null) > 0;
    }



    //////////////RELATIONS/////////////////////////////////////////

    private void addModelAtRelationTable(int modelId){

        if(!(isModelExist(modelId))){



            SQLiteDatabase db = getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(RELATIONS_COLUMN_MODEL_ID,modelId);

            db.insert(TABLE_RELATIONS, null, contentValues);

        }





    }

    private void deleteModelAtRelationTable(int modelId){



        SQLiteDatabase db = this.getWritableDatabase();


        String filter = RELATIONS_COLUMN_MODEL_ID;


        Cursor cursor = db.rawQuery(
                "select * from " + TABLE_RELATIONS + " where " + filter + "= ?", new String[]{String.valueOf(modelId)});


        while (cursor.moveToNext()) {
            String aux = "_id=";

            db.delete(TABLE_RELATIONS, aux + "=" + String.valueOf(cursor.getInt(0)), null);



        }
        cursor.close();





    }

    private boolean isModelExist(int modelId){

        SQLiteDatabase db = this.getWritableDatabase();


        String filter = RELATIONS_COLUMN_MODEL_ID;


        Cursor cursor = db.rawQuery(
                "select * from " + TABLE_RELATIONS + " where " + filter + "= ?", new String[]{String.valueOf(modelId)});


        while (cursor.moveToNext()) {

            return true;
        }
        cursor.close();


        return false;
    }

    private boolean isRelationExistAtModel(RelationType relationType,int modelId,int itemId){
        SQLiteDatabase db = this.getWritableDatabase();
        String filter=RELATIONS_COLUMN_MODEL_ID;
        int columnIndex=0;


        Cursor cursor = db.rawQuery(
                "select * from " + TABLE_RELATIONS + " where " + filter + "= ?", new String[]{String.valueOf(modelId)});

        switch ( relationType){




            case DEALER:

                columnIndex=2;

                break;


            case CUTNOTE:
                columnIndex=3;

                break;

            case EXTERNAL:
                columnIndex=4;
                break;

            case ACCESSORY:
                columnIndex=5;
                break;

        }



        while (cursor.moveToNext()) {



            if(cursor.getBlob(columnIndex)!=null){

                final List<Integer> idList=(List<Integer>) byteArrayToObject(cursor.getBlob(columnIndex));

                for( Integer id :idList){


                    if(id==itemId){

                        return true;
                    }


                }


            }else{


                return false;
            }

        }
        cursor.close();


        return false;

    }

    public void addRelationAtModel(RelationType relationType,int modelId,int itemId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String valueFilter=null;
        int columnIndex = 0;
        List<Integer> idList=null;


        switch (relationType) {


            case DEALER:

                columnIndex = 2;
                valueFilter = RELATIONS_COLUMN_DEALERS;

                break;


            case CUTNOTE:

                valueFilter = RELATIONS_COLUMN_CUTNOTES;
                columnIndex = 3;

                break;

            case EXTERNAL:

                valueFilter = RELATIONS_COLUMN_EXTERNALS;
                columnIndex = 4;
                break;

            case ACCESSORY:

                valueFilter = RELATIONS_COLUMN_ACCESSORIES;
                columnIndex = 5;
                break;

        }


        if (!(isRelationExistAtModel(relationType, modelId, itemId))) {

            Cursor cursor = db.rawQuery(
                    "select * from " + TABLE_RELATIONS + " where " + RELATIONS_COLUMN_MODEL_ID + "= ?", new String[]{String.valueOf(modelId)});

            while (cursor.moveToNext()) {

                if (cursor.getBlob(columnIndex) != null) {

                    idList = (List<Integer>) byteArrayToObject(cursor.getBlob(columnIndex));


                    idList.add(itemId);


                } else {

                    idList = new ArrayList<>();

                    idList.add(itemId);

                }

            }
            cursor.close();


            ContentValues contentValues= new ContentValues();

            contentValues.put(valueFilter,prepareObjectToByte(idList));

            db.update(TABLE_RELATIONS,contentValues,RELATIONS_COLUMN_MODEL_ID +"=="+ String.valueOf(modelId),null);

        }




    }

    public void deleteRelationAtModel(RelationType relationType,int modelId,int itemId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String valueFilter=null;
        int columnIndex = 0;
        List<Integer> idList=null;


        switch (relationType) {



            case DEALER:

                columnIndex = 2;
                valueFilter = RELATIONS_COLUMN_DEALERS;

                break;


            case CUTNOTE:

                valueFilter = RELATIONS_COLUMN_CUTNOTES;
                columnIndex = 3;

                break;

            case EXTERNAL:

                valueFilter = RELATIONS_COLUMN_EXTERNALS;
                columnIndex = 4;
                break;

            case ACCESSORY:

                valueFilter = RELATIONS_COLUMN_ACCESSORIES;
                columnIndex = 5;
                break;

        }


        if ((isRelationExistAtModel(relationType, modelId, itemId))) {

            Cursor cursor = db.rawQuery(
                    "select * from " + TABLE_RELATIONS + " where " + RELATIONS_COLUMN_MODEL_ID + "= ?", new String[]{String.valueOf(modelId)});

            while (cursor.moveToNext()) {

                if (cursor.getBlob(columnIndex) != null) {

                    idList = (List<Integer>) byteArrayToObject(cursor.getBlob(columnIndex));

                    for(int i=idList.size()-1;i>=0;i--){

                        if(itemId==idList.get(i)){

                            idList.remove((int)i);

                        }


                    }

                }
            }
            cursor.close();


            ContentValues contentValues= new ContentValues();

            contentValues.put(valueFilter,prepareObjectToByte(idList));

            db.update(TABLE_RELATIONS,contentValues,RELATIONS_COLUMN_MODEL_ID +"=="+ String.valueOf(modelId),null);

        }




    }

    public void deleteRelationAtAllModels(RelationType relationType,int itemId) {
        String valueFilter=null;
        int columnIndex = 0;
        List<Integer> idList=null;


        switch (relationType) {



            case DEALER:

                columnIndex = 2;
                valueFilter = RELATIONS_COLUMN_DEALERS;

                break;


            case CUTNOTE:

                valueFilter = RELATIONS_COLUMN_CUTNOTES;
                columnIndex = 3;

                break;

            case EXTERNAL:

                valueFilter = RELATIONS_COLUMN_EXTERNALS;
                columnIndex = 4;
                break;

            case ACCESSORY:

                valueFilter = RELATIONS_COLUMN_ACCESSORIES;
                columnIndex = 5;
                break;

        }



        String selectQuery = "SELECT  * FROM " + TABLE_RELATIONS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        try {
            if (cursor.moveToFirst()) {
                do {

                    final int modelId=cursor.getInt(1);

                    if ((isRelationExistAtModel(relationType, modelId, itemId))) {

                        SQLiteDatabase db2 = this.getWritableDatabase();

                        Cursor cursor2 = db2.rawQuery(
                                "select * from " + TABLE_RELATIONS + " where " + RELATIONS_COLUMN_MODEL_ID + "= ?", new String[]{String.valueOf(modelId)});

                        while (cursor2.moveToNext()) {

                            if (cursor2.getBlob(columnIndex) != null) {

                                idList = (List<Integer>) byteArrayToObject(cursor2.getBlob(columnIndex));

                                for (int i = idList.size() - 1; i >= 0; i--) {

                                    if (itemId == idList.get(i)) {

                                        idList.remove((int) i);

                                    }


                                }

                            }
                        }
                        cursor2.close();


                        ContentValues contentValues = new ContentValues();

                        contentValues.put(valueFilter, prepareObjectToByte(idList));

                        db2.update(TABLE_RELATIONS, contentValues, RELATIONS_COLUMN_MODEL_ID + "==" + String.valueOf(modelId), null);
                    }

                } while (cursor.moveToNext());
            }
            cursor.close();

        } catch (Exception e) {
            Utils.toast(mContext, "No se encuentra el material" + e.toString());
        }


    }


    //////////////////UTILS/////////////////////////////////////////////////////////////

    public byte[] prepareObjectToByte(Object object) {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = null;
        byte[] container = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(object);
            out.flush();
            container = bos.toByteArray();

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Import", "FALLO EN LA CONVERISON A BYTE");
            Log.e("Import", e.toString());

        } finally {
            try {
                bos.close();
            } catch (IOException ex) {


                // ignore close exception
            }
        }
        return container;

    }

    public static byte[] imageViewtoByte(Drawable image) {

        Bitmap bitmap = ((BitmapDrawable) image).getBitmap();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;


    }

    public static Drawable byteToDrawable(byte [] data){


        Bitmap b1=BitmapFactory.decodeByteArray(data, 0, data.length);

        return   new BitmapDrawable(b1);

    }

    public static Object byteArrayToObject(byte[] yourBytes) {

        Object o = null;
        ByteArrayInputStream bis = new ByteArrayInputStream(yourBytes);
        ObjectInput in = null;
        try {
            in = new ObjectInputStream(bis);
            o = in.readObject();

        } catch (ClassNotFoundException e) {
            Log.e("Import", "FALLO EN LA CONVERISON A STRING");
            Log.e("Import", e.toString());
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Import", "FALLO EN LA CONVERISON A STRING");
            Log.e("Import", e.toString());
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                // ignore close exception
            }
        }
        return o;
    }

    public static void restart(Context context, int delay) {
        if (delay == 0) {
            delay = 1;
        }
        Log.e("", "restarting app");
        Intent restartIntent = context.getPackageManager()
                .getLaunchIntentForPackage(context.getPackageName());
        PendingIntent intent = PendingIntent.getActivity(
                context, 0,
                restartIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.set(AlarmManager.RTC, System.currentTimeMillis() + delay, intent);
        System.exit(2);
    }

    public String getDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy\nHH:mm:ss");
        Date auxdate = new Date();

        return dateFormat.format(auxdate);
    }




}


