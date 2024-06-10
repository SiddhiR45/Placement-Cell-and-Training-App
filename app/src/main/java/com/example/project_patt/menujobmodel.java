package com.example.project_patt;

public class menujobmodel {
    private String job1;
    private String sal;
    private String com;
    private String loc;

    public menujobmodel() {
    }

    public menujobmodel(String job1, String sal, String com, String loc) {
        this.job1 = job1;
        this.sal = sal;
        this.com = com;
        this.loc = loc;

    }

    public String getJob1() {
        return job1;
    }

    public void setJob1(String job1) {
        this.job1 = job1;
    }

    public String getSal() {
        return sal;
    }

    public void setSal(String sal) {
        this.sal = sal;
    }

    public String getcom() {
        return com;
    }

    public void setCom(String com) {
        this.com = com;
    }
    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }
}

