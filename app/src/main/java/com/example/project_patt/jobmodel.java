package com.example.project_patt;

public class jobmodel {
    private String job;
    private int img;

    public jobmodel() {
    }

    public jobmodel(String job, int img) {
        this.job = job;
        this.img = img;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }
}
