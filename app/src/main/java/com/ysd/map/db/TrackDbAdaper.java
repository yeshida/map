package com.ysd.map.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.SQLException;
import java.util.Calendar;

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
    private static final String CREATED = "created_at";
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
    public Cursor getTrack(long rowId) throws SQLException {
        Cursor mCursor = mDb.query(true, TABLE_NAME, new String[]{KEY_ROWID,NAME,DESC,CREATED}, KEY_ROWID + "=" + rowId, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    //增加
    public long createTrack(String name, String desc) {
        Log.d(TAG, "createTrack");
        ContentValues initialValues = new ContentValues();
        initialValues.put(NAME,name);
        initialValues.put(DESC,desc);
        Calendar calendar = Calendar.getInstance();
        String created = calendar.get(Calendar.YEAR) + "-" + calendar.get(Calendar.MONTH) + "-" + calendar.get(Calendar.DAY_OF_MONTH) + " " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND);
        initialValues.put(CREATED,created);
        initialValues.put(UPDATED,created);
        return mDb.insert(TABLE_NAME, null, initialValues);
    }
    //删除
    public boolean deleteTrack(long rowId) {
        return mDb.delete(TABLE_NAME, KEY_ROWID + "=" + rowId, null) > 0;
    }

    //修改
    public boolean updateTrack(long rowId, String name, String desc) {
        ContentValues args = new ContentValues();
        args.put(NAME,name);
        args.put(DESC, desc);
        Calendar calendar = Calendar.getInstance();
        String updated = calendar.get(Calendar.YEAR) + "-" + calendar.get(Calendar.MONTH) + "-" + calendar.get(Calendar.DAY_OF_MONTH) + " " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND);
        args.put(UPDATED,updated);
        return mDb.update(TABLE_NAME, args, KEY_ROWID + "=" + rowId, null) > 0;
    }
}

