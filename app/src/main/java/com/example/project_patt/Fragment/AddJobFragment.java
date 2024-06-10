package com.example.project_patt.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.project_patt.Add_job;
import com.example.project_patt.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class AddJobFragment extends Fragment {
    private Spinner jobSpinner;
    private EditText jobLocationEditText, companyNameEditText, salaryEditText,descEditText,highEditText;
    private Button addButton;

    private DatabaseReference databaseReference;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_addjob, container, false);

        // Initialize Spinner
        jobSpinner = view.findViewById(R.id.job_spinner);

        // Populate Spinner with job options
        populateSpinner();

        // Initialize Firebase database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("jobs");

        // Initialize views
        jobLocationEditText = view.findViewById(R.id.loc_edittext);
        companyNameEditText = view.findViewById(R.id.compname_edittext);
        salaryEditText = view.findViewById(R.id.sal_edittext);
        descEditText=view.findViewById(R.id.txtDes);
        highEditText=view.findViewById(R.id.txtHighlight);

        addButton = view.findViewById(R.id.upload_button);

        // Set OnClickListener for the add button
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addJob();

            }
        });

        return view;
    }

    private void populateSpinner() {
        // Dummy job options
        List<String> jobOptions = new ArrayList<>();
        jobOptions.add("Select Job"); // hint
        jobOptions.add("Software Developer");
        jobOptions.add("Data Scientist");
        jobOptions.add("Project Manager");
        jobOptions.add("UX/UI Designer");
        jobOptions.add("Business Analyst");
        jobOptions.add("Information Technology");
        jobOptions.add("Mechanical Engineer");

        // Create adapter for Spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, jobOptions);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set adapter to Spinner
        jobSpinner.setAdapter(spinnerAdapter);
    }

    private void addJob() {
        String jobName = jobSpinner.getSelectedItem().toString(); // Retrieve selected item from Spinner
        String jobLocation = jobLocationEditText.getText().toString().trim();
        String companyName = companyNameEditText.getText().toString().trim();
        String salary = salaryEditText.getText().toString().trim();
        String jobDesc=descEditText.getText().toString().trim();
        String jobHigh=highEditText.getText().toString().trim();

        if (!jobName.isEmpty() && !jobLocation.isEmpty() && !companyName.isEmpty() && !salary.isEmpty()) {
            // Generate a unique key for the job
            String jobId = "job" + System.currentTimeMillis(); // Generate unique job ID

            // Create a Job object
            Add_job job = new Add_job(companyName, jobId, jobName, jobLocation, salary,jobDesc,jobHigh);

            // Upload the job to Firebase under "jobs/jobname/jobId"
            databaseReference.child(jobName).child(jobId).setValue(job)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getContext(), "Job added successfully", Toast.LENGTH_SHORT).show();
                                // Clear input fields
                                jobLocationEditText.setText("");
                                companyNameEditText.setText("");
                                salaryEditText.setText("");
                            } else {
                                Toast.makeText(getContext(), "Failed to add job", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
        }
    }
}
