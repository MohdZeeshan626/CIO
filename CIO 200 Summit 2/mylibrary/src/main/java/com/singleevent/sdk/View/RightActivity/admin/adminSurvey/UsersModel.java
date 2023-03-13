package com.singleevent.sdk.View.RightActivity.admin.adminSurvey;

import com.singleevent.sdk.Custom_View.Letter.LetterSectionListItem;

import java.io.Serializable;

/**
 * Created by Lenovo on 23-10-2018.
 */

public class UsersModel extends LetterSectionListItem implements Serializable {
    String profile_pic;
    String first_name;
    String last_name;
    String designation;
    String company;
    String userid;
    String email;
    String admin_flag;

    public String getBeacon_id() {
        return beacon_id;
    }

    public void setBeacon_id(String beacon_id) {
        this.beacon_id = beacon_id;
    }

    String beacon_id;
    int color;
    int count = 0;
    boolean newmessage = false;
    String description;
    String user_blog;
    boolean isSelected;
    private String attendee_option="1";
    int blocked_users;
    public int getBlocked_users() {
        return blocked_users;
    }

    public void setBlocked_users(int blocked_users) {
        this.blocked_users = blocked_users;
    }

    public String getAdmin_flag() {
        return admin_flag;
    }

    public void setAdmin_flag(String admin_flag) {
        this.admin_flag = admin_flag;
    }


    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

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

    public UsersModel(String profile_pic, String first_name, String last_name, String designation, String company, String userid, String email) {
        this.profile_pic = profile_pic;
        this.first_name = first_name;
        this.last_name = last_name;
        this.designation = designation;
        this.company = company;
        this.userid = userid;
        this.email = email;
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

//        User employeeId = (User) obj;
        UsersModel employeeId = (UsersModel) obj;

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
