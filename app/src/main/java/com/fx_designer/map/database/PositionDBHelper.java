package com.fx_designer.map.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Chamath Abeysinghe on 9/9/2016.
 */

public class PositionDBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION=1;
    public static final String DATABASE_NAME="Rates.db";


    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + PositionContract.Position.TABLE_NAME + " (" +
                    PositionContract.Position._ID + " INTEGER PRIMARY KEY," +
                    PositionContract.Position.COLUMN_NAME_POSITION_NAME + " TEXT NOT NULL" + COMMA_SEP +
                    PositionContract.Position.COLUMN_NAME_LATITUDE + " REAL NOT NULL" + COMMA_SEP +
                    PositionContract.Position.COLUMN_NAME_LONGITUDE + " REAL NOT NULL" + COMMA_SEP +
                    PositionContract.Position.COLUMN_NAME_ADDRESS + " TEXT" + COMMA_SEP +
                    PositionContract.Position.COLUMN_NAME_PHONE_NUMBER + " TEXT" + " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + PositionContract.Position.TABLE_NAME;




    public PositionDBHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);
    }



}
