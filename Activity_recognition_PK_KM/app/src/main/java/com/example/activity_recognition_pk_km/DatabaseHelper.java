package com.example.activity_recognition_pk_km;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String TABLE_ACC = "accelerometer_data";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_X = "x";
    public static final String COLUMN_Y = "y";
    public static final String COLUMN_Z = "z";
    public static final String COLUMN_TIME = "timestamp";
    public static final String COLUMN_ACTIVITY = "activity";

    public static final String DATABASE_PREFIX = "";
    public  static final String DATABASE_NAME = "accelerometer_data.db";


    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_ACC + "(" + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_X + " double not null, "
            + COLUMN_Y + " double not null, "
            + COLUMN_Z + " double not null, "
            + COLUMN_TIME + " text not null, "
            + COLUMN_ACTIVITY + " text not null);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_PREFIX + DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DatabaseHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACC);
        onCreate(db);
    }

}