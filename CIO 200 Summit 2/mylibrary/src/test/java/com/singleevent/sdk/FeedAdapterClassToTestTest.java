package com.singleevent.sdk;

import static org.junit.Assert.*;

import com.singleevent.sdk.gallery_camera.Gallery_Model.Image;
import com.singleevent.sdk.model.Feed;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class FeedAdapterClassToTestTest {

    FeedAdapterClassToTest SUT;
    List<FeedTestPojo> feeds=new ArrayList<>();
    List<ImagePojo> images = new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        SUT = new FeedAdapterClassToTest();
    }

    @Test
    public void testPollingCard() {
        feeds.add(new FeedTestPojo("polling{}","ggdgg",images));
        assertTrue(SUT.testAdapterConditions(feeds).equals("poll"));
    }

    @Test
    public void testUrlPreview() {
        feeds.add(new FeedTestPojo("","https://",images));
        assertTrue(SUT.testAdapterConditions(feeds).equals("link_preview"));

    }

    @Test
    public void testYoutube() {
        feeds.add(new FeedTestPojo("","https://youtu",images));
        assertTrue(SUT.testAdapterConditions(feeds).equals("youtube_video"));

    }

    @Test
    public void testVimeo() {
        feeds.add(new FeedTestPojo("","https://progressive.akamaized.net",images));
        assertTrue(SUT.testAdapterConditions(feeds).equals("vimeo_video") || SUT.testAdapterConditions(feeds).equals("link_preview"));
    }

    @Test
    public void testExoPlayer() {
        images.add(new ImagePojo("video/mp4"));
        feeds.add(new FeedTestPojo("","video/mp4",images));
        assertTrue(SUT.testAdapterConditions(feeds).equals("exo_player"));
    }

    @Test
    public void test_addingMultipleAttachments_images() {
        images.add(new ImagePojo("images"));
        images.add(new ImagePojo("images"));
        images.add(new ImagePojo("images"));
        feeds.add(new FeedTestPojo("","anything",images));
        assertTrue(SUT.testAdapterConditions(feeds).equals("multiple medias"));

    }
    @Test
    public void test_addingMultipleAttachments_images_videos() {
        images.add(new ImagePojo("images"));
        images.add(new ImagePojo("video/mp4"));
        images.add(new ImagePojo("images"));
        feeds.add(new FeedTestPojo("","anything",images));
        assertTrue(SUT.testAdapterConditions(feeds).equals("multiple medias"));

    }


}