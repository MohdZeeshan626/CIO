package com.singleevent.sdk.Custom_View;


import com.singleevent.sdk.model.LocalArraylist.ChatMSG;

import java.util.Comparator;

/**
 * Created by Admin on 6/21/2017.
 */

public class SortingMSG implements Comparator<ChatMSG> {

    @Override
    public int compare(ChatMSG user1, ChatMSG user2) {
        if (Long.parseLong(user1.getCreate_date()) == Long.parseLong(user2.getCreate_date()))
            return 0;
        else if (Long.parseLong(user1.getCreate_date()) < Long.parseLong(user2.getCreate_date()))
            return 1;
        else
            return -1;
    }
}
