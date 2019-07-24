package com.liugs.databasetest;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Random;

import static com.liugs.databasetest.Constant.KEY_DB_VERSION;
import static com.liugs.databasetest.Constant.SP_NAME;
import static com.liugs.databasetest.Constant.VERSION;
import static com.liugs.databasetest.Constant.VERSION_NEW;

public class DbUtil {

    public static void saveDbVersion(Context context, int version) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putInt(KEY_DB_VERSION, version);
        edit.apply();
    }

    public static boolean isNewDbVersion(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        int anInt = sharedPreferences.getInt(KEY_DB_VERSION, VERSION);
        return anInt == VERSION_NEW;
    }
}
