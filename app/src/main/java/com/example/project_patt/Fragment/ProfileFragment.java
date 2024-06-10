package com.example.project_patt.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.project_patt.LoginActivity;
import com.example.project_patt.MainActivity;
import com.example.project_patt.R;
import com.example.project_patt.UpdateProfileActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class ProfileFragment extends Fragment {

    private TextView textviewWelcome, textviewFullname, textviewEmail, textviewDob, textviewGender, textviewMobile;
    private ProgressBar progressBar;
    private ImageView imageView;
    private FirebaseAuth authProfile;
    private Context mContext;
    private static final int PICK_FILE_REQUEST_CODE = 1;
    private Button buttonUploadCV;
    private Button buttonViewCV;
    private Uri selectedFileUri;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        mContext = getContext(); // Store the context for later use
        setHasOptionsMenu(true); // Enable options menu for fragment

        // Initialize views
        textviewWelcome = view.findViewById(R.id.textView_show_welcome);
        textviewFullname = view.findViewById(R.id.textView_show_full_name);
        textviewEmail = view.findViewById(R.id.textView_show_email);
        textviewDob = view.findViewById(R.id.textView_show_dob);
        textviewGender = view.findViewById(R.id.textView_show_gender);
        textviewMobile = view.findViewById(R.id.textView_show_mobile);
        progressBar = view.findViewById(R.id.progress_bar);
        imageView = view.findViewById(R.id.imageView_profile_dp);
        buttonUploadCV = view.findViewById(R.id.button_upload_cv);
        buttonViewCV = view.findViewById(R.id.button_view_cv);

        // Set click listener for image view
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, UploadProfilePicActivity.class);
                startActivity(intent);

            }
        });

        // Set click listener for upload CV button
        buttonUploadCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();

            }
        });

        // Set click listener for view CV button
        buttonViewCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewCV();

            }
        });

        // Initialize Firebase authentication
        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();

        if (firebaseUser != null) {
            checkIfEmailVerified(firebaseUser);
            progressBar.setVisibility(View.VISIBLE);
            showUserProfile(firebaseUser);
        } else {
            Toast.makeText(getActivity(), "Something went wrong! User details are not available at the moment", Toast.LENGTH_LONG).show();
        }

        return view;
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*"); // Allow any file type to be selected
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, PICK_FILE_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == MainActivity.RESULT_OK && data != null && data.getData() != null) {
            selectedFileUri = data.getData();
            // Handle the selected file URI as needed
            // You can upload the file to Firebase Storage or perform any other operation
            Toast.makeText(getContext(), "File selected: " + selectedFileUri.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void checkIfEmailVerified(FirebaseUser firebaseUser) {
        if (!firebaseUser.isEmailVerified()) {
            showAlertDialog();
        }
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Email is not verified");
        builder.setMessage("Please verify your email now. You cannot login without email verification next time.");

        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showUserProfile(FirebaseUser firebaseUser) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Registered Users").child(firebaseUser.getUid());

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String fullName = firebaseUser.getDisplayName();
                    String email = firebaseUser.getEmail();
                    String dob = dataSnapshot.child("dob").getValue(String.class);
                    String gender = dataSnapshot.child("gender").getValue(String.class);
                    String mobile = dataSnapshot.child("mobile").getValue(String.class);

                    textviewWelcome.setText("Welcome, " + fullName);
                    textviewFullname.setText(fullName);
                    textviewEmail.setText(email);
                    textviewDob.setText(dob);
                    textviewGender.setText(gender);
                    textviewMobile.setText(mobile);

                    // Load profile picture using Picasso
                    Uri uri = firebaseUser.getPhotoUrl();
                    Picasso.get().load(uri).into(imageView);
                } else {
                    Toast.makeText(mContext, "User data not found", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(mContext, "Error retrieving user data", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void viewCV() {
        if (selectedFileUri != null) {
            // Show progress indicator
            progressBar.setVisibility(View.VISIBLE);

            // Perform file upload operation asynchronously
            uploadFile(selectedFileUri);
        } else {
            showToast("No CV uploaded");
        }
    }

    private void uploadFile(Uri fileUri) {
        // Create a reference to Firebase Storage
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();

        // Generate a unique filename for the CV file
        String fileName = UUID.randomUUID().toString();

        // Create a reference for the CV file in Firebase Storage
        StorageReference cvRef = storageRef.child("cv/" + fileName);

        // Upload the file to Firebase Storage
        cvRef.putFile(fileUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Retrieve the download URL of the uploaded file
                    getDownloadUrl(cvRef);
                })
                .addOnFailureListener(e -> {
                    // Handle upload failure
                    showToast("CV upload failed: " + e.getMessage());
                    // Hide progress indicator
                    progressBar.setVisibility(View.GONE);
                });
    }

    private void getDownloadUrl(StorageReference cvRef) {
        // Retrieve the download URL of the uploaded file
        cvRef.getDownloadUrl()
                .addOnSuccessListener(uri -> {
                    // Open the URL in a browser or a PDF viewer
                    Intent viewIntent = new Intent(Intent.ACTION_VIEW);
                    viewIntent.setData(uri);
                    startActivity(viewIntent);

                    // Hide progress indicator
                    progressBar.setVisibility(View.GONE);
                })
                .addOnFailureListener(e -> {
                    // Handle retrieval failure
                    showToast("Failed to retrieve CV: " + e.getMessage());
                    // Hide progress indicator
                    progressBar.setVisibility(View.GONE);
                });
    }

    private void showToast(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.commom_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_refresh) {
            // Reload the current fragment
            FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
            ft.detach(this).attach(this).commit();
            return true;
        } else if (id == R.id.menu_update_profile) {
            Intent intent = new Intent(mContext, UpdateProfileActivity.class);
            startActivity(intent);
            return true;
        } else if (id==R.id.menu_update_email) {
            Intent intent = new Intent(mContext, UpdateEmailActivity.class);
            startActivity(intent);
            return true;
        } else if (id==R.id.menu_delete_profile) {
            Intent intent=new Intent(mContext,DeleteProfileActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.menu_logout) {
            authProfile.signOut();
            Toast.makeText(mContext, "Logged out", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(mContext, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            getActivity().finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

