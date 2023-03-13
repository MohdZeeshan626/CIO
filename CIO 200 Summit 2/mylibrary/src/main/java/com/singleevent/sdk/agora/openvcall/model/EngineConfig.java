package com.singleevent.sdk.agora.openvcall.model;

public class EngineConfig {
    public int mUid;
    public  String mUids;

    public String mChannel;

    public void reset() {
        mChannel = null;
    }

    public EngineConfig() {
    }
}
