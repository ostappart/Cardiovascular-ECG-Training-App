package com.cse482b.cvdtraining;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;
import java.util.Set;

public class GlobalMethods {
    public static void setPreference(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getPreference(Context context, String key, String defaultValue) {
        SharedPreferences sp = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        return sp.getString(key, defaultValue);
    }

    public static Set<String> getPreferencesKeys(Context context) {
        SharedPreferences sp = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        Map<String, ?> allEntries = sp.getAll();
        Set<String> keys = allEntries.keySet();
        return keys;
    }
}
