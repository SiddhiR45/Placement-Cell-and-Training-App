package com.example.project_patt.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_patt.ItemDividerDecoration;
import com.example.project_patt.ItemSpacingDecoration;
import com.example.project_patt.R;
import com.example.project_patt.Userjobadapter;
import com.example.project_patt.retrieve.applyjob;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Userdata extends Fragment {
    private RecyclerView recyclerView;
    private Userjobadapter adapter;
    private List<applyjob> jobList;

    // Your Firebase Database reference
    private DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_userdata, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewJobs);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new ItemSpacingDecoration(getResources().getDimensionPixelSize(R.dimen.item_spacing))); // Adjust spacing as needed
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        int dividerColor = getResources().getColor(R.color.divider_color); // Define divider color in colors.xml
        int dividerHeight = getResources().getDimensionPixelSize(R.dimen.divider_height); // Define divider height in dimens.xml
        recyclerView.addItemDecoration(new ItemDividerDecoration(dividerColor, dividerHeight));

        jobList = new ArrayList<>();
        adapter = new Userjobadapter(jobList);
        recyclerView.setAdapter(adapter);

        // Initialize Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("JobApplications");

        // Retrieve job details from Firebase
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                jobList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    applyjob job = snapshot.getValue(applyjob.class);
                    jobList.add(job);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Failed to retrieve data", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
