package com.singleevent.sdk.model;


import com.singleevent.sdk.model.Agenda.LocationDetails;
import com.singleevent.sdk.model.Exhibitor.Exhibitorproduct;
import com.singleevent.sdk.model.Polling.polling;

import java.io.Serializable;

/**
 * Created by User on 5/16/2016.
 */
public class Items implements Serializable {

    String imagenumber;
    String value;
    String imageUrl;
    String company;
    int exhibitor_id;
    int sponsor_id;
    String description;
    String detail;
    String website;
    String poll_type;
    int  hide_poll;
    int hide_speaker;
    int poll_type_id;
    String sponsortype;



    String fblink;
    String twittername;
    String linkedinlink;
    String image;
    String docLink;
    String items;
    String sponsor = "";
    String facebook;
    String linkedin;
    Exhibitorproduct[] products = null;
    String question;
    String type;
    String name;
    String details;
    int speakerId;
    int[] agendaId;
    String categories;
    String iconCls;
    String url;
    String link;
    String image_url;
    String date;
    polling[] poll;
    String[] answer;
    String email;
    String checkvalue;
    String speaker_document_url;
    int speaker_document_hide;
    String speaker_document_name;
    EmojiItem[]  emoji_info;
    String emoji_name;
    int active;
    int question_id;
    String survey_type="";

    int banner_id;
    String banner_name;
    String banner_url;
    String default_id;
    String banner_type;
    String button_txt;
    String button_link;
    int type_id;
    int required;
    String exhibitor_document_url;
    String exhibitor_document_name;

    String exhibitor_document_url2;
    String exhibitor_document_name2;

    String exhibitor_document_url3;
    String exhibitor_document_name3;

    String exhibitor_document_url4;
    String exhibitor_document_name4;

    String exhibitor_document_url5;
    String exhibitor_document_name5;
    String floor_id;
    LocationDetails location_detail;
    String map_checkvalue;
    String location;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    public void setValue(String value) {
        this.value = value;
    }


    public EmojiItem getEmoji_info(int i){return emoji_info[i];}
    public int getEmoji_info() {
        if (emoji_info == null) return 0;
        else return emoji_info.length;
    }

    public String getFloor_id() {
        return floor_id;
    }

    public void setFloor_id(String floor_id) {
        this.floor_id = floor_id;
    }

    public LocationDetails getLocation_detail() {
        return location_detail;
    }

    public void setLocation_detail(LocationDetails location_detail) {
        this.location_detail = location_detail;
    }

    public String getMap_checkvalue() {
        return map_checkvalue;
    }

    public void setMap_checkvalue(String map_checkvalue) {
        this.map_checkvalue = map_checkvalue;
    }

    public void setEmoji_info(EmojiItem[] emoji_info) {
        this.emoji_info = emoji_info;
    }

    public String getEmoji_name() {
        return emoji_name;
    }

