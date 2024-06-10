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

import java.util.ArrayList;

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.ViewHolder> {
    private ArrayList<jobmodel> categoryDomains;
    private Context context;


    public JobAdapter(Context context, ArrayList<jobmodel> categoryDomains) {
        this.context = context;
        this.categoryDomains = categoryDomains;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_jobs, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        jobmodel category = categoryDomains.get(holder.getAdapterPosition());

        holder.categoryName.setText(category.getJob());

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
                        navigateToITFragment();
                        break;
                    case 1:
                        // Navigate to another fragment based on position 1
                        navigateToCategoryFoodFragment();
                        break;
                   /* case 2:
                        // Navigate to another fragment based on position 2
                        navigateToCategoryFriedFragment();
                        break;
                    case 3:
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


    private void navigateToITFragment() {
        // Create an instance of the PracticeFragment
        ITJobsFragment practiceFragment = new ITJobsFragment();

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

    private void navigateToCategoryFoodFragment() {
        // Create an instance of the CategoryFoodFragment
        MechanicalEn foodFragment = new MechanicalEn(); // Replace with the actual fragment you want to navigate to

        // Get the FragmentManager and start a new FragmentTransaction
        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Replace the current fragment with the CategoryFoodFragment
        fragmentTransaction.replace(R.id.fragment_container, foodFragment);

        // Add the transaction to the back stack (optional)
        fragmentTransaction.addToBackStack(null);

        // Commit the transaction
        fragmentTransaction.commit();
    }
    /*
    private void navigateToCategoryFriedFragment() {
        // Create an instance of the CategoryFriedFragment
        CategoryFriedFragment friedFragment = new CategoryFriedFragment(); // Replace with the actual fragment you want to navigate to

        // Get the FragmentManager and start a new FragmentTransaction
        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Replace the current fragment with the CategoryFriedFragment
        fragmentTransaction.replace(R.id.fragment_container, friedFragment);

        // Add the transaction to the back stack (optional)
        fragmentTransaction.addToBackStack(null);

        // Commit the transaction
        fragmentTransaction.commit();
    }


*/
    @Override
    public int getItemCount() {
        return categoryDomains.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView categoryPic;
        public TextView categoryName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryPic = itemView.findViewById(R.id.img1);
            categoryName = itemView.findViewById(R.id.jobname);
        }
    }
}
