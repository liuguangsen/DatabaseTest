package com.liugs.databasetest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.liugs.databasetest.entity.Student;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class DbPresenter {
    private WeakReference<DbView> view;

    private MyDbHelper helper;

    public DbPresenter(DbView view) {
        this.view = new WeakReference<>(view);
    }

    public void release() {
        if (view != null) {
            view.clear();
            view = null;
        }
        if (helper != null){
            helper.close();
        }
    }

    public void init(Context applicationContext) {
        if (helper == null) {
            helper = new MyDbHelper(applicationContext);
        }
        insert("第一个", 1);
    }

    public void insert(String name, int age) {
        ContentValues values = new ContentValues();
        values.put(Constant.COLUMN_NAME, name);
        values.put(Constant.COLUMN_AGE, age);
        SQLiteDatabase writableDatabase = helper.getWritableDatabase();
        long id = writableDatabase.insert(Constant.TABLE_NAME, null, values);
        writableDatabase.close();
        Student s = new Student();
        s.setId(id);
        s.setName(name);
        s.setAge(age);
        DbView dbView = view.get();
        if (dbView != null) {
            dbView.refreshListView(s);
        }
    }

    public void search(){
        SQLiteDatabase readableDatabase = helper.getReadableDatabase();
        Cursor query = readableDatabase.query(Constant.TABLE_NAME, null, null, null, null, null, null);
        ArrayList<Student> list = new ArrayList<>();
        Student student;
        while (query.moveToNext()){
            String name = query.getString(query.getColumnIndex(Constant.TABLE_NAME));
            int age = query.getInt(query.getColumnIndex(Constant.COLUMN_AGE));
            student = new Student();
            student.setName(name);
            student.setAge(age);
            list.add(student);
        }
        readableDatabase.close();
        query.close();
        DbView dbView = view.get();
        if (dbView != null){
            dbView.refreshListView(list);
        }
    }
}
