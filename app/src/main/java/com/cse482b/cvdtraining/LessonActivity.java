package com.cse482b.cvdtraining;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.chrisbanes.photoview.PhotoView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * LessonActivity takes care of displaying the module screen.
 */
public class LessonActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * Keeps track of which page/slide of the module we are on.
     */
    private int contentPage = 0;

    /* UI Elements */
    private LinearLayout containerLayout;
    private Button nextButton;

    /** Module data */
    List<List<JSONObject>> jsonObjects;
    private String moduleName;
    private String moduleNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);

        /* UI Elements */
        ImageView homeButton = findViewById(R.id.lesson_home_button);
        TextView lessonTitle = findViewById(R.id.lessons_title);
        Button helpButton = findViewById(R.id.lesson_help_button);
        containerLayout = findViewById(R.id.lesson_layout_view);
        Button backButton = findViewById(R.id.back_button);
        nextButton = findViewById(R.id.next_button);
        jsonObjects = new ArrayList<>();

        homeButton.setOnClickListener(this);
        helpButton.setOnClickListener(this);
        containerLayout.setOnClickListener(this);
        backButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);

        // Load module data
        moduleName = getIntent().getStringExtra("module_name");
        moduleNext = getIntent().getStringExtra("module_unlock");
        String[] moduleFragments = getIntent().getStringExtra("module_fragments").split("\\+");

        lessonTitle.setText(moduleName);

        for (String filename : moduleFragments)  {
            List<JSONObject> jsonObject = GlobalMethods.parseJSONList(this, filename, "raw");
            jsonObjects.add(jsonObject);
        }

        // Add everything to the scroll view
        addBatchToScrollView(containerLayout, jsonObjects.get(savedInstanceState != null ?
                savedInstanceState.getInt("contentPage") : contentPage));
    }

    @Override
    protected void onSaveInstanceState (@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("contentPage", contentPage);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        contentPage = savedInstanceState.getInt("contentPage");
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
                if (contentPage == 0) {
                    Intent home = new Intent(LessonActivity.this, HomeActivity.class);
                    startActivity(home);
                    return;
                }
                if (nextButton.getText() == getResources().getText(R.string.practice) && contentPage == jsonObjects.size() - 1)
                    nextButton.setText(getResources().getText(R.string.next));
                contentPage--;
                containerLayout.removeAllViews();
                addBatchToScrollView(containerLayout, jsonObjects.get(contentPage));
                break;
            case R.id.next_button:
                if (contentPage + 1 >= jsonObjects.size()) {
                    intent = new Intent(LessonActivity.this, PracticeActivity.class);
                    intent.putExtra("module_name", moduleName + " Practice");
                    startActivity(intent);
                    return;
                }
                contentPage++;
                containerLayout.removeAllViews();
                addBatchToScrollView(containerLayout, jsonObjects.get(contentPage));
                if (contentPage == jsonObjects.size() - 1) {
                    nextButton.setText(getResources().getText(R.string.practice));
                    GlobalMethods.setPreference(this, moduleName + "-completion", "COMPLETED");
                    if (!GlobalMethods.getPreference(this, moduleNext + "-completion", "").equals("COMPLETED"))
                        GlobalMethods.setPreference(this, moduleNext + "-completion", "UNLOCKED");
                }
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
                        // PhotoView enables zooming
                        view = new PhotoView(this);
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
