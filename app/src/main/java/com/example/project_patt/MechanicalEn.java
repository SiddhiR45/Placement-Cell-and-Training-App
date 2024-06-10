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
public class MechanicalEn extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private List<Add_job> JobList;
    private JobfetchAdapter jobfetchAdapter;


    public MechanicalEn() {
        // Required empty public constructor
    }


    public static MechanicalEn newInstance(String param1, String param2) {
        MechanicalEn fragment = new MechanicalEn();
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

        View view = inflater.inflate(R.layout.fragment_mechanical_en, container, false);
        // Inflate the layout for this fragment
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference productsRef = database.getReference("jobs/Mechanical Engineer");

        // Initialize the RecyclerView and its adapter for products
        RecyclerView recyclerView = view.findViewById(R.id.displaymechjpb);
        JobList = new ArrayList<>();
        jobfetchAdapter = new JobfetchAdapter(JobList, getSelectedItemsViewModel());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(jobfetchAdapter);

        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: Data changed. Total children: " + dataSnapshot.getChildrenCount());
                JobList.clear();
                for (DataSnapshot jobSnapshot : dataSnapshot.getChildren()) {
                    Add_job job = jobSnapshot.getValue(Add_job.class);
                    JobList.add(job);
                }
                jobfetchAdapter.notifyDataSetChanged();
                Log.d(TAG, "onDataChange: JobList size after update: " + JobList.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle onCancelled
                Log.e(TAG, "onCancelled: Error fetching data: " + databaseError.getMessage());
            }
        });

        return view;
    }

    private SelectedItemsViewModel getSelectedItemsViewModel() {
        return new ViewModelProvider(this).get(SelectedItemsViewModel.class);
    }
}