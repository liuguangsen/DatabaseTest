package com.liugs.databasetest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.liugs.databasetest.entity.Student;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

class DbPresenter {
    private WeakReference<DbView> view;

    private MyDbHelper helper;

    DbPresenter(DbView view) {
        this.view = new WeakReference<>(view);
    }

    void release() {
        if (view != null) {
            view.clear();
            view = null;
        }
        if (helper != null) {
            helper.close();
        }
    }

    void init(Context applicationContext) {
        if (helper == null) {
            helper = new MyDbHelper(applicationContext);
        }
    }

    void insert(int id, String name, int age) {
        ContentValues values = new ContentValues();
        // TODO 如果名字一样和年龄也一样 可以看下 生成的id 是啥样的
        // values.put(Constant.COLUMN_ID,id);
        values.put(Constant.COLUMN_NAME , name + System.currentTimeMillis());
        values.put(Constant.COLUMN_AGE, age);
        SQLiteDatabase writableDatabase = helper.getWritableDatabase();
        long index = writableDatabase.insert(Constant.TABLE_NAME, null, values);
        writableDatabase.close();
        Student s = new Student();
        s.setId(index);
        s.setName(name);
        s.setAge(age);
        DbView dbView = view.get();
        if (dbView != null) {
            dbView.refreshListView(s);
        }
    }

    void search() {
        SQLiteDatabase readableDatabase = helper.getReadableDatabase();
        Cursor query = readableDatabase.query(Constant.TABLE_NAME, null, null, null, null, null, null);
        ArrayList<Student> list = new ArrayList<>();
        Student student;
        while (query.moveToNext()) {
            String name = query.getString(query.getColumnIndex(Constant.COLUMN_NAME));
            int age = query.getInt(query.getColumnIndex(Constant.COLUMN_AGE));
            student = new Student();
            student.setName(name);
            student.setAge(age);
            list.add(student);
        }
        readableDatabase.close();
        query.close();
        DbView dbView = view.get();
        if (dbView != null) {
            dbView.refreshListView(list);
        }
    }

    void update(Student student) {
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query(Constant.TABLE_NAME, null, Constant.COLUMN_ID + "=?", new String[]{student.getId() + ""}, null, null, null);
        if (cursor == null || !cursor.moveToNext()) {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
            return;
        }

        long id = cursor.getLong(cursor.getColumnIndex(Constant.COLUMN_ID));
        ContentValues values = new ContentValues();
        values.put(Constant.COLUMN_ID, student.getId());
        values.put(Constant.COLUMN_NAME, student.getName());
        values.put(Constant.COLUMN_AGE, student.getAge());
        db.update(Constant.TABLE_NAME, values, Constant.COLUMN_ID + "=?", new String[]{id + ""});
        db.close();

        search();
    }

    void delete(Student student) {
        SQLiteDatabase db = helper.getWritableDatabase();
        int delete = db.delete(Constant.TABLE_NAME, Constant.COLUMN_ID + "=?", new String[]{student.getId() + ""});

        search();
    }
}
