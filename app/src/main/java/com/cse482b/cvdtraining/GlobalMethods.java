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

    /**
     * Sets the category of questions that will be drawn from by the PracticeActivity
     * @param category the name of the file in assets/questions without the path and file extension
     */
    public static void setPracticeQuestionCategory(Context context, String category) {
        SharedPreferences sharedPref = context.getSharedPreferences("com.CDV.training.questions", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("questionCategory", category);
        editor.apply();
    }

    /**
     * Sets the specified preference (key-value pair) on the provided context
     * @param context the context to set the preference on
     * @param key the key to set
     * @param value the value to set
     */
    public static void setPreference(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.apply();
    }

    /**
     * Gets the specified preference (key-value pair) on the provided context
     * @param context the context to get the preference from
     * @param key the key to get the value for
     * @param defaultValue the value to return if the key is not found
     * @return the value associated with the key or defaultValue if key is not found
     */
    public static String getPreference(Context context, String key, String defaultValue) {
        SharedPreferences sp = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        return sp.getString(key, defaultValue);
    }

    /**
     * Gets all the keys available in the given context
     * @param context the context to find keys in
     * @return a set of keys available in the context
     */
    public static Set<String> getPreferencesKeys(Context context) {
        SharedPreferences sp = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        Map<String, ?> allEntries = sp.getAll();
        Set<String> keys = allEntries.keySet();
        return keys;
    }

    /**
     * Parses JSON that looks like a list of objects
     * @param context the context to find the json file within
     * @param json the filename (without extension) of the json file to parse
     * @param defType the default resource type to find (e.g. "raw" since that's where we store the json)
     * @return a list of the parsed objects. Has some limitations regarding newlines, so avoid unnecessary newlines within objects.
     */
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
