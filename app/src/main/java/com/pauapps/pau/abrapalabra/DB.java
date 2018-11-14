package com.pauapps.pau.abrapalabra;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Pau on 22/6/2017.
 */

public class DB extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "pictionary.db";
    private static final int DATABASE_VERSION = 6;

    //Table words esp
    private static final String ESP_TABLE_NAME = "palabras";
    private static final String ESP_COLUMN_ID = "id_palabra";
    private static final String ESP_COLUMN_PALABRA = "palabra";

    public DB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        SQLiteDatabase db = this.getReadableDatabase();

        if (numberOfRows() < 3) {
            insertPalabras(context, db);
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table " + ESP_TABLE_NAME +
                        "(" + ESP_COLUMN_ID + " integer primary key autoincrement, " + ESP_COLUMN_PALABRA + " text)"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + ESP_TABLE_NAME);
        onCreate(db);
    }

    private void insertPalabras(Context context, SQLiteDatabase db) {
        String mCSVfile = "palabras.csv";
        AssetManager manager = context.getAssets();
        InputStream inStream = null;
        try {
            inStream = manager.open(mCSVfile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert inStream != null;
        BufferedReader buffer = new BufferedReader(new InputStreamReader(inStream));
        String line;
        db.beginTransaction();
        try {
            while ((line = buffer.readLine()) != null) {
                String[] colums = line.split(",");
                if (colums.length != 1) {
                    Log.d("CSVParser", "Skipping Bad CSV Row");
                    continue;
                }
                ContentValues cv = new ContentValues(0);
                cv.put(ESP_COLUMN_PALABRA, colums[0].trim());
                db.insert(ESP_TABLE_NAME, null, cv);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public ArrayList<String> getPalabras() {
        ArrayList<String> array_list = new ArrayList<>();

        String query = "Select * from " + ESP_TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(query, null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            array_list.add(res.getString(res.getColumnIndex(ESP_COLUMN_PALABRA)));
            res.moveToNext();
        }
        res.close();

        return array_list;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getWritableDatabase();
        return (int) DatabaseUtils.queryNumEntries(db, ESP_TABLE_NAME);
    }

}
