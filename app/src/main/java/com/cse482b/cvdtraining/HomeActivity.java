package com.cse482b.cvdtraining;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.List;


public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ListView listView = findViewById(R.id.moduleList);
        Button settings = findViewById(R.id.settingsButton);
        List<String> itemList = Arrays.asList(
                "Module 1 Name",
                "Module 2 Name",
                "Module 3 Name",
                "Module 4 Name"
        );

        List<String> activityList = Arrays.asList(
                "",
                "",
                "",
                ""
        );

        ListAdapter adapter = new ListAdapter(this, itemList, activityList, true);
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
