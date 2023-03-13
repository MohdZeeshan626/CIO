package com.singleevent.sdk;

import android.app.Application;
import android.content.Context;


import android.text.TextUtils;
import android.util.Log;


import androidx.multidex.MultiDex;
import io.agora.rtc.Constants;
import io.agora.rtc.RtcEngine;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;
import com.datatheorem.android.trustkit.TrustKit;
import com.google.firebase.FirebaseApp;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.singleevent.sdk.View.RightActivity.admin.model.EventUsersHashMap;
import com.singleevent.sdk.agora.openvcall.model.AGEventHandler;
import com.singleevent.sdk.agora.openvcall.model.CurrentUserSettings;
import com.singleevent.sdk.agora.openvcall.model.EngineConfig;
import com.singleevent.sdk.agora.openvcall.model.MyEngineEventHandler;
import com.twitter.sdk.android.core.Twitter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.KeyStore;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import io.paperdb.Paper;
import io.socket.client.IO;
import io.socket.client.Socket;
import okhttp3.OkHttpClient;
import timber.log.Timber;

/**
 * Created by Admin on 5/22/2017.
 */

public class App extends Application {

    private CurrentUserSettings mVideoSettings = new CurrentUserSettings();

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private RtcEngine mRtcEngine;
    private EngineConfig mConfig;
    private MyEngineEventHandler mEventHandler;
    public static final String TAG = App.class.getSimpleName();

    private static char[] INPUT_STR = ApiList.Confidential2;


    public String contents = "";
    public static OkHttpClient client;
    private RequestQueue mRequestQueue;
    private static App mInstance;

    private Socket mSocket;
    private RequestQueue mRequestQueue1;
    SSLSocketFactory pinnedSSLSocketFactory = null;

    {
        try {
            String url = "https://health1.webmobi.in/socket/con";

            mSocket = IO.socket(ApiList.CHAT_SERVER_URL);
//            mSocket = IO.socket(url);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        
    }


    public RtcEngine rtcEngine() {
        return mRtcEngine;
    }

    public EngineConfig config() {
        return mConfig;
    }

    public CurrentUserSettings userSettings() {
        return mVideoSettings;
    }

    public void addEventHandler(AGEventHandler handler) {
        mEventHandler.addEventHandler(handler);
    }

    public void remoteEventHandler(AGEventHandler handler) {
        mEventHandler.removeEventHandler(handler);
    }
    @Override
    public void onCreate() {
        super.onCreate();
//      Fabric.with(this, new Crashlytics());
        FirebaseApp.initializeApp(this);
        Iconify.with(new FontAwesomeModule());
        createRtcEngine();
        mInstance = this;
        Paper.init(getApplicationContext());
        MultiDex.install(this);
        // OR using a custom resource (TrustKit can't be initialized twice)
      //  TrustKit.initializeWithNetworkSecurityConfiguration(this);
	  
	  //Initializing Trustkit
        /*TrustKit.initializeWithNetworkSecurityConfiguration(this,R.xml.network_security_config);
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } *//*else {
            Timber.plant(new CrashReportingTree());
        }*/

    }
private void createRtcEngine() {
        Context context = getApplicationContext();
        String appId = context.getString(R.string.agora_app_id);
        if (TextUtils.isEmpty(appId)) {
            throw new RuntimeException("NEED TO use your App ID, get your own ID at https://dashboard.agora.io/");
        }

        mEventHandler = new MyEngineEventHandler();
        try {
            mRtcEngine = RtcEngine.create(context, appId, mEventHandler);
        } catch (Exception e) {
            log.error(Log.getStackTraceString(e));
            throw new RuntimeException("NEED TO check rtc sdk init fatal error\n" + Log.getStackTraceString(e));
        }
        mRtcEngine.setChannelProfile(Constants.CHANNEL_PROFILE_LIVE_BROADCASTING);
        mRtcEngine.enableVideo();
        mRtcEngine.enableAudioVolumeIndication(200, 3, false);

        mConfig = new EngineConfig();
    }





    public static synchronized App getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue( getApplicationContext());
        }

        return mRequestQueue;
    }

	//Getting RequestQueue for S3
    public RequestQueue getRequestQueuefors3() {
        if (mRequestQueue1 == null) {
            mRequestQueue1 = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue1;
    }





/*
    private SSLSocketFactory newSslSocketFactory() {
        try {

            URL url = new URL("https://www.webmobi.com");
            String serverHostname = url.getHost();

            KeyStore trusted = KeyStore.getInstance("BKS");

            InputStream in = getApplicationContext().getResources().openRawResource(R.raw.keystore);
            try {
                // Initialize the keystore with the provided trusted certificates
                // Provide the password of the keystore
                trusted.load(in, INPUT_STR);
            } finally {
                in.close();
            }

            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
           // TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(trusted);

            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, tmf.getTrustManagers(), null);

           // SSLSocketFactory sf = context.getSocketFactory();
            SSLSocketFactory sf = TrustKit.getInstance().getSSLSocketFactory(serverHostname);
            return sf;



        } catch (Exception e) {
            throw new AssertionError(e);
        }



    }
*/

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public void cancelall() {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(new RequestQueue.RequestFilter() {
                @Override
                public boolean apply(Request<?> request) {
                    return true;
                }
            });
        }
    }

    public Socket getSocket() {
        return mSocket;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public EventUsersHashMap getEventUsersHashMap() {
        return EventUsersHashMap.getInstance();
    }

    /** A tree which logs important information for crash reporting. */
    private static class CrashReportingTree extends Timber.DebugTree {
        @Override public void i(String message, Object... args) {
            // TODO e.g., Crashlytics.log(String.format(message, args));
        }

        @Override public void i(Throwable t, String message, Object... args) {
            i(message, args); // Just add to the log.
        }

        @Override public void e(String message, Object... args) {
            i("ERROR: " + message, args); // Just add to the log.
        }

        @Override public void e(Throwable t, String message, Object... args) {
            e(message, args);

            // TODO e.g., Crashlytics.logException(t);
        }
    }
public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {}
    }
    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    public void clearApplicationData() {
        File cache = getCacheDir();
        File appDir = new File(getFilesDir() + File.separator + "EventsDownload" + File.separator);
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("lib")) {
                    deleteDir(new File(appDir, s));
                    Log.i("TAG", "File /data/data/APP_PACKAGE/\" + s + \" DELETED");
                }
            }
        }
    }
/*
    public static  OkHttpClient getHttpClient(){
        OkHttpClient client = null;
        try {
            URL url = new URL("https://www.webmobi.com");
            String serverHostname = url.getHost();

            //HttpsUrlConnection
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setSSLSocketFactory(TrustKit.getInstance().getSSLSocketFactory(serverHostname));

            client =
                    new OkHttpClient().newBuilder()
                            .sslSocketFactory(TrustKit.getInstance().getSSLSocketFactory(serverHostname),
                                    TrustKit.getInstance().getTrustManager(serverHostname))
                            .build();


        }catch (Exception e){
            e.printStackTrace();
        }
        return client;
    }
*/

}
