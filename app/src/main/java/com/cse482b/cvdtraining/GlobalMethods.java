package com.cse482b.cvdtraining;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GlobalMethods extends AppCompatActivity {

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

    public static List<JSONObject> parseJSONList(Context context, String json, String defType) {
        List<JSONObject> jsonObjects = new ArrayList<>();

            int resourceId = context.getResources().getIdentifier(json, defType, context.getPackageName());
            InputStream inputStream = context.getResources().openRawResource(resourceId);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder jsonString = new StringBuilder();
            String line;
            try {
                while ((line = bufferedReader.readLine()) != null) {
                    jsonString.append(line);
                }
                bufferedReader.close();

                char firstChar = jsonString.charAt(0);
                if (firstChar == '{') {
                    JSONObject jsonObject = new JSONObject(jsonString.toString());
                    jsonObjects.add(jsonObject);
                } else if (firstChar == '[') {
                    JSONArray jsonArray = new JSONArray(jsonString.toString());
                    // Iterate over the JSON array and add each JSON object to the list
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        jsonObjects.add(jsonObject);
                    }
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        return jsonObjects;
    }
}
