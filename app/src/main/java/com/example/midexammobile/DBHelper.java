package com.example.midexammobile;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "student_db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_STUDENT = "students";
    private static final String COLUMN_ID = "student_id";
    private static final String COLUMN_NAME = "student_name";
    private static final String COLUMN_ENROLLED = "enrolled";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_STUDENT + " ("
                + COLUMN_ID + " TEXT PRIMARY KEY, "
                + COLUMN_NAME + " TEXT NOT NULL, "
                + COLUMN_ENROLLED + " TEXT NOT NULL)";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,
                          int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENT);
        onCreate(db);
    }

    // Add student
    public boolean addStudent(StudentModel student) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, student.getStudentId());
        values.put(COLUMN_NAME, student.getStudentName());
        values.put(COLUMN_ENROLLED, student.getEnrolled());

        long result = db.insert(TABLE_STUDENT, null, values);
        db.close();

        return result != -1;
    }

    // Get all students
    public List<StudentModel> getStudentList() {
        List<StudentModel> studentList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_STUDENT, null);

        if (cursor.moveToFirst()) {
            do {
                String studentId = cursor.getString(
                        cursor.getColumnIndexOrThrow(COLUMN_ID));

                String studentName = cursor.getString(
                        cursor.getColumnIndexOrThrow(COLUMN_NAME));

                String enrolled = cursor.getString(
                        cursor.getColumnIndexOrThrow(COLUMN_ENROLLED));

                studentList.add(
                        new StudentModel(studentId, studentName, enrolled));

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return studentList;
    }

    // Update student ID, name, and enrollment status
    public boolean updateStudent(String oldStudentId,
                                 StudentModel student) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, student.getStudentId());
        values.put(COLUMN_NAME, student.getStudentName());
        values.put(COLUMN_ENROLLED, student.getEnrolled());

        int result = db.update(
                TABLE_STUDENT,
                values,
                COLUMN_ID + " = ?",
                new String[]{oldStudentId});

        db.close();

        return result > 0;
    }

    // Delete student
    public boolean deleteStudent(String studentId) {
        SQLiteDatabase db = this.getWritableDatabase();

        int result = db.delete(
                TABLE_STUDENT,
                COLUMN_ID + " = ?",
                new String[]{studentId});

        db.close();

        return result > 0;
    }
}
