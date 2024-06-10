package com.example.project_patt.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.project_patt.Add_job;
import com.example.project_patt.JobAdapter;
import com.example.project_patt.R;
import com.example.project_patt.SlideAdapter;
import com.example.project_patt.jobmodel;
import com.example.project_patt.retrieve.JobfetchAdapter;
import com.example.project_patt.retrieve.SelectedItemsViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HomeFragment extends Fragment {
    //job
    private RecyclerView recyclerView;
    ImageView searchImageView;
    private RecyclerView.Adapter adapter1;
    private RecyclerView recyclerView1;
    private RecyclerView.Adapter adapter2;

    private List<Add_job> JobList;
    private JobfetchAdapter jobfetchAdapter;
    private static final String TAG = "HomeFragment";
    //advertisement
    private ViewPager viewPager;
    private int currentPage = 0;
    private Timer timer;
    final long delay = 5000;
    //
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homefragment, container, false);
        setHasOptionsMenu(true);

        //recylerview logic
        recyclerView = view.findViewById(R.id.jobs);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager1);
        adapter1 = new JobAdapter(requireContext(), getCategoryList());
        recyclerView.setAdapter(adapter1);

        //recylerview2
       /* recyclerView1 = view.findViewById(R.id.jobs1);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView1.setLayoutManager(layoutManager2);
        adapter2 = new JobAdapter1(requireContext(), getCategoryList1(),getdetailslist());
        recyclerView1.setAdapter(adapter2);*/
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference productsRef = database.getReference("jobs");

        // Initialize the RecyclerView and its adapter for products
        RecyclerView recyclerView = view.findViewById(R.id.jobs1);
        //LinearLayoutManager layoutManager2 = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        JobList = new ArrayList<>();
        jobfetchAdapter = new JobfetchAdapter(JobList,getSelectedItemsViewModel());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        recyclerView.setAdapter(jobfetchAdapter);

        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: Data changed. Total children: " + dataSnapshot.getChildrenCount());
                JobList.clear();
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
                        JobList.add(job);
                    }
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


        //viewpager logic
        viewPager = view.findViewById(R.id.adv);
        SlideAdapter viewPagerAdapter = new SlideAdapter(getActivity());
        viewPager.setAdapter(viewPagerAdapter);

        // Set up a timer to auto-scroll the ViewPager
        final Handler handler = new Handler();
        final Runnable update = new Runnable() {
            public void run() {
                if (currentPage == viewPagerAdapter.getCount()) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);
            }
        };

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(update);
            }
        }, delay, delay);
        TextView searchEditText = view.findViewById(R.id.searchEditText1);
        searchEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace "YourFragment" with the fragment you want to switch to
                Fragment newFragment = new JobsFragment();
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, newFragment)
                        .addToBackStack(null)
                        .commit();

            }
        });




        //

        return view;

    }

    private SelectedItemsViewModel getSelectedItemsViewModel() {
        return new ViewModelProvider(this).get(SelectedItemsViewModel.class);
    }

    //displaying the job in recyclerview
    private ArrayList<jobmodel> getCategoryList() {
        ArrayList<jobmodel> categoryList = new ArrayList<>();
        categoryList.add(new jobmodel("IT / Hardware", R.drawable.it));
        categoryList.add(new jobmodel("Mechanical Engineer", R.drawable.mechanical));
        categoryList.add(new jobmodel("Customer Service", R.drawable.customers));

        // Add more categories here
        return categoryList;
    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.noti_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.notification) {
            // Navigate to the MessageFragment
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            // Replace the current fragment with the MessageFragment
            MessageFragment messageFragment = new MessageFragment();
            fragmentTransaction.replace(R.id.fragment_container, messageFragment); // R.id.fragment_container is the ID of the container where you want to replace the fragment
            fragmentTransaction.addToBackStack(null); // Optional, adds the transaction to the back stack
            fragmentTransaction.commit();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }





}
