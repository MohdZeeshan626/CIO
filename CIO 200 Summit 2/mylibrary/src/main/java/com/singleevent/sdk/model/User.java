package com.singleevent.sdk.model;


import com.singleevent.sdk.Custom_View.Letter.LetterSectionListItem;

import java.io.Serializable;

/**
 * Created by Admin on 5/9/2017.
 */

public class User extends LetterSectionListItem implements Serializable {

    String profile_pic;
    String first_name;
    String last_name;
    String designation;
    String company;
    public String userid;
    String email;
    String city;
    int color;
    int count = 0;
    boolean newmessage = false;
    String description;
    String user_blog;
    String website;
    private String attendee_option="1";

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUser_blog() {
        return user_blog;
    }

    public void setUser_blog(String user_blog) {
        this.user_blog = user_blog;
    }

    public User(String profile_pic, String first_name, String last_name, String designation, String company, String userid, String email,String city) {
        this.profile_pic = profile_pic;
        this.first_name = first_name;
        this.last_name = last_name;
        this.designation = designation;
        this.company = company;
        this.userid = userid;
        this.email = email;
        this.city=city;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getCity() {
        return city;
    }
    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isNewmessage() {
        return newmessage;
    }

    public void setNewmessage(boolean newmessage) {
        this.newmessage = newmessage;
    }

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

    @Override
    public String getUniqueId() {
        return userid;
    }

    @Override
    public void calculateSortString() {
        String sortString = "";

        if (first_name.toUpperCase().startsWith("THE ")) {
            sortString = first_name.toUpperCase().replaceFirst("THE ", "").trim();
        } else if (first_name.toUpperCase().startsWith("A ")) {
            sortString = first_name.toUpperCase().replaceFirst("A ", "").trim();
        } else {
            sortString = first_name.toUpperCase().trim();
        }

        sortString += ((company == null ? "" : company.trim()) + (designation == null ? "" : designation.trim()));

        setSortString(sortString);
    }

    public String getAttendee_option() {
        return attendee_option;
    }

    public void setAttendee_option(String attendee_option) {
        this.attendee_option = attendee_option;
    }
}

