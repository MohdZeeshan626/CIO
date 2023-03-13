package com.singleevent.sdk.model.Exhibitor;

import java.io.Serializable;

/**
 * Created by webmodi on 4/1/2016.
 */
public class Exhibitorproduct  implements Serializable {
    String product;
    String description;
    String about;
    String logoLmage;

    public String getLogoLmage() {
        return logoLmage;
    }

    public String getAbout() {
        return about;
    }

    public String getDescription() {
        return description;
    }

    public String getProduct() {
        return product;
    }


}
