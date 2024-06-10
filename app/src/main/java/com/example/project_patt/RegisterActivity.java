package com.example.project_patt;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project_patt.Fragment.CommunicationFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextRegisterFullName,editTextRegisterEmail,editTextRegisterDoB,editTextRegisterMobile,
            editTextRegisterPwd,editTextRegisterConfirmPwd;
    private ProgressBar progressBar;
    private RadioGroup radioGroupRegisterGender;
    private RadioButton radioButtonRegisterGenderSelected;
    private DatePickerDialog picker;
    private static final String TAG="RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().setTitle("Register");
        Toast.makeText(RegisterActivity.this,"You can register now",Toast.LENGTH_LONG).show();
        progressBar=findViewById(R.id.progressBar);
        editTextRegisterFullName=findViewById(R.id.editText_register_full_name);
        editTextRegisterEmail=findViewById(R.id.editText_register_email);
        editTextRegisterDoB=findViewById(R.id.editText_register_dob);
        editTextRegisterMobile=findViewById(R.id.editText_register_mobile);
        editTextRegisterPwd=findViewById(R.id.editText_register_password);
        editTextRegisterConfirmPwd=findViewById(R.id.editText_register_confirm_password);

        //Radio button for gender
        radioGroupRegisterGender=findViewById(R.id.radio_group_register_gender);
        radioGroupRegisterGender.clearCheck();
        ImageView imageViewShowHidePwd = findViewById(R.id.imageView_show_hide_pwd);
        imageViewShowHidePwd.setImageResource(R.drawable.ic_hide_pwd);
        imageViewShowHidePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editTextRegisterPwd.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())) {
                    //if pwd is visible
                    editTextRegisterPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    //change icon
                    imageViewShowHidePwd.setImageResource(R.drawable.ic_hide_pwd);
                } else {
                    editTextRegisterPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    imageViewShowHidePwd.setImageResource(R.drawable.ic_show_pwd);
                }

            }
        });

        //setting up datepicker on edit text
        editTextRegisterDoB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar=Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month=calendar.get(Calendar.MONTH);
                int year=calendar.get(Calendar.YEAR);

                //Date picker Dialog
                picker=new DatePickerDialog(RegisterActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayofmonth) {
                        editTextRegisterDoB.setText(dayofmonth+"/"+(month+1)+"/"+year);
                    }
                }, year,month,day);
                picker.show();

            }
        });

        Button buttonRegister=findViewById(R.id.button_register);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                int selectedGenderID=radioGroupRegisterGender.getCheckedRadioButtonId();
                radioButtonRegisterGenderSelected=findViewById(selectedGenderID);

                //obtain the entered data
                String textFullName=editTextRegisterFullName.getText().toString();
                String textEmail=editTextRegisterEmail.getText().toString();
                String textDob=editTextRegisterDoB.getText().toString().trim();
                String textMobile=editTextRegisterMobile.getText().toString().trim();
                String textPwd=editTextRegisterPwd.getText().toString().trim();
                String textConfirmPwd=editTextRegisterConfirmPwd.getText().toString();
                String textGender;

                //validate mobile no using regex
                String mobileRegex="[6-9][0-9]{9}";
                Matcher mobileMatcher;
                Pattern mobilePattern=Pattern.compile(mobileRegex);
                mobileMatcher=mobilePattern.matcher(textMobile);

                if(TextUtils.isEmpty(textFullName)){
                    Toast.makeText(RegisterActivity.this,"Please enter your full name",Toast.LENGTH_SHORT).show();
                    editTextRegisterFullName.setError("Full name is required");
                    editTextRegisterFullName.requestFocus();

                } else if (TextUtils.isEmpty(textEmail)) {
                    Toast.makeText(RegisterActivity.this,"Please enter your Email",Toast.LENGTH_SHORT).show();
                    editTextRegisterEmail.setError("Email is required");
                    editTextRegisterEmail.requestFocus();

                } else if (!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()) {
                    Toast.makeText(RegisterActivity.this,"Please re-enter your Email",Toast.LENGTH_SHORT).show();
                    editTextRegisterEmail.setError("Valid Email is required");
                    editTextRegisterEmail.requestFocus();

                } else if (TextUtils.isEmpty(textDob)) {
                    Toast.makeText(RegisterActivity.this,"Please enter your Date of birth",Toast.LENGTH_SHORT).show();
                    editTextRegisterDoB.setError("Date of Birth is required");
                    editTextRegisterDoB.requestFocus();

                } else if (radioGroupRegisterGender.getCheckedRadioButtonId()==-1) {
                    Toast.makeText(RegisterActivity.this,"Please select your gender",Toast.LENGTH_SHORT).show();
                    radioButtonRegisterGenderSelected.setError("Gender is required");
                    radioButtonRegisterGenderSelected.requestFocus();

                } else if (TextUtils.isEmpty(textMobile)) {
                    Toast.makeText(RegisterActivity.this,"Please enter your Mobile Number",Toast.LENGTH_SHORT).show();
                    editTextRegisterMobile.setError("Mobile no. is required");
                    editTextRegisterMobile.requestFocus();

                } else if (textMobile.length()!=10) {
                    Toast.makeText(RegisterActivity.this,"Please re-enter your Mobile Number",Toast.LENGTH_SHORT).show();
                    editTextRegisterMobile.setError("Mobile no. should be 10 digits");
                    editTextRegisterMobile.requestFocus();

                } else if (!mobileMatcher.find()) {
                    Toast.makeText(RegisterActivity.this,"Please re-enter your Mobile Number",Toast.LENGTH_SHORT).show();
                    editTextRegisterMobile.setError("Mobile number is not valid");
                    editTextRegisterMobile.requestFocus();

                } else if (TextUtils.isEmpty(textPwd)) {
                    Toast.makeText(RegisterActivity.this,"Please enter your Password",Toast.LENGTH_SHORT).show();
                    editTextRegisterPwd.setError("Password is required");
                    editTextRegisterPwd.requestFocus();

                } else if (textPwd.length()<6) {
                    Toast.makeText(RegisterActivity.this,"Password should be atleast 6 digits",Toast.LENGTH_SHORT).show();
                    editTextRegisterPwd.setError("Password is too weak");
                    editTextRegisterPwd.requestFocus();

                } else if (TextUtils.isEmpty(textConfirmPwd)) {
                    Toast.makeText(RegisterActivity.this,"Please confirm your password",Toast.LENGTH_SHORT).show();
                    editTextRegisterConfirmPwd.setError("Password confirmation  is required");
                    editTextRegisterConfirmPwd.requestFocus();

                } else if (!textPwd.equals(textConfirmPwd)) {
                    Toast.makeText(RegisterActivity.this,"Please enter same password ",Toast.LENGTH_SHORT).show();
                    editTextRegisterConfirmPwd.setError("Password confirmation is required");
                    editTextRegisterConfirmPwd.requestFocus();

                    editTextRegisterConfirmPwd.clearComposingText();

                }else {
                    textGender=radioButtonRegisterGenderSelected.getText().toString();
                    progressBar.setVisibility(View.VISIBLE);
                    registerUser(textFullName,textEmail,textDob,textGender,textMobile,textPwd);
                }


            }
        });
    }

    //register user using the credentials given
    private void registerUser(String textFullName, String textEmail, String textDob, String textGender, String textMobile, String textPwd) {
        FirebaseAuth auth=FirebaseAuth.getInstance();

        //create user profile
        auth.createUserWithEmailAndPassword(textEmail,textPwd).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    FirebaseUser firebaseUser=auth.getCurrentUser();


                    //update display name of user
                    UserProfileChangeRequest profileChangeRequest=new UserProfileChangeRequest.Builder().setDisplayName(textFullName).build();
                    firebaseUser.updateProfile(profileChangeRequest);

                    //enter user data into firebase realtime database
                    CommunicationFragment.ReadwriteUserDetails writeUserDetails = new CommunicationFragment.ReadwriteUserDetails(textFullName,textDob, textGender, textMobile);

                    //extracting user reference from database for "registered users"
                    DatabaseReference referenceProfile= FirebaseDatabase.getInstance().getReference("Registered Users");
                    referenceProfile.child(firebaseUser.getUid()).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()){

                                //send verification Email
                                firebaseUser.sendEmailVerification();

                                Toast.makeText(RegisterActivity.this, "User registered successfully.Please verify your email.", Toast.LENGTH_SHORT).show();


                                finish();//to close register activity
                            }else {
                                Toast.makeText(RegisterActivity.this, "User registered failed.Please try again.", Toast.LENGTH_SHORT).show();

                            }
                            progressBar.setVisibility(View.GONE);

                        }
                    });
                    if (firebaseUser != null) {
                        // Assuming you have predefined admin credentials
                        String adminEmail = "khaturahi09@gmail.com";
                        String adminPassword = "adminpwd";

                        if (textEmail.equals(adminEmail) && textPwd.equals(adminPassword)) {
                            // User is the admin
                            // Start AdminActivity
                            startActivity(new Intent(RegisterActivity.this, Admin.class));
                            finish(); // Finish LoginActivity to prevent user from going back
                        } else {
                            // User is not the admin
                            // Start MainActivity or any other activity for regular users
                            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                            finish(); // Finish LoginActivity to prevent user from going back
                        }
                    }
                }else {
                    try {
                        throw task.getException();
                    }catch (FirebaseAuthWeakPasswordException e){
                        editTextRegisterPwd.setError("Your password is too weak.Kindly use a mix of alphabet,Numbers and special characters.");
                        editTextRegisterPwd.requestFocus();
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        editTextRegisterPwd.setError("Your Email is invalid or already in use.Kindly re-enter or choose another Email.");
                        editTextRegisterPwd.requestFocus();
                    }catch (FirebaseAuthUserCollisionException e){
                        editTextRegisterPwd.setError("You are already register with this email.Use another email.");
                        editTextRegisterPwd.requestFocus();

                    }catch (Exception e){
                        Log.e(TAG,e.getMessage());
                        Toast.makeText(RegisterActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                    progressBar.setVisibility(View.GONE);
                }

            }
        });
    }

    }
