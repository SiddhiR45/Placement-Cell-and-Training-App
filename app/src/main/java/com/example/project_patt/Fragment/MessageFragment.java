// MessageFragment.java
package com.example.project_patt.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_patt.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import chats.NotificationAdapter;

public class MessageFragment extends Fragment {

    private List<String> messages = new ArrayList<>();
    private RecyclerView recyclerView;
    private NotificationAdapter adapter;
    private FirebaseFirestore firestore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);

        recyclerView = view.findViewById(R.id.notiRecycler);
        adapter = new NotificationAdapter(messages);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();

        // Retrieve notification messages from Firestore
        retrieveMessagesFromFirestore();

        return view;
    }

    private void retrieveMessagesFromFirestore() {
        // Fetch messages from Firestore
        // Example code to fetch messages from Firestore:
        firestore.collection("NotificationMsg")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        String title=documentSnapshot.getString("title");
                        String message = documentSnapshot.getString("message");
                        if (message != null) {
                            String titleAndMessage = title + "\n" + message;
                            messages.add(titleAndMessage);
                        }
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    // Handle errors
                });

    }
}
