package com.singleevent.sdk.model.LocalArraylist;

/**
 * Created by Admin on 10/19/2016.
 */
public class Schedulerslot {
    public String getDay() {
        return day;
    }

    String day;

    public Slot getSlot(int i) {
        return slots[i];
    }
    Slot[] slots;

    public int schsize() {
        if (slots == null)
            return 0;
        return slots.length;
    }

}
