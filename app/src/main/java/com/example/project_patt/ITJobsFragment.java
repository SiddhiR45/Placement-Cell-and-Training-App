package com.example.project_patt;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.project_patt.retrieve.JobfetchAdapter;
import com.example.project_patt.retrieve.SelectedItemsViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ITJobsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ITJobsFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private List<Add_job> allJobList;
    private JobfetchAdapter jobAdapter;

    public ITJobsFragment() {
        // Required empty public constructor
    }

    public static ITJobsFragment newInstance(String param1, String param2) {
        ITJobsFragment fragment = new ITJobsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_it_jobs, container, false);
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        allJobList = new ArrayList<>();
        jobAdapter = new JobfetchAdapter(allJobList, getSelectedItemsViewModel());

        RecyclerView recyclerView = view.findViewById(R.id.displayitjpb);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(jobAdapter);

        retrieveJobs(database.getReference("jobs/Information Technology"));
        retrieveJobs(database.getReference("jobs/Data Scientist"));
        retrieveJobs(database.getReference("jobs/Business Analyst"));

        return view;
    }

    private void retrieveJobs(DatabaseReference jobsRef) {
        jobsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: Data changed. Total children: " + dataSnapshot.getChildrenCount());
                for (DataSnapshot jobSnapshot : dataSnapshot.getChildren()) {
                    Add_job job = jobSnapshot.getValue(Add_job.class);
                    allJobList.add(job);
                }
                jobAdapter.notifyDataSetChanged();
                Log.d(TAG, "onDataChange: JobList size after update: " + allJobList.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: Error fetching data: " + databaseError.getMessage());
            }
        });
    }

    private SelectedItemsViewModel getSelectedItemsViewModel() {
        if (getActivity() != null) {
            return new ViewModelProvider(getActivity()).get(SelectedItemsViewModel.class);
        } else {
            return null;
        }
    }
}
