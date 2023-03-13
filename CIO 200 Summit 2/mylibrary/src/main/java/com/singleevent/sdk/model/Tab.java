package com.singleevent.sdk.model;


import com.singleevent.sdk.model.Agenda.Agenda;
import com.singleevent.sdk.model.Agenda.AgendaSurvey;
import com.singleevent.sdk.model.Home.Homebanner;
import com.singleevent.sdk.model.Home.Homeimage;
import com.singleevent.sdk.model.Map.Mapfloorplan;
import com.singleevent.sdk.model.Map.internalmapview;

/**
 * Created by User on 5/16/2016.
 */
public class Tab {

    String type;
    String hashtag;
    String defaultmsg;

    String iconCls;
    String title;
    String caption;
    Homebanner[] banner;
    Items[] items;
    Categories[] categories;
    String checkvalue;
    PDFurl[] pdfurl;
    Homeimage[] image;
    String content;
    Agenda[] agenda;
    AgendaSurvey[] survey;
    String toEmail;
    String phone;
    String fromEmail;
    String url;
    String fblink;
    String screen_name;
    String venue;
    double lat;
    double lng;
    String description;
    Mapfloorplan[] floors = null;
    internalmapview[] maps = null;
    String name;
    String question;
    String ans1;
    String ans2;
    String ans3;
    String ans4;
    String ans5;
    String ans6;
    String ans7;
    String ans8;
    String ans9;
    String ans10;
    String detail;
    String[] cities;
    String mod_display;
    String sub_type = "";
    int position;

    public String getHashtag() {
        return hashtag;
    }

    public void setHashtag(String hashtag) {
        this.hashtag = hashtag;
    }

    public String getDefaultmsg() {
        return defaultmsg;
    }

    public void setDefaultmsg(String defaultmsg) {
        this.defaultmsg = defaultmsg;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPhone() {
        return phone;
    }

    public String getSub_type() {
        return sub_type;
    }

    public void setSub_type(String sub_type) {
        this.sub_type = sub_type;
    }

    public String getMod_display() {
        return mod_display;
    }

    public void setMod_display(String mod_display) {
        this.mod_display = mod_display;
    }

    public Categories getCategories(int i) {
        return categories[i];
    }

    public String getCheckvalue() {
        return checkvalue;
    }

    public PDFurl getPdfurl(int i) {
        return pdfurl[i];
    }

    public String getQuestion() {
        return question;
    }

    public String getAns1() {
        return ans1;
    }

    public String getAns2() {
        return ans2;
    }

    public String getAns3() {
        return ans3;
    }

    public String getAns4() {
        return ans4;
    }

    public String getAns5() {
        return ans5;
    }

    public String getAns6() {
        return ans6;
    }

    public String getAns7() {
        return ans7;
    }

    public String getAns8() {
        return ans8;
    }

    public String getAns9() {
        return ans9;
    }

    public String getAns10() {
        return ans10;
    }

    public String getDetail() {
        return detail;
    }

    public Homebanner getBanner(int i) {
        return banner[i];
    }

    public Items[] getItems() {
        return items;
    }

    public Homeimage getImage(int i) {
        return image[i];
    }

    public String getContent() {
        return content;
    }

    public Agenda getAgenda(int i) {
        return agenda[i];
    }

    public AgendaSurvey getSurvey(int i) {
        return survey[i];
    }

    public String getToEmail() {
        return toEmail;
    }

    public String getFromEmail() {
        return fromEmail;
    }

    public String getUrl() {
        if (url == null)
            return "";
        return url;
    }

    public String getFblink() {
        return fblink;
    }

    public String getScreen_name() {
        return screen_name;
    }

    public String getVenue() {
        return venue;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public String getDescription() {
        return description;
    }

    public Mapfloorplan getFloors(int i) {
        return floors[i];
    }

    public internalmapview getMaps(int i) {
        return maps[i];
    }

    public String getName() {
        return name;
    }

    public String getCities(int i) {
        return cities[i];
    }

    public String getType() {
        return type;
    }

    public String getIconCls() {
        return iconCls;
    }

    public String getTitle() {
        return title;
    }

    public String getCaption() {
        return caption;
    }

    public Items getItems(int i) {
        return items[i];
    }

    public Homebanner[] getBanner() {
        return banner;
    }

    public int getBannerSize() {
        if (banner == null) return 0;
        else return banner.length;
    }

    public Homeimage[] getImage() {
        return image;
    }

    public Agenda[] getAgenda() {
        return agenda;
    }

    public AgendaSurvey[] getSurvey() {
        return survey;
    }

    public Mapfloorplan[] getFloors() {
        return floors;
    }

    public internalmapview[] getMaps() {
        return maps;
    }

    public String[] getCities() {
        return cities;
    }

    public int getAgendaSize() {
        if (agenda == null) return 0;
        else return agenda.length;
    }

    public int getCitiesSize() {
        if (cities == null) return 0;
        else return cities.length;
    }

    public int getMapsSize() {
        if (maps == null) return 0;
        else return maps.length;
    }

    public int getFloorsSize() {
        if (floors == null) return 0;
        else return floors.length;
    }

    public int getSurveySize() {
        if (survey == null) return 0;
        else return survey.length;
    }

    public int getImageSize() {
        if (image == null) return 0;
        else return image.length;
    }

    public int getItemsSize() {
        if (items == null) return 0;
        else return items.length;
    }

    public int getpdfSize() {
        if (pdfurl == null) return 0;
        else return pdfurl.length;
    }

    public int getcatSize() {
        if (categories == null) return 0;
        else return categories.length;
    }
}
