package com.example.midexammobile;

public class StudentModel {

    private String studentId;
    private String studentName;
    private String enrolled;

    public StudentModel(String studentId, String studentName,
                        String enrolled) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.enrolled = enrolled;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getEnrolled() {
        return enrolled;
    }

    public void setEnrolled(String enrolled) {
        this.enrolled = enrolled;
    }
}
