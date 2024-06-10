package com.example.project_patt.Fragment;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_patt.ItemDividerDecoration;
import com.example.project_patt.ItemSpacingDecoration;
import com.example.project_patt.R;
import com.example.project_patt.retrieve.SelectedItemsViewModel;
import com.example.project_patt.retrieve.studydisplayadapter;
import com.example.project_patt.retrieve.studydisplaymodel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.

 * create an instance of this fragment.
 */
public class MockFragment  extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private List<studydisplaymodel> studyList;
    private studydisplayadapter studydisplayadapter;


    public MockFragment() {
        // Required empty public constructor
    }


    public static MockFragment newInstance(String param1, String param2) {
        MockFragment fragment = new MockFragment();
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

        View view = inflater.inflate(R.layout.fragment_mock, container, false);
        // Inflate the layout for this fragment
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference productsRef = database.getReference("studies/Mock Test");

        // Initialize the RecyclerView and its adapter for products
        RecyclerView recyclerView = view.findViewById(R.id.displaymocktest);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new ItemSpacingDecoration(getResources().getDimensionPixelSize(R.dimen.item_spacing))); // Adjust spacing as needed
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        int dividerColor = getResources().getColor(R.color.divider_color); // Define divider color in colors.xml
        int dividerHeight = getResources().getDimensionPixelSize(R.dimen.divider_height); // Define divider height in dimens.xml
        recyclerView.addItemDecoration(new ItemDividerDecoration(dividerColor, dividerHeight));
        studyList = new ArrayList<>();
        studydisplayadapter = new studydisplayadapter(studyList,requireContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(studydisplayadapter);

        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: Data changed. Total children: " + dataSnapshot.getChildrenCount());
                studyList.clear();
                for (DataSnapshot jobSnapshot : dataSnapshot.getChildren()) {
                    studydisplaymodel job = jobSnapshot.getValue(studydisplaymodel.class);
                    studyList.add(job);
                }
                studydisplayadapter.notifyDataSetChanged();
                Log.d(TAG, "onDataChange: JobList size after update: " + studyList.size());
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