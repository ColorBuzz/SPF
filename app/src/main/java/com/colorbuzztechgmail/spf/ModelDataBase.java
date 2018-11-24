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
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.SortedList;
import android.util.Log;
import android.util.SparseBooleanArray;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.colorbuzztechgmail.spf.CutNote.cutNoteStatus;
import com.google.gson.reflect.TypeToken;

/**
 * Created by PC01 on 29/11/2017.
 */

public class ModelDataBase extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "blog1.db";
    public static final int DATABASE_VERSION = 1;

    public enum RelationType{
        DEALER,
        CUTNOTE,
        EXTERNAL,
        ACCESSORY
    }

   ///DIRECTORY TABLE

    public static final String TABLE_DIRECTORY = "directory";

    public static final String DIRECTORY_COLUMN_NAME = "name";


    //// MODEL TABLE
    public static final String TABLE_MODEL = "model";
    public static final String KEY_ID = "_id"; //0
    public static final String MODEL_COLUMN_NAME = "model_name";//1
    public static final String MODEL_COLUMN_SIZES = "sizes";//2
    public static final String MODEL_COLUMN_BASE_SIZE = "base_size";//3
    public static final String MODEL_COLUMN_PARENT = "parent";//4
    public static final String MODEL_COLUMN_IMAGE = "image";//5
    public static final String MODEL_COLUMN_DATE = "date";//6
    public static final String MODEL_COLUMN_DESCRIPT = "description";//7

    //// PIECE MODEL TABLE
    public static final String TABLE_PIECE = "pieces_";
    public static final String PIECE_COLUMN_MODEL_ID = "modelId"; //1
    public static final String PIECE_COLUMN_NAME = "piece_name";//2
    public static final String PIECE_COLUMN_AMOUNT = "amount";//3
    public static final String PIECE_COLUMN_AMOUNT_MIRROR = "amount_mirror";//4
    public static final String PIECE_COLUMN_SIZE = "size";//5
    public static final String PIECE_COLUMN_MATERIAL = "material";//6
    public static final String PIECE_COLUMN_AMOUNT_MATERIAL = "amount_material";//7
    public static final String PIECE_COLUMN_TOOLS = "tools";//8
    public static final String PIECE_COLUMN_IMAGE = "image";//9
    public static final String PIECE_COLUMN_DESCRIPT = "description";//10
    public static final String PIECE_COLUMN_RAWDATA = "rawdata";//11


    ///MATERIAL TABLE


    public static final String TABLE_MATERIAL = "materials_";
    public static final String MATERIAL_COLUMN_MODEL_ID = "modelId"; //1
    public static final String MATERIAL_COLUMN_NAME = "material_name";//2
    public static final String MATERIAL_COLUMN_COLOR = "color";//4



    ///  CUSTOM MATERIAL TABLE


    public static final String TABLE_CUSTOM_MATERIAL = "custom_material";
    public static final String CUSTOM_MATERIAL_COLUMN_NAME = "custom_material_name";//1
    public static final String CUSTOM_MATERIAL_COLUMN_FEETS = "feets";//2
    public static final String CUSTOM_MATERIAL_COLUMN_SEASONS = "seasons";//3
    public static final String CUSTOM_MATERIAL_COLUMN_IMAGE = "image";//4
    public static final String CUSTOM_MATERIAL_COLUMN_DATE = "date";//5
    public static final String CUSTOM_MATERIAL_COLUMN_DESCRIPTION = "description";//6

    /// MATERIALS TYPES


    public static final String TABLE_MATERIAL_TYPES = "material_types";
    public static final String MATERIAL_TYPE_COLUMN_NAME= "type";



    ///  DEALERSHIP TABLE

    public static final String TABLE_DEALERSHIP = "dealership";
    public static final String DEALER_COLUMN_NAME = "dealer_name";//1
    public static final String DEALER_COLUMN_PHONE = "phone";//2
    public static final String DEALER_COLUMN_ADRESS = "adress";//3
    public static final String DEALER_COLUMN_EMAIL = "email";//4
    public static final String DEALER_COLUMN_CATEGORY = "category";//5
    public static final String DEALER_COLUMN_COMPANY = "company";//6
    public static final String DEALER_COLUMN_DATE = "date";//7

    /// EXTERNALWORKS  TABLE

    public static final String TABLE_EXTERNALWORKS = "external_works";
    public static final String EXTERNALWORKS_COLUMN_NAME = "external";//3
    public static final String EXTERNALWORKS_COLUMN_TYPE = "type";//4
    public static final String EXTERNALWORKS_COLUMN_DESCRIPTION = "description";//5
    public static final String EXTERNALWORKS_COLUMN_DATE = "date";//6
    public static final String EXTERNALWORKS_COLUMN_COMPLETED = "completed";//7

    /// CUTNOTESLIST  & CUTNOTELIST TABLE


    public static final String TABLE_CUNOTE_LIST = "cutnotesList";
    public static final String CUTNOTE_COLUMN_REFERENCE = "reference";//2
    public static final String CUTNOTE_COLUMN_STATUS = "status";//4
    public static final String CUTNOTE_COLUMN_PAIR_COUNT = "pair_count";//5
    public static final String CUTNOTE_COLUMN_MAX_PAIR_COUNT = "max_pair_count";//6


    public static final String CUTNOTE_COLUMN_DATE = "date";//7


    public static final String TABLE_CUNOTE = "cutnotes";
    public static final String CUTNOTE_COLUMN_NOTE_NUMBER = "note_number";//2
    public static final String CUTNOTE_COLUMN_NOTES = "notes";//2


