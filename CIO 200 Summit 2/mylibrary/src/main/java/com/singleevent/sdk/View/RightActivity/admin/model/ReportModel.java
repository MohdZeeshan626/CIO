package com.singleevent.sdk.View.RightActivity.admin.model;

/**
 * Created by webMOBI on 12/18/2017.
 */

public class ReportModel {

    private String exhibitorName;
    private String exhibitorId;
    private int leadCount = 0;

    public ReportModel() {

    }

    public ReportModel(String exhibitorName, String exhibitorId, int leadCount) {
        this.exhibitorName = exhibitorName;
        this.exhibitorId = exhibitorId;
        this.leadCount = leadCount;
    }

    public String getExhibitorName() {
        return exhibitorName;
    }

    public void setExhibitorName(String exhibitorName) {
        this.exhibitorName = exhibitorName;
    }

    public String getExhibitorId() {
        return exhibitorId;
    }

    public void setExhibitorId(String exhibitorId) {
        this.exhibitorId = exhibitorId;
    }

    public int getLeadCount() {
        return leadCount;
    }

    public void setLeadCount(int leadCount) {
        this.leadCount = leadCount;
    }
}
