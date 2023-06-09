package com.cse482b.cvdtraining;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.List;

/**
 * SettingsActivity Presents a quick menu of various screens like the dictionary or stats page.
 */
public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        ListView helpList = findViewById(R.id.help_list);
        ImageView settings = findViewById(R.id.home_button);

        List<String> itemList = Arrays.asList(
                "Dictionary",
                "Stats",
                "Reset",
                "About"
        );
        List<String> activityList = Arrays.asList(
                "DictionaryActivity",
                "StatsActivity",
                "ResetModuleDialogActivity",
                "AboutDialogActivity"
        );

        ListAdapter topAdapter = new ListAdapter(this, itemList, activityList);
        helpList.setAdapter(topAdapter);

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }

}