    public void setEmoji_name(String emoji_name) {
        this.emoji_name = emoji_name;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public int getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(int question_id) {
        this.question_id = question_id;
    }

    public String getSurvey_type() {
        return survey_type;
    }

    public void setSurvey_type(String survey_type) {
        this.survey_type = survey_type;
    }

    public String getSpeaker_document_url() {
        return speaker_document_url;
    }

    public void setSpeaker_document_url(String speaker_document_url) {
        this.speaker_document_url = speaker_document_url;
    }




    public String getExhibitor_document_url() {
        return exhibitor_document_url;
    }

    public void setExhibitor_document_url(String exhibitor_document_url) {
        this.exhibitor_document_url = exhibitor_document_url;
    }

    public String getExhibitor_document_name() {
        return exhibitor_document_name;
    }

    public void setExhibitor_document_name(String exhibitor_document_name) {
        this.exhibitor_document_name = exhibitor_document_name;
    }

    public String getExhibitor_document_url2() {
        return exhibitor_document_url2;
    }

    public void setExhibitor_document_url2(String exhibitor_document_url2) {
        this.exhibitor_document_url2 = exhibitor_document_url2;
    }

    public String getExhibitor_document_name2() {
        return exhibitor_document_name2;
    }

    public void setExhibitor_document_name2(String exhibitor_document_name2) {
        this.exhibitor_document_name2 = exhibitor_document_name2;
    }

    public String getExhibitor_document_url3() {
        return exhibitor_document_url3;
    }

    public void setExhibitor_document_url3(String exhibitor_document_url3) {
        this.exhibitor_document_url3 = exhibitor_document_url3;
    }

    public String getExhibitor_document_name3() {
        return exhibitor_document_name3;
    }

    public void setExhibitor_document_name3(String exhibitor_document_name3) {
        this.exhibitor_document_name3 = exhibitor_document_name3;
    }

    public String getExhibitor_document_url4() {
        return exhibitor_document_url4;
    }

    public void setExhibitor_document_url4(String exhibitor_document_url4) {
        this.exhibitor_document_url4 = exhibitor_document_url4;
    }

    public String getExhibitor_document_name4() {
        return exhibitor_document_name4;
    }

    public void setExhibitor_document_name4(String exhibitor_document_name4) {
        this.exhibitor_document_name4 = exhibitor_document_name4;
    }

    public String getExhibitor_document_url5() {
        return exhibitor_document_url5;
    }

    public void setExhibitor_document_url5(String exhibitor_document_url5) {
        this.exhibitor_document_url5 = exhibitor_document_url5;
    }

    public String getExhibitor_document_name5() {
        return exhibitor_document_name5;
    }

    public void setExhibitor_document_name5(String exhibitor_document_name5) {
        this.exhibitor_document_name5 = exhibitor_document_name5;
    }


    public void setImagenumber(String imagenumber) {
        this.imagenumber = imagenumber;
    }

    public int getSpeaker_document_hide() {
        return speaker_document_hide;
    }

    public void setSpeaker_document_hide(int speaker_document_hide) {
        this.speaker_document_hide = speaker_document_hide;
    }

    public String getSpeaker_document_name() {
        return speaker_document_name;
    }

    public void setSpeaker_document_name(String speaker_document_name) {
        this.speaker_document_name = speaker_document_name;
    }

    public int getRequired() {
        return required;
    }
    public String getCheckvalue() {
        return checkvalue;
    }

    public void setRequired(int required) {
        this.required = required;
    }

    public String getBanner_type() {
        return banner_type;
    }

    public void setBanner_type(String banner_type) {
        this.banner_type = banner_type;
    }

    public int getBanner_id() {
        return banner_id;
    }

    public void setBanner_id(int banner_id) {
        this.banner_id = banner_id;
    }
    public String getPoll_type() {
        return poll_type;
    }
    public int getPoll_type_id() {
        return poll_type_id;
    }
    public int getHide_poll() {
        return hide_poll;
    }

    public int getHide_speaker() {
        return hide_speaker;
    }

    public void setHide_speaker(int hide_speaker) {
        this.hide_speaker = hide_speaker;
    }

    public String getBanner_name() {
        return banner_name;
    }

    public void setBanner_name(String banner_name) {
        this.banner_name = banner_name;
    }

    public String getBanner_url() {
        return banner_url;
    }

    public void setBanner_url(String banner_url) {
        this.banner_url = banner_url;
    }

    public String getDefault_id() {
        return default_id;
    }

    public void setDefault_id(String default_id) {
        this.default_id = default_id;
    }

    public String getButton_txt() {
        return button_txt;
    }

    public void setButton_txt(String button_txt) {
        this.button_txt = button_txt;
    }

    public String getButton_link() {
        return button_link;
    }

    public void setButton_link(String button_link) {
        this.button_link = button_link;
    }

    public int getType_id() {
        return type_id;
    }

    public void setType_id(int type_id) {
        this.type_id = type_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAttachment_id() {
        return attachment_id;
    }

    public String getAttachment_type() {
        return attachment_type;
    }

    public String getAttachment_name() {
        return attachment_name;
    }

    public String getAttachment_category() {
        return attachment_category;
    }

    public String getAttachment_url() {

        if (attachment_url.startsWith("http://") || attachment_url.startsWith("https://")) {
            return attachment_url;
        } else {
            return "http://" + attachment_url;
        }
    }

    String attachment_id;
    String attachment_type;
    String attachment_name;
    String attachment_category;
    String attachment_url;


    public int getExhibitor_id() {
        return exhibitor_id;
    }

    public void setExhibitor_id(int exhibitor_id) {
        this.exhibitor_id = exhibitor_id;
    }

    public int getSponsor_id() {
        return sponsor_id;
    }

    public void setSponsor_id(int sponsor_id) {
        this.sponsor_id = sponsor_id;
    }

    public String[] getAnswer() {
        return answer;
    }

    public void setAnswer(String[] answer) {
        this.answer = answer;
    }

    public String getCategories() {
        if (categories == null || categories.equalsIgnoreCase(""))
            return "";
        return categories;
    }


    public polling getpoll(int i) {
        return poll[i];
    }

    public int getpollingSize() {
        if (poll == null) return 0;
        else return poll.length;
    }

    public String getFacebook() {
        if (facebook == null)
            return "";
        return facebook;
    }

    public String getLinkedin() {
        if (linkedin == null)
            return "";
        return linkedin;
    }


    public String getImagenumber() {
        return imagenumber;
    }

    public String getValue() {
        return value;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getCompany() {
        return company;
    }

    public String getDescription() {
        if (description == null)
            return "";
        else
            return description;
    }

    public String getDetail() {
        return detail;
    }

    public String getWebsite() {
        if (website.startsWith("http://") || website.startsWith("https://")) {
            return website;
        } else {
            return "http://" + website;
        }
    }

    public String getSponsortype() {
        return sponsortype;
    }

    public String getFblink() {
        return fblink;
    }

    public String getTwittername() {
        return twittername;
    }

    public String getLinkedinlink() {
        return linkedinlink;
    }

    public String getImage() {
        return image;
    }

    public String getDocLink() {
        return docLink;
    }

    public String getItems() {
        return items;
    }

    public String getSponsor() {
        return sponsor;
    }


    public Exhibitorproduct getProducts(int i) {
        return products[i];
    }

    public String getQuestion() {
        return question;
    }


    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getDetails() {
        return details;
    }

    public int getSpeakerId() {
        return speakerId;
    }

    public int[] getAgendaId() {
        return agendaId;
    }

    public String getIconCls() {
        return iconCls;
    }

    public String getUrl() {
        return url;
    }

    public String getLink() {
        return link;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getDate() {
        return date;
    }


    public Exhibitorproduct[] getProducts() {
        return products;
    }

    public int getAgendaIdSize() {
        if (agendaId == null) return 0;
        else return agendaId.length;
    }

    public int getProductsSize() {
        if (products == null) return 0;
        else return products.length;
    }


}
