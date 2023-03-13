package com.webmobi.gecmedia.Models;

import java.io.Serializable;

/**
 * Created by Admin on 5/9/2017.
 */

public class User implements Serializable {

    String profile_pic;
    String first_name;
    String last_name;
    String designation;
    String company;
    String userid;
    String email;


    public String getProfile_pic() {
        if (profile_pic == null)
            return "";
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getDesignation() {
        if (designation == null)
            return "";
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getCompany() {
        if (company == null)
            return "";
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (this.getClass() != obj.getClass()) {
            return false;
        }

        User employeeId = (User) obj;

        if (this.userid.equalsIgnoreCase(employeeId.userid)) {
            return true;
        }
        return false;
    }
}

