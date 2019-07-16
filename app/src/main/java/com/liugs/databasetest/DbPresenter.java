package com.liugs.databasetest;

import android.content.ContentValues;
import android.content.Context;

import com.liugs.databasetest.entity.Student;

import java.lang.ref.WeakReference;

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
        long id = helper.getWritableDatabase().insert(Constant.TABLE_NAME, null, values);
        Student s = new Student();
        s.setId(id);
        s.setName(name);
        s.setAge(age);
        DbView dbView = view.get();
        if (dbView != null) {
            dbView.refreshListView(s);
        }
    }
}
