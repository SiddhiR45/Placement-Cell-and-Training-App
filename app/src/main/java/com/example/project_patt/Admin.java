package com.example.project_patt;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.project_patt.Fragment.AddJobFragment;
import com.example.project_patt.Fragment.AddStudyFragment;
import com.example.project_patt.Fragment.Userdata;
import com.example.project_patt.Fragment.admin_profile;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import chats.ChatFragment;

public class Admin extends AppCompatActivity {

    private BottomNavigationView bottomNavigation1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);


        bottomNavigation1 = findViewById(R.id.bottomNavigation);
        bottomNavigation1.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.addjob) {
                selectedFragment = new AddJobFragment();
            } else if (item.getItemId() == R.id.chatFragment) {
                selectedFragment = new ChatFragment();
            } else if (item.getItemId() == R.id.addstudyFragment) {
                selectedFragment = new AddStudyFragment();
            } else if (item.getItemId() == R.id.userdataFragment) {
                selectedFragment = new Userdata();
            }else if (item.getItemId() == R.id.adminprofile) {
                selectedFragment = new admin_profile();
            }

            loadFragment(selectedFragment);
            return true;
        });

    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container1, fragment)
                .commit();
    }
}