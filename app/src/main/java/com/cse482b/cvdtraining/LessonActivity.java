package com.cse482b.cvdtraining;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class LessonActivity extends AppCompatActivity {

    int contentPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);

        ImageView home = findViewById(R.id.lesson_home_button);
        Button help = findViewById(R.id.lesson_help_button);
        LinearLayout containerLayout = findViewById(R.id.lesson_layout_view);
        Button back = findViewById(R.id.back_button);
        Button next = findViewById(R.id.next_button);

        String moduleName = getIntent().getStringExtra("module_name");
        String[] moduleFragments;
        switch (moduleName) {
            case "Module 1 Name":
                moduleFragments = new String[]{"example1", "example2"};
                break;
            default:
                return;
        }

        List<JSONObject> jsonObjects = GlobalMethods.parseJSONList(this, moduleFragments, "raw");
        addToScrollView(containerLayout, jsonObjects.get(0));

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LessonActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LessonActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (contentPage == 0) return;
                if (next.getText() == getResources().getText(R.string.practice) && contentPage == jsonObjects.size() - 1) {
                    next.setText(getResources().getText(R.string.next));
                }
                contentPage--;
                containerLayout.removeAllViews();
                addToScrollView(containerLayout, jsonObjects.get(contentPage));
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (contentPage + 1 >= jsonObjects.size()) {
                    // practice activity
                    Intent intent = new Intent(LessonActivity.this, SettingsActivity.class);
                    startActivity(intent);
                    return;
                }
                contentPage++;
                containerLayout.removeAllViews();
                addToScrollView(containerLayout, jsonObjects.get(contentPage));
                if (contentPage == jsonObjects.size() - 1) {
                    next.setText(getResources().getText(R.string.practice));

                }
            }
        });
    }

    // moduleFragments: list of JSON filenames (stored in R.raw)
    // order of elements reflect order of display
    List<JSONObject> parseJSONList(List<String> moduleFragments) {
        List<JSONObject> jsonObjects = new ArrayList<>();

        for (String json : moduleFragments) {
            int resourceId = getResources().getIdentifier(json, "raw", getPackageName());
            InputStream inputStream = getResources().openRawResource(resourceId);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder jsonString = new StringBuilder();
            String line;
            try {
                while ((line = bufferedReader.readLine()) != null) {
                    jsonString.append(line);
                }
                bufferedReader.close();
                JSONObject jsonObject = new JSONObject(jsonString.toString());
                jsonObjects.add(jsonObject);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonObjects;
    }

    void addToScrollView(LinearLayout containerLayout, JSONObject contentFragment) {
        try {
            Iterator<String> keysIterator = contentFragment.keys();
            while (keysIterator.hasNext()) {
                String key = keysIterator.next();
                String value = contentFragment.getString(key);

                View view;
                switch (key) {
                    case "text":
                        view = new TextView(this);
                        ((TextView) view).setText(value);
                        ((TextView) view).setTextSize(getResources().getDimension(com.intuit.ssp.R.dimen._12ssp));
                        ((TextView) view).setTextColor(Color.BLACK);
                        break;
                    case "image":
                        view = new ImageView(this);
                        int resourceId = getResources().getIdentifier(value, "drawable", getPackageName());
                        ((ImageView) view).setImageResource(resourceId);
                        ((ImageView) view).setAdjustViewBounds(true);
                        float paddingVertical = getResources().getDimension(com.intuit.ssp.R.dimen._15ssp);
                        view.setPadding(0, (int) paddingVertical, 0, (int) paddingVertical);
                        break;
                    default:
                        return;
                }

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams
                        (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                containerLayout.addView(view, layoutParams);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
