package com.example.project_patt.retrieve;
public class AddStudyModel {
    private String studyType;
    private String courseName;
    private String studyLink;
    private String fileName;

    public AddStudyModel() {
        // Default constructor required for calls to DataSnapshot.getValue(Study.class)
    }

    public AddStudyModel(String studyType, String courseName, String studyLink, String fileName) {
        this.studyType = studyType;
        this.courseName = courseName;
        this.studyLink = studyLink;
        this.fileName = fileName;
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

    public String getFileName() {
        return fileName;
    }
}
