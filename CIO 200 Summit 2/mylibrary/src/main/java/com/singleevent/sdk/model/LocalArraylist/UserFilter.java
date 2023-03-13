package com.singleevent.sdk.model.LocalArraylist;

/**
 * Created by webmodi on 7/29/2016.
 */
public class UserFilter extends AddingUser {


    public UserFilter(String name, String appId, String userId, String email, String profilepic, String designation, String companyName) {
        this.name = name;
        this.appId = appId;
        this.userId = userId;
        this.email = email;
        this.profilepic = profilepic;
        this.designation=designation;
        this.companyName=companyName;
    }
}
