package com.singleevent.sdk.agora.openvcall.model;

import com.singleevent.sdk.agora.openvcall.model.AGEventHandler;

import io.agora.rtc.IRtcEngineEventHandler;

public interface BeforeCallEventHandler extends AGEventHandler {
    void onLastmileQuality(int quality);

    void onLastmileProbeResult(IRtcEngineEventHandler.LastmileProbeResult result);
}
