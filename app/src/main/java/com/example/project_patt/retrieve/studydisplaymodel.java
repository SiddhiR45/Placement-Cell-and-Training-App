package com.example.project_patt.retrieve;

public class studydisplaymodel {
    private String studyType;
    private String courseName;
    private String studyLink;

    public studydisplaymodel() {
        // Default constructor required for Firebase
    }

    public studydisplaymodel(String studyType, String courseName, String studyLink) {
        this.studyType = studyType;
        this.courseName = courseName;
        this.studyLink = studyLink;
    }

    public String getStudyType() {
        return studyType;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getStudyLink() {
        return studyLink;
    }
}
