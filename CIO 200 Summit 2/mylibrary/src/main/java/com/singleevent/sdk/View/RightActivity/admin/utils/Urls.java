package com.singleevent.sdk.View.RightActivity.admin.utils;

import static com.singleevent.sdk.ApiList.dom;

/**
 * Created by User on 10/6/2016.
 */

public class Urls {

    private static String baseUrlTest1 = "http://192.168.1.18:81";
    private static String baseUrlTest2 = "http://192.168.1.18:82";

  private static String baseUrl = /*"https://www.webmobicrm.com"*/"https://api.webmobi.com";
  //  private static String baseUrl = /*"https://www.webmobicrm.com"*/"http://104.131.76.15:3000";

    private static String loginUrl = dom +"/api/user/checkin_login";
    private static String userAppsUrl = dom + "/api/event/userapps?";
    private static String usersUrl = dom +"/api/user/discovery_app_users?";
    private static String checkInUrl = dom + "/api/user/checkin_users";

//    private static String baseUrlTest1 = "http://159.203.78.208:81";
//    private static String baseUrlTest2 = "http://159.203.78.208:82";
//
//    private static String loginUrl = baseUrlTest2 + "/api/checkinapp/v4/login";
//    private static String userAppsUrl = baseUrlTest1 + "/richpush/v1/dashboard/userapps";
//    private static String usersUrl = baseUrlTest2 + "/api/richpush/v1/getchatusers?";
//    private static String checkInUrl = baseUrlTest2 + "/api/checkinapp/v4/checkin";

    public static String getLoginUrl() {
        return loginUrl;
    }

    public static String getUserAppsUrl() {
        return userAppsUrl;
    }

    public static String getUsersUrl() {
        return usersUrl;
    }

    public static String getCheckInUrl() {
        return checkInUrl;
    }
}
