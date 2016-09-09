package com.fx_designer.map.database;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Chamath Abeysinghe on 9/9/2016.
 */
public class PositionContract {

    private PositionContract(){

    }

    //general detabase Uri s
    public static final String CONTENT_AUTHORITY="com.fx_designer.map";
    public static final String PATH_MOVIE="rate";
    public static final Uri BASE_CONTENT_URI=Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final Uri CONTENT_URI=BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();


    //data table for positions
    public static class Position implements BaseColumns {
        public static final String TABLE_NAME = "positions";
        public static final String COLUMN_NAME_POSITION_NAME = "name";
        public static final String COLUMN_NAME_LONGITUDE = "long";
        public static final String COLUMN_NAME_LATITUDE = "lat";
        public static final String COLUMN_NAME_ADDRESS = "address";
        public static final String COLUMN_NAME_PHONE_NUMBER = "phone";

    }


}
