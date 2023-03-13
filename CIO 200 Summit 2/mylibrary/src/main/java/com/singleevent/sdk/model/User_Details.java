package com.singleevent.sdk.model;


import android.os.Parcel;
import android.os.Parcelable;

public class User_Details implements Parcelable {


    String event_user_id;
    String email;
    String first_name;
    String last_name;
    String mobile;
    String msg;
    String company;
    String country_code;
    String country;
    String city;
    String designation;
    String profile_pic;
    String website;
    String description;
    String user_blog;

    protected User_Details(Parcel in) {
        event_user_id = in.readString();
        email = in.readString();
        msg=in.readString();
        first_name = in.readString();
        last_name = in.readString();
        mobile = in.readString();
        company = in.readString();
        country_code = in.readString();
        country = in.readString();
        city = in.readString();
        designation = in.readString();
        profile_pic = in.readString();
        website = in.readString();
        description = in.readString();
        user_blog = in.readString();
    }

    public static final Creator<User_Details> CREATOR = new Creator<User_Details>() {
        @Override
        public User_Details createFromParcel(Parcel in) {
            return new User_Details(in);
        }

        @Override
        public User_Details[] newArray(int size) {
            return new User_Details[size];
        }
    };

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

    public String getEvent_user_id() {
        return event_user_id;
    }

    public void setEvent_user_id(String event_user_id) {
        this.event_user_id = event_user_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }


    public String getMessege() {
        return msg;
    }

    public void setMessage(String msg) {
        this.msg = msg;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getMobile() {
        if (mobile == null)
            return "";
        return mobile;
    }

    public void setMobile(String mobile) {

        this.mobile = mobile;
    }

    public String getCompany() {
        if (company == null)
            return "";
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCountry_code() {
        if (country_code == null)
            return "";
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getCountry() {
        if (country == null)
            return "";
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        if (city == null)
            return "";
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDesignation() {
        if (designation == null)
            return "";

        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getProfile_pic() {
        if (profile_pic == null)
            return "";
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public String getWebsite() {
        if (website == null)
            return "";
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(event_user_id);
        parcel.writeString(email);
        parcel.writeString(first_name);
        parcel.writeString(last_name);
        parcel.writeString(mobile);
        parcel.writeString(company);
        parcel.writeString(country_code);
        parcel.writeString(country);
        parcel.writeString(city);
        parcel.writeString(designation);
        parcel.writeString(profile_pic);
        parcel.writeString(website);
        parcel.writeString(description);
        parcel.writeString(user_blog);

    }
}
