package com.cse482b.cvdtraining;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AlignmentSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class HomeActivity extends AppCompatActivity {

    boolean newUser = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        newUser = getIntent().getBooleanExtra("newUser", true);
        System.out.println(newUser);
        if (!newUser) {
            String text = "Welcome back, " + GlobalMethods.getPreference(this, "name", "") + "!";
            Spannable centeredText = new SpannableString(text);
            centeredText.setSpan(
                    new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                    0, text.length() - 1,
                    Spannable.SPAN_INCLUSIVE_INCLUSIVE
            );
            Toast.makeText(getApplicationContext(), centeredText, Toast.LENGTH_LONG).show();
        }

        setContentView(R.layout.activity_home);

        ListView listView = findViewById(R.id.module_list);
        Button settings = findViewById(R.id.help_button);
        List<JSONObject> moduleJSONs = GlobalMethods.parseJSONList(this, "modules", "raw");
        List<String> itemList = new ArrayList<>(moduleJSONs.size() / 4);
        List<String> activityList = new ArrayList<>(moduleJSONs.size() / 4);
        List<String> fragmentList = new ArrayList<>(moduleJSONs.size() / 4);
        List<String> questionCategoryList = new ArrayList<>(moduleJSONs.size() / 4);
        Iterator<JSONObject> it = moduleJSONs.iterator();
        try {
            while (it.hasNext()) {
                itemList.add(it.next().getString("name"));
                fragmentList.add(it.next().getString("fragments"));
                questionCategoryList.add(it.next().getString("questions"));
                activityList.add(it.next().getString("activity"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ListAdapter adapter = new ListAdapter(this, itemList, activityList, fragmentList, questionCategoryList);
        listView.setAdapter(adapter);

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
                startActivity(intent);
                // for if user decides to reset
                finish();
            }
        });
    }
}
