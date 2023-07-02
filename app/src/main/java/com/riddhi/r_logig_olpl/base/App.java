package com.riddhi.r_logig_olpl.base;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.BuildConfig;

public class App extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(App.this);
        }

    }

    public static Context getContext() {
        return context;
    }

}
