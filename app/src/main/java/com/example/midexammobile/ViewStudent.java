
package com.example.midexammobile;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ViewStudent extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DBHelper dbHelper;
    private StudentAdapter studentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_student);

        recyclerView = findViewById(R.id.recyclerViewStudents);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new DBHelper(this);

        loadStudents();
    }

    private void loadStudents() {
        List<StudentModel> studentList = dbHelper.getStudentList();

        studentAdapter = new StudentAdapter(
                this, studentList, dbHelper);

        recyclerView.setAdapter(studentAdapter);

        if (studentList.isEmpty()) {
            Toast.makeText(this,
                    "No student data found",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (dbHelper != null && recyclerView != null) {
            loadStudents();
        }
    }
}
