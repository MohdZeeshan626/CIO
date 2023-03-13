package com.singleevent.sdk;

/**
 * Created by Admin on 1/9/2017.
 */

public class ApiList {

    // global topic to receive app wide push notifications
    public static final String LOCALSTORAGE = "MultiEvent";
    public static final String TOPIC_GLOBAL = "global";
    public static char[] Confidential2 = "webmobi".toCharArray();
    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";
    public static  final String Event_ID="a5d72f633c6f8a0e964525123a500376a26d";

    // id to handle the notification in the notification tray
   /* public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;*/

 //  public static String chatdom = "http://104.131.76.15:3030";
   //public static String dom = "https://check1.webmobi.in";

    public static String dom = "https://api.webmobi.com";
    public static String chatdom = "https://chat.webmobi.com";
    public static String ChannelBroadCast = dom + "/api/user/stream_information";
    public static String loginaction = "com.useraction";
    public static String Register_User = dom + "/api/user/single_event_register";

    public static String Login_User = dom + "/api/user/single_event_login";
    public static String Floor_map=dom+ "/api/event/floor_location_detail";
   public static String GetCategory = dom +"/api/event/get_user_category?appid=";
   public static String GetLeaderDetails = dom +"/api/event/leaderboard?appid=";
    public static String MoodFeedback = dom + "/api/event/moodometer_feedback";

    public static String Multi_Event_Login_User = dom + "/api/user/login";
    public static String GetGallery = dom + "/api/event/list_image_gallery?appid=";
    public static String Forgot_Password = dom + "/api/user/forgot_password";
    public static String DeviceLog = dom + "/api/event/device_log";

    public static String Contact_Us = dom + "/api/user/contact_us";

    public static String Schdule = dom + "/api/event/manage_schedule";

    public static String Favorites = dom + "/api/event/manage_favorites";

    public static String Notes = dom + "/api/event/manage_notes";

    public static String Users = dom + "/api/user/discovery_app_users?appid=";

    public static String ChatUsers = chatdom + "/get_chat_users?appid=";

    public static String CHAT_SERVER_URL = chatdom + "/socket/con";
    public static String CHAT_SERVER_U ="https://livesocket.webmobi.com/socket/con";


    public static String ChatHistory = chatdom + "/get_chat_history?appid=";

    public static String SurveyFeedback = dom + "/api/event/appfeedback";

    public static String PollingFeedback = dom + "/api/event/pollfeedback";
    public static String Take_Enroll = dom + "/api/event/sessionRegistration";
    public static String Enroll = dom + "/api/event/get_sessionReg_detail?"+
            "userid=";

    public static String CheckUpdate = dom + "/api/event/jsonversion?";
    public static String Multi_search = dom + "/api/event/multievent_search";


    public static String GetRating = dom + "/api/event/getratings?appid=";

    public static String SetRating = dom + "/api/event/ratings";

    public static String GetProfile = dom + "/api/user/profile?userid=";

    public static String SetProfile = dom + "/api/user/update_profile";

    public static String UpdateProfilePic = dom + "/api/user/profile_photo";

    public static String Chanepwd = dom + "/api/user/change_password";

    public static String Logout = dom + "/api/user/logout";

    public static String CheckIN = dom + "/api/user/checkin";

    public static String Notification = dom + "/api/event/notifications?appid=";

    public static String Post_Feed = dom + "/api/event/post_feed";



    public static String Get_Feed = dom + "/api/event/get_feeds?appid=";

    public static String Get_Comments = dom + "/api/event/get_feed_actions?appid=";

    public static String Get_TimeSlot = dom + "/api/user/get_timeslots?appid=";

    public static String Book_TimeSlot = dom + "/api/user/post_schedule";

    public static String Get_RequestedTimeSlot = dom + "/api/user/get_pending_schedules?appid=";

    public static String SendingBookingRequest = dom + "/api/user/schedule_response";

    public static String Get_Mymeetings = dom + "/api/user/my_meetings?appid=";

    public static String Post_Contacts = dom + "/api/user/usercontactupload";
 public static String updateQRContact = dom + "/api/user/qrcode_contactupload";

    public static final String CONTACT_LIST = dom + "/api/user/contacts?" +
            "userid=";
    public static String MultiEvents = dom + "/api/event/multiple_events?event_id=";

    public static String PSearchEvents1 = dom + "/api/event/multi_private_search?&q=";// + EventID + "&q=";


    public static String GET_Leads = dom + "/api/user/get_leads";
    public static String ADD_Leads = dom + "/api/user/add_leads";
    public static String SendNotification = dom + "/api/user/push_notifications";
    public static String GetUser = dom + "/api/user/get_users?query_string=";//Name&appid=";
    public static String GetSegment = dom + "/api/user/get_segments?appid=";

    public static String Get_Leader_board = dom + "/api/event/leaderboard?";

    //agenda questions
    public static String Agenda_Questions = dom + "/api/event/agenda_questions?";
    public static String Add_Agenda_Question = dom + "/api/event/add_agenda_question";
    public static String Vote_Agenda_Question = dom + "/api/event/vote_agenda_question";

    //in admin panel
    public static String Register_user_event = dom + "/api/user/regusers";

    public static String Post_OptIn_OptOut = dom + "/api/user/opt_in_out";
    public static String GetAdminSurvey = dom + "/api/event/getadmin_survey_ques";
    public static String PostAdminSurveyFeedBack = dom + "/api/event/registration_answers";
    public static String ASSIGN_BEACON = dom + "/api/user/beacon_link";

    public static String Report_Post = dom + "/api/event/hidepost";
    public static String Block_User = dom + "/api/event/block_user";
    public static String Delete_Post = dom + "/api/event/deletepost";

    //contact sharing
    public static String REGISTER_BLE_ID = dom + "/api/user/cnt_exchange";
    //get notes
    public static String NOTES_DETAILS = dom + "/api/event/get_notes?";
}
