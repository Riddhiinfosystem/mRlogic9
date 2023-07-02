package com.riddhi.r_logig_olpl.util;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.riddhi.r_logig_olpl.base.App;
import com.riddhi.r_logig_olpl.login.LoginModel;

public class PreferenceUtil {
    private static SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(App.getContext());

    private static LoginModel loginModel = null;


    public static boolean isUserLoggedIn() {
        return preferences.getBoolean("is_logged_in", false);
    }

    public static void setUserLoggedIn(boolean isLoggedIn) {
        preferences.edit().putBoolean("is_logged_in", isLoggedIn).apply();
    }

    public static LoginModel getUser() {
        if (loginModel == null) {
            loginModel = new Gson().fromJson(preferences.getString("user", null), LoginModel.class);
        }
        return loginModel;
    }

    public static void clearUserData() {
        if (loginModel != null) {
            loginModel = null;
        }
    }

    public static void setUser(LoginModel loginModel) {
        preferences.edit().putString("user", new Gson().toJson(loginModel)).apply();
    }

    public static void clearAll() {
        loginModel = null;
        preferences.edit().clear().apply();
    }

    public static void clear() {
        loginModel = null;
    }
}
