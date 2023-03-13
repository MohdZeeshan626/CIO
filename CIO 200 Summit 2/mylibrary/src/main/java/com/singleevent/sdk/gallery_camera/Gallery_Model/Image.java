package com.singleevent.sdk.gallery_camera.Gallery_Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hoanglam on 7/31/16.
 */
public class Image implements Parcelable {

    public static final Creator<Image> CREATOR = new Creator<Image>() {
        @Override
        public Image createFromParcel(Parcel source) {
            return new Image(source);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };
    private long resource_id;
    private String res_name;
    private String res_url;
    private String res_type;
    private int res_height;
    private int res_width;

    public Image(long id, String name, String path) {
        this.resource_id = id;
        this.res_name = name;
        this.res_url = path;
    }

    public Image(long id, String name, String path, int imageHeight, int imageWidth, String type) {
        this.resource_id = id;
        this.res_name = name;
        this.res_url = path;
        this.res_height = imageHeight;
        this.res_width = imageWidth;
        this.res_type = type;
    }

    protected Image(Parcel in) {
        this.resource_id = in.readLong();
        this.res_name = in.readString();
        this.res_url = in.readString();
        this.res_width = in.readInt();
        this.res_height = in.readInt();
        this.res_type = in.readString();
    }

    public String getType() {
        return res_type;
    }

    public void setType(String type) {
        this.res_type = type;
    }

    public long getId() {
        return resource_id;
    }

    public void setId(long id) {
        this.resource_id = id;
    }

    public String getName() {
        return res_name;
    }

    public void setName(String name) {
        this.res_name = name;
    }

    public String getPath() {
        return res_url;
    }

    public void setPath(String path) {
        this.res_url = path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Image image = (Image) o;
        return image.getPath().equalsIgnoreCase(getPath());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.resource_id);
        dest.writeString(this.res_name);
        dest.writeString(this.res_url);
        dest.writeInt(this.res_width);
        dest.writeInt(this.res_height);
        dest.writeString(this.res_type);
    }

    public int getImageHeight() {
        return res_height;
    }

    public void setImageHeight(int imageHeight) {
        this.res_height = imageHeight;
    }

    public int getImageWidth() {
        return res_width;
    }

    public void setImageWidth(int imageWidth) {
        this.res_width = imageWidth;
    }

    public Integer getArea() {
        return res_height * res_width;
    }
}
