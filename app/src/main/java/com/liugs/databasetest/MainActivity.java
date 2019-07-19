package com.liugs.databasetest;

import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.liugs.databasetest.entity.Student;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements DbView, StudentAdapter.ItemOperation {

    private DbPresenter presenter;
    private StudentAdapter adapter;
    private int requestPermissionCount;
    private OperationDialog operationDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        checkPermission();

        presenter = new DbPresenter(this);
        presenter.init(getApplicationContext());
        presenter.search();

        /*AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(R.layout.update_layout).create();
        dialog.show();*/

    }

    private void initView() {
        ListView listView = findViewById(R.id.listView);
        ArrayList<Student> list = new ArrayList<>();
        adapter = new StudentAdapter(list, this);
        listView.setAdapter(adapter);
        adapter.setCallback(this);

        operationDialog = new OperationDialog(MainActivity.this);
    }

    @Override
    protected void onDestroy() {
        presenter.release();
        presenter = null;
        super.onDestroy();
        MainApplication.getRefWatcher(getApplicationContext()).watch(this);
    }

    @Override
    public void refreshListView(Student s) {
        adapter.addStudent(s);
    }

    @Override
    public void refreshListView(ArrayList<Student> students) {
        adapter.setList(students);
    }

    public void add(View view) {
        operationDialog.show();
        operationDialog.setAddCallback(new OperationDialog.AddOperation() {
            @Override
            public void onStudent(Student student) {
                operationDialog.dismiss();
                presenter.insert(student.getName(), student.getAge());
            }
        });
    }

    @Override
    public void onChange(Student student) {
        operationDialog.show();
        operationDialog.setChangeStudent(student);
        operationDialog.setUpdateCallback(new OperationDialog.UpdateOperation() {
            @Override
            public void onStudent(Student student) {
                operationDialog.dismiss();
                presenter.update(student);
            }
        });
    }

    public void refresh(View view) {
        presenter.search();
    }


    @Override
    public void onDelete(Student student) {
        presenter.delete(student);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // 已经有权限，可以做什么呢
        } else {
            checkPermission();
        }
    }

    private void checkPermission() {
        if (!(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            if (requestPermissionCount > 2) {
                Toast.makeText(this, "你必须开启存储权限，才能操作", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            }
        }
    }
}
