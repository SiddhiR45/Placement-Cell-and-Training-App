// ChatFragment.java
package chats;

import android.app.NotificationManager;
import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.project_patt.MainActivity;
import com.example.project_patt.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;

public class ChatFragment extends Fragment {

    private NotificationManager nm;
    private NotificationChannel nc;
    private NotificationCompat.Builder builder;
    private final String channelID = "com.example.project_patt";
    private final String description = "Text Notification";

    // Declare notificationEditText
    private EditText notificationEditText,titleEdittext;
    private NotificationViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        nm = (NotificationManager) requireActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        viewModel = new ViewModelProvider(requireActivity()).get(NotificationViewModel.class);
        // Find notificationEditText from the layout
        notificationEditText = view.findViewById(R.id.editTextNotification);
        titleEdittext=view.findViewById(R.id.edit_title);
        Button showButton = view.findViewById(R.id.btnAnment);
        showButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve the message from the EditText
                String title=titleEdittext.getText().toString();
                String message = notificationEditText.getText().toString();
                // Store message in Firebase Firestore
                storeMessageInFirestore(title,message);

                // Display notification
                displayNotification(title,message);
                Toast.makeText(requireContext(), "Announcement send successfully", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    private void storeMessageInFirestore(String title,String message) {
        // Store message in Firebase Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> notification = new HashMap<>();
        notification.put("title",title);
        notification.put("message", message);
        db.collection("NotificationMsg")
                .add(notification)
                .addOnSuccessListener(documentReference -> {
                    // Message added successfully
                })
                .addOnFailureListener(e -> {
                    // Error occurred while adding message
                    Log.e("Firestore", "Error adding document", e);
                });
    }

    private void displayNotification(String title, String message) {
        // Construct the FCM message payload
        Map<String, String> data = new HashMap<>();
        data.put("title", title);
        data.put("message", message);

        // Specify the recipient user's FCM token (replace "userFCMToken" with the actual token of the user)
        String userFCMToken = "userFCMToken";

        // Construct the FCM notification message
        FirebaseMessaging.getInstance().send(new RemoteMessage.Builder(userFCMToken)
                .setData(data)
                .build());
        // Log success message
        Log.d("FCM", "Notification sent successfully");
    }


}
