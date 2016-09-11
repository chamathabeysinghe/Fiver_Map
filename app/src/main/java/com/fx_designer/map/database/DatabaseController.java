package com.fx_designer.map.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.fx_designer.map.models.Position;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Chamath Abeysinghe on 9/9/2016.
 */
public class DatabaseController {

    private Context context;
    private PositionDBHelper dbHelper;
    private static DatabaseController databaseController;
    private DatabaseController(Context context) {
        this.context = context;
        dbHelper=new PositionDBHelper(context);
    }

    public static DatabaseController getInstance(Context c){
        if(databaseController==null){
            databaseController=new DatabaseController(c);
        }
        return databaseController;
    }

    /**
     * Insert a new position to the database
     * @param lat
     * @param longitude
     * @param name
     * @param address
     * @param telephone
     */
    public void insertPosition(double lat,double longitude,String name,String category,String address,String telephone){
        SQLiteDatabase db=dbHelper.getWritableDatabase();

        ContentValues values=new ContentValues();
        values.put(PositionContract.Position.COLUMN_NAME_POSITION_NAME,name);
        values.put(PositionContract.Position.COLUMN_NAME_POSITION_CATEGORY,category);
        values.put(PositionContract.Position.COLUMN_NAME_LATITUDE,lat);
        values.put(PositionContract.Position.COLUMN_NAME_LONGITUDE,longitude);
        values.put(PositionContract.Position.COLUMN_NAME_ADDRESS,address);
        values.put(PositionContract.Position.COLUMN_NAME_PHONE_NUMBER,telephone);
        long rowId=db.insert(PositionContract.Position.TABLE_NAME,null,values);
    }

    /**
     * search database for a keyweord
     * @param category
     * @return list of Positions with keyword as name
     */
    public ArrayList<Position> readFromDatabase(String category){

        SQLiteDatabase db=dbHelper.getReadableDatabase();

        String projection[]={PositionContract.Position.COLUMN_NAME_POSITION_NAME, PositionContract.Position.COLUMN_NAME_POSITION_CATEGORY, PositionContract.Position.COLUMN_NAME_LONGITUDE, PositionContract.Position.COLUMN_NAME_LATITUDE
        , PositionContract.Position.COLUMN_NAME_ADDRESS, PositionContract.Position.COLUMN_NAME_PHONE_NUMBER};

        String selection= PositionContract.Position.COLUMN_NAME_POSITION_CATEGORY+" = ? ";
        String selectionArgs[]={category};

        Cursor cursor=db.query(
                PositionContract.Position.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        ArrayList<Position> positions=new ArrayList<>();

        while(cursor.moveToNext()){
            Log.e("Reading","Read a one");
            String readName= cursor.getString(cursor.getColumnIndex(PositionContract.Position.COLUMN_NAME_POSITION_NAME));
            String readCategory=cursor.getString(cursor.getColumnIndex(PositionContract.Position.COLUMN_NAME_POSITION_CATEGORY));
            String address= cursor.getString(cursor.getColumnIndex(PositionContract.Position.COLUMN_NAME_ADDRESS));
            String number= cursor.getString(cursor.getColumnIndex(PositionContract.Position.COLUMN_NAME_PHONE_NUMBER));
            double longitude=cursor.getDouble(cursor.getColumnIndex(PositionContract.Position.COLUMN_NAME_LONGITUDE));
            double lat=cursor.getDouble(cursor.getColumnIndex(PositionContract.Position.COLUMN_NAME_LATITUDE));
            Position position=new Position(lat,longitude,readName,address,number,readCategory);
            Log.e("Reading",readName);
            positions.add(position);
        }
        return positions;
    }



}
