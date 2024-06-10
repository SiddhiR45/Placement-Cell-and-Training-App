package com.example.project_patt;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


public class ShowDetailsFragment extends Fragment {

    private TextView txtLocation, txtsalary, txtExp, txtDes, txttitle, txtcompany,txtHighlight;
    private Add_job selectedJob;
    private static final String ARG_FOOD_DOMAIN = "job_domain";

    public ShowDetailsFragment() {
        // Required empty public constructor
    }

    public static ShowDetailsFragment newInstance(Add_job selectedJob) {
        ShowDetailsFragment fragment = new ShowDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_FOOD_DOMAIN, selectedJob);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_details, container, false);
        initView(view);
        getBundle();
        Button btnApply = view.findViewById(R.id.btnApply);
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addApplication();
            }

            private void addApplication() {
                String jobTitle = txttitle.getText().toString();
                String companyName = txtcompany.getText().toString().trim();

                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser != null) {
                    String userId = currentUser.getUid();
                    String username = currentUser.getDisplayName();
                    if (username != null) {
                        DatabaseReference jobApplicationsRef = FirebaseDatabase.getInstance().getReference("JobApplications");
                        String applicationId = jobApplicationsRef.push().getKey();

                        Map<String, Object> applicationData = new HashMap<>();
                        applicationData.put("jobTitle", jobTitle);
                        applicationData.put("companyName", companyName);
                        applicationData.put("userId", userId); // Add userId
                        applicationData.put("username", username);

                        jobApplicationsRef.child(applicationId).setValue(applicationData)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getActivity(), "Application submitted successfully", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getActivity(), "Failed to submit application", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    } else {
                        Toast.makeText(getActivity(), "Username not found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "User not logged in", Toast.LENGTH_SHORT).show();
                }
            }
        }); // Closing btnApply.setOnClickListener block

        return view;
    }

    private void getBundle() {
        Bundle args = getArguments();
        if (args != null) {
            selectedJob = (Add_job) args.getSerializable(ARG_FOOD_DOMAIN);
            if (selectedJob != null) {
                txtLocation.setText(selectedJob.getLocation());
                txtsalary.setText(selectedJob.getSalary());
                txttitle.setText(selectedJob.getJobName());
                txtcompany.setText(selectedJob.getCompanyName());
                txtExp.setText(selectedJob.getJobName());
                txtDes.setText(selectedJob.getDescription());
                txtHighlight.setText(selectedJob.getHighlight());
            }
        }
    }

    private void initView(View view) {
        txtLocation = view.findViewById(R.id.txtLocation);
        txtsalary = view.findViewById(R.id.txtsalary);
        txtExp = view.findViewById(R.id.txtExp);
        txtDes = view.findViewById(R.id.txtDes);
        txttitle = view.findViewById(R.id.txttitle);
        txtcompany = view.findViewById(R.id.txtcompany);
        txtHighlight=view.findViewById(R.id.txtHighlight);
    }
}

