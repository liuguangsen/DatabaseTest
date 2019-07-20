package com.liugs.databasetest;

import com.liugs.databasetest.entity.Student;

import java.util.ArrayList;

public interface DbView {

    void showToast(String message);

    void refreshListView(Student s);

    void refreshListView(ArrayList<Student> students);
}
