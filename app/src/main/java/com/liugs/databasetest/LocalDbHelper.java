package com.liugs.databasetest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.liugs.databasetest.Constant.DB_NAME;
import static com.liugs.databasetest.Constant.SQL_CREATE_TABLE_V2;
import static com.liugs.databasetest.Constant.VERSION;
import static com.liugs.databasetest.Constant.VERSION_NEW;

public class LocalDbHelper extends SQLiteOpenHelper {
    public LocalDbHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
        DbUtil.saveDbVersion(context, VERSION);
    }

    public LocalDbHelper(Context context, int version) {
        super(context, DB_NAME, null, version);
        DbUtil.saveDbVersion(context, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_V2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion == VERSION && newVersion == VERSION_NEW) {
            db.execSQL("ALTER TABLE " + Constant.TABLE_NAME + " ADD COLUMN idCard INTEGER DEFAULT 0");
        }
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
