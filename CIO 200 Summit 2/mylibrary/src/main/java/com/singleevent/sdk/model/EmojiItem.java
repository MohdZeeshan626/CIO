package com.singleevent.sdk.model;

import java.io.Serializable;

public class EmojiItem implements Serializable {



    String emoji_name;
    String[] tags;

    public String getEmoji_name() {
        return emoji_name;
    }

    public void setEmoji_name(String emoji_name) {
        this.emoji_name = emoji_name;
    }
    public String[] getTags() {
        return tags;
    }
    public String getTags(int i) {
        return tags[i];
    }
    public int getTagSize() {
        if (tags == null) return 0;
        else return tags.length;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public void setAnswer(String[] answer) {
        this.tags = answer;
    }

}
