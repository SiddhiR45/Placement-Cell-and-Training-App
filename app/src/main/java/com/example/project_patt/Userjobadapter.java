package com.example.project_patt;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_patt.retrieve.applyjob;

import java.util.List;

public class Userjobadapter extends RecyclerView.Adapter<Userjobadapter.ViewHolder> {
    private List<applyjob> jobList;

    public Userjobadapter(List<applyjob> jobList) {
        this.jobList = jobList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.userapplydata, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        applyjob job = jobList.get(position);
        holder.bind(job);
    }

    @Override
    public int getItemCount() {
        return jobList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtJobTitle, txtCompanyName, txtUsername;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtJobTitle = itemView.findViewById(R.id.txtJobTitle);
            txtCompanyName = itemView.findViewById(R.id.txtCompanyName);
            txtUsername = itemView.findViewById(R.id.txtUserName);
        }

        public void bind(applyjob job) {
            txtJobTitle.setText(job.getJobTitle());
            txtCompanyName.setText(job.getCompanyName());
            txtUsername.setText(job.getUsername());
        }
    }
}
