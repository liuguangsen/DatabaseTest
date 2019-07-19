package com.liugs.databasetest;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.liugs.databasetest.entity.Student;

class OperationDialog extends Dialog {

    private static final int TYPE_UPDATE = 1;
    private static final int TYPE_ADD = 2;

    private int currentType;
    private Student changeStudent;
    private AppCompatEditText nameEdit;
    private AppCompatEditText ageEdit;
    private UpdateOperation updateOperation;
    private AddOperation addOperation;
    private AppCompatTextView title;

    OperationDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    void setChangeStudent(Student changeStudent) {
        currentType = TYPE_UPDATE;
        this.changeStudent = changeStudent;
        nameEdit.setText(changeStudent.getName());
        nameEdit.setSelection(nameEdit.getText().length());
        ageEdit.setText(String.valueOf(changeStudent.getAge()));
    }

    void setUpdateCallback(UpdateOperation callback) {
        this.updateOperation = callback;
    }

    void setAddCallback(AddOperation addCallback) {
        this.addOperation = addCallback;
        this.title.setText(R.string.add);
        currentType = TYPE_ADD;
    }

    private void init() {
        setContentView(R.layout.update_layout);
        title = findViewById(R.id.dialog_title);
        nameEdit = findViewById(R.id.name);
        ageEdit = findViewById(R.id.age);
        Button sure = findViewById(R.id.sure);
        if (sure != null) {
            sure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Editable nameEdit = OperationDialog.this.nameEdit.getEditableText();
                    Editable ageEdit = OperationDialog.this.ageEdit.getEditableText();
                    String name = nameEdit.toString();
                    String age = ageEdit.toString();
                    if (TextUtils.isEmpty(name) || TextUtils.isEmpty(age)) {
                        Toast.makeText(getContext(), R.string.hint, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (currentType == TYPE_UPDATE) {
                        changeStudent.setName(name);
                        changeStudent.setAge(Integer.valueOf(age));
                        updateOperation.onStudent(changeStudent);
                    } else if (currentType == TYPE_ADD) {
                        Student student = new Student();
                        student.setName(name);
                        student.setAge(Integer.valueOf(age));
                        addOperation.onStudent(student);
                    }
                }
            });
        }
    }


    public interface UpdateOperation {
        void onStudent(Student student);
    }

    public interface AddOperation {
        void onStudent(Student student);
    }
}
