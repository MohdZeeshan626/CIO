package com.singleevent.sdk.View.RightActivity.admin.checkin;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.singleevent.sdk.R;
import com.singleevent.sdk.View.RightActivity.MyQrCodeGeneratorActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by User on 10/3/2016.
 */

public class VolleyUtils {

    private static final int MY_SOCKET_TIMEOUT_MS = 5000;
    private static final int MY_SOCKET_MAX_RETRIES = 1;

    public static JsonObjectRequest createJsonPostReq(final String tag, String url, JSONObject jsonObject,
                                                      final VolleyResponseListener listener) {
        final ProgressDialog pDialog = new ProgressDialog((Context) listener,R.style.MyAlertDialogStyle);

        if (((EventUsersActivity.CHECK_IN_REQ_TAG.equals(tag))|| (MyQrCodeGeneratorActivity.CHECK_IN_REQ_TAG.equals(tag))))
            pDialog.setMessage("Checking User...");
        else
            pDialog.setMessage("Loading...");

        if (((!EventUsersActivity.USER_LIST_REQ_TAG.equals(tag))||(MyQrCodeGeneratorActivity.USER_LIST_REQ_TAG.equals(tag))))
            pDialog.show();

        JsonObjectRequest jsonObjReq;

        jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, jsonObject,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            if (listener != null) {
                                dismissWithCheck(pDialog);
                                listener.onVolleyResponse(tag, response);
                            }
                        } catch (JSONException e) {
                            if (listener != null) {
                                dismissWithCheck(pDialog);
                            }

                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (listener != null) {
                    dismissWithCheck(pDialog);
                    listener.onVolleyError(tag, error);
                }else if (pDialog.isShowing()){
                        pDialog.dismiss();
                }
            }
        });
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                MY_SOCKET_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return jsonObjReq;
    }

    private static void dismissWithCheck(ProgressDialog dialog) {
        if (dialog != null) {
            if (dialog.isShowing()) {

                //get the Context object that was used to great the dialog
                Context context = ((ContextWrapper) dialog.getContext()).getBaseContext();

                // if the Context used here was an activity AND it hasn't been finished or destroyed
                // then dismiss it
                if (context instanceof Activity) {

                    // Api >=17
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        if (!((Activity) context).isFinishing() && !((Activity) context).isDestroyed()) {
                            dismissWithTryCatch(dialog);
                        }
                    } else {

                        // Api < 17. Unfortunately cannot check for isDestroyed()
                        if (!((Activity) context).isFinishing()) {
                            dismissWithTryCatch(dialog);
                        }
                    }
                } else
                    // if the Context used wasn't an Activity, then dismiss it too
                    dismissWithTryCatch(dialog);
            }
            dialog = null;
        }
    }

    private static void dismissWithTryCatch(ProgressDialog dialog) {
        try {
            dialog.dismiss();
        } catch (final IllegalArgumentException e) {
            // Do nothing.
        } catch (final Exception e) {
            // Do nothing.
        } finally {
            dialog = null;
        }
    }
}
