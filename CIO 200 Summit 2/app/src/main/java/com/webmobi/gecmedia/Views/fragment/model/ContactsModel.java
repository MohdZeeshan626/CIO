package com.webmobi.gecmedia.Views.fragment.model;

import java.io.Serializable;

/**
 * Created by webMOBI on 12/11/2017.
 */

public class ContactsModel implements Serializable {

    private String name;
    private String contact_first_name;
    private String contact_last_name;

    public String getContact_first_name() {
        return contact_first_name;
    }

    public void setContact_first_name(String contact_first_name) {
        this.contact_first_name = contact_first_name;
    }

    public String getContact_last_name() {
        return contact_last_name;
    }

    public void setContact_last_name(String contact_last_name) {
        this.contact_last_name = contact_last_name;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getContact_phone() {
        return contact_phone;
    }

    public void setContact_phone(String contact_phone) {
        this.contact_phone = contact_phone;
    }

    public String getContact_email() {
        return contact_email;
    }

    public void setContact_email(String contact_email) {
        this.contact_email = contact_email;
    }

    private String website;


    private String contactId;
    private String userId;
    private String description;
    private String contact_phone;
    private String appId;
    private String imageUrl;
    private String address;
    private String contact_email;
    private String designation;
    private String company;
    private String contact_email_1;
    private String contact_phone_1;
    private String website_1;
    private String card_type;
    private String fax;
    private String contact_info;
    private String card_image_url;

    public String getContact_email_1() {
        return contact_email_1;
    }

    public void setContact_email_1(String contact_email_1) {
        this.contact_email_1 = contact_email_1;
    }

    public String getContact_phone_1() {
        return contact_phone_1;
    }

    public void setContact_phone_1(String contact_phone_1) {
        this.contact_phone_1 = contact_phone_1;
    }

    public String getWebsite_1() {
        return website_1;
    }

    public void setWebsite_1(String website_1) {
        this.website_1 = website_1;
    }

    public String getCard_type() {
        return card_type;
    }

    public void setCard_type(String card_type) {
        this.card_type = card_type;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getCard_image_url() {
        return card_image_url;
    }

    public void setCard_image_url(String card_image_url) {
        this.card_image_url = card_image_url;
    }

    public String getContact_info() {
        return contact_info;
    }

    public void setContact_info(String contact_info) {
        this.contact_info = contact_info;
    }

    public String getEmail() {
        return contact_email;
    }

    public void setEmail(String email) {
        this.contact_email = email;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public ContactsModel() {
    }

    public ContactsModel(String name, String contactId, String userId, String description, String mob, String appId, String imageUrl, String address) {
        this.name = name;
        this.contactId = contactId;
        this.userId = userId;
        this.description = description;
        this.contact_phone = mob;
        this.appId = appId;
        this.imageUrl = imageUrl;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMob() {
        return contact_phone;
    }

    public void setMob(String mob) {
        this.contact_phone = mob;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
