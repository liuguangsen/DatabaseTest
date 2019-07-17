package com.liugs.databasetest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.liugs.databasetest.entity.Student;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements DbView , StudentAdapter.ItemOperation{

    private DbPresenter presenter;
    private StudentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initListView();
        presenter = new DbPresenter(this);
        presenter.init(getApplicationContext());
    }

    private void initListView() {
        ListView listView = findViewById(R.id.listView);
        ArrayList<Student> list = new ArrayList<>();
        adapter = new StudentAdapter(list,this);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        presenter.release();
        presenter = null;
        super.onDestroy();
    }

    @Override
    public void refreshListView(Student s) {
        adapter.addStudent(s);
    }

    @Override
    public void refreshListView(ArrayList<Student> students) {
        adapter.setList(students);
    }

    @Override
    public void onChange(Student student) {

    }

    @Override
    public void onDelete(Student student) {

    }
}
