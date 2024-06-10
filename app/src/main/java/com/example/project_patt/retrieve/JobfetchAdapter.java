package com.example.project_patt.retrieve;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_patt.Add_job;
import com.example.project_patt.R;
import com.example.project_patt.ShowDetailsFragment;

import java.util.ArrayList;
import java.util.List;

public class JobfetchAdapter extends RecyclerView.Adapter<JobfetchAdapter.JobViewHolder> {
    private List<Add_job> jobList;
    private SelectedItemsViewModel selectedItemsViewModel;

    public JobfetchAdapter(List<Add_job> jobList, SelectedItemsViewModel selectedItemsViewModel) {
        this.jobList = jobList;
        this.selectedItemsViewModel = selectedItemsViewModel;
    }

    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_jobs1, parent, false);
        return new JobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobViewHolder holder, int position) {
        Add_job product = jobList.get(position);
        // Bind the data to the ViewHolder views
        holder.jobname1.setText(product.getJobName());
        holder.salary.setText(product.getSalary());
        holder.comname.setText(product.getCompanyName());
        holder.location.setText(product.getLocation());


        holder.linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && position < jobList.size()) {
                    Add_job selectedJob = jobList.get(position);
                    if (selectedJob != null) {
                        // Create a new instance of ShowDetailFragment with the selected job
                        ShowDetailsFragment showDetailFragment = ShowDetailsFragment.newInstance(selectedJob);

                        // Use FragmentTransaction to display the fragment
                        FragmentTransaction fragmentTransaction = ((AppCompatActivity) holder.itemView.getContext()).getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_container, showDetailFragment); // Use your container view id
                        fragmentTransaction.addToBackStack(null); // Add to the back stack if needed
                        fragmentTransaction.commit();
                    }
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return jobList.size();
    }

    public static class JobViewHolder extends RecyclerView.ViewHolder {
        public TextView jobname1;
        public TextView salary;
        public TextView comname;
        public TextView location;
        public LinearLayout linear;

        public JobViewHolder(View itemView) {
            super(itemView);
            jobname1 = itemView.findViewById(R.id.jobname1);
            salary = itemView.findViewById(R.id.salary);
            comname = itemView.findViewById(R.id.comname);
            location = itemView.findViewById(R.id.location);
            linear = itemView.findViewById(R.id.l1);
        }
    }
}
