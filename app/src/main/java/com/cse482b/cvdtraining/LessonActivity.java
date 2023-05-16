package com.cse482b.cvdtraining;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
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


public class LessonActivity extends AppCompatActivity implements View.OnClickListener {

    private int contentPage = 0;

    /** UI Elements */
    private ImageView homeButton;
    private Button helpButton;
    private LinearLayout containerLayout;
    private Button backButton;
    private Button nextButton;

    /** Module data */
    List<List<JSONObject>> jsonObjects;



    /**
     * Sets the category of questions that will be drawn from by the PracticeActivity
     * @param category the name of the file in assets/questions without the path and file extension.
     */
    private void setPracticeQuestionCategory(String category) {
        SharedPreferences sharedPref = getSharedPreferences("com.CDV.training.questions", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("questionCategory", category);
        editor.apply();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);

        homeButton = findViewById(R.id.lesson_home_button);
        helpButton = findViewById(R.id.lesson_help_button);
        containerLayout = findViewById(R.id.lesson_layout_view);
        backButton = findViewById(R.id.back_button);
        nextButton = findViewById(R.id.next_button);
        jsonObjects = new ArrayList<>();

        homeButton.setOnClickListener(this);
        helpButton.setOnClickListener(this);
        containerLayout.setOnClickListener(this);
        backButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);

        String moduleName = getIntent().getStringExtra("module_name");
        String[] moduleFragments;
        switch (moduleName) {
            case "Module 1 Name":
                moduleFragments = new String[]{"example1", "example2"};
                setPracticeQuestionCategory("heartbeatSinus");
                break;
            default:
                return;
        }

        for (String filename : moduleFragments)  {
            List<JSONObject> jsonObject = GlobalMethods.parseJSONList(this, filename, "raw");
            jsonObjects.add(jsonObject);
        }
        addBatchToScrollView(containerLayout, jsonObjects.get(0));
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lesson_home_button:
                Intent intent = new Intent(LessonActivity.this, HomeActivity.class);
                startActivity(intent);
                break;
            case R.id.lesson_help_button:
                intent = new Intent(LessonActivity.this, SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.back_button:
                if (contentPage == 0) return;
                if (nextButton.getText() == getResources().getText(R.string.practice) && contentPage == jsonObjects.size() - 1)
                    nextButton.setText(getResources().getText(R.string.next));
                contentPage--;
                containerLayout.removeAllViews();
                addBatchToScrollView(containerLayout, jsonObjects.get(contentPage));
                break;
            case R.id.next_button:
                if (contentPage + 1 >= jsonObjects.size()) {
                    intent = new Intent(LessonActivity.this, PracticeActivity.class);
                    startActivity(intent);
                    return;
                }
                contentPage++;
                containerLayout.removeAllViews();
                addBatchToScrollView(containerLayout, jsonObjects.get(contentPage));
                if (contentPage == jsonObjects.size() - 1)
                    nextButton.setText(getResources().getText(R.string.practice));
                break;
        }
    }

    void addBatchToScrollView(LinearLayout containerLayout, List<JSONObject> contentFragments) {
        for (JSONObject fragment : contentFragments)
            addToScrollView(containerLayout, fragment);
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
