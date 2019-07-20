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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.liugs.databasetest.entity.Student;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements DbView, StudentAdapter.ItemOperation {

    private DbPresenter presenter;
    private StudentAdapter adapter;
    private int requestPermissionCount;
    private OperationDialog operationDialog;
    private EditText findEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        checkPermission();

        presenter = new DbPresenter(this);
        presenter.init(getApplicationContext());
        presenter.search();
    }

    private void initView() {
        findEdit = findViewById(R.id.find_edit);

        ListView listView = findViewById(R.id.listView);
        ArrayList<Student> list = new ArrayList<>();
        adapter = new StudentAdapter(list, this);
        listView.setAdapter(adapter);
        adapter.setCallback(this);

        operationDialog = new OperationDialog(MainActivity.this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            checkPermission();
        }
    }

    @Override
    protected void onDestroy() {
        presenter.release();
        presenter = null;
        super.onDestroy();
        MainApplication.getRefWatcher(getApplicationContext()).watch(this);
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
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

    public void find(View view) {
        String name = findEdit.getEditableText().toString();
        presenter.find(name);
    }



    private void checkPermission() {
        if (!(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            if (requestPermissionCount > 2) {
                Toast.makeText(this, getString(R.string.save_permission_hint), Toast.LENGTH_SHORT).show();
            } else {
                ++requestPermissionCount;
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            }
        }
    }
}
