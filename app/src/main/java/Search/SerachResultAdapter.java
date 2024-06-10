package Search;

// Import statements for necessary Android and Java classes
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
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

// Adapter class for the RecyclerView
public class SerachResultAdapter extends RecyclerView.Adapter<SerachResultAdapter.ViewHolder> implements Filterable {

    // List to hold the original items and a copy for filtering
    private List<Add_job> itemList;
    private List<Add_job> itemListFull;

    // Constructor to initialize the adapter with a list of items
    public SerachResultAdapter(List<Add_job> itemList) {
        this.itemList = itemList;
        this.itemListFull = new ArrayList<>(itemList); // Make a copy of the original list
    }

    // Method to create ViewHolders
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for the ViewHolder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_jobs1, parent, false);
        return new ViewHolder(view); // Return a new instance of ViewHolder
    }

    // Method to bind data to ViewHolders
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Add_job item = itemList.get(position); // Get the item at the specified position
        holder.bind(item); // Bind the item to the ViewHolder
    }

    // Method to get the total number of items in the list
    @Override
    public int getItemCount() {
        return itemList.size();
    }

    // Method to get the filter for this adapter
    @Override
    public Filter getFilter() {
        return itemFilter;
    }

    // Filter implementation
    private Filter itemFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Add_job> filteredList = new ArrayList<>();

            // If the constraint is null or empty, show all items
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(itemListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                // Filter items based on the constraint
                for (Add_job item : itemListFull) {
                    if (item.getJobName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            // Prepare the results to be returned
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            itemList.clear(); // Clear the current list
            itemList.addAll((List<Add_job>) results.values); // Add filtered items to the list
            notifyDataSetChanged(); // Notify adapter of data change
        }
    };

    // Method to update the list with new items
    public void updateList(List<Add_job> newList) {
        itemList.clear(); // Clear the current list
        itemList.addAll(newList); // Add new items to the list
        notifyDataSetChanged(); // Notify adapter of data change
    }

    // ViewHolder class to hold references to the views for each item
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView jobNameTextView;
        TextView salary;
        TextView comname;
        TextView location;

        // Constructor to initialize the ViewHolder
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize views
            jobNameTextView = itemView.findViewById(R.id.jobname1);
            salary = itemView.findViewById(R.id.salary);
            comname = itemView.findViewById(R.id.comname);
            location = itemView.findViewById(R.id.location);

            // Set click listener for each item
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    // If a valid position is clicked
                    if (position != RecyclerView.NO_POSITION && position < itemList.size()) {
                        Add_job selectedJob = itemList.get(position); // Get the selected job
                        if (selectedJob != null) {
                            // Create a new instance of ShowDetailFragment with the selected job
                            ShowDetailsFragment showDetailFragment = ShowDetailsFragment.newInstance(selectedJob);

                            // Use FragmentTransaction to display the fragment
                            FragmentTransaction fragmentTransaction = ((AppCompatActivity) v.getContext()).getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.fragment_container, showDetailFragment); // Use your container view id
                            fragmentTransaction.addToBackStack(null); // Add to the back stack if needed
                            fragmentTransaction.commit(); // Commit the transaction
                        }
                    }
                }
            });
        }

        // Method to bind data to the ViewHolder
        public void bind(Add_job item) {
            // Set data to views
            jobNameTextView.setText(item.getJobName());
            salary.setText(item.getSalary());
            comname.setText(item.getCompanyName());
            location.setText(item.getLocation());
        }
    }
}
