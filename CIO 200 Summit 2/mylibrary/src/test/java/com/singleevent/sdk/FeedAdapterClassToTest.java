package com.singleevent.sdk;

import java.util.List;

public class FeedAdapterClassToTest {
String cases="";
    public String testAdapterConditions(List<FeedTestPojo> feeds) {
        final FeedTestPojo feed = feeds.get(0);

        if (feed.getPost_content().contains("https://") && !feed.getPost_content().contains("youtu")) {
            cases= "link_preview";
        }
        if (feed.getTitle().startsWith("polling{") && feed.getTitle().endsWith("}")) {
            cases= "poll";
        }
        if (feed.getAttachments().size() > 0) {
            if (feed.getAttachments().size() > 1) {
                cases=  "multiple medias";
            } else {
                if (feed.getAttachments().get(0).getType().contains("image")) {
                    cases= "feed image";
                }
            }
        }
        if (feed.getAttachments().size() <= 0) {
            if ((feed.getPost_content().startsWith("https://")) && feed.getPost_content().contains("youtu") || feed.getPost_content().contains("https://youtube")) {

                cases= "youtube_video";

            }
            // loading vimeo
            else if (feed.getPost_content().contains(".mp4") && feed.getPost_content().startsWith("https://") && feed.getPost_content().contains("progressive.akamaized.net") && feed.getPost_content().length() > 20) {
                cases= "vimeo_video";

            }
        } else {
            if (feed.getAttachments().get(0).getType().equalsIgnoreCase("video/mp4")) {
                cases= "exo_player";
            }
        }


        return cases;
    }

}
