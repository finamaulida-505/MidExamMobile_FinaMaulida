package com.example.midexammobile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText editTextName, editTextID;
    private CheckBox checkBoxHasEnrolled;
    private Button btnAdd, btnView;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextName = findViewById(R.id.editTextName);
        editTextID = findViewById(R.id.editTextID);
        checkBoxHasEnrolled = findViewById(R.id.checkBoxHasEnrolled);

        btnAdd = findViewById(R.id.btnAdd);
        btnView = findViewById(R.id.btnView);

        dbHelper = new DBHelper(this);

        btnAdd.setOnClickListener(v -> addStudent());

        btnView.setOnClickListener(v -> {
            Intent intent = new Intent(
                    MainActivity.this, ViewStudent.class);
            startActivity(intent);
        });
    }

    private void addStudent() {
        String name = editTextName.getText()
                .toString().trim();

        String studentId = editTextID.getText()
                .toString().trim();

        if (name.isEmpty() || studentId.isEmpty()) {
            Toast.makeText(this,
                    "Please enter student name and ID",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (!studentId.matches("\\d{12}")) {
            Toast.makeText(this,
                    "Student ID must contain 12 digits",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        String enrolled = checkBoxHasEnrolled.isChecked()
                ? "Yes" : "No";

        StudentModel student = new StudentModel(
                studentId, name, enrolled);

        boolean success = dbHelper.addStudent(student);

        if (success) {
            Toast.makeText(this,
                    "Student added successfully",
                    Toast.LENGTH_SHORT).show();

            editTextName.setText("");
            editTextID.setText("");
            checkBoxHasEnrolled.setChecked(false);

        } else {
            Toast.makeText(this,
                    "Failed to add student. ID may already exist.",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
