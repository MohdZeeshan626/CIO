package com.singleevent.sdk.View.RightActivity.admin.model;

/**
 * Created by webMOBI on 12/19/2017.
 */

public class ExhibitorReportModel {

    private String table_id;
    private String userid;
    private String appid;
    private String username;
    private String lead_id;
    private  String lead_name;
    private String admin_flag;

    public ExhibitorReportModel() {
    }

    public ExhibitorReportModel(String table_id, String userid, String appid, String username,
                                String lead_id, String lead_name, String admin_flag) {
        this.table_id = table_id;
        this.userid = userid;
        this.appid = appid;
        this.username = username;
        this.lead_id = lead_id;
        this.lead_name = lead_name;
        this.admin_flag = admin_flag;
    }

    public String getTable_id() {
        return table_id;
    }

    public void setTable_id(String table_id) {
        this.table_id = table_id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLead_id() {
        return lead_id;
    }

    public void setLead_id(String lead_id) {
        this.lead_id = lead_id;
    }

    public String getLead_name() {
        return lead_name;
    }

    public void setLead_name(String lead_name) {
        this.lead_name = lead_name;
    }

    public String getAdmin_flag() {
        return admin_flag;
    }

    public void setAdmin_flag(String admin_flag) {
        this.admin_flag = admin_flag;
    }
}
