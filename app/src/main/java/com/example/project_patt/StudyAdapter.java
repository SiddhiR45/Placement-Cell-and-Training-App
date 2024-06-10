package com.example.project_patt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project_patt.Fragment.CommunicationFragment;
import com.example.project_patt.Fragment.InterviewFragment;
import com.example.project_patt.Fragment.MockFragment;

import java.util.ArrayList;

public class StudyAdapter extends RecyclerView.Adapter<StudyAdapter.ViewHolder> {
    private ArrayList<studmodel> categoryDomains;
    private Context context;

    public StudyAdapter(Context context, ArrayList<studmodel> categoryDomains) {
        this.context = context;
        this.categoryDomains = categoryDomains;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_study, parent, false);
        return new StudyAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull StudyAdapter.ViewHolder holder, int position) {
        studmodel category = categoryDomains.get(holder.getAdapterPosition());

        holder.categoryName.setText(category.getStud());

        int drawableResourceId = holder.itemView.getContext().getResources().getIdentifier(String.valueOf(category.getImg()), "drawable", holder.itemView.getContext().getPackageName());

        // Resize the image using Glide
        Glide.with(holder.itemView.getContext())
                .load(drawableResourceId)
                .centerInside()
                .into(holder.categoryPic);
        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                switch (position) {
                    case 0:
                        // Navigate to the PracticeFragment
                        navigateToMockFragment();
                        break;
                   case 1:
                        // Navigate to another fragment based on position 1
                        navigateToInterviewFragment();
                        break;
                    case 2:
                        // Navigate to another fragment based on position 2
                        navigateToCommunicatiomFragment();
                        break;
                    /*case 3:
                        // Navigate to another fragment based on position 3
                        navigateToCategoryCurryFragment();
                        break;
                   */
                    default:
                        // Handle the default case or navigate to a default fragment
                        break;
                }

            }

        });


    }
    private void navigateToMockFragment() {
        // Create an instance of the PracticeFragment
        MockFragment practiceFragment = new MockFragment();

        // Get the FragmentManager and start a new FragmentTransaction
        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Replace the current fragment with the PracticeFragment
        fragmentTransaction.replace(R.id.fragment_container, practiceFragment);

        // Add the transaction to the back stack (optional)
        fragmentTransaction.addToBackStack(null);

        // Commit the transaction
        fragmentTransaction.commit();
    }
    private void navigateToInterviewFragment() {
        // Create an instance of the PracticeFragment
        InterviewFragment practiceFragment = new InterviewFragment();

        // Get the FragmentManager and start a new FragmentTransaction
        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Replace the current fragment with the PracticeFragment
        fragmentTransaction.replace(R.id.fragment_container, practiceFragment);

        // Add the transaction to the back stack (optional)
        fragmentTransaction.addToBackStack(null);

        // Commit the transaction
        fragmentTransaction.commit();
    }
    private void navigateToCommunicatiomFragment() {
        // Create an instance of the PracticeFragment
        CommunicationFragment practiceFragment = new CommunicationFragment();

        // Get the FragmentManager and start a new FragmentTransaction
        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Replace the current fragment with the PracticeFragment
        fragmentTransaction.replace(R.id.fragment_container, practiceFragment);

        // Add the transaction to the back stack (optional)
        fragmentTransaction.addToBackStack(null);

        // Commit the transaction
        fragmentTransaction.commit();
    }






    @Override
    public int getItemCount() {
        return categoryDomains.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView categoryPic;
        public TextView categoryName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryPic = itemView.findViewById(R.id.img2);
            categoryName = itemView.findViewById(R.id.stud);
        }
    }
}
