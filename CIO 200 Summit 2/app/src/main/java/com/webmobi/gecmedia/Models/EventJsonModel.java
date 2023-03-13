package com.webmobi.gecmedia.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EventJsonModel {
    @SerializedName("version")
    @Expose
    private Integer version;
    @SerializedName("appId")
    @Expose
    private String appId;
    @SerializedName("event_id")
    @Expose
    private String eventId;
    @SerializedName("appName")
    @Expose
    private String appName;
    @SerializedName("appUrl")
    @Expose
    private String appUrl;
    @SerializedName("appLoadingImage")
    @Expose
    private String appLoadingImage;
    @SerializedName("appLogo")
    @Expose
    private String appLogo;
    @SerializedName("appDescription")
    @Expose
    private String appDescription;
    @SerializedName("favicon")
    @Expose
    private String favicon;
    @SerializedName("defaultSponsor")
    @Expose
    private String defaultSponsor;
    @SerializedName("forceUpdate")
    @Expose
    private Boolean forceUpdate;
    @SerializedName("icon")
    @Expose
    private Boolean icon;
    @SerializedName("timer")
    @Expose
    private Boolean timer;
    @SerializedName("info_privacy")
    @Expose
    private Boolean infoPrivacy;
    @SerializedName("iosurl")
    @Expose
    private String iosurl;
    @SerializedName("androidurl")
    @Expose
    private String androidurl;
    @SerializedName("weburl")
    @Expose
    private String weburl;
    @SerializedName("private_key")
    @Expose
    private String privateKey;
    @SerializedName("common_password")
    @Expose
    private Integer commonPassword;
    @SerializedName("qr_code")
    @Expose
    private Integer qrCode;
    @SerializedName("startdate")
    @Expose
    private Long startdate;
    @SerializedName("enddate")
    @Expose
    private Long enddate;
    @SerializedName("active")
    @Expose
    private Integer active;
    @SerializedName("venue")
    @Expose
    private String venue;
    @SerializedName("latitude")
    @Expose
    private Double latitude;
    @SerializedName("longitude")
    @Expose
    private Double longitude;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("app_category")
    @Expose
    private String appCategory;
    @SerializedName("registration_flag")
    @Expose
    private Integer registrationFlag;
    @SerializedName("theme_color")
    @Expose
    private String themeColor;
    @SerializedName("theme_strips")
    @Expose
    private String themeStrips;
    @SerializedName("theme_selected")
    @Expose
    private String themeSelected;
    @SerializedName("theme_border")
    @Expose
    private String themeBorder;
    @SerializedName("addon_modules")
    @Expose
    private List<String> addonModules = null;
    @SerializedName("web_template")
    @Expose
    private String webTemplate;
    @SerializedName("event_organizer_email")
    @Expose
    private String eventOrganizerEmail;
    @SerializedName("timezone")
    @Expose
    private String timezone;
    @SerializedName("events")
    @Expose
    private List<Event> events = null;
    @SerializedName("organizer_name")
    @Expose
    private String organizerName;
    @SerializedName("organizer_website")
    @Expose
    private String organizerWebsite;
    @SerializedName("mobile_enable")
    @Expose
    private Integer mobileEnable;
    @SerializedName("app_role_id")
    @Expose
    private Integer appRoleId;
    @SerializedName("theme_type")
    @Expose
    private String themeType;
    @SerializedName("websiteImage")
    @Expose
    private String websiteImage;
    @SerializedName("showFeedsOnImageGallery")
    @Expose
    private String showFeedsOnImageGallery;
    @SerializedName("disable_items")
    @Expose
    private List<String> disableItems = null;

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }

    public String getAppLoadingImage() {
        return appLoadingImage;
    }

    public void setAppLoadingImage(String appLoadingImage) {
        this.appLoadingImage = appLoadingImage;
    }

    public String getAppLogo() {
        return appLogo;
    }

    public void setAppLogo(String appLogo) {
        this.appLogo = appLogo;
    }

    public String getAppDescription() {
        return appDescription;
    }

    public void setAppDescription(String appDescription) {
        this.appDescription = appDescription;
    }

    public String getFavicon() {
        return favicon;
    }

    public void setFavicon(String favicon) {
        this.favicon = favicon;
    }

    public String getDefaultSponsor() {
        return defaultSponsor;
    }

    public void setDefaultSponsor(String defaultSponsor) {
        this.defaultSponsor = defaultSponsor;
    }

    public Boolean getForceUpdate() {
        return forceUpdate;
    }

    public void setForceUpdate(Boolean forceUpdate) {
        this.forceUpdate = forceUpdate;
    }

    public Boolean getIcon() {
        return icon;
    }

    public void setIcon(Boolean icon) {
        this.icon = icon;
    }

    public Boolean getTimer() {
        return timer;
    }

    public void setTimer(Boolean timer) {
        this.timer = timer;
    }

    public Boolean getInfoPrivacy() {
        return infoPrivacy;
    }

    public void setInfoPrivacy(Boolean infoPrivacy) {
        this.infoPrivacy = infoPrivacy;
    }

    public String getIosurl() {
        return iosurl;
    }

    public void setIosurl(String iosurl) {
        this.iosurl = iosurl;
    }

    public String getAndroidurl() {
        return androidurl;
    }

    public void setAndroidurl(String androidurl) {
        this.androidurl = androidurl;
    }

    public String getWeburl() {
        return weburl;
    }

    public void setWeburl(String weburl) {
        this.weburl = weburl;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public Integer getCommonPassword() {
        return commonPassword;
    }

    public void setCommonPassword(Integer commonPassword) {
        this.commonPassword = commonPassword;
    }

    public Integer getQrCode() {
        return qrCode;
    }

    public void setQrCode(Integer qrCode) {
        this.qrCode = qrCode;
    }

    public Long getStartdate() {
        return startdate;
    }

    public void setStartdate(Long startdate) {
        this.startdate = startdate;
    }

    public Long getEnddate() {
        return enddate;
    }

    public void setEnddate(Long enddate) {
        this.enddate = enddate;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAppCategory() {
        return appCategory;
    }

    public void setAppCategory(String appCategory) {
        this.appCategory = appCategory;
    }

    public Integer getRegistrationFlag() {
        return registrationFlag;
    }

    public void setRegistrationFlag(Integer registrationFlag) {
        this.registrationFlag = registrationFlag;
    }

    public String getThemeColor() {
        return themeColor;
    }

    public void setThemeColor(String themeColor) {
        this.themeColor = themeColor;
    }

    public String getThemeStrips() {
        return themeStrips;
    }

    public void setThemeStrips(String themeStrips) {
        this.themeStrips = themeStrips;
    }

    public String getThemeSelected() {
        return themeSelected;
    }

    public void setThemeSelected(String themeSelected) {
        this.themeSelected = themeSelected;
    }

    public String getThemeBorder() {
        return themeBorder;
    }

    public void setThemeBorder(String themeBorder) {
        this.themeBorder = themeBorder;
    }

    public List<String> getAddonModules() {
        return addonModules;
    }

    public void setAddonModules(List<String> addonModules) {
        this.addonModules = addonModules;
    }

    public String getWebTemplate() {
        return webTemplate;
    }

    public void setWebTemplate(String webTemplate) {
        this.webTemplate = webTemplate;
    }

    public String getEventOrganizerEmail() {
        return eventOrganizerEmail;
    }

    public void setEventOrganizerEmail(String eventOrganizerEmail) {
        this.eventOrganizerEmail = eventOrganizerEmail;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public String getOrganizerName() {
        return organizerName;
    }

    public void setOrganizerName(String organizerName) {
        this.organizerName = organizerName;
    }

    public String getOrganizerWebsite() {
        return organizerWebsite;
    }

    public void setOrganizerWebsite(String organizerWebsite) {
        this.organizerWebsite = organizerWebsite;
    }

    public Integer getMobileEnable() {
        return mobileEnable;
    }

    public void setMobileEnable(Integer mobileEnable) {
        this.mobileEnable = mobileEnable;
    }

    public Integer getAppRoleId() {
        return appRoleId;
    }

    public void setAppRoleId(Integer appRoleId) {
        this.appRoleId = appRoleId;
    }

    public String getThemeType() {
        return themeType;
    }

    public void setThemeType(String themeType) {
        this.themeType = themeType;
    }

    public String getWebsiteImage() {
        return websiteImage;
    }

    public void setWebsiteImage(String websiteImage) {
        this.websiteImage = websiteImage;
    }

    public String getShowFeedsOnImageGallery() {
        return showFeedsOnImageGallery;
    }

    public void setShowFeedsOnImageGallery(String showFeedsOnImageGallery) {
        this.showFeedsOnImageGallery = showFeedsOnImageGallery;
    }

    public List<String> getDisableItems() {
        return disableItems;
    }

    public void setDisableItems(List<String> disableItems) {
        this.disableItems = disableItems;
    }

    public class Item {

        @SerializedName("imagenumber")
        @Expose
        private Integer imagenumber;
        @SerializedName("value")
        @Expose
        private String value;
        @SerializedName("size")
        @Expose
        private Integer size;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("imageUrl")
        @Expose
        private String imageUrl;

        public Integer getImagenumber() {
            return imagenumber;
        }

        public void setImagenumber(Integer imagenumber) {
            this.imagenumber = imagenumber;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public Integer getSize() {
            return size;
        }

        public void setSize(Integer size) {
            this.size = size;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

    }

    public class Tab {

        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("order_by")
        @Expose
        private String orderBy;
        @SerializedName("sub_type")
        @Expose
        private String subType;
        @SerializedName("iconCls")
        @Expose
        private String iconCls;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("caption")
        @Expose
        private String caption;
        @SerializedName("checkvalue")
        @Expose
        private String checkvalue;
        @SerializedName("position")
        @Expose
        private Integer position;
        @SerializedName("mod_display")
        @Expose
        private String modDisplay;
        @SerializedName("items")
        @Expose
        private List<Item> items = null;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getOrderBy() {
            return orderBy;
        }

        public void setOrderBy(String orderBy) {
            this.orderBy = orderBy;
        }

        public String getSubType() {
            return subType;
        }

        public void setSubType(String subType) {
            this.subType = subType;
        }

        public String getIconCls() {
            return iconCls;
        }

        public void setIconCls(String iconCls) {
            this.iconCls = iconCls;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getCaption() {
            return caption;
        }

        public void setCaption(String caption) {
            this.caption = caption;
        }

        public String getCheckvalue() {
            return checkvalue;
        }

        public void setCheckvalue(String checkvalue) {
            this.checkvalue = checkvalue;
        }

        public Integer getPosition() {
            return position;
        }

        public void setPosition(Integer position) {
            this.position = position;
        }

        public String getModDisplay() {
            return modDisplay;
        }

        public void setModDisplay(String modDisplay) {
            this.modDisplay = modDisplay;
        }

        public List<Item> getItems() {
            return items;
        }

        public void setItems(List<Item> items) {
            this.items = items;
        }

    }

    public class Event {

        @SerializedName("tabs")
        @Expose
        private List<Tab> tabs = null;

        public List<Tab> getTabs() {
            return tabs;
        }

        public void setTabs(List<Tab> tabs) {
            this.tabs = tabs;
        }

    }
}
