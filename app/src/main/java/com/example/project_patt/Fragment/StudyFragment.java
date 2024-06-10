package com.example.project_patt.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.project_patt.R;
import com.example.project_patt.StudyAdapter;
import com.example.project_patt.studmodel;

import java.util.ArrayList;


public class StudyFragment extends Fragment {
    private RecyclerView recyclerView3;
    private RecyclerView.Adapter adapter3;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_study, container, false);

        //recylerview logic
        recyclerView3 = view.findViewById(R.id.study);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView3.setLayoutManager(layoutManager1);
        adapter3 = new StudyAdapter(requireContext(), getCategoryList());
        recyclerView3.setAdapter(adapter3);

        //recylerview2

        return view;
    }

    private ArrayList<studmodel> getCategoryList() {
        ArrayList<studmodel> categoryList = new ArrayList<>();
        categoryList.add(new studmodel("Mock Test",R.drawable.mock));
        categoryList.add(new studmodel("Interview Questions",R.drawable.inter));
        categoryList.add(new studmodel("Communication",R.drawable.com));
        // Add more categories here
        return categoryList;
    }
}
