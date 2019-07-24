package com.liugs.databasetest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.liugs.databasetest.entity.Student;

import java.util.ArrayList;

public class StudentAdapter extends BaseAdapter {

    private ArrayList<Student> list;
    private Context context;
    private ItemOperation callback;

    public void setCallback(ItemOperation callback) {
        this.callback = callback;
    }

    public StudentAdapter(ArrayList<Student> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list == null ? null : list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_list, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final Student student = list.get(position);
        String data;
        if (DbUtil.isNewDbVersion(MainApplication.getContext())) {
            data = context.getString(R.string.data_new, student.getId(), student.getName(), student.getAge(), student.getIdCard());
        } else {
            data = context.getString(R.string.data, student.getId(), student.getName(), student.getAge());
        }
        holder.dataView.setText(data);
        holder.changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null) {
                    callback.onChange(student);
                }
            }
        });
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null) {
                    callback.onDelete(student);
                }
            }
        });

        return convertView;
    }

    public void addStudent(Student s) {
        if (list != null) {
            list.add(s);
            notifyDataSetChanged();
        }
    }

    public void setList(ArrayList<Student> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    class ViewHolder {
        final TextView dataView;
        final Button changeBtn;
        final Button deleteBtn;

        public ViewHolder(View view) {
            dataView = view.findViewById(R.id.data);
            changeBtn = view.findViewById(R.id.change);
            deleteBtn = view.findViewById(R.id.delete);
        }
    }

    public interface ItemOperation {
        void onChange(Student student);

        void onDelete(Student student);
    }
}
