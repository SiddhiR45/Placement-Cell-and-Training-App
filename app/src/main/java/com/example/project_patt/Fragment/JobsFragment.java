package com.example.project_patt.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_patt.Add_job;
import com.example.project_patt.R;
import com.example.project_patt.retrieve.JobfetchAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Search.SerachResultAdapter;

public class JobsFragment extends Fragment {

    private static final String TAG = "SearchFragment";

    private RecyclerView recyclerView;
    private SearchView searchView;
    private SerachResultAdapter searchResultsAdapter;
    private List<Add_job> jobList;

    public JobsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_jobs, container, false);

        recyclerView = view.findViewById(R.id.searchrecylerview);
        searchView = view.findViewById(R.id.searchView);
        searchView.setEnabled(true); // Ensure that the SearchView is enabled

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(layoutManager);
        searchResultsAdapter = new SerachResultAdapter(new ArrayList<>());
        recyclerView.setAdapter(searchResultsAdapter);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference jobsRef = database.getReference("jobs");
        jobsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: Data changed. Total children: " + dataSnapshot.getChildrenCount());
                jobList = new ArrayList<>();
                for (DataSnapshot vendorSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot productSnapshot : vendorSnapshot.getChildren()) {
                        Add_job job = new Add_job();
                        job.setCompanyName(productSnapshot.child("companyName").getValue(String.class));
                        job.setJobId(productSnapshot.child("jobId").getValue(String.class));
                        job.setJobName(productSnapshot.child("jobName").getValue(String.class));
                        job.setLocation(productSnapshot.child("location").getValue(String.class));
                        job.setSalary(productSnapshot.child("salary").getValue(String.class));
                        job.setDescription(productSnapshot.child("description").getValue(String.class));
                        job.setHighlight(productSnapshot.child("highlight").getValue(String.class));
                        jobList.add(job);
                    }
                }
                searchResultsAdapter.updateList(jobList);
                Log.d(TAG, "onDataChange: JobList size after update: " + jobList.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle onCancelled
                Log.e(TAG, "onCancelled: Error fetching data: " + databaseError.getMessage());
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterJobs(newText);
                return true;
            }
        });

        return view;
    }

    private void filterJobs(String query) {
        if (jobList != null) {
            List<Add_job> filteredList = new ArrayList<>();
            for (Add_job job : jobList) {
                if (job.getJobName().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(job);
                }
            }
            searchResultsAdapter.updateList(filteredList);
        }
    }


    private void showKeyboard(View view) {
        if (view.requestFocus()) {
            InputMethodManager inputMethodManager = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }
}
