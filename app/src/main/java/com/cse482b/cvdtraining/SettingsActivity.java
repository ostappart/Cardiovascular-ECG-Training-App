package com.cse482b.cvdtraining;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.List;


public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ListView topListView = findViewById(R.id.settings_top_list);
        ListView bottomListView = findViewById(R.id.settings_bottom_list);
        Button settings = findViewById(R.id.home_button);

        List<String> topItemList = Arrays.asList(
                "Dictionary",
                "Example",
                "Example"
        );
        List<String> topActivityList = Arrays.asList(
                "",
                "",
                ""
        );
        List<String> bottomItemList = Arrays.asList(
                "Reset All Lessons",
                "Example",
                "Example",
                "Example"
        );
        List<String> bottomActivityList = Arrays.asList(
                "ResetModuleDialogActivity",
                "",
                "",
                ""
        );

        ListAdapter topAdapter = new ListAdapter(this, topItemList, topActivityList, false);
        topListView.setAdapter(topAdapter);

        ListAdapter bottomAdapter = new ListAdapter(this, bottomItemList, bottomActivityList, false);
        bottomListView.setAdapter(bottomAdapter);

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }

}
