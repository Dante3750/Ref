package com.globals.netconnect.netconnect.app;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;


/**
 * Created by john on 7/28/2017.
 */

public class AppController extends Application {
    public static final String TAG = AppController.class.getSimpleName();
    private RequestQueue mRequestQueue;
    private static AppController mInstance;
    private SharedPrefManager pref;

    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized AppController getInstance() {
        if (mInstance == null) {
            mInstance = new AppController();
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null)
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> reg, String tag) {
        reg.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(reg);
    }

    public <T> void addToRequestQueue(Request<T> reg) {
        reg.setTag(TAG);
        getRequestQueue().add(reg);
    }

    public SharedPrefManager getPref() {
        if (pref == null) {
            pref = new SharedPrefManager(this);
        }
        return pref;
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}

