package com.singleevent.sdk.feeds_class;

import com.singleevent.sdk.model.Feed;

/**
 * Created by webMOBI on 9/13/2017.
 */

public interface OnItemClickListener {

    void onLike(Feed feed, int islike, int pos);

    void onComment(Feed feed, int pos);

    void onReport(Feed feed, int pos);

    void onBlockUser(Feed feed, int pos);
    void onRecoverUser(Feed feed, int pos);

    void onDeletePost(int feedid);
}
