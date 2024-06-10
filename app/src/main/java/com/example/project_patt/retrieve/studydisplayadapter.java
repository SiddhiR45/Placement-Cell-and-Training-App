package com.example.project_patt.retrieve;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_patt.R;

import java.util.List;

public class studydisplayadapter extends RecyclerView.Adapter<studydisplayadapter.ViewHolder> {

    private List<studydisplaymodel> studyList;
    private Context context;

    public studydisplayadapter(List<studydisplaymodel> studyList, Context context) {
        this.studyList = studyList;
        this.context = context;
    }

    @NonNull
    @Override
    public studydisplayadapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.studyadd_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull studydisplayadapter.ViewHolder holder, int position) {
        studydisplaymodel study = studyList.get(position);
        holder.textViewStudyType.setText("Study Type: " + study.getStudyType());
        holder.textViewCourseName.setText("Course Name: " + study.getCourseName());
        holder.textViewStudyLink.setText(study.getStudyLink());

        // Load PDF file into WebView
        holder. webViewFile.loadUrl(study.getStudyLink()); // Assuming studyLink is the URL of the PDF file

        // Set click listener for the study link TextView
        holder.textViewStudyLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open study link in a WebView
                holder.webViewFile.loadUrl(study.getStudyLink());

            }
        });
    }

    @Override
    public int getItemCount() {
        return studyList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewStudyType;
        TextView textViewCourseName;
        TextView textViewStudyLink;
        WebView webViewFile;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewStudyType = itemView.findViewById(R.id.textView_study_type);
            textViewCourseName = itemView.findViewById(R.id.textView_course_name);
            textViewStudyLink = itemView.findViewById(R.id.textView_study_link);
            webViewFile = itemView.findViewById(R.id.webView_file);
        }
    }
}
