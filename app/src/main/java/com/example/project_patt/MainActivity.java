package com.example.project_patt;

import static androidx.media.session.MediaButtonReceiver.handleIntent;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.project_patt.Fragment.HomeFragment;
import com.example.project_patt.Fragment.JobsFragment;
import com.example.project_patt.Fragment.MessageFragment;
import com.example.project_patt.Fragment.ProfileFragment;
import com.example.project_patt.Fragment.StudyFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import chats.ChatFragment;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;
    private chats.ChatFragment ChatFragment;

    // Check if this activity was started from the AdminActivity with a message


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.homefragment) {
                selectedFragment = new HomeFragment();
            } else if (item.getItemId() == R.id.jobsFragment) {
                selectedFragment = new JobsFragment();
            } else if (item.getItemId() == R.id.messageFragment) {
                selectedFragment = new MessageFragment();
            } else if (item.getItemId() == R.id.studyFragment) {
                selectedFragment = new StudyFragment();
            } else if (item.getItemId() == R.id.profileFragment) {
                selectedFragment = new ProfileFragment();
            }

            loadFragment(selectedFragment);
            return true;
        });


        // Handle intent if activity was started from a notification
        handleIntent(getIntent());


    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent); // Check for intent when a new intent is received
    }

    private void handleIntent(Intent intent) {
        if (intent != null && intent.hasExtra("message")) {
            String message = intent.getStringExtra("message");

            // Pass the message to the existing MessageFragment
            Bundle bundle = new Bundle();
            bundle.putString("message", message);


            // Find the existing MessageFragment instance by tag
            MessageFragment messageFragment = (MessageFragment) getSupportFragmentManager().findFragmentByTag("messageFragment");
            if (messageFragment == null) {
                // If the fragment is not found, create a new instance
                messageFragment = new MessageFragment();
                messageFragment.setArguments(bundle);
                // Add the MessageFragment to the back stack with a tag
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, messageFragment, "messageFragment")
                        .addToBackStack(null) // Optional: Add to back stack
                        .commit();
            } else {
                // If the fragment is found, update its arguments and display it
                messageFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, messageFragment)
                        .commit();
            }
        }
    }



    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
