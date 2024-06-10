package com.example.project_patt.retrieve;
public class applyjob {
    private String jobTitle;
    private String companyName;
    private String applicantUid; // UID of the user who applied for the job
    private String username; // Name of the user who applied for the job

    // Empty constructor needed for Firebase
    public applyjob() {
    }

    // Constructor with parameters
    public applyjob(String jobTitle, String companyName, String applicantUid) {
        this.jobTitle = jobTitle;
        this.companyName = companyName;
        this.applicantUid = applicantUid;
    }

    // Getters and setters
    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getApplicantUid() {
        return applicantUid;
    }

    public void setApplicantUid(String applicantUid) {
        this.applicantUid = applicantUid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
