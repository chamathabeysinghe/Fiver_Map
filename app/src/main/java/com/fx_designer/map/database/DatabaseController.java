package com.fx_designer.map.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.fx_designer.map.models.Position;

import java.util.ArrayList;

/**
 * Created by Chamath Abeysinghe on 9/9/2016.
 */
public class DatabaseController {

    private Context context;
    private PositionDBHelper dbHelper;
    public DatabaseController(Context context) {
        this.context = context;
        dbHelper=new PositionDBHelper(context);
    }

    public void insertPosition(double lat,double longitude,String name,String address,String telephone){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(PositionContract.Position.COLUMN_NAME_POSITION_NAME,name);
        values.put(PositionContract.Position.COLUMN_NAME_LATITUDE,lat);
        values.put(PositionContract.Position.COLUMN_NAME_LONGITUDE,longitude);
        values.put(PositionContract.Position.COLUMN_NAME_ADDRESS,address);
        values.put(PositionContract.Position.COLUMN_NAME_PHONE_NUMBER,telephone);
        db.insert(PositionContract.Position.TABLE_NAME,null,values);
    }

    public ArrayList<Position> readFromDatabase(String name){
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        String projection[]={PositionContract.Position.COLUMN_NAME_POSITION_NAME, PositionContract.Position.COLUMN_NAME_LONGITUDE, PositionContract.Position.COLUMN_NAME_LATITUDE
        , PositionContract.Position.COLUMN_NAME_ADDRESS, PositionContract.Position.COLUMN_NAME_PHONE_NUMBER};
        String selection= PositionContract.Position.COLUMN_NAME_POSITION_NAME+" = ";
        String selectionArgs[]={name};
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
            String readName= cursor.getString(cursor.getColumnIndex(PositionContract.Position.COLUMN_NAME_POSITION_NAME));
            String address= cursor.getString(cursor.getColumnIndex(PositionContract.Position.COLUMN_NAME_ADDRESS));
            String number= cursor.getString(cursor.getColumnIndex(PositionContract.Position.COLUMN_NAME_PHONE_NUMBER));
            double longitude=cursor.getDouble(cursor.getColumnIndex(PositionContract.Position.COLUMN_NAME_LONGITUDE));
            double lat=cursor.getDouble(cursor.getColumnIndex(PositionContract.Position.COLUMN_NAME_LATITUDE));

            Position position=new Position(lat,longitude,readName,address,number);
            positions.add(position);
        }

        return positions;
    }



}
