package com.ysd.map.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;

/**
 * Created by Administrator on 2016/1/26.
 */
public class TrackDbAdaper extends DbAdapter {
    private static final String TAG="TrackDbAdaper";
    public static final String TABLE_NAME = "tracks";
    private static final String ID = "_id";
    private static final String KEY_ROWID = "_id";
    private static final String NAME = "name";
    private static final String DESC = "desc";
    private static final String DIST = "distance";
    private static final String TRACKEDTIME = "tracked_time";
    private static final String LOCATE_COUNT = "locats_count";
    private static final String UPDATED = "update_at";
    private static final String AVGSPEED = "avg_speed";
    private static final String MAXSPEED = "max_speed";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    private final Context mCtx;

    public TrackDbAdaper(Context mCtx) {
        this.mCtx = mCtx;
    }
    public TrackDbAdaper open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }
    public void close(){
        mDbHelper.close();
    }
}
