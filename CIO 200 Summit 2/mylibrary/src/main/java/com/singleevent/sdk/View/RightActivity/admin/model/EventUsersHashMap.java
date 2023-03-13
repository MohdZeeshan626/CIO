package com.singleevent.sdk.View.RightActivity.admin.model;

import java.util.HashMap;
import java.util.List;

/**
 * Created by User on 10/5/2016.
 */

public class EventUsersHashMap extends HashMap<String, List<EventUser>> {

    private static EventUsersHashMap instance = null;

    private EventUsersHashMap() {
    }

    public static synchronized EventUsersHashMap getInstance() {
        if (instance == null) instance = new EventUsersHashMap();
        return instance;
    }

    @Override
    public List<EventUser> put(String key, List<EventUser> value) {

        return super.put(key, value);
    }
}
