package com.liugs.databasetest;

import android.content.Context;
import android.support.v7.app.AppCompatDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.liugs.databasetest.entity.Student;

class OperationDialog extends AppCompatDialog {

    private Student changeStudent;
    private EditText nameEdit;
    private EditText ageEdit;
    private UpdateOperation callback;

    OperationDialog(Context context) {
        super(context);
        init();
    }

    void setChangeStudent(Student changeStudent) {
        this.changeStudent = changeStudent;
        nameEdit.setText(changeStudent.getName());
        nameEdit.setSelection(nameEdit.getText().length());
        ageEdit.setText(String.valueOf(changeStudent.getAge()));
    }

    void setCallback(UpdateOperation callback) {
        this.callback = callback;
    }

    private void init() {
        setContentView(R.layout.update_layout);
        nameEdit = findViewById(R.id.name);
        ageEdit = findViewById(R.id.age);
        Button sure = findViewById(R.id.sure);
        if (sure != null) {
            sure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeStudent.setName(nameEdit.getEditableText().toString());
                    changeStudent.setAge(Integer.valueOf(ageEdit.getEditableText().toString()));
                    callback.onStudent(changeStudent);
                }
            });
        }
    }

    public interface UpdateOperation {
        void onStudent(Student student);
    }
}
