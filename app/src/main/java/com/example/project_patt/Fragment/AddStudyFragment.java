package com.example.project_patt.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.project_patt.R;
import com.example.project_patt.retrieve.studydisplaymodel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddStudyFragment extends Fragment {

    private DatabaseReference databaseRef;

    private EditText studyTypeEditText;
    private EditText courseNameEditText;
    private EditText studyLinkEditText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_study, container, false);

        databaseRef = FirebaseDatabase.getInstance().getReference("studies");

        studyTypeEditText = rootView.findViewById(R.id.editText_study_type);
        courseNameEditText = rootView.findViewById(R.id.editText_course_name);
        studyLinkEditText = rootView.findViewById(R.id.editText_study_link);

        Button uploadStudyButton = rootView.findViewById(R.id.btn_upload_file);
        uploadStudyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadStudy();
            }
        });

        return rootView;
    }

    private void uploadStudy() {
        String studyType = studyTypeEditText.getText().toString().trim();
        String courseName = courseNameEditText.getText().toString().trim();
        String studyLink = studyLinkEditText.getText().toString().trim();

        if (!studyType.isEmpty() && !courseName.isEmpty() && !studyLink.isEmpty()) {
            studydisplaymodel study = new studydisplaymodel(studyType, courseName, studyLink);

            // Upload study to Firebase under "studies/studyType"
            databaseRef.child(studyType).push().setValue(study);
            showToast("Study uploaded successfully");
        } else {
            showToast("Please fill in all fields");
        }
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}
