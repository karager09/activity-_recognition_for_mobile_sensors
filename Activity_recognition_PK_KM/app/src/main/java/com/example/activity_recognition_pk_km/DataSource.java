package com.example.activity_recognition_pk_km;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DataSource {

    // Database fields
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    private String[] allColumns = { DatabaseHelper.COLUMN_ID,
            DatabaseHelper.COLUMN_X,
            DatabaseHelper.COLUMN_Y,
            DatabaseHelper.COLUMN_Z,
            DatabaseHelper.COLUMN_TIME,
            DatabaseHelper.COLUMN_ACTIVITY};

    public DataSource(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public DatabaseRecord createEntry(double x, double y, double z, String timestamp, String activity) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_X, x);
        values.put(DatabaseHelper.COLUMN_Y, y);
        values.put(DatabaseHelper.COLUMN_Z, z);
        values.put(DatabaseHelper.COLUMN_TIME, timestamp);
        values.put(DatabaseHelper.COLUMN_ACTIVITY, activity);

        long insertId = database.insert(DatabaseHelper.TABLE_ACC, null,
                values);

        Cursor cursor = database.query(DatabaseHelper.TABLE_ACC,
                allColumns, DatabaseHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        DatabaseRecord newDbRecord = cursorToDatabaseRecord(cursor);
        cursor.close();
        return newDbRecord;
    }

    public void deleteGPS(DatabaseRecord databaseRecord) {
        long id = databaseRecord.getId();

        database.delete(DatabaseHelper.TABLE_ACC, DatabaseHelper.COLUMN_ID
                + " = " + id, null);
    }

    public List<DatabaseRecord> getAllGPS() {
        List<DatabaseRecord> databaseRecords = new ArrayList<DatabaseRecord>();

        Cursor cursor = database.query(DatabaseHelper.TABLE_ACC,
                allColumns, null, null, null, null, null);

        if(cursor.moveToFirst()){
            while (!cursor.isAfterLast()) {
                DatabaseRecord databaseRecord = cursorToDatabaseRecord(cursor);
                databaseRecords.add(databaseRecord);
                cursor.moveToNext();
            }
        }
        // Make sure to close the cursor
        cursor.close();
        return databaseRecords;
    }

    private DatabaseRecord cursorToDatabaseRecord(Cursor cursor) {
        DatabaseRecord databaseRecord = null;
        if (cursor != null) {
            databaseRecord = new DatabaseRecord();
            databaseRecord.setId(cursor.getLong(0));
            databaseRecord.setX(cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.COLUMN_X)));
            databaseRecord.setY(cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.COLUMN_Y)));
            databaseRecord.setZ(cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.COLUMN_Z)));
            databaseRecord.setTimestamp(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_TIME)));
            databaseRecord.setActivity(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_ACTIVITY)));
        }
        return databaseRecord;
    }
} 