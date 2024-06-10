package com.example.project_patt;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project_patt.Fragment.DeleteProfileActivity;
import com.example.project_patt.Fragment.ProfileFragment;
import com.example.project_patt.Fragment.ReadWriteUserDetails;
import com.example.project_patt.Fragment.UpdateEmailActivity;
import com.example.project_patt.Fragment.UploadProfilePicActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdateProfileActivity extends AppCompatActivity {

    private EditText editTextUpdateName,editTextUpdateDob,editTextUpdateMobile;
    private RadioGroup radioGroupUpdateGender;
    private RadioButton radioButtonUpdateGenderSelected;
    private String txtFullName,txtDob,txtGender,txtMobile;
    private FirebaseAuth authProfile;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);


        getSupportActionBar().setTitle("Update profile Details");

        progressBar=findViewById(R.id.progressBar);
        editTextUpdateName=findViewById(R.id.editText_update_profile_name);
        editTextUpdateDob=findViewById(R.id.editText_update_profile_dob);
        editTextUpdateMobile=findViewById(R.id.editText_update_profile_mobile);

        radioGroupUpdateGender=findViewById(R.id.radio_group_update_profile_gender);
        authProfile=FirebaseAuth.getInstance();
        FirebaseUser firebaseUser=authProfile.getCurrentUser();

        showProfile(firebaseUser);

        TextView txtUploadProfilepic=findViewById(R.id.textView_profile_upload_pic);
        txtUploadProfilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(UpdateProfileActivity.this, UploadProfilePicActivity.class);
                startActivity(intent);
                finish();

            }
        });
        TextView txtUpdateEmail=findViewById(R.id.textView_profile_update_email);
        txtUpdateEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(UpdateProfileActivity.this, UpdateEmailActivity.class);
                startActivity(intent);
                finish();

            }
        });

        //setting up datepicker on edit text
        editTextUpdateDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String txtSADoB[]=txtDob.split("/");

                int day = Integer.parseInt(txtSADoB[0]);
                int month=Integer.parseInt(txtSADoB[1])-1;
                int year=Integer.parseInt(txtSADoB[2]);

                DatePickerDialog picker;

                //Date picker Dialog
                picker=new DatePickerDialog(UpdateProfileActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayofmonth) {
                        editTextUpdateDob.setText(dayofmonth+"/"+(month+1)+"/"+year);
                    }
                }, year,month,day);
                picker.show();

            }
        });

        Button btnUpdateProfile=findViewById(R.id.button_update_profile);
        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                updateProfile(firebaseUser);


            }
        });

    }
    private void updateProfile(FirebaseUser firebaseUser) {
        int selectedGenderID=radioGroupUpdateGender.getCheckedRadioButtonId();
        radioButtonUpdateGenderSelected=findViewById(selectedGenderID);

        //validate mobile no using regex
        String mobileRegex="[6-9][0-9]{9}";
        Matcher mobileMatcher;
        Pattern mobilePattern=Pattern.compile(mobileRegex);
        mobileMatcher=mobilePattern.matcher(txtMobile);

        if(TextUtils.isEmpty(txtFullName)){
            Toast.makeText(UpdateProfileActivity.this,"Please enter your full name",Toast.LENGTH_LONG).show();
            editTextUpdateName.setError("Full name is required");
            editTextUpdateName.requestFocus();

        } else if (TextUtils.isEmpty(txtDob)) {
            Toast.makeText(UpdateProfileActivity.this,"Please enter your Date of birth",Toast.LENGTH_LONG).show();
            editTextUpdateDob.setError("Date of Birth is required");
            editTextUpdateDob.requestFocus();

        } else if (TextUtils.isEmpty(radioButtonUpdateGenderSelected.getText())) {
            Toast.makeText(UpdateProfileActivity.this,"Please select your gender",Toast.LENGTH_LONG).show();
            radioButtonUpdateGenderSelected.setError("Gender is required");
            radioButtonUpdateGenderSelected.requestFocus();

        } else if (TextUtils.isEmpty(txtMobile)) {
            Toast.makeText(UpdateProfileActivity.this,"Please enter your Mobile Number",Toast.LENGTH_LONG).show();
            editTextUpdateMobile.setError("Mobile no. is required");
            editTextUpdateMobile.requestFocus();

        } else if (txtMobile.length()!=10) {
            Toast.makeText(UpdateProfileActivity.this, "Please re-enter your Mobile Number", Toast.LENGTH_LONG).show();
            editTextUpdateMobile.setError("Mobile no. should be 10 digits");
            editTextUpdateMobile.requestFocus();

        }else {
            txtFullName = editTextUpdateName.getText().toString();
            txtDob = editTextUpdateDob.getText().toString();
            txtMobile = editTextUpdateMobile.getText().toString();
            txtGender = radioButtonUpdateGenderSelected.getText().toString();

            // Database update and display name change
            DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");
            String userID = firebaseUser.getUid();
            progressBar.setVisibility(View.VISIBLE);

            referenceProfile.child(userID).setValue(new ReadWriteUserDetails(txtFullName, txtDob, txtGender, txtMobile)).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        // Update display name
                        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder().setDisplayName(txtFullName).build();
                        firebaseUser.updateProfile(profileUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(UpdateProfileActivity.this, "Update Successfully", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(UpdateProfileActivity.this, ProfileFragment.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(UpdateProfileActivity.this, "Failed to update display name", Toast.LENGTH_SHORT).show();
                                }
                                progressBar.setVisibility(View.GONE);
                            }
                        });
                    } else {
                        Toast.makeText(UpdateProfileActivity.this, "Failed to update profile details", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }
            });
        }
    }

    private void showProfile(FirebaseUser firebaseUser) {
        String userIDofRegistered = firebaseUser.getUid();

        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");
        progressBar.setVisibility(View.VISIBLE);

        referenceProfile.child(userIDofRegistered).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails readWriteUserDetails = snapshot.getValue(ReadWriteUserDetails.class);
                if (readWriteUserDetails != null) {
                    txtFullName =firebaseUser.getDisplayName();
                    txtDob = readWriteUserDetails.getDob();
                    txtGender = readWriteUserDetails.getGender();
                    txtMobile = readWriteUserDetails.getMobile();

                    editTextUpdateName.setText(txtFullName);
                    editTextUpdateDob.setText(txtDob);
                    editTextUpdateMobile.setText(txtMobile);

                    if (txtGender != null) {
                        if (txtGender.equals("Male")) {
                            radioButtonUpdateGenderSelected = findViewById(R.id.radio_male);
                        } else if (txtGender.equals("Female")) {
                            radioButtonUpdateGenderSelected = findViewById(R.id.radio_female);
                        }

                        if (radioButtonUpdateGenderSelected != null) {
                            radioButtonUpdateGenderSelected.setChecked(true);
                        }
                    }
                } else {
                    Toast.makeText(UpdateProfileActivity.this, "User details not found", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateProfileActivity.this, "Error retrieving user details", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.commom_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_refresh) {
            startActivity(getIntent());
            finish();
            overridePendingTransition(0,0);
            // Handle refresh action
        } else if (id == R.id.menu_update_profile ) {
            Intent intent = new Intent(UpdateProfileActivity.this, UpdateProfileActivity.class); // Use "this" to refer to the current activity context
            startActivity(intent);
            finish();
        } else if (id == R.id.menu_update_email) {
            Intent intent = new Intent(UpdateProfileActivity.this, UpdateEmailActivity.class); // Use "this" to refer to the current activity context
            startActivity(intent);
            finish();
        } else if (id == R.id.menu_delete_profile) {
            Intent intent = new Intent(UpdateProfileActivity.this, DeleteProfileActivity.class); // Use "this" to refer to the current activity context
            startActivity(intent);
            finish();
        } else if (id == R.id.menu_logout) {
            authProfile.signOut();
            Toast.makeText(UpdateProfileActivity.this, "Logged out", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, MainActivity.class); // Use "this" to refer to the current activity context
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); // Finish the hosting activity
        } else {
            Toast.makeText(UpdateProfileActivity.this, "Something went wrong!", Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }
}