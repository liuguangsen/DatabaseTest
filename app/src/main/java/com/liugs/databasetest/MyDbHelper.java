package com.liugs.databasetest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.liugs.databasetest.Constant.SQL_CREATE_TABLE_V2;
import static com.liugs.databasetest.Constant.DB_NAME;
import static com.liugs.databasetest.Constant.VERSION;

public class MyDbHelper extends SQLiteOpenHelper {
    public MyDbHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_V2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
