package com.example.trackline24;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

public class Sesion {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public Sesion(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("user_session", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveUserData(JSONObject userData) {
        try {
            editor.putString("user_id", userData.getString("idUser"));
            editor.putString("user_name", userData.getString("userName"));
            editor.putString("user_email", userData.getString("email"));
            editor.apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public boolean isLoggedIn() {
        return sharedPreferences.contains("user_id");
    }

    public void clearSession() {
        editor.clear();
        editor.apply();
    }
}
