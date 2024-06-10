package com.example.project_patt.Fragment;

public class ReadWriteUserDetails {
    private String fullName;
    private String dob;
    private String gender;
    private String mobile;

    public ReadWriteUserDetails() {
        // Default constructor required for calls to DataSnapshot.getValue(ReadWriteUserDetails.class)
    }

    public ReadWriteUserDetails(String fullName,String dob, String gender, String mobile) {
        this.fullName=fullName;
        this.dob = dob;
        this.gender = gender;
        this.mobile = mobile;
    }

    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDob() {
        return dob;
    }

    public String getGender() {
        return gender;
    }

    public String getMobile() {
        return mobile;
    }
}
