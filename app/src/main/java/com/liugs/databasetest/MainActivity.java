package com.liugs.databasetest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.liugs.databasetest.entity.Student;

public class MainActivity extends AppCompatActivity implements DbView {

    private DbPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presenter = new DbPresenter(this);
        presenter.init(getApplicationContext());
    }

    @Override
    protected void onDestroy() {
        presenter.release();
        presenter = null;
        super.onDestroy();
    }

    @Override
    public void refreshListView(Student s) {
        Toast.makeText(MainActivity.this, " " + s.getId(), Toast.LENGTH_LONG).show();
    }
}
