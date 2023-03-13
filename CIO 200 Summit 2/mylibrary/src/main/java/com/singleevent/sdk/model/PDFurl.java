package com.singleevent.sdk.model;

/**
 * Created by Admin on 9/24/2016.
 */
public class PDFurl {


    String name;
    PDF_Details[] details;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public PDF_Details getdetails(int i) {
        return details[i];
    }

    public PDF_Details[] getdetails() {
        return details;
    }
    public int getpdfdetailssize() {
        if (details == null) return 0;
        else return details.length;
    }
}
