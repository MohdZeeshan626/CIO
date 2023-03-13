package com.webmobi.gecmedia.Config;

/**
 * Created by Admin on 1/9/2017.
 */

public class ApiList {

    public static final String REGISTRATION_COMPLETE = "registrationComplete";

    public static final String PUSH_NOTIFICATION = "pushNotification";

    public static String loginaction = "com.useraction";

    public static String EventCategory = "com.eventcategory";


    public static String Eventrecom = "com.eventrecommend";
  public static  final String Event_ID="a5d72f633c6f8a0e964525123a500376a26d";


  //  public static String dom = "http://104.131.76.15:3000";
 //public static String dom = "https://check1.webmobi.in";

    private static String dom = "https://api.webmobi.com";

  public static String multievent_Email_login=dom + "/api/event/multievent_login";
  public static String Container_login=dom + "/api/event/container_login";
  //  public static String chatdom = "http://104.131.76.15:3030";
  public static String chatdom = "https://chat.webmobi.com";
  public static String DeviceLog = dom + "/api/event/device_log";

    public static String Register_User = dom + "/api/user/register";
  public static String GetContainerInfo = dom + "/api/event/getContainerEvents";

    public static String Login_User = dom + "/api/user/login";

    public static String Forgot_Password = dom + "/api/user/forgot_password";

    public static String Social_Media_Login = dom + "/api/user/social_media_connect";


    public static String Nearby = dom + "/api/event/nearby_events";

    public static String Popular = dom + "/api/event/popular_events";

    public static String discovereventsregister = dom + "/api/user/discovery_register";

    public static String DiscoveryUsers = dom + "/api/user/discovery_app_users?appid=";

    public static String Category = dom + "/api/event/category_events?category=";
  public static String MultiEvents = dom + "/api/event/multiple_events?event_id=";

    public static String RecommendEvents = dom + "/api/event/recommended_events?";

    public static String SearchEvents = dom + "/api/event/event_search?q=";

    public static String PSearchEvents1 = dom + "/api/event/multi_private_search?q=";


    public static String DeviceApp = dom + "/api/user/device_app";

    public static String GetEvents = dom + "/api/event/getEvent";


    // id to handle the notification in the notification tray
    public static final int NOTIFICATION_ID = 100;
    public static final int Normal_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;

    public static String Single_event_login = dom + "/api/user/single_event_login";
    public static final String LOCALSTORAGE = "MultiEvent";
}
