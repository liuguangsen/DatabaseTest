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

    private LocalDbHelper helper;

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
            helper = new LocalDbHelper(applicationContext);
        }
    }

    void insert(Student student) {
        ContentValues values = new ContentValues();
        String name = student.getName();
        values.put(Constant.COLUMN_NAME, name);
        int age = student.getAge();
        values.put(Constant.COLUMN_AGE, age);
        int randomIdCard = 0;
        boolean newDbVersion = DbUtil.isNewDbVersion(MainApplication.getContext());
        if (newDbVersion) {
            values.put(Constant.COLUMN_ID_CARD, student.getIdCard());
        }
        SQLiteDatabase writableDatabase = helper.getWritableDatabase();
        long index = writableDatabase.insert(Constant.TABLE_NAME, null, values);
        writableDatabase.close();
        Student s = new Student();
        s.setId(index);
        s.setName(name);
        s.setAge(age);
        if (newDbVersion) {
            s.setIdCard(randomIdCard);
        }
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
            long id = query.getLong(query.getColumnIndex(Constant.COLUMN_ID));
            boolean newDbVersion = DbUtil.isNewDbVersion(MainApplication.getContext());
            int idCard = 0;
            if (newDbVersion) {
                idCard = query.getInt(query.getColumnIndex(Constant.COLUMN_ID_CARD));
            }
            student = new Student();
            student.setName(name);
            student.setAge(age);
            student.setId(id);
            if (newDbVersion) {
                student.setIdCard(idCard);
            }
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
        if (DbUtil.isNewDbVersion(MainApplication.getContext())){
            values.put(Constant.COLUMN_ID_CARD,student.getIdCard());
        }
        db.update(Constant.TABLE_NAME, values, Constant.COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        db.close();

        search();
    }

    void delete(Student student) {
        SQLiteDatabase db = helper.getWritableDatabase();
        int delete = db.delete(Constant.TABLE_NAME, Constant.COLUMN_ID + "=?", new String[]{student.getId() + ""});
        db.close();
        if (delete == 1) {
            db.close();
            search();
        } else {
            view.get().showToast("删除失败");
        }
    }

    void find(String nameParam) {
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query(Constant.TABLE_NAME, null, Constant.COLUMN_NAME + " LIKE '%" + nameParam + "%'", null, null, null, null);
        ArrayList<Student> students = new ArrayList<>();
        while (cursor.moveToNext()) {
            int nameIndex = cursor.getColumnIndex(Constant.COLUMN_NAME);
            int idIndex = cursor.getColumnIndex(Constant.COLUMN_ID);
            long id = cursor.getLong(idIndex);
            int ageIndex = cursor.getColumnIndex(Constant.COLUMN_AGE);
            int age = cursor.getInt(ageIndex);
            boolean newDbVersion = DbUtil.isNewDbVersion(MainApplication.getContext());
            int idCard = 0;
            if (newDbVersion) {
                idCard = cursor.getInt(cursor.getColumnIndex(Constant.COLUMN_ID_CARD));
            }
            String name = cursor.getString(nameIndex);
            Student student = new Student();
            student.setId(id);
            student.setName(name);
            student.setAge(age);
            if (newDbVersion) {
                student.setIdCard(idCard);
            }
            students.add(student);
        }
        cursor.close();
        db.close();
        DbView dbView = view.get();
        if (dbView != null) {
            dbView.refreshListView(students);
        }
    }

    void Upgrade(Context context) {
        helper = new LocalDbHelper(context, Constant.VERSION_NEW);
        helper.getWritableDatabase();
        helper.close();
        view.get().showToast("升级数据库成功");
    }
}
