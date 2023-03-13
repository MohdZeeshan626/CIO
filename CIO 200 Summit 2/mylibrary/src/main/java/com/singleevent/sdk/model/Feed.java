package com.singleevent.sdk.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.singleevent.sdk.gallery_camera.Gallery_Model.Image;
import com.singleevent.sdk.pojo.pollingpojo.PollingAnswersPojo;
import com.singleevent.sdk.pojo.pollingpojo.PollingResultPojo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fujitsu on 30-08-2017.
 */

public class Feed implements Parcelable {

    private String userid;
    private String title;
    private int post_id;
    private String username;
    private String post_content;
    private String profile_pic;
    private String create_time;
    private int likes;
    private int comments;
    private ArrayList<Image> attachments;
    private Like_User[] like_users;
    private Comment_User[] comment_users;
    private int like_status;
    List<PollingResultPojo> poll_resultPojos;
    private List<PollingAnswersPojo> poll_answersPojoList;
    boolean isAnswerdPoll;
    public static final Creator<Feed> CREATOR = new Creator<Feed>() {
        @Override
        public Feed createFromParcel(Parcel in) {
            return new Feed(in);
        }

        @Override
        public Feed[] newArray(int size) {
            return new Feed[size];
        }
    };

    protected Feed(Parcel in) {

        post_id = in.readInt();
        userid = in.readString();
        title = in.readString();
        username = in.readString();
        post_content = in.readString();
        profile_pic = in.readString();
        create_time = in.readString();
        likes = in.readInt();
        comments = in.readInt();
        attachments = in.createTypedArrayList(Image.CREATOR);
        like_status = in.readInt();
    }

    public List<PollingResultPojo> getPoll_resultPojos() {
        return poll_resultPojos;
    }

    public void setPoll_resultPojos(List<PollingResultPojo> poll_resultPojos) {
        this.poll_resultPojos = poll_resultPojos;
    }

    public boolean isAnswerdPoll() {
        return isAnswerdPoll;
    }

    public void setAnswerdPoll(boolean answerdPoll) {
        isAnswerdPoll = answerdPoll;
    }

    public List<PollingAnswersPojo> getPoll_answersPojoList() {
        return poll_answersPojoList;
    }

    public void setPoll_answersPojoList(List<PollingAnswersPojo> poll_answersPojoList) {
        this.poll_answersPojoList = poll_answersPojoList;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public int getLike_status() {
        return like_status;
    }

    public void setLike_status(int like_status) {
        this.like_status = like_status;
    }

    public String getTitle() {
        return title;
    }

    public int likeslength() {
        if (like_users == null)
            return 0;
        return like_users.length;
    }

    public int commentslength() {
        if (comment_users == null)
            return 0;
        return comment_users.length;
    }

    public int attachmentlength() {
        if (attachments == null)
            return 0;
        return attachments.size();
    }


    public Like_User getLike_users(int pos) {
        return like_users[pos];
    }

    public Comment_User getComment_users(int pos) {
        return comment_users[pos];
    }


    public ArrayList<Image> getAttachments() {
        return attachments;
    }

    public void setAttachments(ArrayList<Image> attachments) {
        this.attachments = attachments;
    }

    public int getLikesCount() {
        return likes;
    }


    public int getCommented() {
        return comments;
    }

    public int getId() {
        return post_id;
    }

    public String getName() {
        return username;
    }

    public String getSubject() {
        if (post_content == null)
            return "";
        return post_content;
    }

    public String getProfilePic() {
        if (profile_pic == null)
            return "";
        return profile_pic;
    }

    public String getUserid() {
        return userid;
    }

    public String getTimeStamp() {
        return create_time;
    }


    @Override
    public String toString() {
        return "Feed{" +
                "userid='" + userid + '\'' +
                ", post_id=" + post_id +
                ", post_content='" + post_content + '\'' +
                ", attachments=" + attachments +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Feed feed = (Feed) o;

        return post_id == feed.post_id;

    }

    @Override
    public int hashCode() {
        return post_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userid);
        dest.writeString(title);
        dest.writeInt(post_id);
        dest.writeString(username);
        dest.writeString(post_content);
        dest.writeString(profile_pic);
        dest.writeString(create_time);
        dest.writeInt(likes);
        dest.writeInt(comments);
        dest.writeTypedList(attachments);
        dest.writeInt(like_status);
    }
}
