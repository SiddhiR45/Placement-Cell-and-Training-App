package chats;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_patt.R;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private List<String> notificationMessages;

    public NotificationAdapter(List<String> notificationMessages) {
        this.notificationMessages = notificationMessages;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        String message = notificationMessages.get(position);
        holder.bind(message);
    }

    @Override
    public int getItemCount() {
        return notificationMessages.size();
    }

    public void updateData(List<String> notificationMessages) {
        this.notificationMessages = notificationMessages;
        notifyDataSetChanged(); // Notify RecyclerView to update the data
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.notificationTextView);
        }

        public void bind(String message) {
            textView.setText(message);
        }
    }
}
