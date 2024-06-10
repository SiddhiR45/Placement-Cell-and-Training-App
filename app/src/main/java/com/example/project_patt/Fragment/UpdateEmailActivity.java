package com.example.project_patt.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.project_patt.LoginActivity;
import com.example.project_patt.MainActivity;
import com.example.project_patt.R;
import com.example.project_patt.UpdateProfileActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UpdateEmailActivity extends AppCompatActivity {

    private FirebaseAuth authProfile;
    private FirebaseUser firebaseUser;
    private ProgressBar progressBar;
    private TextView textViewAuthenticated;
    private String userOldEmail,userNewEmail,userPwd;
    private Button btnUpdateEmail;
    private EditText editTextNewEmail,edittxtPwd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_email);

        getSupportActionBar().setTitle("Update Email");

        progressBar=findViewById(R.id.progressBar);
        edittxtPwd=findViewById(R.id.editText_update_email_verify_password);
        editTextNewEmail=findViewById(R.id.editText_update_email_new);
        textViewAuthenticated=findViewById(R.id.textView_update_email_authenticated);
        btnUpdateEmail=findViewById(R.id.button_update_email);

        btnUpdateEmail.setEnabled(false);
        editTextNewEmail.setEnabled(false);

        authProfile=FirebaseAuth.getInstance();
        firebaseUser=authProfile.getCurrentUser();

        userOldEmail=firebaseUser.getEmail();
        TextView txtViewOldEmail=findViewById(R.id.textView_update_email_old);
        txtViewOldEmail.setText(userOldEmail);

        if(firebaseUser.equals("")){
            Toast.makeText(UpdateEmailActivity.this, "Something went wrong!", Toast.LENGTH_LONG).show();
        }else{
            reAuthenticate(firebaseUser);
        }
    }

    private void reAuthenticate(FirebaseUser firebaseUser) {
        Button btnverifyUser=findViewById(R.id.button_authenticate_user);
        btnverifyUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userPwd=edittxtPwd.getText().toString();

                if(TextUtils.isEmpty(userPwd)){
                    Toast.makeText(UpdateEmailActivity.this,"Password is needed to contiue",Toast.LENGTH_LONG).show();
                    edittxtPwd.setError("Please enter your password to authenticate");
                    edittxtPwd.requestFocus();
                }else{
                    progressBar.setVisibility(View.VISIBLE);

                    AuthCredential credential= EmailAuthProvider.getCredential(userOldEmail,userPwd);

                    firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                progressBar.setVisibility(View.GONE);

                                Toast.makeText(UpdateEmailActivity.this,"Password has been verified."+"You can update email now",Toast.LENGTH_LONG).show();

                                textViewAuthenticated.setText("You are authenticated.you can update your email now.");

                                editTextNewEmail.setEnabled(true);
                                edittxtPwd.setEnabled(false);
                                btnverifyUser.setEnabled(false);
                                btnUpdateEmail.setEnabled(true);

                                btnUpdateEmail.setBackgroundTintList((ContextCompat.getColorStateList(UpdateEmailActivity.this,R.color.green)));

                                btnUpdateEmail.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        userNewEmail=editTextNewEmail.getText().toString();
                                        if(TextUtils.isEmpty(userNewEmail)){
                                            Toast.makeText(UpdateEmailActivity.this,"New Email is required",Toast.LENGTH_LONG).show();
                                            editTextNewEmail.setError("Please enter new email");
                                            editTextNewEmail.requestFocus();
                                        } else if (!Patterns.EMAIL_ADDRESS.matcher(userNewEmail).matches()) {
                                            Toast.makeText(UpdateEmailActivity.this, "New Email is required", Toast.LENGTH_LONG).show();
                                            editTextNewEmail.setError("Please enter valid email");
                                            editTextNewEmail.requestFocus();
                                        }else if(userOldEmail.matches(userNewEmail)){
                                            Toast.makeText(UpdateEmailActivity.this,"New Email cannot be same as old email",Toast.LENGTH_LONG).show();
                                            editTextNewEmail.setError("Please enter new email");
                                            editTextNewEmail.requestFocus();
                                        }else{
                                            progressBar.setVisibility(View.VISIBLE);
                                            updateEmail(firebaseUser);
                                        }

                                    }
                                });
                            }else{
                                try {
                                    throw task.getException();
                                }catch (Exception e){
                                    Toast.makeText(UpdateEmailActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                                }
                            }
                        }

                    });
                }

            }
        });
    }

    private void updateEmail(FirebaseUser firebaseUser) {
        firebaseUser.updateEmail(userNewEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    firebaseUser.sendEmailVerification();
                    Toast.makeText(UpdateEmailActivity.this,"Email has been updated.Please verify your new email",Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(UpdateEmailActivity.this,ProfileFragment.class);
                    startActivity(intent);
                    finish();
                }else {
                    try {
                        throw task.getException();
                    }catch (Exception e){
                        Toast.makeText(UpdateEmailActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
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
            Intent intent = new Intent(UpdateEmailActivity.this, UpdateProfileActivity.class); // Use "this" to refer to the current activity context
            startActivity(intent);
            finish();
        } else if (id == R.id.menu_update_email) {
            Intent intent = new Intent(UpdateEmailActivity.this, UpdateEmailActivity.class); // Use "this" to refer to the current activity context
            startActivity(intent);
            finish();
        } else if (id == R.id.menu_delete_profile) {
            Intent intent = new Intent(UpdateEmailActivity.this, DeleteProfileActivity.class); // Use "this" to refer to the current activity context
            startActivity(intent);
            finish();
        } else if (id == R.id.menu_logout) {
            authProfile.signOut();
            Toast.makeText(UpdateEmailActivity.this, "Logged out", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, LoginActivity.class); // Use "this" to refer to the current activity context
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); // Finish the hosting activity
        } else {
            Toast.makeText(UpdateEmailActivity.this, "Something went wrong!", Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }
}