package com.singleevent.sdk.View.RightActivity.admin.checkin;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by User on 10/3/2016.
 */

public interface VolleyResponseListener {

    void onVolleyResponse(String tag, JSONObject response) throws JSONException;

    void onVolleyError(String tag, VolleyError error);
}
