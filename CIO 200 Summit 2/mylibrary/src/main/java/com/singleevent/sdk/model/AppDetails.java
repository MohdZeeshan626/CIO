package com.singleevent.sdk.model;

/**
 * Created by webmodi on 4/23/2016.
 */
public class AppDetails {
    int version;
    String appId;
    String appName;
    String appUrl;
    String appLoadingImage;
    String appDescription;
    String defaultSponsor;
    String networking;
    String staticDataId;
    boolean forceUpdate = false;
    boolean icon;
    long startdate;
    long enddate;
    String theme_color;
    String theme_border;
    String theme_strips;
    String theme_selected;
    String location;
    String venue;
    boolean timer;
    boolean info_privacy;
    private String private_key;

    public String getPrivate_key() {
        return private_key;
    }

    public void setPrivate_key(String private_key) {
        this.private_key = private_key;
    }

    String timezone;

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String[] getDisable_items() {
        return disable_items;
    }

    public void setDisable_items(String[] disable_items) {
        this.disable_items = disable_items;
    }

    String disable_items[] = {};
    String addon_modules[] = {};

    public String[] getAddon_modules() {
        return addon_modules;
    }

    public void setAddon_modules(String[] addon_modules) {
        this.addon_modules = addon_modules;
    }

    public String getAppLogo() {
        return appLogo;
    }

    public void setAppLogo(String appLogo) {
        this.appLogo = appLogo;
    }

    String appLogo;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public long getStartdate() {
        return startdate;
    }

    public void setStartdate(long startdate) {
        this.startdate = startdate;
    }

    public long getEnddate() {
        return enddate;
    }

    public void setEnddate(long enddate) {
        this.enddate = enddate;
    }

    public String getTheme_color() {
        if (theme_color == null || theme_color.equalsIgnoreCase(""))
            return "#5a249d";
        else {
            //return "#" + theme_color;
            if (theme_color.contains("#")) {
                return theme_color;
            } else {
                return "#" + theme_color;
            }
        }

    }

    public String getTheme_border() {

        return theme_border;
    }

    public String getTheme_strips() {
        if (theme_strips == null || theme_strips.equalsIgnoreCase(""))
            return "#0883ad";
        else{
             if(theme_strips.contains("#")){
            return theme_strips;
        }
        else {
            return "#" + theme_strips;
        }
        }
    }

    public String getTheme_selected() {
        if (theme_selected == null || theme_selected.equalsIgnoreCase(""))
            return "#66BCBCBC";
        else
        {
            if(theme_selected.contains("#")){
                return theme_selected;
            }
            else {
                return "#" + theme_selected;
            }
        }
    }

    public String getAppName() {
        return appName;
    }

    public boolean getInfo_privacy() {
        return info_privacy;
    }

    public boolean getTimer() {
        return timer;
    }

    public boolean getIcon() {
        return icon;
    }

    public boolean getForceUpdate() {
        return forceUpdate;
    }

    public String getStaticDataId() {
        return staticDataId;
    }

    public String getNetworking() {
        return networking;
    }

    public String getDefaultSponsor() {
        return defaultSponsor;
    }

    public String getAppDescription() {
        return appDescription;
    }

    public String getAppLoadingImage() {
        return appLoadingImage;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public String getAppId() {
        return appId;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
