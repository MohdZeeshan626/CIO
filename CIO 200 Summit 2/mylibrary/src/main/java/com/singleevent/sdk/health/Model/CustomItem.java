package com.singleevent.sdk.health.Model;

public class CustomItem {
    private String spinneritemname;
    private int spinnneritemimage;
    public CustomItem(String spinneritemname,int spinnneritemimage){
        this.spinneritemname=spinneritemname;
        this.spinnneritemimage=spinnneritemimage;
    }

    public String getSpinneritemname() {
        return spinneritemname;
    }

    public void setSpinneritemname(String spinneritemname) {
        this.spinneritemname = spinneritemname;
    }

    public int getSpinnneritemimage() {
        return spinnneritemimage;
    }

    public void setSpinnneritemimage(int spinnneritemimage) {
        this.spinnneritemimage = spinnneritemimage;
    }
}