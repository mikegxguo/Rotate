package com.mitac.rotate;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class StuDBHelper extends SQLiteOpenHelper {

    private static final String TAG = "TestSQLite";
    public static final int VERSION = 1;

    public StuDBHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public void onCreate(SQLiteDatabase db) {
        String sql = "create table stu_table(id int,sname varchar(20),sage int,ssex varchar(10))";
        Log.i(TAG, "create Database------------->");
        db.execSQL(sql);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "update Database------------->");
        Log.i(TAG, "Old version: "+oldVersion);
        Log.i(TAG, "New version: "+newVersion);
    }
}
