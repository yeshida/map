package com.ysd.map.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/1/26.
 */
public class DbAdapter {
    private static final String TAG = "DbAdapter";
    private static final String DATABASE_NAME = "iTracks.db";
    private static final int DATABASE_VERSION = 1;
    public class DatabaseHelper extends SQLiteOpenHelper{
        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String tracks_sql = "CREATE TABLE "+TrackDbAdaper.TABLE_NAME+
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
