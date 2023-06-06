package com.example.medenjaci.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

public class SessionManager {

    SharedPreferences userSession;
    SharedPreferences.Editor editor;
    Context context;

    private static final String IS_LOGIN = "IsLoggedIn";

    public static final String KEY_USERNAME = "username";
    public static final String KEY_FIRSTNAME = "firstName";
    public static final String KEY_LASTNAME = "lastNem";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_TYPE = "type";

    public SessionManager(Context _context) {
        context = _context;
        userSession = context.getSharedPreferences("userLoginSession", Context.MODE_PRIVATE);
        editor = userSession.edit();
    }

    public void createLoginSession(String username, String firstName, String lastName, String password, String address, String phone, String type) {

        editor.putBoolean(IS_LOGIN, true);

        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_FIRSTNAME, firstName);
        editor.putString(KEY_LASTNAME, lastName);
        editor.putString(KEY_PASSWORD, password);
        editor.putString(KEY_ADDRESS, address);
        editor.putString(KEY_PHONE, phone);
        editor.putString(KEY_TYPE, type);

        editor.commit();
    }

    public void change_info(HashMap<String, String> hashMap) {

        for (Map.Entry<String, String> entry : hashMap.entrySet()) {
            editor.putString(entry.getKey(), entry.getValue());
        }

        editor.commit();
    }

    public HashMap<String, String> getUsersDetailFromSession() {
        HashMap<String, String> userData = new HashMap<String, String>();

        userData.put(KEY_USERNAME, userSession.getString(KEY_USERNAME, null));
        userData.put(KEY_FIRSTNAME, userSession.getString(KEY_FIRSTNAME, null));
        userData.put(KEY_LASTNAME, userSession.getString(KEY_LASTNAME, null));
        userData.put(KEY_PASSWORD, userSession.getString(KEY_PASSWORD, null));
        userData.put(KEY_PHONE, userSession.getString(KEY_PHONE, null));
        userData.put(KEY_ADDRESS, userSession.getString(KEY_ADDRESS, null));
        userData.put(KEY_TYPE, userSession.getString(KEY_TYPE, null));

        return userData;
    }

    public boolean checkLogin() {
        if (userSession.getBoolean(IS_LOGIN, false)) {
            return true;
        } else {
            return false;
        }
    }

    public void logout() {
        editor.clear();
        editor.commit();
    }
}
