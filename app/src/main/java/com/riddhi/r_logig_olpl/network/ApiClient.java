package com.riddhi.r_logig_olpl.network;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    //sgtpl below
    //private static final String API_URL = "http://14.143.152.191/";
    //private static final String API_URL = "http://220.226.105.107/";
   // private static final String API_URL = "http://103.87.173.198/";
   // private static final String API_URL = "http://192.168.0.73/";
    private static final String API_URL = "http://220.226.105.104/";
    private static Retrofit retrofit = null;
    private static NetworkService networkService = null;

    private ApiClient() {
    }

    public static NetworkService getNetworkService() {
        if (networkService == null) {
            networkService = getClient().create(NetworkService.class);
        }
        return networkService;
    }

    public static String GetBaseUrl() {
        return API_URL;
        //return "http://192.168.0.73/" + sharedPreferenceData.getPref("CCode") + "/";
    }

    public static Retrofit getClient() {
        if (retrofit == null) {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addNetworkInterceptor(new StethoInterceptor())
                    .connectTimeout(180, TimeUnit.SECONDS)
                    .readTimeout(180, TimeUnit.SECONDS)
                    .writeTimeout(180, TimeUnit.SECONDS).build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(API_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(okHttpClient)
                    .build();
        }
        return retrofit;
    }
}
