package com.example.midexammobile;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StudentAdapter extends
        RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {

    private final Context context;
    private final List<StudentModel> studentList;
    private final DBHelper dbHelper;

    public StudentAdapter(Context context, List<StudentModel> studentList,
                          DBHelper dbHelper) {
        this.context = context;
        this.studentList = studentList;
        this.dbHelper = dbHelper;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.student_item_list, parent, false);

        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull StudentViewHolder holder, int position) {

        StudentModel student = studentList.get(position);

        holder.textViewID.setText("Student ID: " + student.getStudentId());
        holder.textViewName.setText(student.getStudentName());
        holder.textViewEnrolled.setText(
                "Has Enrolled: " +
                        (student.getEnrolled().equalsIgnoreCase("Yes")
                                ? "Yes" : "No"));

        showViewMode(holder);

        holder.buttonEdit.setOnClickListener(v -> {
            holder.editTextID.setText(student.getStudentId());
            holder.editTextName.setText(student.getStudentName());

            holder.checkBoxEditEnrolled.setChecked(
                    student.getEnrolled().equalsIgnoreCase("Yes"));

            holder.textViewID.setVisibility(View.GONE);
            holder.textViewName.setVisibility(View.GONE);
            holder.textViewEnrolled.setVisibility(View.GONE);

            holder.editTextID.setVisibility(View.VISIBLE);
            holder.editTextName.setVisibility(View.VISIBLE);
            holder.checkBoxEditEnrolled.setVisibility(View.VISIBLE);

            holder.layoutViewButtons.setVisibility(View.GONE);
            holder.layoutEditButtons.setVisibility(View.VISIBLE);
        });

        holder.buttonCancel.setOnClickListener(v -> showViewMode(holder));

        holder.buttonSave.setOnClickListener(v -> {
            String oldId = student.getStudentId();
            String newId = holder.editTextID.getText()
                    .toString().trim();
            String name = holder.editTextName.getText()
                    .toString().trim();

            String enrolled =
                    holder.checkBoxEditEnrolled.isChecked()
                            ? "Yes" : "No";

            if (newId.isEmpty() || name.isEmpty()) {
                Toast.makeText(context,
                        "Please enter Student ID and Name",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            if (!newId.matches("\\d{12}")) {
                Toast.makeText(context,
                        "Student ID must contain 12 digits",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            StudentModel updatedStudent =
                    new StudentModel(newId, name, enrolled);

            boolean success =
                    dbHelper.updateStudent(oldId, updatedStudent);

            if (success) {
                student.setStudentId(newId);
                student.setStudentName(name);
                student.setEnrolled(enrolled);

                showViewMode(holder);
                notifyItemChanged(holder.getBindingAdapterPosition());

                Toast.makeText(context,
                        "Student updated successfully",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context,
                        "Update failed. Student ID may already exist.",
                        Toast.LENGTH_LONG).show();
            }
        });

        holder.buttonDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Delete Student")
                    .setMessage("Are you sure you want to delete this student?")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        int currentPosition =
                                holder.getBindingAdapterPosition();

                        if (currentPosition == RecyclerView.NO_POSITION) {
                            return;
                        }

                        if (dbHelper.deleteStudent(
                                student.getStudentId())) {
                            studentList.remove(currentPosition);
                            notifyItemRemoved(currentPosition);

                            Toast.makeText(context,
                                    "Student deleted successfully",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context,
                                    "Failed to delete student",
                                    Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });
    }

    private void showViewMode(StudentViewHolder holder) {
        holder.textViewID.setVisibility(View.VISIBLE);
        holder.textViewName.setVisibility(View.VISIBLE);
        holder.textViewEnrolled.setVisibility(View.VISIBLE);

        holder.editTextID.setVisibility(View.GONE);
        holder.editTextName.setVisibility(View.GONE);
        holder.checkBoxEditEnrolled.setVisibility(View.GONE);

        holder.layoutViewButtons.setVisibility(View.VISIBLE);
        holder.layoutEditButtons.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    static class StudentViewHolder extends RecyclerView.ViewHolder {

        TextView textViewID, textViewName, textViewEnrolled;
        EditText editTextID, editTextName;
        CheckBox checkBoxEditEnrolled;
        Button buttonEdit, buttonDelete, buttonSave, buttonCancel;
        LinearLayout layoutViewButtons, layoutEditButtons;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewID = itemView.findViewById(R.id.textViewID);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewEnrolled =
                    itemView.findViewById(R.id.textViewEnrolled);

            editTextID = itemView.findViewById(R.id.editTextID);
            editTextName = itemView.findViewById(R.id.editTextName);
            checkBoxEditEnrolled =
                    itemView.findViewById(R.id.checkBoxEditEnrolled);

            buttonEdit = itemView.findViewById(R.id.buttonEdit);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
            buttonSave = itemView.findViewById(R.id.buttonSave);
            buttonCancel = itemView.findViewById(R.id.buttonCancel);

            layoutViewButtons =
                    itemView.findViewById(R.id.layoutViewButtons);
            layoutEditButtons =
                    itemView.findViewById(R.id.layoutEditButtons);
        }
    }
}
