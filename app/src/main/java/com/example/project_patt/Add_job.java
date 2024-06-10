package com.example.project_patt;
import java.io.Serializable;
public class Add_job implements Serializable{
    private String jobId;
    private String jobName;
    private String companyName;
    private String salary;
    private String location;
    private String description;
    private String Highlight;


    public Add_job() {
        // Default constructor required for calls to DataSnapshot.getValue(Job.class)
    }

    public Add_job( String companyName,String jobId,String jobName, String location, String salary,String description,String Highlight) {
        this.companyName = companyName;
        this.jobId = jobId;
        this.jobName = jobName;
        this.location = location;
        this.salary = salary;
        this.description=description;
        this.Highlight=Highlight;
    }



    public String getJobId() {
        return jobId;
    }

    public String getJobName() {
        return jobName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getSalary() {
        return salary;
    }

    public String getLocation() {
        return location;
    }
    public String getDescription(){return description;}
    public String getHighlight(){
        return Highlight;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public void setLocation(String location) {
        this.location = location;
    }
    public void setDescription(String description){
        this.description=description;
    }
    public void setHighlight(String highlight){
        this.Highlight=highlight;
    }

}