///  CUSTUMER TABLE

    public static final String TABLE_CUSTUMER = "custumers";
    public static final String CUSTUMER_COLUMN_NAME = "custumer_name";//1
    public static final String CUSTUMER_COLUMN_PHONE = "phone";//2
    public static final String CUSTUMER_COLUMN_ADRESS = "adress";//3
    public static final String CUSTUMER_COLUMN_EMAIL = "email";//4
    public static final String CUSTUMER_COLUMN_COMPANY = "company";//5
    public static final String CUSTUMER_COLUMN_DATE = "date";//6


    /// ACCESSORIES  TABLE

    public static final String TABLE_ACCESSORIES = "accessories";
    public static final String ACCESSORIES_COLUMN_NAME = "_name";//1
    public static final String ACCESSORIES_COLUMN_TYPE = "type";//4
    public static final String ACCESSORIES_COLUMN_DESCRIPTION = "description";//5
    public static final String ACCESSORIES_COLUMN_DATE = "date";//6
    public static final String ACCESSORIES_COLUMN_IMAGE = "image";//7



    ///CUTNOTELIST REFERENCE
    public static final String TABLE_REFERENCE = "reference";
    public static final String REFERENCE_COLUMN_VALUE = "_value";//1


    /// RELATIONS  TABLE


    public static String TABLE_MODEL_TABLES_RELATION = "model_tables_relation";


    public static final String TABLE_MODEL__PREVIEW_PIECES_RELATIONS = "model_preview_pieces_relations";

    public static final String TABLE_MODEL_DIRECTORY_RELATIONS = "model_category_relations";

    public static final String TABLE_MODEL_CUSTUMER_RELATIONS = "model_custumer_relations";

    public static final String TABLE_MODEL_CUTNOTE_LIST_RELATIONS = "model_cutnote_relations";

    public static final String TABLE_MODEL_DEALERS_RELATIONS = "model_dealers_relations";

    public static final String TABLE_MODEL_ACCESSORIES_RELATIONS = "model_accessories_relations";

    public static final String TABLE_MODEL_EXTERNAL_RELATIONS = "model_external_relations";

    public static final String TABLE_MATERIAL_CUSTOM_MATERIAL_RELATIONS = "material_custom_material_relations";

    public static final String TABLE_CUTNOTE_CUTNOTE_LIST_RELATIONS = "cutnote_cutnote_list_relations";

    public static final String TABLE_DEALER_CUSTOM_MATERIAL_RELATIONS = "dealer_custom_material_relations";

    public static final String TABLE_MATERIAL_TYPES_CUSTOM_MATERIAL_RELATIONS = "material_types_custom_material_relations";

    public static final String KEY_MODEL_ID= "modelId";
    public static final String KEY_DIRECTORY_ID= "directoryId";
    public static final String KEY_CUSTUMER_ID= "custumerId";
    public static final String KEY_CUTNOTE_ID= "cutnoteId";
    public static final String KEY_CUTNOTE_LIST_ID= "cutnotelistId";
    public static final String KEY_DEALER_ID= "dealerId";
    public static final String KEY_MATERIAL_ID= "materialId";
    public static final String KEY_MATERIAL_TYPE_ID= "materialTypeId";
    public static final String KEY_CUSTOM_MATERIAL_ID= "custommaterialId";
    public static final String KEY_ACCESSORIES_ID= "accessoriesId";
    public static final String KEY_EXTERNAL_ID= "externalId";
    public static final String KEY_PIECE_ID= "pieceId";



    /////////////---------FTS TABLES------------------------//////////

    public static final String FTS_MODEL="fts_model";

    public static final String FTS_CUSTOM_MATERIAL="fts_custom_material";

    public static final String FTS_CUTNOTE_LIST="fts_cutnote_list";




    ///////////----------------- TRIGGERS---------------//////////////////
    public static final String TRG_CM_BU="cm_bu";
    public static final String TRG_CM_BD="cm_bd";
    public static final String TRG_CM_AU="cm_au";
    public static final String TRG_CM_AI="cm_ai";

    public static final String TRG_MOD_BU="mod_bu";
    public static final String TRG_MOD_BD="mod_bd";
    public static final String TRG_MOD_AU="mod_au";
    public static final String TRG_MOD_AI="mod_ai";


    public static final String TRG_CTL_BU="ctl_bu";
    public static final String TRG_CTL_BD="ctl_bd";
    public static final String TRG_CTL_AU="ctl_au";
    public static final String TRG_CTL_AI="ctl_ai";

    public static final String TRG_DIR_AU="dir_au";
    public static final String TRG_CUS_AU="cus_au";
    public static final String TRG_MT_AU="mt_au";
    public static final String TRG_DLR_AU="dlr_au";


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

        db.execSQL("CREATE TABLE " + TABLE_MODEL + "(" +
                KEY_ID + " INTEGER PRIMARY KEY, " +
                MODEL_COLUMN_NAME + " TEXT, " +
                MODEL_COLUMN_SIZES + " BLOB, " +
                MODEL_COLUMN_BASE_SIZE + " TEXT, " +
                MODEL_COLUMN_PARENT + " TEXT, " +
                MODEL_COLUMN_IMAGE + " BLOB, " +
                MODEL_COLUMN_DATE + " TEXT, " +
                MODEL_COLUMN_DESCRIPT + " TEXT)"
        );


        db.execSQL("CREATE TABLE " + TABLE_CUSTUMER + "(" +
                KEY_ID + " INTEGER PRIMARY KEY, " +
                CUSTUMER_COLUMN_NAME + " TEXT, " +
                CUSTUMER_COLUMN_PHONE + " TEXT, " +
                CUSTUMER_COLUMN_ADRESS + " TEXT, " +
                CUSTUMER_COLUMN_EMAIL + " TEXT, " +
                CUSTUMER_COLUMN_COMPANY + " TEXT, " +
                CUSTUMER_COLUMN_DATE + " TEXT)");

        db.execSQL("CREATE TABLE " + TABLE_DIRECTORY + "(" +
                KEY_ID + " INTEGER PRIMARY KEY, " +
                DIRECTORY_COLUMN_NAME + " TEXT)"
        );

        db.execSQL("CREATE TABLE " + TABLE_MATERIAL_TYPES + "(" +
                KEY_ID + " INTEGER PRIMARY KEY, " +
                MATERIAL_TYPE_COLUMN_NAME + " TEXT)");

        db.execSQL("CREATE TABLE " + TABLE_CUSTOM_MATERIAL + "(" +
                KEY_ID + " INTEGER PRIMARY KEY, " +
                CUSTOM_MATERIAL_COLUMN_NAME + " TEXT, " +
                CUSTOM_MATERIAL_COLUMN_FEETS + " FLOAT, " +
                CUSTOM_MATERIAL_COLUMN_SEASONS + " TEXT, " +
                CUSTOM_MATERIAL_COLUMN_IMAGE + " BLOB, " +
                CUSTOM_MATERIAL_COLUMN_DATE + " TEXT, " +
                CUSTOM_MATERIAL_COLUMN_DESCRIPTION + " BLOB)");


        db.execSQL("CREATE TABLE " + TABLE_DEALERSHIP + "(" +
                KEY_ID + " INTEGER PRIMARY KEY, " +
                DEALER_COLUMN_NAME + " TEXT, " +
                DEALER_COLUMN_PHONE + " TEXT, " +
                DEALER_COLUMN_ADRESS + " TEXT, " +
                DEALER_COLUMN_EMAIL + " TEXT, " +
                DEALER_COLUMN_CATEGORY + " TEXT, " +
                DEALER_COLUMN_COMPANY + " TEXT, " +
                DEALER_COLUMN_DATE + " TEXT)");


        db.execSQL("CREATE TABLE " + TABLE_EXTERNALWORKS + "(" +
                KEY_ID + " INTEGER PRIMARY KEY, " +
                EXTERNALWORKS_COLUMN_NAME + " TEXT, " +
                EXTERNALWORKS_COLUMN_TYPE + " TEXT, " +
                EXTERNALWORKS_COLUMN_DESCRIPTION + " TEXT, " +
                EXTERNALWORKS_COLUMN_DATE + " TEXT, " +
                EXTERNALWORKS_COLUMN_COMPLETED + " INTEGER)");


        db.execSQL("CREATE TABLE " + TABLE_ACCESSORIES + "(" +
                KEY_ID + " INTEGER PRIMARY KEY, " +
                ACCESSORIES_COLUMN_NAME + " TEXT, " +
                ACCESSORIES_COLUMN_TYPE + " TEXT, " +
                ACCESSORIES_COLUMN_DESCRIPTION + " TEXT, " +
                ACCESSORIES_COLUMN_DATE + " TEXT, " +
                ACCESSORIES_COLUMN_IMAGE + " BLOB)");


        db.execSQL("CREATE TABLE " + TABLE_CUNOTE_LIST + "(" +
                KEY_ID + " INTEGER PRIMARY KEY, " +
                CUTNOTE_COLUMN_REFERENCE + " INTEGER, " +
                CUTNOTE_COLUMN_STATUS + " TEXT, " +
                CUTNOTE_COLUMN_MAX_PAIR_COUNT+ " INTEGER, " +
                CUTNOTE_COLUMN_PAIR_COUNT + " INTEGER, " +
                CUTNOTE_COLUMN_DATE + " TEXT)");

        db.execSQL("CREATE TABLE " + TABLE_CUNOTE + "(" +
                KEY_ID + " INTEGER PRIMARY KEY, " +
                KEY_CUTNOTE_LIST_ID + " INTEGER," +
                CUTNOTE_COLUMN_NOTE_NUMBER + " INTEGER, " +
                CUTNOTE_COLUMN_REFERENCE + " INTEGER, " +
                CUTNOTE_COLUMN_STATUS + " TEXT, " +
                CUTNOTE_COLUMN_NOTES + " BLOB, " +
                CUTNOTE_COLUMN_DATE + " TEXT)");


        db.execSQL("CREATE TABLE " + TABLE_REFERENCE + "(" +
                KEY_ID + " INTEGER PRIMARY KEY, " +
                REFERENCE_COLUMN_VALUE + " INTEGER)");


        ///--------------RELATIONS-----------------------------////////


        db.execSQL("CREATE TABLE " + TABLE_MODEL__PREVIEW_PIECES_RELATIONS + "(" +
                KEY_ID + " INTEGER PRIMARY KEY, " +
                KEY_MODEL_ID + " INTEGER, " +
                KEY_PIECE_ID + " INTEGER, " +
                KEY_MATERIAL_ID + " INTEGER)");


        db.execSQL("CREATE TABLE " + TABLE_MODEL_DIRECTORY_RELATIONS + "(" +
                KEY_ID + " INTEGER PRIMARY KEY, " +
                KEY_MODEL_ID + " INTEGER, " +
                KEY_DIRECTORY_ID + " INTEGER)");

        db.execSQL("CREATE TABLE " + TABLE_MODEL_CUSTUMER_RELATIONS + "(" +
                KEY_ID + " INTEGER PRIMARY KEY, " +
                KEY_MODEL_ID + " INTEGER, " +
                KEY_CUSTUMER_ID + " INTEGER)");

        db.execSQL("CREATE TABLE " + TABLE_MODEL_CUTNOTE_LIST_RELATIONS + "(" +
                KEY_ID + " INTEGER PRIMARY KEY, " +
                KEY_MODEL_ID + " INTEGER, " +
                KEY_CUTNOTE_LIST_ID + " INTEGER)");


        db.execSQL("CREATE TABLE " + TABLE_MODEL_DEALERS_RELATIONS + "(" +
                KEY_ID + " INTEGER PRIMARY KEY, " +
                KEY_MODEL_ID + " INTEGER, " +
                KEY_DEALER_ID + " INTEGER)");

        db.execSQL("CREATE TABLE " + TABLE_MODEL_ACCESSORIES_RELATIONS + "(" +
                KEY_ID + " INTEGER PRIMARY KEY, " +
                KEY_MODEL_ID + " INTEGER, " +
                KEY_ACCESSORIES_ID + " INTEGER)");

        db.execSQL("CREATE TABLE " + TABLE_MODEL_EXTERNAL_RELATIONS + "(" +
                KEY_ID + " INTEGER PRIMARY KEY, " +
                KEY_MODEL_ID + " INTEGER, " +
                KEY_EXTERNAL_ID + " INTEGER)");

       /* db.execSQL("CREATE TABLE " + TABLE_CUTNOTE_CUTNOTE_LIST_RELATIONS + "(" +
                KEY_ID + " INTEGER PRIMARY KEY, " +
                KEY_CUTNOTE_LIST_ID + " INTEGER, " +
                KEY_CUTNOTE_ID + " INTEGER)");
*/
        db.execSQL("CREATE TABLE " + TABLE_MATERIAL_CUSTOM_MATERIAL_RELATIONS + "(" +
                KEY_ID + " INTEGER PRIMARY KEY, " +
                KEY_MODEL_ID + " INTEGER, " +
                KEY_MATERIAL_ID + " INTEGER, " +
                KEY_CUSTOM_MATERIAL_ID + " INTEGER)");

        db.execSQL("CREATE TABLE " + TABLE_DEALER_CUSTOM_MATERIAL_RELATIONS + "(" +
                KEY_ID + " INTEGER PRIMARY KEY, " +
                KEY_DEALER_ID + " INTEGER, " +
                KEY_CUSTOM_MATERIAL_ID + " INTEGER)");

        db.execSQL("CREATE TABLE " + TABLE_MATERIAL_TYPES_CUSTOM_MATERIAL_RELATIONS + "(" +
                KEY_ID + " INTEGER PRIMARY KEY, " +
                KEY_MATERIAL_TYPE_ID + " INTEGER, " +
                KEY_CUSTOM_MATERIAL_ID + " INTEGER)");



        ////////////--------------------- FTS_TABLES--------------------///////////////////////


        db.execSQL("CREATE VIRTUAL TABLE " +  FTS_CUSTOM_MATERIAL + " USING fts3 ("+  CUSTOM_MATERIAL_COLUMN_NAME +"," + MATERIAL_TYPE_COLUMN_NAME
                + ","+  DEALER_COLUMN_NAME + "," + CUSTOM_MATERIAL_COLUMN_DESCRIPTION +","+
                CUSTOM_MATERIAL_COLUMN_SEASONS +")");

        db.execSQL("CREATE VIRTUAL TABLE " +  FTS_MODEL + " USING fts3 ("+  MODEL_COLUMN_NAME +"," + MODEL_COLUMN_BASE_SIZE
                + ","+  DIRECTORY_COLUMN_NAME + "," + MODEL_COLUMN_DESCRIPT +","+
                CUSTUMER_COLUMN_NAME +")");

        db.execSQL("CREATE VIRTUAL TABLE " +  FTS_CUTNOTE_LIST + " USING fts3 ("+  MODEL_COLUMN_NAME +"," + CUTNOTE_COLUMN_PAIR_COUNT
                + ","+  CUTNOTE_COLUMN_REFERENCE + "," + CUTNOTE_COLUMN_STATUS +","+
                CUSTUMER_COLUMN_NAME +")");
       /////////////--------------TRIGGERS------------//////////




        db.execSQL( "CREATE TRIGGER IF NOT EXISTS " +  TRG_MOD_BU + " BEFORE UPDATE ON " + TABLE_MODEL + " BEGIN DELETE FROM " + FTS_MODEL + " WHERE " + "docid=old." + KEY_ID + "; END;");

        db.execSQL( "CREATE TRIGGER IF NOT EXISTS " +  TRG_MOD_BD + " BEFORE DELETE ON " + TABLE_MODEL + " BEGIN DELETE FROM " + FTS_MODEL + " WHERE " + "docid=old." + KEY_ID + "; END;");

        db.execSQL( "CREATE TRIGGER IF NOT EXISTS " +  TRG_CM_BU + " BEFORE UPDATE ON " + TABLE_CUSTOM_MATERIAL + " BEGIN DELETE FROM " + FTS_CUSTOM_MATERIAL + " WHERE " + "docid=old." + KEY_ID + "; END;");

        db.execSQL( "CREATE TRIGGER IF NOT EXISTS " +  TRG_CM_BD + " BEFORE DELETE ON " + TABLE_CUSTOM_MATERIAL + " BEGIN DELETE FROM " + FTS_CUSTOM_MATERIAL + " WHERE " + "docid=old." + KEY_ID + "; END;");


        db.execSQL( "CREATE TRIGGER IF NOT EXISTS " +  TRG_CTL_BU + " BEFORE UPDATE ON " + TABLE_CUNOTE_LIST + " BEGIN DELETE FROM " + FTS_CUTNOTE_LIST + " WHERE " + "docid=old." + KEY_ID + "; END;");

        db.execSQL( "CREATE TRIGGER IF NOT EXISTS " +  TRG_CTL_BD + " BEFORE DELETE ON " + TABLE_CUNOTE_LIST + " BEGIN DELETE FROM " + FTS_CUTNOTE_LIST + " WHERE " + "docid=old." + KEY_ID + "; END;");




        db.execSQL( "CREATE TRIGGER " +  TRG_MOD_AU + " AFTER UPDATE ON " + TABLE_MODEL
                +  " BEGIN INSERT INTO " + FTS_MODEL + " (docid," +  MODEL_COLUMN_NAME +","+ DIRECTORY_COLUMN_NAME +","+ CUSTUMER_COLUMN_NAME +","
                + MODEL_COLUMN_BASE_SIZE + ","+MODEL_COLUMN_DESCRIPT
                +") VALUES " + "(new."+ KEY_ID + ",new."+ MODEL_COLUMN_NAME
                +", (SELECT " + DIRECTORY_COLUMN_NAME +" FROM " + TABLE_DIRECTORY  + " WHERE " + KEY_ID + " IN (SELECT " + KEY_DIRECTORY_ID + " FROM " + TABLE_MODEL_DIRECTORY_RELATIONS + " tmd"
                + " WHERE tmd." + KEY_DIRECTORY_ID + " = " + "new." + KEY_ID +"))"
                +",(SELECT " + CUSTUMER_COLUMN_NAME +" FROM " + TABLE_CUSTUMER  + " WHERE " + KEY_ID + " IN (SELECT " + KEY_CUSTUMER_ID + " FROM " + TABLE_MODEL_CUSTUMER_RELATIONS + " tmc"
                + " WHERE tmc." + KEY_CUSTUMER_ID + " = " + "new." + KEY_ID +"))"
                + ",new."+ MODEL_COLUMN_BASE_SIZE+",new."+ MODEL_COLUMN_DESCRIPT+ ");"
                + " UPDATE " + FTS_CUTNOTE_LIST
                + " SET "+  MODEL_COLUMN_NAME+ "=new."+ MODEL_COLUMN_NAME + " WHERE "+ MODEL_COLUMN_NAME + "=old."+MODEL_COLUMN_NAME + ";"
                + " END;");

        db.execSQL( "CREATE TRIGGER " +  TRG_DIR_AU + " AFTER UPDATE ON " + TABLE_DIRECTORY
                +  " BEGIN UPDATE " + FTS_MODEL
                + " SET "+  DIRECTORY_COLUMN_NAME+ "=new."+ DIRECTORY_COLUMN_NAME + " WHERE "+ DIRECTORY_COLUMN_NAME + "=old."+DIRECTORY_COLUMN_NAME + "; END;");

        db.execSQL( "CREATE TRIGGER " +  TRG_CUS_AU + " AFTER UPDATE ON " + TABLE_CUSTUMER
                +  " BEGIN UPDATE " + FTS_MODEL
                + " SET "+  CUSTUMER_COLUMN_NAME+ "=new."+ CUSTUMER_COLUMN_NAME + " WHERE "+ CUSTUMER_COLUMN_NAME + "=old."+CUSTUMER_COLUMN_NAME + "; END;");







    db.execSQL( "CREATE TRIGGER " +  TRG_CM_AU + " AFTER UPDATE ON " + TABLE_CUSTOM_MATERIAL
               +  " BEGIN INSERT INTO " + FTS_CUSTOM_MATERIAL + " (docid," +  CUSTOM_MATERIAL_COLUMN_NAME +","+ MATERIAL_TYPE_COLUMN_NAME +","+ DEALER_COLUMN_NAME +","
                + CUSTOM_MATERIAL_COLUMN_DESCRIPTION + ","+CUSTOM_MATERIAL_COLUMN_SEASONS
                +") VALUES " + "(new."+ KEY_ID + ",new."+ CUSTOM_MATERIAL_COLUMN_NAME
                +", (SELECT " + MATERIAL_TYPE_COLUMN_NAME +" FROM " + TABLE_MATERIAL_TYPES  + " WHERE " + KEY_ID + " IN (SELECT " + KEY_MATERIAL_TYPE_ID + " FROM " + TABLE_MATERIAL_TYPES_CUSTOM_MATERIAL_RELATIONS + " tmtcm"
                + " WHERE tmtcm." + KEY_CUSTOM_MATERIAL_ID + " = " + "new." + KEY_ID +"))"
                +",(SELECT " + DEALER_COLUMN_NAME +" FROM " + TABLE_DEALERSHIP  + " WHERE " + KEY_ID + " IN (SELECT " + KEY_DEALER_ID + " FROM " + TABLE_DEALER_CUSTOM_MATERIAL_RELATIONS + " tdcm"
                + " WHERE tdcm." + KEY_CUSTOM_MATERIAL_ID + " = " + "new." + KEY_ID +"))"
                + ",new."+ CUSTOM_MATERIAL_COLUMN_DESCRIPTION+",new."+ CUSTOM_MATERIAL_COLUMN_SEASONS+"); END;");

        db.execSQL( "CREATE TRIGGER " +  TRG_MT_AU + " AFTER UPDATE ON " + TABLE_MATERIAL_TYPES
                +  " BEGIN UPDATE " + FTS_CUSTOM_MATERIAL
                + " SET "+  MATERIAL_TYPE_COLUMN_NAME+ "=new."+ MATERIAL_TYPE_COLUMN_NAME + " WHERE "+ MATERIAL_TYPE_COLUMN_NAME + "=old."+MATERIAL_TYPE_COLUMN_NAME + "; END;");

        db.execSQL( "CREATE TRIGGER " +  TRG_DLR_AU + " AFTER UPDATE ON " + TABLE_DEALERSHIP
                +  " BEGIN UPDATE " + FTS_CUSTOM_MATERIAL
                + " SET "+  DEALER_COLUMN_NAME+ "=new."+ DEALER_COLUMN_NAME + " WHERE "+ DEALER_COLUMN_NAME + "=old."+DEALER_COLUMN_NAME + "; END;");




        db.execSQL( "CREATE TRIGGER " +  TRG_CTL_AU + " AFTER UPDATE ON " + TABLE_CUNOTE_LIST
                +  " BEGIN INSERT INTO " + FTS_CUTNOTE_LIST + " (docid," +  MODEL_COLUMN_NAME +","+ CUTNOTE_COLUMN_REFERENCE +","+ CUTNOTE_COLUMN_STATUS +","
                + CUTNOTE_COLUMN_PAIR_COUNT + ","+CUSTUMER_COLUMN_NAME
                +") VALUES " + "(new."+ KEY_ID
                +",(SELECT " + MODEL_COLUMN_NAME +" FROM " + TABLE_MODEL  + " WHERE " + KEY_ID + " IN (SELECT " + KEY_MODEL_ID + " FROM " + TABLE_MODEL_CUTNOTE_LIST_RELATIONS + " tmcl"
                + " WHERE tmcl." + KEY_CUTNOTE_LIST_ID + " = " + "new." + KEY_ID +"))"
                +",(SELECT " + CUSTUMER_COLUMN_NAME +" FROM " + TABLE_CUSTUMER  + " WHERE " + KEY_ID + " IN (SELECT " + KEY_CUSTUMER_ID + " FROM " + TABLE_MODEL_CUSTUMER_RELATIONS + " tmc"
                + " WHERE tmc." + KEY_MODEL_ID  + " IN (SELECT " + KEY_MODEL_ID + " FROM " + TABLE_MODEL_CUTNOTE_LIST_RELATIONS + " tmcl"
                + " WHERE tmcl." + KEY_CUTNOTE_LIST_ID + " = " + "new." + KEY_ID + ")))"
                + ",new." + CUTNOTE_COLUMN_STATUS + ",new." + CUTNOTE_COLUMN_REFERENCE + ",new." + CUTNOTE_COLUMN_PAIR_COUNT + "); END;");





        /*db.execSQL( "CREATE TRIGGER " +  TRG_CM_AI + " AFTER INSERT ON " + TABLE_CUSTOM_MATERIAL
                +  " BEGIN INSERT INTO " + FTS_CUSTOM_MATERIAL + " (docid," +  CUSTOM_MATERIAL_COLUMN_NAME +","+ MATERIAL_TYPE_COLUMN_NAME +","+ DEALER_COLUMN_NAME +","
                + CUSTOM_MATERIAL_COLUMN_DESCRIPTION + ","+CUSTOM_MATERIAL_COLUMN_SEASONS
                +") VALUES " + "(new."+ KEY_ID + ",new."+ CUSTOM_MATERIAL_COLUMN_NAME
                +", (SELECT " + MATERIAL_TYPE_COLUMN_NAME +" FROM " + TABLE_MATERIAL_TYPES  + " WHERE " + KEY_ID + " IN (SELECT " + KEY_MATERIAL_TYPE_ID + " FROM " + TABLE_MATERIAL_TYPES_CUSTOM_MATERIAL_RELATIONS + " tmtcm"
                + " WHERE tmtcm." + KEY_CUSTOM_MATERIAL_ID + " = " + "new." + KEY_ID +"))"
                +",(SELECT " + DEALER_COLUMN_NAME +" FROM " + TABLE_DEALERSHIP  + " WHERE " + KEY_ID + " IN (SELECT " + KEY_DEALER_ID + " FROM " + TABLE_DEALER_CUSTOM_MATERIAL_RELATIONS + " tdcm"
                + " WHERE tdcm." + KEY_CUSTOM_MATERIAL_ID + " = " + "new." + KEY_ID +"))"
                + ",new."+ CUSTOM_MATERIAL_COLUMN_DESCRIPTION+",new."+ CUSTOM_MATERIAL_COLUMN_SEASONS+"); END;");*/




        setDefaultDirectory(db);
        setDefaultDealership(db);
        setDefaultMaterialTypes(db);
        setDefaultCustumer(db);
        initializeReferenceNote(db);



    }

    @Override

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        deletaAllTablePieces();
        deletaAllTableMAterial();
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_MODEL);
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_DIRECTORY);
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_CUSTUMER);
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_MATERIAL_TYPES);
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_CUSTOM_MATERIAL);
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_DEALERSHIP);
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_EXTERNALWORKS);
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_ACCESSORIES);
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_CUNOTE_LIST);
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_CUNOTE);
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_REFERENCE);


        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MODEL__PREVIEW_PIECES_RELATIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MODEL_DIRECTORY_RELATIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MODEL_CUSTUMER_RELATIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MODEL_CUTNOTE_LIST_RELATIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MODEL_DEALERS_RELATIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MODEL_ACCESSORIES_RELATIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MODEL_EXTERNAL_RELATIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CUTNOTE_CUTNOTE_LIST_RELATIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MATERIAL_CUSTOM_MATERIAL_RELATIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEALER_CUSTOM_MATERIAL_RELATIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MATERIAL_TYPES_CUSTOM_MATERIAL_RELATIONS);

        db.execSQL(" DROP TABLE IF EXISTS " + FTS_CUSTOM_MATERIAL);
        db.execSQL(" DROP TABLE IF EXISTS " + FTS_MODEL);
        db.execSQL(" DROP TABLE IF EXISTS " + FTS_CUTNOTE_LIST);


        db.execSQL(" DROP TRIGGER IF EXISTS " + TRG_CM_BU);
        db.execSQL(" DROP TRIGGER IF EXISTS " + TRG_CM_BD);
        db.execSQL(" DROP TRIGGER IF EXISTS " + TRG_CM_AI);
        db.execSQL(" DROP TRIGGER IF EXISTS " + TRG_CM_AU);


        db.execSQL(" DROP TRIGGER IF EXISTS " + TRG_MOD_BU);
        db.execSQL(" DROP TRIGGER IF EXISTS " + TRG_MOD_BD);
        db.execSQL(" DROP TRIGGER IF EXISTS " + TRG_MOD_AI);
        db.execSQL(" DROP TRIGGER IF EXISTS " + TRG_MOD_AU);


        db.execSQL(" DROP TRIGGER IF EXISTS " + TRG_CTL_BU);
        db.execSQL(" DROP TRIGGER IF EXISTS " + TRG_CTL_BD);
        db.execSQL(" DROP TRIGGER IF EXISTS " + TRG_CTL_AI);
        db.execSQL(" DROP TRIGGER IF EXISTS " + TRG_CTL_AU);

        db.execSQL(" DROP TRIGGER IF EXISTS " + TRG_DIR_AU);
        db.execSQL(" DROP TRIGGER IF EXISTS " + TRG_CUS_AU);
        db.execSQL(" DROP TRIGGER IF EXISTS " + TRG_MT_AU);
        db.execSQL(" DROP TRIGGER IF EXISTS " + TRG_DLR_AU);



        onCreate(db);

        Utils.toast(mContext, "DATABASE UPDATED-AUTORESTART");

        restart(mContext, 10);
    }

    ///////----------------------------------INITIALIZE VALUES-----------------------------------------------///////////////

    private void setDefaultDirectory(SQLiteDatabase db) {

        String[] categoryList = mContext.getResources().getStringArray(R.array.default_categories);
        for (String category : categoryList) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DIRECTORY_COLUMN_NAME, category);
            db.insert(TABLE_DIRECTORY, null, contentValues);

        }
    }

    private void setDefaultCustumer(SQLiteDatabase db) {


        ContentValues contentValues = new ContentValues();
        contentValues.put(CUSTUMER_COLUMN_NAME, mContext.getString(R.string.importNoAsigned_Cat));
        db.insert(TABLE_CUSTUMER, null, contentValues);


    }

    private void setDefaultDealership(SQLiteDatabase db) {


        String[] dealershipList = mContext.getResources().getStringArray(R.array.default_dealership);
        for (String dealer : dealershipList) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DEALER_COLUMN_NAME, dealer);
            db.insert(TABLE_DEALERSHIP, null, contentValues);

        }
    }

    private void setDefaultMaterialTypes(SQLiteDatabase db) {


        String[] typesList = mContext.getResources().getStringArray(R.array.default_materialsType);
        for (String type : typesList) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(MATERIAL_TYPE_COLUMN_NAME, type);
            db.insert(TABLE_MATERIAL_TYPES, null, contentValues);

        }
    }

    private void initializeReferenceNote(SQLiteDatabase db){


        ContentValues contentValues=new ContentValues();

        contentValues.put(REFERENCE_COLUMN_VALUE,0);


        db.insert(TABLE_REFERENCE,null,contentValues);





    }


    /////////--------------------------------------------CATEGORY-----------------------------------------------/////////


    private Category createDirectory(Cursor cursor){

        Category category=new Category(cursor.getString(cursor.getColumnIndex(DIRECTORY_COLUMN_NAME)));

        category.setId(cursor.getInt(0));
        category.setItemCount(getModelCountAtDirectory(category.getName()));

        return category;

    }

    public long insertCategory(String name) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DIRECTORY_COLUMN_NAME, name);
        long id=db.insert(TABLE_DIRECTORY, null, contentValues);
        closeDB();
        return id;
    }

    public boolean deleteCategory(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String filter = KEY_ID;

        removeModelDirectoryRelation(id);

        boolean state=db.delete(TABLE_DIRECTORY, filter + "=" + String.valueOf(id), null) > 0;

        closeDB();

        return state;


    }

    public boolean updateDirectory(long id, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String filter =KEY_ID;
        ContentValues contentValues = new ContentValues();
        contentValues.put(DIRECTORY_COLUMN_NAME, name);

        return db.update(TABLE_DIRECTORY, contentValues, filter + "=" + String.valueOf(id), null) > 0;
    }

    public List<String> getCategoryString() {
        List<String> buffer = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_DIRECTORY;
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


        Collections.sort(buffer.subList(1,buffer.size()));

        closeDB();

        return buffer;
    }

    public List<Category> getCategoryClass(boolean all) {

        CategoryNameSortedList categories=new CategoryNameSortedList();

        String selectQuery = "SELECT  * FROM " + TABLE_DIRECTORY;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        try {
            if (cursor.moveToFirst()) {
                do {

                    final Category category=createDirectory(cursor);

                    if(all){
                        categories.add(category);
                    }else{

                        if(category.getItemCount()>0){

                            categories.add(category);


                        }

                    }

                } while (cursor.moveToNext());
            }
            cursor.close();

        } catch (Exception e) {
            Utils.toast(mContext, e.toString());
        }

        closeDB();
        return categories.getItemList();
    }

    public Category getDirectory(long id) {

        Category category = null;
        SQLiteDatabase db = this.getWritableDatabase();
        String filter = "_id";

        Cursor c = db.rawQuery(
                "select * from " + TABLE_DIRECTORY + " where " + filter + " = ?", new String[]{String.valueOf(id)});


        try {

            category = new Category();
            while (c.moveToNext()) {


               category=createDirectory(c);

            }
            c.close();

        } catch (Exception e) {
            Utils.toast(mContext, e.toString());
        }
        closeDB();

        return category;
    }

    public String getDirectoryName(long id){

        String name=null;
        SQLiteDatabase db = this.getReadableDatabase();


        String filter = KEY_ID;


        Cursor cursor = db.rawQuery(
                "select * from " + TABLE_DIRECTORY + " where " + filter + "= ?", new String[]{String.valueOf(id)});

        while (cursor.moveToNext()) {

            name=cursor.getString(1);
            break;

        }
        cursor.close();


        closeDB();


        return name;
    }

    public Long getDirectoryId(String directory){



      long id=0;
        SQLiteDatabase db = this.getReadableDatabase();


        String filter = DIRECTORY_COLUMN_NAME;


        Cursor cursor = db.rawQuery(
                "select * from " + TABLE_DIRECTORY + " where " + filter + "= ?", new String[]{directory});

        while (cursor.moveToNext()) {

            id=cursor.getLong(0);
            break;

        }
        cursor.close();





        return id;



    }

    public boolean existDirectory(@NonNull String directory) {
        SQLiteDatabase db = this.getReadableDatabase();
        boolean exist=false;

        String filter = DIRECTORY_COLUMN_NAME;

        Cursor cursor = db.rawQuery(
                "select * from " + TABLE_DIRECTORY+ " where " + filter + "= ?" +  " or " + filter + "= ?" , new String[]{String.valueOf(directory),String.valueOf(directory).toLowerCase()});


        try {

            while (cursor.moveToNext()) {
                exist=true;
                break;

            }
            cursor.close();

        } catch (Exception e) {
            Utils.toast(mContext, e.toString());
        }


        return exist;
    }

    public boolean isDirectoryAssigned(long id) {

        SQLiteDatabase db = this.getWritableDatabase();

        boolean exist=false;

        String filter = KEY_DIRECTORY_ID;

        Cursor cursor = db.rawQuery(
                "select * from " + TABLE_MODEL_DIRECTORY_RELATIONS+ " where " + filter + "= ?", new String[]{String.valueOf(id)});





        try {

            while (cursor.moveToNext()) {
                exist=true;
                break;

            }
            cursor.close();

        } catch (Exception e) {
            Utils.toast(mContext, e.toString());
        }


        closeDB();

        return exist;
    }

    public int getDirectoryCount(){

        String countQuery = "SELECT  * FROM " + TABLE_DIRECTORY;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        final int count = cursor.getCount();

        cursor.close();

        // return count
        return count;


    }

    public List<Category> getAddRecentDirectory(int count) {

        int i = 0;

        List<Category> directoryList = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_DIRECTORY;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        try {
            if (cursor.moveToLast()) {

                do {


                    if (i < count) {

                        directoryList.add(createDirectory(cursor));
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

        closeDB();

        return directoryList;
    }


    /////////--------------------------------------PREVIEW MODEL------------------------------------------------------------------------////////////

    public   PreviewModelInfo createPreviewModel(Cursor cursor){

        final PreviewModelInfo model = new PreviewModelInfo();

              model.setId(cursor.getInt(0));
              model.setName(cursor.getString(1));

              if (cursor.getBlob(2) != null) {
                  final List<String> Sizes = (List<String>) byteArrayToObject(cursor.getBlob(2));
                  model.setSizeList(Sizes);

              }

              model.setBaseSize(cursor.getString(3));
              model.setParent(cursor.getString(4));

              if (cursor.getBlob(5) != null) {
                  byte[] blob = cursor.getBlob(5);
                  Bitmap bitmap = BitmapFactory.decodeByteArray(blob, 0, blob.length);
                  Drawable drawable = new BitmapDrawable(Resources.getSystem(), bitmap);
                  model.setBmp(drawable);
              }
              model.setDate(cursor.getString(6));
              model.setDescription(cursor.getString(7));

              model.setDirectory(getDirectoryNameAtModel(cursor.getInt(0)));
              model.setCustumer(getCustumerNameAtModel(cursor.getInt(0)));


        return model;



    }

    public List<PreviewModelInfo> loadData() {
        List<PreviewModelInfo> modelList = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_MODEL;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        try {
            if (cursor.moveToFirst()) {
                do {


                    modelList.add(createPreviewModel(cursor));

                    // get the data into array, or class variable
                } while (cursor.moveToNext());
            }
            cursor.close();

        } catch (Exception e) {
            Utils.toast(mContext, e.toString());
        }

        return modelList;
    }

    public long  addNewModel(Model model) {


        ContentValues contentValues;

        SQLiteDatabase db = this.getWritableDatabase();

        final String Name,Parent,Custumer, BaseSize, Description, Date;
        byte[] image = null;
        byte[] Sizes = null;


        Name = model.getName();
        Parent = model.getParent();
        Description = model.getDescription();

        if(model.getBaseSize()!=null){

            BaseSize = model.getBaseSize();

        }else{


            BaseSize=model.getMaxSize();
        }



        if (model.getBmp() != null) {
            image = imageViewtoByte(model.getBmp());

        }
        Date = getDate();


        if (!model.getSize().isEmpty()) {

            Sizes = prepareObjectToByte(model.getSize());

        }



        final long id = insertModel(Name,Parent, Date, Description, BaseSize, image, Sizes);

        createPieceTable(String.valueOf(id), db);
        createMaterialTable(String.valueOf(id), db);

        for (MaterialGroup material : model.getMaterials()) {


            contentValues = new ContentValues();
            contentValues.put(MATERIAL_COLUMN_MODEL_ID, String.valueOf(id));
            contentValues.put(MATERIAL_COLUMN_NAME, material.getName());
            long materialId=db.insert(TABLE_MATERIAL + String.valueOf(id), null, contentValues);


            for (List<Piece> pieceList : material.getPieceList()) {

                for (int i = 0; i < pieceList.size(); i++) {

                    if(i==0){

                       // SPFdecoder.extraData(pieceList.get(i),mContext);

                    }

                          pieceList.get(i).setModelId(Utils.toIntExact(id));
                          long pieceId=insertPiece(pieceList.get(i));


                    if (i == 0) {

                        insertPreviewPieces(id,pieceId,materialId);

                    }



                }


            }


        }


        insertModelDirectoryRelation(id,getDirectoryId(model.getDirectory()));
        if(model.getCustumer()!=null){

            insertModelCustumerRelation(id,getCustumerId(model.getCustumer()));


        }else {
            insertModelCustumerRelation(id,1);

        }


        ContentValues contentValues1=new ContentValues();

        contentValues1.put("docid",id);
        contentValues1.put(MODEL_COLUMN_NAME,Name);
        contentValues1.put(DIRECTORY_COLUMN_NAME,model.getDirectory());
        contentValues1.put(CUSTUMER_COLUMN_NAME,model.getCustumer());
        contentValues1.put(MODEL_COLUMN_BASE_SIZE,BaseSize);
        contentValues1.put(MODEL_COLUMN_DESCRIPT,Description);

        db.insert(FTS_MODEL,null,contentValues1);





        closeDB();
        return id;


    }

    private long insertModel(String name,String parent, String date, String description, String baseSize, byte[] image, byte[] sizes) {


        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MODEL_COLUMN_NAME, name);
         contentValues.put(MODEL_COLUMN_PARENT, parent);
        contentValues.put(MODEL_COLUMN_IMAGE, image);
        contentValues.put(MODEL_COLUMN_BASE_SIZE, baseSize);
        contentValues.put(MODEL_COLUMN_SIZES, sizes);
        contentValues.put(MODEL_COLUMN_DESCRIPT, description);
        contentValues.put(MODEL_COLUMN_DATE, date);

        return db.insert(TABLE_MODEL, null, contentValues);
    }

    public void updatePreviewData(PreviewModelInfo model) {

        SQLiteDatabase db = this.getWritableDatabase();
        String filter = "_id=";
        filter += model.getId();
        ContentValues contentValues = new ContentValues();


        if(model.getName()!=null)
        contentValues.put(MODEL_COLUMN_NAME, model.getName());


        if (model.getSizeList() != null)
            contentValues.put(MODEL_COLUMN_SIZES, prepareObjectToByte(model.getSizeList()));


        if (model.getBmp() != null)
            contentValues.put(MODEL_COLUMN_IMAGE, imageViewtoByte(model.getBmp()));

        if(model.getBaseSize()!=null)
        contentValues.put(MODEL_COLUMN_BASE_SIZE, model.getBaseSize());


        if(model.getDescription()!=null)
        contentValues.put(MODEL_COLUMN_DESCRIPT, model.getDescription());

        if(model.getDirectory()!=null)
         updateModelDirectoryRelation(model.getId(),getDirectoryId(model.getDirectory()));

        if(model.getCustumer()!=null)
         updateModelCustumerRelation(model.getId(),getCustumerId(model.getCustumer()));

        if(contentValues.size()>0)
        db.update(TABLE_MODEL, contentValues, filter, null);

        closeDB();

    }

    public boolean deleteModelData(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String filter = "_id=";

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PIECE + String.valueOf(id));//Borrado de tabla de piezas ,asociadas al modelo

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MATERIAL + String.valueOf(id));//Borrado de tabla de materiales,asociadas al modelo

        removeModelDirectoryRelationByModel(id);
        removeModelCustumerRelationByModel(id);

        removeModelPreviewPiecesRelationByModel(id);
        removeMaterialCustomMaterialRelationByModel(id);
        removeModelCutNoteListRelationByModel(id);


        return db.delete(TABLE_MODEL, filter + "=" + id, null) > 0;
    }

    public boolean deleteAllData() {

        SQLiteDatabase db = this.getReadableDatabase();

        this.onUpgrade(db, db.getVersion(), db.getVersion() + 1);


        return true;
    }

    public PreviewModelInfo getPreviewModelById(long id) {


        PreviewModelInfo model=null;

        SQLiteDatabase db = this.getReadableDatabase();


        String filter = KEY_ID;


        Cursor cursor = db.rawQuery(
                "select * from " + TABLE_MODEL + " where " + filter + "= ?", new String[]{String.valueOf(id)});

        while (cursor.moveToNext()) {

            model=createPreviewModel(cursor);
            break;

        }
        cursor.close();


       closeDB();


        return model;
    }

    public int getLastIndexModel() {

        String selectQuery = "SELECT  * FROM " + TABLE_MODEL;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToLast();
        final int lastIndex = cursor.getInt(0);
        cursor.close();
        return lastIndex;
    }

    public int getModelsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_MODEL;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        final int count = cursor.getCount();

        cursor.close();

        // return count
        return count;
    }

    public List<PreviewModelInfo> getAddRecentModel(int count) {

        int i = 0;

        List<PreviewModelInfo> modelList = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_MODEL;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        try {
            if (cursor.moveToLast()) {

                do {


                    if (i < count) {

                        modelList.add(createPreviewModel(cursor));
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

        closeDB();

        return modelList;
    }

    public List<PreviewModelInfo> getPreviewModelByCategory(String category){


        List<PreviewModelInfo> models=new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_MODEL + " m, "
                + TABLE_DIRECTORY + " d, " + TABLE_MODEL_DIRECTORY_RELATIONS + " tdm WHERE d."
                + DIRECTORY_COLUMN_NAME + " = '" + category + "'" + " AND d." + KEY_ID
                + " = " + "tdm." + KEY_DIRECTORY_ID + " AND m." + KEY_ID + " = "
                + "tdm." + KEY_MODEL_ID;

        Cursor c  = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {

                models.add(createPreviewModel(c));


            } while (c.moveToNext());
        }
        c.close();

        closeDB();


        return models;



    }
    public List<PreviewModelInfo> getPreviewModelByCustumer(String custumer){


        List<PreviewModelInfo> models=new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_MODEL + " m, "
                + TABLE_CUSTUMER + " c, " + TABLE_MODEL_CUSTUMER_RELATIONS + " tmc WHERE c."
                + CUSTUMER_COLUMN_NAME + " = '" + custumer + "'" + " AND c." + KEY_ID
                + " = " + "tmc." + KEY_CUSTUMER_ID + " AND m." + KEY_ID + " = "
                + "tmc." + KEY_MODEL_ID;

        Cursor c  = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {

                models.add(createPreviewModel(c));


            } while (c.moveToNext());
        }
        c.close();

        closeDB();


        return models;



    }
    public List<PreviewModelInfo> getPreviewModelByName(String name){

        List<PreviewModelInfo> models=new ArrayList<>();


        SQLiteDatabase db = this.getWritableDatabase();


        String filter = MODEL_COLUMN_NAME;



        Cursor c = db.rawQuery(
                "select * from " + TABLE_MODEL + " where " + filter + "= ?", new String[]{name,name.toLowerCase()});

        while (c.moveToNext()) {

            models.add(createPreviewModel(c));

        }


        return models;

    }

    public List<PreviewModelInfo> getPreviewModelByDate(String date){

        List<PreviewModelInfo> models=new ArrayList<>();


        SQLiteDatabase db = this.getWritableDatabase();


        String filter = MODEL_COLUMN_DATE;



        Cursor c = db.rawQuery(
                "select * from " + TABLE_MODEL + " where " + filter + "= ?", new String[]{date,date.toLowerCase()});

        while (c.moveToNext()) {

            models.add(createPreviewModel(c));

        }


        return models;

    }

    public List<PreviewModelInfo> getPreviewModelCoincidenceValue(String value){

        List<PreviewModelInfo> models=new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_MODEL;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        String[] array = new String[2];
        array = (value).split(" ");
        SparseBooleanArray state=new SparseBooleanArray();
        SparseBooleanArray state1=new SparseBooleanArray();
        try {
            if (cursor.moveToFirst()) {
                do {

                    for(int i =0;i<array.length;i++){



                        if(cursor.getString(cursor.getColumnIndex(MODEL_COLUMN_NAME)).toLowerCase().contains(array[i].toLowerCase())){

                           state.append(0,true);

                        }else{


                            state.append(0,false);
                        }
                        if(getCustumerNameAtModel(cursor.getInt(cursor.getColumnIndex(KEY_ID))).toLowerCase().contains(array[i].toLowerCase())){

                            state.append(1,true);

                        }else{


                            state.append(1,false);

                        }
                        if(cursor.getString(cursor.getColumnIndex(MODEL_COLUMN_DESCRIPT)).toLowerCase().contains(array[i].toLowerCase())){

                            state.append(2,true);

                        }else{


                            state.append(2,false);
                        }
                        if(getDirectoryNameAtModel(cursor.getInt(cursor.getColumnIndex(KEY_ID))).toLowerCase().contains(array[i].toLowerCase())){

                            state.append(3,true);

                        }else{


                            state.append(3,false);
                        }

                        if(state.indexOfValue(true)>=0){

                            state1.append(i,true);


                        }else{


                            state1.append(i,false);
                        }

                        state.clear();



                    }

                    if(state1.indexOfValue(false)<0){


                        models.add(createPreviewModel(cursor));
                    }

                    state1.clear();

                    // get the data into array, or class variable
                } while (cursor.moveToNext());
            }
            cursor.close();

        } catch (Exception e) {
            Utils.toast(mContext, e.toString());
        }


        return models;

    }

    public List<String> getModelsNamesWithAssignedCutNoteList(){

        List<String>dealerList=new ArrayList<>();
        HashSet hs=new HashSet();


        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_MODEL + " tm, "
                + TABLE_MODEL_CUTNOTE_LIST_RELATIONS + " tmcl WHERE tmcl."
                + KEY_MODEL_ID + " = " + "tm." + KEY_ID;

        Cursor c  = db.rawQuery(selectQuery, null);



        try {
            if (c.moveToFirst()) {
                do {



                    hs.add(c.getString(c.getColumnIndex(MODEL_COLUMN_NAME)));


                } while (c.moveToNext());
            }
            c.close();

        } catch (Exception e) {
            Utils.toast(mContext, "No se encuentra el material" + e.toString());
        }


        dealerList.addAll(hs);

        Collections.sort(dealerList);

        return dealerList;
    }


    public PreviewModelInfo getPreviewModelByCutNoteList(long cutNoteListId){



        SQLiteDatabase db = this.getReadableDatabase();


        String selectQuery = "SELECT  * FROM " + TABLE_MODEL + " m, "
                + TABLE_MODEL_CUTNOTE_LIST_RELATIONS + " tmcl WHERE tmcl."
                + KEY_CUTNOTE_LIST_ID + " = '" + String.valueOf(cutNoteListId) + "'" + " AND tmcl." + KEY_MODEL_ID
                + " = " + "m." + KEY_ID;

        Cursor c  = db.rawQuery(selectQuery, null);

        PreviewModelInfo model=null;


        try {
            if (c.moveToFirst()) {
                do {



                   model=createPreviewModel(c);


                } while (c.moveToNext());
            }
            c.close();

        } catch (Exception e) {
            Utils.toast(mContext, "No se encuentra el material" + e.toString());
        }


        return model;
    }

    public Drawable getModelImage(long id){

        Drawable img=null;
        SQLiteDatabase db = this.getReadableDatabase();

        String from[]=new String[]{MODEL_COLUMN_IMAGE};
        String where = KEY_ID + "= ? ";
        String[] whereArgs = new String[]{String.valueOf(id)};
        Cursor c  = db.query(TABLE_MODEL, from, where, whereArgs, null, null, null, null);



        try {

            while (c.moveToNext()){


                if (c.getBlob(c.getColumnIndex(MODEL_COLUMN_IMAGE)) != null) {
                    byte[] blob = c.getBlob(c.getColumnIndex(MODEL_COLUMN_IMAGE));
                    final  Bitmap bitmap = BitmapFactory.decodeByteArray(blob, 0, blob.length);
                    final Drawable drawable = new BitmapDrawable(Resources.getSystem(), bitmap);
                    img=(drawable);
                    break;
                }



            }

            c.close();

        } catch (Exception e) {
            Utils.toast(mContext, "No se encuentra la imagen" + e.toString());
        }

        closeDB();

        return  img;
    }



    ////////----------------------------------------------PIECES--------------------------------------------------------------////////////

   private long insertPiece(Piece piece){

       SQLiteDatabase db = this.getWritableDatabase();


       ContentValues contentValues = new ContentValues();
       contentValues.put(PIECE_COLUMN_MODEL_ID, String.valueOf(piece.getModelId()));
       contentValues.put(PIECE_COLUMN_NAME,piece.getName());
       contentValues.put(PIECE_COLUMN_AMOUNT, piece.getAmount());
       contentValues.put(PIECE_COLUMN_AMOUNT_MIRROR, piece.getAmount_mirror());
       contentValues.put(PIECE_COLUMN_SIZE,piece.getSize());
       contentValues.put(PIECE_COLUMN_MATERIAL,piece.getMaterial());
       contentValues.put(PIECE_COLUMN_AMOUNT_MATERIAL, piece.getAmount_material());

       if (!piece.getToolList().isEmpty()) {

           contentValues.put(PIECE_COLUMN_TOOLS, prepareObjectToByte(piece.getToolList()));

       }

       if (piece.getImage() != null) {
           contentValues.put(PIECE_COLUMN_IMAGE, imageViewtoByte(piece.getImage()));
       }

       contentValues.put(PIECE_COLUMN_DESCRIPT,piece.getDescription());

       contentValues.put(PIECE_COLUMN_RAWDATA, prepareObjectToByte(piece.getRawData()));

      return db.insert(TABLE_PIECE + String.valueOf(piece.getModelId()), null, contentValues);


   }

    public void createPieceTable(String modelId, SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + TABLE_PIECE + modelId + "(" +
                KEY_ID + " INTEGER PRIMARY KEY, " +
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

    public Piece createPiece (Cursor cursor,boolean withImage) {

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

            if (cursor.getBlob(11) != null) {
                piece.setRawData( (List<String>) byteArrayToObject(cursor.getBlob(11)));

            }

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


        Cursor c = db.rawQuery(
                "select * from " + TABLE_PIECE + pieceModelId + " where " + pieceName + " = ? AND " + pieceMaterial + " = ? ", new String[]{piece.getName(), piece.getMaterial()});

        while (c.moveToNext()) {


            String filter = "_id=";

            delete = db.delete(TABLE_PIECE + pieceModelId, filter + "=" + c.getInt(0), null) > 0;

            removeModelPreviewPiecesRelationByPiece(piece.getModelId(),piece.getId());

        }


             closeDB();


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

    public List<Piece> getHighLightPieces(@NonNull long modelId,boolean withImage){

        List<Piece>pieceList=new ArrayList<>();


        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_PIECE  + String.valueOf(modelId)+ " tp, "
                + TABLE_MODEL + " tm, " + TABLE_MODEL__PREVIEW_PIECES_RELATIONS + " tmp WHERE tm."
                + KEY_ID + " = '" + String.valueOf(modelId) + "'" + " AND tp." + KEY_ID
                + " = " + "tmp." + KEY_PIECE_ID + " AND tm." + KEY_ID + " = "
                + "tmp." + KEY_MODEL_ID;

        Cursor c  = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {

                pieceList.add(createPiece(c,withImage));


            } while (c.moveToNext());
        }
        c.close();

       // closeDB();



        return pieceList;
    }

    public boolean isHightLightPiece(Piece piece){

        SQLiteDatabase db = this.getReadableDatabase();
        boolean exist=false;

        String filter1 = KEY_MODEL_ID;
        String filter2 = KEY_PIECE_ID;


        Cursor cursor = db.rawQuery(
                "select * from " + TABLE_MODEL__PREVIEW_PIECES_RELATIONS+ " where " + filter1 + "= ?" +  " and " + filter2 + "= ?" , new String[]{String.valueOf(piece.getModelId()),String.valueOf(piece.getId())});


        try {

            while (cursor.moveToNext()) {
                exist=true;
                break;

            }
            cursor.close();

        } catch (Exception e) {
            Utils.toast(mContext, e.toString());
        }


        return exist;





    }

    public List<Piece> getHighLightPiecesByMaterial(@NonNull long modelId,long materialId,boolean withImage){

        List<Piece>pieceList=new ArrayList<>();


        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_PIECE  + String.valueOf(modelId)+ " tp, "
                + TABLE_MATERIAL+String.valueOf(modelId) + " tm, " + TABLE_MODEL__PREVIEW_PIECES_RELATIONS + " tmp WHERE tm."
                + KEY_ID + " = '" + String.valueOf(materialId) + "'" + " AND tp." + KEY_ID
                + " = " + "tmp." + KEY_PIECE_ID + " AND tm." + KEY_ID + " = "
                + "tmp." + KEY_MATERIAL_ID + " AND " + String.valueOf(modelId) +" = " + "tmp." + KEY_MODEL_ID;

        Cursor c  = db.rawQuery(selectQuery, null);

        while (c.moveToNext()){

                pieceList.add(createPiece(c,withImage));



        }
        c.close();

        closeDB();



        return pieceList;
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

        String selectQuery = "SELECT  * FROM " + TABLE_MODEL;
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

        if(isHightLightPiece(piece))
            updateModelPreviewPiece(piece.getModelId(),piece.getId(),getMaterialByName(piece.getModelId(),piece.getMaterial()).getId());

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

                            if(isHightLightPiece(oldPiece))
                                updateModelPreviewPiece(oldPiece.getModelId(),oldPiece.getId(),getMaterialByName(oldPiece.getModelId(),newPiece.getMaterial()).getId());


                            db.update(TABLE_PIECE + oldPiece.getModelId(), contentValues, filter, null);


                        }

                    }


                } while (cursor.moveToNext());

            }
            cursor.close();

        } catch (Exception e) {
            Utils.toast(mContext, e.toString());
        }


    }

    public List<String> getRawDataOfPiece(long modelId,long pieceId){

        SQLiteDatabase db=getReadableDatabase();
        List<String>rawData=null;


        Cursor  c=db.query(TABLE_PIECE + String.valueOf(modelId),new String[]{PIECE_COLUMN_RAWDATA},KEY_ID + " =?  AND " + PIECE_COLUMN_MODEL_ID + " =? ",new String[]{String.valueOf(modelId),String.valueOf(pieceId)}
        ,null,null,null);


        while (c.moveToNext()) {

            if (c.getBlob(c.getColumnIndex(PIECE_COLUMN_RAWDATA)) != null) {
                rawData =(List<String>) byteArrayToObject(c.getBlob(c.getColumnIndex(PIECE_COLUMN_RAWDATA)));



            }
        }
      return   rawData;





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

    ///////--------------------------------------------MATERIALS----------------------------------------------///////////////

    private Material createMaterial(Cursor cursor){

        Material material=null;


        material = new Material(cursor.getString(2));
        material.setId(cursor.getInt(0));
        material.setModelId(Integer.valueOf(cursor.getString(1)));
        material.setCustomMaterialId(getCustomMaterialAtMaterial(material.getModelId(),material.getId()));
        material.setPieceCount(getPieceCountByMaterial(Long.valueOf(cursor.getString(1)),cursor.getInt(0)));

        return material;
    }

    public boolean existMaterial(long modelId,String materialName){




        SQLiteDatabase db = this.getReadableDatabase();
        boolean exist=false;

        String filter = MATERIAL_COLUMN_NAME;

        Cursor cursor = db.rawQuery(
                "select * from " + TABLE_MATERIAL+String.valueOf(modelId) + " where " + filter + "= ?" +  " or " + filter + "= ?" , new String[]{String.valueOf(materialName),String.valueOf(materialName).toLowerCase()});


        try {

            while (cursor.moveToNext()) {
                exist=true;
                break;

            }
            cursor.close();

        } catch (Exception e) {
            Utils.toast(mContext, e.toString());
        }


        return exist;

    }

    private void createMaterialTable(String modelId, SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + TABLE_MATERIAL + modelId + "(" +
                KEY_ID + " INTEGER PRIMARY KEY, " +
                MATERIAL_COLUMN_MODEL_ID + " TEXT, " +
                MATERIAL_COLUMN_NAME + " TEXT, " +
                MATERIAL_COLUMN_COLOR + " INTEGER)");


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


                    bufferMaterial.add(createMaterial(cursor));

                } while (cursor.moveToNext());
            }
            cursor.close();

        } catch (Exception e) {
            Utils.toast(mContext, e.toString());
        }


        return bufferMaterial;

    }

    public long addMaterial(@NonNull long modelId, @NonNull String name,long customMaterialId) {

        long id=0;

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MATERIAL_COLUMN_MODEL_ID, String.valueOf(modelId));
        contentValues.put(MATERIAL_COLUMN_NAME, name);

          id= db.insert(TABLE_MATERIAL + String.valueOf(modelId), null, contentValues);

        if( customMaterialId>0 ){

         insertMaterialCustomMaterialRelation(modelId,id,customMaterialId);

        }



      return id;


    }

    public boolean deleteMaterial(@NonNull long modelId, @NonNull long materialId) {


        SQLiteDatabase db = this.getWritableDatabase();
        String filter = "_id=";


         removeMaterialCustomMaterialRelationByMaterial(modelId,materialId);

        return db.delete(TABLE_MATERIAL + String.valueOf(modelId), filter + "=" + materialId, null) > 0;
    }

    public Material getMaterialByName(@NonNull long modelId, @NonNull String name) {

        Material material = null;

        SQLiteDatabase db= getReadableDatabase();

         String where = KEY_MODEL_ID + "= ? AND "  + MATERIAL_COLUMN_NAME + "= ?";
        String[] whereArgs = new String[]{String.valueOf(modelId),name};

        Cursor c  = db.query(TABLE_MATERIAL+String.valueOf(modelId),null, where, whereArgs, null, null, null, null);
        try {


            while (c.moveToNext()) {


                 material= createMaterial(c);



            }
            c.close();

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


                        material=createMaterial(cursor);
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

    public int getPieceCountByMaterial(long modelId,long materialId){

        SQLiteDatabase db = this.getReadableDatabase();


        String where = KEY_MODEL_ID + "= ? AND "  + KEY_MATERIAL_ID + "= ?";
        String[] whereArgs = new String[]{String.valueOf(modelId),String.valueOf(materialId)};
        Cursor c  = db.query(TABLE_MODEL__PREVIEW_PIECES_RELATIONS, null, where, whereArgs, null, null, null, null);

       final int count = c.getCount();

       closeDB();

        return count;


    }

    public boolean updateMaterial(@NonNull long modelId, @NonNull long materialId, @Nullable String name,long customMaterialId) {

        SQLiteDatabase db = this.getWritableDatabase();
        String filter = "_id=";
        ContentValues contentValues = new ContentValues();


        if(customMaterialId>0){



           if(! updateMaterialCustomMaterialRelation(modelId,materialId,customMaterialId))
               insertMaterialCustomMaterialRelation(modelId,materialId,customMaterialId);

        }else{


            removeMaterialCustomMaterialRelationByMaterial(modelId,materialId);

        }

        if (name != null) {

            contentValues.put(MATERIAL_COLUMN_NAME, name);
            return db.update(TABLE_MATERIAL + String.valueOf(modelId), contentValues, filter + "=" + String.valueOf(materialId), null) > 0;
        }
        return true;

    }

    public void updateMaterialAtPiece(Piece piece,String newMaterial) {

        String selectQuery = "SELECT  * FROM " + TABLE_PIECE + String.valueOf(piece.getModelId());
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);



        try {
            if (cursor.moveToFirst()) {
                do {

                    if (cursor.getString(2).equals(piece.getName())) {

                        if (cursor.getString(6).equals(piece.getMaterial())) {

                            String filter = "_id=";
                            filter += cursor.getInt(cursor.getColumnIndex(KEY_ID));
                            ContentValues contentValues = new ContentValues();
                             contentValues.put(PIECE_COLUMN_MATERIAL,newMaterial);


                            db.update(TABLE_PIECE + piece.getModelId(), contentValues, filter, null);


                        }

                    }


                } while (cursor.moveToNext());

            }
            cursor.close();

            if(isHightLightPiece(piece))
            updateModelPreviewPiece(piece.getModelId(),piece.getId(),getMaterialByName(piece.getModelId(),newMaterial).getId());
        } catch (Exception e) {
            Utils.toast(mContext, e.toString());
        }


    }

    public boolean deletaAllTableMAterial() {

        String selectQuery = "SELECT  * FROM " + TABLE_MODEL;
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

    /////////------------------------------------CUSTOM MATERIALS-----------------------------------------------------------------------------------------------------------------------------------//////////////

    public CustomMaterial createCustomMaterial(Cursor cursor,boolean withImage){



       final CustomMaterial material = new CustomMaterial(cursor.getString(1));
        material.setId(cursor.getInt(0));
        material.setName(cursor.getString(1));
        material.setFeets(cursor.getFloat(2));


        material.setType(getMaterialTypeAtCustomMaterial(material.getId()));
        material.setDealership( getDealerNameAtCustomMaterial(material.getId()));
        material.setSeasons(cursor.getString(3));

        if(withImage) {
            if (cursor.getBlob(4) != null) {
                byte[] blob = cursor.getBlob(4);
                final Bitmap bitmap = BitmapFactory.decodeByteArray(blob, 0, blob.length);
                final Drawable drawable = new BitmapDrawable(Resources.getSystem(), bitmap);
                material.setImage(drawable);
            }
        }

        material.setDate(cursor.getString(5));
        material.setDescription(cursor.getString(6));








        return material;

    }

    public List<CustomMaterial> getAllCustomMaterial() {

        List<CustomMaterial> bufferMaterial = null;


        String selectQuery = "SELECT  * FROM " + TABLE_CUSTOM_MATERIAL;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        try {

            bufferMaterial = new ArrayList<>();
            if (cursor.moveToFirst()) {
                do {


                    bufferMaterial.add(createCustomMaterial(cursor,true));

                } while (cursor.moveToNext());
            }
            cursor.close();

        } catch (Exception e) {
            Utils.toast(mContext, e.toString());
        }


        return bufferMaterial;
    }

    public List<CustomMaterial> getRecentCustomMaterial(int count) {

        int i = 0;

        List<CustomMaterial> materialList = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_CUSTOM_MATERIAL;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        try {
            if (cursor.moveToLast()) {

                do {


                    if (i < count) {

                       materialList.add(createCustomMaterial(cursor,true));
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

        closeDB();

        return materialList;
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

    private long insertCustomMaterial(@NonNull String name, String type, float feets, String seasons, byte[] image, String date,String description) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(CUSTOM_MATERIAL_COLUMN_NAME, name);
        contentValues.put(CUSTOM_MATERIAL_COLUMN_FEETS, feets);
        contentValues.put(CUSTOM_MATERIAL_COLUMN_SEASONS, seasons);
        contentValues.put(CUSTOM_MATERIAL_COLUMN_IMAGE, image);
        contentValues.put(CUSTOM_MATERIAL_COLUMN_DATE, date);
        contentValues.put(CUSTOM_MATERIAL_COLUMN_DESCRIPTION,description);
        return db.insert(TABLE_CUSTOM_MATERIAL, null, contentValues);
    }

    public long  addCustomMaterial(CustomMaterial customMaterial) {



        String name, type, dealership, seasons, date,description;
        Float feets;
        long id=0;

        byte[] image = null;

        name = customMaterial.getName();
        type = customMaterial.getType();
        description=customMaterial.getDescription();

        if (type == null) {

            type = mContext.getResources().getString(R.string.importNoAsigned_Cat);
        }

         addMaterialType(type);

        feets = customMaterial.getFeets();
        dealership = customMaterial.getDealership();


        seasons = customMaterial.getSeasons();

        if (seasons == null) {

            seasons = mContext.getResources().getString(R.string.importNoAsigned_Cat);

        }
        date = getDate();

        if (customMaterial.getImage() != null) {

            image = imageViewtoByte(customMaterial.getImage());

        } else {

            Drawable drawable = mContext.getDrawable(R.drawable.leather);
            image = imageViewtoByte(drawable);


        }

        if(description==null){


            description = mContext.getResources().getString(R.string.importNoAsigned_Cat);



        }




       id= insertCustomMaterial(name, type, feets, seasons, image, date,description);

        if (dealership != null) {

            insertDealerCustomMaterialRelation(getDealerShipByName(dealership).getId(),id);
        }

            insertMaterialTypeCustomMaterialRelation(getMaterialTypeId(type),id);


        SQLiteDatabase db = this.getWritableDatabase();



        final ContentValues contentValues=new ContentValues();



        contentValues.put("docid",id);

        contentValues.put(CUSTOM_MATERIAL_COLUMN_NAME,customMaterial.getName());
        contentValues.put(MATERIAL_TYPE_COLUMN_NAME, customMaterial.getType());
        contentValues.put(DEALER_COLUMN_NAME, customMaterial.getDealership());
        contentValues.put(CUSTOM_MATERIAL_COLUMN_SEASONS, customMaterial.getSeasons());
        contentValues.put(CUSTOM_MATERIAL_COLUMN_DESCRIPTION, customMaterial.getDescription());

        db.insert(FTS_CUSTOM_MATERIAL,null,contentValues);


        return   id;





    }

    public Drawable getCustomMaterialImage(long id){

        Drawable img=null;
        SQLiteDatabase db = this.getReadableDatabase();

        String from[]=new String[]{CUSTOM_MATERIAL_COLUMN_IMAGE};
        String where = KEY_ID + "= ? ";
        String[] whereArgs = new String[]{String.valueOf(id)};
        Cursor c  = db.query(TABLE_CUSTOM_MATERIAL, from, where, whereArgs, null, null, null, null);



        try {

            while (c.moveToNext()){


                if (c.getBlob(c.getColumnIndex(CUSTOM_MATERIAL_COLUMN_IMAGE)) != null) {
                    byte[] blob = c.getBlob(c.getColumnIndex(CUSTOM_MATERIAL_COLUMN_IMAGE));
                    final  Bitmap bitmap = BitmapFactory.decodeByteArray(blob, 0, blob.length);
                    final Drawable drawable = new BitmapDrawable(Resources.getSystem(), bitmap);
                    img=(drawable);
                    break;
                }



            }

            c.close();

        } catch (Exception e) {
            Utils.toast(mContext, "No se encuentra la imagen" + e.toString());
        }

        closeDB();

        return  img;
    }

    public boolean deleteCustomMaterial(long id) {


        boolean delete=false;
        SQLiteDatabase db = this.getWritableDatabase();
        String filter = "_id=";

        delete= db.delete(TABLE_CUSTOM_MATERIAL, filter + "=" + String.valueOf(id), null) > 0;

        if (delete){

            removeMaterialCustomMaterialRelationByCustomMaterial(id);
            removeDealerCustomMaterialRelationByCustomMaterial(id);
            removeMaterialTypeCustomMaterialRelationByCustomMaterial(id);


        }


      return delete;


    }

    public boolean updateCustomMaterial(CustomMaterial material) {
        SQLiteDatabase db = this.getWritableDatabase();
        String filter = "_id=";
        ContentValues contentValues = new ContentValues();

        contentValues.put(CUSTOM_MATERIAL_COLUMN_NAME, material.getName());
        contentValues.put(CUSTOM_MATERIAL_COLUMN_FEETS, material.getFeets());
        contentValues.put(CUSTOM_MATERIAL_COLUMN_SEASONS, material.getSeasons());
        contentValues.put(CUSTOM_MATERIAL_COLUMN_DATE, material.getDate());
        contentValues.put(CUSTOM_MATERIAL_COLUMN_DESCRIPTION,material.getDescription());

        if (material.getImage() != null) {
            contentValues.put(CUSTOM_MATERIAL_COLUMN_IMAGE, imageViewtoByte(material.getImage()));
        }else{
            Drawable drawable = mContext.getDrawable(R.drawable.leather);
            contentValues.put(CUSTOM_MATERIAL_COLUMN_IMAGE, imageViewtoByte(drawable));

        }

            updateDealerCustomMaterialRelation(getDealerShipByName(material.getDealership()).getId(),material.getId());

        if(!existMaterialType(material.getType())){

            updateMaterialTypeCustomMaterialRelation(insertMaterialTypes(material.getType()),material.getId());

        }else{
            updateMaterialTypeCustomMaterialRelation(getMaterialTypeId(material.getType()),material.getId());

        }





        return db.update(TABLE_CUSTOM_MATERIAL, contentValues, filter + "=" + String.valueOf(material.getId()), null) > 0;
    }

    public CustomMaterial getCustomMaterial(long custom_materialId) {


         CustomMaterial material=null;
         SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CUSTOM_MATERIAL,null,KEY_ID + " =? ",new String[]{String.valueOf(custom_materialId)},null,null, null);

        try {

            while(cursor.moveToNext()){

                material=createCustomMaterial(cursor,true);
                break;

            }


            cursor.close();

        } catch (Exception e) {
            Utils.toast(mContext, "No se encuentra el material" + e.toString());
        }


        return material;
    }

    public List<CustomMaterial> getCustomMaterialByDealerName(String dealerName){


        List<CustomMaterial> materials=new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery=null;


             selectQuery = "SELECT  * FROM " + TABLE_CUSTOM_MATERIAL + " tcm, "
                    + TABLE_DEALERSHIP + " td, " + TABLE_DEALER_CUSTOM_MATERIAL_RELATIONS + " tdcm WHERE td."
                    + DEALER_COLUMN_NAME + " = '" + dealerName + "'" + " AND td." + KEY_ID
                    + " = " + "tdcm." + KEY_DEALER_ID + " AND tcm." + KEY_ID + " = "
                    + "tdcm." + KEY_CUSTOM_MATERIAL_ID;


            Cursor c = db.rawQuery(selectQuery, null);

            if (c.moveToFirst()) {
                do {




                        materials.add(createCustomMaterial(c,true));


                } while (c.moveToNext());
            }
            c.close();


        closeDB();


        return materials;



    }

    public List<CustomMaterial>getCustomMaterialByType(String type){


        List<CustomMaterial> materials=new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery=null;


        selectQuery = "SELECT  * FROM " + TABLE_CUSTOM_MATERIAL + " tcm, "
                + TABLE_MATERIAL_TYPES + " tmt, " + TABLE_MATERIAL_TYPES_CUSTOM_MATERIAL_RELATIONS + " tmtcm WHERE tmt."
                + MATERIAL_TYPE_COLUMN_NAME + " = '" + type + "'" + " AND tmt." + KEY_ID
                + " = " + "tmtcm." + KEY_MATERIAL_TYPE_ID + " AND tcm." + KEY_ID + " = "
                + "tmtcm." + KEY_CUSTOM_MATERIAL_ID;


        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {




                materials.add(createCustomMaterial(c,true));


            } while (c.moveToNext());
        }
        c.close();


        closeDB();


        return materials;



    }

    public List<CustomMaterial> getCustomMaterialByValue(String value){

        List<CustomMaterial> materials=new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_CUSTOM_MATERIAL;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        String[] array = new String[2];
        array = (value).split(" ");
        SparseBooleanArray state=new SparseBooleanArray();
        SparseBooleanArray state1=new SparseBooleanArray();

        try {
            if (cursor.moveToFirst()) {
                do {
                    for(int i =0;i<array.length;i++) {


                        if (cursor.getString(cursor.getColumnIndex(CUSTOM_MATERIAL_COLUMN_NAME)).toLowerCase().contains(array[i].toLowerCase())) {

                            state.append(0,true);

                        }else{

                            state.append(0,false);

                        }
                        if (getMaterialTypeAtCustomMaterial(cursor.getInt(cursor.getColumnIndex(KEY_ID))).toLowerCase().contains(array[i].toLowerCase())) {

                            state.append(1,true);

                        }else{

                            state.append(1,false);

                        }
                    if (cursor.getString(cursor.getColumnIndex(CUSTOM_MATERIAL_COLUMN_DESCRIPTION)).toLowerCase().contains(array[i].toLowerCase())) {

                            state.append(2,true);

                        }else{

                            state.append(2,false);

                        }
                        if (cursor.getString(cursor.getColumnIndex(CUSTOM_MATERIAL_COLUMN_SEASONS)).toLowerCase().contains(array[i].toLowerCase())) {

                            state.append(3,true);

                        }else{

                            state.append(3,false);

                        }

                        if (getDealerNameAtCustomMaterial(cursor.getInt(cursor.getColumnIndex(KEY_ID))).toLowerCase().contains(array[i].toLowerCase())) {

                            state.append(4,true);

                        }else{

                            state.append(4,false);

                        }

                        if(state.indexOfValue(true)>=0){


                           state1.append(i,true);

                        }else {

                            state1.append(i,false);

                        }
                        state.clear();


                    }

                    if(state1.indexOfValue(false)<0){


                       materials.add(createCustomMaterial(cursor,true));

                    }
                    state1.clear();


                    // get the data into array, or class variable
                } while (cursor.moveToNext());
            }
            cursor.close();

        } catch (Exception e) {
            Utils.toast(mContext, e.toString());
        }


        return materials;

    }

    public boolean existCustomMaterial(String materialName){




        SQLiteDatabase db = this.getReadableDatabase();
        boolean exist=false;

        String filter = CUSTOM_MATERIAL_COLUMN_NAME;

        Cursor cursor = db.rawQuery(
                "select * from " + TABLE_CUSTOM_MATERIAL + " where " + filter + "= ?" +  " or " + filter + "= ?" , new String[]{String.valueOf(materialName),String.valueOf(materialName).toLowerCase()});


        try {

            while (cursor.moveToNext()) {
                exist=true;
                break;

            }
            cursor.close();

        } catch (Exception e) {
            Utils.toast(mContext, e.toString());
        }


        return exist;

    }


    ////////---------------------------------------CUTNOTELIST------------------------------------------------------//////////

    public    CutNoteList createCutNoteList(Cursor cursor){



        CutNoteList cutNoteList=new CutNoteList(cursor.getPosition());
        cutNoteList.setId(cursor.getInt(0));
        cutNoteList.setModel(getModelNameAtCutNoteList(cursor.getInt(0)));
        cutNoteList.setReference(cursor.getInt(1));
        cutNoteList.setStatus(Utils.convertStringToStatus(mContext,cursor.getString(2)));
        cutNoteList.setMaxPairCount( (cursor.getInt(3)));
        cutNoteList.setTotalPairCount( (cursor.getInt(4)));
        cutNoteList.setDate(cursor.getString(5));
        cutNoteList.setNoteCount(getNoteCountAtCutNoteList(cursor.getInt(cursor.getColumnIndex(KEY_ID))));
        //cutNoteList.addNote(getCutNoteB

        return  cutNoteList;
    }

    private Category createCutNoteListModelCategory(Cursor c){

        Category category=new Category();;



        category.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        category.setName(c.getString(c.getColumnIndex(MODEL_COLUMN_NAME)));
        category.setItemCount(getCutNoteListCountAtModel(category.getName()));



        return category;

    }

    private Category createCutNoteListCustumerCategory(Cursor c){

        Category category=new Category();;



        category.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        category.setName(c.getString(c.getColumnIndex(CUSTUMER_COLUMN_NAME)));
        category.setItemCount(getCutNoteListCountAtCustumer(category.getName()));



        return category;

    }

    public List<Category> getCutNoteListModelCategory(boolean all) {

        CategoryNameSortedList categories=new CategoryNameSortedList();


        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_MODEL;

        Cursor c  = db.rawQuery(selectQuery, null);



        try {
            if (c.moveToFirst()) {
                do {

                      final Category category=createCutNoteListModelCategory(c);

                       if(all){
                           categories.add(category);
                       }else{

                           if(category.getItemCount()>0){

                               categories.add(category);


                           }

                       }



                } while (c.moveToNext());
            }
            c.close();

        } catch (Exception e) {
            Utils.toast(mContext, "No se encuentran tipos de material " + e.toString());
        }


        return categories.getItemList();
    }

    public List<Category> getCutNoteListCustumerCategory(boolean all) {

        CategoryNameSortedList categories=new CategoryNameSortedList();


        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_CUSTUMER;

        Cursor c  = db.rawQuery(selectQuery, null);



        try {
            if (c.moveToFirst()) {
                do {

                    final Category category=createCutNoteListCustumerCategory(c);

                    if(all){
                        categories.add(category);
                    }else{

                        if(category.getItemCount()>0){

                            categories.add(category);


                        }

                    }



                } while (c.moveToNext());
            }
            c.close();

        } catch (Exception e) {
            Utils.toast(mContext, "No se encuentran tipos de material " + e.toString());
        }


        return categories.getItemList();
    }


    public List<Category> getCutNoteListCategoryWithAssignedModel(){

        CategoryNameSortedList categories=new CategoryNameSortedList();



        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_MODEL + " tm, "
                + TABLE_MODEL_CUTNOTE_LIST_RELATIONS + " tmcl WHERE tmcl."
                + KEY_MODEL_ID + " = " + "tm." + KEY_ID;

        Cursor c  = db.rawQuery(selectQuery, null);



        try {
            if (c.moveToFirst()) {
                do {



                    categories.add(createCutNoteListModelCategory(c));


                } while (c.moveToNext());
            }
            c.close();

        } catch (Exception e) {
            Utils.toast(mContext, "No se encuentra el material" + e.toString());
        }


        return categories.getItemList();
    }


    public List<CutNoteList> getAllCutNotesList() {

        List<CutNoteList> cutNotesBuffer = null;


        String selectQuery = "SELECT  * FROM " + TABLE_CUNOTE_LIST;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        try {

            cutNotesBuffer = new ArrayList<>();
            if (cursor.moveToFirst()) {
                do {



                    cutNotesBuffer.add(createCutNoteList(cursor));

                } while (cursor.moveToNext());
            }
            cursor.close();

        } catch (Exception e) {
            Utils.toast(mContext, e.toString());
        }


        Collections.sort(cutNotesBuffer, new Comparator<CutNoteList>() {
            @Override
            public int compare(CutNoteList cutNoteList, CutNoteList t1) {
                return Long.compare(cutNoteList.getReference(),t1.getReference());
            }
        });
        return cutNotesBuffer;
    }

    public List<CutNoteList> getRecentCutNoteList(int count) {

        int i = 0;

        List<CutNoteList> cutNoteList = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_CUNOTE_LIST;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        try {
            if (cursor.moveToLast()) {

                do {


                    if (i < count) {

                        cutNoteList.add(createCutNoteList(cursor));
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

        closeDB();

        return cutNoteList;
    }

    public long addCutNoteList(CutNoteList cutNoteList){


        long id=0;
        cutNoteList.setReference(getNewReference());

        id=insertCutNoteList(cutNoteList.getModelId(),cutNoteList.getReference(),Utils.convertStatusToString(mContext,cutNoteList.getStatus()),cutNoteList.getMaxPairCount(),cutNoteList.getTotalPairCount(),getDate());

        if(!cutNoteList.getCutNoteList().isEmpty()) {

            for (CutNote note : cutNoteList.getCutNoteList()) {

                note.setReference(cutNoteList.getReference());

                addCutNote(id,note);


            }

        }


        ContentValues contentValues=new ContentValues();
        contentValues.put("docid",id);
        contentValues.put(MODEL_COLUMN_NAME,getModelNameAtCutNoteList(id));
        contentValues.put(CUTNOTE_COLUMN_STATUS,Utils.convertStatusToString(mContext,cutNoteList.getStatus()));
        contentValues.put(CUTNOTE_COLUMN_REFERENCE,cutNoteList.getReference());
        contentValues.put(CUTNOTE_COLUMN_PAIR_COUNT,String.valueOf(cutNoteList.getTotalPairCount()));
        contentValues.put(CUSTUMER_COLUMN_NAME,getCustumerNameAtModel(cutNoteList.getModelId()));


        SQLiteDatabase db=this.getWritableDatabase();

        db.insert(FTS_CUTNOTE_LIST,null,contentValues);

          return  id;



    }

    private long insertCutNoteList(@NonNull long modelId,@NonNull long reference,String status,int maxPairCount,int totalPairCount,String date){

        long id=0;


        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(CUTNOTE_COLUMN_REFERENCE, reference);
        contentValues.put(CUTNOTE_COLUMN_STATUS, status);
        contentValues.put(CUTNOTE_COLUMN_MAX_PAIR_COUNT, maxPairCount);
        contentValues.put(CUTNOTE_COLUMN_PAIR_COUNT, totalPairCount);
        contentValues.put(CUTNOTE_COLUMN_DATE, date);




        id=db.insert(TABLE_CUNOTE_LIST, null, contentValues);


        insertModelCutNoteListRelation(modelId,id);

        return id;

    }

    public CutNoteList getCutNoteListById(int cutNoteListId){

        CutNoteList cutNoteList=null;

        SQLiteDatabase db = this.getWritableDatabase();


        String filter = KEY_ID;


        Cursor cursor = db.rawQuery(
                "select * from " + TABLE_CUNOTE_LIST + " where " + filter + "= ?", new String[]{String.valueOf(cutNoteListId)});

        while (cursor.moveToNext()) {

            cutNoteList=createCutNoteList(cursor);
            break;

        }
        cursor.close();



        return  cutNoteList;
    }

    public List<CutNoteList> getCutNoteListByModel(int modelId){


        List<CutNoteList>cutNoteList=new ArrayList<>();


        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_CUNOTE_LIST + " tn, "
                + TABLE_MODEL_CUTNOTE_LIST_RELATIONS+ " tcn WHERE tcn."
                + KEY_MODEL_ID + " = '" + String.valueOf(modelId) + "'" + " AND tn." + KEY_ID
                + " = " + "tcn." + KEY_CUTNOTE_LIST_ID;

        Cursor c  = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {

                cutNoteList.add(createCutNoteList(c));


            } while (c.moveToNext());
        }
        c.close();


        return  cutNoteList;
    }

    public List<CutNoteList> getCutNoteListByModel(String modelName){


        List<CutNoteList>cutNoteList=new ArrayList<>();


        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_CUNOTE_LIST + " tcl, "
                + TABLE_MODEL + " tm, "
                + TABLE_MODEL_CUTNOTE_LIST_RELATIONS+ " tmcl WHERE tm."
                + MODEL_COLUMN_NAME + " = '" + modelName + "'" + " AND tm." + KEY_ID
                + " = " + "tmcl." + KEY_MODEL_ID + " AND tcl." + KEY_ID + " = " + "tmcl." + KEY_CUTNOTE_LIST_ID ;

        Cursor c  = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {

                cutNoteList.add(createCutNoteList(c));


            } while (c.moveToNext());
        }
        c.close();


        return  cutNoteList;
    }

    public List<CutNoteList> getCutNoteListByStatus(String status){


        List<CutNoteList>cutNoteList=new ArrayList<>();


        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.rawQuery(
                "select * from " + TABLE_CUNOTE_LIST + " where " + CUTNOTE_COLUMN_STATUS + "= ?", new String[]{ Utils.convertStringToStatus(mContext,status).name()});


        if (c.moveToFirst()) {
            do {

                cutNoteList.add(createCutNoteList(c));


            } while (c.moveToNext());
        }
        c.close();


        return  cutNoteList;
    }

    public CutNoteList  getCutNoteListByReference(long reference){


        CutNoteList cutNoteList=null;


        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.rawQuery(
                "select * from " + TABLE_CUNOTE_LIST + " where " + CUTNOTE_COLUMN_REFERENCE + "= ?", new String[]{String.valueOf(reference)});


        if (c.moveToFirst()) {
            do {

                cutNoteList=(createCutNoteList(c));
                break;

            } while (c.moveToNext());
        }
        c.close();


        return  cutNoteList;
    }


    public  int getCutNoteListPairCount(long cutNoteListId){


        SQLiteDatabase db= getReadableDatabase();

        Cursor cursor=db.query(TABLE_CUNOTE,null,KEY_CUTNOTE_LIST_ID + " =? ",new String[]{String.valueOf(cutNoteListId)},null,null,null,null);

      int  pairCount=0;

        while (cursor.moveToNext()){




            final CutNote cutNote=createCutNote(cursor);

            pairCount=pairCount+cutNote.getPairCount();


        }

        cursor.close();

        return pairCount;



    }

    public boolean updateCutNoteList(CutNoteList cutNoteList) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CUTNOTE_COLUMN_STATUS, Utils.convertStatusToString(mContext,cutNoteList.getStatus()));
        contentValues.put(CUTNOTE_COLUMN_MAX_PAIR_COUNT, cutNoteList.getMaxPairCount());
        contentValues.put(CUTNOTE_COLUMN_PAIR_COUNT, cutNoteList.getTotalPairCount());


        if(!cutNoteList.getCutNoteList().isEmpty()) {

            removeAllCutNotByCutNoteListId(cutNoteList.getId());

            for (CutNote note : cutNoteList.getCutNoteList()) {

                note.setReference(cutNoteList.getReference());


                addCutNote(cutNoteList.getId(),note);


            }

        }

        return db.update(TABLE_CUNOTE_LIST, contentValues, KEY_ID + "=" + String.valueOf(cutNoteList.getId()), null) > 0;
    }

    public boolean updateCutNoteListStatusById(long cutNoteListId ,CutNote.cutNoteStatus status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CUTNOTE_COLUMN_STATUS, Utils.convertStatusToString(mContext,status));
         return db.update(TABLE_CUNOTE_LIST, contentValues, KEY_ID + "=" + String.valueOf(cutNoteListId), null) > 0;
    }

    public boolean deleteCutNoteList(long cutNoteListId){

        SQLiteDatabase db = this.getReadableDatabase();
        String filter = "_id=";

        boolean delete=false;



                removeAllCutNotByCutNoteListId(cutNoteListId);


        delete= db.delete(TABLE_CUNOTE_LIST, filter + "=" + String.valueOf(cutNoteListId), null) > 0;

        if(delete){

           removeModelCutNoteListByCutNotelistId(cutNoteListId);



        }

        return delete;




    }

    public List<CutNoteList> getCutNoteListByCoincidenceValue(String value){

        List<CutNoteList> cutNoteLists=new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_CUNOTE_LIST;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        String[] array = new String[0];
        array = (value).split(" ");
        SparseBooleanArray state=new SparseBooleanArray();
        SparseBooleanArray state1=new SparseBooleanArray();
        try {
            if (cursor.moveToFirst()) {
                do {


                     String name=getModelNameAtCutNoteList(cursor.getInt(cursor.getColumnIndex(KEY_ID)));


                    for(int i =0;i<array.length;i++){



                        if(name.toLowerCase().contains(array[i].toLowerCase())){

                            state.append(0,true);

                        }else{


                            state.append(0,false);
                        }
                        if(cursor.getString(cursor.getColumnIndex(CUTNOTE_COLUMN_REFERENCE)).toLowerCase().contains(array[i].toLowerCase())){

                            state.append(1,true);

                        }else{


                            state.append(1,false);

                        }
                        if(cursor.getString(cursor.getColumnIndex(CUTNOTE_COLUMN_STATUS)).toLowerCase().contains(array[i].toLowerCase())){

                            state.append(2,true);

                        }else{


                            state.append(2,false);
                        }


                        if(state.indexOfValue(true)>=0){

                            state1.append(i,true);


                        }else{


                            state1.append(i,false);
                        }

                        state.clear();



                    }

                    if(state1.indexOfValue(false)<0){


                        cutNoteLists.add(createCutNoteList(cursor));
                    }

                    state1.clear();

                    // get the data into array, or class variable
                } while (cursor.moveToNext());
            }
            cursor.close();

        } catch (Exception e) {
            Utils.toast(mContext, e.toString());
        }


        return cutNoteLists;

    }

    public List<Category> getCutNoteStatusCategory(){

        List<Category >categories=new ArrayList<>();

        Category category=new Category();
        category.setName(mContext.getResources().getString(R.string.cutNotes_status_in_progress));
        category.setItemCount(getCutNoteListCountAtStatus(Utils.convertStringToStatus(mContext,category.getName())));
        categories.add(category);

        Category category1=new Category();
        category1.setName(mContext.getResources().getString(R.string.cutNotes_status_indeterminated));
        category1.setItemCount(getCutNoteListCountAtStatus(Utils.convertStringToStatus(mContext,category1.getName())));
        categories.add(category1);

        Category category2=new Category();
        category2.setName(mContext.getResources().getString(R.string.cutNotes_status_finished));
        category2.setItemCount(getCutNoteListCountAtStatus(Utils.convertStringToStatus(mContext,category2.getName())));
        categories.add(category2);




        return categories;

    }

    public Integer getCutNoteListCountAtStatus(CutNote.cutNoteStatus status){


        SQLiteDatabase db = this.getReadableDatabase();
        int count=0;



        String filter = CUTNOTE_COLUMN_STATUS;


        Cursor cursor = db.rawQuery(
                "select * from " + TABLE_CUNOTE_LIST + " where " + filter + "= ?", new String[]{ Utils.convertStatusToString(mContext,status)});


        count=cursor.getCount();

        cursor.close();

        closeDB();

        return count;


    }


    //////////-------------------------------------CUTNOTE---------------------------------------------------------------------------------------////////////

    public long getReference(){


        String selectQuery= "SELECT * FROM " + TABLE_REFERENCE +" ORDER BY " +  KEY_ID  + " DESC LIMIT 1";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        long  refernece=0;
        if(cursor.moveToFirst())
           refernece  =  cursor.getInt( cursor.getColumnIndex(REFERENCE_COLUMN_VALUE) );
        cursor.close();

        return refernece;
    }

    public long getNewReference(){


        long value=0;



        SQLiteDatabase db1= getWritableDatabase();

        ContentValues contentValues=new ContentValues();

        value=getReference()+1;

        contentValues.put(REFERENCE_COLUMN_VALUE,value);


        db1.update(TABLE_REFERENCE,contentValues,null,null);

        return value;

    }

    public    CutNote createCutNote(Cursor cursor){


        CutNote cutNote=new CutNote(cursor.getInt(cursor.getColumnIndex(CUTNOTE_COLUMN_NOTE_NUMBER)));

        cutNote.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
        cutNote.setModel(getModelNameAtCutNoteList(cursor.getInt(cursor.getColumnIndex(KEY_CUTNOTE_LIST_ID))));
        cutNote.setReference(cursor.getInt(cursor.getColumnIndex(CUTNOTE_COLUMN_REFERENCE)));
        cutNote.setStatus(cutNoteStatus.valueOf(cursor.getString(cursor.getColumnIndex(CUTNOTE_COLUMN_STATUS))));

        if(cursor.getBlob(cursor.getColumnIndex(CUTNOTE_COLUMN_NOTES))!=null){



            cutNote.setSizeList((Map<String, Integer>)byteArrayToObject(cursor.getBlob(cursor.getColumnIndex(CUTNOTE_COLUMN_NOTES))));




        }
        cutNote.setDate(cursor.getString(cursor.getColumnIndex(CUTNOTE_COLUMN_DATE)));
















        return  cutNote;
    }

    public long addCutNote(long cutNoteListId,CutNote cutNote){




        return insertCutNote(cutNoteListId,cutNote.getReference(),cutNote.getNoteNumber(), cutNote.getStatus().name(),prepareObjectToByte(cutNote.getSizeList()),getDate());



    }

    public void updateCutNote(CutNote note){

        SQLiteDatabase db=getWritableDatabase();

        ContentValues contentValues= new ContentValues();

        contentValues.put(CUTNOTE_COLUMN_NOTE_NUMBER,note.getNoteNumber());
        contentValues.put(CUTNOTE_COLUMN_STATUS,note.getStatus().name());
        contentValues.put(CUTNOTE_COLUMN_NOTES,prepareObjectToByte(note.getSizeList()));

        if(note.getStatus().equals(cutNoteStatus.IN_PROGRESS)){

            updateCutNoteListStatusById(getCutNoteListByReference(note.getReference()).getId(),cutNoteStatus.IN_PROGRESS);

        }


        db.update(TABLE_CUNOTE,contentValues,KEY_ID + " =? ",new String[]{String.valueOf(note.getId())});



    }

    public boolean deleteCutNote(long cutNoteListId,long cutNoteId){


        boolean delete = false;


        SQLiteDatabase db = this.getWritableDatabase();
        String where = KEY_ID + " = ? " + KEY_CUTNOTE_LIST_ID + " = ? ";
        String[] whereArgs = new String[]{String.valueOf(cutNoteListId),String.valueOf(cutNoteId)};


        delete= db.delete(TABLE_CUNOTE, where, whereArgs) > 0;


        return delete;


    }

    public boolean deleteCutNote(long cutNoteId){


        boolean delete = false;


        SQLiteDatabase db = this.getWritableDatabase();
        String where = KEY_ID + " = ? ";
        String[] whereArgs = new String[]{String.valueOf(cutNoteId)};


        delete= db.delete(TABLE_CUNOTE, where, whereArgs) > 0;


        return delete;


    }


    public  int getNoteCountAtCutNoteList(long cutNoteListId){


        SQLiteDatabase db= getReadableDatabase();

        Cursor cursor=db.query(TABLE_CUNOTE,null,KEY_CUTNOTE_LIST_ID + " =? ",new String[]{String.valueOf(cutNoteListId)},null,null,null,null);

        final int count = cursor.getCount();

        cursor.close();

        return count;



    }

    public CutNote getCutNoteByNoteNumber(long cutNoteListId,int noteNumber){


        CutNote note=null;


        SQLiteDatabase db= getReadableDatabase();


        Cursor c  = db.query(TABLE_REFERENCE,null, KEY_CUTNOTE_LIST_ID + " =? " + CUTNOTE_COLUMN_NOTE_NUMBER + " =? ", new String[]{String.valueOf(cutNoteListId),String.valueOf(noteNumber)}, null, null, null, null);

        while (c.moveToNext()){
            note= createCutNote(c);

        }



        c.close();


        return note;

    }

    public List<CutNote>getCutNoteByReference(long reference){

        SQLiteDatabase db= getReadableDatabase();

        Cursor cursor=db.query(TABLE_CUNOTE,null,CUTNOTE_COLUMN_REFERENCE + " =? ",new String[]{String.valueOf(reference)},null,null,null,null);

        List<CutNote> cutNoteList=new ArrayList<>();


        try{

            if (cursor.moveToFirst()) {
                do {



                    cutNoteList.add(createCutNote(cursor));

                } while (cursor.moveToNext());
            }
            cursor.close();

        } catch (Exception e) {
            Utils.toast(mContext, e.toString());
        }



        return cutNoteList;
    }

    public CutNote getCutNote(long id){





        CutNote cutNote=null;

        SQLiteDatabase db = this.getReadableDatabase();


        String filter = KEY_ID;


        Cursor cursor = db.rawQuery(
                "select * from " + TABLE_CUNOTE + " where " + filter + "= ?", new String[]{String.valueOf(id)});

        while (cursor.moveToNext()) {

            cutNote=createCutNote(cursor);
            break;

        }
        cursor.close();


        closeDB();


        return cutNote;




    }


    public List<CutNote>getCutNoteByCuteNoteListId(long  cutNoteLisyId){

        SQLiteDatabase db= getReadableDatabase();

        Cursor cursor=db.query(TABLE_CUNOTE,null,KEY_CUTNOTE_LIST_ID + " =? ",new String[]{String.valueOf(cutNoteLisyId)},null,null,null,null);

        List<CutNote> cutNoteList=new ArrayList<>();


        try{

            if (cursor.moveToFirst()) {
                do {



                    cutNoteList.add(createCutNote(cursor));

                } while (cursor.moveToNext());
            }
            cursor.close();

        } catch (Exception e) {
            Utils.toast(mContext, e.toString());
        }



        return cutNoteList;
    }

    public long insertCutNote(long cutNoteListId,long reference ,int noteNumber, String status,byte[]values,String date){



        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_CUTNOTE_LIST_ID,cutNoteListId);
        contentValues.put(CUTNOTE_COLUMN_NOTE_NUMBER, noteNumber);
        contentValues.put(CUTNOTE_COLUMN_REFERENCE, reference);
        contentValues.put(CUTNOTE_COLUMN_STATUS, status);
        contentValues.put(CUTNOTE_COLUMN_NOTES, values);
        contentValues.put(CUTNOTE_COLUMN_DATE, date);

        return db.insert(TABLE_CUNOTE, null, contentValues);





    }

    public boolean removeAllCutNotByCutNoteListId(long cutNoteListId){


        boolean delete = false;


        SQLiteDatabase db = this.getWritableDatabase();
        String where = KEY_CUTNOTE_LIST_ID + " =? ";
        String[] whereArgs = new String[]{String.valueOf(cutNoteListId)};


        delete= db.delete(TABLE_CUNOTE, where, whereArgs) > 0;


        return delete;


    }

    ///////////---------------------------------MATERIALS TYPES------------------------------------------------------------------------------------/////////

    private Category createMaterialTypeCategory(Cursor c){

          Category materialCategory;

                    materialCategory=new Category();

                    materialCategory.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                    materialCategory.setName(c.getString(c.getColumnIndex(MATERIAL_TYPE_COLUMN_NAME)));
                    materialCategory.setItemCount(getMaterialCountAtMaterialType(materialCategory.getName()));



          return materialCategory;

    }

    public long addMaterialType(String name){

        long id=0;

        if(!existMaterialType(name)){

          id=  insertMaterialTypes(name);


        }


        return id;

    }

    public boolean existMaterialType(String name){



        SQLiteDatabase db = this.getReadableDatabase();
        boolean exist=false;

        String filter = MATERIAL_TYPE_COLUMN_NAME;

        Cursor cursor = db.rawQuery(
                "select * from " + TABLE_MATERIAL_TYPES + " where " + filter + "= ?" +  " or " + filter + "= ?", new String[]{String.valueOf(name),String.valueOf(name).toLowerCase()});


        try {

            while (cursor.moveToNext()) {
                exist=true;
                break;

            }
            cursor.close();

        } catch (Exception e) {
            Utils.toast(mContext, e.toString());
        }


        return exist;

    }

    public long insertMaterialTypes(String name) {


        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MATERIAL_TYPE_COLUMN_NAME, name);
       long id= db.insert(TABLE_MATERIAL_TYPES, null, contentValues);


        return id;

    }

    public Category getMaterialTypeCategory(long id) {

        Category category = null;
        SQLiteDatabase db = this.getWritableDatabase();
        String filter = "_id";

        Cursor c = db.rawQuery(
                "select * from " + TABLE_MATERIAL_TYPES + " where " + filter + " = ?", new String[]{String.valueOf(id)});


        try {

            category = new Category();
            while (c.moveToNext()) {


                category=createMaterialTypeCategory(c);

            }
            c.close();

        } catch (Exception e) {
            Utils.toast(mContext, e.toString());
        }
        closeDB();

        return category;
    }

    public List<String> getMaterialsTypes() {

        List<String> types = new ArrayList<>();
         SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_MATERIAL_TYPES,new String[]{MATERIAL_TYPE_COLUMN_NAME},null,null,null,null, null);

        try {
            if (cursor.moveToFirst()) {
                do {


                    types.add(cursor.getString(cursor.getColumnIndex(MATERIAL_TYPE_COLUMN_NAME)));

                } while (cursor.moveToNext());
            }
            cursor.close();

        } catch (Exception e) {
            Utils.toast(mContext, e.toString());
        }

         Collections.sort(types);

        return types;
    }

    public List<Category> getMaterialsTypesCategory(boolean all) {

        CategoryNameSortedList categories=new CategoryNameSortedList();


        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_MATERIAL_TYPES;

        Cursor c  = db.rawQuery(selectQuery, null);



        try {
            if (c.moveToFirst()) {
                do {


                    final Category category=createMaterialTypeCategory(c);

                    if(all){
                        categories.add(category);
                    }else{

                        if(category.getItemCount()>0){

                            categories.add(category);


                        }

                    }


                } while (c.moveToNext());
            }
            c.close();

        } catch (Exception e) {
            Utils.toast(mContext, "No se encuentran tipos de material " + e.toString());
        }


        return categories.getItemList();
    }

    public List<Category> getMaterialsTypesCategoryWithAssignedMaterials() {

        CategoryNameSortedList categories=new CategoryNameSortedList();


        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_MATERIAL_TYPES + " tm, "
                + TABLE_MATERIAL_TYPES_CUSTOM_MATERIAL_RELATIONS + " tmcl WHERE tmcl."
                + KEY_MATERIAL_TYPE_ID + " = " + "tm." + KEY_ID;

        Cursor c  = db.rawQuery(selectQuery, null);



        try {
            if (c.moveToFirst()) {
                do {



                    categories.add(createMaterialTypeCategory(c));


                } while (c.moveToNext());
            }
            c.close();

        } catch (Exception e) {
            Utils.toast(mContext, "No se encuentra el material" + e.toString());
        }




        return categories.getItemList();
    }

    public long getMaterialTypeId(String name) {



        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_MATERIAL_TYPES,null,  MATERIAL_TYPE_COLUMN_NAME+ " =? ",new String[]{name},null,null, null);

        try {

            while (cursor.moveToNext()){

               return cursor.getInt(cursor.getColumnIndex(KEY_ID));

            }


            cursor.close();

        } catch (Exception e) {
            Utils.toast(mContext, "No se puede obtener el proveedor" + e.toString());
        }


        return 0;
    }

    public String getMaterialTypeName(long id){





        String name=null;
        SQLiteDatabase db = this.getReadableDatabase();


        String filter = KEY_ID;


        Cursor cursor = db.rawQuery(
                "select * from " + TABLE_MATERIAL_TYPES + " where " + filter + "= ?", new String[]{String.valueOf(id)});

        while (cursor.moveToNext()) {

            name=cursor.getString(1);
            break;

        }
        cursor.close();



        return name;



    }

    public boolean updateMaterialType(long id, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String filter =KEY_ID;
        ContentValues contentValues = new ContentValues();
        contentValues.put(MATERIAL_TYPE_COLUMN_NAME, name);

        return db.update(TABLE_MATERIAL_TYPES, contentValues, filter + "=" + String.valueOf(id), null) > 0;
    }


    public boolean deleteMaterialType(long id){

        boolean delete=false;
        SQLiteDatabase db = this.getWritableDatabase();
        List typesList = new ArrayList();
        typesList.addAll(Arrays.asList(mContext.getResources().getStringArray(R.array.default_materialsType)));

        if(!typesList.contains(getMaterialTypeName(id))){

            delete= db.delete(TABLE_MATERIAL_TYPES, KEY_ID + " =? ", new String[]{String.valueOf(id)}) > 0;


        }




        return  delete;

    }


    public boolean isMaterialTypeAssigned(long id) {

        SQLiteDatabase db = this.getWritableDatabase();

        boolean exist=false;

        String filter = KEY_MATERIAL_TYPE_ID;

        Cursor cursor = db.rawQuery(
                "select * from " + TABLE_MATERIAL_TYPES_CUSTOM_MATERIAL_RELATIONS+ " where " + filter + "= ?", new String[]{String.valueOf(id)});





        try {

            while (cursor.moveToNext()) {
                exist=true;
                break;

            }
            cursor.close();

        } catch (Exception e) {
            Utils.toast(mContext, e.toString());
        }


        return exist;
    }


    ///////////----------------------DEALERSHIPS--------------------------------------------------------------------------------------------/////////

    private Category createDealerCategory(Cursor c){

        Category dealerCategory;

        dealerCategory=new Category();

        dealerCategory.setId(c.getInt(c.getColumnIndex(KEY_ID)));
       dealerCategory.setName(c.getString(c.getColumnIndex(DEALER_COLUMN_NAME)));
       dealerCategory.setItemCount(getMaterialCountAtDealer(dealerCategory.getName()));



        return dealerCategory;

    }

    public Category getDealerCategory(long id) {

        Category category = null;
        SQLiteDatabase db = this.getWritableDatabase();
        String filter = "_id";

        Cursor c = db.rawQuery(
                "select * from " + TABLE_DEALERSHIP + " where " + filter + " = ?", new String[]{String.valueOf(id)});


        try {

            category = new Category();
            while (c.moveToNext()) {


                category=createDealerCategory(c);

            }
            c.close();

        } catch (Exception e) {
            Utils.toast(mContext, e.toString());
        }
        closeDB();

        return category;
    }

    public List<Category> getDealersCategory(boolean all) {

        CategoryNameSortedList categories=new CategoryNameSortedList();

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_DEALERSHIP;

        Cursor c  = db.rawQuery(selectQuery, null);



        try {
            if (c.moveToFirst()) {
                do {


                    final Category category=createDealerCategory(c);

                    if(all){
                        categories.add(category);
                    }else{

                        if(category.getItemCount()>0){

                            categories.add(category);


                        }

                    }


                } while (c.moveToNext());
            }
            c.close();

        } catch (Exception e) {
            Utils.toast(mContext, "No se encuentran proveedores " + e.toString());
        }



        return categories.getItemList();
    }

    private static Dealership createDealer(Cursor cursor){


        Dealership dealer=new Dealership();

        dealer.setId(cursor.getInt(0));
        dealer.setName(cursor.getString(1));
        dealer.setPhone(cursor.getString(2));
        dealer.setAdress(cursor.getString(3));
        dealer.setEmail(cursor.getString(4));
        dealer.setCategory(cursor.getString(5));
        dealer.setCompany(cursor.getString(6));
        dealer.setDate(cursor.getString(7));


        return dealer;
    }

    public List<Dealership> getDealerShips() {

        List<Dealership> dealers = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_DEALERSHIP;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        try {
            if (cursor.moveToFirst()) {
                do {

                    dealers.add(createDealer(cursor));

                } while (cursor.moveToNext());
            }
            cursor.close();

        } catch (Exception e) {
            Utils.toast(mContext, e.toString());
        }


        closeDB();

        Collections.sort(dealers, new Comparator<Dealership>() {
            @Override
            public int compare(Dealership dealership, Dealership t1) {

                return dealership.getName().compareToIgnoreCase(t1.getName());
            }
        });
        return dealers;
    }

    public long addDealership(Dealership dealership) {


        if (dealership.getCategory() == null) {


            dealership.setCategory(mContext.getResources().getString(R.string.importNoAsigned_Cat));

        }


       return insertDealership(dealership.getName(), dealership.getPhone(), dealership.getAdress(), dealership.getEmail(), dealership.getCategory(), dealership.getCompany(), getDate());

     }

    public long insertDealership(String name, String phone, String adress, String email, String category, String company, String date) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DEALER_COLUMN_NAME, name);
        contentValues.put(DEALER_COLUMN_PHONE, phone);
        contentValues.put(DEALER_COLUMN_ADRESS, adress);
        contentValues.put(DEALER_COLUMN_EMAIL, email);
        contentValues.put(DEALER_COLUMN_CATEGORY, category);
        contentValues.put(DEALER_COLUMN_COMPANY, company);
        contentValues.put(DEALER_COLUMN_DATE, date);
       return db.insert(TABLE_DEALERSHIP, null, contentValues);
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

    public boolean deleteDealership(long dealerId) {

        boolean delete=false;
        SQLiteDatabase db = this.getWritableDatabase();
        delete= db.delete(TABLE_DEALERSHIP, KEY_ID + " =? ", new String[]{String.valueOf(dealerId)}) > 0;

       if(delete){

           removeDealerCustomMaterialRelation(dealerId);
       }

       return  delete;
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

    public Dealership getDealerShipById(int id) {

        Dealership dealer = new Dealership();


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_DEALERSHIP,null, KEY_ID + " =? ",new String[]{String.valueOf(id)},null,null, null);

        try {

            while (cursor.moveToNext()){

                dealer=createDealer(cursor);
                break;
            }


            cursor.close();

        } catch (Exception e) {
            Utils.toast(mContext, "No se puede obtener el proveedor" + e.toString());
        }


        return dealer;
    }

    public Dealership getDealerShipByName(String name) {

        Dealership dealer = new Dealership();


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_DEALERSHIP,null, DEALER_COLUMN_NAME + " =? ",new String[]{name},null,null, null);

        try {

            while (cursor.moveToNext()){

                dealer=createDealer(cursor);
                break;
            }


            cursor.close();

        } catch (Exception e) {
            Utils.toast(mContext, "No se puede obtener el proveedor" + e.toString());
        }


        return dealer;
    }

    public List<String> getDealersCategories() {

        final List<String> titleList = new ArrayList<>();


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_DEALERSHIP,new String[]{DEALER_COLUMN_CATEGORY},null,null,null,null,null);
        HashSet hs = new HashSet();
        try {
            if (cursor.moveToFirst()) {
                do {


                    hs.add(cursor.getString(cursor.getColumnIndex(DEALER_COLUMN_CATEGORY)));

                } while (cursor.moveToNext());
            }
            cursor.close();

        } catch (Exception e) {
            Utils.toast(mContext, e.toString());
        }

        titleList.addAll(hs);
        Collections.sort(titleList);
        closeDB();

        return titleList;

    }

    public List<String> getDealersNamesWithAssignedCustomMaterials(){

        List<String>dealerList=new ArrayList<>();
        HashSet hs=new HashSet();


        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_DEALERSHIP + " td, "
                + TABLE_DEALER_CUSTOM_MATERIAL_RELATIONS + " tdcm WHERE tdcm."
                + KEY_DEALER_ID + " = " + "td." + KEY_ID;

        Cursor c  = db.rawQuery(selectQuery, null);



        try {
            if (c.moveToFirst()) {
                do {



                        hs.add(c.getString(c.getColumnIndex(DEALER_COLUMN_NAME)));


                } while (c.moveToNext());
            }
            c.close();

        } catch (Exception e) {
            Utils.toast(mContext, "No se encuentra el material" + e.toString());
        }


        dealerList.addAll(hs);

        Collections.sort(dealerList);

        return dealerList;
    }

    public List<Category> getDealerCategoryWithAssignedCustomMaterial(){


        CategoryNameSortedList categories=new CategoryNameSortedList();



        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_DEALERSHIP + " td, "
                + TABLE_DEALER_CUSTOM_MATERIAL_RELATIONS + " tdcm WHERE tdcm."
                + KEY_DEALER_ID + " = " + "td." + KEY_ID;

        Cursor c  = db.rawQuery(selectQuery, null);



        try {
            if (c.moveToFirst()) {
                do {



                    categories.add(createDealerCategory(c));


                } while (c.moveToNext());
            }
            c.close();

        } catch (Exception e) {
            Utils.toast(mContext, "No se encuentra el material" + e.toString());
        }









        return categories.getItemList();
    }

    public boolean existDealer(String dealerName){


        SQLiteDatabase db = this.getReadableDatabase();
        boolean exist=false;

        String filter = DEALER_COLUMN_NAME;

        Cursor cursor = db.rawQuery(
                "select * from " + TABLE_DEALERSHIP + " where " + filter + "= ?" +  " or " + filter + "= ?", new String[]{String.valueOf(dealerName),String.valueOf(dealerName).toLowerCase()});


        try {

            while (cursor.moveToNext()) {
                exist=true;
                break;

            }
            cursor.close();

        } catch (Exception e) {
            Utils.toast(mContext, e.toString());
        }

        return exist;
    }

    public boolean isDealerAssigned(long id) {

        SQLiteDatabase db = this.getWritableDatabase();

        boolean exist=false;

        String filter = KEY_DEALER_ID;

        Cursor cursor = db.rawQuery(
                "select * from " + TABLE_DEALER_CUSTOM_MATERIAL_RELATIONS+ " where " + filter + "= ?", new String[]{String.valueOf(id)});





        try {

            while (cursor.moveToNext()) {
                exist=true;
                break;

            }
            cursor.close();

        } catch (Exception e) {
            Utils.toast(mContext, e.toString());
        }


        return exist;
    }


    /////////////----------------------CUSTUMER-------------------------------------------/////////////

    private Category createCustumerCategory(Cursor c){

        Category custumerCategory;

       custumerCategory=new Category();

        custumerCategory.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        custumerCategory.setName(c.getString(c.getColumnIndex(CUSTUMER_COLUMN_NAME)));
        custumerCategory.setItemCount(getModelCountAtCustumer(custumerCategory.getName()));



        return custumerCategory;

    }

    public long addCustumer(Custumer custumer) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(CUSTUMER_COLUMN_NAME,custumer.name);
        return db.insert(TABLE_CUSTUMER, null, contentValues);

    }

    public Long getCustumerId(String custumerName){



        long id=0;
        SQLiteDatabase db = this.getReadableDatabase();


        String filter = CUSTUMER_COLUMN_NAME;


        Cursor cursor = db.rawQuery(
                "select * from " + TABLE_CUSTUMER + " where " + filter + "= ?", new String[]{custumerName});

        while (cursor.moveToNext()) {

            id=cursor.getLong(0);
            break;

        }
        cursor.close();





        return id;



    }

    public Category getCustumerCategory(long id) {

        Category category = null;
        SQLiteDatabase db = this.getWritableDatabase();
        String filter = "_id";

        Cursor c = db.rawQuery(
                "select * from " + TABLE_CUSTUMER + " where " + filter + " = ?", new String[]{String.valueOf(id)});


        try {

            category = new Category();
            while (c.moveToNext()) {


                category=createCustumerCategory(c);

            }
            c.close();

        } catch (Exception e) {
            Utils.toast(mContext, e.toString());
        }
        closeDB();

        return category;
    }

    public List<Category> getCustumerCategory(boolean all) {

        CategoryNameSortedList categories=new CategoryNameSortedList();

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_CUSTUMER;

        Cursor c  = db.rawQuery(selectQuery, null);



        try {
            if (c.moveToFirst()) {
                do {


                    final Category category=createCustumerCategory(c);

                    if(all){
                        categories.add(category);
                    }else{

                        if(category.getItemCount()>0){

                            categories.add(category);

                            break;


                        }

                    }


                } while (c.moveToNext());
            }
            c.close();

        } catch (Exception e) {
            Utils.toast(mContext, "No se encuentran proveedores ");
        }



        return categories.getItemList();
    }

    public int getCustumerCount(){



        String countQuery = "SELECT  * FROM " + TABLE_CUSTUMER;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        final int count = cursor.getCount();

        cursor.close();

        // return count
        return count;







    }


    ///////////------------------------ACCESSORIES----------------------------------------/////////////

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

    ///////////--------------------------EXTERNALWORKS------------------------------------------/////////

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



    /////////////////-----------------FTS-------------------------////////


    public List<String> printFTS(){


        List<String>data=null;

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + FTS_CUSTOM_MATERIAL;

        Cursor c  = db.rawQuery(selectQuery, null);



        try {
            if (c.moveToFirst()) {
                data=new ArrayList<>();
                do {

                    String line= "docid" + String.valueOf(c.getInt(0)) + ",";
                    line+= CUSTOM_MATERIAL_COLUMN_NAME + c.getString(1)+ ",";
                    line+=  MATERIAL_TYPE_COLUMN_NAME + c.getString(2)+ ",";
                    line+= DEALER_COLUMN_NAME + c.getString(3)+ ",";
                    line+= CUSTOM_MATERIAL_COLUMN_DESCRIPTION + c.getString(4)+ ",";
                    line+= CUSTOM_MATERIAL_COLUMN_SEASONS + c.getString(5) + '\n';

                    data.add(line);



                } while (c.moveToNext());
            }
            c.close();

        } catch (Exception e) {
            Utils.toast(mContext, "No se encuentran proveedores ");
        }


          return data;


    }



    //////-------------------------RELATIONS---------------------------------------------////////


   //--------Model,Piece,PieceMaterial

    private void insertPreviewPieces(long modelId,long pieceId,long materialId){
        SQLiteDatabase db = getWritableDatabase();


            final ContentValues contentValues=new ContentValues();

            contentValues.put(KEY_MODEL_ID,modelId);
            contentValues.put(KEY_PIECE_ID, pieceId);
            contentValues.put(KEY_MATERIAL_ID, materialId);


        db.insert(TABLE_MODEL__PREVIEW_PIECES_RELATIONS,null,contentValues);






    }

    private boolean removeModelPreviewPiecesRelationByPiece(long modelId, long pieceId){

        SQLiteDatabase db = this.getWritableDatabase();
        String where =KEY_MODEL_ID + " = ? AND " + KEY_PIECE_ID + " = ? ";
        String[] whereArgs = new String[]{String.valueOf(modelId), String.valueOf(pieceId)};


        return db.delete(TABLE_MODEL__PREVIEW_PIECES_RELATIONS, where, whereArgs) > 0;


    }

    private boolean removeModelPreviewPiecesRelationByModel(long modelId){

        SQLiteDatabase db = this.getWritableDatabase();
        String where =KEY_MODEL_ID + " = ? ";
        String[] whereArgs = new String[]{String.valueOf(modelId)};


        return db.delete(TABLE_MODEL__PREVIEW_PIECES_RELATIONS, where, whereArgs) > 0;


    }

    public boolean updateModelPreviewPiece(long modelId, long pieceId, long materialId) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_MATERIAL_ID,materialId);


        String where =KEY_MODEL_ID + " = ? AND " + KEY_PIECE_ID + " = ? ";
        String[] whereArgs = new String[]{String.valueOf(modelId), String.valueOf(pieceId)};

        return db.update(TABLE_MODEL__PREVIEW_PIECES_RELATIONS, contentValues, where, whereArgs) > 0;
    }

    //--------Model,Material,Custom Material------------------------------------------/////////

    private void insertMaterialCustomMaterialRelation(long modelId, long materialId, long custommaterialId){
        SQLiteDatabase db = this.getWritableDatabase();


        final ContentValues contentValues=new ContentValues();

        contentValues.put(KEY_MODEL_ID,modelId);
        contentValues.put(KEY_MATERIAL_ID, materialId);
        contentValues.put(KEY_CUSTOM_MATERIAL_ID,custommaterialId);


        db.insert(TABLE_MATERIAL_CUSTOM_MATERIAL_RELATIONS,null,contentValues);






    }

    private boolean removeMaterialCustomMaterialRelationByMaterial(long modelId, long materialId){

        SQLiteDatabase db = this.getWritableDatabase();
        String where =KEY_MODEL_ID + " = ? AND " + KEY_MATERIAL_ID + " = ? ";
        String[] whereArgs = new String[]{String.valueOf(modelId), String.valueOf(materialId)};


        return db.delete(TABLE_MATERIAL_CUSTOM_MATERIAL_RELATIONS, where, whereArgs) > 0;


    }

    private boolean removeMaterialCustomMaterialRelationByCustomMaterial(long customMaterilaId){

        SQLiteDatabase db = this.getWritableDatabase();
        String where =KEY_CUSTOM_MATERIAL_ID + " = ? ";
        String[] whereArgs = new String[]{String.valueOf(customMaterilaId)};


        return db.delete(TABLE_MATERIAL_CUSTOM_MATERIAL_RELATIONS, where, whereArgs) > 0;


    }

    private boolean removeMaterialCustomMaterialRelationByModel(long modelId){

        SQLiteDatabase db = this.getWritableDatabase();
        String where =KEY_MODEL_ID + " = ? ";
        String[] whereArgs = new String[]{String.valueOf(modelId)};


        return db.delete(TABLE_MATERIAL_CUSTOM_MATERIAL_RELATIONS, where, whereArgs) > 0;


    }

    public boolean updateMaterialCustomMaterialRelation(long modelId, long materialId, long custommaterialId) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_CUSTOM_MATERIAL_ID,custommaterialId);


         String where =KEY_MODEL_ID + " = ? AND " + KEY_MATERIAL_ID + " = ? ";
        String[] whereArgs = new String[]{String.valueOf(modelId), String.valueOf(materialId)};



        return db.update(TABLE_MATERIAL_CUSTOM_MATERIAL_RELATIONS, contentValues, where, whereArgs) > 0;
    }

    private long getCustomMaterialAtMaterial(long modelId,long materialId){


        long customMaterialId=0;

        SQLiteDatabase db= this.getReadableDatabase();

        String where = KEY_MODEL_ID + "= ? AND "  + KEY_MATERIAL_ID + "= ?";
        String[] whereArgs = new String[]{String.valueOf(modelId),String.valueOf(materialId)};

        Cursor c  = db.query(TABLE_MATERIAL_CUSTOM_MATERIAL_RELATIONS,new String[]{KEY_CUSTOM_MATERIAL_ID}, where, whereArgs, null, null, null, null);
        try {


            while (c.moveToNext()) {


                customMaterialId=c.getInt(c.getColumnIndex(KEY_CUSTOM_MATERIAL_ID));


            }
            c.close();

        } catch (Exception e) {
            Utils.toast(mContext, e.toString());
        }


        return customMaterialId;



    }

    //--------Model,CutnoteList------------------------------------------/////////

    private void insertModelCutNoteListRelation(long modelId, long cutNoteListId){
        SQLiteDatabase db =this.getWritableDatabase();


        final ContentValues contentValues=new ContentValues();

        contentValues.put(KEY_MODEL_ID,modelId);
        contentValues.put(KEY_CUTNOTE_LIST_ID, cutNoteListId);


        db.insert(TABLE_MODEL_CUTNOTE_LIST_RELATIONS,null,contentValues);






    }

    private boolean removeModelCutNoteListByCutNotelistId(long cutNoteListId){

        SQLiteDatabase db = this.getReadableDatabase();
        String where =KEY_CUTNOTE_LIST_ID + " = ? ";
        String[] whereArgs = new String[]{String.valueOf(cutNoteListId)};

        return db.delete(TABLE_MODEL_CUTNOTE_LIST_RELATIONS, where, whereArgs) > 0;


    }

    private boolean removeModelCutNoteListRelationByModel(long modelId){

        boolean delete=false;

        SQLiteDatabase db = this.getReadableDatabase();
        String where =KEY_MODEL_ID + " = ? ";
        String[] whereArgs = new String[]{String.valueOf(modelId)};


        Cursor c  = db.query(TABLE_MODEL_CUTNOTE_LIST_RELATIONS,new String[]{KEY_CUTNOTE_LIST_ID},where,whereArgs,null,null,null);


        while (c.moveToNext()){


            deleteCutNoteList(c.getInt(c.getColumnIndex(KEY_CUTNOTE_LIST_ID)));



        }

        delete= db.delete(TABLE_MODEL_CUTNOTE_LIST_RELATIONS, where, whereArgs) > 0;

        c.close();

         return delete;


    }

    private String getModelNameAtCutNoteList(long cutNoteListId){

        String name=null;


        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_MODEL + " tm, "
                + TABLE_MODEL_CUTNOTE_LIST_RELATIONS + " tcn WHERE tcn."
                + KEY_CUTNOTE_LIST_ID + " = '" + String.valueOf(cutNoteListId) + "'" + " AND tm." + KEY_ID
                + " = " + "tcn." + KEY_MODEL_ID ;

        Cursor c  = db.rawQuery(selectQuery, null);

        while (c.moveToNext()){

          name=c.getString(c.getColumnIndex(MODEL_COLUMN_NAME));

        }
        c.close();

        if(name==null){


            name=mContext.getResources().getString(R.string.importNoAsigned_Cat);
        }

        return name;
    }

    private Integer getCutNoteListCountAtModel(String modelName){


        SQLiteDatabase db = this.getReadableDatabase();
        int count=0;


        String selectQuery = "SELECT  * FROM " + TABLE_MODEL + " m, "
                +  TABLE_MODEL_CUTNOTE_LIST_RELATIONS + " tmcl WHERE m."
                +  MODEL_COLUMN_NAME + " = '" + modelName + "'" + " AND m." + KEY_ID
                + " = " + "tmcl." + KEY_MODEL_ID;

        Cursor c  = db.rawQuery(selectQuery, null);

        count=c.getCount();

        c.close();

        return count;

    }

    private Integer getCutNoteListCountAtCustumer(String custumerName){


        SQLiteDatabase db = this.getReadableDatabase();
        int count=0;


    String selectQuery = "SELECT  * FROM " + TABLE_MODEL_CUSTUMER_RELATIONS + " tmc, " + TABLE_CUSTUMER + " c, "
                +  TABLE_MODEL_CUTNOTE_LIST_RELATIONS + " tmcl WHERE c."
                +  CUSTUMER_COLUMN_NAME + " = '" + custumerName + "'" + " AND c." + KEY_ID
                + " = " + "tmc." + KEY_CUSTUMER_ID + " AND tmcl." + KEY_MODEL_ID + " = " + "tmc." + KEY_MODEL_ID ;

        Cursor c  = db.rawQuery(selectQuery, null);

        count=c.getCount();

        c.close();

        return count;

    }


    //---------Model,Directory------------------------------------------/////////

    private long insertModelDirectoryRelation(long modelId,long directoryId){



        SQLiteDatabase db = this.getWritableDatabase();


            final ContentValues contentValues=new ContentValues();


            contentValues.put(KEY_MODEL_ID,modelId);
            contentValues.put(KEY_DIRECTORY_ID, directoryId);

         Long id=   db.insert(TABLE_MODEL_DIRECTORY_RELATIONS,null,contentValues);






        return   id;



    }

     private  boolean removeModelDirectoryRelation(long directoryId){


        SQLiteDatabase db = this.getReadableDatabase();
        String filter = KEY_DIRECTORY_ID;

        return db.delete(TABLE_MODEL_DIRECTORY_RELATIONS, filter + "=" + String.valueOf(directoryId), null) > 0;


    }

    private boolean removeModelDirectoryRelationByModel(long modelId){

        boolean delete=false;

        SQLiteDatabase db = this.getReadableDatabase();
        String where =KEY_MODEL_ID + " = ? ";
        String[] whereArgs = new String[]{String.valueOf(modelId)};


        delete= db.delete(TABLE_MODEL_DIRECTORY_RELATIONS, where, whereArgs) > 0;


        return delete;


    }

    private   String getDirectoryNameAtModel(long modelId){

        String name=null;


        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_DIRECTORY + " td, "
                + TABLE_MODEL_DIRECTORY_RELATIONS + " tmd WHERE tmd."
                + KEY_MODEL_ID + " = '" + String.valueOf(modelId) + "'" + " AND td." + KEY_ID
                + " = " + "tmd." + KEY_DIRECTORY_ID ;

        Cursor c  = db.rawQuery(selectQuery, null);

        while (c.moveToNext()){

            name=c.getString(c.getColumnIndex(DIRECTORY_COLUMN_NAME));

        }
        c.close();

        if(name==null){


            name=mContext.getResources().getString(R.string.importNoAsigned_Cat);
        }

        return name;
    }

    private Integer getModelCountAtDirectory(String directoryName){


        SQLiteDatabase db = this.getReadableDatabase();
        int count=0;


        String selectQuery = "SELECT  * FROM " + TABLE_DIRECTORY + " td, "
                +  TABLE_MODEL_DIRECTORY_RELATIONS + " tmd WHERE td."
                +  DIRECTORY_COLUMN_NAME + " = '" + directoryName + "'" + " AND td." + KEY_ID
                + " = " + "tmd." + KEY_DIRECTORY_ID;

        Cursor c  = db.rawQuery(selectQuery, null);

        count=c.getCount();

        c.close();

        return count;

    }

    public boolean updateModelDirectoryRelation(long modelId, long directoryId) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_DIRECTORY_ID,directoryId);


        String where =KEY_MODEL_ID + " = ? ";
        String[] whereArgs = new String[]{String.valueOf(modelId)};



        if( db.update(TABLE_MODEL_DIRECTORY_RELATIONS, contentValues, where, whereArgs) > 0){

            return true;
        }

        return false;
    }


    //---------Model,Custumer------------------------------------------/////////

    private long insertModelCustumerRelation(long modelId,long custumerId){



        SQLiteDatabase db = this.getWritableDatabase();


        final ContentValues contentValues=new ContentValues();


        contentValues.put(KEY_MODEL_ID,modelId);
        contentValues.put(KEY_CUSTUMER_ID, custumerId);

        Long id=   db.insert(TABLE_MODEL_CUSTUMER_RELATIONS,null,contentValues);





        return   id;



    }

    private  boolean removeModelCustumerRelation(long custumerId){


        SQLiteDatabase db = this.getReadableDatabase();
        String filter = KEY_CUSTUMER_ID;

        return db.delete(TABLE_MODEL_CUSTUMER_RELATIONS, filter + "=" + String.valueOf(custumerId), null) > 0;


    }

    private boolean removeModelCustumerRelationByModel(long modelId){

        boolean delete=false;

        SQLiteDatabase db = this.getReadableDatabase();
        String where =KEY_MODEL_ID + " = ? ";
        String[] whereArgs = new String[]{String.valueOf(modelId)};


        delete= db.delete(TABLE_MODEL_CUSTUMER_RELATIONS, where, whereArgs) > 0;


        return delete;


    }

    private String getCustumerNameAtModel(long modelId){

        String name=null;


        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_CUSTUMER + " tc, "
                + TABLE_MODEL_CUSTUMER_RELATIONS + " tmd WHERE tmd."
                + KEY_MODEL_ID + " = '" + String.valueOf(modelId) + "'" + " AND tc." + KEY_ID
                + " = " + "tmd." + KEY_CUSTUMER_ID ;

        Cursor c  = db.rawQuery(selectQuery, null);

        while (c.moveToNext()){

            name=c.getString(c.getColumnIndex(CUSTUMER_COLUMN_NAME));

        }
        c.close();

        if(name==null){


            name=mContext.getResources().getString(R.string.importNoAsigned_Cat);
        }

        return name;
    }

    private Integer getModelCountAtCustumer(String custumerName){


        SQLiteDatabase db = this.getReadableDatabase();
        int count=0;


        String selectQuery = "SELECT  * FROM " + TABLE_CUSTUMER + " tc, "
                +  TABLE_MODEL_CUSTUMER_RELATIONS + " tmc WHERE tc."
                +  CUSTUMER_COLUMN_NAME + " = '" + custumerName + "'" + " AND tc." + KEY_ID
                + " = " + "tmc." + KEY_CUSTUMER_ID;

        Cursor c  = db.rawQuery(selectQuery, null);

        count=c.getCount();

        c.close();

        return count;

    }

    public boolean updateModelCustumerRelation(long modelId, long custumerId) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_CUSTUMER_ID,custumerId);


        String where =KEY_MODEL_ID + " = ? ";
        String[] whereArgs = new String[]{String.valueOf(modelId)};



        if( db.update(TABLE_MODEL_CUSTUMER_RELATIONS, contentValues, where, whereArgs) > 0){

            return true;
        }

        return false;
    }

    // Custom Material,Dealer------------------------------------------/////////

    private long insertDealerCustomMaterialRelation(long delaerId,long customMaterialId){



        SQLiteDatabase db = this.getWritableDatabase();


        final ContentValues contentValues=new ContentValues();


        contentValues.put(KEY_DEALER_ID,delaerId);
        contentValues.put(KEY_CUSTOM_MATERIAL_ID, customMaterialId);

        Long id=   db.insert(TABLE_DEALER_CUSTOM_MATERIAL_RELATIONS,null,contentValues);






        return   id;



    }

    private  boolean removeDealerCustomMaterialRelation(long dealerId ){


        SQLiteDatabase db = this.getReadableDatabase();
        String filter = KEY_DEALER_ID;

        return db.delete(TABLE_DEALER_CUSTOM_MATERIAL_RELATIONS, filter + "=" + String.valueOf(dealerId), null) > 0;


    }

    private boolean removeDealerCustomMaterialRelationByCustomMaterial(long customMaterialId){

        boolean delete=false;

        SQLiteDatabase db = this.getReadableDatabase();
        String where =KEY_CUSTOM_MATERIAL_ID + " = ? ";
        String[] whereArgs = new String[]{String.valueOf(customMaterialId)};


        delete= db.delete(TABLE_DEALER_CUSTOM_MATERIAL_RELATIONS, where, whereArgs) > 0;


        return delete;


    }

    private String getDealerNameAtCustomMaterial(long customMaterialId){

        String name=null;


        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_DEALERSHIP + " td, "
                + TABLE_DEALER_CUSTOM_MATERIAL_RELATIONS + " tdcm WHERE tdcm."
                + KEY_CUSTOM_MATERIAL_ID + " = '" + String.valueOf(customMaterialId) + "'" + " AND td." + KEY_ID
                + " = " + "tdcm." + KEY_DEALER_ID ;

        Cursor c  = db.rawQuery(selectQuery, null);

        while (c.moveToNext()){

            name=c.getString(c.getColumnIndex(DEALER_COLUMN_NAME));

        }
        c.close();


        if(name==null){


            name=mContext.getResources().getString(R.string.importNoAsigned_Cat);
        }

        return name;
    }

    public boolean updateDealerCustomMaterialRelation(long dealerId, long custommaterialId) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_DEALER_ID,dealerId);


        String where =KEY_CUSTOM_MATERIAL_ID + " = ? ";
        String[] whereArgs = new String[]{String.valueOf(custommaterialId)};



       if( db.update(TABLE_DEALER_CUSTOM_MATERIAL_RELATIONS, contentValues, where, whereArgs) > 0){

           return true;
       }else{


           insertDealerCustomMaterialRelation(dealerId,custommaterialId);

           return true;

       }

    }

    private Integer getMaterialCountAtDealer(String dealer){


        SQLiteDatabase db = this.getReadableDatabase();
        int count=0;


        String selectQuery = "SELECT  * FROM " + TABLE_DEALERSHIP + " td, "
                +  TABLE_DEALER_CUSTOM_MATERIAL_RELATIONS + " tdcm WHERE td."
                +  DEALER_COLUMN_NAME + " = '" + dealer+ "'" + " AND td." + KEY_ID
                + " = " + "tdcm." + KEY_DEALER_ID;

        Cursor c  = db.rawQuery(selectQuery, null);

        count=c.getCount();

        c.close();

        return count;

    }



    //Material Type,Custom Materail------------------------------------------/////////

    private long insertMaterialTypeCustomMaterialRelation(long typeId,long customMaterialId){



        SQLiteDatabase db = this.getWritableDatabase();


        final ContentValues contentValues=new ContentValues();


        contentValues.put(KEY_MATERIAL_TYPE_ID,typeId);
        contentValues.put(KEY_CUSTOM_MATERIAL_ID, customMaterialId);

        Long id=   db.insert(TABLE_MATERIAL_TYPES_CUSTOM_MATERIAL_RELATIONS,null,contentValues);




        return   id;



    }

    private boolean removeMaterialTypeCustomMaterialRelationByCustomMaterial(long customMaterialId){

        boolean delete=false;

        SQLiteDatabase db = this.getReadableDatabase();
        String where =KEY_CUSTOM_MATERIAL_ID + " = ? ";
        String[] whereArgs = new String[]{String.valueOf(customMaterialId)};

        final String type=getMaterialTypeAtCustomMaterial(customMaterialId);
        final long typeId=getMaterialTypeId(type);

        delete= db.delete(TABLE_MATERIAL_TYPES_CUSTOM_MATERIAL_RELATIONS, where, whereArgs) > 0;

        if(delete){


           if(!isMaterialTypeAssigned(typeId)){

               deleteMaterialType(typeId);



           }

        }


        return delete;


    }

    private boolean removeMaterialTypeCustomMaterialRelationByMaterialType(long materialTypeId){

        boolean update=false;

        SQLiteDatabase db = this.getReadableDatabase();
        String where =KEY_MATERIAL_TYPE_ID + " = ? ";
        String[] whereArgs = new String[]{String.valueOf(materialTypeId)};

        ContentValues contentValues=new ContentValues();

        contentValues.put(KEY_MATERIAL_TYPE_ID,getMaterialTypeId(mContext.getString(R.string.importNoAsigned_Cat)));



        update=  db.update(TABLE_MATERIAL_TYPES_CUSTOM_MATERIAL_RELATIONS,contentValues, where, whereArgs)>0;



        return update;


    }


    private String getMaterialTypeAtCustomMaterial(long customMaterialId){

        String name=null;


        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_MATERIAL_TYPES + " tmt, "
                + TABLE_MATERIAL_TYPES_CUSTOM_MATERIAL_RELATIONS + " tmtcm WHERE tmtcm."
                + KEY_CUSTOM_MATERIAL_ID + " = '" + String.valueOf(customMaterialId) + "'" + " AND tmt." + KEY_ID
                + " = " + "tmtcm." + KEY_MATERIAL_TYPE_ID ;

        Cursor c  = db.rawQuery(selectQuery, null);

        while (c.moveToNext()){

            name=c.getString(c.getColumnIndex(MATERIAL_TYPE_COLUMN_NAME));

        }
        c.close();


        return name;
    }

    public boolean updateMaterialTypeCustomMaterialRelation(long typeId, long custommaterialId) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_MATERIAL_TYPE_ID,typeId);


        String where =KEY_CUSTOM_MATERIAL_ID + " = ? ";
        String[] whereArgs = new String[]{String.valueOf(custommaterialId)};

        final long oldTypeId=getMaterialTypeId(getMaterialTypeAtCustomMaterial(custommaterialId));


        if( db.update(TABLE_MATERIAL_TYPES_CUSTOM_MATERIAL_RELATIONS, contentValues, where, whereArgs) > 0){


            if(!isMaterialTypeAssigned(oldTypeId)){

                deleteMaterialType(oldTypeId);



            }

            return true;
        }else{


            insertMaterialTypeCustomMaterialRelation(typeId,custommaterialId);

            return true;

        }


    }

    private Integer getMaterialCountAtMaterialType(String materialType){


        SQLiteDatabase db = this.getReadableDatabase();
        int count=0;


        String selectQuery = "SELECT  * FROM " + TABLE_MATERIAL_TYPES + " tmt, "
                +  TABLE_MATERIAL_TYPES_CUSTOM_MATERIAL_RELATIONS + " tmtcm WHERE tmt."
                +  MATERIAL_TYPE_COLUMN_NAME + " = '" + materialType + "'" + " AND tmt." + KEY_ID
                + " = " + "tmtcm." + KEY_MATERIAL_TYPE_ID;

        Cursor c  = db.rawQuery(selectQuery, null);

        count=c.getCount();

        c.close();

        return count;

    }

    ////////---------------------------------------------UTILS-----------------------------------------///////////////
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

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

    public static String objectToJson(List<CutNote> values){

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        return gson.toJson(values).toString();
    }

    public static List<CutNote> jsonToObject(String json){

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        return gson.fromJson(json,new TypeToken<ArrayList<CutNote>>() {}.getType());
    }


    private class CategoryNameSortedList{



        private SortedList.Callback<Category> mCallback = new SortedList.Callback<Category>() {

            @Override
            public int compare(Category a1, Category a2) {

                return ALPHABETICAL_COMPARATOR.compare(a1,a2);
            }

            @Override
            public void onInserted(int position, int count) {

            }

            @Override
            public void onMoved(int fromPosition, int toPosition) {


            }

            @Override
            public void onChanged(int position, int count) {

            }

            @Override
            public void onRemoved(int position, int count) {


            }




            @Override
            public boolean areContentsTheSame(Category oldItem, Category newItem) {
                return oldItem.equals(newItem);
            }

            @Override
            public boolean areItemsTheSame(Category item1, Category item2) {

                return (item1.getName().equals(item2.getName()));
            }
        };
        private SortedList categories;
        public CategoryNameSortedList() {
          categories  = new SortedList<>(Category.class,mCallback);
        }


        public void add(Category category){

            categories.add(category);

        }

        public List<Category> getItemList(){

            final List<Category>items=new ArrayList<>();

            for (int i=0;i<categories.size();i++){

                items.add((Category) categories.get(i));
            }
            return items;

        }



    }

    private static final Comparator<Category> ALPHABETICAL_COMPARATOR = new Comparator<Category>() {
        @Override
        public int compare(Category lhs, Category rhs) {
            return (((Category) lhs).getName()).compareTo(((Category) rhs).getName());
        }


    };

}


